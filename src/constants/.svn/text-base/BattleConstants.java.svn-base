package constants;

import client.Battler;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import server.Randomizer;
import server.life.Element;
import server.life.MapleLifeFactory;
import server.life.MapleMonsterStats;
import tools.Pair;

public class BattleConstants {

    public static Point getNPCPos(int mapid) {
        switch (mapid) {
            case 925020010: //battle tower
            case 925020011:
            case 925020012:
            case 925020013:
            case 925020014:
                return new Point(252, 2);
            case 980010100: //gym leaders, elite4
            case 980010200:
            case 980010300:
                return new Point(142, 91);
            case 980010020:
                return new Point(190, 205);
            case 980010000:
                return new Point(447, 89);
        }
        return null;
    }

    public static enum PokemonMap { // eh?...=.=

        MAP1(190000000, 1, 4, 2, new Point(20, 35), new Point(320, 35)),
        MAP2(190000001, 1, 4, 0, new Point(-220, 215), new Point(80, 215)),
        MAP3(190000002, 1, 4, 3, new Point(-400, 215), new Point(-100, 215)),
        MAP4(191000000, 6, 4, 7, new Point(130, 278), new Point(430, 278)),
        MAP5(191000001, 6, 4, 1, new Point(-90, -15), new Point(210, -30)), //diff y values	
        MAP6(192000000, 11, 4, 4, new Point(1100, 2205), new Point(1400, 2205)),
        MAP7(192000001, 11, 4, 4, new Point(1100, 2205), new Point(1400, 2205)),
        MAP8(195000000, 16, 4, 3, new Point(1500, 1294), new Point(1800, 1294)), //diff y values	
        MAP9(195010000, 16, 4, 2, new Point(300, 1659), new Point(0, 1659), true), //diff y values	
        MAP10(195020000, 16, 4, 1, new Point(70, -31), new Point(370, -31)), //diff y values	
        MAP11(195030000, 16, 4, 1, new Point(-200, 160), new Point(100, 160)),
        MAP12(196000000, 21, 4, 5, new Point(-700, -26), new Point(-400, -26)),
        MAP13(196010000, 21, 4, 1, new Point(100, 454), new Point(400, 454)),
        MAP14(197000000, 26, 4, 0, new Point(250, 132), new Point(550, 132)),
        MAP15(197010000, 26, 4, 2, new Point(-600, -78), new Point(-300, -78)),
        MAP16(600010000, 31, 4, 5, new Point(-950, -1307), new Point(-650, -1307)),
        MAP17(880000000, 36, 4, 4, new Point(-1300, 215), new Point(-1000, 215)),
        MAP18(881000000, 41, 4, 3, new Point(1500, -173), new Point(1800, -173)),
        MAP19(809020000, 46, 4, 0, new Point(400, 338), new Point(700, 338)),
        MAP21(922220000, 51, 4, 0, new Point(0, 153), new Point(300, 153)),
        MAP22(924000100, 56, 4, 0, new Point(-400, 422), new Point(-100, 422)),
        MAP23(925010300, 61, 4, 0, new Point(-533, 333), new Point(-233, 333)),
        MAP24(950000100, 66, 4, 0, new Point(1300, 275), new Point(1600, 275)),
        MAP25(970020001, 71, 4, 0, new Point(300, 155), new Point(600, 155)),
        MAP26(970020002, 76, 4, 0, new Point(-300, 543), new Point(0, 543)),
        MAP27(970020003, 81, 4, 0, new Point(-50, 181), new Point(250, 181)), //diff y values
        MAP28(970020004, 86, 4, 0, new Point(-500, -18), new Point(-200, -18)),
        MAP29(910300000, 91, 4, 0, new Point(-100, -2131), new Point(200, -2131)),
        MAP32(910210000, 96, 4, 0, new Point(700, 165), new Point(1000, 165)),
        MAP33(910500000, 101, 9, 2, new Point(0, 130), new Point(300, 130)),
        MAP34(910500100, 111, 9, 0, new Point(-700, -200), new Point(-400, -200)),
        MAP35(910500200, 121, 9, 1, new Point(-100, -1130), new Point(200, -1130)),
        MAP36(922020100, 131, 19, 2, new Point(-1000, 42), new Point(-700, 42));
        //980010000, 980010100/980010200/980010300, 980010020 - ground/rock or something, arena for ariant lobby
        //battle tower - 925020010 - so gongs room [edit portalscript]
        public int id, minLevel, maxLevel, portalId;
        public boolean facingLeft;
        public Point pos0, pos1;

        private PokemonMap(int id, int minLevel, int offset, int portalId, Point pos0, Point pos1) {
            this.id = id;
            this.minLevel = minLevel;
            this.portalId = portalId;
            this.maxLevel = minLevel + offset;
            this.pos0 = pos0;
            this.pos1 = pos1;
            this.facingLeft = false;
        }

        private PokemonMap(int id, int minLevel, int offset, int portalId, Point pos0, Point pos1, boolean facingLeft) {
            this.id = id;
            this.minLevel = minLevel;
            this.portalId = portalId;
            this.maxLevel = minLevel + offset;
            this.pos0 = pos0;
            this.pos1 = pos1;
            this.facingLeft = facingLeft;
        }
    }

    public static final boolean isBattleMap(final int mapid) {
        return getMap(mapid) != null;
    }

    public static final PokemonMap getMap(final int mapid) {
        for (PokemonMap map : PokemonMap.values()) {
            if (map.id == mapid) {
                return map;
            }
        }
        return null;
    }

    public static enum Evolution {

        NONE(0),
        LEVEL(1),
        STONE(2);
        public int value;

        private Evolution(int value) {
            this.value = value;
        }
    }

    public static enum MobExp {

        EASY(0.07),
        ERRATIC(0.10),
        FAST(0.13),
        STANDARD(0.16),
        SLOW(0.19),
        FRUSTRATING(0.22),
        IMPOSSIBLE(0.25);
        //(value + (value * level / 10)) * (level ^ 2) = exp needed
        //(maxHP / exp) * (level / 2) * (trainer ? 1.5 : 1) = expGained
        public double value;

        private MobExp(double value) {
            this.value = value;
        }
    }
    private static List<PokedexEntry> pokedexEntries = new ArrayList<PokedexEntry>();
    private static List<Integer> gmMobs = new ArrayList<Integer>();
    private static EnumMap<PokemonMap, LinkedList<Pair<Integer, Integer>>> mapsToMobs = new EnumMap<PokemonMap, LinkedList<Pair<Integer, Integer>>>(PokemonMap.class);

    public static void init() {
        Map<Integer, List<Pair<Integer, Integer>>> mobsToMaps = new HashMap<Integer, List<Pair<Integer, Integer>>>();
        for (PokemonMap map : PokemonMap.values()) {
            LinkedList<Integer> set_check = new LinkedList<Integer>();
            LinkedList<Pair<Integer, Integer>> set = new LinkedList<Pair<Integer, Integer>>();
            for (PokemonMob mob : PokemonMob.values()) {
                for (int i = 0; i < mob.evolutions.size(); i++) {
                    final int id = mob.evolutions.get(i);
                    final MapleMonsterStats mons = MapleLifeFactory.getMonsterStats(id);
                    if (mons == null) {
                        System.out.println("WARNING: monster " + id + " does not exist.");
                    } else if ((id == 6400007 || !mons.isBoss()) && mons.getLevel() >= map.minLevel && mons.getLevel() <= map.maxLevel && !set_check.contains(id) && canAdd(id)) {
                        set.add(new Pair<Integer, Integer>(id, i + 1));
                        set_check.add(id);
                        List<Pair<Integer, Integer>> mtm = mobsToMaps.get(id);
                        if (mtm == null) {
                            mtm = new ArrayList<Pair<Integer, Integer>>();
                            mobsToMaps.put(id, mtm);
                        }
                        mtm.add(new Pair<Integer, Integer>(map.id, i + 1));
                    }
                }
            }
            set_check.clear();
            mapsToMobs.put(map, set);
        }
        LinkedHashMap<Integer, PokedexEntry> pokedex = new LinkedHashMap<Integer, PokedexEntry>();
        //pokedex
        int pokedexNum = 1;
        for (PokemonMob mob : PokemonMob.values()) {
            for (int i = 0; i < mob.evolutions.size(); i++) {
                final int id = mob.evolutions.get(i);
                if (!pokedex.containsKey(Integer.valueOf(id))) {
                    PokedexEntry pe = new PokedexEntry(id, pokedexNum);
                    List<Pair<Integer, Integer>> mtm = mobsToMaps.get(id);
                    if (mtm != null) {
                        pe.maps = new ArrayList<Pair<Integer, Integer>>();
                        for (Pair<Integer, Integer> mt : mtm) {
                            pe.maps.add(new Pair<Integer, Integer>(mt.left, (int) Math.round((1.0 / (double) mapsToMobs.get(getMap(mt.left)).size()) / (double) mt.right * 10000.0)));
                        }
                    } else {
                        pe.maps = null;
                    }
                    pe.dummyBattler = new Battler(MapleLifeFactory.getMonsterStats(id));
                    pe.dummyBattler.resetNature();
                    pe.dummyBattler.clearIV();
                    if (pe.dummyBattler.getStats().isBoss() && mob.type == MobExp.EASY) {
                        gmMobs.add(Integer.valueOf(id));
                        continue; //gm boss
                    }
                    if (i != 0) {
                        final MapleMonsterStats mm = MapleLifeFactory.getMonsterStats(mob.evolutions.get(i - 1));
                        if (mm != null) {
                            if (mob.evoItem != null && i == (mob.evolutions.size() - 1)) { //last pokemon
                                pe.pre.put(mob.evolutions.get(i - 1), mob.evoItem.id);
                            } else {
                                pe.pre.put(mob.evolutions.get(i - 1), pe.dummyBattler.getLevel());
                            }
                        }
                    }

                    if (i != (mob.evolutions.size() - 1)) {
                        final MapleMonsterStats mm = MapleLifeFactory.getMonsterStats(mob.evolutions.get(i + 1));
                        if (mm != null) {
                            if (mob.evoItem != null && i == (mob.evolutions.size() - 2)) { //2nd last pokemon
                                pe.evo.put(mob.evolutions.get(i + 1), mob.evoItem.id);
                            } else {
                                pe.evo.put(mob.evolutions.get(i + 1), (int) mm.getLevel());
                            }
                        }
                    }

                    pokedex.put(Integer.valueOf(id), pe);
                } else {
                    PokedexEntry pe = pokedex.get(Integer.valueOf(id));
                    if (i != 0 && !pe.pre.containsKey(mob.evolutions.get(i - 1))) {
                        final MapleMonsterStats mm = MapleLifeFactory.getMonsterStats(mob.evolutions.get(i - 1));
                        if (mm != null) {
                            if (mob.evoItem != null && i == (mob.evolutions.size() - 1)) { //last pokemon
                                pe.pre.put(mob.evolutions.get(i - 1), mob.evoItem.id);
                            } else {
                                pe.pre.put(mob.evolutions.get(i - 1), pe.dummyBattler.getLevel());
                            }
                        }
                    }
                    if (i != (mob.evolutions.size() - 1) && !pe.evo.containsKey(mob.evolutions.get(i + 1))) {
                        final MapleMonsterStats mm = MapleLifeFactory.getMonsterStats(mob.evolutions.get(i + 1));
                        if (mm != null) {
                            if (mob.evoItem != null && i == (mob.evolutions.size() - 2)) { //2nd last pokemon
                                pe.evo.put(mob.evolutions.get(i + 1), mob.evoItem.id);
                            } else {
                                pe.evo.put(mob.evolutions.get(i + 1), (int) mm.getLevel());
                            }
                        }
                    }
                }
            }
        }
        pokedexEntries.addAll(pokedex.values());
        mobsToMaps.clear();
        pokedex.clear();
    }

