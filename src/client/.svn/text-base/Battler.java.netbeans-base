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
package client;

import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.BattleConstants;
import constants.BattleConstants.Evolution;
import constants.BattleConstants.PokemonElement;
import constants.BattleConstants.PokemonMob;
import constants.BattleConstants.HoldItem;
import constants.BattleConstants.PokemonAbility;
import constants.BattleConstants.PokemonNature;
import constants.BattleConstants.PokemonStat;
import constants.GameConstants;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;
import server.Randomizer;
import server.life.Element;
import server.life.ElementalEffectiveness;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.maps.MapleMap;
import tools.Pair;
import tools.StringUtil;
import tools.packet.MobPacket;

public class Battler implements Serializable {

    private static final long serialVersionUID = 7179541993413738569L;
    private int level, exp, charId, monsterId;
    private byte gender, abilityIndex;
    private String name;
    private PokemonMob family;
    private transient MapleMonsterStats stats;
    private PokemonElement[] elements = new PokemonElement[] {PokemonElement.None, PokemonElement.None};
    private PokemonNature nature;
    private PokemonAbility ability;
    private HoldItem item;
    private EnumMap<PokemonStat, Pair<Byte, Double>> mods = new EnumMap<PokemonStat, Pair<Byte, Double>>(PokemonStat.class);
    //temp stats
    private long hp;
    private transient MonsterStatusEffect status; //only one, like in pokemon.
    private transient WeakReference<MapleMonster> mons;
    private int statusTurnsLeft, tempLevel;
    private List<Integer> damagedChars = new ArrayList<Integer>(); //distribute exp according

    public Battler(int level, int exp, int charId, int monsterId, String name, PokemonNature nature, int itemId, byte gender, byte hpIV, byte atkIV, byte defIV, byte spatkIV, byte spdefIV, byte speedIV, byte evaIV, byte accIV, byte ability) {
	if (level > 200) {
	    level = 200;
	}
        this.level = level;
	this.nature = nature;
        this.exp = exp;
        this.charId = charId;
        this.monsterId = monsterId;
        this.name = name;
	setStats();
	this.item = HoldItem.getPokemonItem(itemId);
	if (gender < 0) {
	    gender = (byte)(Randomizer.nextInt(2) + 1);
	}
	if (hpIV < 0) {
	    hpIV = (byte)Randomizer.nextInt(101);
	}
	if (atkIV < 0) {
	    atkIV = (byte)Randomizer.nextInt(101);
	}
	if (defIV < 0) {
	    defIV = (byte)Randomizer.nextInt(101);
	}
	if (spatkIV < 0) {
	    spatkIV = (byte)Randomizer.nextInt(101);
	}
	if (spdefIV < 0) {
	    spdefIV = (byte)Randomizer.nextInt(101);
	}
	if (speedIV < 0) {
	    speedIV = (byte)Randomizer.nextInt(101);
	}
	if (evaIV < 0) {
	    evaIV = (byte)Randomizer.nextInt(101);
	}
	if (accIV < 0) {
	    accIV = (byte)Randomizer.nextInt(101);
	}
	if (ability < 0) {
	    ability = (byte)Randomizer.nextInt(2);
	}
	this.gender = gender;
	for (PokemonStat stat : PokemonStat.values()) {
	    byte theIV = 50;
	    switch(stat) {
		case ATK:
		    theIV = atkIV;
		    break;
		case DEF:
		    theIV = defIV;
		    break;
		case SPATK:
		    theIV = spatkIV;
		    break;
		case SPDEF:
		    theIV = spdefIV;
		    break;
		case SPEED:
		    theIV = speedIV;
		    break;
		case EVA:
		    theIV = evaIV;
		    break;
		case ACC:
		    theIV = accIV;
		    break;
		case HP:
		    theIV = hpIV;
		    break;
	    }
	    mods.put(stat, new Pair<Byte, Double>(theIV, 1.0));
	}
	this.abilityIndex = ability;
        calculateFamily();
    }
	
    public Battler(MapleMonsterStats stats) {
        this.level = stats.getLevel();
        this.exp = 0;
        this.charId = 0;
        this.monsterId = stats.getId();
        this.name = stats.getName();
        this.stats = stats;
	for (PokemonStat stat : PokemonStat.values()) {
	    mods.put(stat, new Pair<Byte, Double>((byte)Randomizer.nextInt(101), 1.0));
	}
		this.abilityIndex = (byte)Randomizer.nextInt(2);
		this.gender = (byte)(Randomizer.nextInt(2) + 1);
		this.nature = PokemonNature.randomNature();
        calculateFamily();
	}

