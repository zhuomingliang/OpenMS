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
import constants.GameConstants;
import java.awt.Point;
import java.io.File;
import java.util.*;

import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataFileEntry;
import provider.MapleDataProviderFactory;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataTool;
import server.Randomizer;
import tools.StringUtil;
import tools.Triple;

public class SkillFactory {

    private static final Map<Integer, Skill> skills = new HashMap<Integer, Skill>();
    private static final Map<String, Integer> delays = new HashMap<String, Integer>();
    private static final Map<Integer, CraftingEntry> crafts = new HashMap<Integer, CraftingEntry>();
    private static final Map<Integer, FamiliarEntry> familiars = new HashMap<Integer, FamiliarEntry>();
    private static final Map<Integer, List<Integer>> skillsByJob = new HashMap<Integer, List<Integer>>();
    private static final Map<Integer, SummonSkillEntry> SummonSkillInformation = new HashMap<Integer, SummonSkillEntry>();

    public static void load() {
        final MapleData delayData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Character.wz")).getData("00002000.img");
        final MapleData stringData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz")).getData("Skill.img");
        final MapleDataProvider datasource = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Skill.wz"));
        final MapleDataDirectoryEntry root = datasource.getRoot();
        int del = 0; //buster is 67 but its the 57th one!
        for (MapleData delay : delayData) {
            if (!delay.getName().equals("info")) {
                delays.put(delay.getName(), del);
                del++;
            }
        }

        int skillid;
        MapleData summon_data;
        SummonSkillEntry sse;

        for (MapleDataFileEntry topDir : root.getFiles()) { // Loop thru jobs
            if (topDir.getName().length() <= 8) {
				for (MapleData data : datasource.getData(topDir.getName())) { // Loop thru each jobs
                    if (data.getName().equals("skill")) {
                        for (MapleData data2 : data) { // Loop thru each jobs
                            if (data2 != null) {
                                skillid = Integer.parseInt(data2.getName());
                                Skill skil = Skill.loadFromData(skillid, data2, delayData);
                                List<Integer> job = skillsByJob.get(skillid / 10000);
                                if (job == null) {
                                    job = new ArrayList<Integer>();
                                    skillsByJob.put(skillid / 10000, job);
                                }
                                job.add(skillid);
                                skil.setName(getName(skillid, stringData));
                                skills.put(skillid, skil);

                                summon_data = data2.getChildByPath("summon/attack1/info");
                                if (summon_data != null) {
                                    sse = new SummonSkillEntry();
                                    sse.type = (byte) MapleDataTool.getInt("type", summon_data, 0);
                                    sse.mobCount = (byte) (skillid == 33101008 ? 3 : MapleDataTool.getInt("mobCount", summon_data, 1));
                                    sse.attackCount = (byte) MapleDataTool.getInt("attackCount", summon_data, 1);
                                    if (summon_data.getChildByPath("range/lt") != null) {
                                        final MapleData ltd = summon_data.getChildByPath("range/lt");
                                        sse.lt = (Point) ltd.getData();
                                        sse.rb = (Point) summon_data.getChildByPath("range/rb").getData();
                                    } else {
                                        sse.lt = new Point(-100, -100);
                                        sse.rb = new Point(100, 100);
                                    }
                                    //sse.range = (short) MapleDataTool.getInt("range/r", summon_data, 0);
                                    sse.delay = MapleDataTool.getInt("effectAfter", summon_data, 0) + MapleDataTool.getInt("attackAfter", summon_data, 0);
                                    for (MapleData effect : summon_data) {
                                        if (effect.getChildren().size() > 0) {
                                            for (final MapleData effectEntry : effect) {
                                                sse.delay += MapleDataTool.getIntConvert("delay", effectEntry, 0);
                                            }
                                        }
                                    }
                                    for (MapleData effect : data2.getChildByPath("summon/attack1")) {
                                        sse.delay += MapleDataTool.getIntConvert("delay", effect, 0);
                                    }
                                    SummonSkillInformation.put(skillid, sse);
                                }
                            }
                        }
                    }
                }
            } else if (topDir.getName().startsWith("Familiar")) {
                for (MapleData data : datasource.getData(topDir.getName())) {
                    skillid = Integer.parseInt(data.getName());
                    FamiliarEntry skil = new FamiliarEntry();
                    skil.prop = (byte) MapleDataTool.getInt("prop", data, 0);
                    skil.time = (byte) MapleDataTool.getInt("time", data, 0);
                    skil.attackCount = (byte) MapleDataTool.getInt("attackCount", data, 1);
                    skil.targetCount = (byte) MapleDataTool.getInt("targetCount", data, 1);
                    skil.speed = (byte) MapleDataTool.getInt("speed", data, 1);
                    skil.knockback = MapleDataTool.getInt("knockback", data, 0) > 0 || MapleDataTool.getInt("attract", data, 0) > 0;
                    if (data.getChildByPath("lt") != null) {
                        skil.lt = (Point) data.getChildByPath("lt").getData();
                        skil.rb = (Point) data.getChildByPath("rb").getData();
                    }
                    if (MapleDataTool.getInt("stun", data, 0) > 0) {
                        skil.status.add(MonsterStatus.STUN);
                    }
                    //if (MapleDataTool.getInt("poison", data, 0) > 0) {
                    //	status.add(MonsterStatus.POISON);
                    //}
                    if (MapleDataTool.getInt("slow", data, 0) > 0) {
                        skil.status.add(MonsterStatus.SPEED);
                    }
                    familiars.put(skillid, skil);
                }
            } else if (topDir.getName().startsWith("Recipe")) {
                for (MapleData data : datasource.getData(topDir.getName())) {
                    skillid = Integer.parseInt(data.getName());
                    CraftingEntry skil = new CraftingEntry(skillid, (byte) MapleDataTool.getInt("incFatigability", data, 0), (byte) MapleDataTool.getInt("reqSkillLevel", data, 0), (byte) MapleDataTool.getInt("incSkillProficiency", data, 0), MapleDataTool.getInt("needOpenItem", data, 0) > 0, MapleDataTool.getInt("period", data, 0));
                    for (MapleData d : data.getChildByPath("target")) {
                        skil.targetItems.add(new Triple<Integer, Integer, Integer>(MapleDataTool.getInt("item", d, 0), MapleDataTool.getInt("count", d, 0), MapleDataTool.getInt("probWeight", d, 0)));
                    }
                    for (MapleData d : data.getChildByPath("recipe")) {
                        skil.reqItems.put(MapleDataTool.getInt("item", d, 0), MapleDataTool.getInt("count", d, 0));
                    }
                    crafts.put(skillid, skil);
                }
            }
        }
    }