    public static boolean isGMMob(int idd) {
        return gmMobs.contains(Integer.valueOf(idd));
    }

    public static class PokedexEntry {

        public int id, num;
        public Battler dummyBattler; //for hp, exp, 
        public Map<Integer, Integer> pre = new LinkedHashMap<Integer, Integer>(), evo = new LinkedHashMap<Integer, Integer>();
        public List<Pair<Integer, Integer>> maps;

        public PokedexEntry(int id, int num) {
            this.id = id;
            this.num = num;
        }

        public List<Entry<Integer, Integer>> getPre() {
            return new ArrayList<Entry<Integer, Integer>>(pre.entrySet());
        }

        public List<Entry<Integer, Integer>> getEvo() {
            return new ArrayList<Entry<Integer, Integer>>(evo.entrySet());
        }
    }

    public static boolean canAdd(int id) {
        switch (id) {
            case 5100001:
            case 5130106: //transforming yetis, 0 exp
                return false;
        }
        return true;
    }

    public static LinkedList<Pair<Integer, Integer>> getMobs(PokemonMap mapp) {
        return mapsToMobs.get(mapp);
    }

    public static List<PokedexEntry> getAllPokedex() {
        return pokedexEntries;
    }

    public static byte getGender(PokemonMob mob) {
        switch (mob) {
            case Bunny:
            case Maverick_Y:
            case Maverick_B:
            case Maverick_V:
            case Maverick_S:
            case Guard:
            case Lord:
            case Roid:
            case Frankenroid:
            case Roi:
            case Mutae:
            case Rumo:
            case Robot:
            case Robo:
            case Block:
            case Block_Golem:
            case CD:
            case Mannequin:
            case Ninja:
            case Training_Robot:
            case Veil:
            case Veil_2:
            case Veil_3:
            case Veil_4:
            case Veil_5:
            case Unveil:
            case Cake:
            case Egg:
            case Accessory:
            case Clown:
            case Fire_Sentinel:
            case Ice_Sentinel:
            case Warrior:
            case Mage:
            case Mage_1:
            case Bowman:
            case Rogue:
            case Rogue_1:
            case Pirate:
            case Jar:
            case Vehicle:
            case Dummy_Strong:
            case Dummy:
            case Keeper:
            case Keeper_2:
                return 0;
            case Dragon:
            case Human_M:
            case Boss_M:
            case Viking:
            case Bird:
            case Black_Bird:
            case Red_Bird:
            case Blue_Bird:
            case Manon:
            case Griffey:
            case Mask:
            case Crow:
                return 1;
            case Kyrin_1:
            case Kyrin_2:
            case Human_F:
            case Witch:
            case Monkey:
            case White_Monkey:
            case Fairy:
            case Doll_V:
            case Doll_H:
            case Road_Auf:
            case Road_Dunas:
			case Cygnus_Boss:
                return 2;
        }
        return -1;
    }

    public static enum PokemonMob {

