package server.quest;

import constants.GameConstants;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import client.MapleCharacter;
import client.MapleQuestStatus;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import scripting.NPCScriptManager;
import tools.Pair;
import tools.packet.CField.EffectPacket;

public class MapleQuest implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private static final Map<Integer, MapleQuest> quests = new LinkedHashMap<Integer, MapleQuest>();
    protected int id;
    protected final List<MapleQuestRequirement> startReqs = new LinkedList<MapleQuestRequirement>();
    protected final List<MapleQuestRequirement> completeReqs = new LinkedList<MapleQuestRequirement>();
    protected final List<MapleQuestAction> startActs = new LinkedList<MapleQuestAction>();
    protected final List<MapleQuestAction> completeActs = new LinkedList<MapleQuestAction>();
    protected final Map<String, List<Pair<String, Pair<String, Integer>>>> partyQuestInfo = new LinkedHashMap<String, List<Pair<String, Pair<String, Integer>>>>(); //[rank, [more/less/equal, [property, value]]]
    protected final Map<Integer, Integer> relevantMobs = new LinkedHashMap<Integer, Integer>();
    private boolean autoStart = false, autoPreComplete = false, repeatable = false, customend = false, blocked = false, autoAccept = false, autoComplete = false, scriptedStart = false;
    private int viewMedalItem = 0, selectedSkillID = 0;
    protected String name = "";

    protected MapleQuest(final int id) {
        this.id = id;
    }

    private static MapleQuest loadQuest(ResultSet rs, PreparedStatement psr, PreparedStatement psa, PreparedStatement pss, PreparedStatement psq, PreparedStatement psi, PreparedStatement psp) throws SQLException {
	final MapleQuest ret = new MapleQuest(rs.getInt("questid"));
	ret.name = rs.getString("name");
	ret.autoStart = rs.getInt("autoStart") > 0;
	ret.autoPreComplete = rs.getInt("autoPreComplete") > 0;
	ret.autoAccept = rs.getInt("autoAccept") > 0;
	ret.autoComplete = rs.getInt("autoComplete") > 0;
	ret.viewMedalItem = rs.getInt("viewMedalItem");
	ret.selectedSkillID = rs.getInt("selectedSkillID");
	ret.blocked = rs.getInt("blocked") > 0; //ult.explorer quests will dc as the item isn't there...

	psr.setInt(1, ret.id);
	ResultSet rse = psr.executeQuery();
	while (rse.next()) {
		final MapleQuestRequirementType type = MapleQuestRequirementType.getByWZName(rse.getString("name"));
		final MapleQuestRequirement req = new MapleQuestRequirement(ret, type, rse);
                if (type.equals(MapleQuestRequirementType.interval)) {
                        ret.repeatable = true;
		} else if (type.equals(MapleQuestRequirementType.normalAutoStart)) {
			ret.repeatable = true;
			ret.autoStart = true;
                } else if (type.equals(MapleQuestRequirementType.startscript)) {
                        ret.scriptedStart = true;
                } else if (type.equals(MapleQuestRequirementType.endscript)) {
                        ret.customend = true;
                } else if (type.equals(MapleQuestRequirementType.mob)) {
			for (Pair<Integer, Integer> mob : req.getDataStore()) {
				ret.relevantMobs.put(mob.left, mob.right);
			}
		}	
		if (rse.getInt("type") == 0) {
			ret.startReqs.add(req);
		} else {
			ret.completeReqs.add(req);
		}
	}
	rse.close();

	psa.setInt(1, ret.id);
	rse = psa.executeQuery();
	while (rse.next()) {
		final MapleQuestActionType ty = MapleQuestActionType.getByWZName(rse.getString("name"));
		if (rse.getInt("type") == 0) { //pass it over so it will set ID + type once done
			if (ty == MapleQuestActionType.item && ret.id == 7103) { //pap glitch
				continue;
			}
			ret.startActs.add(new MapleQuestAction(ty, rse, ret, pss, psq, psi));
		} else {
			if (ty == MapleQuestActionType.item && ret.id == 7102) { //pap glitch
				continue;
			}
			ret.completeActs.add(new MapleQuestAction(ty, rse, ret, pss, psq, psi));
		}
	}
	rse.close();

	psp.setInt(1, ret.id);
	rse = psp.executeQuery();
	while (rse.next()) {
		if (!ret.partyQuestInfo.containsKey(rse.getString("rank"))) {
			ret.partyQuestInfo.put(rse.getString("rank"), new ArrayList<Pair<String, Pair<String, Integer>>>());
		}
		ret.partyQuestInfo.get(rse.getString("rank")).add(new Pair<String, Pair<String, Integer>>(rse.getString("mode"), new Pair<String, Integer>(rse.getString("property"), rse.getInt("value"))));
        }
	rse.close();
        return ret;
    }

    public List<Pair<String, Pair<String, Integer>>> getInfoByRank(final String rank) {
        return partyQuestInfo.get(rank);
    }
	
	public boolean isPartyQuest() {
	     return partyQuestInfo.size() > 0;
	}

    public final int getSkillID() {
        return selectedSkillID;
    }

    public final String getName() {
        return name;
    }

    public final List<MapleQuestAction> getCompleteActs() {
	return completeActs;
    }

    public static void initQuests() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM wz_questdata");
	    PreparedStatement psr = con.prepareStatement("SELECT * FROM wz_questreqdata WHERE questid = ?");
	    PreparedStatement psa = con.prepareStatement("SELECT * FROM wz_questactdata WHERE questid = ?");
	    PreparedStatement pss = con.prepareStatement("SELECT * FROM wz_questactskilldata WHERE uniqueid = ?");
	    PreparedStatement psq = con.prepareStatement("SELECT * FROM wz_questactquestdata WHERE uniqueid = ?");
	    PreparedStatement psi = con.prepareStatement("SELECT * FROM wz_questactitemdata WHERE uniqueid = ?");
	    PreparedStatement psp = con.prepareStatement("SELECT * FROM wz_questpartydata WHERE questid = ?");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quests.put(rs.getInt("questid"), loadQuest(rs, psr, psa, pss, psq, psi, psp));
            }
            rs.close();
            ps.close();
	    psr.close();
	    psa.close();
	    pss.close();
	    psq.close();
	    psi.close();
	    psp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MapleQuest getInstance(int id) {
        MapleQuest ret = quests.get(id);
        if (ret == null) {
            ret = new MapleQuest(id);
            quests.put(id, ret); //by this time we have already initialized
        }
        return ret;
    }

    public static Collection<MapleQuest> getAllInstances() {
	return quests.values();
    }

    public boolean canStart(MapleCharacter c, Integer npcid) {
        if (c.getQuest(this).getStatus() != 0 && !(c.getQuest(this).getStatus() == 2 && repeatable)) {
            return false;
        }
	if (blocked && !c.isGM()) {
	    return false;
	}
	//if (autoAccept) {
	//    return true; //need script
	//}
        for (MapleQuestRequirement r : startReqs) {
	    if (r.getType() == MapleQuestRequirementType.dayByDay && npcid != null) { //everyday. we don't want ok
		forceComplete(c, npcid);
		return false;
	    }
            if (!r.check(c, npcid)) {
                return false;
            }
        }
        return true;
    }

    public boolean canComplete(MapleCharacter c, Integer npcid) {
        if (c.getQuest(this).getStatus() != 1) {
            return false;
        }
	if (blocked && !c.isGM()) {
	    return false;
	}
	if (autoComplete && npcid != null && viewMedalItem <= 0) {
	    forceComplete(c, npcid);
	    return false; //skip script
	}
        for (MapleQuestRequirement r : completeReqs) {
            if (!r.check(c, npcid)) {
                return false;
            }
        }
        return true;
    }

    public final void RestoreLostItem(final MapleCharacter c, final int itemid) {
	if (blocked && !c.isGM()) {
	    return;
	}
        for (final MapleQuestAction a : startActs) {
            if (a.RestoreLostItem(c, itemid)) {
                break;
            }
        }
    }

    public void start(MapleCharacter c, int npc) {
        if ((autoStart || checkNPCOnMap(c, npc)) && canStart(c, npc)) {
            for (MapleQuestAction a : startActs) {
                if (!a.checkEnd(c, null)) { //just in case
                    return;
                }
            }
            for (MapleQuestAction a : startActs) {
                a.runStart(c, null);
            }
            if (!customend) {
                forceStart(c, npc, null);
            } else {
                NPCScriptManager.getInstance().endQuest(c.getClient(), npc, getId(), true);
            }
        }
    }

    public void complete(MapleCharacter c, int npc) {
        complete(c, npc, null);
    }

    public void complete(MapleCharacter c, int npc, Integer selection) {
        if (c.getMap() != null && (autoPreComplete || checkNPCOnMap(c, npc)) && canComplete(c, npc)) {
            for (MapleQuestAction a : completeActs) {
                if (!a.checkEnd(c, selection)) {
                    return;
                }
            }
            forceComplete(c, npc);
            for (MapleQuestAction a : completeActs) {
                a.runEnd(c, selection);
            }
            // we save forfeits only for logging purposes, they shouldn't matter anymore
            // completion time is set by the constructor

            c.getClient().getSession().write(EffectPacket.showForeignEffect(12)); // Quest completion
            c.getMap().broadcastMessage(c, EffectPacket.showForeignEffect(c.getId(), 12), false);
        }
    }

    public void forfeit(MapleCharacter c) {
        if (c.getQuest(this).getStatus() != (byte) 1) {
            return;
        }
        final MapleQuestStatus oldStatus = c.getQuest(this);
        final MapleQuestStatus newStatus = new MapleQuestStatus(this, (byte) 0);
        newStatus.setForfeited(oldStatus.getForfeited() + 1);
        newStatus.setCompletionTime(oldStatus.getCompletionTime());
        c.updateQuest(newStatus);
    }

    public void forceStart(MapleCharacter c, int npc, String customData) {
        final MapleQuestStatus newStatus = new MapleQuestStatus(this, (byte) 1, npc);
        newStatus.setForfeited(c.getQuest(this).getForfeited());
        newStatus.setCompletionTime(c.getQuest(this).getCompletionTime());
        newStatus.setCustomData(customData);
        c.updateQuest(newStatus);
    }

    public void forceComplete(MapleCharacter c, int npc) {
        final MapleQuestStatus newStatus = new MapleQuestStatus(this, (byte) 2, npc);
        newStatus.setForfeited(c.getQuest(this).getForfeited());
        c.updateQuest(newStatus);
    }

    public int getId() {
        return id;
    }

    public Map<Integer, Integer> getRelevantMobs() {
        return relevantMobs;
    }

    private boolean checkNPCOnMap(MapleCharacter player, int npcid) {
        //mir = 1013000
        return (GameConstants.isEvan(player.getJob()) && npcid == 1013000) || npcid == 9000040 || npcid == 9000066 || (player.getMap() != null && player.getMap().containsNPC(npcid));
    }

    public int getMedalItem() {
        return viewMedalItem;
    }

    public boolean isBlocked() {
	return blocked;
    }

    public static enum MedalQuest {

        Beginner(29005, 29015, 15, new int[]{100000000, 100020400, 100040000, 101000000, 101020300, 101040300, 102000000, 102020500, 102030400, 102040200, 103000000, 103020200, 103030400, 103040000, 104000000, 104020000, 106020100, 120000000, 120020400, 120030000}),
        ElNath(29006, 29012, 50, new int[]{200000000, 200010100, 200010300, 200080000, 200080100, 211000000, 211030000, 211040300, 211041200, 211041800}),
        LudusLake(29007, 29012, 40, new int[]{222000000, 222010400, 222020000, 220000000, 220020300, 220040200, 221020701, 221000000, 221030600, 221040400}),
        Underwater(29008, 29012, 40, new int[]{230000000, 230010400, 230010200, 230010201, 230020000, 230020201, 230030100, 230040000, 230040200, 230040400}),
        MuLung(29009, 29012, 50, new int[]{251000000, 251010200, 251010402, 251010500, 250010500, 250010504, 250000000, 250010300, 250010304, 250020300}),
        NihalDesert(29010, 29012, 70, new int[]{261030000, 261020401, 261020000, 261010100, 261000000, 260020700, 260020300, 260000000, 260010600, 260010300}),
        MinarForest(29011, 29012, 70, new int[]{240000000, 240010200, 240010800, 240020401, 240020101, 240030000, 240040400, 240040511, 240040521, 240050000}),
        Sleepywood(29014, 29015, 50, new int[]{105000000, 105000000, 105010100, 105020100, 105020300, 105030000, 105030100, 105030300, 105030500, 105030500}); //repeated map
        public int questid, level, lquestid;
        public int[] maps;

        private MedalQuest(int questid, int lquestid, int level, int[] maps) {
            this.questid = questid; //infoquest = questid -2005, customdata = questid -1995
            this.level = level;
            this.lquestid = lquestid;
            this.maps = maps; //note # of maps
        }
    }

    public boolean hasStartScript() {
	return scriptedStart;
    }

    public boolean hasEndScript() {
	return customend;
    }
}