    public Battler(MapleMonster stats) {
        this(stats.getStats());
        this.mons = new WeakReference<MapleMonster>(stats);

    }

    public final void setStats() {
        this.stats = MapleLifeFactory.getMonsterStats(monsterId);
    }

    public final byte getGender() {
	return gender;
    } 

    public final byte getAbilityIndex() {
	return abilityIndex;
    } 

    public final PokemonAbility getAbility() {
	return ability;
    }

    public final byte getIV(PokemonStat stat) {
	return mods.get(stat).left;
    }

    public final void resetNature() {
	this.nature = PokemonNature.Bashful;
    }

    public final long calcHP() {
	final long ourHp = BattleConstants.getPokemonCustomHP(monsterId, stats.getHp());
        return (long)((ourHp + Math.round(ourHp * (getLevel() - stats.getLevel()) / 50.0)) * getMod(PokemonStat.HP));
    }

    public final long calcBaseHP() {
        return (long)((stats.getHp() + Math.round(stats.getHp() * (getLevel() - stats.getLevel()) / 50.0)));
    }

    public int getHPPercent() {
        return (int) Math.ceil((hp * 100.0) / calcHP());
    }

    public final long getCurrentHP() {
        return hp;
    }

    public final String getGenderString() {
	return (gender == 2 ? "Female" : (gender == 1 ? "Male" : ""));
    }

    public final String getStatusString() {
	if (hp <= 0) {
            return "FAINTED";
        } else if (status == null) {
            return "NONE";
        }
        return StringUtil.makeEnumHumanReadable(status.getStati().name()).toUpperCase() + " for " + statusTurnsLeft + " turns";
    }

    public final String getItemString() {
	if (item == null || item.customName == null) {
            return "None";
        }
        return item.customName;
    }

    public final String getAbilityString() {
	if (ability == null) {
            return "None";
        }
        return StringUtil.makeEnumHumanReadable(ability.name()) + " - " + ability.desc;
    }

    public final void setStatus(MonsterStatusEffect mse) {
	final MonsterStatus stat = mse.getStati();
	if (ability == PokemonAbility.Immunity && stat == MonsterStatus.POISON) {
	    return;
	}
	if (ability == PokemonAbility.Insomnia && stat == MonsterStatus.DARKNESS) {
	    return;
	}
	if (ability == PokemonAbility.Limber && stat == MonsterStatus.SHADOW_WEB) {
	    return;
	}
	if (ability == PokemonAbility.MagmaArmor && stat == MonsterStatus.FREEZE) {
	    return;
	}
	if (ability == PokemonAbility.OwnTempo && stat == MonsterStatus.SHOWDOWN) {
	    return;
	}
	if (ability == PokemonAbility.WaterVeil && stat == MonsterStatus.BURN) {
	    return;
	}
	if (this.status != null) {
	    return;
	}
        this.status = mse;
        this.statusTurnsLeft = Randomizer.nextInt(3) + 2; //- 1, so really 1-3 turns
        getMonster().applyStatus(mse);
	if (ability == PokemonAbility.Guts) {
	    for (int zz = 0; zz < getStatusTurns() - 1; zz++) {
	        setMod(PokemonStat.ATK, increaseMod(getMod(PokemonStat.ATK)));
	    }
	} else if (ability == PokemonAbility.MarvelScale) {
	    for (int zz = 0; zz < getStatusTurns() - 1; zz++) {
	        setMod(PokemonStat.DEF, increaseMod(getMod(PokemonStat.DEF)));
	    }
	} else if (ability == PokemonAbility.QuickFeet) {
	    for (int zz = 0; zz < getStatusTurns() - 1; zz++) {
	        setMod(PokemonStat.SPEED, increaseMod(getMod(PokemonStat.SPEED)));
	    }
	} else if (stat == MonsterStatus.SPEED || stat == MonsterStatus.SHADOW_WEB) {
	    for (int zz = 0; zz < getStatusTurns() - 1; zz++) {
                setMod(PokemonStat.SPEED, decreaseMod(getMod(PokemonStat.SPEED)));
	    }
	} else if (stat == MonsterStatus.BURN) {
	    for (int zz = 0; zz < getStatusTurns() - 1; zz++) {
	        setMod(PokemonStat.ATK, decreaseMod(getMod(PokemonStat.ATK)));
	    }
	    if (ability == PokemonAbility.FlareBoost) {
	        setMod(PokemonStat.SPATK, increaseMod(getMod(PokemonStat.SPATK)));
	    }
	} else if (stat == MonsterStatus.IMPRINT) {
	    for (int zz = 0; zz < getStatusTurns() - 1; zz++) {
	        setMod(PokemonStat.SPATK, decreaseMod(getMod(PokemonStat.SPATK)));
	    }
	} else if (stat == MonsterStatus.SHOWDOWN) {
	    for (int zz = 0; zz < getStatusTurns() - 1; zz++) {
	        setMod(PokemonStat.ATK, increaseMod(getMod(PokemonStat.ATK)));
	        setMod(PokemonStat.SPATK, increaseMod(getMod(PokemonStat.SPATK)));
	        setMod(PokemonStat.DEF, decreaseMod(getMod(PokemonStat.DEF)));
	        setMod(PokemonStat.SPDEF, decreaseMod(getMod(PokemonStat.SPDEF)));
	    }
	    if (ability == PokemonAbility.TangledFeet) {
	        setMod(PokemonStat.SPEED, increaseMod(getMod(PokemonStat.SPEED)));
	    }
	} else if (stat == MonsterStatus.POISON) {
	    if (ability == PokemonAbility.Unaware) {
	        setMod(PokemonStat.ATK, increaseMod(getMod(PokemonStat.ATK)));
	    }
        }
    }

