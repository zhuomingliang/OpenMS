 package handling.channel.handler;
 
 import client.MapleBuffStat;
 import client.MapleCharacter;

 import client.MapleClient;
 import client.MapleJob;
 import client.MapleQuestStatus;
 import client.PlayerStats;
 import client.Skill;
 import client.SkillFactory;
 import client.SkillMacro;
 import client.anticheat.CheatTracker;
 import client.anticheat.CheatingOffense;
 import client.inventory.Item;
 import client.inventory.MapleAndroid;
 import client.inventory.MapleInventory;
 import client.inventory.MapleInventoryType;
 import client.status.MonsterStatus;
 import client.status.MonsterStatusEffect;
 import constants.GameConstants;
           import constants.ServerConstants;
 import constants.ServerConstants.PlayerGMRank;
 import handling.channel.ChannelServer;
           import java.lang.ref.WeakReference;
 import java.awt.Point;
 import java.io.PrintStream;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;
 import java.util.Map.Entry;
 import org.apache.mina.common.IoSession;
 import scripting.EventInstanceManager;
           import server.Timer.CloneTimer;
 import server.MapleInventoryManipulator;
 import server.MapleItemInformationProvider;
 import server.MaplePortal;
 import server.MapleStatEffect;
 import server.Randomizer;
 import server.events.MapleEvent;
 import server.events.MapleEventType;
import server.events.MapleSnowball;
 import server.events.MapleSnowball.MapleSnowballs;
 import server.life.MapleMonster;
 import server.life.MapleMonsterStats;
 import server.life.MobAttackInfo;
 import server.life.MobSkill;
 import server.life.MobSkillFactory;
 import server.maps.Event_PyramidSubway;
 import server.maps.FieldLimitType;
 import server.maps.MapleFootholdTree;
 import server.maps.MapleMap;
 import server.maps.MapleMapFactory;
