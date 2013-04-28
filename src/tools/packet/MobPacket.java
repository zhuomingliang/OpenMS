/*     */ package tools.packet;
/*     */ 
/*     */ import client.status.MonsterStatus;
/*     */ import client.status.MonsterStatusEffect;
/*     */ import handling.SendPacketOpcode;
/*     */ import java.awt.Point;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import server.life.ChangeableStats;
/*     */ import server.life.MapleMonster;
/*     */ import server.life.MapleMonsterStats;
/*     */ import server.life.MobSkill;
/*     */ import server.maps.MapleMap;
import server.maps.MapleNodes;
/*     */ import server.maps.MapleNodes.MapleNodeInfo;
/*     */ import server.movement.LifeMovementFragment;
/*     */ import tools.ConcurrentEnumMap;
/*     */ import tools.Pair;
/*     */ import tools.data.MaplePacketLittleEndianWriter;
/*     */ 
/*     */ public class MobPacket
/*     */ {
/*     */   public static byte[] damageMonster(int oid, long damage)
/*     */   {
/*  45 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/*  47 */     mplew.writeShort(SendPacketOpcode.DAMAGE_MONSTER.getValue());
/*  48 */     mplew.writeInt(oid);
/*  49 */     mplew.write(0);
/*  50 */     if (damage > 2147483647L)
/*  51 */       mplew.writeInt(2147483647);
/*     */     else {
/*  53 */       mplew.writeInt((int)damage);
/*     */     }
/*     */ 
/*  56 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] damageFriendlyMob(MapleMonster mob, long damage, boolean display) {
/*  60 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/*  62 */     mplew.writeShort(SendPacketOpcode.DAMAGE_MONSTER.getValue());
/*  63 */     mplew.writeInt(mob.getObjectId());
/*  64 */     mplew.write(display ? 1 : 2);
/*  65 */     if (damage > 2147483647L)
/*  66 */       mplew.writeInt(2147483647);
/*     */     else {
/*  68 */       mplew.writeInt((int)damage);
/*     */     }
/*  70 */     if (mob.getHp() > 2147483647L)
/*  71 */       mplew.writeInt((int)(mob.getHp() / mob.getMobMaxHp() * 2147483647.0D));
/*     */     else {
/*  73 */       mplew.writeInt((int)mob.getHp());
/*     */     }
/*  75 */     if (mob.getMobMaxHp() > 2147483647L)
/*  76 */       mplew.writeInt(2147483647);
/*     */     else {
/*  78 */       mplew.writeInt((int)mob.getMobMaxHp());
/*     */     }
/*     */ 
/*  81 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] killMonster(int oid, int animation) {
/*  85 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/*  87 */     mplew.writeShort(SendPacketOpcode.KILL_MONSTER.getValue());
/*  88 */     mplew.writeInt(oid);
/*  89 */     mplew.write(animation);
/*  90 */     if (animation == 4) {
/*  91 */       mplew.writeInt(-1);
/*     */     }
/*     */ 
/*  94 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] suckMonster(int oid, int chr) {
/*  98 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 100 */     mplew.writeShort(SendPacketOpcode.KILL_MONSTER.getValue());
/* 101 */     mplew.writeInt(oid);
/* 102 */     mplew.write(4);
/* 103 */     mplew.writeInt(chr);
/*     */ 
/* 105 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] healMonster(int oid, int heal) {
/* 109 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 111 */     mplew.writeShort(SendPacketOpcode.DAMAGE_MONSTER.getValue());
/* 112 */     mplew.writeInt(oid);
/* 113 */     mplew.write(0);
/* 114 */     mplew.writeInt(-heal);
/*     */ 
/* 116 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] MobToMobDamage(int oid, int dmg, int mobid) {
/* 120 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 122 */     mplew.writeShort(SendPacketOpcode.MOB_TO_MOB_DAMAGE.getValue());
/* 123 */     mplew.writeInt(oid);
/* 124 */     mplew.write(0);
/* 125 */     mplew.writeInt(dmg);
/* 126 */     mplew.writeInt(mobid);
/* 127 */     mplew.write(1);
/*     */ 
/* 129 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] getMobSkillEffect(int oid, int skillid, int cid, int skilllevel) {
/* 133 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 135 */     mplew.writeShort(SendPacketOpcode.SKILL_EFFECT_MOB.getValue());
/* 136 */     mplew.writeInt(oid);
/* 137 */     mplew.writeInt(skillid);
/* 138 */     mplew.writeInt(cid);
/* 139 */     mplew.writeShort(skilllevel);
/*     */ 
/* 141 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] getMobCoolEffect(int oid, int itemid) {
/* 145 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 147 */     mplew.writeShort(SendPacketOpcode.ITEM_EFFECT_MOB.getValue());
/* 148 */     mplew.writeInt(oid);
/* 149 */     mplew.writeInt(itemid);
/*     */ 
/* 151 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] showMonsterHP(int oid, int remhppercentage) {
/* 155 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 157 */     mplew.writeShort(SendPacketOpcode.SHOW_MONSTER_HP.getValue());
/* 158 */     mplew.writeInt(oid);
/* 159 */     mplew.write(remhppercentage);
/*     */ 
/* 161 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] showCygnusAttack(int oid) {
/* 165 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 167 */     mplew.writeShort(SendPacketOpcode.CYGNUS_ATTACK.getValue());
/* 168 */     mplew.writeInt(oid);
/*     */ 
/* 170 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] showMonsterResist(int oid) {
/* 174 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 176 */     mplew.writeShort(SendPacketOpcode.MONSTER_RESIST.getValue());
/* 177 */     mplew.writeInt(oid);
/* 178 */     mplew.writeInt(0);
/* 179 */     mplew.writeShort(1);
/* 180 */     mplew.writeInt(0);
/*     */ 
/* 182 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] showBossHP(MapleMonster mob) {
/* 186 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 188 */     mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
/* 189 */     mplew.write(5);
/* 190 */     mplew.writeInt(mob.getId() == 9400589 ? 9300184 : mob.getId());
/* 191 */     if (mob.getHp() > 2147483647L)
/* 192 */       mplew.writeInt((int)(mob.getHp() / mob.getMobMaxHp() * 2147483647.0D));
/*     */     else {
/* 194 */       mplew.writeInt((int)mob.getHp());
/*     */     }
/* 196 */     if (mob.getMobMaxHp() > 2147483647L)
/* 197 */       mplew.writeInt(2147483647);
/*     */     else {
/* 199 */       mplew.writeInt((int)mob.getMobMaxHp());
/*     */     }
/* 201 */     mplew.write(mob.getStats().getTagColor());
/* 202 */     mplew.write(mob.getStats().getTagBgColor());
/*     */ 
/* 204 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] showBossHP(int monsterId, long currentHp, long maxHp) {
/* 208 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 210 */     mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
/* 211 */     mplew.write(5);
/* 212 */     mplew.writeInt(monsterId);
/* 213 */     if (currentHp > 2147483647L)
/* 214 */       mplew.writeInt((int)(currentHp / maxHp * 2147483647.0D));
/*     */     else {
/* 216 */       mplew.writeInt((int)(currentHp <= 0L ? -1L : currentHp));
/*     */     }
/* 218 */     if (maxHp > 2147483647L)
/* 219 */       mplew.writeInt(2147483647);
/*     */     else {
/* 221 */       mplew.writeInt((int)maxHp);
/*     */     }
/* 223 */     mplew.write(6);
/* 224 */     mplew.write(5);
/*     */ 
/* 229 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] moveMonster(boolean useskill, int skill, int unk, int oid, Point startPos, List<LifeMovementFragment> moves) {
/* 233 */     return moveMonster(useskill, skill, unk, oid, startPos, moves, null, null);
/*     */   }
/*     */ 
/*     */   public static byte[] moveMonster(boolean useskill, int skill, int unk, int oid, Point startPos, List<LifeMovementFragment> moves, List<Integer> unk2, List<Pair<Integer, Integer>> unk3) {
/* 237 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 239 */     mplew.writeShort(SendPacketOpcode.MOVE_MONSTER.getValue());
/* 240 */     mplew.writeInt(oid);
/* 241 */     mplew.write(useskill ? 1 : 0);
/* 242 */     mplew.write(skill);
/* 243 */     mplew.writeInt(unk);
/* 244 */     mplew.write(unk3 == null ? 0 : unk3.size());
/* 245 */     if (unk3 != null) {
/* 246 */       for (Pair i : unk3) {
/* 247 */         mplew.writeShort(((Integer)i.left).intValue());
/* 248 */         mplew.writeShort(((Integer)i.right).intValue());
/*     */       }
/*     */     }
/* 251 */     mplew.write(unk2 == null ? 0 : unk2.size());
/* 252 */     if (unk2 != null) {
/* 253 */       for (Integer i : unk2) {
/* 254 */         mplew.writeShort(i.intValue());
/*     */       }
/*     */     }
/* 257 */     mplew.writePos(startPos);
/* 258 */     mplew.writeShort(8);
/* 259 */     mplew.writeShort(1);
/* 260 */     PacketHelper.serializeMovementList(mplew, moves);
/*     */ 
/* 262 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] spawnMonster(MapleMonster life, int spawnType, int link) {
/* 266 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 268 */     mplew.writeShort(SendPacketOpcode.SPAWN_MONSTER.getValue());
/* 269 */     mplew.writeInt(life.getObjectId());
/* 270 */     mplew.write(1);
/* 271 */     mplew.writeInt(life.getId());
/* 272 */     addMonsterStatus(mplew, life);
/* 273 */     mplew.writePos(life.getTruePosition());
/* 274 */     mplew.write(life.getStance());
/* 275 */     mplew.writeShort(0);
/* 276 */     mplew.writeShort(life.getFh());
/* 277 */     mplew.write(spawnType);
/* 278 */     if ((spawnType == -3) || (spawnType >= 0)) {
/* 279 */       mplew.writeInt(link);
/*     */     }
/* 281 */     mplew.write(life.getCarnivalTeam());
/* 282 */     mplew.writeInt(63000);
/* 283 */     mplew.writeInt(0);
/* 284 */     mplew.writeInt(0);
/* 285 */     mplew.write(-1);
/* 286 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static void addMonsterStatus(MaplePacketLittleEndianWriter mplew, MapleMonster life) {
/* 290 */     if (life.getStati().size() <= 1) {
/* 291 */       life.addEmpty();
/*     */     }
/* 293 */     mplew.write(life.getChangedStats() != null ? 1 : 0);
/* 294 */     if (life.getChangedStats() != null) {
/* 295 */       mplew.writeInt(life.getChangedStats().hp > 2147483647L ? 2147483647 : (int)life.getChangedStats().hp);
/* 296 */       mplew.writeInt(life.getChangedStats().mp);
/* 297 */       mplew.writeInt(life.getChangedStats().exp);
/* 298 */       mplew.writeInt(life.getChangedStats().watk);
/* 299 */       mplew.writeInt(life.getChangedStats().matk);
/* 300 */       mplew.writeInt(life.getChangedStats().PDRate);
/* 301 */       mplew.writeInt(life.getChangedStats().MDRate);
/* 302 */       mplew.writeInt(life.getChangedStats().acc);
/* 303 */       mplew.writeInt(life.getChangedStats().eva);
/* 304 */       mplew.writeInt(life.getChangedStats().pushed);
/* 305 */       mplew.writeInt(life.getChangedStats().level);
/*     */     }
/* 307 */     boolean ignore_imm = (life.getStati().containsKey(MonsterStatus.WEAPON_DAMAGE_REFLECT)) || (life.getStati().containsKey(MonsterStatus.MAGIC_DAMAGE_REFLECT));
/* 308 */      Collection<MonsterStatusEffect> buffs = life.getStati().values();
/* 309 */     getLongMask_NoRef(mplew, buffs, ignore_imm);
/* 310 */     for (MonsterStatusEffect buff : buffs)
/* 311 */       if ((buff != null) && (buff.getStati() != MonsterStatus.WEAPON_DAMAGE_REFLECT) && (buff.getStati() != MonsterStatus.MAGIC_DAMAGE_REFLECT) && ((!ignore_imm) || ((buff.getStati() != MonsterStatus.WEAPON_IMMUNITY) && (buff.getStati() != MonsterStatus.MAGIC_IMMUNITY) && (buff.getStati() != MonsterStatus.DAMAGE_IMMUNITY)))) {
/* 312 */         if ((buff.getStati() != MonsterStatus.SUMMON) && (buff.getStati() != MonsterStatus.EMPTY_3)) {
/* 313 */           if ((buff.getStati() == MonsterStatus.EMPTY_1) || (buff.getStati() == MonsterStatus.EMPTY_2) || (buff.getStati() == MonsterStatus.EMPTY_3) || (buff.getStati() == MonsterStatus.EMPTY_4) || (buff.getStati() == MonsterStatus.EMPTY_5) || (buff.getStati() == MonsterStatus.EMPTY_6)) {
/* 314 */             mplew.writeShort(Integer.valueOf((int)System.currentTimeMillis()).shortValue());
/* 315 */             mplew.writeShort(0);
/* 316 */           } else if (buff.getStati() == MonsterStatus.EMPTY_7) {
/* 317 */             mplew.write(0);
/*     */           } else {
/* 319 */             mplew.writeInt(buff.getX().intValue());
/*     */           }
/* 321 */           if (buff.getMobSkill() != null) {
/* 322 */             mplew.writeShort(buff.getMobSkill().getSkillId());
/* 323 */             mplew.writeShort(buff.getMobSkill().getSkillLevel());
/* 324 */           } else if (buff.getSkill() > 0) {
/* 325 */             mplew.writeInt(buff.getSkill());
/*     */           }
/*     */         }
/* 328 */         if (buff.getStati() != MonsterStatus.EMPTY_7) {
/* 329 */           mplew.writeShort(buff.getStati().isEmpty() ? 0 : buff.getStati() == MonsterStatus.HYPNOTIZE ? 40 : 1);
/* 330 */           if ((buff.getStati() == MonsterStatus.EMPTY_1) || (buff.getStati() == MonsterStatus.EMPTY_3))
/* 331 */             mplew.writeShort(0);
/* 332 */           else if ((buff.getStati() == MonsterStatus.EMPTY_4) || (buff.getStati() == MonsterStatus.EMPTY_5))
/* 333 */             mplew.writeInt(0);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public static byte[] controlMonster(MapleMonster life, boolean newSpawn, boolean aggro)
/*     */   {
/* 341 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 343 */     mplew.writeShort(SendPacketOpcode.SPAWN_MONSTER_CONTROL.getValue());
/* 344 */     mplew.write(aggro ? 2 : 1);
/* 345 */     mplew.writeInt(life.getObjectId());
/* 346 */     mplew.write(1);
/* 347 */     mplew.writeInt(life.getId());
/* 348 */     addMonsterStatus(mplew, life);
/*     */ 
/* 350 */     mplew.writePos(life.getTruePosition());
/* 351 */     mplew.write(life.getStance());
/* 352 */     mplew.writeShort(0);
/* 353 */     mplew.writeShort(life.getFh());
/* 354 */     mplew.write(newSpawn ? -2 : life.isFake() ? -4 : -1);
/* 355 */     mplew.write(life.getCarnivalTeam());
/* 356 */     mplew.writeInt(63000);
/* 357 */     mplew.writeInt(0);
/* 358 */     mplew.writeInt(0);
/* 359 */     mplew.write(-1);
/*     */ 
/* 361 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] stopControllingMonster(int oid) {
/* 365 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 367 */     mplew.writeShort(SendPacketOpcode.SPAWN_MONSTER_CONTROL.getValue());
/* 368 */     mplew.write(0);
/* 369 */     mplew.writeInt(oid);
/*     */ 
/* 371 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] makeMonsterReal(MapleMonster life) {
/* 375 */     return spawnMonster(life, -1, 0);
/*     */   }
/*     */ 
/*     */   public static byte[] makeMonsterFake(MapleMonster life) {
/* 379 */     return spawnMonster(life, -4, 0);
/*     */   }
/*     */ 
/*     */   public static byte[] makeMonsterEffect(MapleMonster life, int effect) {
/* 383 */     return spawnMonster(life, effect, 0);
/*     */   }
/*     */ 
/*     */   public static byte[] moveMonsterResponse(int objectid, short moveid, int currentMp, boolean useSkills, int skillId, int skillLevel) {
/* 387 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 389 */     mplew.writeShort(SendPacketOpcode.MOVE_MONSTER_RESPONSE.getValue());
/* 390 */     mplew.writeInt(objectid);
/* 391 */     mplew.writeShort(moveid);
/* 392 */     mplew.write(useSkills ? 1 : 0);
/* 393 */     mplew.writeShort(currentMp);
/* 394 */     mplew.write(skillId);
/* 395 */     mplew.write(skillLevel);
/* 396 */     mplew.writeInt(0);
/*     */ 
/* 398 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   private static void getLongMask_NoRef(MaplePacketLittleEndianWriter mplew, Collection<MonsterStatusEffect> ss, boolean ignore_imm) {
/* 402 */     int[] mask = new int[8];
/* 403 */     for (MonsterStatusEffect statup : ss) {
/* 404 */       if ((statup != null) && (statup.getStati() != MonsterStatus.WEAPON_DAMAGE_REFLECT) && (statup.getStati() != MonsterStatus.MAGIC_DAMAGE_REFLECT) && ((!ignore_imm) || ((statup.getStati() != MonsterStatus.WEAPON_IMMUNITY) && (statup.getStati() != MonsterStatus.MAGIC_IMMUNITY) && (statup.getStati() != MonsterStatus.DAMAGE_IMMUNITY)))) {
/* 405 */         mask[(statup.getStati().getPosition() - 1)] |= statup.getStati().getValue();
/*     */       }
/*     */     }
/* 408 */     for (int i = mask.length; i >= 1; i--)
/* 409 */       mplew.writeInt(mask[(i - 1)]);
/*     */   }
/*     */ 
/*     */   public static byte[] applyMonsterStatus(int oid, MonsterStatus mse, int x, MobSkill skil)
/*     */   {
/* 414 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 416 */     mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
/* 417 */     mplew.writeInt(oid);
/* 418 */     PacketHelper.writeSingleMask(mplew, mse);
/*     */ 
/* 420 */     mplew.writeInt(x);
/* 421 */     mplew.writeShort(skil.getSkillId());
/* 422 */     mplew.writeShort(skil.getSkillLevel());
/* 423 */     mplew.writeShort(mse.isEmpty() ? 1 : 0);
/*     */ 
/* 425 */     mplew.writeShort(0);
/* 426 */     mplew.write(1);
/* 427 */     mplew.write(1);
/*     */ 
/* 429 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] applyMonsterStatus(MapleMonster mons, MonsterStatusEffect ms) {
/* 433 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 435 */     mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
/* 436 */     mplew.writeInt(mons.getObjectId());
/* 437 */     PacketHelper.writeSingleMask(mplew, ms.getStati());
/*     */ 
/* 439 */     mplew.writeInt(ms.getX().intValue());
/* 440 */     if (ms.isMonsterSkill()) {
/* 441 */       mplew.writeShort(ms.getMobSkill().getSkillId());
/* 442 */       mplew.writeShort(ms.getMobSkill().getSkillLevel());
/* 443 */     } else if (ms.getSkill() > 0) {
/* 444 */       mplew.writeInt(ms.getSkill());
/*     */     }
/* 446 */     mplew.writeShort(ms.getStati().isEmpty() ? 1 : 0);
/*     */ 
/* 448 */     mplew.writeShort(0);
/* 449 */     mplew.write(1);
/* 450 */     mplew.write(1);
/*     */ 
/* 452 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] applyMonsterStatus(MapleMonster mons, List<MonsterStatusEffect> mse) {
/* 456 */     if ((mse.size() <= 0) || (mse.get(0) == null)) {
/* 457 */       return CWvsContext.enableActions();
/*     */     }
/* 459 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 461 */     mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
/* 462 */     mplew.writeInt(mons.getObjectId());
/* 463 */     MonsterStatusEffect ms = (MonsterStatusEffect)mse.get(0);
/* 464 */     if (ms.getStati() == MonsterStatus.POISON) {
/* 465 */       PacketHelper.writeSingleMask(mplew, MonsterStatus.EMPTY);
/* 466 */       mplew.write(mse.size());
/* 467 */       for (MonsterStatusEffect m : mse) {
/* 468 */         mplew.writeInt(m.getFromID());
/* 469 */         if (m.isMonsterSkill()) {
/* 470 */           mplew.writeShort(m.getMobSkill().getSkillId());
/* 471 */           mplew.writeShort(m.getMobSkill().getSkillLevel());
/* 472 */         } else if (m.getSkill() > 0) {
/* 473 */           mplew.writeInt(m.getSkill());
/*     */         }
/* 475 */         mplew.writeInt(m.getX().intValue());
/* 476 */         mplew.writeInt(1000);
/* 477 */         mplew.writeInt(0);
/* 478 */         mplew.writeInt(5);
/* 479 */         mplew.writeInt(0);
/*     */       }
/* 481 */       mplew.writeShort(300);
/* 482 */       mplew.write(1);
/* 483 */       mplew.write(1);
/*     */     } else {
/* 485 */       PacketHelper.writeSingleMask(mplew, ms.getStati());
/*     */ 
/* 487 */       mplew.writeInt(ms.getX().intValue());
/* 488 */       if (ms.isMonsterSkill()) {
/* 489 */         mplew.writeShort(ms.getMobSkill().getSkillId());
/* 490 */         mplew.writeShort(ms.getMobSkill().getSkillLevel());
/* 491 */       } else if (ms.getSkill() > 0) {
/* 492 */         mplew.writeInt(ms.getSkill());
/*     */       }
/* 494 */       mplew.writeShort(0);
/*     */ 
/* 496 */       mplew.writeShort(0);
/* 497 */       mplew.write(1);
/* 498 */       mplew.write(1);
/*     */     }
/*     */ 
/* 501 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] applyMonsterStatus(int oid, Map<MonsterStatus, Integer> stati, List<Integer> reflection, MobSkill skil) {
/* 505 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 507 */     mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
/* 508 */     mplew.writeInt(oid);
/* 509 */     PacketHelper.writeMask(mplew, stati.keySet());
/*     */ 
/* 511 */     for (Map.Entry mse : stati.entrySet()) {
/* 512 */       mplew.writeInt(((Integer)mse.getValue()).intValue());
/* 513 */       mplew.writeShort(skil.getSkillId());
/* 514 */       mplew.writeShort(skil.getSkillLevel());
/* 515 */       mplew.writeShort(0);
/*     */     }
/*     */ 
/* 518 */     for (Integer ref : reflection) {
/* 519 */       mplew.writeInt(ref.intValue());
/*     */     }
/* 521 */     mplew.writeLong(0L);
/* 522 */     mplew.writeShort(0);
/*     */ 
/* 524 */     int size = stati.size();
/* 525 */     if (reflection.size() > 0) {
/* 526 */       size /= 2;
/*     */     }
/* 528 */     mplew.write(size);
/* 529 */     mplew.write(1);
/*     */ 
/* 531 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] cancelMonsterStatus(int oid, MonsterStatus stat) {
/* 535 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 537 */     mplew.writeShort(SendPacketOpcode.CANCEL_MONSTER_STATUS.getValue());
/* 538 */     mplew.writeInt(oid);
/* 539 */     PacketHelper.writeSingleMask(mplew, stat);
/* 540 */     mplew.write(1);
/* 541 */     mplew.write(2);
/*     */ 
/* 543 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] cancelPoison(int oid, MonsterStatusEffect m) {
/* 547 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 549 */     mplew.writeShort(SendPacketOpcode.CANCEL_MONSTER_STATUS.getValue());
/* 550 */     mplew.writeInt(oid);
/* 551 */     PacketHelper.writeSingleMask(mplew, MonsterStatus.EMPTY);
/* 552 */     mplew.writeInt(0);
/* 553 */     mplew.writeInt(1);
/* 554 */     mplew.writeInt(m.getFromID());
/* 555 */     if (m.isMonsterSkill()) {
/* 556 */       mplew.writeShort(m.getMobSkill().getSkillId());
/* 557 */       mplew.writeShort(m.getMobSkill().getSkillLevel());
/* 558 */     } else if (m.getSkill() > 0) {
/* 559 */       mplew.writeInt(m.getSkill());
/*     */     }
/* 561 */     mplew.write(3);
/*     */ 
/* 563 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] talkMonster(int oid, int itemId, String msg) {
/* 567 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 569 */     mplew.writeShort(SendPacketOpcode.TALK_MONSTER.getValue());
/* 570 */     mplew.writeInt(oid);
/* 571 */     mplew.writeInt(500);
/* 572 */     mplew.writeInt(itemId);
/* 573 */     mplew.write(itemId <= 0 ? 0 : 1);
/* 574 */     mplew.write((msg == null) || (msg.length() <= 0) ? 0 : 1);
/* 575 */     if ((msg != null) && (msg.length() > 0)) {
/* 576 */       mplew.writeMapleAsciiString(msg);
/*     */     }
/* 578 */     mplew.writeInt(1);
/*     */ 
/* 580 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] removeTalkMonster(int oid) {
/* 584 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 586 */     mplew.writeShort(SendPacketOpcode.REMOVE_TALK_MONSTER.getValue());
/* 587 */     mplew.writeInt(oid);
/*     */ 
/* 589 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getNodeProperties(MapleMonster objectid, MapleMap map) {
/* 593 */     if (objectid.getNodePacket() != null) {
/* 594 */       return objectid.getNodePacket();
/*     */     }
/* 596 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 598 */     mplew.writeShort(SendPacketOpcode.MONSTER_PROPERTIES.getValue());
/* 599 */     mplew.writeInt(objectid.getObjectId());
/* 600 */     mplew.writeInt(map.getNodes().size());
/* 601 */     mplew.writeInt(objectid.getPosition().x);
/* 602 */     mplew.writeInt(objectid.getPosition().y);
/* 603 */     for (MapleNodes.MapleNodeInfo mni : map.getNodes()) {
/* 604 */       mplew.writeInt(mni.x);
/* 605 */       mplew.writeInt(mni.y);
/* 606 */       mplew.writeInt(mni.attr);
/* 607 */       if (mni.attr == 2) {
/* 608 */         mplew.writeInt(500);
/*     */       }
/*     */     }
/* 611 */     mplew.writeInt(0);
/* 612 */     mplew.write(0);
/* 613 */     mplew.write(0);
/*     */ 
/* 615 */     objectid.setNodePacket(mplew.getPacket());
/* 616 */     return objectid.getNodePacket();
/*     */   }
/*     */ 
/*     */   public static byte[] showMagnet(int mobid, boolean success) {
/* 620 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 622 */     mplew.writeShort(SendPacketOpcode.SHOW_MAGNET.getValue());
/* 623 */     mplew.writeInt(mobid);
/* 624 */     mplew.write(success ? 1 : 0);
/* 625 */     mplew.write(0);
/*     */ 
/* 627 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] catchMonster(int mobid, int itemid, byte success) {
/* 631 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 633 */     mplew.writeShort(SendPacketOpcode.CATCH_MONSTER.getValue());
/* 634 */     mplew.writeInt(mobid);
/* 635 */     mplew.writeInt(itemid);
/* 636 */     mplew.write(success);
/*     */ 
/* 638 */     return mplew.getPacket();
/*     */   }
/*     */ }

/* Location:           C:\Users\Sjögren\Desktop\lithium.jar
 * Qualified Name:     tools.packet.MobPacket
 * JD-Core Version:    0.6.0
 */
       
       