    public static List<Integer> getSkillsByJob(final int jobId) {
        return skillsByJob.get(jobId);
    }

    public static String getSkillName(final int id) {
        Skill skil = getSkill(id);
        if (skil != null) {
            return skil.getName();
        }
        return null;
    }

    public static Integer getDelay(final String id) {
        if (Delay.fromString(id) != null) {
            return Delay.fromString(id).i;
        }
        return delays.get(id);
    }

    private static String getName(final int id, final MapleData stringData) {
        String strId = Integer.toString(id);
        strId = StringUtil.getLeftPaddedStr(strId, '0', 7);
        MapleData skillroot = stringData.getChildByPath(strId);
        if (skillroot != null) {
            return MapleDataTool.getString(skillroot.getChildByPath("name"), "");
        }
        return "";
    }

    public static SummonSkillEntry getSummonData(final int skillid) {
        return SummonSkillInformation.get(skillid);
    }

    public static Collection<Skill> getAllSkills() {
        return skills.values();
    }

    public static Skill getSkill(final int id) {
        if (!skills.isEmpty()) {
            if (id >= 92000000 && crafts.containsKey(Integer.valueOf(id))) { //92000000
                return crafts.get(Integer.valueOf(id));
            }
            return skills.get(Integer.valueOf(id));
        }

        return null;
    }
	
	 public static long getDefaultSExpiry(final Skill skill) {
        if (skill == null) {
            return -1;
        }
        return (skill.isTimeLimited() ? (System.currentTimeMillis() + (long) (30L * 24L * 60L * 60L * 1000L)) : -1);
    }