import server.movement.LifeMovementFragment;
 import server.quest.MapleQuest;
 import tools.FileoutputUtil;
 import tools.Pair;
 import tools.data.LittleEndianAccessor;
 import tools.packet.CField;
 import tools.packet.CField.EffectPacket;
 import tools.packet.CField.UIPacket;
 import tools.packet.CWvsContext;
 import tools.packet.CWvsContext.InventoryPacket;
 import tools.packet.MTSCSPacket;
 import tools.packet.MobPacket;
 
 public class PlayerHandler
 {
   public static int isFinisher(int skillid)
   {
/*   68 */     switch (skillid) {
     case 1111003:
/*   70 */       return GameConstants.GMS ? 1 : 10;
     case 1111005:
/*   72 */       return GameConstants.GMS ? 2 : 10;
     case 11111002:
/*   74 */       return GameConstants.GMS ? 1 : 10;
     case 11111003:
/*   76 */       return GameConstants.GMS ? 2 : 10;
     }
/*   78 */     return 0;
   }
 
   public static void ChangeSkillMacro(LittleEndianAccessor slea, MapleCharacter chr) {
/*   82 */     int num = slea.readByte();
 
/*   87 */     for (int i = 0; i < num; i++) {
/*   88 */       String name = slea.readMapleAsciiString();
/*   89 */       int shout = slea.readByte();
/*   90 */       int skill1 = slea.readInt();
/*   91 */       int skill2 = slea.readInt();
/*   92 */       int skill3 = slea.readInt();
 
/*   94 */       SkillMacro macro = new SkillMacro(skill1, skill2, skill3, name, shout, i);
/*   95 */       chr.updateMacros(i, macro);
     }
   }
 
   public static final void ChangeKeymap(LittleEndianAccessor slea, MapleCharacter chr) {
/*  100 */     if ((slea.available() > 8L) && (chr != null)) {
/*  101 */       slea.skip(4);
/*  102 */       int numChanges = slea.readInt();
 
/*  104 */       for (int i = 0; i < numChanges; i++) {
/*  105 */         int key = slea.readInt();
/*  106 */         byte type = slea.readByte();
/*  107 */         int action = slea.readInt();
/*  108 */         if ((type == 1) && (action >= 1000)) {
/*  109 */           Skill skil = SkillFactory.getSkill(action);
/*  110 */           if ((skil != null) && (
/*  111 */             ((!skil.isFourthJob()) && (!skil.isBeginnerSkill()) && (skil.isInvisible()) && (chr.getSkillLevel(skil) <= 0)) || (GameConstants.isLinkedAranSkill(action)) || (action % 10000 < 1000) || (action >= 91000000)))
           {
             continue;
           }
         }
/*  116 */         chr.changeKeybinding(key, type, action);
       }
/*  118 */     } else if (chr != null) {
/*  119 */       int type = slea.readInt(); int data = slea.readInt();
/*  120 */       switch (type) {
       case 1:
/*  122 */         if (data <= 0)
/*  123 */           chr.getQuestRemove(MapleQuest.getInstance(122221));
         else {
/*  125 */           chr.getQuestNAdd(MapleQuest.getInstance(122221)).setCustomData(String.valueOf(data));
         }
/*  127 */         break;
       case 2:
/*  129 */         if (data <= 0)
/*  130 */           chr.getQuestRemove(MapleQuest.getInstance(122223));
         else
/*  132 */           chr.getQuestNAdd(MapleQuest.getInstance(122223)).setCustomData(String.valueOf(data));
       }
     }
   }
 
   public static final void UseTitle(int itemId, MapleClient c, MapleCharacter chr)
   {
/*  140 */     if ((chr == null) || (chr.getMap() == null)) {
/*  141 */       return;
     }
/*  143 */     Item toUse = chr.getInventory(MapleInventoryType.SETUP).findById(itemId);
/*  144 */     if (toUse == null) {
/*  145 */       return;
     }
/*  147 */     if (itemId <= 0)
/*  148 */       chr.getQuestRemove(MapleQuest.getInstance(124000));
     else {
/*  150 */       chr.getQuestNAdd(MapleQuest.getInstance(124000)).setCustomData(String.valueOf(itemId));
     }
/*  152 */     chr.getMap().broadcastMessage(chr, CField.showTitle(chr.getId(), itemId), false);
/*  153 */     c.getSession().write(CWvsContext.enableActions());
   }
 
    public static final void UseChair(final int itemId, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || chr.getMap() == null) {
            return;
        }
        final Item toUse = chr.getInventory(MapleInventoryType.SETUP).findById(itemId);
        if (toUse == null) {
            chr.getCheatTracker().registerOffense(CheatingOffense.USING_UNAVAILABLE_ITEM, Integer.toString(itemId));
            return;
        }
        if (GameConstants.isFishingMap(chr.getMapId()) && itemId == 3011000) {
            chr.startFishingTask();
        }
        chr.setChair(itemId);
        chr.getMap().broadcastMessage(chr, CField.showChair(chr.getId(), itemId), false);
        c.getSession().write(CWvsContext.enableActions());
    }
 
   public static final void CancelChair(short id, MapleClient c, MapleCharacter chr) {
/*  176 */     if (id == -1) {
/*  177 */       chr.cancelFishingTask();
/*  178 */       chr.setChair(0);
/*  179 */       c.getSession().write(CField.cancelChair(-1));
/*  180 */       if (chr.getMap() != null)
/*  181 */         chr.getMap().broadcastMessage(chr, CField.showChair(chr.getId(), 0), false);
     }
     else {
/*  184 */       chr.setChair(id);
/*  185 */       c.getSession().write(CField.cancelChair(id));
     }
   }
 
   public static final void TrockAddMap(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
/*  190 */     byte addrem = slea.readByte();
/*  191 */     byte vip = slea.readByte();
 
/*  193 */     if (vip == 1) {
/*  194 */       if (addrem == 0)
/*  195 */         chr.deleteFromRegRocks(slea.readInt());
/*  196 */       else if (addrem == 1) {
/*  197 */         if (!FieldLimitType.VipRock.check(chr.getMap().getFieldLimit()))
/*  198 */           chr.addRegRockMap();
         else
/*  200 */           chr.dropMessage(1, "This map is not available to enter for the list.");
       }
     }
/*  203 */     else if (vip == 2) {
/*  204 */       if (addrem == 0)
/*  205 */         chr.deleteFromRocks(slea.readInt());
/*  206 */       else if (addrem == 1) {
/*  207 */         if (!FieldLimitType.VipRock.check(chr.getMap().getFieldLimit()))
/*  208 */           chr.addRockMap();
         else
/*  210 */           chr.dropMessage(1, "This map is not available to enter for the list.");
       }
     }
/*  213 */     else if (vip == 3) {
/*  214 */       if (addrem == 0)
/*  215 */         chr.deleteFromHyperRocks(slea.readInt());
/*  216 */       else if (addrem == 1) {
/*  217 */         if (!FieldLimitType.VipRock.check(chr.getMap().getFieldLimit()))
/*  218 */           chr.addHyperRockMap();
         else {
/*  220 */           chr.dropMessage(1, "This map is not available to enter for the list.");
         }
       }
     }
/*  224 */       c.getSession().write(MTSCSPacket.OnMapTransferResult(chr, vip, addrem == 0));
   }
 
   public static final void CharInfoRequest(int objectid, MapleClient c, MapleCharacter chr) {
/*  228 */     if ((c.getPlayer() == null) || (c.getPlayer().getMap() == null)) {
/*  229 */       return;
     }
/*  231 */     MapleCharacter player = c.getPlayer().getMap().getCharacterById(objectid);
/*  232 */     c.getSession().write(CWvsContext.enableActions());
/*  233 */     if ((player != null) && (
/*  234 */       (!player.isGM()) || (c.getPlayer().isGM())))
/*  235 */       c.getSession().write(CWvsContext.charInfo(player, c.getPlayer().getId() == objectid));
   }
 
   public static final void TakeDamage(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
   {
/*  242 */     slea.skip(4);
/*  243 */     chr.updateTick(slea.readInt());
/*  244 */     byte type = slea.readByte();
/*  245 */     slea.skip(1);
/*  246 */     int damage = slea.readInt();
/*  247 */     slea.skip(2);
/*  248 */     boolean isDeadlyAttack = false;
/*  249 */     boolean pPhysical = false;
/*  250 */     int oid = 0;
/*  251 */     int monsteridfrom = 0;
/*  252 */     int fake = 0;
/*  253 */     int mpattack = 0;
/*  254 */     int skillid = 0;
/*  255 */     int pID = 0;
/*  256 */     int pDMG = 0;
/*  257 */     byte direction = 0;
/*  258 */     byte pType = 0;
/*  259 */     Point pPos = new Point(0, 0);
/*  260 */     MapleMonster attacker = null;
/*  261 */     if ((chr == null) || (chr.isHidden()) || (chr.getMap() == null)) {
/*  262 */       c.getSession().write(CWvsContext.enableActions());
/*  263 */       return;
     }
/*  265 */     if ((chr.isGM()) && (chr.isInvincible())) {
/*  266 */       c.getSession().write(CWvsContext.enableActions());
/*  267 */       return;
     }
/*  269 */     PlayerStats stats = chr.getStat();
/*  270 */     if ((type != -2) && (type != -3) && (type != -4)) {
/*  271 */       monsteridfrom = slea.readInt();
/*  272 */       oid = slea.readInt();
/*  273 */       attacker = chr.getMap().getMonsterByOid(oid);
/*  274 */       direction = slea.readByte();
 
/*  276 */       if ((attacker == null) || (attacker.getId() != monsteridfrom) || (attacker.getLinkCID() > 0) || (attacker.isFake()) || (attacker.getStats().isFriendly())) {
/*  277 */         return;
       }
/*  279 */       if (chr.getMapId() == 915000300) {
/*  280 */         MapleMap to = chr.getClient().getChannelServer().getMapFactory().getMap(915000200);
/*  281 */         chr.dropMessage(5, "You've been found out! Retreat!");
/*  282 */         chr.changeMap(to, to.getPortal(1));
/*  283 */         return;
       }
/*  285 */       if ((type != -1) && (damage > 0)) {
/*  286 */         MobAttackInfo attackInfo = attacker.getStats().getMobAttack(type);
/*  287 */         if (attackInfo != null) {
/*  288 */           if ((attackInfo.isElement) && (stats.TER > 0) && (Randomizer.nextInt(100) < stats.TER)) {
/*  289 */             System.out.println(new StringBuilder().append("Avoided ER from mob id: ").append(monsteridfrom).toString());
/*  290 */             return;
           }
/*  292 */           if (attackInfo.isDeadlyAttack()) {
/*  293 */             isDeadlyAttack = true;
/*  294 */             mpattack = stats.getMp() - 1;
           } else {
/*  296 */             mpattack += attackInfo.getMpBurn();
           }
/*  298 */           MobSkill skill = MobSkillFactory.getMobSkill(attackInfo.getDiseaseSkill(), attackInfo.getDiseaseLevel());
/*  299 */           if ((skill != null) && ((damage == -1) || (damage > 0))) {
/*  300 */             skill.applyEffect(chr, attacker, false);
           }
/*  302 */           attacker.setMp(attacker.getMp() - attackInfo.getMpCon());
         }
       }
/*  305 */       skillid = slea.readInt();
/*  306 */       pDMG = slea.readInt();
/*  307 */       byte defType = slea.readByte();
/*  308 */       slea.skip(1);
/*  309 */       if (defType == 1) {
/*  310 */         Skill bx = SkillFactory.getSkill(31110008);
/*  311 */         int bof = chr.getTotalSkillLevel(bx);
/*  312 */         if (bof > 0) {
/*  313 */           MapleStatEffect eff = bx.getEffect(bof);
/*  314 */           if (Randomizer.nextInt(100) <= eff.getX()) {
/*  315 */             chr.handleForceGain(oid, 31110008, eff.getZ());
           }
         }
       }
/*  319 */       if (skillid != 0) {
/*  320 */         pPhysical = slea.readByte() > 0;
/*  321 */         pID = slea.readInt();
/*  322 */         pType = slea.readByte();
/*  323 */         slea.skip(4);
/*  324 */         pPos = slea.readPos();
       }
     }
/*  327 */     if (damage == -1) {
/*  328 */       fake = 4020002 + (chr.getJob() / 10 - 40) * 100000;
/*  329 */       if ((fake != 4120002) && (fake != 4220002)) {
/*  330 */         fake = 4120002;
       }
/*  332 */       if ((type == -1) && (chr.getJob() == 122) && (attacker != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-10) != null) && 
/*  333 */         (chr.getTotalSkillLevel(1220006) > 0)) {
/*  334 */         MapleStatEffect eff = SkillFactory.getSkill(1220006).getEffect(chr.getTotalSkillLevel(1220006));
/*  335 */         attacker.applyStatus(chr, new MonsterStatusEffect(MonsterStatus.STUN, Integer.valueOf(1), 1220006, null, false), false, eff.getDuration(), true, eff);
/*  336 */         fake = 1220006;
       }
 
/*  339 */       if (chr.getTotalSkillLevel(fake) <= 0)
/*  340 */         return;
     }
/*  342 */     else if ((damage < -1) || (damage > 200000))
     {
/*  344 */       c.getSession().write(CWvsContext.enableActions());
/*  345 */       return;
     }
/*  347 */     if ((chr.getStat().dodgeChance > 0) && (Randomizer.nextInt(100) < chr.getStat().dodgeChance)) {
/*  348 */       c.getSession().write(CField.EffectPacket.showForeignEffect(20));
/*  349 */       return;
     }
/*  351 */     if ((pPhysical) && (skillid == 1201007) && (chr.getTotalSkillLevel(1201007) > 0)) {
/*  352 */       damage -= pDMG;
/*  353 */       if (damage > 0) {
/*  354 */         MapleStatEffect eff = SkillFactory.getSkill(1201007).getEffect(chr.getTotalSkillLevel(1201007));
/*  355 */         long enemyDMG = Math.min(damage * (eff.getY() / 100), attacker.getMobMaxHp() / 2L);
/*  356 */         if (enemyDMG > pDMG) {
/*  357 */           enemyDMG = pDMG;
         }
/*  359 */         if (enemyDMG > 1000L) {
/*  360 */           enemyDMG = 1000L;
         }
/*  362 */         attacker.damage(chr, enemyDMG, true, 1201007);
       } else {
/*  364 */         damage = 1;
       }
     }
/*  367 */     chr.getCheatTracker().checkTakeDamage(damage);
/*  368 */     Pair modify = chr.modifyDamageTaken(damage, attacker);
/*  369 */     damage = ((Double)modify.left).intValue();
/*  370 */     if (damage > 0) {
/*  371 */       chr.getCheatTracker().setAttacksWithoutHit(false);
 
/*  373 */       if (chr.getBuffedValue(MapleBuffStat.MORPH) != null) {
/*  374 */         chr.cancelMorphs();
       }
 
/*  385 */       boolean mpAttack = (chr.getBuffedValue(MapleBuffStat.MECH_CHANGE) != null) && (chr.getBuffSource(MapleBuffStat.MECH_CHANGE) != 35121005);
/*  386 */       if (chr.getBuffedValue(MapleBuffStat.MAGIC_GUARD) != null) {
/*  387 */         int hploss = 0; int mploss = 0;
/*  388 */         if (isDeadlyAttack) {
/*  389 */           if (stats.getHp() > 1) {
/*  390 */             hploss = stats.getHp() - 1;
           }
/*  392 */           if (stats.getMp() > 1) {
/*  393 */             mploss = stats.getMp() - 1;
           }
/*  395 */           if (chr.getBuffedValue(MapleBuffStat.INFINITY) != null) {
/*  396 */             mploss = 0;
           }
/*  398 */           chr.addMPHP(-hploss, -mploss);
         }
         else
         {
/*  402 */           mploss = (int)(damage * (chr.getBuffedValue(MapleBuffStat.MAGIC_GUARD).doubleValue() / 100.0D)) + mpattack;
/*  403 */           hploss = damage - mploss;
/*  404 */           if (chr.getBuffedValue(MapleBuffStat.INFINITY) != null) {
/*  405 */             mploss = 0;
/*  406 */           } else if (mploss > stats.getMp()) {
/*  407 */             mploss = stats.getMp();
/*  408 */             hploss = damage - mploss + mpattack;
           }
/*  410 */           chr.addMPHP(-hploss, -mploss);
         }
       }
/*  413 */       else if (chr.getStat().mesoGuardMeso > 0.0D)
       {
/*  416 */         int mesoloss = (int)(damage * (chr.getStat().mesoGuardMeso / 100.0D));
/*  417 */         if (chr.getMeso() < mesoloss) {
/*  418 */           chr.gainMeso(-chr.getMeso(), false);
/*  419 */           chr.cancelBuffStats(new MapleBuffStat[] { MapleBuffStat.MESOGUARD });
         } else {
/*  421 */           chr.gainMeso(-mesoloss, false);
         }
/*  423 */         if ((isDeadlyAttack) && (stats.getMp() > 1)) {
/*  424 */           mpattack = stats.getMp() - 1;
         }
/*  426 */         chr.addMPHP(-damage, -mpattack);
       }
/*  428 */       else if (isDeadlyAttack) {
/*  429 */         chr.addMPHP(stats.getHp() > 1 ? -(stats.getHp() - 1) : 0, (stats.getMp() > 1) && (!mpAttack) ? -(stats.getMp() - 1) : 0);
       } else {
/*  431 */         chr.addMPHP(-damage, mpAttack ? 0 : -mpattack);
       }
 
/*  434 */       if (!GameConstants.GMS) {
/*  435 */         chr.handleBattleshipHP(-damage);
       }
/*  437 */       if ((chr.inPVP()) && (chr.getStat().getHPPercent() <= 20)) {
/*  438 */         chr.getStat(); SkillFactory.getSkill(PlayerStats.getSkillByJob(93, chr.getJob())).getEffect(1).applyTo(chr);
       }
     }
/*  441 */     byte offset = 0;
/*  442 */     int offset_d = 0;
/*  443 */     if (slea.available() == 1L) {
/*  444 */       offset = slea.readByte();
/*  445 */       if ((offset == 1) && (slea.available() >= 4L)) {
/*  446 */         offset_d = slea.readInt();
       }
/*  448 */       if ((offset < 0) || (offset > 2)) {
/*  449 */         offset = 0;
       }
     }
 
/*  453 */     chr.getMap().broadcastMessage(chr, CField.damagePlayer(chr.getId(), type, damage, monsteridfrom, direction, skillid, pDMG, pPhysical, pID, pType, pPos, offset, offset_d, fake), false);
   }
 
   public static final void AranCombo(MapleClient c, MapleCharacter chr, int toAdd) {
/*  457 */     if ((chr != null) && (chr.getJob() >= 2000) && (chr.getJob() <= 2112)) {
/*  458 */       short combo = chr.getCombo();
/*  459 */       long curr = System.currentTimeMillis();
 
/*  461 */       if ((combo > 0) && (curr - chr.getLastCombo() > 7000L))
       {
/*  464 */         combo = 0;
       }
/*  466 */       combo = (short)Math.min(30000, combo + toAdd);
/*  467 */       chr.setLastCombo(curr);
/*  468 */       chr.setCombo(combo);
 
/*  470 */       c.getSession().write(CField.testCombo(combo));
 
/*  472 */       switch (combo) {
       case 10:
       case 20:
       case 30:
       case 40:
       case 50:
       case 60:
       case 70:
       case 80:
       case 90:
       case 100:
/*  483 */         if (chr.getSkillLevel(21000000) < combo / 10) break;
/*  484 */         SkillFactory.getSkill(21000000).getEffect(combo / 10).applyComboBuff(chr, combo);
       }
     }
   }
 
   public static final void UseItemEffect(int itemId, MapleClient c, MapleCharacter chr)
   {
/*  492 */     Item toUse = chr.getInventory((itemId == 4290001) || (itemId == 4290000) ? MapleInventoryType.ETC : MapleInventoryType.CASH).findById(itemId);
/*  493 */     if ((toUse == null) || (toUse.getItemId() != itemId) || (toUse.getQuantity() < 1)) {
/*  494 */       c.getSession().write(CWvsContext.enableActions());
/*  495 */       return;
     }
/*  497 */     if (itemId != 5510000) {
/*  498 */       chr.setItemEffect(itemId);
     }
/*  500 */     chr.getMap().broadcastMessage(chr, CField.itemEffect(chr.getId(), itemId), false);
   }
 
   public static final void CancelItemEffect(int id, MapleCharacter chr) {
/*  504 */     chr.cancelEffect(MapleItemInformationProvider.getInstance().getItemEffect(-id), false, -1L);
   }
 
   public static final void CancelBuffHandler(int sourceid, MapleCharacter chr)
   {
/*  509 */     if ((chr == null) || (chr.getMap() == null)) {
/*  510 */       return;
     }
/*  512 */     Skill skill = SkillFactory.getSkill(sourceid);
 
/*  514 */     if (skill.isChargeSkill()) {
/*  515 */       chr.setKeyDownSkill_Time(0L);
/*  516 */       chr.getMap().broadcastMessage(chr, CField.skillCancel(chr, sourceid), false);
     } else {
/*  518 */       chr.cancelEffect(skill.getEffect(1), false, -1L);
     }
   }
 
   public static final void CancelMech(LittleEndianAccessor slea, MapleCharacter chr) {
/*  523 */     if (chr == null) {
/*  524 */       return;
     }
/*  526 */     int sourceid = slea.readInt();
/*  527 */     if ((sourceid % 10000 < 1000) && (SkillFactory.getSkill(sourceid) == null)) {
/*  528 */       sourceid += 1000;
     }
/*  530 */     Skill skill = SkillFactory.getSkill(sourceid);
/*  531 */     if (skill == null) {
/*  532 */       return;
     }
/*  534 */     if (skill.isChargeSkill()) {
/*  535 */       chr.setKeyDownSkill_Time(0L);
/*  536 */       chr.getMap().broadcastMessage(chr, CField.skillCancel(chr, sourceid), false);
     } else {
/*  538 */       chr.cancelEffect(skill.getEffect(slea.readByte()), false, -1L);
     }
   }
 
   public static final void QuickSlot(LittleEndianAccessor slea, MapleCharacter chr) {
/*  543 */     if ((slea.available() == 32L) && (chr != null)) {
/*  544 */       StringBuilder ret = new StringBuilder();
/*  545 */       for (int i = 0; i < 8; i++) {
/*  546 */         ret.append(slea.readInt()).append(",");
       }
/*  548 */       ret.deleteCharAt(ret.length() - 1);
/*  549 */       chr.getQuestNAdd(MapleQuest.getInstance(123000)).setCustomData(ret.toString());
     }
   }
 
   public static final void SkillEffect(LittleEndianAccessor slea, MapleCharacter chr) {
/*  554 */     int skillId = slea.readInt();
/*  555 */     if (skillId >= 91000000) {
/*  556 */       chr.getClient().getSession().write(CWvsContext.enableActions());
/*  557 */       return;
     }
/*  559 */     byte level = slea.readByte();
/*  560 */     short direction = slea.readShort();
/*  561 */     byte unk = slea.readByte();
 
/*  563 */     Skill skill = SkillFactory.getSkill(GameConstants.getLinkedAranSkill(skillId));
/*  564 */     if ((chr == null) || (skill == null) || (chr.getMap() == null)) {
/*  565 */       return;
     }
/*  567 */     int skilllevel_serv = chr.getTotalSkillLevel(skill);
 
/*  569 */     if ((skilllevel_serv > 0) && (skilllevel_serv == level) && ((skillId == 33101005) || (skill.isChargeSkill()))) {
/*  570 */       chr.setKeyDownSkill_Time(System.currentTimeMillis());
/*  571 */       if (skillId == 33101005) {
/*  572 */         chr.setLinkMid(slea.readInt(), 0);
       }
/*  574 */       chr.getMap().broadcastMessage(chr, CField.skillEffect(chr, skillId, level, direction, unk), false);
     }
   }
 
   public static final void SpecialMove(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
/*  579 */     if ((chr == null) || (chr.hasBlockedInventory()) || (chr.getMap() == null) || (slea.available() < 9L)) {
/*  580 */       c.getSession().write(CWvsContext.enableActions());
/*  581 */       return;
     }
/*  583 */     slea.skip(4);
/*  584 */     int skillid = slea.readInt();
/*  585 */     if (skillid >= 91000000) {
/*  586 */       c.getSession().write(CWvsContext.enableActions());
/*  587 */       return;
     }
/*  589 */     if (skillid == 23111008) {
/*  590 */       skillid += Randomizer.nextInt(2);
     }
/*  592 */     int skillLevel = slea.readByte();
/*  593 */     Skill skill = SkillFactory.getSkill(skillid);
/*  594 */     if ((skill == null) || ((GameConstants.isAngel(skillid)) && (chr.getStat().equippedSummon % 10000 != skillid % 10000)) || ((chr.inPVP()) && (skill.isPVPDisabled()))) {
/*  595 */       c.getSession().write(CWvsContext.enableActions());
/*  596 */       return;
     }
/*  598 */     int levelCheckSkill = 0;
/*  599 */     if ((GameConstants.isPhantom(chr.getJob())) && (!MapleJob.getById(skillid / 10000).isPhantom())) {
/*  600 */       int skillJob = skillid / 10000;
/*  601 */       if (skillJob % 100 == 0)
/*  602 */         levelCheckSkill = 24001001;
/*  603 */       else if (skillJob % 10 == 0)
/*  604 */         levelCheckSkill = 24101001;
/*  605 */       else if (skillJob % 10 == 1)
/*  606 */         levelCheckSkill = 24111001;
       else {
/*  608 */         levelCheckSkill = 24121001;
       }
     }
/*  611 */     if ((levelCheckSkill == 0) && ((chr.getTotalSkillLevel(GameConstants.getLinkedAranSkill(skillid)) <= 0) || (chr.getTotalSkillLevel(GameConstants.getLinkedAranSkill(skillid)) != skillLevel))) {
/*  612 */       if ((!GameConstants.isMulungSkill(skillid)) && (!GameConstants.isPyramidSkill(skillid)) && (chr.getTotalSkillLevel(GameConstants.getLinkedAranSkill(skillid)) <= 0)) {
/*  613 */         c.getSession().close();
/*  614 */         return;
       }
/*  616 */       if (GameConstants.isMulungSkill(skillid)) {
/*  617 */         if (chr.getMapId() / 10000 != 92502) {
/*  618 */           return;
         }
/*  620 */         if (chr.getMulungEnergy() < 10000) {
/*  621 */           return;
         }
/*  623 */         chr.mulung_EnergyModify(false);
       }
/*  625 */       else if ((GameConstants.isPyramidSkill(skillid)) && 
/*  626 */         (chr.getMapId() / 10000 != 92602) && (chr.getMapId() / 10000 != 92601)) {
/*  627 */         return;
       }
     }
 
/*  631 */     if (GameConstants.isEventMap(chr.getMapId())) {
/*  632 */       for (MapleEventType t : MapleEventType.values()) {
/*  633 */         MapleEvent e = ChannelServer.getInstance(chr.getClient().getChannel()).getEvent(t);
/*  634 */         if ((e.isRunning()) && (!chr.isGM())) {
/*  635 */           for (int i : e.getType().mapids) {
/*  636 */             if (chr.getMapId() == i) {
/*  637 */               chr.dropMessage(5, "You may not use that here.");
/*  638 */               return;
             }
           }
         }
       }
     }
/*  644 */     skillLevel = chr.getTotalSkillLevel(GameConstants.getLinkedAranSkill(skillid));
/*  645 */     MapleStatEffect effect = chr.inPVP() ? skill.getPVPEffect(skillLevel) : skill.getEffect(skillLevel);
/*  646 */     if ((effect.isMPRecovery()) && (chr.getStat().getHp() < chr.getStat().getMaxHp() / 100 * 10)) {
/*  647 */       c.getPlayer().dropMessage(5, "You do not have the HP to use this skill.");
/*  648 */       c.getSession().write(CWvsContext.enableActions());
/*  649 */       return;
     }
/*  651 */     if ((effect.getCooldown(chr) > 0) && (!chr.isGM())) {
/*  652 */       if (chr.skillisCooling(skillid)) {
/*  653 */         c.getSession().write(CWvsContext.enableActions());
/*  654 */         return;
       }
/*  656 */       if ((skillid != 5221006) && (skillid != 35111002)) {
/*  657 */         c.getSession().write(CField.skillCooldown(skillid, effect.getCooldown(chr)));
/*  658 */         chr.addCooldown(skillid, System.currentTimeMillis(), effect.getCooldown(chr) * 1000);
       }
     }
     int mobID;
     MapleMonster mob;
/*  662 */     switch (skillid) {
     case 1121001:
     case 1221001:
     case 1321001:
     case 9001020:
     case 9101020:
     case 31111003:
/*  669 */       byte number_of_mobs = slea.readByte();
/*  670 */       slea.skip(3);
/*  671 */       for (int i = 0; i < number_of_mobs; i++) {
/*  672 */         int mobId = slea.readInt();
 
/*  674 */         mob = chr.getMap().getMonsterByOid(mobId);
/*  675 */         if (mob == null)
           continue;
/*  677 */         mob.switchController(chr, mob.isControllerHasAggro());
/*  678 */         mob.applyStatus(chr, new MonsterStatusEffect(MonsterStatus.STUN, Integer.valueOf(1), skillid, null, false), false, effect.getDuration(), true, effect);
       }
 
/*  681 */       chr.getMap().broadcastMessage(chr, CField.EffectPacket.showBuffeffect(chr.getId(), skillid, 1, chr.getLevel(), skillLevel, slea.readByte()), chr.getTruePosition());
/*  682 */       c.getSession().write(CWvsContext.enableActions());
/*  683 */       break;
     case 30001061:
/*  685 */       mobID = slea.readInt();
/*  686 */       mob = chr.getMap().getMonsterByOid(mobID);
/*  687 */       if (mob != null) {
/*  688 */         boolean success = (mob.getHp() <= mob.getMobMaxHp() / 2L) && (mob.getId() >= 9304000) && (mob.getId() < 9305000);
/*  689 */         chr.getMap().broadcastMessage(chr, CField.EffectPacket.showBuffeffect(chr.getId(), skillid, 1, chr.getLevel(), skillLevel, (byte)(success ? 1 : 0)), chr.getTruePosition());
/*  690 */         if (success) {
/*  691 */           chr.getQuestNAdd(MapleQuest.getInstance(111112)).setCustomData(String.valueOf((mob.getId() - 9303999) * 10));
/*  692 */           chr.getMap().killMonster(mob, chr, true, false, (byte)1);
/*  693 */           chr.cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
/*  694 */           c.getSession().write(CWvsContext.updateJaguar(chr));
         } else {
/*  696 */           chr.dropMessage(5, "The monster has too much physical strength, so you cannot catch it.");
         }
       }
/*  699 */       c.getSession().write(CWvsContext.enableActions());
/*  700 */       break;
     case 30001062:
/*  702 */       chr.dropMessage(5, "No monsters can be summoned. Capture a monster first.");
/*  703 */       c.getSession().write(CWvsContext.enableActions());
/*  704 */       break;
     case 33101005:
/*  706 */       mobID = chr.getFirstLinkMid();
/*  707 */       mob = chr.getMap().getMonsterByOid(mobID);
/*  708 */       chr.setKeyDownSkill_Time(0L);
/*  709 */       chr.getMap().broadcastMessage(chr, CField.skillCancel(chr, skillid), false);
/*  710 */       if (mob != null) {
/*  711 */         boolean success = (mob.getStats().getLevel() < chr.getLevel()) && (mob.getId() < 9000000) && (!mob.getStats().isBoss());
/*  712 */         if (success) {
/*  713 */           chr.getMap().broadcastMessage(MobPacket.suckMonster(mob.getObjectId(), chr.getId()));
/*  714 */           chr.getMap().killMonster(mob, chr, false, false, (byte)-1);
         } else {
/*  716 */           chr.dropMessage(5, "The monster has too much physical strength, so you cannot catch it.");
         }
       } else {
/*  719 */         chr.dropMessage(5, "No monster was sucked. The skill failed.");
       }
/*  721 */       c.getSession().write(CWvsContext.enableActions());
/*  722 */       break;
     case 4341003:
/*  724 */       chr.setKeyDownSkill_Time(0L);
/*  725 */       chr.getMap().broadcastMessage(chr, CField.skillCancel(chr, skillid), false);
     default:
/*  728 */       Point pos = null;
/*  729 */       if ((slea.available() == 5L) || (slea.available() == 7L)) {
/*  730 */         pos = slea.readPos();
       }
/*  732 */       if (effect.isMagicDoor()) {
/*  733 */         if (!FieldLimitType.MysticDoor.check(chr.getMap().getFieldLimit()))
/*  734 */           effect.applyTo(c.getPlayer(), pos);
         else
/*  736 */           c.getSession().write(CWvsContext.enableActions());
       }
       else {
/*  739 */         int mountid = MapleStatEffect.parseMountInfo(c.getPlayer(), skill.getId());
/*  740 */         if ((mountid != 0) && (mountid != GameConstants.getMountItem(skill.getId(), c.getPlayer())) && (!c.getPlayer().isIntern()) && (c.getPlayer().getBuffedValue(MapleBuffStat.MONSTER_RIDING) == null) && (c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-122) == null) && 
/*  741 */           (!GameConstants.isMountItemAvailable(mountid, c.getPlayer().getJob()))) {
/*  742 */           c.getSession().write(CWvsContext.enableActions());
/*  743 */           return;
         }
 
         effect.applyTo(c.getPlayer(), pos);
       }
     }
   }
 
	public static final void closeRangeAttack(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr, final boolean energy) {
		if (chr == null || (energy && chr.getBuffedValue(MapleBuffStat.ENERGY_CHARGE) == null && chr.getBuffedValue(MapleBuffStat.BODY_PRESSURE) == null && chr.getBuffedValue(MapleBuffStat.DARK_AURA) == null && chr.getBuffedValue(MapleBuffStat.TORNADO) == null && chr.getBuffedValue(MapleBuffStat.SUMMON) == null && chr.getBuffedValue(MapleBuffStat.RAINING_MINES) == null && chr.getBuffedValue(MapleBuffStat.TELEPORT_MASTERY) == null)) {
			return;
		}
		if (chr.hasBlockedInventory() || chr.getMap() == null) {
			return;
		}
		AttackInfo attack = DamageParse.parseDmgM(slea, chr);
		if (attack == null) {
			c.getSession().write(CWvsContext.enableActions());
			return;
		}
		final boolean mirror = chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null;
		double maxdamage = chr.getStat().getCurrentMaxBaseDamage();
		final Item shield = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
		int attackCount = (shield != null && shield.getItemId() / 10000 == 134 ? 2 : 1);
		int skillLevel = 0;
		MapleStatEffect effect = null;
		Skill skill = null;

		if (attack.skill != 0) {
			skill = SkillFactory.getSkill(GameConstants.getLinkedAranSkill(attack.skill));
			if (skill == null || (GameConstants.isAngel(attack.skill) && (chr.getStat().equippedSummon % 10000) != (attack.skill % 10000))) {
				c.getSession().write(CWvsContext.enableActions());
				return;
			}
			skillLevel = chr.getTotalSkillLevel(skill);
			effect = attack.getAttackEffect(chr, skillLevel, skill);
			if (effect == null) {
				return;
			}
			if (GameConstants.isEventMap(chr.getMapId())) {
				for (MapleEventType t : MapleEventType.values()) {
					final MapleEvent e = ChannelServer.getInstance(chr.getClient().getChannel()).getEvent(t);
					if (e.isRunning() && !chr.isGM()) {
						for (int i : e.getType().mapids) {
							if (chr.getMapId() == i) {
								chr.dropMessage(5, "You may not use that here.");
								return; //non-skill cannot use
							}
						}
					}
				}
			}
			maxdamage *= (effect.getDamage() + chr.getStat().getDamageIncrease(attack.skill)) / 100.0;
			attackCount = effect.getAttackCount();

			if (effect.getCooldown(chr) > 0 && !chr.isGM() && !energy) {
				if (chr.skillisCooling(attack.skill)) {
					c.getSession().write(CWvsContext.enableActions());
					return;
				}
				c.getSession().write(CField.skillCooldown(attack.skill, effect.getCooldown(chr)));
				chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown(chr) * 1000);
			}
		}
		attack = DamageParse.Modify_AttackCrit(attack, chr, 1, effect);
		attackCount *= (mirror ? 2 : 1);
		if (!energy) {
			if ((chr.getMapId() == 109060000 || chr.getMapId() == 109060002 || chr.getMapId() == 109060004) && attack.skill == 0) {
				MapleSnowballs.hitSnowball(chr);
			}
			// handle combo orbconsume
			int numFinisherOrbs = 0;
			final Integer comboBuff = chr.getBuffedValue(MapleBuffStat.COMBO);

			if (isFinisher(attack.skill) > 0) { // finisher
				if (comboBuff != null) {
					numFinisherOrbs = comboBuff.intValue() - 1;
				}
				if (numFinisherOrbs <= 0) {
					return;
				}
				chr.handleOrbconsume(isFinisher(attack.skill));
				if (!GameConstants.GMS) {
					maxdamage *= numFinisherOrbs;
				}
			}
		}
		chr.checkFollow();
		if (!chr.isHidden()) {
			chr.getMap().broadcastMessage(chr, CField.closeRangeAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.speed, attack.allDamage, energy, chr.getLevel(), chr.getStat().passive_mastery(), attack.unk, attack.charge), chr.getTruePosition());
		} else {
			chr.getMap().broadcastGMMessage(chr, CField.closeRangeAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.speed, attack.allDamage, energy, chr.getLevel(), chr.getStat().passive_mastery(), attack.unk, attack.charge), false);
		}
		DamageParse.applyAttack(attack, skill, c.getPlayer(), attackCount, maxdamage, effect, mirror ? AttackType.NON_RANGED_WITH_MIRROR : AttackType.NON_RANGED);
		WeakReference<MapleCharacter>[] clones = chr.getClones();
		for (int i = 0; i < clones.length; i++) {
			if (clones[i].get() != null) {
				final MapleCharacter clone = clones[i].get();
				final Skill skil2 = skill;
				final int skillLevel2 = skillLevel;
				final int attackCount2 = attackCount;
				final double maxdamage2 = maxdamage;
				final MapleStatEffect eff2 = effect;
				final AttackInfo attack2 = DamageParse.DivideAttack(attack, chr.isGM() ? 1 : 4);
				CloneTimer.getInstance().schedule(new Runnable() {
					public void run() {
						if (!clone.isHidden()) {
							clone.getMap().broadcastMessage(CField.closeRangeAttack(clone.getId(), attack2.tbyte, attack2.skill, skillLevel2, attack2.display, attack2.speed, attack2.allDamage, energy, clone.getLevel(), clone.getStat().passive_mastery(), attack2.unk, attack2.charge));
						} else {
							clone.getMap().broadcastGMMessage(clone, CField.closeRangeAttack(clone.getId(), attack2.tbyte, attack2.skill, skillLevel2, attack2.display, attack2.speed, attack2.allDamage, energy, clone.getLevel(), clone.getStat().passive_mastery(), attack2.unk, attack2.charge), false);
						}
						DamageParse.applyAttack(attack2, skil2, chr, attackCount2, maxdamage2, eff2, mirror ? AttackType.NON_RANGED_WITH_MIRROR : AttackType.NON_RANGED);
					}
				}, 500 * i + 500);
			}
		}
	}

 
   public static final void rangedAttack(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
   {
/*  864 */     if (chr == null) {
/*  865 */       return;
     }
/*  867 */     if ((chr.hasBlockedInventory()) || (chr.getMap() == null)) {
/*  868 */       return;
     }
/*  870 */     AttackInfo attack = DamageParse.parseDmgR(slea, chr);
/*  871 */     if (attack == null) {
/*  872 */       c.getSession().write(CWvsContext.enableActions());
/*  873 */       return;
     }
/*  875 */     int bulletCount = 1; int skillLevel = 0;
/*  876 */     MapleStatEffect effect = null;
/*  877 */     Skill skill = null;
/*  878 */     boolean AOE = attack.skill == 4111004;
/*  879 */     boolean noBullet = ((chr.getJob() >= 3500) && (chr.getJob() <= 3512)) || (GameConstants.isCannon(chr.getJob())) || (GameConstants.isJett(chr.getJob())) || (GameConstants.isPhantom(chr.getJob())) || (GameConstants.isMercedes(chr.getJob()));
/*  880 */     if (attack.skill != 0) {
/*  881 */       skill = SkillFactory.getSkill(GameConstants.getLinkedAranSkill(attack.skill));
/*  882 */       if ((skill == null) || ((GameConstants.isAngel(attack.skill)) && (chr.getStat().equippedSummon % 10000 != attack.skill % 10000))) {
/*  883 */         c.getSession().write(CWvsContext.enableActions());
/*  884 */         return;
       }
/*  886 */       skillLevel = chr.getTotalSkillLevel(skill);
/*  887 */       effect = attack.getAttackEffect(chr, skillLevel, skill);
/*  888 */       if (effect == null) {
/*  889 */         return;
       }
/*  891 */       if (GameConstants.isEventMap(chr.getMapId())) {
/*  892 */         for (MapleEventType t : MapleEventType.values()) {
/*  893 */           MapleEvent e = ChannelServer.getInstance(chr.getClient().getChannel()).getEvent(t);
/*  894 */           if ((e.isRunning()) && (!chr.isGM())) {
/*  895 */             for (int i : e.getType().mapids) {
/*  896 */               if (chr.getMapId() == i) {
/*  897 */                 chr.dropMessage(5, "You may not use that here.");
/*  898 */                 return;
               }
             }
           }
         }
       }
            switch (attack.skill) {
                case 13101005:
                case 21110004: // Ranged but uses attackcount instead
                case 14101006: // Vampure
                case 21120006:
                case 11101004:
                case 51001004: // Mihile || Soul Blade
                case 1077:
                case 1078:
                case 1079:
                case 11077:
                case 11078:
                case 11079:
                case 15111007:
                case 13111007: //Wind Shot
                case 33101007:
                case 33101002:
                case 33121002:
                case 33121001:
                case 21100004:
                case 21110011:
                case 21100007:
                case 21000004:
                case 5121002:
                case 5921002:
                case 4121003:
                case 4221003:
                case 5221017:
                    
                case 5721007:
                    
                case 5221016:
                case 5721006:
                case 5211008:
                case 5201001:
                case 5721003:
                case 5711000:
                case 4111013:
                case 5121016:
                case 51111007: // Mihile || Radiant Buster
                case 51121008: // Mihile || Radiant Buster
                case 5121013:
                case 5221013:
                case 5721004:
                case 5721001:
                case 5321001:
                case 14111008:
                    AOE = true;
                    bulletCount = effect.getAttackCount();
                    break;
                case 35121005:
                case 35111004:
                case 35121013:
                    AOE = true;
                    bulletCount = 6;
                    break;
                default:
                    bulletCount = effect.getBulletCount();
                    break;
            }
            if (noBullet && effect.getBulletCount() < effect.getAttackCount()) {
                bulletCount = effect.getAttackCount();
            }
/*  945 */       if ((noBullet) && (effect.getBulletCount() < effect.getAttackCount())) {
/*  946 */         bulletCount = effect.getAttackCount();
       }
/*  948 */       if ((effect.getCooldown(chr) > 0) && (!chr.isGM()) && (((attack.skill != 35111004) && (attack.skill != 35121013)) || (chr.getBuffSource(MapleBuffStat.MECH_CHANGE) != attack.skill))) {
/*  949 */         if (chr.skillisCooling(attack.skill)) {
/*  950 */           c.getSession().write(CWvsContext.enableActions());
/*  951 */           return;
         }
/*  953 */         c.getSession().write(CField.skillCooldown(attack.skill, effect.getCooldown(chr)));
/*  954 */         chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown(chr) * 1000);
       }
     }
/*  957 */     attack = DamageParse.Modify_AttackCrit(attack, chr, 2, effect);
/*  958 */     Integer ShadowPartner = chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER);
/*  959 */     if (ShadowPartner != null) {
/*  960 */       bulletCount *= 2;
     }
