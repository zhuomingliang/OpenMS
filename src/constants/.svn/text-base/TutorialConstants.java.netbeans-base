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

import client.MapleCharacter;

/*
 * @author Akin
 */

public class TutorialConstants {
    
    public static final int[] tutorialDrops = {4031220, 4000353, 4000136}; // itemids for the tut drop
    public static final int dropPosX = 219; // x-position for tut drop
    public static final int dropPosY = -85; // y-postion for tut drop
    public static final int tutorialDropsMap = 30000; // mapid for tut drop
    public static final int beginnerNPC = 9000054; // opening npc
    public static final String beginnerNPCName = "Ranch Owner"; // opening npc
    
        
    public static boolean isBeginnerEquip(final int itemid) { // If you edit the equip id's make sure you edit the positions too
        switch (itemid) {
            case 1000005: // Men's Ninja Hat
            case 1010002: // Ninja Mask for Men
            case 1050071: // Men's Ninja Overall
            case 1072353: // White Ninja Sandals for Transformation 
            case 1080000: // White Ninja Gloves
            case 1001005: // Women's Ninja Hat
            case 1011000: // Ninja Mask for Women
            case 1051061: // Women's Ninja Uniform
            case 1072354: // Black Voyson Shoes for Transformation
            case 1080001: // Red Ninja Gloves
            case 1142073: // Be My Friend
                return true;
        }
        return false;
    }
    
    public static final byte[] position = { // position for the beginner equips (do NOT edit if you don't know what it does)
        -101, // Hat position
        -102, // Face accesory position
        -105, // Overall postion
        -107, // Shoes position
        -108, // Gloves position
        -49   // Medal position
    };
    
    public static final int[] starterPack = {
         1082149, // Brown Work Gloves
         1372005, // Wooden Wand
         1302007, // Long Sword
         1332063, // Beginner Thief's short sword
         1432000, // Spear
         1442000, // Pole Arm
         1452002, // War Bow
         1462047, // Xaru
         1472000, // Garnier
         1302001, // Saw
         1382000, // Wooden Staff
         1492000, // Pistol
         1482000  // Steel Knuckler
    };
    
    public static final int[] equipStats = { // the beginner clothes stats
        10,// str, dex, int and luk 
        5, // weapon and magic att
        69, // scrolled amount (max is 128)
        10, // enhancement stars (max is 128)
        0}; // upgradable slots
    
    public static String getStageMSG(final MapleCharacter chr, final int id) {
        switch (id) {
            case 10000:
                return "Welcome to stage 1";
            case 20000:
                return "Welcome to stage 2";
            case 30000:
                return "Welcome to stage 3";
            case 30001:
                return "Time to choose an occupation";
            case 40000:
                return "Welcome to stage 4";
            case 50000:
                return "Welcome to stage 5";
            case 913030000:
                return "Oh my god!, what happened to this place?";
         }
        return "Welcome back #e" + chr.getName() + "#n!";
    }
    
    public static String getTutorialTalk(final MapleCharacter chr, final int id) {
        switch (id) {
            case 10000:
                return "Hello, I am the #e" + beginnerNPCName + "'s #rattractive daughter#k#n. here to introduce you to " + chr.getClient().getChannelServer().getServerName() + "! ";
            case 20000:
                return "This is a #equiz on " + chr.getClient().getChannelServer().getServerName() + "#n, so simply use #bthe bulletin board as a reference#k and #e#rpass my quiz#k#n!";
            case 30000:
                return "Hey look, #ea chef#n...\r\n \r\n#e#rWho's hungry#k#n?";
            case 30001:
                return "#eHey, it's the boss#n...\r\n \r\n#e#rI tried to get a job from him for ages#n#k...";
            case 40000:
                return "This is the easiest stage, please collect the items which are dropped by the monster!";
            case 50000:
                return "Pff, welcome to the last stage.\r\nTalk to #e" + beginnerNPCName + "#n for the requirements!";
            case 913030000:
                return "Oh my god!, what happened to this place?\r\nLook there is one person alive, it's #e" + beginnerNPCName + "#n.\r\nI guess he needs your help.";
         }
        return "Welcome back #e" + chr.getName() + "#n!";
    }
        
    public static int getQuest(final MapleCharacter chr, final int id) {
        switch (id / 10000) {
            case 1:
                return 50000; // quest id for stage 1
            case 2:  
                return 50001; // quest id for stage 2
            case 3:  
                return 50002; // quest id for stage 3
            case 4:  
                return 50003; // quest id for stage 4
            case 5: 
                return 50004; // quest id for stage 5
            default:
                return 99999;                       
        }
    }
    
    public static final String getPortalBlockedMsg() {
        return "You haven't finished this stage yet.";
    }
    
    public static final String getDropBlockedMsg() {
        return "You cannot drop your tutorial equips.";
    }
    
    public static final String getTradeBlockedMsg() {
        return "You cannot trade your tutorial equips.";
    }
    
    public static final String getEquipBlockedMsg() {
        return "You cannot unequip your tutorial equips.";
    }
}