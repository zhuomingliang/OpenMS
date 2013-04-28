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
package client.status;

import client.MapleDisease;
import constants.GameConstants;
import handling.Buffstat;
import java.io.Serializable;

public enum MonsterStatus implements Serializable, Buffstat {

    WATK(0x1, 1),
    WDEF(0x2, 1),
    MATK(0x4, 1),
    MDEF(0x8, 1),
    ACC(0x10, 1),
    AVOID(0x20, 1),
    SPEED(0x40, 1),
    STUN(0x80, 1),
    FREEZE(0x100, 1),
    POISON(0x200, 1),
    SEAL(0x400, 1),
    SHOWDOWN(0x800, 1),
    WEAPON_ATTACK_UP(0x1000, 1),
    WEAPON_DEFENSE_UP(0x2000, 1),
    MAGIC_ATTACK_UP(0x4000, 1),
    MAGIC_DEFENSE_UP(0x8000, 1),
    DOOM(0x10000, 1),
    SHADOW_WEB(0x20000, 1),
    WEAPON_IMMUNITY(0x40000, 1),
    MAGIC_IMMUNITY(0x80000, 1),
    DAMAGE_IMMUNITY(0x200000, 1),
    NINJA_AMBUSH(0x400000, 1),
    BURN(0x1000000, 1),
    DARKNESS(0x2000000, 1),
    HYPNOTIZE(0x10000000, 1),
    WEAPON_DAMAGE_REFLECT(0x20000000, 1),
    MAGIC_DAMAGE_REFLECT(0x40000000, 1),
    NEUTRALISE(0x2, 2), // first int on v.87 or else it won't work.
    IMPRINT(0x4, 2),
    MONSTER_BOMB(0x8, 2),
    MAGIC_CRASH(0x10, 2),
    //speshul comes after
    EMPTY(0x8000000, 1, true),
    SUMMON(0x80000000, 1, true), //all summon bag mobs have.
    EMPTY_1(0x20, 2, !GameConstants.GMS), //chaos
    EMPTY_2(0x40, 2, true),
    EMPTY_3(0x80, 2, true),
    EMPTY_4(0x100, 2, GameConstants.GMS), //jump
    EMPTY_5(0x200, 2, GameConstants.GMS),
    EMPTY_6(0x400, 2, GameConstants.GMS),;
    static final long serialVersionUID = 0L;
    private final int i;
    private final int first;
    private final boolean end;

    private MonsterStatus(int i, int first) {
        this.i = i;
        this.first = first;
        this.end = false;
    }

    private MonsterStatus(int i, int first, boolean end) {
        this.i = i;
        this.first = first;
        this.end = end;
    }

    public int getPosition() {
        return first;
    }

    public boolean isEmpty() {
        return end;
    }

    public int getValue() {
        return i;
    }

    public static final MonsterStatus getBySkill_Pokemon(final int skill) {
        switch (skill) {
            case 120:
                return SEAL;
            case 121:
                return DARKNESS;
            case 123:
                return STUN;
            case 125:
                return POISON;
            case 126:
                return SPEED;
            case 137:
                return FREEZE;
        }
        return null;
    }

    public static final MapleDisease getLinkedDisease(final MonsterStatus skill) {
        switch (skill) {
            case STUN:
            case SHADOW_WEB:
                return MapleDisease.STUN;
            case POISON:
            case BURN:
                return MapleDisease.POISON;
            case SEAL:
            case MAGIC_CRASH:
                return MapleDisease.SEAL;
            case FREEZE:
                return MapleDisease.FREEZE;
            case DARKNESS:
                return MapleDisease.DARKNESS;
            case SPEED:
                return MapleDisease.SLOW;
        }
        return null;
    }
}