    public final MonsterStatusEffect getCurrentStatus() {
        return status;
    }

    public final void decreaseStatusTurns() {
        if (status == null) {
            return;
        }
        this.statusTurnsLeft--;
        if (statusTurnsLeft <= 0) {
            getMonster().cancelStatus(status.getStati());
            status = null;
        }
    }

    public final int getStatusTurns() {
        return statusTurnsLeft;
    }

    public final MapleMonster getMonster() {
        return mons.get();
    }

    public final void setMonster(MapleMonster mons) {
        this.mons = new WeakReference<MapleMonster>(mons);
    }

    public void setCharacterId(int cc) {
	this.charId = cc;
    }

    public HoldItem getItem() {
	return item;
    }

    public void setItem(int t) {
	this.item = HoldItem.getPokemonItem(t);
    }

    public final String getName() {
        return name == null ? getOriginalName() : name;
    }

    public final int getCharacterId() {
        return charId;
    }

    public final int getMonsterId() {
        return monsterId;
    }

    public final int getExp() {
        return exp;
    }

    public final String getExpString() {
	return StringUtil.makeEnumHumanReadable(family.type.name());
    }

    public final int getNextExp() {
	if (level >= 200) {
	    return 0;
	}
        return (int) Math.ceil(family.type.value + (family.type.value * level / 10.0)) * level * level;
    }

    public final Evolution getEvolutionType() {
        for (int i = 0; i < family.evolutions.size(); i++) {
            if (monsterId == family.evolutions.get(i)) {
                if (i == (family.evolutions.size() - 1)) { //last in the evolution
                    return Evolution.NONE;
                } else if (i != (family.evolutions.size() - 2)) {
                    return Evolution.LEVEL;
                } else {
                    if (family.evoItem != null) {
			MapleMonster theMob = MapleLifeFactory.getMonster(family.evolutions.get(family.evolutions.size() - 1));
			if (level > (theMob.getStats().getLevel() - 5)) {
			    return Evolution.STONE; //must be anywhere near the level and then use a stone (dont want level 40 bigfoots jumping around)
			}
		    }
		    return Evolution.LEVEL;
                }
            }
        }
        return Evolution.NONE;
    }

    public final MapleMonsterStats getStats() {
        return stats;
    }

    public final double increaseMod(double mod) {
	return ability == PokemonAbility.Contrary ? decreaseM(mod) : increaseM(mod);
    }

    public final double increaseM(double mod) {
        if (mod == 3.5) {
            return 4.0;
        }
        if (mod == 3.0) {
            return 3.5;
        }
        if (mod == 2.5) {
            return 3.0;
        }
        if (mod == 2.0) {
            return 2.5;
        }
        if (mod == 1.5) {
            return 2.0;
        }
        if (mod == 1.0) {
            return 1.5;
        }
        if (mod == 0.66) {
            return 1.0;
        }
        if (mod == 0.5) {
            return 0.66;
        }
        if (mod == 0.4) {
            return 0.5;
        }
        if (mod == 0.33) {
            return 0.4;
        }
        if (mod == 0.285) {
            return 0.33;
        }
        if (mod == 0.25) {
            return 0.285;
        }
        return mod;
    }