        //snail(1) -> blue snail(3) -> red snail(5) -> coke snail(10), moss snail (76), oversized snail (160)
        Snail(PokemonItem.Perfect_Pitch, MobExp.FAST, PokemonAbility.QuickFeet, PokemonAbility.Unburden, 100100, 100101, 130101, 9500144, 4250000, 8600000),
        //snail(1) -> blue snail(3) -> red snail(5) -> mano(10), moss snail (76), oversized snail (160)
        Snail_2(PokemonItem.Perfect_Pitch, MobExp.FAST, PokemonAbility.QuickFeet, PokemonAbility.Unburden, 100100, 100101, 130101, 2220000, 4250000, 8600000),
        //muru(1), murupa(3), murupia(5), murumuru(7), murukun(9)
        Muru(null, MobExp.EASY, PokemonAbility.ShedSkin, PokemonAbility.ShieldDust, 100130, 100131, 100132, 100133, 100134),
        //tino(1), tiv(3), timu(5), tiru(7), tiguru of exam(35), mutant (163, 164, 165)
        Ti(null, MobExp.EASY, PokemonAbility.Filter, PokemonAbility.TintedLens, 100120, 100121, 100122, 100123, 9001011, 8600004, 8600005, 8600006),
        //sprout(1), morning glory(3), grape juice(5), poison flower(43), poison sprite(45)
        Flower(null, MobExp.FAST, PokemonAbility.PurePower, PokemonAbility.SheerForce, 150000, 150001, 150002, 9300174, 9300179),
        //sprout(1), morning glory(3), grape juice(5), andras(25), red nirg(120)
        Andras(PokemonItem.Courage_Piece, MobExp.FAST, PokemonAbility.PurePower, PokemonAbility.SheerForce, 150000, 150001, 150002, 9400609, 9400591),
        //sprout(1), morning glory(3), grape juice(5), amdusias(25), rellik(120)
        Amdusias(PokemonItem.Accuracy_Piece, MobExp.FAST, PokemonAbility.PurePower, PokemonAbility.SheerForce, 150000, 150001, 150002, 9400610, 9400592),
        //sprout(1), morning glory(3), grape juice(5), crocell(25), heron(120)
        Crocell(PokemonItem.Freedom_Piece, MobExp.FAST, PokemonAbility.PurePower, PokemonAbility.SheerForce, 150000, 150001, 150002, 9400611, 9400589),
        //sprout(1), morning glory(3), grape juice(5), marbas(25), margana(120)
        Marbas(PokemonItem.Wisdom_Piece, MobExp.FAST, PokemonAbility.PurePower, PokemonAbility.SheerForce, 150000, 150001, 150002, 9400612, 9400590),
        //sprout(1), morning glory(3), grape juice(5), valefor(25), hsalf(120)
        Valefor(PokemonItem.Dexterity_Piece, MobExp.FAST, PokemonAbility.PurePower, PokemonAbility.SheerForce, 150000, 150001, 150002, 9400613, 9400593),
        //spore(3),, trainee(12), renegade(33)
        Spore(null, MobExp.ERRATIC, PokemonAbility.EffectSpore, PokemonAbility.Immunity, 120100, 9300386, 3300000),
        //black cat(5), sage cat(95), king sage cat(100)
        Sage_Cat(PokemonItem.Bright_Feather, MobExp.SLOW, PokemonAbility.RunAway, PokemonAbility.Normalize, 9400636, 6130209, 7220002),
        //orange mush(6) -> green(7) -> blue(10) -> horny(13) -> zombie(15) -> drunk blue(16) -> coke(20) -> urban fungus(21) -> boomer(27) -> poison(31), moss mushroom(80), mutant mushroom (161)
        Mushroom(PokemonItem.Perfect_Pitch, MobExp.FAST, PokemonAbility.EffectSpore, PokemonAbility.Overgrow, 1210102, 1110100, 2110200, 2220100, 2230101, /*9400702,*/ 9500152, 9400539, 9400550, 3300001, 5250000, 8600001),
        //orange mush(6) -> green(7) -> blue(10) -> horny(13) -> mushmom(15) -> blue mushmom(18), zombie mushmom(21), mushmom(60), zombie mushmom(65), blue mushmom(90)
        Mushmom(PokemonItem.Melted_Chocolate, MobExp.FAST, PokemonAbility.EffectSpore, PokemonAbility.Overgrow, 1210102, 1110100, 2110200, 2220100, 6130100, 8220007, 6300005, 9300191, 9300196, 9300209),
        //jr boogie(7), red boogie(15), blue boogie(30), green boogie(45), black boogie(65), boogie(70)
        Boogie(null, MobExp.STANDARD, PokemonAbility.OwnTempo, PokemonAbility.Limber, 3230300, 9400005, 9400006, 9400007, 9400008, 6130104),
        //jr boogie(7), red boogie(15), blue boogie(30), green boogie(45), black boogie(65), boogie(70)
        Chaos_Boogie(null, MobExp.STANDARD, PokemonAbility.OwnTempo, PokemonAbility.Limber, 3230300, 9400005, 9400006, 9400007, 9400008, 8800111),
        //pig(7) -> drunk(8), ribbon pig(10) -> blue ribbon pig(14) -> iron hog(22) -> intoxicated pig(32) -> lupin(33) -> coke pig(35) -> mutant pig (161)
        Pig(PokemonItem.Perfect_Pitch, MobExp.STANDARD, PokemonAbility.Gluttony, PokemonAbility.Scrappy, 1210100, /*9400701,*/ 1210101, 1210104, 4230103, 3300002, 9302011, 9500143, 8600003),
        //pig(7) -> drunk(8), ribbon pig(10) -> blue ribbon pig(14) -> wild boar(19) -> fire boar(26) -> crystal boar(40) -> iron boar(70) -> primitive boar (84)
        Boar(PokemonItem.Perfect_Pitch, MobExp.STANDARD, PokemonAbility.Gluttony, PokemonAbility.Scrappy, 1210100, /*9400701,*/ 1210101, 1210104, 2230102, 3210100, 9400516, 4230400, 5250002),
        //drunk slime(5), slime(7) -> bubbling(10) -> coke slime(15) -> street slime(19) -> devil slime(23) -> O slime(38) -> king slime(40)
        Boss_Slime(PokemonItem.Melted_Chocolate, MobExp.STANDARD, PokemonAbility.Illuminate, PokemonAbility.MagicGuard, 9400737, 210100, 1210103, 9500151, 9400538, 9300027, 9400521, 9300187),
        //drunk slime(5), slime(7) -> bubbling(10) -> coke slime(15) -> street slime(19) -> devil slime(23) -> O Slime(38) -> silver slime(40) -> emo slime(47), red slime(55) -> cube slime(78) -> gold slime(107) -> mutant slime (161)
        Slime(PokemonItem.Perfect_Pitch, MobExp.STANDARD, PokemonAbility.Illuminate, PokemonAbility.MagicGuard, 9400737, 210100, 1210103, 9500151, 9400538, 9300027, 9400521, 9400203, 9420528, 9400204, 3110300, 7120105, 8600002),
        //treacherous fox(7), cloud fox(30), dark cloud fox(45), samiho(62), nine tailed(65)
        Fox(null, MobExp.ERRATIC, PokemonAbility.SpeedBoost, PokemonAbility.Unnerve, 9300385, 9400002, 9400004, 5100004, 7220001),
        //jr.mv(8), wooden(28), rocky(29)
        Mask(null, MobExp.EASY, PokemonAbility.ShadowTag, PokemonAbility.Insomnia, 9400706, 2230110, 2230111),
        //jr.mv(8), mv(10)
        MV(PokemonItem.Deathly_Fear, MobExp.EASY, PokemonAbility.ShadowTag, PokemonAbility.Insomnia, 9400706, 9400746),
        //stump(8) -> dark stump(10) -> axe stump(12) -> dark axe stump(14) -> ghost stump(16) -> stumpy(20) -> poisoned lord tree(43), chlorotrap(45), bacal(80), berserkie(85), duku(115)
        Stump(null, MobExp.SLOW, PokemonAbility.SapSipper, PokemonAbility.SapSipper, 130100, 1110101, 1130100, 2130100, 1140100, 3220000, 9300172, 9420527, 9420523, 9420514, 9420519),
        //frog(5), frog(8), toad(28)
        Frog(null, MobExp.EASY, PokemonAbility.Immunity, PokemonAbility.PoisonHeal, 9400634, 9420001, 9420000),
        //candle(10), anniv cake(15) cake(30)
        Cake(null, MobExp.EASY, PokemonAbility.NaturalCure, PokemonAbility.Synchronize, 9400506, 9400570, 9400507),
        //candle(10), anniv cake(15) cake(30)
        Cake_2(null, MobExp.EASY, PokemonAbility.NaturalCure, PokemonAbility.Synchronize, 9400512, 9400570, 9400513),
        //A(10), B(11), C(12), D(13), force(18)
        Training_Robot(null, MobExp.SLOW, PokemonAbility.Heatproof, PokemonAbility.Stall, 9300409, 9300410, 9300411, 9300412, 9300413),
        //goblin fire(11), firebomb(51), blin(64), firebrand(90), elements(160)
        Veil(null, MobExp.FAST, PokemonAbility.FlareBoost, PokemonAbility.FlameBody, 9300083, 5100002, 6130201, 9400577, 8610000),
        //goblin fire(11), firebomb(51), blin(64), firebrand(90), elements(160)
        Veil_2(null, MobExp.FAST, PokemonAbility.FlareBoost, PokemonAbility.FlameBody, 9300083, 5100002, 6130201, 9400577, 8610001),
        //goblin fire(11), firebomb(51), blin(64), firebrand(90), elements(160)
        Veil_3(null, MobExp.FAST, PokemonAbility.FlareBoost, PokemonAbility.FlameBody, 9300083, 5100002, 6130201, 9400577, 8610002),
        //goblin fire(11), firebomb(51), blin(64), firebrand(90), elements(160)
        Veil_4(null, MobExp.FAST, PokemonAbility.FlareBoost, PokemonAbility.FlameBody, 9300083, 5100002, 6130201, 9400577, 8610003),
        //goblin fire(11), firebomb(51), blin(64), firebrand(90), elements(160)
        Veil_5(null, MobExp.FAST, PokemonAbility.FlareBoost, PokemonAbility.FlameBody, 9300083, 5100002, 6130201, 9400577, 8610004),
        //goblin fire(11), firebomb(51), blin(64),
        Unveil(null, MobExp.FAST, PokemonAbility.FlareBoost, PokemonAbility.FlameBody, 9300083, 5100002, 6130202),
        //stirge(11), blood stirge(20), deathly fear(43), gargoyle(62), flyeye(122), demon gargoyle(125)
        Flying(null, MobExp.EASY, PokemonAbility.SuperLuck, PokemonAbility.Sniper, 2300100, 9400595, 9300084, 9300025, 4230107, 8840001),
        //octopus(12), octopirate(35), bloctopus(54), king bloctopus(58), mateon(66), plateon(69), mecateon(71), MT-09(73)
        Alien(null, MobExp.STANDARD, PokemonAbility.Klutz, PokemonAbility.Multiscale, 1120100, 9001005, 3230302, 3230103, 4230120, 4230121, 4230122, 5120100),
        //jr wraith(13) -> wraith(15) -> shade(17) -> glutton ghoul(21) ->  nightghost(60) -> book ghost(84) -> elderwraith(95)
        Wraith(null, MobExp.STANDARD, PokemonAbility.Forewarn, PokemonAbility.Frisk, 3230101, 4230102, 5090000, 9400556, 9400003, 5120506, 9400580),
        //jr wraith(13) -> wraith(15) -> shade(17) -> glutton ghoul(21) ->  nightghost(60) -> book ghost(84) -> saitie(95)
        Wraith_Ghost(null, MobExp.STANDARD, PokemonAbility.Forewarn, PokemonAbility.Frisk, 3230101, 4230102, 5090000, 9400556, 9400003, 5120506, 6110301),
        //jr wraith(13) -> wraith(15) -> shade(17) -> glutton ghoul(21) ->  nightghost(60) -> scholar ghost(70)
        Wraith_Boss(PokemonItem.Saint_Stone, MobExp.STANDARD, PokemonAbility.Forewarn, PokemonAbility.Frisk, 3230101, 4230102, 5090000, 9400556, 9400003, 6090003),
        //normal(13), royal(15), fierry(30), magikA(40),
        Fairy(null, MobExp.STANDARD, PokemonAbility.SereneGrace, PokemonAbility.Illuminate, 3000001, 3000007, 9400526, 9400517),
        //patrol robot(14), robot S(28)
        Patrol(null, MobExp.STANDARD, PokemonAbility.MotorDrive, PokemonAbility.Regenerator, 1150000, 2150003),
        //rooster(15), black rooster(20), duck(22), crow(25), chirrpy(50) , tweeter(61), kiyo(79), typhon(100), soaring hawk(103), blood harp(114), black bird(120), eagle(125)
        Bird(null, MobExp.FAST, PokemonAbility.Defiant, PokemonAbility.Compoundeyes, 9420005, 9600001, 9600002, 9400000, 3230307, 3230308, 3100102, 9400574, 8300000, 8140002, 9400599, 8210004),
        //rooster(15), black rooster(20), duck(22), crow(25), gryphon(50) , tweeter(61), kiyo(79), typhon(100), soaring eagle(103), harp(109), black crow(115)
        Black_Bird(PokemonItem.Saint_Stone, MobExp.FAST, PokemonAbility.Defiant, PokemonAbility.Compoundeyes, 9420005, 9600001, 9600002, 9400000, 9400544, 3230308, 3100102, 9400574, 8300001, 8140001, 9400014),
        //rooster(15), black rooster(20), duck(22), crow(25), chirrpy(50) , tweeter(61), kiyo(79), typhon(100), soaring hawk(103), blood harp(114), black bird(120), phoenix(120)
        Red_Bird(PokemonItem.Phoenix_Egg, MobExp.FAST, PokemonAbility.Defiant, PokemonAbility.Compoundeyes, 9420005, 9600001, 9600002, 9400000, 3230307, 3230308, 3100102, 9400574, 8300000, 8140002, 9400599, 9300089),
        //rooster(15), black rooster(20), duck(22), crow(25), gryphon(50) , tweeter(61), kiyo(79), typhon(100), soaring eagle(103), harp(109), black bird(120), freezer(120)
        Blue_Bird(PokemonItem.Freezer_Egg, MobExp.FAST, PokemonAbility.Defiant, PokemonAbility.Compoundeyes, 9420005, 9600001, 9600002, 9400000, 9400544, 3230308, 3100102, 9400574, 8300001, 8140001, 9400599, 9300090),
        //rooster(15), black rooster(20), duck(22), crow(25), gryphon(50) , tweeter(61), kiyo(79), typhon(100), dragonoir(107), dragon rider(120)
        Dragon(PokemonItem.Dragon_Heart, MobExp.FAST, PokemonAbility.Defiant, PokemonAbility.Compoundeyes, 9420005, 9600001, 9600002, 9400000, 9400544, 3230308, 3100102, 9400574, 8300006, 8300007),
        //rooster(15), black rooster(20), duck(22), crow(25), gryphon(50) , tweeter(61), kiyo(79), typhon(100), manon(100)
        Manon(PokemonItem.Dragon_Heart, MobExp.FAST, PokemonAbility.Defiant, PokemonAbility.Compoundeyes, 9420005, 9600001, 9600002, 9400000, 9400544, 3230308, 3100102, 9400574, 9300291),
        //rooster(15), black rooster(20), duck(22), crow(25), chirrpy(50) , tweeter(61), kiyo(79), typhon(100), griffey(100)
        Griffey(PokemonItem.Griffey_Wind, MobExp.FAST, PokemonAbility.Defiant, PokemonAbility.Compoundeyes, 9420005, 9600001, 9600002, 9400000, 3230307, 3230308, 3100102, 9400574, 9300292),
        //rooster(15), black rooster(20), duck(22), crow(25), chirrpy(50) , tweeter(61), kiyo(79), thief crow(80)
        Crow(PokemonItem.Intelligence_Document, MobExp.FAST, PokemonAbility.Defiant, PokemonAbility.Compoundeyes, 9420005, 9600001, 9600002, 9400000, 3230307, 3230308, 3100102, 9001013),
        //baby(15), wild(21), peach monkey(97)
        Monkey(null, MobExp.FAST, PokemonAbility.EarlyBird, PokemonAbility.Contrary, 9500383, 9500384, 6130207),
        //baby(15), white baby(20), white mama(34), peach monkey(97)
        White_Monkey(null, MobExp.FAST, PokemonAbility.EarlyBird, PokemonAbility.Contrary, 9500383, 9500385, 9500386, 6130207),
        //strange sign(16), stopnow(28), safety first(31) nospeed(33)
        Sign(null, MobExp.FAST, PokemonAbility.Aftermath, PokemonAbility.AngerPoint, 1150001, 9420500, 3150000, 9420503),
        //evil eye(18) -> curse eye(20) -> cold eye(22) -> surgeon eye(24) -> indigo eye(38), wild kargo(60), eye of time(120)
        Eye(null, MobExp.STANDARD, PokemonAbility.Adaptability, PokemonAbility.Analytic, 2230100, 3230100, 4230100, 2230113, 9400515, 6230100, 8200000),
        //evil eye(18) -> curse eye(20) -> drunk eye(22) -> surgeon eye(24) -> indigo eye(38), wild kargo(60), eye of time(120)
        Eye_Drunk(null, MobExp.STANDARD, PokemonAbility.Adaptability, PokemonAbility.Analytic, 2230100, 3230100, /*9400703,*/ 2230113, 9400515, 6230100, 8200000),
        //serpent(18), jr necki(19), astaroth(35)
        Dark_Snake(null, MobExp.STANDARD, PokemonAbility.Intimidate, PokemonAbility.PoisonTouch, 1150002, 2130103, 9400633),
        //serpent(18), jr necki(19), python(60), bellamoa(71), blue flower(81), slygie(95), adv leviathan(100)
        Blue_Snake(PokemonItem.Water_Bottle, MobExp.STANDARD, PokemonAbility.Intimidate, PokemonAbility.PoisonTouch, 1150002, 2130103, 9420002, 2100105, 4230503, 9420516, 9300293),
        //serpent(18), jr necki(19), python(60), bellamoa(71), red flower(81), slygie(95), giant centipede(108)
        Red_Snake(PokemonItem.Yellow_Crystal, MobExp.STANDARD, PokemonAbility.Intimidate, PokemonAbility.PoisonTouch, 1150002, 2130103, 9420002, 2100105, 4230504, 9420516, 5220004),
        //biner(18), batoo(23), freezer(38), dark fission(52), oly oly(56), rodeo(61), charmer(65), octobunny(75), veetron(90), petrifighter(110)
        Black_Slime(null, MobExp.STANDARD, PokemonAbility.ClearBody, PokemonAbility.Stench, 9420502, 9420506, 9420501, 9420529, 9420530, 9420533, 9420534, 9420508, 9420515, 9420517),
        //stone(20), darkstone(23), mixed(26), coke(50), ice(60), puppet(67), enraged(90), castle(125), castle(127), nest(150)
        Golem(null, MobExp.SLOW, PokemonAbility.ClearBody, PokemonAbility.BigPecks, 5130101, 5130102, 5150000, 9500149, 9500150, 9300416, 9300024, 9300287, 8840005, 8210005, 8190002),
        //stone(20), darkstone(23), mixed(26), poison golem(45, 46, 48)
        Poison_Golem(null, MobExp.SLOW, PokemonAbility.ClearBody, PokemonAbility.BigPecks, 5130101, 5130102, 5150000, 9300180, 9300181, 9300182),
        //shadow(20), giant(25), lord(30), knight(54), master(60)
        Dual_Blade(null, MobExp.ERRATIC, PokemonAbility.SpeedBoost, PokemonAbility.QuickFeet, 9001015, 9001016, 9001017, 9001018),
        //yellow egg(20), green egg(35)
        Egg(null, MobExp.EASY, PokemonAbility.MarvelScale, PokemonAbility.Scrappy, 9400511, 9400510),
        //water thief monster(20), swamp monster(25), poisoned stone bug(45), stone bug(82)
        Monster(PokemonItem.Perfect_Pitch, MobExp.FAST, PokemonAbility.NoGuard, PokemonAbility.Hustle, 2150000, 2230114, 9300173, 5250001),
        //ligator(21), croco(23), dyle(28), alli(99), crocky(115), red crocky(125)
        Crocodile(null, MobExp.FAST, PokemonAbility.WaterVeil, PokemonAbility.WaterAbsorb, 3110100, 5130103, 6220000, 6130204, 8210000, 8840002),
        //dust box(22), streetlight(25)
        Accessory(null, MobExp.FAST, PokemonAbility.Contrary, PokemonAbility.Contrary, 2150001, 2150002),
        //stray dog(23), stylish stray(25), angry stray(42), hector(54), white fang(55), werewolf(75), lycanthrope(128), wolf underling(150)
        Wolf(null, MobExp.STANDARD, PokemonAbility.HugePower, PokemonAbility.HyperCutter, 9410000, 9410001, 9410002, 5130104, 5140000, 9500132, 8140000, 9300354),
        //genin(25), ashigaru(30), chunin(50), kunoichi(60), jonin(80), ninto(100),samurai(120)
        Ninja(null, MobExp.FAST, PokemonAbility.QuickFeet, PokemonAbility.SpeedBoost, 9400400, 9400404, 9400401, 9400406, 9400402, 9400403, 9400405),
        //yellow(25), red(40)
        Lizard(null, MobExp.FAST, PokemonAbility.DrySkin, PokemonAbility.Moxie, 9420004, 9420003),
        //white(25), black(37)
        Sheep(null, MobExp.EASY, PokemonAbility.Intimidate, PokemonAbility.Moody, 9600003, 9600008),
        //lupin(26), zlupin(28), drunk(30), faust(33)
        Lupin(PokemonItem.Minidungeon_Crystal, MobExp.STANDARD, PokemonAbility.Guts, PokemonAbility.BigPecks, 3210800, 4230101, 9400738, 5220002),
        //lupin(26), zlupin(28), pig(32), clown(40), biker(47)
        Lupin_Clown(null, MobExp.STANDARD, PokemonAbility.Guts, PokemonAbility.BigPecks, 3210800, 4230101, 9302011, 9410003, 9410004),
        //soldier(26), skeledog(31), mummydog(32), officer(35), commander(36), skelegon(149), skelosaurus(157)
        Skeleton(null, MobExp.STANDARD, PokemonAbility.BadDreams, PokemonAbility.WeakArmor, 5150001, 4230125, 4230126, 6230602, 7130103, 8190003, 8190004),
        //psycho jack(30), twisted jester(70)
        Clown(null, MobExp.ERRATIC, PokemonAbility.BadDreams, PokemonAbility.WeakArmor, 9400558, 9400640),
        //suspicious thief(23), bunny doll(30), drumming(52), moon(55), rabbit(56), gold rabbit(57), female(68), male(69), black wings henchman(70)
        Bunny(null, MobExp.EASY, PokemonAbility.QuickFeet, PokemonAbility.SpeedBoost, 9300414, 9400649, 3230400, 4230300, 5160000, 5160001, 2100100, 2100101, 9300392),
        //flaming raccoon(30), racoco(70), racaroni(76), raco(80)
        Raco(null, MobExp.FAST, PokemonAbility.Moody, PokemonAbility.Moxie, 9400001, 7150000, 7150003, 8105000),
        //white(30), black(35)
        Goat(null, MobExp.EASY, PokemonAbility.Intimidate, PokemonAbility.Moody, 9600004, 9600005),
        //malady(30), snow witch(58)
        Witch(PokemonItem.Ice_Pick, MobExp.FAST, PokemonAbility.Intimidate, PokemonAbility.IronBarbs, 5300100, 6090001),
        //so gong(20), scarred bear(30), mu gong shadow(54), cart bear(74), mingu(80) grizzly(88), panda(89), tae roon(90), bearwolf(122), mu gong (150)
        Bear(PokemonItem.Minidungeon_Diamond, MobExp.FAST, PokemonAbility.SpeedBoost, PokemonAbility.Defeatist, 9300269, 9001012, 9300350, 7150002, 9300270, 5120500, 6130203, 7220000, 8210003, 9300215),
        //cherry(32), mango(33), melon(34), tea(37)
        Red_Tea(null, MobExp.EASY, PokemonAbility.WaterAbsorb, PokemonAbility.WaterVeil, 3400000, 3400001, 3400002, 9410005),
        //cherry(32), mango(33), melon(34), tea(37)
        Yellow_Tea(null, MobExp.EASY, PokemonAbility.WaterAbsorb, PokemonAbility.WaterVeil, 3400000, 3400001, 3400002, 9410006),
        //cherry(32), mango(33), melon(34), tea(37)
        Green_Tea(null, MobExp.EASY, PokemonAbility.WaterAbsorb, PokemonAbility.WaterVeil, 3400000, 3400001, 3400002, 9410007),
        //helmet(33), guard(34), jr.(43), pepe(48), dark(49), scuba(66)
        Pepe(null, MobExp.ERRATIC, PokemonAbility.ThickFat, PokemonAbility.ThickFat, 3300003, 3300004, 5400000, 6130103, 6230100, 3210450),
        //baby muncher(33), big muncher(35), ore muncher(108)
        Muncher(null, MobExp.FAST, PokemonAbility.BattleArmor, PokemonAbility.MagmaArmor, 3150001, 3150002, 8105005),
        //cow(33), ox(38)
        Cow(null, MobExp.EASY, PokemonAbility.Moxie, PokemonAbility.Moody, 9600006, 9600007),
        //jr sentinel(35), sentinel(38), fire(39)
        Fire_Sentinel(null, MobExp.FAST, PokemonAbility.MagicGuard, PokemonAbility.MagicGuard, 5200000, 3000000, 5200002),
        //jr sentinel(35), sentinel(38), ice(39)
        Ice_Sentinel(null, MobExp.FAST, PokemonAbility.MagicGuard, PokemonAbility.MagicGuard, 5200000, 3000000, 5200001),
        //sakura cellion(33), jr.cellion(36), cellion(46), jr.lucida(50), lucida(54), eliza(83)
        Cellion(PokemonItem.Most_Corrupted, MobExp.FAST, PokemonAbility.TintedLens, PokemonAbility.Filter, 9400509, 3210200, 5120001, 6230401, 7130000, 9500315),
        //jr.lioner(36), lioner(46), jr.lucida(50), lucida(54), eliza(83)
        Lioner(PokemonItem.Most_Corrupted, MobExp.FAST, PokemonAbility.TintedLens, PokemonAbility.Filter, 3210201, 5120002, 6230401, 7130000, 9500315),
        //jr.grupin(36), grupin(46), jr.lucida(50), lucida(54), eliza(93)
        Grupin(PokemonItem.Most_Corrupted, MobExp.FAST, PokemonAbility.TintedLens, PokemonAbility.Filter, 3210202, 5120003, 6230401, 7130000, 9500315),
        //lorang(36), clang(37), king clang(42)
        Lorang(PokemonItem.Emerald_Crystal, MobExp.FAST, PokemonAbility.PoisonPoint, PokemonAbility.PoisonTouch, 3230102, 4230104, 5220000),
        //fire tusk(36), electrophant(41), mammoth(61), golden mammoth(63)
        Elephant(null, MobExp.FAST, PokemonAbility.Static, PokemonAbility.Blaze, 9400542, 9400543, 6160001, 6160002),
        //chronos(37), platoon(41), master(46), alishar(56)
        Chronos(PokemonItem.More_Corrupted, MobExp.ERRATIC, PokemonAbility.BadDreams, PokemonAbility.Defiant, 9300015, 9300016, 9300017, 9300192),
        //star(38), lunar(41), luster(43), ghost(48), papa(58)
        Pixie(PokemonItem.Corrupted_Item, MobExp.EASY, PokemonAbility.MagicGuard, PokemonAbility.SereneGrace, 3230200, 4230106, 5120000, 9300038, 9300039),
        //coke seal(38), play seal(42), seal(68), freezer(72), sparker(73)
        Walrus(null, MobExp.FAST, PokemonAbility.ThickFat, PokemonAbility.VoltAbsorb, 9500145, 9500146, 3230405, 4230124, 4230123),
        //kid(38), female(39), male(40)
        Mannequin(null, MobExp.EASY, PokemonAbility.MarvelScale, PokemonAbility.Unnerve, 4300006, 4300007, 4300008),
        //tortie(39), ice turtle(45)
        Tortie(null, MobExp.SLOW, PokemonAbility.Stall, PokemonAbility.OwnTempo, 4130101, 9500148),
        //leprechaun(40), bigfoot(102)
        Leprechaun(PokemonItem.Magical_Array, MobExp.ERRATIC, PokemonAbility.Defeatist, PokemonAbility.TangledFeet, 9400583, 9400575),
        //nightmare(40), toy trojan(57), headless horseman(100)
        Horse(PokemonItem.Life_Root, MobExp.ERRATIC, PokemonAbility.Defeatist, PokemonAbility.TangledFeet, 9400563, 3230305, 9400549),
        //killa bee(40), big spider(72), wolf spider(80)
        Spider(null, MobExp.FAST, PokemonAbility.ToxicBoost, PokemonAbility.TangledFeet, 9400540, 7150001, 9400545),
        //leatty(40), dark leatty(42)
        Leatty(null, MobExp.EASY, PokemonAbility.Multiscale, PokemonAbility.Heatproof, 5300000, 5300001),
        //night lantern(45), dreamy ghost(100)
        Night(null, MobExp.SLOW, PokemonAbility.LiquidOoze, PokemonAbility.QuickFeet, 9400011, 9400013),
        //night lantern(45), bamboo warrior(68),
        Night_Boss(PokemonItem.Bright_Feather, MobExp.SLOW, PokemonAbility.LiquidOoze, PokemonAbility.QuickFeet, 9400011, 6090002),
        //latest hits(41), oldies(42), cheap amp(43), fancy amp(44), guitar hero(45)
        CD(PokemonItem.Deathly_Fear, MobExp.ERRATIC, PokemonAbility.Stall, PokemonAbility.Defeatist, 4300009, 4300010, 4300011, 4300012, 4300013),
        //red(43), blue(54), water(60), stone(66)
        Goblin(null, MobExp.FAST, PokemonAbility.Analytic, PokemonAbility.Adaptability, 9500387, 9500388, 9400012, 9500389),
        //red(43), blue(54), water(60), yellow(66)
        Yellow_Goblin(null, MobExp.FAST, PokemonAbility.Analytic, PokemonAbility.Adaptability, 9500387, 9500388, 9400012, 7130400),
        //red(43), blue(54), water(60), blue(66)
        Blue_Goblin(null, MobExp.FAST, PokemonAbility.Analytic, PokemonAbility.Adaptability, 9500387, 9500388, 9400012, 7130401),
        //red(43), blue(54), water(60), green(66)
        Green_Goblin(null, MobExp.FAST, PokemonAbility.Analytic, PokemonAbility.Adaptability, 9500387, 9500388, 9400012, 7130402),
        //ratz(43), black ratz(46)
        Ratz(null, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.Limber, 3110102, 3210205),
        //ratz(43), retz(46)
        Retz(null, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.Limber, 3110102, 3210208),
        //Extra A(45), B(47), C(50), leader A(62), leader B(64), male boss(95), bodyguard A (152)
        Human_M(PokemonItem.Gold_Pick, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400100, 9400101, 9400102, 9400110, 9400111, 9400120, 9400112),
        //bodyguard A (152), bodyguard B(155), the boss(175)
        Boss_M(PokemonItem.Gold_Pick, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400112, 9400113, 9400300),
        //extra D(72), anego(130)
        Human_F(PokemonItem.Gold_Pick, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400103, 9400121),
        //Extra A(45), B(47), C(50), leader A(62), leader B(64), boss(70), official(165), advanced (169), boss(170)
        Warrior(PokemonItem.Courage_Piece, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400100, 9400101, 9400102, 9400110, 9400111, 9001000, 8610005, 8610010, 8850000),
        //Extra A(45), B(47), C(50), leader A(62), leader B(64), boss(70), official(165), advanced (169), boss(170)
        Mage(PokemonItem.Wisdom_Piece, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400100, 9400101, 9400102, 9400110, 9400111, 9001001, 8610006, 8610011, 8850001),
        //Extra A(45), B(47), C(50), leader A(62), leader B(64), boss(70), ifrit(169)
        Mage_1(PokemonItem.Wisdom_Piece, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400100, 9400101, 9400102, 9400110, 9400111, 9001001, 8610015),
        //Extra A(45), B(47), C(50), leader A(62), leader B(64), boss(70), official(165), advanced (169), boss(170)
        Bowman(PokemonItem.Accuracy_Piece, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400100, 9400101, 9400102, 9400110, 9400111, 9001002, 8610007, 8610012, 8850002),
        //Extra A(45), B(47), C(50), leader A(62), leader B(64), boss(70), official(165), advanced (169), boss(170)
        Rogue(PokemonItem.Dexterity_Piece, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400100, 9400101, 9400102, 9400110, 9400111, 9001003, 8610008, 8610013, 8850003),
        //Extra A(45), B(47), C(50), leader A(62), leader B(64), boss(70), watchman(169)
        Rogue_1(PokemonItem.Coin, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400100, 9400101, 9400102, 9400110, 9400111, 9001003, 8610016),
        //Extra A(45), B(47), C(50), leader A(62), leader B(64), boss(70), official(165), advanced (169), boss(170)
        Pirate(PokemonItem.Freedom_Piece, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9400100, 9400101, 9400102, 9400110, 9400111, 9001004, 8610009, 8610014, 8850004),
        //kyrin(70), battleship(120)
        Kyrin_1(PokemonItem.Coin, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9001004, 9300158),
        //kyrin(70), inferno(120)
        Kyrin_2(PokemonItem.Coin, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.ShadowTag, 9001004, 9300159),
        //jr.(45), transforming(45), yeti(65), transformed yeti(117), yeti+pepe(122)
        Yeti_Weak(null, MobExp.STANDARD, PokemonAbility.ThickFat, PokemonAbility.Unburden, 5100000, 5100001, 9300258, 6300001, 7130102),
        //jr.(45), transforming(45), yeti(65), transformed yeti(117), snowman(122)
        Yeti_Strong(PokemonItem.Blue_Crystal, MobExp.STANDARD, PokemonAbility.ThickFat, PokemonAbility.Unburden, 5100000, 5100001, 9300258, 6300001, 8220001),
        //jr.(45), transforming dark(46), dark yeti(68), transformed dark yeti(119), dark yeti+pepe(124)
        Dark_Yeti_Weak(null, MobExp.STANDARD, PokemonAbility.ThickFat, PokemonAbility.Unburden, 5100000, 5130106, 9500128, 6400001, 8140100),
        //i am robot(45), guard robot(67), robot L(79), security camera(88)
        Camera(PokemonItem.Summoning_Frame, MobExp.STANDARD, PokemonAbility.MotorDrive, PokemonAbility.Static, 9400546, 6150000, 7150004, 7090000),
        //jar(47), ginseng(93)
        Jar(null, MobExp.FAST, PokemonAbility.ShieldDust, PokemonAbility.ClearBody, 9001022, 4230506),
        //baby(48), jr(66), crimson(100), geist (101)
        Balrog(PokemonItem.Most_Corrupted, MobExp.IMPOSSIBLE, PokemonAbility.Intimidate, PokemonAbility.Intimidate, 6400007, 8130100, 8150000, 9400514),
        //trucker(48), tippo red(80), tippo blue(83), montrecer(105)
        Vehicle(null, MobExp.FAST, PokemonAbility.MotorDrive, PokemonAbility.Regenerator, 9420507, 9420504, 9420505, 9420518),
        //brown(49), pink(51), panda(54), soul(67), master soul(98), death teddy(112), master death(116)
        Teddy(null, MobExp.FAST, PokemonAbility.Synchronize, PokemonAbility.Insomnia, 3000005, 3110101, 3210203, 9001028, 6230500, 7130010, 7130300),
        //scarecrow(50), straw(82), wooden(83), master(84)
        Dummy_Strong(PokemonItem.Black_Diamond, MobExp.ERRATIC, PokemonAbility.Forewarn, PokemonAbility.Frisk, 9400539, 5120503, 5120504, 5090001),
        //straw(50), wooden(51), master(84)
        Dummy(PokemonItem.Black_Diamond, MobExp.ERRATIC, PokemonAbility.Forewarn, PokemonAbility.Frisk, 9001020, 9001021, 5090001),
        //copper(50), normal(52), red(54), ice(56), dark(58), 
        Drake(null, MobExp.STANDARD, PokemonAbility.Adaptability, PokemonAbility.Aftermath, 4130100, 5130100, 6130100, 6230600, 6230601),
        //sophilia doll(50), voodoo(60)
        Doll_V(null, MobExp.FAST, PokemonAbility.Contrary, PokemonAbility.LiquidOoze, 9400559, 9400561),
        //sophilia doll air(50), hoodoo(60)
        Doll_H(null, MobExp.FAST, PokemonAbility.Contrary, PokemonAbility.NoGuard, 9400560, 9400562),
        //root(53), sr.(96)
        Bellflower(null, MobExp.STANDARD, PokemonAbility.Overgrow, PokemonAbility.SapSipper, 9001023, 5120502),
        //cicle(54), cico(58)
        Sea(null, MobExp.ERRATIC, PokemonAbility.Torrent, PokemonAbility.WaterAbsorb, 2230105, 2230106),
        //propelly(54), helly(56), planey(57)
        Plane(null, MobExp.FAST, PokemonAbility.MotorDrive, PokemonAbility.Stall, 3230303, 3210206, 3230304),
        //big(50), krappi(56) -> bubblefish(60) -> flowerfish(61) -> krip(62) -> mask fish(64) -> poopa(70) -> goby(91) -> bone fish(95), shark warped(100), cold shark(107)
        Fish(null, MobExp.FAST, PokemonAbility.WaterVeil, PokemonAbility.Torrent, /*9600064,*/ 2230107, 2230109, 2230200, 3000006, 3230104, 4230200, 7130020, 8140600, 9300099, 8150101),
        //big(50), krappi(56) -> bubblefish(60) -> flowerfish(61) -> krip(62) -> mask fish(64) -> poison poopa(70) -> goby(91) -> bone fish(95), shark(105), cold shark(107)
        Fish_Poison(null, MobExp.FAST, PokemonAbility.WaterVeil, PokemonAbility.Torrent, /*9600064,*/ 2230107, 2230109, 2230200, 3000006, 3230104, 4230201, 7130020, 8140600, 8150100, 8150101),
        //scorpie(57), gold scorpie(58), scorpion(81)
        Scorpion(null, MobExp.STANDARD, PokemonAbility.ToxicBoost, PokemonAbility.PoisonTouch, 5160002, 5160003, 2110301),
        //decaying(57), miner(132), riche(135)
        Zombie(PokemonItem.Black_Diamond, MobExp.SLOW, PokemonAbility.ToxicBoost, PokemonAbility.PoisonTouch, 9001027, 5130108, 6090000),
        //snow witch(58), black witch(120)
        Black_Witch(PokemonItem.Black_Diamond, MobExp.FAST, PokemonAbility.Intimidate, PokemonAbility.IronBarbs, 6090001, 9001010),
        //hodori(58), hogul(60)
        Tiger(null, MobExp.EASY, PokemonAbility.Intimidate, PokemonAbility.HyperCutter, 5100003, 5100005),
        //lite(58), normal(62), snackbar(85)
        Coketump(PokemonItem.Coin, MobExp.ERRATIC, PokemonAbility.Regenerator, PokemonAbility.NaturalCure, 9500154, 9500153, 8220009),
        //experimental(58), frankenroid(77), angry(80)
        Frankenroid(PokemonItem.Black_Diamond, MobExp.STANDARD, PokemonAbility.Regenerator, PokemonAbility.Immunity, 9300154, 9300151, 9300152),
        //scaredy(59), jester(68), booper(82), scarlion(100, 140)
        Scarlion(PokemonItem.Maple_Marble, MobExp.STANDARD, PokemonAbility.Stall, PokemonAbility.PurePower, 9420531, 9420535, 9420538, 9420548),
        //rata(59), fros(72), viker(87), gallop(94), targa(100, 140)
        Targa(PokemonItem.Maple_Marble, MobExp.STANDARD, PokemonAbility.Stall, PokemonAbility.PurePower, 9420532, 9420536, 9420539, 9420540, 9420543),
        //tragos(59), kephivara(60), ferret(61), gold ferret(62), xerxes(63)
        Xerxes(PokemonItem.Ancient_Relic, MobExp.SLOW, PokemonAbility.Stall, PokemonAbility.Immunity, 5160005, 5160006, 5160004, 6160000, 6160003),
        //robo(61), master(63)
        Robo(null, MobExp.FAST, PokemonAbility.Stall, PokemonAbility.MotorDrive, 4230111, 4230112),
        //block (63), king block(65), rombot(68)
        Block_Golem(PokemonItem.Corrupted, MobExp.SLOW, PokemonAbility.PurePower, PokemonAbility.ShieldDust, 4230109, 4230110, 4130103),
        //block (63), king block(65), door lock(68)
        Block(PokemonItem.More_Corrupted, MobExp.SLOW, PokemonAbility.PurePower, PokemonAbility.ShieldDust, 4230109, 4230110, 9300390),
        //macis(64), spear(66)
        Tauro(null, MobExp.EASY, PokemonAbility.VoltAbsorb, PokemonAbility.Static, 7130100, 7130101),
        //barnard(65), zeta(67), ultra(70), chief(75), zeno(78)
        Gray(PokemonItem.Ancient_Relic, MobExp.FAST, PokemonAbility.RunAway, PokemonAbility.Adaptability, 4230116, 4230117, 4230118, 4240000, 6220001),
        //pachu(66), cuzco(69), puco(73), punco(76), opachu(85)
        Zakum(null, MobExp.FAST, PokemonAbility.AngerPoint, PokemonAbility.ClearBody, 6300004, 6400003, 6230101, 6300003, 6400004),
        //chaos pachu(66), cuzco(69), puco(73), punco(76), opachu(85)
        Chaos_Zakum(null, MobExp.SLOW, PokemonAbility.AngerPoint, PokemonAbility.ClearBody, 8800116, 8800114, 8800112, 8800113, 8800115),
        //guard robot(67), robot L(79), security(89), enhanced security(90), poison gas(120)
        Robot(PokemonItem.Ventilation, MobExp.STANDARD, PokemonAbility.MotorDrive, PokemonAbility.Regenerator, 6150000, 7150004, 8105001, 8105002, 9001035),
        //tick(68), ticktock(70), timer(75)
        Tick(PokemonItem.Pocket_Watch, MobExp.ERRATIC, PokemonAbility.Insomnia, PokemonAbility.EarlyBird, 3210207, 4230113, 5220003),
        //kru(68), kru(100), captain(105),
        Crew(null, MobExp.FAST, PokemonAbility.SheerForce, PokemonAbility.Scrappy, 9001030, 6130208, 7130104),
        //kru(68), lord pirate(68)
        Crew_Angry(PokemonItem.Summoning_Frame, MobExp.FAST, PokemonAbility.SheerForce, PokemonAbility.Scrappy, 9001030, 9300105),
        //jr(70), normal(73), royal(76), deo(80)
        Cactus(PokemonItem.Heart_of_Heart, MobExp.FAST, PokemonAbility.SapSipper, PokemonAbility.EffectSpore, 2100102, 2100103, 2100104, 3220001),
        //windraider(70), stormbreaker(80), firebrand(90), nightshadow(100), crimson guardian(120), azure(120)
        Keeper(PokemonItem.Rainbow_Leaf, MobExp.SLOW, PokemonAbility.Intimidate, PokemonAbility.Forewarn, 9400576, 9400581, 9400578, 9400579, 9400582, 9400596),
        //windraider(70), stormbreaker(80), firebrand(90), nightshadow(100), crimson guardian(120), scarlet(120)
        Keeper_2(PokemonItem.Rainbow_Leaf, MobExp.SLOW, PokemonAbility.Intimidate, PokemonAbility.Forewarn, 9400576, 9400581, 9400578, 9400579, 9400582, 9400597),
        //boar(70), rhino(71), ani(115)
        Ani(PokemonItem.Deathly_Fear, MobExp.SLOW, PokemonAbility.Intimidate, PokemonAbility.Gluttony, 8210006, 8210007, 8210010),
        //low star(71), bloody boom(75), high star(80)
        Boom(null, MobExp.FAST, PokemonAbility.ShadowTag, PokemonAbility.ShedSkin, 8500003, 8510100, 8500004),
        //ear plug(72), meerkat(76), scarf(77)
        Plead(null, MobExp.STANDARD, PokemonAbility.Gluttony, PokemonAbility.Guts, 2100106, 2100108, 2100107),
        //buffoon(74), lazy(106)
        Buffoon(null, MobExp.ERRATIC, PokemonAbility.Truant, PokemonAbility.HugePower, 6300100, 6400100),
        //timer(75), pap clock(90)
        Papulatus_Clock(PokemonItem.Pocket_Watch, MobExp.ERRATIC, PokemonAbility.Insomnia, PokemonAbility.EarlyBird, 5220003, 9500180),
        //timer(75), pap(90)
        Papulatus(PokemonItem.Pocket_Watch, MobExp.ERRATIC, PokemonAbility.Insomnia, PokemonAbility.EarlyBird, 5220003, 9500181),
        //chipmunk(76), red(77), black(79)
        Porky(null, MobExp.FAST, PokemonAbility.Normalize, PokemonAbility.Gluttony, 4230500, 4230501, 4230502),
        //sandrat(77), sanddwarf(84), desertgiant(88), 
        Sand(null, MobExp.ERRATIC, PokemonAbility.SheerForce, PokemonAbility.Intimidate, 2110300, 3100101, 4230600),
        //sandrat(77), darksanddwarf(84), desertgiant(88), 
        Dark_Sand(null, MobExp.ERRATIC, PokemonAbility.SheerForce, PokemonAbility.Intimidate, 2110300, 3110101, 4230600),
        //rumo(82), triple rumo(83), rurumo(84)
        Rumo(PokemonItem.Black_Diamond, MobExp.FAST, PokemonAbility.SapSipper, PokemonAbility.Overgrow, 3110302, 3110303, 6090004),
        //hector(83), combat(85), ferocious(86), rex(87)
        Hoblin_1(PokemonItem.Magical_Array, MobExp.ERRATIC, PokemonAbility.Intimidate, PokemonAbility.QuickFeet, 9300276, 9300279, 9300280, 9300281),
        //elite(83), combat(85), ferocious(86), rex(87)
        Hoblin_2(PokemonItem.Magical_Array, MobExp.ERRATIC, PokemonAbility.Intimidate, PokemonAbility.QuickFeet, 9300277, 9300279, 9300280, 9300281),
        //elitegreen(83), combat(85), ferocious(86), rex(87)
        Hoblin_3(PokemonItem.Magical_Array, MobExp.ERRATIC, PokemonAbility.Intimidate, PokemonAbility.QuickFeet, 9300278, 9300279, 9300280, 9300281),
        //iron(83), reinforced iron(84), mith(85), reinforced mith(89), chimera(102)
        Mutae(PokemonItem.Black_Diamond, MobExp.FAST, PokemonAbility.Multiscale, PokemonAbility.MagicGuard, 4110300, 4110301, 4110302, 5110300, 8220002),
        //roid(88), huroid(89), D.Roy(92), AF roid(93), AF broken roid (96), attacking android(120)
        Roid(null, MobExp.STANDARD, PokemonAbility.Regenerator, PokemonAbility.Immunity, 5110301, 5110302, 7110300, 8105003, 8105004, 9001034),
        //roid(88), huroid(89), D.Roy(92), AF roid(93), deet and roi(96)
        Roi(PokemonItem.Black_Diamond, MobExp.STANDARD, PokemonAbility.Regenerator, PokemonAbility.Immunity, 5110301, 5110302, 7110300, 8105003, 8090000),
        //reindeer(88), reindeer(118), blood(120)
        Reindeer(null, MobExp.STANDARD, PokemonAbility.Gluttony, PokemonAbility.Guts, 5120505, 8210001, 8210002),
        //pac pinky(88), selkie(90), slimy(93), anchor(98), latanica(100)
        Ghost(PokemonItem.Old_Glove, MobExp.FAST, PokemonAbility.Frisk, PokemonAbility.NoGuard, 9420509, 9420511, 9420510, 9420512, 9420513),
        //buffy(92), lazy(95)
        Buffy(null, MobExp.FAST, PokemonAbility.Truant, PokemonAbility.Moody, 6130200, 6230300),
        //homun(95), homunculus(98), homunscullo(100)
        Homun(null, MobExp.STANDARD, PokemonAbility.Scrappy, PokemonAbility.Filter, 6110300, 7110301, 8110300),
        //rash(97), dark rash(103)
        Rash(null, MobExp.FAST, PokemonAbility.BattleArmor, PokemonAbility.EarlyBird, 7130500, 7130501),
        //beetle(99), dual(105)
        Beetle(null, MobExp.FAST, PokemonAbility.BigPecks, PokemonAbility.BattleArmor, 7130002, 7130003),
        //scarlion(100, 140)
        Scarlion_Boss(PokemonItem.Maple_Marble, MobExp.STANDARD, PokemonAbility.Stall, PokemonAbility.PurePower, 9420548, 9420549),
        //targa(100, 140)
        Targa_Boss(PokemonItem.Maple_Marble, MobExp.STANDARD, PokemonAbility.Stall, PokemonAbility.PurePower, 9420543, 9420544),
        //green(100), green(136), dark(142)
        Cornian(null, MobExp.FRUSTRATING, PokemonAbility.HyperCutter, PokemonAbility.Defiant, 9500374, 8150200, 8150201),
        //hobi(101), green(103), hankie(107)
        Hobi(null, MobExp.STANDARD, PokemonAbility.OwnTempo, PokemonAbility.Limber, 7130600, 7130601, 7130004),
        //nex(102, 105, 110, 115, 118, 120)
        Gatekeeper_Nex(null, MobExp.IMPOSSIBLE, PokemonAbility.Intimidate, PokemonAbility.MagicGuard, 7120100, 7120101, 7120102, 8120100, 8120101, 8140510),
        //red(104), blue(137), dark(141)
        Wyvern(null, MobExp.SLOW, PokemonAbility.Intimidate, PokemonAbility.Defeatist, 8300002, 8150301, 8150302),
        //klock(105), dark klock(108)
        Klock(null, MobExp.STANDARD, PokemonAbility.EarlyBird, PokemonAbility.Insomnia, 8140100, 8140200),
        //ghost pirate(105), dual ghost pirate(114), viking(119), gig spirit viking(122)
        Viking(null, MobExp.SLOW, PokemonAbility.SuperLuck, PokemonAbility.Sniper, 7140000, 7160000, 8141000, 8141100),
        //overlord A(108), overlord B(110), afterlord(117), protolord(119), Oberon(122)
        Lord(PokemonItem.Black_Tornado, MobExp.FRUSTRATING, PokemonAbility.NaturalCure, PokemonAbility.Regenerator, 7120106, 7120107, 8120102, 8120103, 8220012),
        //birk(114), dual birk(116)
        Birk(null, MobExp.STANDARD, PokemonAbility.Synchronize, PokemonAbility.TintedLens, 8140110, 8140111),
        //iruvata(114), Aufheben(117)
        Road_Auf(PokemonItem.Cold_Heart, MobExp.IMPOSSIBLE, PokemonAbility.Immunity, PokemonAbility.MarvelScale, 7120109, 8220011),
        //iruvata(114), Dunas(117)
        Road_Dunas(PokemonItem.Whirlwind, MobExp.IMPOSSIBLE, PokemonAbility.Immunity, PokemonAbility.MarvelScale, 7120109, 8220010),
        //watch(119), grim(122), gatekeeper(126)
        PhantomGatekeeper(null, MobExp.FRUSTRATING, PokemonAbility.ShedSkin, PokemonAbility.ShadowTag, 8142000, 8143000, 8160000),
        //watch(119), grim(122), thanatos(126)
        PhantomThanatos(null, MobExp.FRUSTRATING, PokemonAbility.ShedSkin, PokemonAbility.ShadowTag, 8142000, 8143000, 8170000),
        //jr cerebes(123), cerebes(134), bain(136)
        Cerebes(null, MobExp.FRUSTRATING, PokemonAbility.RunAway, PokemonAbility.FlameBody, 4230108, 7130001, 8140500),
        //Imperial(125), Royal(127), Imperial(143)
        Guard(PokemonItem.Corrupted_Item, MobExp.IMPOSSIBLE, PokemonAbility.MagmaArmor, PokemonAbility.BattleArmor, 8140511, 8140512/*, 9400287*/),
        //boar(125), rhino(127), ani(128)
        Ani_Strong(PokemonItem.Deathly_Fear, MobExp.IMPOSSIBLE, PokemonAbility.Intimidate, PokemonAbility.Gluttony, 8840003, 8840004, 8210012),
        //boar(125), rhino(127), ani(120)
        Ani_Weak(PokemonItem.Deathly_Fear, MobExp.IMPOSSIBLE, PokemonAbility.Intimidate, PokemonAbility.Stall, 8840003, 8840004, 8210011),
        //boar(125), rhino(127), von leon(129)
        Von_Strong(PokemonItem.Deathly_Fear, MobExp.IMPOSSIBLE, PokemonAbility.Intimidate, PokemonAbility.Stall, 8840003, 8840004, 8840000),
        //blue(127), red(128)
        Turtle(null, MobExp.FRUSTRATING, PokemonAbility.Stall, PokemonAbility.Analytic, 8140700, 8140701),
        //rexton(131), brexton(133),
        Ton(null, MobExp.FRUSTRATING, PokemonAbility.Stall, PokemonAbility.Analytic, 8140702, 8140703),
        //Type A(121), Type S(123), Type D(124), Type A(141), Type Y(143)
        Maverick_Y(null, MobExp.FRUSTRATING, PokemonAbility.MotorDrive, PokemonAbility.MotorDrive, 8120104, 8120105, 8120106/*, 9400256, 9400257*/),
        //Type A(121), Type S(123), Type D(124), Type A(141), Type B(143)
        Maverick_B(null, MobExp.FRUSTRATING, PokemonAbility.MotorDrive, PokemonAbility.MotorDrive, 8120104, 8120105, 8120106/*, 9400256, 9400258*/),
        //Type A(121), Type S(123), Type D(124), Type A(141), Type V(143)
        Maverick_V(null, MobExp.FRUSTRATING, PokemonAbility.MotorDrive, PokemonAbility.MotorDrive, 8120104, 8120105, 8120106/*, 9400256, 9400259*/),
        //Type A(121), Type S(123), Type D(124), Type A(141), Type S(143)
        Maverick_S(null, MobExp.FRUSTRATING, PokemonAbility.MotorDrive, PokemonAbility.MotorDrive, 8120104, 8120105, 8120106/*, 9400256, 9400274*/),
        //memory monk(142), memory monk trainee(144), qualm monk(151), qualm monk trainee(153), oblivion monk(160), oblivion monk trainee(162) 
        Monk(null, MobExp.FRUSTRATING, PokemonAbility.SereneGrace, PokemonAbility.MagicGuard, 8200001, 8200002, 8200005, 8200006, 8200009, 8200010),
        //memory guardian(146), chief memory guardian(148), dodo(150)
        Dodo(PokemonItem.Maple_Marble, MobExp.IMPOSSIBLE, PokemonAbility.Truant, PokemonAbility.Stall, 8200003, 8200004, 8220004),
        //qualm guardian(155), chief qualm guardian(157), lilinof(159)
        Lillinof(PokemonItem.Rainbow_Leaf, MobExp.IMPOSSIBLE, PokemonAbility.Defeatist, PokemonAbility.Stall, 8200007, 8200008, 8220005),
        //oblivion guardian(164), chief oblivion guardian(166), raika(168)
        Raika(PokemonItem.Black_Hole, MobExp.IMPOSSIBLE, PokemonAbility.Defeatist, PokemonAbility.Stall, 8200011, 8200012, 8220006),
        //memory guardian(146), chief memory guardian(148), qualm guardian(155), chief qualm guardian(157), oblivion guardian(164), chief oblivion guardian(166)
        Guardian(null, MobExp.IMPOSSIBLE, PokemonAbility.Defeatist, PokemonAbility.Truant, 8200003, 8200004, 8200007, 8200008, 8200011, 8200012),
        //AHEAD ARE GM MOBS ONLY, they must be BOSSES
        //cygnus(180)
        Cygnus_Boss(null, MobExp.EASY, PokemonAbility.WonderGuard, PokemonAbility.WonderGuard, 8850011),
        //pink bean(180)
        Pink_Bean(null, MobExp.EASY, PokemonAbility.WonderGuard, PokemonAbility.WonderGuard, 8820001);
        //TODOO visitor mobs? silent crusade mobs, chinese statue things + old man boss, ximending/china bosses crows and stuff
        public PokemonItem evoItem;
        public MobExp type;
        public PokemonAbility ability1, ability2;
        public List<Integer> evolutions;

