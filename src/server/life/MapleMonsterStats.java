/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server.life;

import constants.GameConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import tools.Pair;

public class MapleMonsterStats {

    private byte cp, selfDestruction_action, tagColor, tagBgColor, rareItemDropLevel, HPDisplayType, summonType, PDRate, MDRate, category;
    private short level, charismaEXP;
    private long hp;
    private int id, exp, mp, removeAfter, buffToGive, fixedDamage, selfDestruction_hp, dropItemPeriod, point, eva, acc, PhysicalAttack, MagicAttack, speed, partyBonusR, pushed;
    private boolean boss, undead, ffaLoot, firstAttack, isExplosiveReward, mobile, fly, onlyNormalAttack, friendly, noDoom, invincible, partyBonusMob, changeable, escort;
    private String name, mobType;
    private EnumMap<Element, ElementalEffectiveness> resistance = new EnumMap<Element, ElementalEffectiveness>(Element.class);
    private List<Integer> revives = new ArrayList<Integer>();
    private List<Pair<Integer, Integer>> skills = new ArrayList<Pair<Integer, Integer>>();
    private List<MobAttackInfo> mai = new ArrayList<MobAttackInfo>();
    private BanishInfo banish;
	
	public MapleMonsterStats(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp;//(hp * 3L / 2L);
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public short getCharismaEXP() {
        return charismaEXP;
    }

    public void setCharismaEXP(short leve) {
        this.charismaEXP = leve;
    }

    public void setSelfD(byte selfDestruction_action) {
        this.selfDestruction_action = selfDestruction_action;
    }

    public byte getSelfD() {
        return selfDestruction_action;
    }

    public void setSelfDHP(int selfDestruction_hp) {
        this.selfDestruction_hp = selfDestruction_hp;
    }

    public int getSelfDHp() {
        return selfDestruction_hp;
    }

    public void setFixedDamage(int damage) {
        this.fixedDamage = damage;
    }

    public int getFixedDamage() {
        return fixedDamage;
    }

    public void setPushed(int damage) {
        this.pushed = damage;
    }

    public int getPushed() {
        return pushed;
    }

	
    public void setPhysicalAttack(final int PhysicalAttack) {
        this.PhysicalAttack = PhysicalAttack;
    }

    public int getPhysicalAttack() {
        return PhysicalAttack;
    }

    public final void setMagicAttack(final int MagicAttack) {
        this.MagicAttack = MagicAttack;
    }

    public final int getMagicAttack() {
        return MagicAttack;
    }

    public final void setEva(final int eva) {
        this.eva = eva;
    }

    public final int getEva() {
        return eva;
    }


    public final void setAcc(final int acc) {
        this.acc = acc;
    }

    public final int getAcc() {
        return acc;
    }

    public final void setSpeed(final int speed) {
        this.speed = speed;
    }

    public final int getSpeed() {
        return speed;
    }

    public final void setPartyBonusRate(final int speed) {
        this.partyBonusR = speed;
    }

    public final int getPartyBonusRate() {
        return partyBonusR;
    }

    public void setOnlyNormalAttack(boolean onlyNormalAttack) {
        this.onlyNormalAttack = onlyNormalAttack;
    }

    public boolean getOnlyNoramlAttack() {
        return onlyNormalAttack;
    }

    public BanishInfo getBanishInfo() {
        return banish;
    }

    public void setBanishInfo(BanishInfo banish) {
        this.banish = banish;
    }

    public int getRemoveAfter() {
        return removeAfter;
    }

    public void setRemoveAfter(int removeAfter) {
        this.removeAfter = removeAfter;
    }

    public byte getrareItemDropLevel() {
        return rareItemDropLevel;
    }

    public void setrareItemDropLevel(byte rareItemDropLevel) {
        this.rareItemDropLevel = rareItemDropLevel;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setFfaLoot(boolean ffaLoot) {
        this.ffaLoot = ffaLoot;
    }

    public boolean isFfaLoot() {
        return ffaLoot;
    }
	
    public void setEscort(boolean ffaL) {
        this.escort = ffaL;
    }

    public boolean isEscort() {
        return escort;
    }

    public void setExplosiveReward(boolean isExplosiveReward) {
        this.isExplosiveReward = isExplosiveReward;
    }

    public boolean isExplosiveReward() {
        return isExplosiveReward;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public boolean getMobile() {
        return mobile;
    }

    public void setFly(boolean fly) {
        this.fly = fly;
    }

    public boolean getFly() {
        return fly;
    }

    public List<Integer> getRevives() {
        return revives;
    }

    public void setRevives(List<Integer> revives) {
        this.revives = revives;
    }

    public void setUndead(boolean undead) {
        this.undead = undead;
    }

    public boolean getUndead() {
        return undead;
    }

    public void setSummonType(byte selfDestruction) {
        this.summonType = selfDestruction;
    }

    public byte getSummonType() {
        return summonType;
    }

    public void setCategory(byte selfDestruction) {
        this.category = selfDestruction;
    }

    public byte getCategory() {
        return category;
    }

    public void setPDRate(byte selfDestruction) {
        this.PDRate = selfDestruction;
    }

    public byte getPDRate() {
        return PDRate;
    }

    public void setMDRate(byte selfDestruction) {
        this.MDRate = selfDestruction;
    }

    public byte getMDRate() {
        return MDRate;
    }

    public EnumMap<Element, ElementalEffectiveness> getElements() {
	return resistance;
    }

    public void setEffectiveness(Element e, ElementalEffectiveness ee) {
        resistance.put(e, ee);
    }

    public void removeEffectiveness(Element e) {
        resistance.remove(e);
    }

    public ElementalEffectiveness getEffectiveness(Element e) {
        ElementalEffectiveness elementalEffectiveness = resistance.get(e);
        if (elementalEffectiveness == null) {
            return ElementalEffectiveness.NORMAL;
        } else {
            return elementalEffectiveness;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return mobType;
    }

    public void setType(String mobt) {
        this.mobType = mobt;
    }

    public byte getTagColor() {
        return tagColor;
    }

    public void setTagColor(int tagColor) {
        this.tagColor = (byte) tagColor;
    }

    public byte getTagBgColor() {
        return tagBgColor;
    }

    public void setTagBgColor(int tagBgColor) {
        this.tagBgColor = (byte) tagBgColor;
    }

    public void setSkills(List<Pair<Integer, Integer>> skill_) {
        for (Pair<Integer, Integer> skill : skill_) {
            skills.add(skill);
        }
    }

    public List<Pair<Integer, Integer>> getSkills() {
        return Collections.unmodifiableList(this.skills);
    }

    public byte getNoSkills() {
        return (byte) skills.size();
    }

    public boolean hasSkill(int skillId, int level) {
        for (Pair<Integer, Integer> skill : skills) {
            if (skill.getLeft() == skillId && skill.getRight() == level) {
                return true;
            }
        }
        return false;
    }

    public void setFirstAttack(boolean firstAttack) {
        this.firstAttack = firstAttack;
    }

    public boolean isFirstAttack() {
        return firstAttack;
    }

    public void setCP(byte cp) {
        this.cp = cp;
    }

    public byte getCP() {
        return cp;
    }

    public void setPoint(int cp) {
        this.point = cp;
    }

    public int getPoint() {
        return point;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void setInvincible(boolean invin) {
        this.invincible = invin;
    }

    public boolean isInvincible() {
        return invincible;
    }
	
    public void setChange(boolean invin) {
        this.changeable = invin;
    }

    public boolean isChangeable() {
        return changeable;
    }

    public void setPartyBonus(boolean invin) {
        this.partyBonusMob = invin;
    }

    public boolean isPartyBonus() {
        return partyBonusMob;
    }

    public void setNoDoom(boolean doom) {
        this.noDoom = doom;
    }

    public boolean isNoDoom() {
        return noDoom;
    }

    public void setBuffToGive(int buff) {
        this.buffToGive = buff;
    }

    public int getBuffToGive() {
        return buffToGive;
    }

    public byte getHPDisplayType() {
        return HPDisplayType;
    }

    public void setHPDisplayType(byte HPDisplayType) {
        this.HPDisplayType = HPDisplayType;
    }

    public int getDropItemPeriod() {
        return dropItemPeriod;
    }

    public void setDropItemPeriod(int d) {
        this.dropItemPeriod = d;
    }

    public void addMobAttack(MobAttackInfo ma) {
	this.mai.add(ma);
    }

    public MobAttackInfo getMobAttack(int attack) {
	if (attack >= this.mai.size() || attack < 0) {
	    return null;
	}
	return this.mai.get(attack);
    }

    public List<MobAttackInfo> getMobAttacks() {
	return this.mai;
    }
	
    public int dropsMeso() {
        if (getRemoveAfter() != 0 || isInvincible() || getOnlyNoramlAttack() || getDropItemPeriod() > 0 || getCP() > 0 || getPoint() > 0 || getFixedDamage() > 0 || getSelfD() != -1 || getPDRate() <= 0 || getMDRate() <= 0) {
            return 0;
        }
        //final String mt = stats.getMobType();
        //if (mt != null && mt.length() > 0 && mt.charAt(0) == '7') {
        //    return 0; //bosses; magatia pq
        //}
        final int mobId = getId() / 100000;
        if (GameConstants.getPartyPlayHP(getId()) > 0 || mobId == 97 || mobId == 95 || mobId == 93 || mobId == 91 || mobId == 90) {
            return 0;
        }
        if (isExplosiveReward()) {
            return 7;
        }
        if (isBoss()) {
            return 2;
        }
        return 1;
    }
}
