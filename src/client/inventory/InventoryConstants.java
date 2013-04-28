/*     */ package client.inventory;
/*     */ 
/*     */ public class InventoryConstants
/*     */ {
/*     */   public static final short WEAPON = -11;
/*     */   public static final short MOUNT = -18;
/*     */   public static final short BOTTOM = -6;
/*     */   public static final short SHIELD = -10;
/*     */   public static final short MEDAL = -49;
/*     */   public static final short SHOE = -7;
/*     */   public static final short TOP = -5;
/*     */   public static final short EARRING = -4;
/*     */   public static final short HAT = -1;
/*     */   public static final short GLOVE = -8;
/*     */   public static final short CAPE = -9;
/*     */   public static final short FACE = -13;
/*     */   public static final short RING1 = -12;
/*     */   public static final short RING2 = -13;
/*     */   public static final short RING3 = -14;
/*     */   public static final short RING4 = -15;
/*     */   public static final short SADDLE = -19;
/*     */   public static final short PENDANT = -17;
/*     */   public static final short BELT = -50;
/*     */   public static final short PET_EQUIP = -123;
/*     */   public static final short AUTO_HP = -124;
/*     */   public static final short AUTO_MP = -125;
/*     */   public static final short CS_RING1 = -112;
/*     */   public static final short CS_RING2 = -113;
/*     */   public static final short CS_RING3 = -114;
/*     */   public static final short CS_RING4 = -115;
/*     */   public static final short CODEX = 55;
/*     */   public static final int LOCK = 1;
/*     */   public static final int SPIKES = 2;
/*     */   public static final int COLD = 4;
/*     */   public static final int UNTRADEABLE = 8;
/*     */   public static final int KARMA = 16;
/*     */   public static final int CHARM_EXP = 32;
/*     */   public static final int PET_COME = 128;
/*     */   public static final int UNKNOWN_SKILL = 256;
/*     */   public static final byte HIDDEN = 1;
/*     */   public static final byte RARE = 17;
/*     */   public static final byte EPIC = 18;
/*     */   public static final byte UNIQUE = 19;
/*     */   public static final byte LEGENDARY = 20;
/*     */ 
/*     */   public static boolean idMatchesSlot(int itemid, short slot)
/*     */   {
/*  42 */     return ((slot != -11) || (isWeapon(itemid))) && ((slot != -18) || (isMount(itemid))) && ((slot != -6) || (isBottom(itemid))) && ((slot != -10) || (isShield(itemid)) || (itemid / 10000 == 135)) && ((slot != -49) || (itemid / 10000 == 114)) && ((slot != -7) || (isShoe(itemid))) && ((slot != -5) || (isTop(itemid)) || (isOverall(itemid))) && ((slot != -4) || (isEarring(itemid))) && ((slot != -1) || (isHat(itemid))) && ((slot != -8) || (isGlove(itemid))) && ((slot != -9) || (isCape(itemid))) && ((slot != -13) || (isFaceAccessory(itemid))) && (((slot == -12) || (slot == -13) || (slot == -14) || (slot == -15) || (slot == -112) || (slot == -113) || (slot == -114) || (slot == -115)) && ((isRing(itemid)) && ((slot != -19) || (isSaddle(itemid))) && ((slot != -17) || (isNeckAccessory(itemid))) && ((slot != -50) || (isBelt(itemid))) && (((slot == -123) || (slot == -124) || (slot == -125)) && ((isPetEquip(itemid)) && ((slot != 55) || (itemid == 1172000))))));
/*     */   }
/*     */ 
/*     */   public static boolean isRechargeable(int itemid)
/*     */   {
/*  47 */     return (isThrowingStar(itemid)) || (isBullet(itemid));
/*     */   }
/*     */ 
/*     */   public static boolean isThrowingStar(int itemId) {
/*  51 */     return itemId / 10000 == 207;
/*     */   }
/*     */ 
/*     */   public static boolean isBullet(int itemId) {
/*  55 */     return itemId / 10000 == 233;
/*     */   }
/*     */ 
/*     */   public static boolean isArrowForCrossBow(int itemId) {
/*  59 */     return itemId / 1000 == 2061;
/*     */   }
/*     */ 
/*     */   public static boolean isArrowForBow(int itemId) {
/*  63 */     return itemId / 1000 == 2060;
/*     */   }
/*     */ 
/*     */   public static boolean isOverall(int itemId) {
/*  67 */     return itemId / 10000 == 105;
/*     */   }
/*     */ 
/*     */   public static boolean isHat(int itemid) {
/*  71 */     return itemid / 10000 == 100;
/*     */   }
/*     */ 
/*     */   public static boolean isFaceAccessory(int itemid) {
/*  75 */     return itemid / 10000 == 101;
/*     */   }
/*     */ 
/*     */   public static boolean isTop(int itemid) {
/*  79 */     return itemid / 10000 == 104;
/*     */   }
/*     */ 
/*     */   public static boolean isBottom(int itemid) {
/*  83 */     return itemid / 10000 == 106;
/*     */   }
/*     */ 
/*     */   public static boolean isShoe(int itemid) {
/*  87 */     return itemid / 10000 == 107;
/*     */   }
/*     */ 
/*     */   public static boolean isGlove(int itemid) {
/*  91 */     return itemid / 10000 == 108;
/*     */   }
/*     */ 
/*     */   public static boolean isCape(int itemid) {
/*  95 */     return itemid / 10000 == 110;
/*     */   }
/*     */ 
/*     */   public static boolean isNeckAccessory(int itemid) {
/*  99 */     return itemid / 10000 == 112;
/*     */   }
/*     */ 
/*     */   public static boolean isBelt(int itemid) {
/* 103 */     return itemid / 10000 == 113;
/*     */   }
/*     */ 
/*     */   public static boolean isEarring(int itemid) {
/* 107 */     return itemid / 10000 == 103;
/*     */   }
/*     */ 
/*     */   public static boolean isEyeAccessory(int itemid) {
/* 111 */     return itemid / 10000 == 102;
/*     */   }
/*     */ 
/*     */   public static boolean isArmor(int itemid) {
/* 115 */     return (isTop(itemid)) || (isBottom(itemid)) || (isOverall(itemid)) || (isHat(itemid)) || (isGlove(itemid)) || (isShoe(itemid)) || (isCape(itemid));
/*     */   }
/*     */ 
/*     */   public static boolean isAccessory(int id) {
/* 119 */     return (isFaceAccessory(id)) || (isNeckAccessory(id)) || (isEarring(id)) || (isEyeAccessory(id)) || (isBelt(id)) || (isMedal(id));
/*     */   }
/*     */ 
/*     */   public static boolean isMedal(int id) {
/* 123 */     return id / 10000 == 114;
/*     */   }
/*     */ 
/*     */   public static boolean isShield(int id) {
/* 127 */     return id / 10000 == 109;
/*     */   }
/*     */ 
/*     */   public static boolean isRing(int id) {
/* 131 */     return id / 10000 == 111;
/*     */   }
/*     */ 
/*     */   public static boolean isMount(int id) {
/* 135 */     return (id / 10000 == 190) || (id / 10000 == 193);
/*     */   }
/*     */ 
/*     */   public static boolean isSaddle(int id) {
/* 139 */     return id / 10000 == 191;
/*     */   }
/*     */ 
/*     */   public static boolean isTamingMob(int id) {
/* 143 */     return (isMount(id)) || (isSaddle(id));
/*     */   }
/*     */ 
/*     */   public static boolean isPetEquip(int id)
/*     */   {
/* 148 */     return (id / 10000 >= 180) && (id / 10000 <= 183);
/*     */   }
/*     */ 
/*     */   public static boolean isDragon(int id) {
/* 152 */     return (id / 10000 >= 194) && (id / 10000 <= 197);
/*     */   }
/*     */ 
/*     */   public static boolean isMechanic(int id) {
/* 156 */     return (id / 10000 >= 161) && (id / 10000 <= 165);
/*     */   }
/*     */ 
/*     */   public static boolean isMonsterBook(int id) {
/* 160 */     return id == 1172000;
/*     */   }
/*     */ 
/*     */   public static boolean isAndroid(int id) {
/* 164 */     return (id / 10000 >= 166) && (id / 10000 <= 167);
/*     */   }
/*     */ 
/*     */   public static boolean isWeapon(int id) {
/* 168 */     return ((id / 10000 >= 130) && (id / 10000 <= 153)) || (id / 10000 == 170);
/*     */   }
/*     */ 
/*     */   public static boolean isFamiliar(int id) {
/* 172 */     return id / 10000 == 996;
/*     */   }
/*     */ 
/*     */   public static boolean isConsume(int id) {
/* 176 */     return (id / 10000 >= 200) && (id / 10000 < 300);
/*     */   }
/*     */ 
/*     */   public static boolean isEtc(int id) {
/* 180 */     return (id / 10000 >= 400) && (id / 10000 < 500);
/*     */   }
/*     */ 
/*     */   public static boolean isPet(int id) {
/* 184 */     return id / 10000 == 500;
/*     */   }
/*     */ 
/*     */   public static boolean isInstall(int id) {
/* 188 */     return (id / 10000 >= 300) && (id / 10000 < 400);
/*     */   }
/*     */ 
/*     */   public static boolean isCashNotEquip(int id) {
/* 192 */     return id / 1000 >= 500;
/*     */   }
/*     */ 
/*     */   public static boolean isSpecial(int id) {
/* 196 */     return (id / 10000 >= 900) && (id / 10000 < 1000);
/*     */   }
/*     */ 
/*     */   public static boolean isFriendshipRing(int itemid) {
/* 200 */     switch (itemid) {
/*     */     case 1112800:
/*     */     case 1112801:
/*     */     case 1112802:
/*     */     case 1112810:
/*     */     case 1112811:
/*     */     case 1112816:
/*     */     case 1112817:
/* 208 */       return true;
/*     */     case 1112803:
/*     */     case 1112804:
/*     */     case 1112805:
/*     */     case 1112806:
/*     */     case 1112807:
/*     */     case 1112808:
/*     */     case 1112809:
/*     */     case 1112812:
/*     */     case 1112813:
/*     */     case 1112814:
/* 210 */     case 1112815: } return false;
/*     */   }
/*     */ 
/*     */   public static boolean isCrushRing(int itemid)
/*     */   {
/* 215 */     switch (itemid) {
/*     */     case 1112001:
/*     */     case 1112002:
/*     */     case 1112003:
/*     */     case 1112005:
/*     */     case 1112006:
/*     */     case 1112007:
/*     */     case 1112012:
/*     */     case 1112015:
/* 224 */       return true;
/*     */     case 1112004:
/*     */     case 1112008:
/*     */     case 1112009:
/*     */     case 1112010:
/*     */     case 1112011:
/*     */     case 1112013:
/* 226 */     case 1112014: } return false;
/*     */   }
/*     */ 
/*     */   public static boolean isWeddingRing(int itemid)
/*     */   {
/* 231 */     switch (itemid) {
/*     */     case 1112300:
/*     */     case 1112301:
/*     */     case 1112302:
/*     */     case 1112303:
/*     */     case 1112304:
/*     */     case 1112305:
/*     */     case 1112306:
/*     */     case 1112307:
/*     */     case 1112308:
/*     */     case 1112309:
/*     */     case 1112310:
/*     */     case 1112311:
/*     */     case 1112315:
/*     */     case 1112316:
/*     */     case 1112317:
/*     */     case 1112318:
/*     */     case 1112319:
/*     */     case 1112320:
/*     */     case 1112803:
/*     */     case 1112806:
/*     */     case 1112807:
/*     */     case 1112808:
/*     */     case 1112809:
/* 255 */       return true;
/*     */     }
/* 257 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean is1hWeapon(int id)
/*     */   {
/* 262 */     return id / 10000 < 140;
/*     */   }
/*     */ 
/*     */   public static boolean is2hWeapon(int id) {
/* 266 */     return !is1hWeapon(id);
/*     */   }
/*     */ }

/* Location:           C:\Users\Sjögren\Desktop\lithium.jar
 * Qualified Name:     client.inventory.InventoryConstants
 * JD-Core Version:    0.6.0
 */