    public final double decreaseMod(double mod) {
	return ability == PokemonAbility.Contrary ? increaseM(mod) : decreaseM(mod);
    }

    private final double decreaseM(double mod) {
        if (mod == 4.0) {
            return 3.5;
        }
        if (mod == 3.5) {
            return 3.0;
        }
        if (mod == 3.0) {
            return 2.5;
        }
        if (mod == 2.5) {
            return 2.0;
        }
        if (mod == 2.0) {
            return 1.5;
        }
        if (mod == 1.5) {
            return 1.0;
        }
        if (mod == 1.0) {
            return 0.66;
        }
        if (mod == 0.66) {
            return 0.5;
        }
        if (mod == 0.5) {
            return 0.4;
        }
        if (mod == 0.4) {
            return 0.33;
        }
        if (mod == 0.33) {
            return 0.285;
        }
        if (mod == 0.285) {
            return 0.25;
        }
        return mod;
    }

    public final PokemonNature getNature() {
	return nature;
    }

    public final void setMod(PokemonStat stat, double mod) {
        mods.get(stat).right = mod;
    }

    public final int getEVA() {
        return (int) Math.round(((int)stats.getEva() + getLevel()) * getMod(PokemonStat.EVA) * (item == HoldItem.Sea_Dust ? 1.2 : 1.0)); //just like after big bang, too high level = bad evasion
    }

    public final int getACC() {
        return (int) Math.round(((int)stats.getAcc() + getLevel()) * getMod(PokemonStat.ACC) * (ability == PokemonAbility.Compoundeyes ? 1.3 : (ability == PokemonAbility.Hustle ? 0.7 : 1.0))); //just like after big bang, too high level = bad evasion
    }

    public final int getATK(int atk) {
        return (int) Math.round(((int)(atk <= 0 || stats.getMobAttack(atk) == null || stats.getMobAttack(atk).PADamage <= 0 ? stats.getPhysicalAttack() : stats.getMobAttack(atk).PADamage) + (int)(getLevel() - stats.getLevel()) * 5) * getMod(PokemonStat.ATK) * (ability == PokemonAbility.Defeatist && getHPPercent() <= 50 ? 0.5 : (ability == PokemonAbility.HugePower || ability == PokemonAbility.Hustle ? 2.0 : (ability == PokemonAbility.PurePower ? 1.5 : 1.0))));
    }

    public final int getSpATK(int atk) {
        return (int) Math.round(((int)(atk <= 0 || stats.getMobAttack(atk) == null || stats.getMobAttack(atk).MADamage <= 0 ? stats.getMagicAttack() : stats.getMobAttack(atk).MADamage) + (int)(getLevel() - stats.getLevel()) * 5) * getMod(PokemonStat.SPATK) * (ability == PokemonAbility.Defeatist && getHPPercent() <= 50 ? 0.5 : 1.0));
    }

    public final int getDEF() {
        return (int) Math.round((int)stats.getPDRate() * getMod(PokemonStat.DEF));
    }

    public final int getSpDEF() {
        return (int) Math.round((int)stats.getMDRate() * getMod(PokemonStat.SPDEF));
    }

    public final int getSpeed() {
        return (int) Math.round(((int)stats.getSpeed() + (ability == PokemonAbility.Stall ? 0 : 100) + (int)(getLevel() - stats.getLevel()) / 2) * (getMod(PokemonStat.SPEED)));
    }

    public double getMod(PokemonStat stat) {
	return mods.get(stat).right * ((mods.get(stat).left / 250.0) + 0.8) * (nature.inc == stat ? 1.1 : 1.0) * (nature.dec == stat ? 0.9 : 1.0);
    }

    public int getAverageIV() {
	int total = 0, num = 0;
	for (Entry<PokemonStat, Pair<Byte, Double>> stat : mods.entrySet()) {
	    if (stat.getKey() != PokemonStat.NONE) {
		total += stat.getValue().left;
		num++;
	    }
	}
	return total / num;
    }