/*  962 */     int projectile = 0; int visProjectile = 0;
/*  963 */     if ((!AOE) && (chr.getBuffedValue(MapleBuffStat.SOULARROW) == null) && (!noBullet)) {
/*  964 */       Item ipp = chr.getInventory(MapleInventoryType.USE).getItem((short)attack.slot);
/*  965 */       if (ipp == null) {
/*  966 */         return;
       }
/*  968 */       projectile = ipp.getItemId();
 
/*  970 */       if (attack.csstar > 0) {
/*  971 */         if (chr.getInventory(MapleInventoryType.CASH).getItem((short)attack.csstar) == null) {
/*  972 */           return;
         }
/*  974 */         visProjectile = chr.getInventory(MapleInventoryType.CASH).getItem((short)attack.csstar).getItemId();
       } else {
/*  976 */         visProjectile = projectile;
       }
 
/*  979 */       if (chr.getBuffedValue(MapleBuffStat.SPIRIT_CLAW) == null) {
/*  980 */         int bulletConsume = bulletCount;
/*  981 */         if ((effect != null) && (effect.getBulletConsume() != 0)) {
/*  982 */           bulletConsume = effect.getBulletConsume() * (ShadowPartner != null ? 2 : 1);
         }
/*  984 */         if ((chr.getJob() == 412) && (bulletConsume > 0) && (ipp.getQuantity() < MapleItemInformationProvider.getInstance().getSlotMax(projectile))) {
/*  985 */           Skill expert = SkillFactory.getSkill(4120010);
/*  986 */           if (chr.getTotalSkillLevel(expert) > 0) {
/*  987 */             MapleStatEffect eff = expert.getEffect(chr.getTotalSkillLevel(expert));
/*  988 */             if (eff.makeChanceResult()) {
/*  989 */               ipp.setQuantity((short)(ipp.getQuantity() + 1));
/*  990 */               c.getSession().write(CWvsContext.InventoryPacket.updateInventorySlot(MapleInventoryType.USE, ipp, false));
/*  991 */               bulletConsume = 0;
/*  992 */               c.getSession().write(CWvsContext.InventoryPacket.getInventoryStatus());
             }
           }
         }
/*  996 */         if ((bulletConsume > 0) && 
/*  997 */           (!MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, projectile, bulletConsume, false, true))) {
/*  998 */           chr.dropMessage(5, "You do not have enough arrows/bullets/stars.");
/*  999 */           return;
         }
       }
     }