    public static CraftingEntry getCraft(final int id) {
        if (!crafts.isEmpty()) {
            return crafts.get(Integer.valueOf(id));
        }

        return null;
    }

    public static FamiliarEntry getFamiliar(final int id) {
        if (!familiars.isEmpty()) {
            return familiars.get(Integer.valueOf(id));
        }

        return null;
    }

    public static class CraftingEntry extends Skill {
        //reqSkillProficiency -> always seems to be 0

        public boolean needOpenItem;
        public int period;
        public byte incFatigability, reqSkillLevel, incSkillProficiency;
        public List<Triple<Integer, Integer, Integer>> targetItems = new ArrayList<Triple<Integer, Integer, Integer>>(); // itemId / amount / probability
        public Map<Integer, Integer> reqItems = new HashMap<Integer, Integer>(); // itemId / amount

        public CraftingEntry(int id, byte incFatigability, byte reqSkillLevel, byte incSkillProficiency, boolean needOpenItem, int period) {
            super(id);
            this.incFatigability = incFatigability;
            this.reqSkillLevel = reqSkillLevel;
            this.incSkillProficiency = incSkillProficiency;
            this.needOpenItem = needOpenItem;
            this.period = period;
        }
    }

    public static class FamiliarEntry {

        public byte prop, time, attackCount, targetCount, speed;
        public Point lt, rb;
        public boolean knockback;
        public EnumSet<MonsterStatus> status = EnumSet.noneOf(MonsterStatus.class);

        public final boolean makeChanceResult() {
            return prop >= 100 || Randomizer.nextInt(100) < prop;
        }
    }
    
    public static enum Delay {