        private PokemonMob(PokemonItem evoItem, MobExp type, PokemonAbility ability1, PokemonAbility ability2, Integer... evo) {
            this.type = type;
            this.ability1 = ability1;
            this.ability2 = ability2;
            this.evoItem = evoItem;
            this.evolutions = Arrays.asList(evo);
        }
    }

    public static interface PItem {

        public int getItemChance();

        public int getId();
    }

    public static enum HoldItem implements PItem {

        Green_Star(3992010, 0.5, "Scope Lens - increases critical rate"),
        Orange_Star(3992012, 0.5, "Quick Claw - sometimes goes first"),
        King_Star(3992025, 0.5, "King Star - increases status rate"),
        Strange_Slush(3992011, 0.5, "Shell Bell - absorbs HP"),
        Maha_Charm(3994185, 0.5, "EXP Share - any monsters holding this will share EXP"),
        Question_Mark(3800088, 2.0, "Everstone - prevents your monster from evolution"),
        Mini_Dragon(3994187, 0.5, "Life Orb - more damage, but you get hurt"),
        Pheremone(4031507, 0.5, "Black Herb - cannot have any status"),
        Kenta_Report(4031509, 0.5, "White Herb - cannot have stats lowered"),
        Other_World_Key(4031409, 0.5, "Expert Key - super effective attacks do more damage"),
        Ripped_Note(4031252, 0.5, "Ripped Note - higher chance of increasing your own stats"),
        Herb_Pouch(4031555, 0.5, "Herb Pouch - higher chance of decreasing enemy stats"),
        Sea_Dust(4031251, 0.5, "Brightpowder - increases evasion rate"),
        Medal(4031160, 0.5, "Medal - increases damage of attacks of the same type"),
        Dark_Chocolate(4031110, 3.0, "Dark Chocolate - a not very effective attack to the opponent is negated, one time use"),
        White_Chocolate(4031109, 3.0, "White Chocolate - a super effective attack from the opponent is negated, one time use"),
        Red_Candy(4032444, 4.0, "Red Candy - when under 50% HP, upgrades attack, one time use"),
        Blue_Candy(4032445, 4.0, "Blue Candy - when under 50% HP, upgrades defense, one time use"),
        Green_Candy(4032446, 4.0, "Green Candy - when under 50% HP, upgrades speed, one time use"),
        Strawberry(4140102, 4.0, "Heal Berry - heals 10% HP when under 50% HP, one time use"),
        Pineapple(4140101, 4.0, "Cure Berry - heals status, one time use");
        public int id;
        public String customName;
        public double catchChance;