/* 1003 */     else if ((chr.getJob() >= 3500) && (chr.getJob() <= 3512)) {
/* 1004 */       visProjectile = 2333000;
/* 1005 */     } else if (GameConstants.isCannon(chr.getJob())) {
/* 1006 */       visProjectile = 2333001;
     }
 
/* 1009 */     int projectileWatk = 0;
/* 1010 */     if (projectile != 0) {
/* 1011 */       projectileWatk = MapleItemInformationProvider.getInstance().getWatkForProjectile(projectile);
     }
/* 1013 */     PlayerStats statst = chr.getStat();
     double basedamage;
/* 1014 */     switch (attack.skill) {
     case 4001344:
     case 4121007:
     case 14001004:
     case 14111005:
/* 1019 */       basedamage = Math.max(statst.getCurrentMaxBaseDamage(), statst.getTotalLuk() * 5.0F * (statst.getTotalWatk() + projectileWatk) / 100.0F);
/* 1020 */       break;
     case 4111004:
/* 1023 */       basedamage = 53000.0D;
/* 1024 */       break;
     default:
/* 1026 */       basedamage = statst.getCurrentMaxBaseDamage();
/* 1027 */       switch (attack.skill) {
       case 3101005:
/* 1029 */         basedamage *= effect.getX() / 100.0D;
       }
 
     }
 
