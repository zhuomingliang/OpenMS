/*     */ package client;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public enum MapleJob
/*     */ {
/*   4 */   BEGINNER(0), 
/*   5 */   WARRIOR(100), 
/*   6 */   FIGHTER(110), 
/*   7 */   CRUSADER(111), 
/*   8 */   HERO(112), 
/*   9 */   PAGE(120), 
/*  10 */   WHITEKNIGHT(121), 
/*  11 */   PALADIN(122), 
/*  12 */   SPEARMAN(130), 
/*  13 */   DRAGONKNIGHT(131), 
/*  14 */   DARKKNIGHT(132), 
/*  15 */   MAGICIAN(200), 
/*  16 */   FP_WIZARD(210), 
/*  17 */   FP_MAGE(211), 
/*  18 */   FP_ARCHMAGE(212), 
/*  19 */   IL_WIZARD(220), 
/*  20 */   IL_MAGE(221), 
/*  21 */   IL_ARCHMAGE(222), 
/*  22 */   CLERIC(230), 
/*  23 */   PRIEST(231), 
/*  24 */   BISHOP(232), 
/*  25 */   BOWMAN(300), 
/*  26 */   HUNTER(310), 
/*  27 */   RANGER(311), 
/*  28 */   BOWMASTER(312), 
/*  29 */   CROSSBOWMAN(320), 
/*  30 */   SNIPER(321), 
/*  31 */   MARKSMAN(322), 
/*  32 */   THIEF(400), 
/*  33 */   ASSASSIN(410), 
/*  34 */   HERMIT(411), 
/*  35 */   NIGHTLORD(412), 
/*  36 */   BANDIT(420), 
/*  37 */   CHIEFBANDIT(421), 
/*  38 */   SHADOWER(422), 
/*  39 */   BLADE_RECRUIT(430), 
/*  40 */   BLADE_ACOLYTE(431), 
/*  41 */   BLADE_SPECIALIST(432), 
/*  42 */   BLADE_LORD(433), 
/*  43 */   BLADE_MASTER(434), 
/*  44 */   PIRATE(500), 
/*  45 */   PIRATE_CS(501), 
/*  46 */   JETT1(508), 
/*  47 */   BRAWLER(510), 
/*  48 */   MARAUDER(511), 
/*  49 */   BUCCANEER(512), 
/*  50 */   GUNSLINGER(520), 
/*  51 */   OUTLAW(521), 
/*  52 */   CORSAIR(522), 
/*  53 */   CANNONEER(530), 
/*  54 */   CANNON_BLASTER(531), 
/*  55 */   CANNON_MASTER(532), 
/*  56 */   JETT2(570), 
/*  57 */   JETT3(571), 
/*  58 */   JETT4(572), 
/*  59 */   MANAGER(800), 
/*  60 */   GM(900), 
/*  61 */   SUPERGM(910), 
/*  62 */   NOBLESSE(1000), 
/*  63 */   DAWNWARRIOR1(1100), 
/*  64 */   DAWNWARRIOR2(1110), 
/*  65 */   DAWNWARRIOR3(1111), 
/*  66 */   DAWNWARRIOR4(1112), 
/*  67 */   BLAZEWIZARD1(1200), 
/*  68 */   BLAZEWIZARD2(1210), 
/*  69 */   BLAZEWIZARD3(1211), 
/*  70 */   BLAZEWIZARD4(1212), 
/*  71 */   WINDARCHER1(1300), 
/*  72 */   WINDARCHER2(1310), 
/*  73 */   WINDARCHER3(1311), 
/*  74 */   WINDARCHER4(1312), 
/*  75 */   NIGHTWALKER1(1400), 
/*  76 */   NIGHTWALKER2(1410), 
/*  77 */   NIGHTWALKER3(1411), 
/*  78 */   NIGHTWALKER4(1412), 
/*  79 */   THUNDERBREAKER1(1500), 
/*  80 */   THUNDERBREAKER2(1510), 
/*  81 */   THUNDERBREAKER3(1511), 
/*  82 */   THUNDERBREAKER4(1512), 
/*  83 */   LEGEND(2000), 
/*  84 */   EVAN_NOOB(2001), 
/*  85 */   ARAN1(2100), 
/*  86 */   ARAN2(2110), 
/*  87 */   ARAN3(2111), 
/*  88 */   ARAN4(2112), 
/*  89 */   EVAN1(2200), 
/*  90 */   EVAN2(2210), 
/*  91 */   EVAN3(2211), 
/*  92 */   EVAN4(2212), 
/*  93 */   EVAN5(2213), 
/*  94 */   EVAN6(2214), 
/*  95 */   EVAN7(2215), 
/*  96 */   EVAN8(2216), 
/*  97 */   EVAN9(2217), 
/*  98 */   EVAN10(2218), 
/*  99 */   MERCEDES_NOOB(2002), 
/* 100 */   MERCEDES1(2300), 
/* 101 */   MERCEDES2(2310), 
/* 102 */   MERCEDES3(2311), 
/* 103 */   MERCEDES4(2312), 
/* 104 */   PHANTOM_NOOB(2003), 
/* 105 */   PHANTOM1(2400), 
/* 106 */   PHANTOM2(2410), 
/* 107 */   PHANTOM3(2411), 
/* 108 */   PHANTOM4(2412), 
/* 109 */   CITIZEN(3000), 
/* 110 */   CITIZEN_DS(3001), 
/* 111 */   DEMON_SLAYER1(3100), 
/* 112 */   DEMON_SLAYER2(3110), 
/* 113 */   DEMON_SLAYER3(3111), 
/* 114 */   DEMON_SLAYER4(3112), 
/* 115 */   BATTLE_MAGE_1(3200), 
/* 116 */   BATTLE_MAGE_2(3210), 
/* 117 */   BATTLE_MAGE_3(3211), 
/* 118 */   BATTLE_MAGE_4(3212), 
/* 119 */   WILD_HUNTER_1(3300), 
/* 120 */   WILD_HUNTER_2(3310), 
/* 121 */   WILD_HUNTER_3(3311), 
/* 122 */   WILD_HUNTER_4(3312), 
/* 123 */   MECHANIC_1(3500), 
/* 124 */   MECHANIC_2(3510), 
/* 125 */   MECHANIC_3(3511), 
/* 126 */   MECHANIC_4(3512), 
            MIHILE_0(5000),
            MIHILE_1(5100),
            MIHILE_2(5110),
            MIHILE_3(5111),
            MIHILE_4(5112),
/* 127 */   ADDITIONAL_SKILLS(9000);
/*     */ 
/*     */   private final int jobid;
/*     */ 
/* 131 */   private MapleJob(int id) { this.jobid = id; }
/*     */ 
/*     */   public int getId()
/*     */   {
/* 135 */     return this.jobid;
/*     */   }
/*     */ 
/*     */   public static String getName(MapleJob mjob) {
/* 139 */     return mjob.name();
/*     */   }
/*     */ 
/*     */   public static MapleJob getById(int id) {
/* 143 */     for (MapleJob l : values()) {
/* 144 */       if (l.getId() == id) {
/* 145 */         return l;
/*     */       }
/*     */     }
/* 148 */     return null;
/*     */   }
/*     */ 
/*     */   public static int getEncodingByJob(int job) {
/* 152 */     int exponent = job / 100;
/* 153 */     return (int)Math.pow(2.0D, exponent);
/*     */   }
/*     */ 
/*     */   public boolean isA(MapleJob basejob) {
/* 157 */     return (this.jobid >= basejob.getId()) && (basejob.getId() % 100 > 0 ? this.jobid / 10 == basejob.getId() / 10 : this.jobid / 100 == basejob.getId() / 100);
/*     */   }
/*     */ 
/*     */   public boolean isSeparatedSp() {
/* 161 */     return (this.jobid == 508) || (this.jobid / 10 == 57) || (this.jobid == 2001) || (this.jobid == 2002) || (this.jobid == 2003) || (this.jobid >= 2200);
/*     */   }
/*     */ 
/*     */   public int getBaseJob() {
/* 165 */     return this.jobid - this.jobid % 100;
/*     */   }
/*     */ 
/*     */   public int getBeginnerJob() {
/* 169 */     if (this.jobid / 1000 == 1)
/* 170 */       return 1000;
/* 171 */     if ((this.jobid == 2000) || (this.jobid / 100 == 21))
/* 172 */       return 2000;
/* 173 */     if ((this.jobid == 2001) || (this.jobid / 100 == 22))
/* 174 */       return 2001;
/* 175 */     if ((this.jobid == 2002) || (this.jobid / 100 == 23))
/* 176 */       return 2002;
/* 177 */     if ((this.jobid == 2003) || (this.jobid / 100 == 24))
/* 178 */       return 2003;
/* 179 */     if ((this.jobid == 3000) || (this.jobid >= 3200))
/* 180 */       return 3000;
/* 181 */     if ((this.jobid == 3001) || (this.jobid / 100 == 31))
/* 182 */       return 3001;
/* 183 */     if (this.jobid < 1000) {
/* 184 */       return 0;
/*     */     }
/* 186 */     System.out.println("getBeginnerJob() is unknown for jobid: " + this.jobid);
/* 187 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean isBeginner()
/*     */   {
/* 192 */     return this.jobid == getBeginnerJob();
/*     */   }
/*     */ 
/*     */   public boolean isWH() {
/* 196 */     return this.jobid / 100 == 33;
/*     */   }
/*     */ 
/*     */   public boolean isEvan() {
/* 200 */     return (this.jobid == 2001) || (this.jobid / 100 == 22);
/*     */   }
/*     */ 
/*     */   public boolean isEvanButNotNoob() {
/* 204 */     return this.jobid / 100 == 22;
/*     */   }
/*     */ 
/*     */   public boolean isDemonSlayer() {
/* 208 */     return (this.jobid == 3001) || (this.jobid / 100 == 31);
/*     */   }
/*     */ 
/*     */   public boolean isDemonSlayerButNotNoob() {
/* 212 */     return this.jobid / 100 == 31;
/*     */   }
/*     */ 
/*     */   public boolean isCygnus() {
/* 216 */     return (this.jobid >= 1000) && (this.jobid < 1600);
/*     */   }
/*     */ 
/*     */   public boolean isMechanic() {
/* 220 */     return this.jobid / 100 == 35;
/*     */   }
/*     */ 
/*     */   public boolean isMercedes() {
/* 224 */     return (this.jobid == 2002) || (this.jobid / 100 == 23);
/*     */   }

            public boolean isMihile() {
                return (this.jobid >= 5000) && (this.jobid < 5600);
            }
/*     */ 
/*     */   public boolean isCannonShooter() {
/* 228 */     return this.jobid / 10 == 53;
/*     */   }
/*     */ 
/*     */   public boolean isAran() {
/* 232 */     return (this.jobid == 2000) || (this.jobid / 100 == 21);
/*     */   }
/*     */ 
/*     */   public boolean isResistance() {
/* 236 */     return this.jobid / 1000 == 3;
/*     */   }
/*     */ 
/*     */   public boolean isJett() {
/* 240 */     return (this.jobid == 508) || (this.jobid / 10 == 57);
/*     */   }
/*     */ 
/*     */   public boolean isPhantom() {
/* 244 */     return (this.jobid == 2003) || (this.jobid / 100 == 24);
/*     */   }
/*     */ }

/* Location:           C:\Users\Sjögren\Desktop\lithium.jar
 * Qualified Name:     client.MapleJob
 * JD-Core Version:    0.6.0
 */