        private HoldItem(int id, double chance, String customName) {
            this.id = id;
            this.catchChance = chance;
            this.customName = customName;
        }

        public static HoldItem getPokemonItem(int itemId) {
            for (HoldItem i : HoldItem.values()) {
                if (i.id == itemId) {
                    return i;
                }
            }
            return null;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getItemChance() {
            return (int) (catchChance * 2.0);
        }
    }

    public static enum PokemonItem implements PItem {

        Basic_Ball(3992017, 1.0),
        Great_Ball(3992018, 1.5),
        Ultra_Ball(3992019, 2.0),
        Yellow_Crystal(4001268, 2.0),
        Blue_Crystal(4001269, 2.0),
        Black_Diamond(4001469, 2.0),
        Emerald_Crystal(4001468, 2.0),
        Saint_Stone(4020012, 1.0),
        Minidungeon_Crystal(4001391, 1.0),
        Ice_Pick(4310007, 2.0),
        Minidungeon_Diamond(4001392, 0.5),
        Bright_Feather(3994193, 1.0),
        Gold_Pick(4310001, 0.5),
        Coin(4310002, 1.0),
        Corrupted(4001237, 3.0),
        More_Corrupted(4001238, 2.0),
        Most_Corrupted(4001239, 1.0),
        Corrupted_Item(4001240, 2.0),
        Ancient_Relic(4001302, 2.0),
        Heart_of_Heart(4001244, 2.0),
        Phoenix_Egg(4001113, 1.0),
        Freezer_Egg(4001114, 1.0),
        Black_Hole(4001190, 0.5),
        Maple_Marble(4031456, 0.5),
        Rainbow_Leaf(4032733, 0.5),
        Courage_Piece(4032234, 0.5),
        Wisdom_Piece(4032235, 0.5),
        Accuracy_Piece(4032236, 0.5),
        Dexterity_Piece(4032237, 0.5),
        Freedom_Piece(4032238, 0.5),
        Intelligence_Document(4001192, 2.0),
        Water_Bottle(4001195, 2.0),
        Dragon_Heart(4031449, 1.0),
        Griffey_Wind(4031457, 1.0),
        Deathly_Fear(4031448, 0.5),
        Summoning_Frame(4031451, 1.0),
        Magical_Array(4031453, 0.5),
        Life_Root(4031461, 1.0),
        Black_Tornado(4031458, 1.0),
        Perfect_Pitch(4310000, 1.0),
        Cold_Heart(4031460, 1.0),
        Ventilation(4031462, 2.0),
        Old_Glove(4031465, 1.0),
        Pocket_Watch(4001393, 1.0),
        Melted_Chocolate(3994199, 1.0),
        Whirlwind(4031459, 1.0);
        public int id;
        public double catchChance;