/* 1034 */     if (effect != null) {
/* 1035 */       basedamage *= (effect.getDamage() + statst.getDamageIncrease(attack.skill)) / 100.0D;
 
/* 1037 */       int money = effect.getMoneyCon();
/* 1038 */       if (money != 0) {
/* 1039 */         if (money > chr.getMeso()) {
/* 1040 */           money = chr.getMeso();
         }
/* 1042 */         chr.gainMeso(-money, false);
       }
     }
/* 1045 */     chr.checkFollow();
/* 1046 */     if (!chr.isHidden()) {
/* 1047 */       if (attack.skill == 3211006)
/* 1048 */         chr.getMap().broadcastMessage(chr, CField.strafeAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.speed, visProjectile, attack.allDamage, attack.position, chr.getLevel(), chr.getStat().passive_mastery(), attack.unk, chr.getTotalSkillLevel(3220010)), chr.getTruePosition());
       else {
/* 1050 */         chr.getMap().broadcastMessage(chr, CField.rangedAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.speed, visProjectile, attack.allDamage, attack.position, chr.getLevel(), chr.getStat().passive_mastery(), attack.unk), chr.getTruePosition());
       }
     }
/* 1053 */     else if (attack.skill == 3211006)
/* 1054 */       chr.getMap().broadcastGMMessage(chr, CField.strafeAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.speed, visProjectile, attack.allDamage, attack.position, chr.getLevel(), chr.getStat().passive_mastery(), attack.unk, chr.getTotalSkillLevel(3220010)), false);
     else {
/* 1056 */       chr.getMap().broadcastGMMessage(chr, CField.rangedAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.speed, visProjectile, attack.allDamage, attack.position, chr.getLevel(), chr.getStat().passive_mastery(), attack.unk), false);
     }
 
