/*
 * This file is part of the OdinMS MapleStory Private Server
 * Copyright (C) 2011 Patrick Huy and Matthias Butz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package constants;

import server.Randomizer;
import tools.Pair;

/**
 *
 * @author AuroX
 */
public class OccupationConstants {

    private static final int[] occuEXP = {0, 2000, 4000, 6500, 9500, 13000, 17000, 21000, 25500, 30000};

    public static int getOccExpForLevel(final int level) {
        if (level < 0 || level >= occuEXP.length) {
            return Integer.MAX_VALUE;
        }
        return occuEXP[level];
    }

    public static double getExpMultiplier(final short occuId) {
        switch (occuId / 100) { // Get Type
            case 2: // Gamer
                return (1.0 + ((double) ((occuId % 10 == 0) ? 10 : (occuId % 10)) / 10));
            case 3: // Hunter
                return (occuId == 310 ? 0.5 : 0.3);
            case 4: // Vortex
                return 0.6;
            case 5: // NX Whore
                return 0.8;
        }
        return 1.0; // Normal (Ninja / No occ)
    }

    public static double getMesoMultiplier(final short occuId) {
		if (occuId / 100 ==  5) { // NX Whore
            return 0.8;
		}
        return 1.0; // Normal
    }

    public static double getDropMultiplier(final short occuId) {
        if (occuId / 100 ==  5) { // NX Whore
            return 0.8;
		}
		return 1.0; // Normal
    }

    public static double getCashMultiplier(final short occuId) {
        if (occuId / 100 == 5) { // NX Whore
            return 2.0;
        }
        return 1.0; // Normal
    }

    public static int getGamerChance(final short id) {
        if (id / 100 == 2) { // Gamer
            return ((id % 10) * 4);
        }
        return 0; // Normal
    }

    public static Pair<Integer, Integer> getHunterChance(final short id) {
        final int chance = (id % 2 == 0) ? 100 : 30;
        final int level = id % 10;
        int drops = 2;
        if (level == 3 || level == 4) {
            drops = 3;
        } else if (level == 5 || level == 6) {
            drops = 4;
        } else if (level == 7 || level == 8) {
            drops = 5;
        } else if (level == 9 || level == 0) {
            drops = 6;
        }
        return new Pair<>(chance, drops);
    }

    public static byte getNinjaClones(final short occ) {
        byte size = 1; // Base, all ninja = 100% 1st clone
        if (occ >= 103 && Randomizer.nextInt(100) < (occ <= 104 ? 70 : (occ <= 106 ? 80 : (occ <= 108 ? 90 : 100)))) {
            size++;
        }
        if (occ >= 105 && Randomizer.nextInt(100) < (occ <= 106 ? 70 : (occ <= 108 ? 80 : 90))) {
            size++;
        }
        if (occ >= 107 && Randomizer.nextInt(100) < (occ <= 108 ? 60 : 70)) {
            size++;
        }
        if (occ >= 109 && Randomizer.nextInt(100) < 60) {
            size++;
        }
        return size;
    }

    public static double getCloneDMG(final short occ) {
        if (occ / 100 != 1) {
            return 100.0; // Damage 0% if not GM
        }
        switch (occ % 10) {
            case 1:
                return 4.0;
            case 2:
            case 3:
                return 2.8;
            case 4:
            case 5:
                return 2.5;
            case 6:
            case 7:
                return 2.0;
            case 8:
            case 9:
            case 0: // Level 10 
                return 1.8;
        }
        return 100.0; // Damage 0% if not GM
    }

    public static double getVortexRange(final short id) {
        if (id / 100 != 4) {
            return 0.005; // Default, 0.05% from screen
        }
        double range = 0.01;
        final byte level = (byte) (id % 10);
        if (level == 9) {
            range += 0.085;
        } else {
            range += ((level == 0 ? 9 : level) * 0.01);
        }
        return range;
    }

    public static String toString(final int id) {
        switch (id / 100) {
            case 1:
                return "Ninja";
            case 2:
                return "Gamer";
            case 3:
                return "Hunter";
            case 4:
                return "Vortex";
            case 5:
                return "NX Whore";
        }
        return "Undefined";
    }
}