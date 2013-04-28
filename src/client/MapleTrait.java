package client;

import constants.GameConstants;
import tools.packet.CWvsContext.InfoPacket;

public class MapleTrait {

    public static enum MapleTraitType { //in order

        charisma(500, MapleStat.CHARISMA), //ambition
        insight(500, MapleStat.INSIGHT),
        will(500, MapleStat.WILL), //willpower
        craft(500, MapleStat.CRAFT), //diligence
        sense(500, MapleStat.SENSE), //empathy
        charm(5000, MapleStat.CHARM);
        final int limit;
        final MapleStat stat;

        private MapleTraitType(int type, MapleStat theStat) {
            this.limit = type;
            this.stat = theStat;
        }

        public int getLimit() {
            return limit;
        }

        public MapleStat getStat() {
            return stat;
        }

        public static MapleTraitType getByQuestName(String q) {
            String qq = q.substring(0, q.length() - 3); //e.g. charmEXP, charmMin
            for (MapleTraitType t : MapleTraitType.values()) {
                if (t.name().equals(qq)) {
                    return t;
                }
            }
            return null;
        }
    }
    private MapleTraitType type;
    private int totalExp = 0, localTotalExp = 0;
    private short exp = 0;
    private byte level = 0;

    public MapleTrait(MapleTraitType t) {
        this.type = t;
    }

    public void setExp(int e) {
        this.totalExp = e;
		this.localTotalExp = e;
        recalcLevel();
    }

    public void addExp(int e) {
        this.totalExp += e;
		this.localTotalExp += e;
        if (e != 0) {
            recalcLevel();
        }
    }

    public void addExp(int e, MapleCharacter c) {
        addTrueExp(e * c.getClient().getChannelServer().getTraitRate(), c);
    }
	
    public void addTrueExp(int e, MapleCharacter c) {
        if (e != 0) {
			this.totalExp += e;
			this.localTotalExp += e;
			c.updateSingleStat(type.stat, totalExp);
			c.getClient().getSession().write(InfoPacket.showTraitGain(type, e));
            recalcLevel();
        }
    }

    public boolean recalcLevel() {
        if (totalExp < 0) {
            totalExp = 0;
			localTotalExp = 0;
            level = 0;
            exp = 0;
            return false;
        }
        final int oldLevel = level;
        for (byte i = 0; i < 100; i++) {
            if (GameConstants.getTraitExpNeededForLevel(i) > localTotalExp) {
                exp = (short) (GameConstants.getTraitExpNeededForLevel(i) - localTotalExp);
                level = (byte) (i - 1);
                return level > oldLevel;
            }
        }
        exp = 0;
        level = 100;
        totalExp = GameConstants.getTraitExpNeededForLevel(level);
		localTotalExp = totalExp;
        return level > oldLevel;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getTotalExp() {
        return totalExp;
    }
	
	public int getLocalTotalExp() {
		return localTotalExp;
	}
	
	public void addLocalExp(int e) {
		this.localTotalExp += e;
	}
	
	public void clearLocalExp() {
		this.localTotalExp = totalExp;
	}

    public MapleTraitType getType() {
        return type;
    }
}