/* 1059 */     DamageParse.applyAttack(attack, skill, chr, bulletCount, basedamage, effect, ShadowPartner != null ? AttackType.RANGED_WITH_SHADOWPARTNER : AttackType.RANGED);
   }
 
   public static final void MagicDamage(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
   {
/* 1096 */     if ((chr == null) || (chr.hasBlockedInventory()) || (chr.getMap() == null)) {
/* 1097 */       return;
     }
/* 1099 */     AttackInfo attack = DamageParse.parseDmgMa(slea, chr);
/* 1100 */     if (attack == null) {
/* 1101 */       c.getSession().write(CWvsContext.enableActions());
/* 1102 */       return;
     }
/* 1104 */     Skill skill = SkillFactory.getSkill(GameConstants.getLinkedAranSkill(attack.skill));
/* 1105 */     if ((skill == null) || ((GameConstants.isAngel(attack.skill)) && (chr.getStat().equippedSummon % 10000 != attack.skill % 10000))) {
/* 1106 */       c.getSession().write(CWvsContext.enableActions());
/* 1107 */       return;
     }
/* 1109 */     int skillLevel = chr.getTotalSkillLevel(skill);
/* 1110 */     MapleStatEffect effect = attack.getAttackEffect(chr, skillLevel, skill);
/* 1111 */     if (effect == null) {
/* 1112 */       return;
     }
/* 1114 */     attack = DamageParse.Modify_AttackCrit(attack, chr, 3, effect);
/* 1115 */     if (GameConstants.isEventMap(chr.getMapId())) {
/* 1116 */       for (MapleEventType t : MapleEventType.values()) {
/* 1117 */         MapleEvent e = ChannelServer.getInstance(chr.getClient().getChannel()).getEvent(t);
/* 1118 */         if ((e.isRunning()) && (!chr.isGM())) {
/* 1119 */           for (int i : e.getType().mapids) {
/* 1120 */             if (chr.getMapId() == i) {
/* 1121 */               chr.dropMessage(5, "You may not use that here.");
/* 1122 */               return;
             }
           }
         }
       }
     }
/* 1128 */     double maxdamage = chr.getStat().getCurrentMaxBaseDamage() * (effect.getDamage() + chr.getStat().getDamageIncrease(attack.skill)) / 100.0D;
/* 1129 */     if (GameConstants.isPyramidSkill(attack.skill))
/* 1130 */       maxdamage = 1.0D;
/* 1131 */     else if ((GameConstants.isBeginnerJob(skill.getId() / 10000)) && (skill.getId() % 10000 == 1000)) {
/* 1132 */       maxdamage = 40.0D;
     }
/* 1134 */     if ((effect.getCooldown(chr) > 0) && (!chr.isGM())) {
/* 1135 */       if (chr.skillisCooling(attack.skill)) {
/* 1136 */         c.getSession().write(CWvsContext.enableActions());
/* 1137 */         return;
       }
/* 1139 */       c.getSession().write(CField.skillCooldown(attack.skill, effect.getCooldown(chr)));
/* 1140 */       chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown(chr) * 1000);
     }
/* 1142 */     chr.checkFollow();
/* 1143 */     if (!chr.isHidden())
/* 1144 */       chr.getMap().broadcastMessage(chr, CField.magicAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.speed, attack.allDamage, attack.charge, chr.getLevel(), attack.unk), chr.getTruePosition());
     else {
/* 1146 */       chr.getMap().broadcastGMMessage(chr, CField.magicAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.speed, attack.allDamage, attack.charge, chr.getLevel(), attack.unk), false);
     }
