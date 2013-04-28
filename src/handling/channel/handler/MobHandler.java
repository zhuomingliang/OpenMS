/*     */ package handling.channel.handler;
/*     */ 
/*     */ import client.MapleCharacter;
/*     */ import client.MapleCharacterUtil;
/*     */ import client.MapleClient;
/*     */ import client.MonsterFamiliar;
/*     */ import client.SkillFactory;
/*     */ import client.SkillFactory.FamiliarEntry;
/*     */ import client.anticheat.CheatTracker;
/*     */ import client.anticheat.CheatingOffense;
/*     */ import client.inventory.Item;
/*     */ import client.inventory.MapleInventory;
/*     */ import client.inventory.MapleInventoryType;
/*     */ import client.status.MonsterStatus;
/*     */ import client.status.MonsterStatusEffect;
/*     */ import constants.GameConstants;
/*     */ import handling.world.MapleParty;
/*     */ import handling.world.MaplePartyCharacter;
/*     */ import java.awt.Point;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.mina.common.IoSession;
/*     */ import scripting.EventInstanceManager;
/*     */ import server.MapleInventoryManipulator;
/*     */ import server.MapleItemInformationProvider;
/*     */ import server.Randomizer;
/*     */ import server.StructFamiliar;
/*     */ import server.life.MapleLifeFactory;
/*     */ import server.life.MapleMonster;
/*     */ import server.life.MapleMonsterStats;
/*     */ import server.life.MobSkill;
/*     */ import server.life.MobSkillFactory;
/*     */ import server.maps.MapleMap;
import server.maps.MapleNodes;
/*     */ import server.maps.MapleNodes.MapleNodeInfo;
/*     */ import server.movement.AbsoluteLifeMovement;
/*     */ import server.movement.LifeMovement;
/*     */ import server.movement.LifeMovementFragment;
/*     */ import tools.FileoutputUtil;
/*     */ import tools.Pair;
/*     */ import tools.Triple;
/*     */ import tools.data.LittleEndianAccessor;
/*     */ import tools.packet.CField;
/*     */ import tools.packet.CWvsContext;
/*     */ import tools.packet.MobPacket;
/*     */ 
/*     */ public class MobHandler
/*     */ {
/*     */   public static final void MoveMonster(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
/*     */   {
/*  65 */     if ((chr == null) || (chr.getMap() == null)) {
/*  66 */       return;
/*     */     }
/*     */ 
/*  69 */     int oid = slea.readInt();
/*     */ 
/*  76 */     MapleMonster monster = chr.getMap().getMonsterByOid(oid);
/*  77 */     slea.readByte();
/*  78 */     if (monster == null) {
/*  79 */       return;
/*     */     }
/*  81 */     if (monster.getLinkCID() > 0) {
/*  82 */       return;
/*     */     }
/*  84 */     short moveid = slea.readShort();
/*  85 */     boolean useSkill = slea.readByte() > 0;
/*  86 */     byte skill = slea.readByte();
/*  87 */     int unk = slea.readInt();
/*  88 */     int realskill = 0;
/*  89 */     int level = 0;
/*     */ 
/*  91 */     if (useSkill) {
/*  92 */       byte size = monster.getNoSkills();
/*  93 */       boolean used = false;
/*     */ 
/*  95 */       if (size > 0) {
/*  96 */         final Pair<Integer, Integer> skillToUse = monster.getSkills().get((byte) Randomizer.nextInt(size));
/*  97 */         realskill = ((Integer)skillToUse.getLeft()).intValue();
/*  98 */         level = ((Integer)skillToUse.getRight()).intValue();
/*     */ 
/* 100 */         MobSkill mobSkill = MobSkillFactory.getMobSkill(realskill, level);
/*     */ 
/* 102 */         if ((mobSkill != null) && (!mobSkill.checkCurrentBuff(chr, monster))) {
/* 103 */           long now = System.currentTimeMillis();
/* 104 */           long ls = monster.getLastSkillUsed(realskill);
/*     */ 
/* 106 */           if ((ls == 0L) || ((now - ls > mobSkill.getCoolTime()) && (!mobSkill.onlyOnce()))) {
/* 107 */             monster.setLastSkillUsed(realskill, now, mobSkill.getCoolTime());
/*     */ 
/* 109 */             int reqHp = (int)((float)monster.getHp() / (float)monster.getMobMaxHp() * 100.0F);
/* 110 */             if (reqHp <= mobSkill.getHP()) {
/* 111 */               used = true;
/* 112 */               mobSkill.applyEffect(chr, monster, true);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 117 */       if (!used) {
/* 118 */         realskill = 0;
/* 119 */         level = 0;
/*     */       }
/*     */     }
/* 122 */     final List<Pair<Integer, Integer>> unk3 = new ArrayList<>();
/* 123 */     byte size1 = slea.readByte();
/*     */ 
/* 145 */     for (int i = 0; i < size1; i++) {
/* 146 */       unk3.add(new Pair(Integer.valueOf(slea.readShort()), Integer.valueOf(slea.readShort())));
/*     */     }
/* 148 */     final List<Integer> unk2 = new ArrayList<>();
/* 149 */     byte size = slea.readByte();
/* 150 */     for (int i = 0; i < size; i++) {
/* 151 */       unk2.add(Integer.valueOf(slea.readShort()));
/*     */     }
/* 153 */     slea.skip(1);
/* 154 */     slea.skip(4);
/* 155 */     slea.skip(4);
/* 156 */     slea.skip(4);
/* 157 */     slea.skip(4);
/* 158 */     slea.skip(1);
/* 159 */     slea.skip(4);
/* 160 */     slea.skip(4);
/* 161 */     Point startPos = monster.getPosition();
/* 162 */         List<LifeMovementFragment> res = null;
/*     */     try {
/* 164 */       res = MovementParse.parseMovement(slea, 2);
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/* 166 */       FileoutputUtil.outputFileError("Log_Movement.rtf", e);
/* 167 */       FileoutputUtil.log("Log_Movement.rtf", "MOBID " + monster.getId() + ", AIOBE Type2:\n" + slea.toString(true));
/* 168 */       return;
/*     */     }
/* 170 */     if ((res != null) && (chr != null) && (res.size() > 0)) {
/* 171 */       MapleMap map = chr.getMap();
/* 172 */       for (LifeMovementFragment move : res) {
/* 173 */         if ((move instanceof AbsoluteLifeMovement)) {
/* 174 */           Point endPos = ((LifeMovement)move).getPosition();
/* 175 */           if ((endPos.x < map.getLeft() - 250) || (endPos.y < map.getTop() - 250) || (endPos.x > map.getRight() + 250) || (endPos.y > map.getBottom() + 250)) {
/* 176 */             chr.getCheatTracker().checkMoveMonster(endPos);
/* 177 */             return;
/*     */           }
/*     */         }
/*     */       }
/* 181 */       c.getSession().write(MobPacket.moveMonsterResponse(monster.getObjectId(), moveid, monster.getMp(), monster.isControllerHasAggro(), realskill, level));
/* 182 */       if (slea.available() != 36L)
/*     */       {
/* 185 */         FileoutputUtil.log("Log_Packet_Except.rtf", "slea.available != 35 (movement parsing error)\n" + slea.toString(true));
/*     */ 
/* 187 */         return;
/*     */       }
/* 189 */       MovementParse.updatePosition(res, monster, -1);
/* 190 */       Point endPos = monster.getTruePosition();
/* 191 */       map.moveMonster(monster, endPos);
/* 192 */       map.broadcastMessage(chr, MobPacket.moveMonster(useSkill, skill, unk, monster.getObjectId(), startPos, res, unk2, unk3), endPos);
/* 193 */       chr.getCheatTracker().checkMoveMonster(endPos);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void FriendlyDamage(LittleEndianAccessor slea, MapleCharacter chr) {
/* 198 */     MapleMap map = chr.getMap();
/* 199 */     if (map == null) {
/* 200 */       return;
/*     */     }
/* 202 */     MapleMonster mobfrom = map.getMonsterByOid(slea.readInt());
/* 203 */     slea.skip(4);
/* 204 */     MapleMonster mobto = map.getMonsterByOid(slea.readInt());
/*     */ 
/* 206 */     if ((mobfrom != null) && (mobto != null) && (mobto.getStats().isFriendly())) {
/* 207 */       int damage = mobto.getStats().getLevel() * Randomizer.nextInt(mobto.getStats().getLevel()) / 2;
/* 208 */       mobto.damage(chr, damage, true);
/* 209 */       checkShammos(chr, mobto, map);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void MobBomb(LittleEndianAccessor slea, MapleCharacter chr) {
/* 214 */     MapleMap map = chr.getMap();
/* 215 */     if (map == null) {
/* 216 */       return;
/*     */     }
/* 218 */     MapleMonster mobfrom = map.getMonsterByOid(slea.readInt());
/* 219 */     slea.skip(4);
/* 220 */     slea.readInt();
/*     */ 
/* 222 */     if ((mobfrom != null) && (mobfrom.getBuff(MonsterStatus.MONSTER_BOMB) != null));
/*     */   }
/*     */ 
/*     */   public static final void checkShammos(MapleCharacter chr, MapleMonster mobto, MapleMap map)
/*     */   {
/*     */     MapleMap mapp;
/* 232 */     if ((!mobto.isAlive()) && (mobto.getStats().isEscort())) {
/* 233 */       for (MapleCharacter chrz : map.getCharactersThreadsafe()) {
/* 234 */         if ((chrz.getParty() != null) && (chrz.getParty().getLeader().getId() == chrz.getId()))
/*     */         {
/* 236 */           if (!chrz.haveItem(2022698)) break;
/* 237 */           MapleInventoryManipulator.removeById(chrz.getClient(), MapleInventoryType.USE, 2022698, 1, false, true);
/* 238 */           mobto.heal((int)mobto.getMobMaxHp(), mobto.getMobMaxMp(), true);
/* 239 */           return;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 244 */       map.broadcastMessage(CWvsContext.serverNotice(6, "Your party has failed to protect the monster."));
/* 245 */       mapp = chr.getMap().getForcedReturnMap();
/* 246 */       for (MapleCharacter chrz : map.getCharactersThreadsafe())
/* 247 */         chrz.changeMap(mapp, mapp.getPortal(0));
/*     */     }
/* 249 */     else if ((mobto.getStats().isEscort()) && (mobto.getEventInstance() != null)) {
/* 250 */       mobto.getEventInstance().setProperty("HP", String.valueOf(mobto.getHp()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void MonsterBomb(int oid, MapleCharacter chr) {
/* 255 */     MapleMonster monster = chr.getMap().getMonsterByOid(oid);
/*     */ 
/* 257 */     if ((monster == null) || (!chr.isAlive()) || (chr.isHidden()) || (monster.getLinkCID() > 0)) {
/* 258 */       return;
/*     */     }
/* 260 */     byte selfd = monster.getStats().getSelfD();
/* 261 */     if (selfd != -1)
/* 262 */       chr.getMap().killMonster(monster, chr, false, false, selfd);
/*     */   }
/*     */ 
/*     */   public static final void AutoAggro(int monsteroid, MapleCharacter chr)
/*     */   {
/* 267 */     if ((chr == null) || (chr.getMap() == null) || (chr.isHidden())) {
/* 268 */       return;
/*     */     }
/* 270 */     MapleMonster monster = chr.getMap().getMonsterByOid(monsteroid);
/*     */ 
/* 272 */     if ((monster != null) && (chr.getTruePosition().distanceSq(monster.getTruePosition()) < 200000.0D) && (monster.getLinkCID() <= 0))
/* 273 */       if (monster.getController() != null) {
/* 274 */         if (chr.getMap().getCharacterById(monster.getController().getId()) == null)
/* 275 */           monster.switchController(chr, true);
/*     */         else
/* 277 */           monster.switchController(monster.getController(), true);
/*     */       }
/*     */       else
/* 280 */         monster.switchController(chr, true);
/*     */   }
/*     */ 
/*     */   public static final void HypnotizeDmg(LittleEndianAccessor slea, MapleCharacter chr)
/*     */   {
/* 286 */     MapleMonster mob_from = chr.getMap().getMonsterByOid(slea.readInt());
/* 287 */     slea.skip(4);
/* 288 */     int to = slea.readInt();
/* 289 */     slea.skip(1);
/* 290 */     int damage = slea.readInt();
/*     */ 
/* 294 */     MapleMonster mob_to = chr.getMap().getMonsterByOid(to);
/*     */ 
/* 296 */     if ((mob_from != null) && (mob_to != null) && (mob_to.getStats().isFriendly())) {
/* 297 */       if (damage > 30000) {
/* 298 */         return;
/*     */       }
/* 300 */       mob_to.damage(chr, damage, true);
/* 301 */       checkShammos(chr, mob_to, chr.getMap());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void DisplayNode(LittleEndianAccessor slea, MapleCharacter chr) {
/* 306 */     MapleMonster mob_from = chr.getMap().getMonsterByOid(slea.readInt());
/* 307 */     if (mob_from != null)
/* 308 */       chr.getClient().getSession().write(MobPacket.getNodeProperties(mob_from, chr.getMap()));
/*     */   }
/*     */ 
/*     */   public static final void MobNode(LittleEndianAccessor slea, MapleCharacter chr)
/*     */   {
/* 313 */     MapleMonster mob_from = chr.getMap().getMonsterByOid(slea.readInt());
/* 314 */     int newNode = slea.readInt();
/* 315 */     int nodeSize = chr.getMap().getNodes().size();
/* 316 */     if ((mob_from != null) && (nodeSize > 0)) {
/* 317 */       MapleNodes.MapleNodeInfo mni = chr.getMap().getNode(newNode);
/* 318 */       if (mni == null) {
/* 319 */         return;
/*     */       }
/* 321 */       if (mni.attr == 2) {
/* 322 */         switch (chr.getMapId() / 100) {
/*     */         case 9211200:
/*     */         case 9211201:
/*     */         case 9211202:
/*     */         case 9211203:
/*     */         case 9211204:
/* 328 */           chr.getMap().talkMonster("Please escort me carefully.", 5120035, mob_from.getObjectId());
/* 329 */           break;
/*     */         case 9320001:
/*     */         case 9320002:
/*     */         case 9320003:
/* 333 */           chr.getMap().talkMonster("Please escort me carefully.", 5120051, mob_from.getObjectId());
/*     */         }
/*     */       }
/*     */ 
/* 337 */       mob_from.setLastNode(newNode);
/* 338 */       if (chr.getMap().isLastNode(newNode))
/* 339 */         switch (chr.getMapId() / 100) {
/*     */         case 9211200:
/*     */         case 9211201:
/*     */         case 9211202:
/*     */         case 9211203:
/*     */         case 9211204:
/*     */         case 9320001:
/*     */         case 9320002:
/*     */         case 9320003:
/* 348 */           chr.getMap().broadcastMessage(CWvsContext.serverNotice(5, "Proceed to the next stage."));
/* 349 */           chr.getMap().removeMonster(mob_from);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void RenameFamiliar(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
/*     */   {
/* 358 */     MonsterFamiliar mf = (MonsterFamiliar)c.getPlayer().getFamiliars().get(Integer.valueOf(slea.readInt()));
/* 359 */     String newName = slea.readMapleAsciiString();
/* 360 */     if ((mf != null) && (mf.getName().equals(mf.getOriginalName())) && (MapleCharacterUtil.isEligibleCharName(newName, false))) {
/* 361 */       mf.setName(newName);
/* 362 */       c.getSession().write(CField.renameFamiliar(mf));
/*     */     } else {
/* 364 */       chr.dropMessage(1, "Name was not eligible.");
/*     */     }
/* 366 */     c.getSession().write(CWvsContext.enableActions());
/*     */   }
/*     */ 
/*     */   public static final void SpawnFamiliar(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
/* 370 */     c.getPlayer().updateTick(slea.readInt());
/* 371 */     int mId = slea.readInt();
/* 372 */     c.getSession().write(CWvsContext.enableActions());
/* 373 */     c.getPlayer().removeFamiliar();
/* 374 */     if ((c.getPlayer().getFamiliars().containsKey(Integer.valueOf(mId))) && (slea.readByte() > 0)) {
/* 375 */       MonsterFamiliar mf = (MonsterFamiliar)c.getPlayer().getFamiliars().get(Integer.valueOf(mId));
/* 376 */       if (mf.getFatigue() > 0)
/* 377 */         c.getPlayer().dropMessage(1, "Please wait " + mf.getFatigue() + " seconds to summon it.");
/*     */       else
/* 379 */         c.getPlayer().spawnFamiliar(mf, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void MoveFamiliar(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
/*     */   {
/* 385 */     slea.skip(13);
/* 386 */     List res = MovementParse.parseMovement(slea, 6);
/* 387 */     if ((chr != null) && (chr.getSummonedFamiliar() != null) && (res.size() > 0)) {
/* 388 */       Point pos = chr.getSummonedFamiliar().getPosition();
/* 389 */       MovementParse.updatePosition(res, chr.getSummonedFamiliar(), 0);
/* 390 */       chr.getSummonedFamiliar().updatePosition(res);
/* 391 */       if (!chr.isHidden())
/* 392 */         chr.getMap().broadcastMessage(chr, CField.moveFamiliar(chr.getId(), pos, res), chr.getTruePosition());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void AttackFamiliar(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
/*     */   {
/* 398 */     if (chr.getSummonedFamiliar() == null) {
/* 399 */       return;
/*     */     }
/* 401 */     slea.skip(6);
/* 402 */     int skillid = slea.readInt();

/* 403 */     SkillFactory.FamiliarEntry f = SkillFactory.getFamiliar(skillid);
/* 404 */     if (f == null) {
/* 405 */       return;
/*     */     }
/* 407 */     byte unk = slea.readByte();
/* 408 */     byte size = slea.readByte();
/* 409 */      final List<Triple<Integer, Integer, List<Integer>>> attackPair = new ArrayList<Triple<Integer, Integer, List<Integer>>>(size);
/* 410 */     for (int i = 0; i < size; i++) {
/* 411 */       int oid = slea.readInt();
/* 412 */       int type = slea.readInt();
/* 413 */       slea.skip(10);
/* 414 */       byte si = slea.readByte();
/* 415 */       List attack = new ArrayList(si);
/* 416 */       for (int x = 0; x < si; x++) {
/* 417 */         attack.add(Integer.valueOf(slea.readInt()));
/*     */       }
/* 419 */       attackPair.add(new Triple(Integer.valueOf(oid), Integer.valueOf(type), attack));
/*     */     }
/* 421 */     if ((attackPair.isEmpty()) || (!chr.getCheatTracker().checkFamiliarAttack(chr)) || (attackPair.size() > f.targetCount)) {
/* 422 */       return;
/*     */     }
/* 424 */     MapleMonsterStats oStats = chr.getSummonedFamiliar().getOriginalStats();
/* 425 */     chr.getMap().broadcastMessage(chr, CField.familiarAttack(chr.getId(), unk, attackPair), chr.getTruePosition());
/* 426 */     for (Triple attack : attackPair) {
/* 427 */       MapleMonster mons = chr.getMap().getMonsterByOid(((Integer)attack.left).intValue());
/* 428 */       if ((mons == null) || (!mons.isAlive()) || (mons.getStats().isFriendly()) || (mons.getLinkCID() > 0) || (((List)attack.right).size() > f.attackCount)) {
/*     */         continue;
/*     */       }
/* 431 */       if ((chr.getTruePosition().distanceSq(mons.getTruePosition()) > 640000.0D) || (chr.getSummonedFamiliar().getTruePosition().distanceSq(mons.getTruePosition()) > GameConstants.getAttackRange(f.lt, f.rb))) {
/* 432 */         chr.getCheatTracker().registerOffense(CheatingOffense.ATTACK_FARAWAY_MONSTER_SUMMON);
/*     */       }
/* 434 */       for (Iterator i$ = ((List)attack.right).iterator(); i$.hasNext(); ) { int damage = ((Integer)i$.next()).intValue();
/* 435 */         if (damage <= oStats.getPhysicalAttack() * 4) {
/* 436 */           mons.damage(chr, damage, true);
/*     */         }
/*     */       }
/* 439 */       if ((f.makeChanceResult()) && (mons.isAlive())) {
/* 440 */         for (MonsterStatus s : f.status) {
/* 441 */           mons.applyStatus(chr, new MonsterStatusEffect(s, Integer.valueOf(f.speed), MonsterStatusEffect.genericSkill(s), null, false), false, f.time * 1000, false, null);
/*     */         }
/* 443 */         if (f.knockback) {
/* 444 */           mons.switchController(chr, true);
/*     */         }
/*     */       }
/*     */     }
/* 448 */     chr.getSummonedFamiliar().addFatigue(chr, attackPair.size());
/*     */   }
/*     */ 
/*     */   public static final void TouchFamiliar(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
/*     */   {
/* 453 */     if (chr.getSummonedFamiliar() == null) {
/* 454 */       return;
/*     */     }
/* 456 */     slea.skip(6);
/* 457 */     byte unk = slea.readByte();
/*     */ 
/* 459 */     MapleMonster target = chr.getMap().getMonsterByOid(slea.readInt());
/* 460 */     if (target == null) {
/* 461 */       return;
/*     */     }
/* 463 */     int type = slea.readInt();
/* 464 */     slea.skip(4);
/* 465 */     int damage = slea.readInt();
/* 466 */     int maxDamage = chr.getSummonedFamiliar().getOriginalStats().getPhysicalAttack() * 5;
/* 467 */     if (damage < maxDamage) {
/* 468 */       damage = maxDamage;
/*     */     }
/* 470 */     if ((!target.getStats().isFriendly()) && (chr.getCheatTracker().checkFamiliarAttack(chr))) {
/* 471 */       chr.getMap().broadcastMessage(chr, CField.touchFamiliar(chr.getId(), unk, target.getObjectId(), type, 600, damage), chr.getTruePosition());
/* 472 */       target.damage(chr, damage, true);
/* 473 */       chr.getSummonedFamiliar().addFatigue(chr);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void UseFamiliar(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
/* 478 */     if ((chr == null) || (!chr.isAlive()) || (chr.getMap() == null) || (chr.hasBlockedInventory())) {
/* 479 */       c.getSession().write(CWvsContext.enableActions());
/* 480 */       return;
/*     */     }
/* 482 */     c.getPlayer().updateTick(slea.readInt());
/* 483 */     short slot = slea.readShort();
/* 484 */     int itemId = slea.readInt();
/* 485 */     Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
/*     */ 
/* 487 */     c.getSession().write(CWvsContext.enableActions());
/* 488 */     if ((toUse == null) || (toUse.getQuantity() < 1) || (toUse.getItemId() != itemId) || (itemId / 10000 != 287)) {
/* 489 */       return;
/*     */     }
/* 491 */     StructFamiliar f = MapleItemInformationProvider.getInstance().getFamiliarByItem(itemId);
/* 492 */     if (MapleLifeFactory.getMonsterStats(f.mob).getLevel() <= c.getPlayer().getLevel()) {
/* 493 */       MonsterFamiliar mf = (MonsterFamiliar)c.getPlayer().getFamiliars().get(Integer.valueOf(f.familiar));
/* 494 */       if (mf != null) {
/* 495 */         if (mf.getVitality() >= 3) {
/* 496 */           mf.setExpiry(Math.min(System.currentTimeMillis() + 7776000000L, mf.getExpiry() + 2592000000L));
/*     */         } else {
/* 498 */           mf.setVitality(mf.getVitality() + 1);
/* 499 */           mf.setExpiry(mf.getExpiry() + 2592000000L);
/*     */         }
/*     */       } else {
/* 502 */         mf = new MonsterFamiliar(c.getPlayer().getId(), f.familiar, System.currentTimeMillis() + 2592000000L);
/* 503 */         c.getPlayer().getFamiliars().put(Integer.valueOf(f.familiar), mf);
/*     */       }
/* 505 */       MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (byte)1, false, false);
/* 506 */       c.getSession().write(CField.registerFamiliar(mf));
/* 507 */       return;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Sjögren\Desktop\lithium.jar
 * Qualified Name:     handling.channel.handler.MobHandler
 * JD-Core Version:    0.6.0
 */
       
       