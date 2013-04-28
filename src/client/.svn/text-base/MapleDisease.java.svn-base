/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GU Affero General Public License version 3
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

import handling.Buffstat;
import java.io.Serializable;
import server.Randomizer;

public enum MapleDisease implements Serializable, Buffstat {

    STUN(0x20000, 1, 123),
    POISON(0x40000, 1, 125),
    SEAL(0x80000, 1, 120),
    DARKNESS(0x100000, 1, 121),
    WEAKEN(0x40000000, 1, 122),
    CURSE(0x80000000, 1, 124),
    SLOW(0x1, 2, 126),
    MORPH(0x2, 2, 172),
    SEDUCE(0x80, 2, 128),
    ZOMBIFY(0x4000, 2, 133),
    REVERSE_DIRECTION(0x80000, 2, 132),
    POTION(0x800, 3, 134),
    SHADOW(0x1000, 3, 135), //receiving damage/moving
    BLIND(0x2000, 3, 136),
    FREEZE(0x80000, 3, 137),
    DISABLE_POTENTIAL(0x4000000, 4, 138),
    TORNADO(0x40000000, 4, 173),
    FLAG(0x2, 6, 799); // PVP - Capture the Flag
    // 127 = 1 snow?
    // 129 = turn?
    // 131 = poison also, without msg
    // 133, become undead?..50% recovery?
    // 0x100 is disable skill except buff
    private static final long serialVersionUID = 0L;
    private int i;
    private int first;
    private int disease;

    private MapleDisease(int i, int first, int disease) {
        this.i = i;
        this.first = first;
        this.disease = disease;
    }

    public int getPosition() {
        return first;
    }

    public int getValue() {
        return i;
    }

    public int getDisease() {
        return disease;
    }

    public static final MapleDisease getRandom() {
        while (true) {
            for (MapleDisease dis : MapleDisease.values()) {
                if (Randomizer.nextInt(MapleDisease.values().length) == 0) {
                    return dis;
                }
            }
        }
    }

    public static final MapleDisease getBySkill(final int skill) {
        for (MapleDisease d : MapleDisease.values()) {
            if (d.getDisease() == skill) {
                return d;
            }
        }
        return null;
    }
}