/* 1148 */     DamageParse.applyAttackMagic(attack, skill, c.getPlayer(), effect, maxdamage);
   }
 
   public static final void DropMeso(int meso, MapleCharacter chr)
   {
/* 1174 */     if ((!chr.isAlive()) || (meso < 10) || (meso > 50000) || (meso > chr.getMeso())) {
/* 1175 */       chr.getClient().getSession().write(CWvsContext.enableActions());
/* 1176 */       return;
     }
/* 1178 */     chr.gainMeso(-meso, false, true);
/* 1179 */     chr.getMap().spawnMesoDrop(meso, chr.getTruePosition(), chr, chr, true, (byte)0);
/* 1180 */     chr.getCheatTracker().checkDrop(true);
   }
 
   public static final void ChangeAndroidEmotion(int emote, MapleCharacter chr) {
/* 1184 */     if ((emote > 0) && (chr != null) && (chr.getMap() != null) && (!chr.isHidden()) && (emote <= 17) && (chr.getAndroid() != null))
/* 1185 */       chr.getMap().broadcastMessage(CField.showAndroidEmotion(chr.getId(), emote));
   }
 


    public static void MoveAndroid(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        slea.skip(8);
        final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 3);

        if (res != null && chr != null && !res.isEmpty() && chr.getMap() != null && chr.getAndroid() != null) { // map crash hack
            final Point pos = new Point(chr.getAndroid().getPos());
            chr.getAndroid().updatePosition(res);
            chr.getMap().broadcastMessage(chr, CField.moveAndroid(chr.getId(), pos, res), false);
        }
    }

 
   public static final void ChangeEmotion(int emote, MapleCharacter chr) {
/* 1201 */     if (emote > 7) {
/* 1202 */       int emoteid = 5159992 + emote;
/* 1203 */       MapleInventoryType type = GameConstants.getInventoryType(emoteid);
/* 1204 */       if (chr.getInventory(type).findById(emoteid) == null) {
/* 1205 */         chr.getCheatTracker().registerOffense(CheatingOffense.USING_UNAVAILABLE_ITEM, Integer.toString(emoteid));
/* 1206 */         return;
       }
     }
/* 1209 */     if ((emote > 0) && (chr != null) && (chr.getMap() != null) && (!chr.isHidden()))
/* 1210 */       chr.getMap().broadcastMessage(chr, CField.facialExpression(chr, emote), false);
   }
 
   public static final void Heal(LittleEndianAccessor slea, MapleCharacter chr)
   {
/* 1227 */     if (chr == null) {
/* 1228 */       return;
     }
/* 1230 */     chr.updateTick(slea.readInt());
/* 1231 */     if (slea.available() >= 8L) {
/* 1232 */       slea.skip((slea.available() >= 12L) && (GameConstants.GMS) ? 8 : 4);
     }
/* 1234 */     int healHP = slea.readShort();
/* 1235 */     int healMP = slea.readShort();
 
/* 1237 */     PlayerStats stats = chr.getStat();
 
/* 1239 */     if (stats.getHp() <= 0) {
/* 1240 */       return;
     }
/* 1242 */     long now = System.currentTimeMillis();
/* 1243 */     if ((healHP != 0) && (chr.canHP(now + 1000L))) {
/* 1244 */       if (healHP > stats.getHealHP())
       {
/* 1246 */         healHP = (int)stats.getHealHP();
       }
/* 1248 */       chr.addHP(healHP);
     }
/* 1250 */     if ((healMP != 0) && (!GameConstants.isDemon(chr.getJob())) && (chr.canMP(now + 1000L))) {
/* 1251 */       if (healMP > stats.getHealMP())
       {
/* 1253 */         healMP = (int)stats.getHealMP();
       }
/* 1255 */       chr.addMP(healMP);
     }
   }
 
   public static final void MovePlayer(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
   {
/* 1261 */     slea.skip(1);
/* 1262 */     slea.skip(4);
/* 1263 */     slea.skip(4);
/* 1264 */     slea.skip(4);
/* 1265 */     slea.skip(4);
/* 1266 */     if (chr == null) {
/* 1267 */       return;
/* 1269 */     }Point Original_Pos = chr.getPosition();
     List res;
     try { res = MovementParse.parseMovement(slea, 1);
     } catch (ArrayIndexOutOfBoundsException e) {
/* 1274 */       System.out.println(new StringBuilder().append("AIOBE Type1:\n").append(slea.toString(true)).toString());
/* 1275 */       return;
     }
 
/* 1278 */     if ((res != null) && (c.getPlayer().getMap() != null)) {
/* 1279 */       if ((slea.available() < 11L) || (slea.available() > 26L)) {
/* 1280 */         return;
       }
/* 1282 */       MapleMap map = c.getPlayer().getMap();
 
/* 1284 */       if (chr.isHidden()) {
/* 1285 */         chr.setLastRes(res);
/* 1286 */         c.getPlayer().getMap().broadcastGMMessage(chr, CField.movePlayer(chr.getId(), res, Original_Pos), false);
       } else {
/* 1288 */         c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.movePlayer(chr.getId(), res, Original_Pos), false);
       }
 
/* 1291 */       MovementParse.updatePosition(res, chr, 0);
/* 1292 */       Point pos = chr.getTruePosition();
/* 1293 */       map.movePlayer(chr, pos);
/* 1294 */       if ((chr.getFollowId() > 0) && (chr.isFollowOn()) && (chr.isFollowInitiator())) {
/* 1295 */         MapleCharacter fol = map.getCharacterById(chr.getFollowId());
/* 1296 */         if (fol != null) {
/* 1297 */           Point original_pos = fol.getPosition();
/* 1298 */           fol.getClient().getSession().write(CField.moveFollow(Original_Pos, original_pos, pos, res));
/* 1299 */           MovementParse.updatePosition(res, fol, 0);
/* 1300 */           map.movePlayer(fol, pos);
/* 1301 */           map.broadcastMessage(fol, CField.movePlayer(fol.getId(), res, original_pos), false);
         } else {
/* 1303 */           chr.checkFollow();
         }
 
       }
 
/* 1331 */       int count = c.getPlayer().getFallCounter();
/* 1332 */       boolean samepos = (pos.y > c.getPlayer().getOldPosition().y) && (Math.abs(pos.x - c.getPlayer().getOldPosition().x) < 5);
/* 1333 */       if ((samepos) && ((pos.y > map.getBottom() + 250) || (map.getFootholds().findBelow(pos) == null))) {
/* 1334 */         if (count > 5) {
/* 1335 */           c.getPlayer().changeMap(map, map.getPortal(0));
/* 1336 */           c.getPlayer().setFallCounter(0);
         } else {
/* 1338 */           count++; c.getPlayer().setFallCounter(count);
         }
/* 1340 */       } else if (count > 0) {
/* 1341 */         c.getPlayer().setFallCounter(0);
       }
/* 1343 */       c.getPlayer().setOldPosition(pos);
/* 1344 */       if ((!samepos) && (c.getPlayer().getBuffSource(MapleBuffStat.DARK_AURA) == 32120000))
/* 1345 */         c.getPlayer().getStatForBuff(MapleBuffStat.DARK_AURA).applyMonsterBuff(c.getPlayer());
/* 1346 */       else if ((!samepos) && (c.getPlayer().getBuffSource(MapleBuffStat.YELLOW_AURA) == 32120001))
/* 1347 */         c.getPlayer().getStatForBuff(MapleBuffStat.YELLOW_AURA).applyMonsterBuff(c.getPlayer());
     }
   }
 
   public static final void ChangeMapSpecial(String portal_name, MapleClient c, MapleCharacter chr)
   {
/* 1353 */     if ((chr == null) || (chr.getMap() == null)) {
/* 1354 */       return;
     }
/* 1356 */     MaplePortal portal = chr.getMap().getPortal(portal_name);
 
/* 1358 */    // if (chr.getGMLevel() > ServerConstants.PlayerGMRank.GM.getLevel()) {
/* 1359 */     //  chr.dropMessage(6, new StringBuilder().append(portal.getScriptName()).append(" accessed").toString());
   //  }
/* 1361 */     if ((portal != null) && (!chr.hasBlockedInventory()))
/* 1362 */       portal.enterPortal(c);
     else
/* 1364 */       c.getSession().write(CWvsContext.enableActions());
   }
 
   public static final void ChangeMap(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
   {
/* 1369 */     if ((chr == null) || (chr.getMap() == null)) {
/* 1370 */       return;
     }
/* 1372 */     if (slea.available() != 0L)
     {
/* 1374 */       slea.readByte();
/* 1375 */       int targetid = slea.readInt();
/* 1376 */       if (GameConstants.GMS) {
/* 1377 */         slea.readInt();
       }
/* 1379 */       MaplePortal portal = chr.getMap().getPortal(slea.readMapleAsciiString());
/* 1380 */       if (slea.available() >= 7L) {
/* 1381 */         chr.updateTick(slea.readInt());
       }
/* 1383 */       slea.skip(1);
/* 1384 */       boolean wheel = (slea.readShort() > 0) && (!GameConstants.isEventMap(chr.getMapId())) && (chr.haveItem(5510000, 1, false, true)) && (chr.getMapId() / 1000000 != 925);
 
/* 1386 */       if ((targetid != -1) && (!chr.isAlive())) {
/* 1387 */         chr.setStance(0);
/* 1388 */         if ((chr.getEventInstance() != null) && (chr.getEventInstance().revivePlayer(chr)) && (chr.isAlive())) {
/* 1389 */           return;
         }
/* 1391 */         if (chr.getPyramidSubway() != null) {
/* 1392 */           chr.getStat().setHp(50, chr);
/* 1393 */           chr.getPyramidSubway().fail(chr);
/* 1394 */           return;
         }
 
/* 1397 */         if (!wheel) {
/* 1398 */           chr.getStat().setHp(50, chr);
 
/* 1400 */           MapleMap to = chr.getMap().getReturnMap();
/* 1401 */           chr.changeMap(to, to.getPortal(0));
         } else {
/* 1403 */           c.getSession().write(CField.EffectPacket.useWheel((byte)(chr.getInventory(MapleInventoryType.CASH).countById(5510000) - 1)));
/* 1404 */           chr.getStat().setHp(chr.getStat().getMaxHp() / 100 * 40, chr);
/* 1405 */           MapleInventoryManipulator.removeById(c, MapleInventoryType.CASH, 5510000, 1, true, false);
 
/* 1407 */           MapleMap to = chr.getMap();
/* 1408 */           chr.changeMap(to, to.getPortal(0));
         }
/* 1410 */       } else if ((targetid != -1) && (chr.isIntern())) {
/* 1411 */         MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
/* 1412 */         if (to != null)
/* 1413 */           chr.changeMap(to, to.getPortal(0));
         else
/* 1415 */           chr.dropMessage(5, "Map is NULL. Use !warp <mapid> instead.");
       }
/* 1417 */       else if ((targetid != -1) && (!chr.isIntern())) {
/* 1418 */         int divi = chr.getMapId() / 100;
/* 1419 */         boolean unlock = false; boolean warp = false;
/* 1420 */         if (divi == 9130401) {
/* 1421 */           warp = (targetid / 100 == 9130400) || (targetid / 100 == 9130401);
/* 1422 */           if (targetid / 10000 != 91304) {
/* 1423 */             warp = true;
/* 1424 */             unlock = true;
/* 1425 */             targetid = 130030000;
           }
/* 1427 */         } else if (divi == 9130400) {
/* 1428 */           warp = (targetid / 100 == 9130400) || (targetid / 100 == 9130401);
/* 1429 */           if (targetid / 10000 != 91304) {
/* 1430 */             warp = true;
/* 1431 */             unlock = true;
/* 1432 */             targetid = 130030000;
           }
/* 1434 */         } else if (divi == 9140900) {
/* 1435 */           warp = (targetid == 914090011) || (targetid == 914090012) || (targetid == 914090013) || (targetid == 140090000);
/* 1436 */         } else if ((divi == 9120601) || (divi == 9140602) || (divi == 9140603) || (divi == 9140604) || (divi == 9140605)) {
/* 1437 */           warp = (targetid == 912060100) || (targetid == 912060200) || (targetid == 912060300) || (targetid == 912060400) || (targetid == 912060500) || (targetid == 3000100);
/* 1438 */           unlock = true;
/* 1439 */         } else if (divi == 9101500) {
/* 1440 */           warp = (targetid == 910150006) || (targetid == 101050010);
/* 1441 */           unlock = true;
/* 1442 */         } else if ((divi == 9140901) && (targetid == 140000000)) {
/* 1443 */           unlock = true;
/* 1444 */           warp = true;
/* 1445 */         } else if ((divi == 9240200) && (targetid == 924020000)) {
/* 1446 */           unlock = true;
/* 1447 */           warp = true;
/* 1448 */         } else if ((targetid == 980040000) && (divi >= 9800410) && (divi <= 9800450)) {
/* 1449 */           warp = true;
/* 1450 */         } else if ((divi == 9140902) && ((targetid == 140030000) || (targetid == 140000000))) {
/* 1451 */           unlock = true;
/* 1452 */           warp = true;
/* 1453 */         } else if ((divi == 9000900) && (targetid / 100 == 9000900) && (targetid > chr.getMapId())) {
/* 1454 */           warp = true;
/* 1455 */         } else if ((divi / 1000 == 9000) && (targetid / 100000 == 9000)) {
/* 1456 */           unlock = (targetid < 900090000) || (targetid > 900090004);
/* 1457 */           warp = true;
/* 1458 */         } else if ((divi / 10 == 1020) && (targetid == 1020000)) {
/* 1459 */           unlock = true;
/* 1460 */           warp = true;
/* 1461 */         } else if ((chr.getMapId() == 900090101) && (targetid == 100030100)) {
/* 1462 */           unlock = true;
/* 1463 */           warp = true;
/* 1464 */         } else if ((chr.getMapId() == 2010000) && (targetid == 104000000)) {
/* 1465 */           unlock = true;
/* 1466 */           warp = true;
/* 1467 */         } else if ((chr.getMapId() == 106020001) || (chr.getMapId() == 106020502)) {
/* 1468 */           if (targetid == chr.getMapId() - 1) {
/* 1469 */             unlock = true;
/* 1470 */             warp = true;
           }
/* 1472 */         } else if ((chr.getMapId() == 0) && (targetid == 10000)) {
/* 1473 */           unlock = true;
/* 1474 */           warp = true;
/* 1475 */         } else if ((chr.getMapId() == 931000011) && (targetid == 931000012)) {
/* 1476 */           unlock = true;
/* 1477 */           warp = true;
/* 1478 */         } else if ((chr.getMapId() == 931000021) && (targetid == 931000030)) {
/* 1479 */           unlock = true;
/* 1480 */           warp = true;
         }
/* 1482 */         if (unlock) {
/* 1483 */           c.getSession().write(CField.UIPacket.IntroDisableUI(false));
/* 1484 */           c.getSession().write(CField.UIPacket.IntroLock(false));
/* 1485 */           c.getSession().write(CWvsContext.enableActions());
         }
/* 1487 */         if (warp) {
/* 1488 */           MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
/* 1489 */           chr.changeMap(to, to.getPortal(0));
         }
       }
/* 1492 */       else if ((portal != null) && (!chr.hasBlockedInventory())) {
/* 1493 */         portal.enterPortal(c);
       } else {
/* 1495 */         c.getSession().write(CWvsContext.enableActions());
       }
     }
   }
 
   public static final void InnerPortal(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr)
   {
/* 1502 */     if ((chr == null) || (chr.getMap() == null)) {
/* 1503 */       return;
     }
/* 1505 */     MaplePortal portal = chr.getMap().getPortal(slea.readMapleAsciiString());
/* 1506 */     int toX = slea.readShort();
/* 1507 */     int toY = slea.readShort();
 
/* 1511 */     if (portal == null)
/* 1512 */       return;
/* 1513 */     if ((portal.getPosition().distanceSq(chr.getTruePosition()) > 22500.0D) && (!chr.isGM())) {
/* 1514 */       chr.getCheatTracker().registerOffense(CheatingOffense.USING_FARAWAY_PORTAL);
/* 1515 */       return;
     }
/* 1517 */     chr.getMap().movePlayer(chr, new Point(toX, toY));
/* 1518 */     chr.checkFollow();
   }
 
   public static final void snowBall(LittleEndianAccessor slea, MapleClient c)
   {
/* 1527 */     c.getSession().write(CWvsContext.enableActions());
   }
 
   public static final void leftKnockBack(LittleEndianAccessor slea, MapleClient c)
   {
/* 1532 */     if (c.getPlayer().getMapId() / 10000 == 10906) {
/* 1533 */       c.getSession().write(CField.leftKnockBack());
/* 1534 */       c.getSession().write(CWvsContext.enableActions());
     }
   }
 
   public static final void ReIssueMedal(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
/* 1539 */     MapleQuest q = MapleQuest.getInstance(slea.readShort());
/* 1540 */     int itemid = q.getMedalItem();
/* 1541 */     if ((itemid != slea.readInt()) || (itemid <= 0) || (q == null) || (chr.getQuestStatus(q.getId()) != 2)) {
/* 1542 */       c.getSession().write(CField.UIPacket.reissueMedal(itemid, 4));
/* 1543 */       return;
     }
/* 1545 */     if (chr.haveItem(itemid, 1, true, true)) {
/* 1546 */       c.getSession().write(CField.UIPacket.reissueMedal(itemid, 3));
/* 1547 */       return;
     }
/* 1549 */     if (!MapleInventoryManipulator.checkSpace(c, itemid, 1, "")) {
/* 1550 */       c.getSession().write(CField.UIPacket.reissueMedal(itemid, 2));
/* 1551 */       return;
     }
/* 1553 */     if (chr.getMeso() < 100) {
/* 1554 */       c.getSession().write(CField.UIPacket.reissueMedal(itemid, 1));
/* 1555 */       return;
     }
/* 1557 */     chr.gainMeso(-100, true, true);
/* 1558 */     MapleInventoryManipulator.addById(c, itemid, (byte)1, new StringBuilder().append("Redeemed item through medal quest ").append(q.getId()).append(" on ").append(FileoutputUtil.CurrentReadable_Date()).toString());
/* 1559 */     c.getSession().write(CField.UIPacket.reissueMedal(itemid, 0));
   }
 

 }