        private PokemonItem(int id, double chance) {
            this.id = id;
            this.catchChance = chance;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getItemChance() {
            return (int) (catchChance * 2.0);
        }

        public static boolean isPokemonItem(int itemId) {
            return getPokemonItem(itemId) != null;
        }

        public static PItem getPokemonItem(int itemId) {
            for (PokemonItem i : PokemonItem.values()) {
                if (i.id == itemId) {
                    return i;
                }
            }
            return HoldItem.getPokemonItem(itemId);
        }
    }

    public static enum Turn {

        ATTACK, SWITCH, DISABLED, TRUANT; //run and ball are automatic
    }

    public static enum PokemonStat {

        ATK, DEF, SPATK, SPDEF, SPEED, EVA, ACC, HP, NONE;

        public static PokemonStat getRandom() {
            return PokemonStat.values()[Randomizer.nextInt(PokemonStat.values().length - 1)]; //not NONE
        }
    }

    public static enum PokemonNature {

        Bashful(PokemonStat.NONE, PokemonStat.NONE),
        Docile(PokemonStat.NONE, PokemonStat.NONE),
        Hardy(PokemonStat.NONE, PokemonStat.NONE),
        Quirky(PokemonStat.NONE, PokemonStat.NONE),
        Serious(PokemonStat.NONE, PokemonStat.NONE),
        Lonely(PokemonStat.ATK, PokemonStat.DEF),
        Adamant(PokemonStat.ATK, PokemonStat.SPATK),
        Naughty(PokemonStat.ATK, PokemonStat.SPDEF),
        Brave(PokemonStat.ATK, PokemonStat.SPEED),
        Bold(PokemonStat.DEF, PokemonStat.ATK),
        Impish(PokemonStat.DEF, PokemonStat.SPATK),
        Lax(PokemonStat.DEF, PokemonStat.SPDEF),
        Relaxed(PokemonStat.DEF, PokemonStat.SPEED),
        Modest(PokemonStat.SPATK, PokemonStat.ATK),
        Mild(PokemonStat.SPATK, PokemonStat.DEF),
        Rash(PokemonStat.SPATK, PokemonStat.SPDEF),
        Quiet(PokemonStat.SPATK, PokemonStat.SPEED),
        Calm(PokemonStat.SPDEF, PokemonStat.ATK),
        Gentle(PokemonStat.SPDEF, PokemonStat.DEF),
        Careful(PokemonStat.SPDEF, PokemonStat.SPATK),
        Sassy(PokemonStat.SPDEF, PokemonStat.SPEED),
        Timid(PokemonStat.SPEED, PokemonStat.ATK),
        Hasty(PokemonStat.SPEED, PokemonStat.DEF),
        Jolly(PokemonStat.SPEED, PokemonStat.SPATK),
        Naive(PokemonStat.SPEED, PokemonStat.SPDEF);
        public PokemonStat inc, dec;

