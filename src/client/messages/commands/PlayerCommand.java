package client.messages.commands;

//import client.MapleInventory;
//import client.MapleInventoryType;
import client.inventory.Item;
import server.RankingWorker;
import client.MapleCharacter;
import constants.ServerConstants.PlayerGMRank;
import client.MapleClient;
import client.MapleStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.MapleInventoryType;
import client.messages.commands.CommandExecute.PokemonExecute;
import client.messages.commands.CommandExecute.TradeExecute;
import constants.BattleConstants.PokemonItem;
import constants.GameConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.NPCScriptManager;
import server.ItemInformation;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.PokemonBattle;
import server.RankingWorker.RankingInformation;
import server.life.MapleMonster;


import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.SavedLocationType;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;
import tools.packet.CWvsContext;

/**
 *
 * @author Emilyx3
 */
public class PlayerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.NORMAL;
    }

    public static class STR extends DistributeStatCommands {

        public STR() {
            stat = MapleStat.STR;
        }
    }

    public static class DEX extends DistributeStatCommands {

        public DEX() {
            stat = MapleStat.DEX;
        }
    }

    public static class INT extends DistributeStatCommands {

        public INT() {
            stat = MapleStat.INT;
        }
    }

    public static class LUK extends DistributeStatCommands {

        public LUK() {
            stat = MapleStat.LUK;
        }
    }

    public abstract static class DistributeStatCommands extends CommandExecute {

        protected MapleStat stat = null;
        private static int statLim = 999;

        private void setStat(MapleCharacter player, int amount) {
            switch (stat) {
                case STR:
                    player.getStat().setStr((short) amount, player);
                    player.updateSingleStat(MapleStat.STR, player.getStat().getStr());
                    break;
                case DEX:
                    player.getStat().setDex((short) amount, player);
                    player.updateSingleStat(MapleStat.DEX, player.getStat().getDex());
                    break;
                case INT:
                    player.getStat().setInt((short) amount, player);
                    player.updateSingleStat(MapleStat.INT, player.getStat().getInt());
                    break;
                case LUK:
                    player.getStat().setLuk((short) amount, player);
                    player.updateSingleStat(MapleStat.LUK, player.getStat().getLuk());
                    break;
            }
        }

        private int getStat(MapleCharacter player) {
            switch (stat) {
                case STR:
                    return player.getStat().getStr();
                case DEX:
                    return player.getStat().getDex();
                case INT:
                    return player.getStat().getInt();
                case LUK:
                    return player.getStat().getLuk();
                default:
                    throw new RuntimeException(); //Will never happen.
            }
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(5, "Invalid number entered.");
                return 0;
            }
            int change = 0;
            try {
                change = Integer.parseInt(splitted[1]);
            } catch (NumberFormatException nfe) {
                c.getPlayer().dropMessage(5, "Invalid number entered.");
                return 0;
            }
            if (change <= 0) {
                c.getPlayer().dropMessage(5, "You must enter a number greater than 0.");
                return 0;
            }
            if (c.getPlayer().getRemainingAp() < change) {
                c.getPlayer().dropMessage(5, "You don't have enough AP for that.");
                return 0;
            }
            if (getStat(c.getPlayer()) + change > statLim) {
                c.getPlayer().dropMessage(5, "The stat limit is " + statLim + ".");
                return 0;
            }
            setStat(c.getPlayer(), getStat(c.getPlayer()) + change);
            c.getPlayer().setRemainingAp((short) (c.getPlayer().getRemainingAp() - change));
            c.getPlayer().updateSingleStat(MapleStat.AVAILABLEAP, c.getPlayer().getRemainingAp());
            c.getPlayer().dropMessage(5, StringUtil.makeEnumHumanReadable(stat.name()) + " has been raised by " + change + ".");
            return 1;
        }
    }

    public static class Mob extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            MapleMonster mob = null;
            for (final MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                if (mob.isAlive()) {
                    c.getPlayer().dropMessage(6, "Monster " + mob.toString());
                    break; //only one
                }
            }
            if (mob == null) {
                c.getPlayer().dropMessage(6, "No monster was found.");
            }
            return 1;
        }
    }

    public static class Challenge extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length <= 1) {
                c.getPlayer().dropMessage(6, "@challenge [playername OR accept/decline OR block/unblock]");
                return 0;
            }
            if (c.getPlayer().getBattler(0) == null) {
                c.getPlayer().dropMessage(6, "You have no monsters!");
                return 0;
            }
            if (splitted[1].equalsIgnoreCase("accept")) {
                if (c.getPlayer().getChallenge() > 0) {
                    final MapleCharacter chr = c.getPlayer().getMap().getCharacterById(c.getPlayer().getChallenge());
                    if (chr != null) {
                        if ((c.getPlayer().isInTownMap() || c.getPlayer().isGM() || chr.isInTownMap() || chr.isGM()) && chr.getBattler(0) != null && chr.getChallenge() == c.getPlayer().getId() && chr.getBattle() == null && c.getPlayer().getBattle() == null) {
                            if (c.getPlayer().getPosition().y != chr.getPosition().y) {
                                c.getPlayer().dropMessage(6, "Please be near them.");
                                return 0;
                            } else if (c.getPlayer().getPosition().distance(chr.getPosition()) > 600.0 || c.getPlayer().getPosition().distance(chr.getPosition()) < 400.0) {
                                c.getPlayer().dropMessage(6, "Please be at a moderate distance from them.");
                                return 0;
                            }
                            chr.setChallenge(0);
                            chr.dropMessage(6, c.getPlayer().getName() + " has accepted!");
                            c.getPlayer().setChallenge(0);
                            final PokemonBattle battle = new PokemonBattle(chr, c.getPlayer());
                            chr.setBattle(battle);
                            c.getPlayer().setBattle(battle);
                            battle.initiate();
                        } else {
                            c.getPlayer().dropMessage(6, "You may only use it in towns, or the other character has no monsters, or something failed.");
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "They do not exist in the map.");
                    }
                } else {
                    c.getPlayer().dropMessage(6, "You don't have a challenge.");
                }
            } else if (splitted[1].equalsIgnoreCase("decline")) {
                if (c.getPlayer().getChallenge() > 0) {
                    c.getPlayer().cancelChallenge();
                } else {
                    c.getPlayer().dropMessage(6, "You don't have a challenge.");
                }
            } else if (splitted[1].equalsIgnoreCase("block")) {
                if (c.getPlayer().getChallenge() == 0) {
                    c.getPlayer().setChallenge(-1);
                    c.getPlayer().dropMessage(6, "You have blocked challenges.");
                } else {
                    c.getPlayer().dropMessage(6, "You have a challenge or they are already blocked.");
                }
            } else if (splitted[1].equalsIgnoreCase("unblock")) {
                if (c.getPlayer().getChallenge() < 0) {
                    c.getPlayer().setChallenge(0);
                    c.getPlayer().dropMessage(6, "You have unblocked challenges.");
                } else {
                    c.getPlayer().dropMessage(6, "You didn't block challenges.");
                }
            } else {
                if (c.getPlayer().getChallenge() == 0) {
                    final MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
                    if (chr != null && chr.getMap() == c.getPlayer().getMap() && chr.getId() != c.getPlayer().getId()) {
                        if ((c.getPlayer().isInTownMap() || c.getPlayer().isGM() || chr.isInTownMap() || chr.isGM()) && chr.getBattler(0) != null && chr.getChallenge() == 0 && chr.getBattle() == null && c.getPlayer().getBattle() == null) {
                            chr.setChallenge(c.getPlayer().getId());
                            chr.dropMessage(6, c.getPlayer().getName() + " has challenged you! Type @challenge [accept/decline] to answer!");
                            c.getPlayer().setChallenge(chr.getId());
                            c.getPlayer().dropMessage(6, "Successfully sent the request.");
                        } else {
                            c.getPlayer().dropMessage(6, "You may only use it in towns, or the other character has no monsters, or they have a challenge.");
                        }
                    } else {
                        c.getPlayer().dropMessage(6, splitted[1] + " does not exist in the map.");
                    }
                } else {
                    c.getPlayer().dropMessage(6, "You have a challenge or you have blocked them.");
                }
            }
            return 1;
        }
    }

    
    
    
    
    public static class search extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length == 1) {
                c.getPlayer().dropMessage(6, splitted[0] + ": <ITEM>");
            } else if (splitted.length == 2) {
                c.getPlayer().dropMessage(6, "Provide something to search.");
            } else {
                String type = splitted[1];
                String search = StringUtil.joinStringFrom(splitted, 2);
                MapleData data = null;
                MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/" + "String.wz"));
                c.getPlayer().dropMessage(6, "<<Type: " + type + " | Search: " + search + ">>");
                 if (type.equalsIgnoreCase("ITEM")) {
                    List<String> retItems = new ArrayList<String>();
                    for (ItemInformation itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
                        if (itemPair != null && itemPair.name != null && itemPair.name.toLowerCase().contains(search.toLowerCase())) {
                            retItems.add(itemPair.itemId + " - " + itemPair.name);
                        }
                    }
                    if (retItems != null && retItems.size() > 0) {
                        for (String singleRetItem : retItems) {
                            c.getPlayer().dropMessage(6, singleRetItem);
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "No Items Found.");
                    }
                
                } else {
                    c.getPlayer().dropMessage(6, "Sorry, that search call is unavailable.");
                }
            }
            return 0;
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public abstract static class OpenNPCCommand extends CommandExecute {

        protected int npc = -1;
        private static int[] npcs = { //Ish yur job to make sure these are in order and correct ;(
            9270035,
            9010018,
            9000000,
            9000030,
            9010000,
            9000085,
            9000018,
            9201094};

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (npc != 6 && npc != 5 && npc != 4 && npc != 3 && npc != 1 && c.getPlayer().getMapId() != 910000000) { //drpcash can use anywhere
                if (c.getPlayer().getLevel() < 10 && c.getPlayer().getJob() != 200) {
                    c.getPlayer().dropMessage(5, "You must be over level 10 to use this command.");
                    return 0;
                }
                if (c.getPlayer().isInBlockedMap()) {
                    c.getPlayer().dropMessage(5, "You may not use this command here.");
                    return 0;
                }
            } else if (npc == 1) {
                if (c.getPlayer().getLevel() < 70) {
                    c.getPlayer().dropMessage(5, "You must be over level 70 to use this command.");
                    return 0;
                }
            }
            if (c.getPlayer().hasBlockedInventory()) {
                c.getPlayer().dropMessage(5, "You may not use this command here.");
                return 0;
            }
            NPCScriptManager.getInstance().start(c, npcs[npc]);
            return 1;
        }
    }

    public static class npc extends OpenNPCCommand {

        public npc() {
            npc = 0;
        }
    }

    public static class DCash extends OpenNPCCommand {

        public DCash() {
            npc = 1;
        }
    }

    public static class Event extends OpenNPCCommand {

        public Event() {
            npc = 2;
        }
    }

    public static class CheckDrop extends OpenNPCCommand {

        public CheckDrop() {
            npc = 4;
        }
    }

    public static class Pokedex extends OpenNPCCommand {

        public Pokedex() {
            npc = 5;
        }
    }

    public static class Pokemon extends OpenNPCCommand {

        public Pokemon() {
            npc = 6;
        }
    }
    
    public static class freesmega extends OpenNPCCommand {

        public freesmega() {
            npc = 7;
        }
    }

    /*public static class ClearSlot extends CommandExecute {
    
    private static MapleInventoryType[] invs = {
    MapleInventoryType.EQUIP,
    MapleInventoryType.USE,
    MapleInventoryType.SETUP,
    MapleInventoryType.ETC,
    MapleInventoryType.CASH,};
    
    @Override
    public int execute(MapleClient c, String[] splitted) {
    MapleCharacter player = c.getPlayer();
    if (splitted.length < 2 || player.hasBlockedInventory()) {
    c.getPlayer().dropMessage(5, "@clearslot <eq/use/setup/etc/cash/all>");
    return 0;
    } else {
    MapleInventoryType type;
    if (splitted[1].equalsIgnoreCase("eq")) {
    type = MapleInventoryType.EQUIP;
    } else if (splitted[1].equalsIgnoreCase("use")) {
    type = MapleInventoryType.USE;
    } else if (splitted[1].equalsIgnoreCase("setup")) {
    type = MapleInventoryType.SETUP;
    } else if (splitted[1].equalsIgnoreCase("etc")) {
    type = MapleInventoryType.ETC;
    } else if (splitted[1].equalsIgnoreCase("cash")) {
    type = MapleInventoryType.CASH;
    } else if (splitted[1].equalsIgnoreCase("all")) {
    type = null;
    } else {
    c.getPlayer().dropMessage(5, "Invalid. @clearslot <eq/use/setup/etc/cash/all>");
    return 0;
    }
    if (type == null) { //All, a bit hacky, but it's okay
    for (MapleInventoryType t : invs) {
    type = t;
    MapleInventory inv = c.getPlayer().getInventory(type);
    byte start = -1;
    for (byte i = 0; i < inv.getSlotLimit(); i++) {
    if (inv.getItem(i) != null) {
    start = i;
    break;
    }
    }
    if (start == -1) {
    c.getPlayer().dropMessage(5, "There are no items in that inventory.");
    return 0;
    }
    int end = 0;
    for (byte i = start; i < inv.getSlotLimit(); i++) {
    if (inv.getItem(i) != null) {
    MapleInventoryManipulator.removeFromSlot(c, type, i, inv.getItem(i).getQuantity(), true);
    } else {
    end = i;
    break;//Break at first empty space.
    }
    }
    c.getPlayer().dropMessage(5, "Cleared slots " + start + " to " + end + ".");
    }
    } else {
    MapleInventory inv = c.getPlayer().getInventory(type);
    byte start = -1;
    for (byte i = 0; i < inv.getSlotLimit(); i++) {
    if (inv.getItem(i) != null) {
    start = i;
    break;
    }
    }
    if (start == -1) {
    c.getPlayer().dropMessage(5, "There are no items in that inventory.");
    return 0;
    }
    byte end = 0;
    for (byte i = start; i < inv.getSlotLimit(); i++) {
    if (inv.getItem(i) != null) {
    MapleInventoryManipulator.removeFromSlot(c, type, i, inv.getItem(i).getQuantity(), true);
    } else {
    end = i;
    break;//Break at first empty space.
    }
    }
    c.getPlayer().dropMessage(5, "Cleared slots " + start + " to " + end + ".");
    }
    return 1;
    }
    }
    }*/
    public static class FM extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            for (int i : GameConstants.blockedMaps) {
                if (c.getPlayer().getMapId() == i) {
                    c.getPlayer().dropMessage(5, "You may not use this command here.");
                    return 0;
                }
            }
            if (c.getPlayer().getLevel() < 10 && c.getPlayer().getJob() != 200) {
                c.getPlayer().dropMessage(5, "You must be over level 10 to use this command.");
                return 0;
            }
            if (c.getPlayer().hasBlockedInventory() || c.getPlayer().getMap().getSquadByMap() != null || c.getPlayer().getEventInstance() != null || c.getPlayer().getMap().getEMByMap() != null || c.getPlayer().getMapId() >= 990000000/* || FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())*/) {
                c.getPlayer().dropMessage(5, "You may not use this command here.");
                return 0;
            }
            if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || (c.getPlayer().getMapId() / 100 == 1030008) || (c.getPlayer().getMapId() / 100 == 922010) || (c.getPlayer().getMapId() / 10 == 13003000)) {
                c.getPlayer().dropMessage(5, "You may not use this command here.");
                return 0;
            }
            c.getPlayer().saveLocation(SavedLocationType.FREE_MARKET, c.getPlayer().getMap().getReturnMap().getId());
            MapleMap map = c.getChannelServer().getMapFactory().getMap(910000000);
            c.getPlayer().changeMap(map, map.getPortal(0));
            return 1;
        }
    }

    public static class EA extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.removeClickedNPC();
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(CWvsContext.enableActions());
            return 1;
        }
    }
    
    public static class dispose extends CommandExecute
  {
    public int execute(MapleClient c, String[] splitted)
    {
      c.removeClickedNPC();
      NPCScriptManager.getInstance().dispose(c);
      c.getSession().write(CWvsContext.enableActions());
      c.getPlayer().dropMessage(6, "You have been disposed.");
      return 1;
    }
  }

    public static class TSmega extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setSmega();
            return 1;
        }
    }

    public static class Ranking extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 4) { //job start end
                c.getPlayer().dropMessage(5, "Use @ranking [job] [start number] [end number] where start and end are ranks of the players");
                final StringBuilder builder = new StringBuilder("JOBS: ");
                for (String b : RankingWorker.getJobCommands().keySet()) {
                    builder.append(b);
                    builder.append(" ");
                }
                c.getPlayer().dropMessage(5, builder.toString());
            } else {
                int start = 1, end = 20;
                try {
                    start = Integer.parseInt(splitted[2]);
                    end = Integer.parseInt(splitted[3]);
                } catch (NumberFormatException e) {
                    c.getPlayer().dropMessage(5, "You didn't specify start and end number correctly, the default values of 1 and 20 will be used.");
                }
                if (end < start || end - start > 20) {
                    c.getPlayer().dropMessage(5, "End number must be greater, and end number must be within a range of 20 from the start number.");
                } else {
                    final Integer job = RankingWorker.getJobCommand(splitted[1]);
                    if (job == null) {
                        c.getPlayer().dropMessage(5, "Please use @ranking to check the job names.");
                    } else {
                        final List<RankingInformation> ranks = RankingWorker.getRankingInfo(job.intValue());
                        if (ranks == null || ranks.size() <= 0) {
                            c.getPlayer().dropMessage(5, "Please try again later.");
                        } else {
                            int num = 0;
                            for (RankingInformation rank : ranks) {
                                if (rank.rank >= start && rank.rank <= end) {
                                    if (num == 0) {
                                        c.getPlayer().dropMessage(6, "Rankings for " + splitted[1] + " - from " + start + " to " + end);
                                        c.getPlayer().dropMessage(6, "--------------------------------------");
                                    }
                                    c.getPlayer().dropMessage(6, rank.toString());
                                    num++;
                                }
                            }
                            if (num == 0) {
                                c.getPlayer().dropMessage(5, "No ranking was returned.");
                            }
                        }
                    }
                }
            }
            return 1;
        }
    }

    public static class Check extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, "You currently have " + c.getPlayer().getCSPoints(1) + " Cash.");
            c.getPlayer().dropMessage(6, "You currently have " + c.getPlayer().getPoints() + " donation points.");
            c.getPlayer().dropMessage(6, "You currently have " + c.getPlayer().getVPoints() + " voting points.");
            c.getPlayer().dropMessage(6, "You currently have " + c.getPlayer().getIntNoRecord(GameConstants.BOSS_PQ) + " Boss Party Quest points.");
            c.getPlayer().dropMessage(6, "The time is currently " + FileoutputUtil.CurrentReadable_TimeGMT() + " GMT.");
            return 1;
        }
    }

    public static class Help extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "@str, @dex, @int, @luk <amount to add>");
            c.getPlayer().dropMessage(5, "@mob < Information on the closest monster >");
            c.getPlayer().dropMessage(5, "@check < Displays various information >");
            c.getPlayer().dropMessage(5, "@fm < Warp to FM >");
            c.getPlayer().dropMessage(5, "@search item < You may search up an item. >");
            /*c.getPlayer().dropMessage(5, "@changesecondpass - Change second password, @changesecondpass <current Password> <new password> <Confirm new password> ");*/
            c.getPlayer().dropMessage(5, "@npc < Universal Town Warp / Event NPC >");
            c.getPlayer().dropMessage(5, "@freesmega < Free Smega NPC / Pot Scroll Seller >");
            c.getPlayer().dropMessage(5, "@dcash < Universal Cash Item Dropper >");
            /*if (!GameConstants.GMS) {
            c.getPlayer().dropMessage(5, "@pokedex < Universal Information >");
            c.getPlayer().dropMessage(5, "@pokemon < Universal Monsters Information >");
            c.getPlayer().dropMessage(5, "@challenge < playername, or accept/decline or block/unblock >");
            }*/
            c.getPlayer().dropMessage(5, "@tsmega < Toggle super megaphone on/off >");
            c.getPlayer().dropMessage(5, "@ea < If you are unable to attack or talk to NPC >");
            /*c.getPlayer().dropMessage(5, "@clearslot < Cleanup that trash in your inventory >");*/
            c.getPlayer().dropMessage(5, "@ranking < Use @ranking for more details >");
            c.getPlayer().dropMessage(5, "@checkdrop < Use @checkdrop for more details >");
            return 1;
        }
    }

    public static class TradeHelp extends TradeExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(-2, "[System] : <@offerequip, @offeruse, @offersetup, @offeretc, @offercash> <quantity> <name of the item>");
            return 1;
        }
    }

    public abstract static class OfferCommand extends TradeExecute {

        protected int invType = -1;

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(-2, "[Error] : <quantity> <name of item>");
            } else if (c.getPlayer().getLevel() < 70) {
                c.getPlayer().dropMessage(-2, "[Error] : Only level 70+ may use this command");
            } else {
                int quantity = 1;
                try {
                    quantity = Integer.parseInt(splitted[1]);
                } catch (Exception e) { //swallow and just use 1
                }
                String search = StringUtil.joinStringFrom(splitted, 2).toLowerCase();
                Item found = null;
                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                for (Item inv : c.getPlayer().getInventory(MapleInventoryType.getByType((byte) invType))) {
                    if (ii.getName(inv.getItemId()) != null && ii.getName(inv.getItemId()).toLowerCase().contains(search)) {
                        found = inv;
                        break;
                    }
                }
                if (found == null) {
                    c.getPlayer().dropMessage(-2, "[Error] : No such item was found (" + search + ")");
                    return 0;
                }
                if (GameConstants.isPet(found.getItemId()) || GameConstants.isRechargable(found.getItemId())) {
                    c.getPlayer().dropMessage(-2, "[Error] : You may not trade this item using this command");
                    return 0;
                }
                if (quantity > found.getQuantity() || quantity <= 0 || quantity > ii.getSlotMax(found.getItemId())) {
                    c.getPlayer().dropMessage(-2, "[Error] : Invalid quantity");
                    return 0;
                }
                if (!c.getPlayer().getTrade().setItems(c, found, (byte) -1, quantity)) {
                    c.getPlayer().dropMessage(-2, "[Error] : This item could not be placed");
                    return 0;
                } else {
                    c.getPlayer().getTrade().chatAuto("[System] : " + c.getPlayer().getName() + " offered " + ii.getName(found.getItemId()) + " x " + quantity);
                }
            }
            return 1;
        }
    }

    public static class OfferEquip extends OfferCommand {

        public OfferEquip() {
            invType = 1;
        }
    }

    public static class OfferUse extends OfferCommand {

        public OfferUse() {
            invType = 2;
        }
    }

    public static class OfferSetup extends OfferCommand {

        public OfferSetup() {
            invType = 3;
        }
    }

    public static class OfferEtc extends OfferCommand {

        public OfferEtc() {
            invType = 4;
        }
    }

    public static class OfferCash extends OfferCommand {

        public OfferCash() {
            invType = 5;
        }
    }

    public static class BattleHelp extends PokemonExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(-3, "(...I can use @use <attack name> to take down the enemy...)");
            c.getPlayer().dropMessage(-3, "(...I can use @info to check out the stats of my battle...)");
            c.getPlayer().dropMessage(-3, "(...I can use @ball <basic, great, ultra> to use an ball, but only if I have it...)");
            c.getPlayer().dropMessage(-3, "(...I can use @run if I don't want to fight anymore...)");
            c.getPlayer().dropMessage(-4, "(...This is a tough choice! What do I do?...)"); //last msg they see
            return 1;
        }
    }

    public static class Ball extends PokemonExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getBattle().getInstanceId() < 0 || c.getPlayer().getBattle().isTrainerBattle()) {
                c.getPlayer().dropMessage(-3, "(...I can't use it in a trainer battle...)");
                return 0;
            }
            if (splitted.length <= 1) {
                c.getPlayer().dropMessage(-3, "(...I can use @ball <basic, great, or ultra> if I have the ball...)");
                return 0;
            }
            PokemonItem item = null;
            if (splitted[1].equalsIgnoreCase("basic")) {
                item = PokemonItem.Basic_Ball;
            } else if (splitted[1].equalsIgnoreCase("great")) {
                item = PokemonItem.Great_Ball;
            } else if (splitted[1].equalsIgnoreCase("ultra")) {
                item = PokemonItem.Ultra_Ball;
            }
            if (item != null) {
                if (c.getPlayer().haveItem(item.id, 1)) {
                    if (c.getPlayer().getBattle().useBall(c.getPlayer(), item)) {
                        MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(item.id), item.id, 1, false, false);
                    } else {
                        c.getPlayer().dropMessage(-3, "(...The monster is too strong, maybe I don't need it...)");
                        return 0;
                    }
                } else {
                    c.getPlayer().dropMessage(-3, "(...I don't have a " + splitted[1] + " ball...)");
                    return 0;
                }
            } else {
                c.getPlayer().dropMessage(-3, "(...I can use @ball <basic, great, or ultra> if I have the ball...)");
                return 0;
            }
            return 1;
        }
    }

    public static class Info extends PokemonExecute {

        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().start(c, 9000021); //no checks are needed
            return 1;
        }
    }

    public static class Run extends PokemonExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getBattle().forfeit(c.getPlayer(), false);
            return 1;
        }
    }

    public static class Use extends PokemonExecute {

        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length <= 1) {
                c.getPlayer().dropMessage(-3, "(...I need an attack name...)");
                return 0;
            }
            if (!c.getPlayer().getBattle().attack(c.getPlayer(), StringUtil.joinStringFrom(splitted, 1))) {
                c.getPlayer().dropMessage(-3, "(...I've already selected an action...)");
            }
            return 1;
        }
    }
}
