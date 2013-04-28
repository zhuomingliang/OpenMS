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
package server.maps;

import java.awt.Point;

import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import constants.GameConstants;
import client.anticheat.CheatingOffense;
import server.MapleStatEffect;
import tools.packet.CField.SummonPacket;

public class MapleSummon extends AnimatedMapleMapObject {

    private final int ownerid, skillLevel, ownerLevel, skill;
    private MapleMap map; //required for instanceMaps
    private short hp;
    private boolean changedMap = false;
    private SummonMovementType movementType;
    // Since player can have more than 1 summon [Pirate] 
    // Let's put it here instead of cheat tracker
    private int lastSummonTickCount;
    private byte Summon_tickResetCount;
    private long Server_ClientSummonTickDiff;
	private long lastAttackTime;

    public MapleSummon(final MapleCharacter owner, final MapleStatEffect skill, final Point pos, final SummonMovementType movementType) {
        this(owner, skill.getSourceId(), skill.getLevel(), pos, movementType);
    }

    public MapleSummon(final MapleCharacter owner, final int sourceid, final int level, final Point pos, final SummonMovementType movementType) {
        super();
        this.ownerid = owner.getId();
        this.ownerLevel = owner.getLevel();
        this.skill = sourceid;
        this.map = owner.getMap();
        this.skillLevel = level;
        this.movementType = movementType;
        setPosition(pos);

        if (!isPuppet()) { // Safe up 12 bytes of data, since puppet doesn't attack.
            lastSummonTickCount = 0;
            Summon_tickResetCount = 0;
            Server_ClientSummonTickDiff = 0;
			lastAttackTime = 0;
        }
    }

    @Override
    public final void sendSpawnData(final MapleClient client) {
    }

    @Override
    public final void sendDestroyData(final MapleClient client) {
        client.getSession().write(SummonPacket.removeSummon(this, false));
    }

    public final void updateMap(final MapleMap map) {
        this.map = map;
    }

    public final MapleCharacter getOwner() {
        return map.getCharacterById(ownerid);
    }

    public final int getOwnerId() {
        return ownerid;
    }

    public final int getOwnerLevel() {
        return ownerLevel;
    }

    public final int getSkill() {
        return skill;
    }

    public final short getHP() {
        return hp;
    }

    public final void addHP(final short delta) {
        this.hp += delta;
    }

    public final SummonMovementType getMovementType() {
        return movementType;
    }

    public final boolean isPuppet() {
        switch (skill) {
            case 3111002:
            case 3211002:
			case 3120012:
			case 3220012:
            case 13111004:
            case 4341006:
            case 33111003:
                return true;
        }
        return isAngel();
    }
	
	public final boolean isAngel() {
		return GameConstants.isAngel(skill);
	}
	
	public final boolean isMultiAttack() {
		if (skill != 35111002 && skill != 35121003 && (isGaviota() || skill == 33101008 || skill >= 35000000) && skill != 35111009 && skill != 35111010 && skill != 35111001) {
			return false;
		}
		return true;
	}

    public final boolean isGaviota() {
        return skill == 5211002;
    }

    public final boolean isBeholder() {
        return skill == 1321007;
    }

    public final boolean isMultiSummon() {
        return skill == 5211002 || skill == 5211001 || skill == 5220002 || skill == 32111006 || skill == 33101008;
    }

    public final boolean isSummon() {
        switch (skill) {
            case 12111004:
            case 1321007: //beholder
            case 2321003:
            case 2121005:
            case 5711001: // turret
            case 2221005:
            case 5211001: // Pirate octopus summon
            case 5211002:
            case 5220002: // wrath of the octopi
            case 13111004:
            case 11001004:
            case 12001004:
            case 13001004:
            case 14001005:
            case 15001004:
            case 33111005:
            case 35111001:
            case 35111010:
            case 35111009:
            case 35111002: //pre-bb = 35111002, 35111004(amp?), 35111005(accel)
            case 35111005:
            case 35111011:
            case 35121009:
            case 35121010:
            case 35121011:
            case 4111007:
            case 4211007: //dark flare
            case 32111006:
	    case 33101008:
	    case 35121003:
	    case 3101007:
	    case 3201007:
	    case 3111005:
	    case 3211005:
	    case 5321003:
	    case 5321004:
	    case 23111008:
	    case 23111009:
	    case 23111010:
                return true;
        }
        return isAngel();
    }

    public final int getSkillLevel() {
        return skillLevel;
    }

    public final int getSummonType() {
		if (isAngel()) {
			return 2;
		} else if ((skill != 33111003 && skill != 3120012 && skill != 3220012 && isPuppet()) || skill == 33101008 || skill == 35111002) {
            return 0;
        }
        switch (skill) {
            case 1321007:
                return 2; //buffs and stuff
            case 35111001: //satellite.
            case 35111009:
            case 35111010:
                return 3; //attacks what you attack
            case 35121009: //bots n. tots
                return 5; //sub summons
			case 35121003:
				return 6; //charge
            case 4111007: // test
            case 4211007: //dark flare
            	return 7; //attacks what you get hit by
        }
        return 1;
    }

    @Override
    public final MapleMapObjectType getType() {
        return MapleMapObjectType.SUMMON;
    }

    public final void CheckSummonAttackFrequency(final MapleCharacter chr, final int tickcount) {
        final int tickdifference = (tickcount - lastSummonTickCount);
        if (tickdifference < SkillFactory.getSummonData(skill).delay) {
            chr.getCheatTracker().registerOffense(CheatingOffense.FAST_SUMMON_ATTACK);
        }
        final long STime_TC = System.currentTimeMillis() - tickcount;
        final long S_C_Difference = Server_ClientSummonTickDiff - STime_TC;
        if (S_C_Difference > 500) {
            chr.getCheatTracker().registerOffense(CheatingOffense.FAST_SUMMON_ATTACK);
        }
        Summon_tickResetCount++;
        if (Summon_tickResetCount > 4) {
            Summon_tickResetCount = 0;
            Server_ClientSummonTickDiff = STime_TC;
        }
        lastSummonTickCount = tickcount;
    }
	
    public final void CheckPVPSummonAttackFrequency(final MapleCharacter chr) {
        final long tickdifference = (System.currentTimeMillis() - lastAttackTime);
        if (tickdifference < SkillFactory.getSummonData(skill).delay) {
            chr.getCheatTracker().registerOffense(CheatingOffense.FAST_SUMMON_ATTACK);
        }
        lastAttackTime = System.currentTimeMillis();
    }

    public final boolean isChangedMap() {
        return changedMap;
    }

    public final void setChangedMap(boolean cm) {
        this.changedMap = cm;
    }
}