        private PokemonNature(PokemonStat inc, PokemonStat dec) {
            this.inc = inc;
            this.dec = dec;
        }

        public static PokemonNature randomNature() {
            return PokemonNature.values()[Randomizer.nextInt(PokemonNature.values().length)];
        }
    }

    public enum PokemonElement { //where the element is the attacking type

        None(0, false, new int[0], new int[0], new int[0]),
        Fire(-1, true, new int[0], new int[]{-1, -5, -9, -10, -12}, new int[]{-2, -6, -8, -11, -14}),
        Ice(-2, true, new int[]{-12}, new int[]{-1, -2, -4, -9, -10}, new int[]{-7, -8, -13, -14}),
        Lightning(-3, true, new int[0], new int[]{-2, -5, -8, -11, -13}, new int[]{-6, -7, -9, -14}),
        Poison(-4, true, new int[]{-11, -14}, new int[]{-1, -2, -4, -6, -12}, new int[]{-3, -5, -7, -8, -9, -10}),
        Holy(-5, true, new int[]{-4}, new int[]{-5, -11}, new int[]{-6, -12, -13}),
        Dark(-6, true, new int[]{-5}, new int[]{-6, -9}, new int[]{-3, -7, -11}),
        Mammal(-7, false, new int[0], new int[]{-4, -10, -11}, new int[]{-8, -9, -12}),
        Plant(-8, false, new int[0], new int[]{-1, -4, -7, -8}, new int[]{-5, -6, -9, -14}),
        Fish(-9, false, new int[0], new int[]{-7, -9, -10}, new int[]{-1, -2, -8}),
        Reptile(-10, false, new int[0], new int[]{-4, -6, -7, -11, -13}, new int[]{-1, -3, -9}),
        Spirit(-11, true, new int[]{-15}, new int[]{-1, -2, -3, -5, -6, -7}, new int[]{-4, -11, -12}),
        Devil(-12, true, new int[0], new int[]{-8, -9, -10, -12, -14}, new int[]{-1, -2, -4, -5, -6}),
        Immortal(-13, false, new int[]{-13}, new int[]{-2, -7, -9, -10}, new int[]{-1, -4, -11, -12, -15}),
        Enchanted(-14, false, new int[0], new int[]{-2, -3, -5, -6, -8, -14}, new int[]{-4, -7, -13, -15}),
        Normal(-15, false, new int[]{-11}, new int[]{-13}, new int[0]);
        public int trueId;
        public boolean special;
        public Set<Integer> immune = new HashSet<Integer>(), notEffective = new HashSet<Integer>(), superEffective = new HashSet<Integer>();