    public String getIVString() {
	final StringBuilder ss = new StringBuilder();
	for (Entry<PokemonStat, Pair<Byte, Double>> stat : mods.entrySet()) {
	    if (stat.getKey() != PokemonStat.NONE) {
		ss.append("#b#e").append(StringUtil.makeEnumHumanReadable(stat.getKey().name()).toUpperCase()).append("#n#k - ").append(getIVString(stat.getValue().left)).append("\r\n");
	    }
	}
	return ss.append("#b#e").append("OVERALL").append("#n#k - ").append(getIVString_Average(getAverageIV())).toString();
    }

    public String getIVString(int avg) {
	if (avg >= 90) {
	    return "This stat is absolutely flawless!";
	} else if (avg >= 80) {
	    return "This stat is amazing, outstanding even.";
	} else if (avg >= 70) {
	    return "This stat is pretty good.";
	} else if (avg >= 60) {
	    return "This stat is just above average.";
	} else if (avg >= 50) {
	    return "This stat is about average.";
	} else if (avg >= 40) {
	    return "This stat is just below average.";
	} else if (avg >= 30) {
	    return "This stat could be much better.";
	} else if (avg >= 20) {
	    return "This stat isn't that great.";
	} else if (avg >= 10) {
	    return "This stat will be outdone by many other monsters.";
	} else {
	    return "This stat is just horrendous.";
	}
    }


    public String getIVString_Average(int avg) {
	if (avg >= 90) {
	    return "This monster is absolutely flawless!";
	} else if (avg >= 80) {
	    return "This monster is amazing, outstanding even.";
	} else if (avg >= 70) {
	    return "This monster does pretty well.";
	} else if (avg >= 60) {
	    return "This monster is just above average.";
	} else if (avg >= 50) {
	    return "This monster is about average.";
	} else if (avg >= 40) {
	    return "This monster is just below average.";
	} else if (avg >= 30) {
	    return "This monster could do much better.";
	} else if (avg >= 20) {
	    return "This monster isn't that great.";
	} else if (avg >= 10) {
	    return "This monster will be outperformed by many other monsters.";
	} else {
	    return "This monster should be abandoned right away.";
	}
    }

    public void clearIV() {
	for (Pair<Byte, Double> stat : mods.values()) {
	    stat.left = (byte)50;
	}
	this.gender = (byte)0;
    }

    public final PokemonElement[] getElements() {
        return elements;
    }

    public final String getElementString() {
        return StringUtil.makeEnumHumanReadable(elements[0].name()).toUpperCase() + (elements[1] == PokemonElement.None ? "" : ("/" + StringUtil.makeEnumHumanReadable(elements[1].name()).toUpperCase()));
    }

    public final String getNatureString() {
        return StringUtil.makeEnumHumanReadable(nature.name()) + "(+" + StringUtil.makeEnumHumanReadable(nature.inc.name()) + "/-" + StringUtil.makeEnumHumanReadable(nature.dec.name()) + ")";
    }

    public final String getFamilyString() {
        return StringUtil.makeEnumHumanReadable(family.name());
    }

    public final int getLevel() {
        return tempLevel > 0 ? tempLevel : level;
    }

    public final void setTempLevel(int te) {
	this.tempLevel = te;
    }

    public final PokemonMob getFamily() {
        return family;
    }

    public final String getOriginalName() {
        return stats.getName();
    }

    public final void resetHP() {
        hp = calcHP();
    }

    public final void resetStats() {
	wipeStatus();
        wipe();
        resetHP();
	tempLevel = 0;
    }

    public final void wipeStatus() {
        status = null;
        statusTurnsLeft = 0;
    }

    public final void wipe() {
        mons = new WeakReference<MapleMonster>(null);
        damagedChars.clear();
	for (Pair<Byte, Double> stat : mods.values()) {
	    stat.right = 1.0;
	}
    }

    public final void pushElement(final PokemonElement pe) {
	for (int i = 0; i < elements.length; i++) {
	    if (elements[i] == PokemonElement.None) {
		elements[i] = pe;
		return;
	    }
	}
    }

    public final int getElementSize() {
	int ret = 0;
	for (int i = 0; i < elements.length; i++) {
	    if (elements[i] != PokemonElement.None) {
		ret++;
	    }
	}
	return ret;
    }