        walk1(0x00),
        walk2(0x01),
        stand1(0x02),
        stand2(0x03),
        alert(0x04),
        swingO1(0x05),
        swingO2(0x06),
        swingO3(0x07),
        swingOF(0x08),
        swingT1(0x09),
        swingT2(0x0A),
        swingT3(0x0B),
        swingTF(0x0C),
        swingP1(0x0D),
        swingP2(0x0E),
        swingPF(0x0F),
        stabO1(0x10),
        stabO2(0x11),
        stabOF(0x12),
        stabT1(0x13),
        stabT2(0x14),
        stabTF(0x15),
        swingD1(0x16),
        swingD2(0x17),
        stabD1(0x18),
        swingDb1(0x19),
        swingDb2(0x1A),
        swingC1(0x1B),
        swingC2(0x1C),
        rushBoom(0x1C),
        tripleBlow(GameConstants.GMS ? 0x1D : 0x19),
        quadBlow(GameConstants.GMS ? 0x1E : 0x1A),
        deathBlow(GameConstants.GMS ? 0x1F : 0x1B),
        finishBlow(GameConstants.GMS ? 0x20 : 0x1C),
        finishAttack(GameConstants.GMS ? 0x21 : 0x1D),
        finishAttack_link(GameConstants.GMS ? 0x22 : 0x1E),
        finishAttack_link2(GameConstants.GMS ? 0x22 : 0x1E),
        shoot1(GameConstants.GMS ? 0x23 : 0x1F),
        shoot2(GameConstants.GMS ? 0x24 : 0x20),
        shootF(GameConstants.GMS ? 0x25 : 0x21),
        shootDb2(0x28),
        shotC1(0x29),
        dash(GameConstants.GMS ? 0x2B : 0x25),
        dash2(GameConstants.GMS ? 0x2C : 0x26), //hack. doesn't really exist
        proneStab(GameConstants.GMS ? 0x2F : 0x29),
        prone(GameConstants.GMS ? 0x30 : 0x2A),
        heal(GameConstants.GMS ? 0x31 : 0x2B),
        fly(GameConstants.GMS ? 0x32 : 0x2C),
        jump(GameConstants.GMS ? 0x33 : 0x2D),
        sit(GameConstants.GMS ? 0x34 : 0x2E),
        rope(GameConstants.GMS ? 0x35 : 0x2F),
        dead(GameConstants.GMS ? 0x36 : 0x30),
        ladder(GameConstants.GMS ? 0x37 : 0x31),
        rain(GameConstants.GMS ? 0x38 : 0x32),
        alert2(GameConstants.GMS ? 0x40 : 0x34),
        alert3(GameConstants.GMS ? 0x41 : 0x35),
        alert4(GameConstants.GMS ? 0x42 : 0x36),
        alert5(GameConstants.GMS ? 0x43 : 0x37),
        alert6(GameConstants.GMS ? 0x44 : 0x38),
        alert7(GameConstants.GMS ? 0x45 : 0x39),
        ladder2(GameConstants.GMS ? 0x46 : 0x3A),
        rope2(GameConstants.GMS ? 0x47 : 0x3B),
        shoot6(GameConstants.GMS ? 0x48 : 0x3C),
        magic1(GameConstants.GMS ? 0x49 : 0x3D),
        magic2(GameConstants.GMS ? 0x4A : 0x3E),
        magic3(GameConstants.GMS ? 0x4B : 0x3F),
        magic5(GameConstants.GMS ? 0x4C : 0x40),
        magic6(GameConstants.GMS ? 0x4D : 0x41),
        explosion(GameConstants.GMS ? 0x4D : 0x41),
        burster1(GameConstants.GMS ? 0x4E : 0x42),
        burster2(GameConstants.GMS ? 0x4F : 0x43),
        savage(GameConstants.GMS ? 0x50 : 0x44),
        avenger(GameConstants.GMS ? 0x51 : 0x45),
        assaulter(GameConstants.GMS ? 0x52 : 0x46),
        prone2(GameConstants.GMS ? 0x53 : 0x47),
        assassination(GameConstants.GMS ? 0x54 : 0x48),
        assassinationS(GameConstants.GMS ? 0x55 : 0x49),
        tornadoDash(GameConstants.GMS ? 0x58 : 0x4C),
        tornadoDashStop(GameConstants.GMS ? 0x58 : 0x4C),
        tornadoRush(GameConstants.GMS ? 0x58 : 0x4C),
        rush(GameConstants.GMS ? 0x59 : 0x4D),
        rush2(GameConstants.GMS ? 0x5A : 0x4E),
        brandish1(GameConstants.GMS ? 0x5B : 0x4F),
        brandish2(GameConstants.GMS ? 0x5C : 0x50),
        braveSlash(GameConstants.GMS ? 0x5D : 0x51),
        braveslash1(GameConstants.GMS ? 0x5D : 0x51),
        braveslash2(GameConstants.GMS ? 0x5E : 0x51),
        braveslash3(GameConstants.GMS ? 0x5F : 0x51),
        braveslash4(GameConstants.GMS ? 0x60 : 0x51),
        darkImpale(0x61),
        sanctuary(GameConstants.GMS ? 0x62 : 0x52),
        meteor(GameConstants.GMS ? 0x63 : 0x53),
        paralyze(GameConstants.GMS ? 0x64 : 0x54),
        blizzard(GameConstants.GMS ? 0x65 : 0x55),
        genesis(GameConstants.GMS ? 0x66 : 0x56),
        blast(GameConstants.GMS ? 0x69 : 0x58),
        smokeshell(GameConstants.GMS ? 0x6A : 0x59),
        showdown(GameConstants.GMS ? 0x6B : 0x5A),
        ninjastorm(GameConstants.GMS ? 0x6C : 0x5B),
        chainlightning(GameConstants.GMS ? 0x6D : 0x5C),
        holyshield(GameConstants.GMS ? 0x6E : 0x5D),
        resurrection(GameConstants.GMS ? 0x6F : 0x5E),
        somersault(GameConstants.GMS ? 0x70 : 0x5F),
        straight(GameConstants.GMS ? 0x71 : 0x60),
        eburster(GameConstants.GMS ? 0x72 : 0x61),
        backspin(GameConstants.GMS ? 0x73 : 0x62),
        eorb(GameConstants.GMS ? 0x74 : 0x63),
        screw(GameConstants.GMS ? 0x75 : 0x64),
        doubleupper(GameConstants.GMS ? 0x76 : 0x65),
        dragonstrike(GameConstants.GMS ? 0x77 : 0x66),
        doublefire(GameConstants.GMS ? 0x78 : 0x67),
        triplefire(GameConstants.GMS ? 0x79 : 0x68),
		fake(GameConstants.GMS ? 0x7A : 0x69),
        airstrike(GameConstants.GMS ? 0x7B : 0x6A),
        edrain(GameConstants.GMS ? 0x7C : 0x6B),
        octopus(GameConstants.GMS ? 0x7D : 0x6C),
        backstep(GameConstants.GMS ? 0x7E : 0x6D),
        shot(GameConstants.GMS ? 0x7F : 0x6E),
        rapidfire(GameConstants.GMS ? 0x7F : 0x6E),
        fireburner(GameConstants.GMS ? 0x81 : 0x70),
        coolingeffect(GameConstants.GMS ? 0x82 : 0x71),
        fist(GameConstants.GMS ? 0x84 : 0x72),
        timeleap(GameConstants.GMS ? 0x85 : 0x73),
        homing(GameConstants.GMS ? 0x86 : 0x75),
        ghostwalk(GameConstants.GMS ? 0x87 : 0x76),
        ghoststand(GameConstants.GMS ? 0x88 : 0x77),
        ghostjump(GameConstants.GMS ? 0x89 : 0x78),
        ghostproneStab(GameConstants.GMS ? 0x8A : 0x79),
        ghostladder(GameConstants.GMS ? 0x8B : 0x7A),
        ghostrope(GameConstants.GMS ? 0x8C : 0x7B),
        ghostfly(GameConstants.GMS ? 0x8D : 0x7C),
        ghostsit(GameConstants.GMS ? 0x8E : 0x7D),
        cannon(GameConstants.GMS ? 0x8F : 0x7E),
        torpedo(GameConstants.GMS ? 0x90 : 0x7F),
        darksight(GameConstants.GMS ? 0x91 : 0x80),
        bamboo(GameConstants.GMS ? 0x92 : 0x81),
        pyramid(GameConstants.GMS ? 0x93 : 0x82),
        wave(GameConstants.GMS ? 0x94 : 0x83),
        blade(GameConstants.GMS ? 0x95 : 0x84),
        souldriver(GameConstants.GMS ? 0x96 : 0x85),
        firestrike(GameConstants.GMS ? 0x97 : 0x86),
        flamegear(GameConstants.GMS ? 0x98 : 0x87),
        stormbreak(GameConstants.GMS ? 0x99 : 0x88),
        vampire(GameConstants.GMS ? 0x9A : 0x89),
        swingT2PoleArm(GameConstants.GMS ? 0x9C : 0x8B),
        swingP1PoleArm(GameConstants.GMS ? 0x9D : 0x8C),
        swingP2PoleArm(GameConstants.GMS ? 0x9E : 0x8D),
        doubleSwing(GameConstants.GMS ? 0x9F : 0x8E),
        tripleSwing(GameConstants.GMS ? 0xA0 : 0x8F),
        fullSwingDouble(GameConstants.GMS ? 0xA1 : 0x90),
        fullSwingTriple(GameConstants.GMS ? 0xA2 : 0x91),
        overSwingDouble(GameConstants.GMS ? 0xA3 : 0x92),
        overSwingTriple(GameConstants.GMS ? 0xA4 : 0x93),
        rollingSpin(GameConstants.GMS ? 0xA5 : 0x94),
        comboSmash(GameConstants.GMS ? 0xA6 : 0x95),
        comboFenrir(GameConstants.GMS ? 0xA7 : 0x96),
        comboTempest(GameConstants.GMS ? 0xA8 : 0x97),
        finalCharge(GameConstants.GMS ? 0xA9 : 0x98),
        finalBlow(GameConstants.GMS ? 0xAB : 0x9A),
        finalToss(GameConstants.GMS ? 0xAC : 0x9B),
        magicmissile(GameConstants.GMS ? 0xAD : 0x9C),
        lightningBolt(GameConstants.GMS ? 0xAE : 0x9D),
        dragonBreathe(GameConstants.GMS ? 0xAF : 0x9E),
        breathe_prepare(GameConstants.GMS ? 0xB0 : 0x9F),
        dragonIceBreathe(GameConstants.GMS ? 0xB1 : 0xA0),
        icebreathe_prepare(GameConstants.GMS ? 0xB2 : 0xA1),
        blaze(GameConstants.GMS ? 0xB3 : 0xA2),
        fireCircle(GameConstants.GMS ? 0xB4 : 0xA3),
        illusion(GameConstants.GMS ? 0xB5 : 0xA4),
        magicFlare(GameConstants.GMS ? 0xB6 : 0xA5),
        elementalReset(GameConstants.GMS ? 0xB7 : 0xA6),
        magicRegistance(GameConstants.GMS ? 0xB8 : 0xA7),
        magicBooster(GameConstants.GMS ? 0xB9 : 0xA8),
        magicShield(GameConstants.GMS ? 0xBA : 0xA9),
        recoveryAura(GameConstants.GMS ? 0xBB : 0xAA),
        flameWheel(GameConstants.GMS ? 0xBC : 0xAB),
        killingWing(GameConstants.GMS ? 0xBD : 0xAC),
        OnixBlessing(GameConstants.GMS ? 0xBE : 0xAD),
        Earthquake(GameConstants.GMS ? 0xBF : 0xAE),
        soulStone(GameConstants.GMS ? 0xC0 : 0xAF),
        dragonThrust(GameConstants.GMS ? 0xC1 : 0xB0),
        ghostLettering(GameConstants.GMS ? 0xC2 : 0xB1),
        darkFog(GameConstants.GMS ? 0xC3 : 0xB2),
        slow(GameConstants.GMS ? 0xC4 : 0xB3),
        mapleHero(GameConstants.GMS ? 0xC5 : 0xB4),
        Awakening(GameConstants.GMS ? 0xC6 : 0xB5),
        flyingAssaulter(GameConstants.GMS ? 0xC7 : 0xB6),
        tripleStab(GameConstants.GMS ? 0xC8 : 0xB7),
        fatalBlow(GameConstants.GMS ? 0xC9 : 0xB8),
        slashStorm1(GameConstants.GMS ? 0xCA : 0xB9),
        slashStorm2(GameConstants.GMS ? 0xCB : 0xBA),
        bloodyStorm(GameConstants.GMS ? 0xCC : 0xBB),
        flashBang(GameConstants.GMS ? 0xCD : 0xBC),
        upperStab(GameConstants.GMS ? 0xCE : 0xBD),
        bladeFury(GameConstants.GMS ? 0xCF : 0xBE),
        chainPull(GameConstants.GMS ? 0xD1 : 0xC0),
        chainAttack(GameConstants.GMS ? 0xD1 : 0xC0),
        owlDead(GameConstants.GMS ? 0xD2 : 0xC1),
        monsterBombPrepare(GameConstants.GMS ? 0xD4 : 0xC3),
        monsterBombThrow(GameConstants.GMS ? 0xD4 : 0xC3),
        finalCut(GameConstants.GMS ? 0xD5 : 0xC4),
        finalCutPrepare(GameConstants.GMS ? 0xD5 : 0xC4),
        suddenRaid(GameConstants.GMS ? 0xD7 : 0xC6), //idk, not in data anymore
        fly2(GameConstants.GMS ? 0xD8 : 0xC7),
        fly2Move(GameConstants.GMS ? 0xD9 : 0xC8),
        fly2Skill(GameConstants.GMS ? 0xDA : 0xC9),
        knockback(GameConstants.GMS ? 0xDB : 0xCA),
        rbooster_pre(GameConstants.GMS ? 0xDF : 0xCE),
        rbooster(GameConstants.GMS ? 0xDF : 0xCE),
        rbooster_after(GameConstants.GMS ? 0xDF : 0xCE),
        crossRoad(GameConstants.GMS ? 0xE2 : 0xD1),
        nemesis(GameConstants.GMS ? 0xE3 : 0xD2),
        tank(GameConstants.GMS ? 0xEA : 0xD9),
        tank_laser(GameConstants.GMS ? 0xEE : 0xDD),
        siege_pre(GameConstants.GMS ? 0xF0 : 0xDF),
        tank_siegepre(GameConstants.GMS ? 0xF0 : 0xDF), //just to make it work with the skill, these two
        sonicBoom(GameConstants.GMS ? 0xF3 : 0xE2),
        darkLightning(GameConstants.GMS ? 0xF5 : 0xE4),
        darkChain(GameConstants.GMS ? 0xF6 : 0xE5),
        cyclone_pre(0),
        cyclone(0), //energy attack
        glacialchain(0xF7),
        flamethrower(GameConstants.GMS ? 0xFB : 0xE9),
        flamethrower_pre(GameConstants.GMS ? 0xFB : 0xE9),
        flamethrower2(GameConstants.GMS ? 0xFC : 0xEA),
        flamethrower_pre2(GameConstants.GMS ? 0xFC : 0xEA),
        gatlingshot(GameConstants.GMS ? 0x101 : 0xEF),
        gatlingshot2(GameConstants.GMS ? 0x102 : 0xF0),
        drillrush(GameConstants.GMS ? 0x103 : 0xF1),
        earthslug(GameConstants.GMS ? 0x104 : 0xF2),
        rpunch(GameConstants.GMS ? 0x105 : 0xF3),
        clawCut(GameConstants.GMS ? 0x106 : 0xF4),
        swallow(GameConstants.GMS ? 0x109 : 0xF7),
        swallow_attack(GameConstants.GMS ? 0x109 : 0xF7),
        swallow_loop(GameConstants.GMS ? 0x109 : 0xF7),
        flashRain(GameConstants.GMS ? 0x111 : 0xF9),
        OnixProtection(GameConstants.GMS ? 0x11C : 0x108),
        OnixWill(GameConstants.GMS ? 0x11D : 0x109),
        phantomBlow(GameConstants.GMS ? 0x11E : 0x10A),
        comboJudgement(GameConstants.GMS ? 0x11F : 0x10B),
        arrowRain(GameConstants.GMS ? 0x120 : 0x10C),
        arrowEruption(GameConstants.GMS ? 0x121 : 0x10D),
        iceStrike(GameConstants.GMS ? 0x122 : 0x10E),
        swingT2Giant(GameConstants.GMS ? 0x125 : 0x111),
        cannonJump(0x127),
        swiftShot(0x128),
        giganticBackstep(0x12A),
        mistEruption(0x12B),
        cannonSmash(0x12C),
        cannonSlam(0x12D),
        flamesplash(0x12E),
        noiseWave(0x132),
        superCannon(0x136),
        jShot(0x138),
        demonSlasher(0x139),
        bombExplosion(0x13A),
        cannonSpike(0x13B),
        speedDualShot(0x13C),
        strikeDual(0x13D),
        bluntSmash(0x13F),
        crossPiercing(0x140),
        piercing(0x141),
        elfTornado(0x143),
        immolation(0x144),
        multiSniping(0x147),
        windEffect(0x148),
        elfrush(0x149),
        elfrush2(0x149),
        dealingRush(0x14E),
        maxForce0(0x150),
        maxForce1(0x151),
        maxForce2(0x152),
        maxForce3(0x153),
        //special: pirate morph attacks
        iceAttack1(GameConstants.GMS ? 0x158 : 0x112),
        iceAttack2(GameConstants.GMS ? 0x159 : 0x113),
        iceSmash(GameConstants.GMS ? 0x15A : 0x114),
        iceTempest(GameConstants.GMS ? 0x15B : 0x115),
        iceChop(GameConstants.GMS ? 0x15C : 0x116),
        icePanic(GameConstants.GMS ? 0x15D : 0x117),
        iceDoubleJump(GameConstants.GMS ? 0x15E : 0x118),
        shockwave(GameConstants.GMS ? 0x169 : 0x124),
        demolition(GameConstants.GMS ? 0x16A : 0x125),
        snatch(GameConstants.GMS ? 0x16B : 0x126),
        windspear(GameConstants.GMS ? 0x16C : 0x127),
        windshot(GameConstants.GMS ? 0x16D : 0x128);
        public int i;

        private Delay(int i) {
            this.i = i;
        }

        public static Delay fromString(String s) {
            for (Delay b : Delay.values()) {
                if (b.name().equalsIgnoreCase(s)) {
                    return b;
                }
            }
            return null;
        }
    }
}