        private PokemonElement(int trueId, boolean special, int[] immune, int[] notEffective, int[] superEffective) {
            this.special = special;
            this.trueId = trueId;
            for (int e : immune) {
                this.immune.add(e);
            }
            for (int e : notEffective) {
                this.notEffective.add(e);
            }
            for (int e : superEffective) {
                this.superEffective.add(e);
            }
        }

        public static PokemonElement getFromElement(Element c) {
            switch (c) {
                case FIRE:
                    return Fire;
                case ICE:
                    return Ice;
                case LIGHTING:
                    return Lightning;
                case POISON:
                    return Poison;
                case HOLY:
                    return Holy;
                case DARKNESS:
                    return Dark;
            }
            return None;
        }

        public static PokemonElement getById(int c) {
            switch (c) {
                case 1:
                    return Mammal;
                case 2:
                    return Plant;
                case 3:
                    return Fish;
                case 4:
                    return Reptile;
                case 5:
                    return Spirit;
                case 6:
                    return Devil;
                case 7:
                    return Immortal;
                case 8:
                    return Enchanted;
            }
            return None;
        }

        public double getEffectiveness(PokemonElement[] stats) {
            double ret = 1.0;
            for (int i = 0; i < stats.length; i++) {
                if (immune.contains(stats[i].trueId)) {
                    return 0.0;
                } else if (notEffective.contains(stats[i].trueId)) {
                    ret /= 2.0;
                } else if (superEffective.contains(stats[i].trueId)) {
                    ret *= 2.0;
                }
            }
            return ret;
        }
    }

    public static enum PokemonAbility {

        Adaptability("Powers moves of the same type"),
        Aftermath("Damages the foe when KOed"),
        Analytic("Powers moves when moving last"),
        AngerPoint("Raises Attack/Sp.Attack when taking a critical hit"),
        BadDreams("Hurts a foe if they are in darkness"),
        BattleArmor("Protects from critical attacks"),
        BigPecks("Protects Defense from lowering"),
        Blaze("Powers up fire type moves in a pinch"),
        ClearBody("Prevents stats from lowering"),
        Compoundeyes("Accuracy is increased"),
        Contrary("Inverts stat modifiers"),
        Defeatist("Halves Attack/Sp.Attack when below 50% HP"),
        Defiant("Raises Attack two stages when any stat is lowered"),
        DrySkin("Fire type moves are more effective, Fish type moves heal HP"),
        EarlyBird("Awakens quickly from darkness"),
        EffectSpore("Contact may paralyze, poison, or cause darkness"),
        Filter("Powers down super effective moves"),
        FlameBody("Contact may burn"),
        FlareBoost("Increases Sp.Attack to 1.5x when burned."),
        Forewarn("Tells of the opponent's type"),
        Frisk("Tells of the opponent's held item"),
        Gluttony("Uses one time items earlier"),
        Guts("Boosts Attack if there is a status problem"),
        Heatproof("Halves fire type moves effect"),
        HugePower("Doubled Attack stat"),
        Hustle("Doubled Attack stat, with lower accuracy"),
        HyperCutter("Prevents Attack from being lowered"),
        Illuminate("Raises likelihood of finding wild pokemon"),
        Immunity("Prevents poison"),
        Insomnia("Prevents darkness"),
        Intimidate("Lowers opponent's Attack"),
        IronBarbs("Damages opponent 1/8 HP on contact"),
        Klutz("Opponent can't use any held items"),
        Limber("Prevents paralysis"),
        LiquidOoze("Hurts foes when they try to absorb HP"),
        MagicGuard("Only hurt by attacks"),
        MagmaArmor("Prevents freezing"),
        MarvelScale("Boosts Defense when there is status"),
        Moody("Raises random stat two stages, lowers another"),
        MotorDrive("Raises Speed when hit by electricity"),
        Moxie("Raises Attack when KOing a monster"),
        Multiscale("When full HP, halves damage taken"),
        NaturalCure("All status healed when switching out"),
        NoGuard("Ensures hit"),
        Normalize("All moves become Normal type"),
        Overgrow("Powers up Plant in a pinch"),
        OwnTempo("Prevents Showdown status"),
        Pickpocket("Steals opponent's held item on contact"),
        PoisonHeal("Heals HP when poisoned"),
        PoisonPoint("Poisons foe on contact"),
        PoisonTouch("Poisons foe on attack, 20% chance"),
        PurePower("Raises power of Physical moves"),
        QuickFeet("Raises Speed in status problem"),
        Regenerator("Heals 1/3 HP when switching"),
        RunAway("Ensures escape from wild monsters"),
        SapSipper("Absorbs Plant moves"),
        Scrappy("All immunities do not apply"),
        SereneGrace("Boosts added effects"),
        ShadowTag("Prevents escape"),
        ShedSkin("Has a greater chance to heal status problems"),
        SheerForce("Increases power, but prevents extra effects"),
        ShieldDust("Blocks extra effects"),
        Sniper("Increased power of critical hits"),
        SpeedBoost("Increases Speed every turn"),
        Stall("Moves last"),
        Static("Paralysis on contact"),
        Stench("Lower chance of meeting wild monsters"),
        SuperLuck("Increased critical hit rate"),
        Synchronize("Opponent receives same status"),
        TangledFeet("Raises evasion when confused"),
        ThickFat("Resists Fire and Ice moves"),
        TintedLens("Powers up not very effective moves"),
        Torrent("Powers up Fish type moves in a pinch"),
        ToxicBoost("Attack 1.5x when poisoned"),
        Truant("Does nothing every second turn"),
        Unaware("Ignores stat changes by the foe"),
        Unburden("Raises Speed if any held item is used"),
        Unnerve("Prevents opposition from using one time use items"),
        VoltAbsorb("Heals HP when hit by electricity"),
        WaterAbsorb("Heals HP when hit by water"),
        WaterVeil("Prevents burning"),
        WeakArmor("Raises Speed, lowers Defense when hit"),
        WonderGuard("Only super effective moves hit");
        public String desc;

        private PokemonAbility(String desc) {
            this.desc = desc;
        }
    }

    public static long getPokemonCustomHP(int mobId, long def) {
        switch (mobId) {
            case 8840000:
            case 9400112:
            case 9400113:
                return def / 200;
            case 8300006:
            case 8300007:
            case 9420543:
            case 9420548:
            case 9400589:
            case 9400590:
            case 9400591:
            case 9400592:
            case 9400593:
            case 8850001:
            case 8850002:
            case 8850003:
            case 8850004:
            case 8850000:
                return def / 100;
            case 9300158:
            case 9300159:
            case 9420544:
            case 9420549:
            case 9400300:
                return def / 50;
            case 9400293:
                return def / 40;
            case 9400405:
            case 9400121:
            case 9300215:
            case 8840003:
            case 8840004:
                return def / 20;
            case 9001010:
            case 9400014:
            case 8210000:
            case 8210001:
            case 8210002:
            case 8210003:
            case 8210004:
            case 8210005:
            case 8210010:
            case 8210011:
            case 8210012:
            case 8840001:
            case 8840002:
            case 8840005:
            case 8600000:
            case 8600001:
            case 8600002:
            case 8600003:
            case 8600004:
            case 8600005:
            case 8600006:
            case 8610000:
            case 8610001:
            case 8610002:
            case 8610003:
            case 8610004:
            case 8610005:
            case 8610006:
            case 8610007:
            case 8610008:
            case 8610009:
            case 8610010:
            case 8610011:
            case 8610012:
            case 8610013:
            case 8610014:
                return def / 10;
            case 6090000:
                return 1350000;
            case 8220004:
                return 2350000;
            case 8220005:
                return 3200000;
            case 8220006:
                return 4100000;
        }
        return def;
    }
}