    private final void calculateFamily() {
        //i hate doing it like this
        if (stats == null) {
            return;
        }
	pushElement(PokemonElement.getById(stats.getCategory()));
        for (Entry<Element, ElementalEffectiveness> e : stats.getElements().entrySet()) {
            if (e.getValue() == ElementalEffectiveness.IMMUNE || e.getValue() == ElementalEffectiveness.STRONG) {
                pushElement(PokemonElement.getFromElement(e.getKey()));
                break;
            }
        }
        if (elements[0] == PokemonElement.None) {
            pushElement(PokemonElement.Normal);
        }
        resetStats();
	final List<PokemonMob> ourFamilies = new ArrayList<PokemonMob>();
        for (PokemonMob mob : PokemonMob.values()) {
            if (mob.evolutions.contains(monsterId)) {
		ourFamilies.add(mob);
            }
        }
	if (ourFamilies.size() > 0) {
	    family = ourFamilies.get(Randomizer.nextInt(ourFamilies.size()));
	}
	if (family != null) {
	    byte gender = BattleConstants.getGender(family);
	    if (gender >= 0) {
		this.gender = gender;
	    }
	    if (abilityIndex == 0) {
		this.ability = family.ability1;
	    } else {
		this.ability = family.ability2;
	    }
	}
    }

    public void setName(String n) {
        this.name = n;
    }

    public final double getCatchRate() {
        return 256.0 - (level * 255.0 / 200.0); //bosses cannot be caught
    }

    public final double canCatch(final double catchChance) {
        return ((3.0 * calcHP() - 2.0 * getCurrentHP()) * (getCatchRate() * catchChance) / (3.0 * calcHP())) * (status == null ? 1 : 1.5);
    }

    public final void damage(final int damage, final MapleMap map, final int uniqueidFrom, final boolean leaveStanding) {
	final long oldHp = hp;
        hp -= damage;
        hp = (long) Math.min(hp, calcHP());
        hp = (long) Math.max(hp, leaveStanding ? 1 : 0);
	if (map != null) {
            final int oid = getMonster().getObjectId();
            map.broadcastMessage(MobPacket.damageMonster(oid, damage));
	}
    }

    public void addMonsterId(int uniqueId){
 	if (!damagedChars.contains(Integer.valueOf(uniqueId))) {
	    damagedChars.add(Integer.valueOf(uniqueId));
  	}
    }

    public void removeMonsterId(int uniqueId){
 	for (int i = 0; i < damagedChars.size(); i++) {
	    if (damagedChars.size() > i && damagedChars.get(i) == uniqueId) {
		damagedChars.remove(i);
	    }
  	}
    }

    public final int getTrueLevel() {
	return level;
    }

    public final int getOurExp() {
	final int theExp = Math.max(1, stats.getExp());
        return (int) Math.min(100, calcBaseHP() / (theExp == 1 ? (stats.getLevel() / 5) : theExp)) * level / 2;
    }

    public final int getExp(boolean npc, int uniqueId) {
        if (!damagedChars.contains(Integer.valueOf(uniqueId))) {
            return 0;
        }
	final int theExp = Math.max(1, stats.getExp());
        return (int) Math.min(100, calcBaseHP() / (theExp == 1 ? (stats.getLevel() / 5) : theExp)) * level * (npc ? 3 : 2) / 4 / damagedChars.size();
    }

    public void gainExp(int xp, MapleCharacter chr) {
        if (level >= 200) {
            exp = 0;
            return;
        }
        exp += xp;
        while (exp > getNextExp()) {
            exp -= getNextExp();
            level++;
            if (level >= 200) {
                exp = 0;
                return;
            }
            if (getEvolutionType() != Evolution.LEVEL || item == HoldItem.Question_Mark) {
                continue;
            }
            evolve(false, chr);
        }
    }

    public void evolve(boolean skipCheck, MapleCharacter chr) {
        final boolean rename = name.equalsIgnoreCase(stats.getName());
        List<Integer> evo = family.evolutions;
        int ourIndex = -1;
        for (int i = 0; i < evo.size(); i++) {
            if (evo.get(i) == this.monsterId) {
                ourIndex = i;
                break;
            }
        }
        if (ourIndex >= 0 && evo.size() > (ourIndex+1)) {
            final MapleMonster next = MapleLifeFactory.getMonster(evo.get(ourIndex + 1));
	    if (level >= next.getStats().getLevel() || skipCheck) {
           	 this.monsterId = next.getId();
           	 this.stats = next.getStats();
           	 if (rename) {
           	     this.name = stats.getName();
           	 }
		 chr.getMonsterBook().monsterCaught(chr.getClient(), next.getId(), stats.getName());
	    }
        }
    }

    public List<Integer> getDamaged() {
        return damagedChars;
    }
}
