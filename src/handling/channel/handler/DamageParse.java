/*      */ package handling.channel.handler;
/*      */ 
/*      */ import client.MapleBuffStat;
/*      */ import client.MapleCharacter;
/*      */ import client.MapleClient;
/*      */ import client.PlayerStats;
/*      */ import client.Skill;
/*      */ import client.SkillFactory;
/*      */ import client.anticheat.CheatTracker;
/*      */ import client.anticheat.CheatingOffense;
/*      */ import client.inventory.Item;
/*      */ import client.inventory.MapleInventory;
/*      */ import client.inventory.MapleInventoryType;
/*      */ import client.status.MonsterStatus;
/*      */ import client.status.MonsterStatusEffect;
/*      */ import constants.GameConstants;
/*      */ import handling.channel.ChannelServer;
/*      */ import java.awt.Point;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import org.apache.mina.common.IoSession;
/*      */ import server.MapleStatEffect;
/*      */ import server.Randomizer;
/*      */ import server.life.Element;
/*      */ import server.life.ElementalEffectiveness;
/*      */ import server.life.MapleMonster;
/*      */ import server.life.MapleMonsterStats;
/*      */ import server.maps.Event_PyramidSubway;
/*      */ import server.maps.MapleMap;
/*      */ import server.maps.MapleMapItem;
/*      */ import server.maps.MapleMapObject;
/*      */ import server.maps.MapleMapObjectType;
/*      */ import tools.AttackPair;
/*      */ import tools.Pair;
/*      */ import tools.data.LittleEndianAccessor;
/*      */ import tools.packet.CField;
/*      */ import tools.packet.CWvsContext;
/*      */ 
/*      */ public class DamageParse
/*      */ {
/*      */   public static void applyAttack(AttackInfo attack, Skill theSkill, MapleCharacter player, int attackCount, double maxDamagePerMonster, MapleStatEffect effect, AttackType attack_type)
/*      */   {
/*   59 */     if (!player.isAlive()) {
/*   60 */       player.getCheatTracker().registerOffense(CheatingOffense.ATTACKING_WHILE_DEAD);
/*   61 */       return;
/*      */     }
/*   63 */     if ((attack.real) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
/*   64 */       player.getCheatTracker().checkAttack(attack.skill, attack.lastAttackTickCount);
/*      */     }
/*   66 */     if (attack.skill != 0) {
/*   67 */       if (effect == null) {
/*   68 */         player.getClient().getSession().write(CWvsContext.enableActions());
/*   69 */         return;
/*      */       }
/*   71 */       if (GameConstants.isMulungSkill(attack.skill)) {
/*   72 */         if (player.getMapId() / 10000 != 92502)
/*      */         {
/*   74 */           return;
/*      */         }
/*   76 */         if (player.getMulungEnergy() < 10000) {
/*   77 */           return;
/*      */         }
/*   79 */         player.mulung_EnergyModify(false);
/*      */       }
/*   81 */       else if (GameConstants.isPyramidSkill(attack.skill)) {
/*   82 */         if (player.getMapId() / 1000000 != 926)
/*      */         {
/*   84 */           return;
/*      */         }
/*   86 */         if ((player.getPyramidSubway() == null) || (!player.getPyramidSubway().onSkillUse(player))) {
/*   87 */           return;
/*      */         }
/*      */       }
/*   90 */       else if (GameConstants.isInflationSkill(attack.skill)) {
/*   91 */         if (player.getBuffedValue(MapleBuffStat.GIANT_POTION) == null)
/*   92 */           return;
/*      */       }
/*   94 */       else if ((attack.targets > effect.getMobCount()) && (attack.skill != 1211002) && (attack.skill != 1220010)) {
/*   95 */         player.getCheatTracker().registerOffense(CheatingOffense.MISMATCHING_BULLETCOUNT);
/*   96 */         return;
/*      */       }
/*      */     }
/*   99 */     if (player.getClient().getChannelServer().isAdminOnly()) {
/*  100 */       player.dropMessage(-1, new StringBuilder().append("Animation: ").append(Integer.toHexString((attack.display & 0x8000) != 0 ? attack.display - 32768 : attack.display)).toString());
/*      */     }
/*  102 */     boolean useAttackCount = (attack.skill != 4211006) && (attack.skill != 3221007) && (attack.skill != 23121003) && ((attack.skill != 1311001) || (player.getJob() != 132)) && (attack.skill != 3211006);
/*      */ 
/*  109 */     if ((attack.hits > 0) && (attack.targets > 0))
/*      */     {
/*  111 */       if (!player.getStat().checkEquipDurabilitys(player, -1)) {
/*  112 */         player.dropMessage(5, "An item has run out of durability but has no inventory room to go to.");
/*  113 */         return;
/*      */       }
/*      */     }
/*  116 */     int totDamage = 0;
/*  117 */     MapleMap map = player.getMap();
/*      */ 
/*  119 */     if (attack.skill == 4211006) {
/*  120 */       for (AttackPair oned : attack.allDamage) {
/*  121 */         if (oned.attack != null) {
/*      */           continue;
/*      */         }
/*  124 */         MapleMapObject mapobject = map.getMapObject(oned.objectid, MapleMapObjectType.ITEM);
/*      */ 
/*  126 */         if (mapobject != null) {
/*  127 */           MapleMapItem mapitem = (MapleMapItem)mapobject;
/*  128 */           mapitem.getLock().lock();
/*      */           try {
/*  130 */             if (mapitem.getMeso() > 0) {
/*  131 */               if (mapitem.isPickedUp())
/*      */                 return;
/*  134 */               map.removeMapObject(mapitem);
/*  135 */               map.broadcastMessage(CField.explodeDrop(mapitem.getObjectId()));
/*  136 */               mapitem.setPickedUp(true); } else { player.getCheatTracker().registerOffense(CheatingOffense.ETC_EXPLOSION);
/*      */               return;
/*      */             }
/*      */           } finally {
/*  142 */             mapitem.getLock().unlock();
/*      */           }
/*      */         } else {
/*  145 */           player.getCheatTracker().registerOffense(CheatingOffense.EXPLODING_NONEXISTANT);
/*  146 */           return;
/*      */         }
/*      */       }
/*      */     }
/*  150 */     int totDamageToOneMonster = 0;
/*  151 */     long hpMob = 0L;
/*  152 */     PlayerStats stats = player.getStat();
/*      */ 
/*  154 */     int CriticalDamage = stats.passive_sharpeye_percent();
/*  155 */     int ShdowPartnerAttackPercentage = 0;
/*  156 */     if ((attack_type == AttackType.RANGED_WITH_SHADOWPARTNER) || (attack_type == AttackType.NON_RANGED_WITH_MIRROR)) {
/*  157 */       MapleStatEffect shadowPartnerEffect = player.getStatForBuff(MapleBuffStat.SHADOWPARTNER);
/*  158 */       if (shadowPartnerEffect != null) {
/*  159 */         ShdowPartnerAttackPercentage += shadowPartnerEffect.getX();
/*      */       }
/*  161 */       attackCount /= 2;
/*      */     }
/*  163 */     ShdowPartnerAttackPercentage *= (CriticalDamage + 100) / 100;
/*  164 */     if (attack.skill == 4221001) {
/*  165 */       ShdowPartnerAttackPercentage *= 10;
/*      */     }
/*      */ 
/*  168 */     double maxDamagePerHit = 0.0D;
/*      */     MapleMonster monster;
/*  173 */     for (AttackPair oned : attack.allDamage) {
/*  174 */       monster = map.getMonsterByOid(oned.objectid);
/*      */ 
/*  176 */       if ((monster != null) && (monster.getLinkCID() <= 0)) {
/*  177 */         totDamageToOneMonster = 0;
/*  178 */         hpMob = monster.getMobMaxHp();
/*  179 */         MapleMonsterStats monsterstats = monster.getStats();
/*  180 */         int fixeddmg = monsterstats.getFixedDamage();
/*  181 */         boolean Tempest = (monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006) || (attack.skill == 21120006) || (attack.skill == 1221011);
/*      */ 
/*  183 */         if ((!Tempest) && (!player.isGM())) {
/*  184 */           if (((player.getJob() >= 3200) && (player.getJob() <= 3212) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) || (attack.skill == 3221007) || (attack.skill == 23121003) || (((player.getJob() < 3200) || (player.getJob() > 3212)) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT))))
/*  185 */             maxDamagePerHit = CalculateMaxWeaponDamagePerHit(player, monster, attack, theSkill, effect, maxDamagePerMonster, Integer.valueOf(CriticalDamage));
/*      */           else {
/*  187 */             maxDamagePerHit = 1.0D;
/*      */           }
/*      */         }
/*  190 */         byte overallAttackCount = 0;
/*      */ 
/*  192 */         int criticals = 0;
/*  193 */         for (Pair eachde : oned.attack) {
/*  194 */           Integer eachd = (Integer)eachde.left;
/*  195 */           overallAttackCount = (byte)(overallAttackCount + 1);
/*  196 */           if (((Boolean)eachde.right).booleanValue()) {
/*  197 */             criticals++;
/*      */           }
/*  199 */           if ((useAttackCount) && (overallAttackCount - 1 == attackCount)) {
/*  200 */             maxDamagePerHit = maxDamagePerHit / 100.0D * (ShdowPartnerAttackPercentage * (monsterstats.isBoss() ? stats.bossdam_r : stats.dam_r) / 100.0D);
/*      */           }
/*      */ 
/*  203 */           if (fixeddmg != -1) {
/*  204 */             if (monsterstats.getOnlyNoramlAttack())
/*  205 */               eachd = Integer.valueOf(attack.skill != 0 ? 0 : fixeddmg);
/*      */             else {
/*  207 */               eachd = Integer.valueOf(fixeddmg);
/*      */             }
/*      */           }
/*  210 */           else if (monsterstats.getOnlyNoramlAttack())
/*  211 */             eachd = Integer.valueOf(attack.skill != 0 ? 0 : Math.min(eachd.intValue(), (int)maxDamagePerHit));
/*  212 */           else if (!player.isGM()) {
/*  213 */             if (Tempest) {
/*  214 */               if (eachd.intValue() > monster.getMobMaxHp()) {
/*  215 */                 eachd = Integer.valueOf((int)Math.min(monster.getMobMaxHp(), 2147483647L));
/*  216 */                 player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE);
/*      */               }
/*      */             }
/*  219 */             else if (((player.getJob() >= 3200) && (player.getJob() <= 3212) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) || (attack.skill == 23121003) || (((player.getJob() < 3200) || (player.getJob() > 3212)) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)))) {
/*  220 */               if (eachd.intValue() > maxDamagePerHit) {
/*  221 */                 player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(maxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
/*  222 */                 if (attack.real) {
/*  223 */                   player.getCheatTracker().checkSameDamage(eachd.intValue(), maxDamagePerHit);
/*      */                 }
/*  225 */                 if (eachd.intValue() > maxDamagePerHit * 2.0D) {
/*  226 */                   player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_2, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(maxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
/*  227 */                   eachd = Integer.valueOf((int)(maxDamagePerHit * 2.0D));
/*  228 */                   if (eachd.intValue() >= 2499999) {
/*  229 */                     player.getClient().getSession().close();
/*  230 */                     return;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*  235 */             else if (eachd.intValue() > maxDamagePerHit) {
/*  236 */               eachd = Integer.valueOf((int)maxDamagePerHit);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  241 */           if (player == null) {
/*  242 */             return;
/*      */           }
/*  244 */           totDamageToOneMonster += eachd.intValue();
/*      */ 
/*  246 */           if (((eachd.intValue() == 0) || (monster.getId() == 9700021)) && (player.getPyramidSubway() != null)) {
/*  247 */             player.getPyramidSubway().onMiss(player);
/*      */           }
/*      */         }
/*  250 */         totDamage += totDamageToOneMonster;
/*  251 */         player.checkMonsterAggro(monster);
/*      */ 
/*  253 */         if ((GameConstants.getAttackDelay(attack.skill, theSkill) >= 100) && (!GameConstants.isNoDelaySkill(attack.skill)) && (attack.skill != 3101005) && (!monster.getStats().isBoss()) && (player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants.getAttackRange(effect, player.getStat().defRange))) {
/*  254 */           player.getCheatTracker().registerOffense(CheatingOffense.ATTACK_FARAWAY_MONSTER, new StringBuilder().append("[Distance: ").append(player.getTruePosition().distanceSq(monster.getTruePosition())).append(", Expected Distance: ").append(GameConstants.getAttackRange(effect, player.getStat().defRange)).append(" Job: ").append(player.getJob()).append("]").toString());
/*      */         }
/*      */ 
/*  257 */         if (player.getBuffedValue(MapleBuffStat.PICKPOCKET) != null) {
/*  258 */           switch (attack.skill) {
/*      */           case 0:
/*      */           case 4001334:
/*      */           case 4201005:
/*      */           case 4211002:
/*      */           case 4211004:
/*      */           case 4221003:
/*      */           case 4221007:
/*  266 */             handlePickPocket(player, monster, oned);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  271 */         if ((totDamageToOneMonster > 0) || (attack.skill == 1221011) || (attack.skill == 21120006)) {
/*  272 */           if (GameConstants.isDemon(player.getJob())) {
/*  273 */             player.handleForceGain(monster.getObjectId(), attack.skill);
/*      */           }
          if ((GameConstants.isPhantom(player.getJob())) && (attack.skill != 24120002) && (attack.skill != 24100003)) {
            player.handleCardStack();
          }
/*  275 */           if (attack.skill != 1221011)
/*  276 */             monster.damage(player, totDamageToOneMonster, true, attack.skill);
/*      */           else {
/*  278 */             monster.damage(player, monster.getStats().isBoss() ? 500000L : monster.getHp() - 1L, true, attack.skill);
/*      */           }
/*      */ 
/*  281 */           if (monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)) {
/*  282 */             player.addHP(-(7000 + Randomizer.nextInt(8000)));
/*      */           }
/*  284 */           player.onAttack(monster.getMobMaxHp(), monster.getMobMaxMp(), attack.skill, monster.getObjectId(), totDamage, 0);
/*  285 */           switch (attack.skill)
/*      */           {
/*      */           case 4001002:
/*      */           case 4001334:
/*      */           case 4001344:
/*      */           case 4111005:
/*      */           case 4121007:
/*      */           case 4201005:
/*      */           case 4211002:
/*      */           case 4221001:
/*      */           case 4221007:
/*      */           case 4301001:
/*      */           case 4311002:
/*      */           case 4311003:
/*      */           case 4331000:
/*      */           case 4331004:
/*      */           case 4331005:
/*      */           case 4331006:
/*      */           case 4341002:
/*      */           case 4341004:
/*      */           case 4341005:
/*      */           case 4341009:
/*      */           case 14001004:
/*      */           case 14111002:
/*      */           case 14111005:
/*  310 */             int[] skills = { 4120005, 4220005, 4340001, 14110004 };
/*  311 */             for (int i : skills) {
/*  312 */               Skill skill = SkillFactory.getSkill(i);
/*  313 */               if (player.getTotalSkillLevel(skill) > 0) {
/*  314 */                 MapleStatEffect venomEffect = skill.getEffect(player.getTotalSkillLevel(skill));
/*  315 */                 if (!venomEffect.makeChanceResult()) break;
/*  316 */                 monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.POISON, Integer.valueOf(1), i, null, false), true, venomEffect.getDuration(), true, venomEffect); break;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  322 */             break;
/*      */           case 4201004:
/*  325 */             monster.handleSteal(player);
/*  326 */             break;
/*      */           case 21000002:
/*      */           case 21100001:
/*      */           case 21100002:
/*      */           case 21100004:
/*      */           case 21110002:
/*      */           case 21110003:
/*      */           case 21110004:
/*      */           case 21110006:
/*      */           case 21110007:
/*      */           case 21110008:
/*      */           case 21120002:
/*      */           case 21120005:
/*      */           case 21120006:
/*      */           case 21120009:
/*      */           case 21120010:
            MapleStatEffect eff;
            if ((player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) && (!(monster.getStats().isBoss()))) {
              eff = player.getStatForBuff(MapleBuffStat.WK_CHARGE);
              if (eff != null) {
                monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.SPEED, Integer.valueOf(eff.getX()), eff.getSourceId(), null, false), false, eff.getY() * 1000, true, eff);
              }
            }
            if ((player.getBuffedValue(MapleBuffStat.BODY_PRESSURE) != null) && (!(monster.getStats().isBoss()))) {
              eff = player.getStatForBuff(MapleBuffStat.BODY_PRESSURE);

              if ((eff != null) && (eff.makeChanceResult()) && (!(monster.isBuffed(MonsterStatus.NEUTRALISE)))) {
                monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.NEUTRALISE, Integer.valueOf(1), eff.getSourceId(), null, false), false, eff.getX() * 1000, true, eff);
              }

            }

          }
/*      */ 
/*  362 */           if (totDamageToOneMonster > 0) {
/*  363 */             Item weapon_ = player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-11);
/*  364 */             if (weapon_ != null) {
/*  365 */               MonsterStatus stat = GameConstants.getStatFromWeapon(weapon_.getItemId());
/*  366 */               if ((stat != null) && (Randomizer.nextInt(100) < GameConstants.getStatChance())) {
/*  367 */                 MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(stat, Integer.valueOf(GameConstants.getXForStat(stat)), GameConstants.getSkillForStat(stat), null, false);
/*  368 */                 monster.applyStatus(player, monsterStatusEffect, false, 10000L, false, null);
/*      */               }
/*      */             }
/*  371 */             if (player.getBuffedValue(MapleBuffStat.BLIND) != null) {
/*  372 */               MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.BLIND);
/*      */ 
/*  374 */               if ((eff != null) && (eff.makeChanceResult())) {
/*  375 */                 MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.ACC, Integer.valueOf(eff.getX()), eff.getSourceId(), null, false);
/*  376 */                 monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, true, eff);
/*      */               }
/*      */             }
/*      */ 
/*  380 */             if (player.getBuffedValue(MapleBuffStat.HAMSTRING) != null) {
/*  381 */               MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.HAMSTRING);
/*      */ 
/*  383 */               if ((eff != null) && (eff.makeChanceResult())) {
/*  384 */                 MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.SPEED, Integer.valueOf(eff.getX()), 3121007, null, false);
/*  385 */                 monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, true, eff);
/*      */               }
/*      */             }
/*  388 */             if ((player.getJob() == 121) || (player.getJob() == 122)) {
/*  389 */               Skill skill = SkillFactory.getSkill(1211006);
/*  390 */               if (player.isBuffFrom(MapleBuffStat.WK_CHARGE, skill)) {
/*  391 */                 MapleStatEffect eff = skill.getEffect(player.getTotalSkillLevel(skill));
/*  392 */                 MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.FREEZE, Integer.valueOf(1), skill.getId(), null, false);
/*  393 */                 monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 2000, true, eff);
/*      */               }
/*      */             }
/*  396 */            

/*      */           }
/*  403 */           if ((effect != null) && (effect.getMonsterStati().size() > 0) && 
/*  404 */             (effect.makeChanceResult()))
/*  405 */             for (Map.Entry z : effect.getMonsterStati().entrySet())
/*  406 */               monster.applyStatus(player, new MonsterStatusEffect((MonsterStatus)z.getKey(), (Integer)z.getValue(), theSkill.getId(), null, false), effect.isPoison(), effect.getDuration(), true, effect);
/*      */         }
/*      */       }
/*      */     }

/*  413 */     if ((attack.skill == 4331003) && ((hpMob <= 0L) || (totDamageToOneMonster < hpMob))) {
/*  414 */       return;
/*      */     }
/*  416 */     if ((hpMob > 0L) && (totDamageToOneMonster > 0)) {
/*  417 */       player.afterAttack(attack.targets, attack.hits, attack.skill);
/*      */     }
/*  419 */     if ((attack.skill != 0) && ((attack.targets > 0) || ((attack.skill != 4331003) && (attack.skill != 4341002))) && (!GameConstants.isNoDelaySkill(attack.skill))) {
/*  420 */       effect.applyTo(player, attack.position);
/*      */     }
/*  422 */     if ((totDamage > 1) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
/*  423 */       CheatTracker tracker = player.getCheatTracker();
/*      */ 
/*  425 */       tracker.setAttacksWithoutHit(true);
/*  426 */       if (tracker.getAttacksWithoutHit() > 1000)
/*  427 */         tracker.registerOffense(CheatingOffense.ATTACK_WITHOUT_GETTING_HIT, Integer.toString(tracker.getAttacksWithoutHit()));
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final void applyAttackMagic(AttackInfo attack, Skill theSkill, MapleCharacter player, MapleStatEffect effect, double maxDamagePerHit)
/*      */   {
/*  433 */     if (!player.isAlive()) {
/*  434 */       player.getCheatTracker().registerOffense(CheatingOffense.ATTACKING_WHILE_DEAD);
/*  435 */       return;
/*      */     }
/*  437 */     if ((attack.real) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
/*  438 */       player.getCheatTracker().checkAttack(attack.skill, attack.lastAttackTickCount);
/*      */     }
/*      */ 
/*  443 */     if ((attack.hits > effect.getAttackCount()) || (attack.targets > effect.getMobCount())) {
/*  444 */       player.getCheatTracker().registerOffense(CheatingOffense.MISMATCHING_BULLETCOUNT);
/*  445 */       return;
/*      */     }
/*  447 */     if ((attack.hits > 0) && (attack.targets > 0) && 
/*  448 */       (!player.getStat().checkEquipDurabilitys(player, -1))) {
/*  449 */       player.dropMessage(5, "An item has run out of durability but has no inventory room to go to.");
/*  450 */       return;
/*      */     }
/*      */ 
/*  453 */     if (GameConstants.isMulungSkill(attack.skill)) {
/*  454 */       if (player.getMapId() / 10000 != 92502)
/*      */       {
/*  456 */         return;
/*      */       }
/*  458 */       if (player.getMulungEnergy() < 10000) {
/*  459 */         return;
/*      */       }
/*  461 */       player.mulung_EnergyModify(false);
/*      */     }
/*  463 */     else if (GameConstants.isPyramidSkill(attack.skill)) {
/*  464 */       if (player.getMapId() / 1000000 != 926)
/*      */       {
/*  466 */         return;
/*      */       }
/*  468 */       if ((player.getPyramidSubway() == null) || (!player.getPyramidSubway().onSkillUse(player))) {
/*  469 */         return;
/*      */       }
/*      */     }
/*  472 */     else if ((GameConstants.isInflationSkill(attack.skill)) && 
/*  473 */       (player.getBuffedValue(MapleBuffStat.GIANT_POTION) == null)) {
/*  474 */       return;
/*      */     }
/*      */ 
/*  477 */     if (player.getClient().getChannelServer().isAdminOnly()) {
/*  478 */       player.dropMessage(-1, new StringBuilder().append("Animation: ").append(Integer.toHexString((attack.display & 0x8000) != 0 ? attack.display - 32768 : attack.display)).toString());
/*      */     }
/*  480 */     PlayerStats stats = player.getStat();
/*  481 */     Element element = player.getBuffedValue(MapleBuffStat.ELEMENT_RESET) != null ? Element.NEUTRAL : theSkill.getElement();
/*      */ 
/*  483 */     double MaxDamagePerHit = 0.0D;
/*  484 */     int totDamage = 0;
/*      */ 
/*  488 */     int CriticalDamage = stats.passive_sharpeye_percent();
/*  489 */     Skill eaterSkill = SkillFactory.getSkill(GameConstants.getMPEaterForJob(player.getJob()));
/*  490 */     int eaterLevel = player.getTotalSkillLevel(eaterSkill);
/*      */ 
/*  492 */     MapleMap map = player.getMap();
/*      */ 
/*  494 */     for (AttackPair oned : attack.allDamage) {
/*  495 */       MapleMonster monster = map.getMonsterByOid(oned.objectid);
/*      */ 
/*  497 */       if ((monster != null) && (monster.getLinkCID() <= 0)) {
/*  498 */         boolean Tempest = (monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006) && (!monster.getStats().isBoss());
/*  499 */         int totDamageToOneMonster = 0;
/*  500 */         MapleMonsterStats monsterstats = monster.getStats();
/*  501 */         int fixeddmg = monsterstats.getFixedDamage();
/*  502 */         if ((!Tempest) && (!player.isGM())) {
/*  503 */           if ((!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT)))
/*  504 */             MaxDamagePerHit = CalculateMaxMagicDamagePerHit(player, theSkill, monster, monsterstats, stats, element, Integer.valueOf(CriticalDamage), maxDamagePerHit, effect);
/*      */           else {
/*  506 */             MaxDamagePerHit = 1.0D;
/*      */           }
/*      */         }
/*  509 */         byte overallAttackCount = 0;
/*      */ 
/*  511 */         for (Pair eachde : oned.attack) {
/*  512 */           Integer eachd = (Integer)eachde.left;
/*  513 */           overallAttackCount = (byte)(overallAttackCount + 1);
/*  514 */           if (fixeddmg != -1) {
/*  515 */             eachd = Integer.valueOf(monsterstats.getOnlyNoramlAttack() ? 0 : fixeddmg);
/*      */           }
/*  517 */           else if (monsterstats.getOnlyNoramlAttack())
/*  518 */             eachd = Integer.valueOf(0);
/*  519 */           else if (!player.isGM())
/*      */           {
/*  522 */             if (Tempest)
/*      */             {
/*  524 */               if (eachd.intValue() > monster.getMobMaxHp()) {
/*  525 */                 eachd = Integer.valueOf((int)Math.min(monster.getMobMaxHp(), 2147483647L));
/*  526 */                 player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_MAGIC);
/*      */               }
/*  528 */             } else if ((!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) {
/*  529 */               if (eachd.intValue() > MaxDamagePerHit) {
/*  530 */                 player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_MAGIC, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(MaxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
/*  531 */                 if (attack.real) {
/*  532 */                   player.getCheatTracker().checkSameDamage(eachd.intValue(), MaxDamagePerHit);
/*      */                 }
/*  534 */                 if (eachd.intValue() > MaxDamagePerHit * 2.0D)
/*      */                 {
/*  536 */                   player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_MAGIC_2, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(MaxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
/*  537 */                   eachd = Integer.valueOf((int)(MaxDamagePerHit * 2.0D));
/*      */ 
/*  539 */                   if (eachd.intValue() >= 2499999) {
/*  540 */                     player.getClient().getSession().close();
/*  541 */                     return;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*  546 */             else if (eachd.intValue() > MaxDamagePerHit) {
/*  547 */               eachd = Integer.valueOf((int)MaxDamagePerHit);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  552 */           totDamageToOneMonster += eachd.intValue();
/*      */         }
/*  554 */         totDamage += totDamageToOneMonster;
/*  555 */         player.checkMonsterAggro(monster);
/*      */ 
/*  557 */         if ((GameConstants.getAttackDelay(attack.skill, theSkill) >= 100) && (!GameConstants.isNoDelaySkill(attack.skill)) && (!monster.getStats().isBoss()) && (player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants.getAttackRange(effect, player.getStat().defRange))) {
/*  558 */           player.getCheatTracker().registerOffense(CheatingOffense.ATTACK_FARAWAY_MONSTER, new StringBuilder().append("[Distance: ").append(player.getTruePosition().distanceSq(monster.getTruePosition())).append(", Expected Distance: ").append(GameConstants.getAttackRange(effect, player.getStat().defRange)).append(" Job: ").append(player.getJob()).append("]").toString());
/*      */         }
/*  560 */         if ((attack.skill == 2301002) && (!monsterstats.getUndead())) {
/*  561 */           player.getCheatTracker().registerOffense(CheatingOffense.HEAL_ATTACKING_UNDEAD);
/*  562 */           return;
/*      */         }
/*      */ 
/*  565 */         if (totDamageToOneMonster > 0) {
/*  566 */           monster.damage(player, totDamageToOneMonster, true, attack.skill);
/*  567 */           if (monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT)) {
/*  568 */             player.addHP(-(7000 + Randomizer.nextInt(8000)));
/*      */           }
/*  570 */           if (player.getBuffedValue(MapleBuffStat.SLOW) != null) {
/*  571 */             MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.SLOW);
/*      */ 
/*  573 */             if ((eff != null) && (eff.makeChanceResult()) && (!monster.isBuffed(MonsterStatus.SPEED))) {
/*  574 */               monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.SPEED, Integer.valueOf(eff.getX()), eff.getSourceId(), null, false), false, eff.getY() * 1000, true, eff);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  580 */           player.onAttack(monster.getMobMaxHp(), monster.getMobMaxMp(), attack.skill, monster.getObjectId(), totDamage, 0);
/*      */ 
/*  582 */           switch (attack.skill) {
/*      */           case 2221003:
/*  584 */             monster.setTempEffectiveness(Element.ICE, effect.getDuration());
/*  585 */             break;
/*      */           case 2121003:
/*  587 */             monster.setTempEffectiveness(Element.FIRE, effect.getDuration());
/*      */           }
/*      */ 
/*  590 */           if ((effect != null) && (effect.getMonsterStati().size() > 0) && 
/*  591 */             (effect.makeChanceResult())) {
/*  592 */             for (Map.Entry z : effect.getMonsterStati().entrySet()) {
/*  593 */               monster.applyStatus(player, new MonsterStatusEffect((MonsterStatus)z.getKey(), (Integer)z.getValue(), theSkill.getId(), null, false), effect.isPoison(), effect.getDuration(), true, effect);
/*      */             }
/*      */           }
/*      */ 
/*  597 */           if (eaterLevel > 0) {
/*  598 */             eaterSkill.getEffect(eaterLevel).applyPassive(player, monster);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  603 */     if (attack.skill != 2301002) {
/*  604 */       effect.applyTo(player);
/*      */     }
/*      */ 
/*  607 */     if ((totDamage > 1) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
/*  608 */       CheatTracker tracker = player.getCheatTracker();
/*  609 */       tracker.setAttacksWithoutHit(true);
/*      */ 
/*  611 */       if (tracker.getAttacksWithoutHit() > 1000)
/*  612 */         tracker.registerOffense(CheatingOffense.ATTACK_WITHOUT_GETTING_HIT, Integer.toString(tracker.getAttacksWithoutHit()));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final double CalculateMaxMagicDamagePerHit(MapleCharacter chr, Skill skill, MapleMonster monster, MapleMonsterStats mobstats, PlayerStats stats, Element elem, Integer sharpEye, double maxDamagePerMonster, MapleStatEffect attackEffect)
/*      */   {
/*  618 */     int dLevel = Math.max(mobstats.getLevel() - chr.getLevel(), 0) * 2;
/*  619 */     int HitRate = Math.min((int)Math.floor(Math.sqrt(stats.getAccuracy())) - (int)Math.floor(Math.sqrt(mobstats.getEva())) + 100, 100);
/*  620 */     if (dLevel > HitRate) {
/*  621 */       HitRate = dLevel;
/*      */     }
/*  623 */     HitRate -= dLevel;
/*  624 */     if ((HitRate <= 0) && ((!GameConstants.isBeginnerJob(skill.getId() / 10000)) || (skill.getId() % 10000 != 1000))) {
/*  625 */       return 0.0D;
/*      */     }
/*      */ 
/*  628 */     int CritPercent = sharpEye.intValue();
/*  629 */     ElementalEffectiveness ee = monster.getEffectiveness(elem);
/*      */     double elemMaxDamagePerMob;
/*  630 */     switch (ee) {
/*      */     case IMMUNE:
/*  632 */       elemMaxDamagePerMob = 1.0D;
/*  633 */       break;
/*      */     default:
/*  635 */       elemMaxDamagePerMob = ElementalStaffAttackBonus(elem, maxDamagePerMonster * ee.getValue(), stats);
/*      */     }
/*      */ 
/*  641 */     int MDRate = monster.getStats().getMDRate();
/*  642 */     MonsterStatusEffect pdr = monster.getBuff(MonsterStatus.MDEF);
/*  643 */     if (pdr != null) {
/*  644 */       MDRate += pdr.getX().intValue();
/*      */     }
/*  646 */     elemMaxDamagePerMob -= elemMaxDamagePerMob * (Math.max(MDRate - stats.ignoreTargetDEF - attackEffect.getIgnoreMob(), 0) / 100.0D);
/*      */ 
/*  648 */     elemMaxDamagePerMob += elemMaxDamagePerMob / 100.0D * CritPercent;
/*      */ 
/*  655 */     elemMaxDamagePerMob *= (monster.getStats().isBoss() ? chr.getStat().bossdam_r : chr.getStat().dam_r) / 100.0D;
/*  656 */     MonsterStatusEffect imprint = monster.getBuff(MonsterStatus.IMPRINT);
/*  657 */     if (imprint != null) {
/*  658 */       elemMaxDamagePerMob += elemMaxDamagePerMob * imprint.getX().intValue() / 100.0D;
/*      */     }
/*  660 */     elemMaxDamagePerMob += elemMaxDamagePerMob * chr.getDamageIncrease(monster.getObjectId()) / 100.0D;
/*  661 */     if (GameConstants.isBeginnerJob(skill.getId() / 10000)) {
/*  662 */       switch (skill.getId() % 10000) {
/*      */       case 1000:
/*  664 */         elemMaxDamagePerMob = 40.0D;
/*  665 */         break;
/*      */       case 1020:
/*  667 */         elemMaxDamagePerMob = 1.0D;
/*  668 */         break;
/*      */       case 1009:
/*  670 */         elemMaxDamagePerMob = monster.getStats().isBoss() ? monster.getMobMaxHp() / 30L * 100L : monster.getMobMaxHp();
/*      */       }
/*      */     }
/*      */ 
/*  674 */     switch (skill.getId()) {
/*      */     case 32001000:
/*      */     case 32101000:
/*      */     case 32111002:
/*      */     case 32121002:
/*  679 */       elemMaxDamagePerMob *= 1.5D;
/*      */     }
/*      */ 
/*  682 */     if (elemMaxDamagePerMob > 999999.0D)
/*  683 */       elemMaxDamagePerMob = 999999.0D;
/*  684 */     else if (elemMaxDamagePerMob <= 0.0D) {
/*  685 */       elemMaxDamagePerMob = 1.0D;
/*      */     }
/*      */ 
/*  688 */     return elemMaxDamagePerMob;
/*      */   }
/*      */ 
/*      */     private static final double ElementalStaffAttackBonus(final Element elem, double elemMaxDamagePerMob, final PlayerStats stats) {
        switch (elem) {
            case FIRE:
                return (elemMaxDamagePerMob / 100) * (stats.element_fire + stats.getElementBoost(elem));
            case ICE:
                return (elemMaxDamagePerMob / 100) * (stats.element_ice + stats.getElementBoost(elem));
            case LIGHTING:
                return (elemMaxDamagePerMob / 100) * (stats.element_light + stats.getElementBoost(elem));
            case POISON:
                return (elemMaxDamagePerMob / 100) * (stats.element_psn + stats.getElementBoost(elem));
            default:
                return (elemMaxDamagePerMob / 100) * (stats.def + stats.getElementBoost(elem));
        }
    }
/*      */ 
  private static void handlePickPocket(final MapleCharacter player, final MapleMonster mob, AttackPair oned) {
        final int maxmeso = player.getBuffedValue(MapleBuffStat.PICKPOCKET).intValue();

        for (final Pair<Integer, Boolean> eachde : oned.attack) {
            final Integer eachd = eachde.left;
            if (player.getStat().pickRate >= 100 || Randomizer.nextInt(99) < player.getStat().pickRate) {
                player.getMap().spawnMesoDrop(Math.min((int) Math.max(((double) eachd / (double) 20000) * (double) maxmeso, (double) 1), maxmeso), new Point((int) (mob.getTruePosition().getX() + Randomizer.nextInt(100) - 50), (int) (mob.getTruePosition().getY())), mob, player, false, (byte) 0);
            }
        }
    }
/*      */   private static double CalculateMaxWeaponDamagePerHit(MapleCharacter player, MapleMonster monster, AttackInfo attack, Skill theSkill, MapleStatEffect attackEffect, double maximumDamageToMonster, Integer CriticalDamagePercent)
/*      */   {
/*  718 */     int dLevel = Math.max(monster.getStats().getLevel() - player.getLevel(), 0) * 2;
/*  719 */     int HitRate = Math.min((int)Math.floor(Math.sqrt(player.getStat().getAccuracy())) - (int)Math.floor(Math.sqrt(monster.getStats().getEva())) + 100, 100);
/*  720 */     if (dLevel > HitRate) {
/*  721 */       HitRate = dLevel;
/*      */     }
/*  723 */     HitRate -= dLevel;
/*  724 */     if ((HitRate <= 0) && ((!GameConstants.isBeginnerJob(attack.skill / 10000)) || (attack.skill % 10000 != 1000)) && (!GameConstants.isPyramidSkill(attack.skill)) && (!GameConstants.isMulungSkill(attack.skill)) && (!GameConstants.isInflationSkill(attack.skill))) {
/*  725 */       return 0.0D;
/*      */     }
/*  727 */     if ((player.getMapId() / 1000000 == 914) || (player.getMapId() / 1000000 == 927)) {
/*  728 */       return 999999.0D;
/*      */     }
/*      */ 
/*  731 */      List<Element> elements = new ArrayList<Element>();
/*  732 */     boolean defined = false;
/*  733 */     int CritPercent = CriticalDamagePercent.intValue();
/*  734 */     int PDRate = monster.getStats().getPDRate();
/*  735 */     MonsterStatusEffect pdr = monster.getBuff(MonsterStatus.WDEF);
/*  736 */     if (pdr != null) {
/*  737 */       PDRate += pdr.getX().intValue();
/*      */     }
/*  739 */     if (theSkill != null) {
/*  740 */       elements.add(theSkill.getElement());
/*  741 */       if (GameConstants.isBeginnerJob(theSkill.getId() / 10000)) {
/*  742 */         switch (theSkill.getId() % 10000) {
/*      */         case 1000:
/*  744 */           maximumDamageToMonster = 40.0D;
/*  745 */           defined = true;
/*  746 */           break;
/*      */         case 1020:
/*  748 */           maximumDamageToMonster = 1.0D;
/*  749 */           defined = true;
/*  750 */           break;
/*      */         case 1009:
/*  752 */           maximumDamageToMonster = monster.getStats().isBoss() ? monster.getMobMaxHp() / 30L * 100L : monster.getMobMaxHp();
/*  753 */           defined = true;
/*      */         }
/*      */       }
/*      */ 
/*  757 */       switch (theSkill.getId()) {
/*      */       case 1311005:
/*  759 */         PDRate = monster.getStats().isBoss() ? PDRate : 0;
/*  760 */         break;
/*      */       case 3221001:
/*      */       case 33101001:
/*  763 */         maximumDamageToMonster *= attackEffect.getMobCount();
/*  764 */         defined = true;
/*  765 */         break;
/*      */       case 3101005:
/*  767 */         defined = true;
/*  768 */         break;
/*      */       case 32001000:
/*      */       case 32101000:
/*      */       case 32111002:
/*      */       case 32121002:
/*  773 */         maximumDamageToMonster *= 1.5D;
/*  774 */         break;
/*      */       case 1221009:
/*      */       case 3221007:
/*      */       case 4331003:
/*      */       case 23121003:
/*  779 */         if (monster.getStats().isBoss()) break;
/*  780 */         maximumDamageToMonster = monster.getMobMaxHp();
/*  781 */         defined = true; break;
/*      */       case 1221011:
/*      */       case 21120006:
/*  786 */         maximumDamageToMonster = monster.getStats().isBoss() ? 500000L : monster.getHp() - 1L;
/*  787 */         defined = true;
/*  788 */         break;
/*      */       case 3211006:
/*  790 */         if (monster.getStatusSourceID(MonsterStatus.FREEZE) != 3211003) break;
/*  791 */         defined = true;
/*  792 */         maximumDamageToMonster = 999999.0D;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  797 */     double elementalMaxDamagePerMonster = maximumDamageToMonster;
/*  798 */     if ((player.getJob() == 311) || (player.getJob() == 312) || (player.getJob() == 321) || (player.getJob() == 322))
/*      */     {
/*  800 */       Skill mortal = SkillFactory.getSkill((player.getJob() == 311) || (player.getJob() == 312) ? 3110001 : 3210001);
/*  801 */       if (player.getTotalSkillLevel(mortal) > 0) {
/*  802 */         MapleStatEffect mort = mortal.getEffect(player.getTotalSkillLevel(mortal));
/*  803 */         if ((mort != null) && (monster.getHPPercent() < mort.getX())) {
/*  804 */           elementalMaxDamagePerMonster = 999999.0D;
/*  805 */           defined = true;
/*  806 */           if (mort.getZ() > 0)
/*  807 */             player.addHP(player.getStat().getMaxHp() * mort.getZ() / 100);
/*      */         }
/*      */       }
/*      */     }
/*  811 */     else if ((player.getJob() == 221) || (player.getJob() == 222))
/*      */     {
/*  813 */       Skill mortal = SkillFactory.getSkill(2210000);
/*  814 */       if (player.getTotalSkillLevel(mortal) > 0) {
/*  815 */         MapleStatEffect mort = mortal.getEffect(player.getTotalSkillLevel(mortal));
/*  816 */         if ((mort != null) && (monster.getHPPercent() < mort.getX())) {
/*  817 */           elementalMaxDamagePerMonster = 999999.0D;
/*  818 */           defined = true;
/*      */         }
/*      */       }
/*      */     }
/*  822 */     if ((!defined) || ((theSkill != null) && ((theSkill.getId() == 33101001) || (theSkill.getId() == 3221001)))) {
/*  823 */       if (player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {
/*  824 */         int chargeSkillId = player.getBuffSource(MapleBuffStat.WK_CHARGE);
/*      */ 
/*  826 */         switch (chargeSkillId) {
/*      */         case 1211003:
/*      */         case 1211004:
/*  829 */           elements.add(Element.FIRE);
/*  830 */           break;
/*      */         case 1211005:
/*      */         case 1211006:
/*      */         case 21111005:
/*  834 */           elements.add(Element.ICE);
/*  835 */           break;
/*      */         case 1211007:
/*      */         case 1211008:
/*      */         case 15101006:
/*  839 */           elements.add(Element.LIGHTING);
/*  840 */           break;
/*      */         case 1221003:
/*      */         case 1221004:
                   case 51111003: // Mihile || Radiant Charge
/*      */         case 11111007:
/*  844 */           elements.add(Element.HOLY);
/*  845 */           break;
/*      */         case 12101005:
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  851 */       if (player.getBuffedValue(MapleBuffStat.LIGHTNING_CHARGE) != null) {
/*  852 */         elements.add(Element.LIGHTING);
/*      */       }
/*  854 */       if (player.getBuffedValue(MapleBuffStat.ELEMENT_RESET) != null)
/*  855 */         elements.clear();
/*      */       double elementalEffect;
/*  857 */       if (elements.size() > 0)
/*      */       {
/*  860 */         switch (attack.skill) {
/*      */         case 3111003:
/*      */         case 3211003:
/*  863 */           elementalEffect = attackEffect.getX() / 100.0D;
/*  864 */           break;
/*      */         default:
/*  866 */           elementalEffect = 0.5D / elements.size();
/*      */         }
/*      */ 
/*  869 */           for (Element element : elements) {
                    switch (monster.getEffectiveness(element)) {
                        case IMMUNE:
                            elementalMaxDamagePerMonster = 1;
                            break;
                        case WEAK:
                            elementalMaxDamagePerMonster *= (1.0 + elementalEffect + player.getStat().getElementBoost(element));
                            break;
                        case STRONG:
                            elementalMaxDamagePerMonster *= (1.0 - elementalEffect - player.getStat().getElementBoost(element));
                            break;
                    }
                }
            }
            
/*      */ 
/*  884 */       elementalMaxDamagePerMonster -= elementalMaxDamagePerMonster * (Math.max(PDRate - Math.max(player.getStat().ignoreTargetDEF, 0) - Math.max(attackEffect == null ? 0 : attackEffect.getIgnoreMob(), 0), 0) / 100.0D);
/*      */ 
/*  887 */       elementalMaxDamagePerMonster += elementalMaxDamagePerMonster / 100.0D * CritPercent;
/*      */ 
/*  896 */       MonsterStatusEffect imprint = monster.getBuff(MonsterStatus.IMPRINT);
/*  897 */       if (imprint != null) {
/*  898 */         elementalMaxDamagePerMonster += elementalMaxDamagePerMonster * imprint.getX().intValue() / 100.0D;
/*      */       }
/*      */ 
/*  901 */       elementalMaxDamagePerMonster += elementalMaxDamagePerMonster * player.getDamageIncrease(monster.getObjectId()) / 100.0D;
/*  902 */       elementalMaxDamagePerMonster *= ((monster.getStats().isBoss()) && (attackEffect != null) ? player.getStat().bossdam_r + attackEffect.getBossDamage() : player.getStat().dam_r) / 100.0D;
/*      */     }
/*  904 */     if (elementalMaxDamagePerMonster > 999999.0D) {
/*  905 */       if (!defined)
/*  906 */         elementalMaxDamagePerMonster = 999999.0D;
/*      */     }
/*  908 */     else if (elementalMaxDamagePerMonster <= 0.0D) {
/*  909 */       elementalMaxDamagePerMonster = 1.0D;
/*      */     }
/*  911 */     return elementalMaxDamagePerMonster;
/*      */   }
/*      */ 
/*      */    public static final AttackInfo DivideAttack(final AttackInfo attack, final int rate) {
        attack.real = false;
        if (rate <= 1) {
            return attack; //lol
        }
        for (AttackPair p : attack.allDamage) {
            if (p.attack != null) {
                for (Pair<Integer, Boolean> eachd : p.attack) {
                    eachd.left /= rate; //too ex.
                }
            }
        }
        return attack;
    }
/*      */ 
/*      */   public static final AttackInfo Modify_AttackCrit(AttackInfo attack, MapleCharacter chr, int type, MapleStatEffect effect)
/*      */   {
/*      */     int CriticalRate;
/*      */     boolean shadow;
/*      */     List damages;
/*      */     List damage;
/*  930 */     if ((attack.skill != 4211006) && (attack.skill != 3211003) && (attack.skill != 4111004)) {
/*  931 */       CriticalRate = chr.getStat().passive_sharpeye_rate() + (effect == null ? 0 : effect.getCr());
/*  932 */       shadow = (chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null) && ((type == 1) || (type == 2));
/*  933 */       damages = new ArrayList(); damage = new ArrayList();
/*      */ 
/*  935 */       for (AttackPair p : attack.allDamage) {
/*  936 */         if (p.attack != null) {
/*  937 */           int hit = 0;
/*  938 */           int mid_att = shadow ? p.attack.size() / 2 : p.attack.size();
/*      */ 
/*  940 */           int toCrit = (attack.skill == 4221001) || (attack.skill == 3221007) || (attack.skill == 23121003) || (attack.skill == 4341005) || (attack.skill == 4331006) || (attack.skill == 21120005) ? mid_att : 0;
/*  941 */           if (toCrit == 0) {
/*  942 */             for (Pair eachd : p.attack) {
/*  943 */               if ((!((Boolean)eachd.right).booleanValue()) && (hit < mid_att)) {
/*  944 */                 if ((((Integer)eachd.left).intValue() > 999999) || (Randomizer.nextInt(100) < CriticalRate)) {
/*  945 */                   toCrit++;
/*      */                 }
/*  947 */                 damage.add(eachd.left);
/*      */               }
/*  949 */               hit++;
/*      */             }
/*  951 */             if (toCrit == 0) {
/*  952 */               damage.clear();
/*  953 */               continue;
/*      */             }
/*  955 */             Collections.sort(damage);
/*  956 */             for (int i = damage.size(); i > damage.size() - toCrit; i--) {
/*  957 */               damages.add(damage.get(i - 1));
/*      */             }
/*  959 */             damage.clear();
/*      */           }
/*  961 */           hit = 0;
/*  962 */           for (Pair eachd : p.attack) {
/*  963 */             if (!((Boolean)eachd.right).booleanValue()) {
/*  964 */               if (attack.skill == 4221001)
/*  965 */                 eachd.right = Boolean.valueOf(hit == 3);
/*  966 */               else if ((attack.skill == 3221007) || (attack.skill == 23121003) || (attack.skill == 21120005) || (attack.skill == 4341005) || (attack.skill == 4331006) || (((Integer)eachd.left).intValue() > 999999))
/*  967 */                 eachd.right = Boolean.valueOf(true);
/*  968 */               else if (hit >= mid_att) {
/*  969 */                 eachd.right = ((Pair)p.attack.get(hit - mid_att)).right;
/*      */               }
/*      */               else {
/*  972 */                 eachd.right = Boolean.valueOf(damages.contains(eachd.left));
/*      */               }
/*      */             }
/*  975 */             hit++;
/*      */           }
/*  977 */           damages.clear();
/*      */         }
/*      */       }
/*      */     }
/*  981 */     return attack;
/*      */   }
/*      */ 
/*      */   public static final AttackInfo parseDmgMa(LittleEndianAccessor lea, MapleCharacter chr)
/*      */   {
/*      */     try {
/*  987 */       AttackInfo ret = new AttackInfo();
/*      */ 
/*  995 */       lea.skip(1);
/*  996 */       ret.tbyte = lea.readByte();
/*      */ 
/*  998 */       ret.targets = (byte)(ret.tbyte >>> 4 & 0xF);
/*  999 */       ret.hits = (byte)(ret.tbyte & 0xF);
/* 1000 */       ret.skill = lea.readInt();
/* 1001 */       if (ret.skill >= 91000000) {
/* 1002 */         return null;
/*      */       }
/* 1004 */       lea.skip(GameConstants.GMS ? 9 : 17);
/* 1005 */       if (GameConstants.isMagicChargeSkill(ret.skill))
/* 1006 */         ret.charge = lea.readInt();
/*      */       else {
/* 1008 */         ret.charge = -1;
/*      */       }
/* 1010 */       ret.unk = lea.readByte();
/* 1011 */       ret.display = lea.readUShort();
/*      */ 
/* 1017 */       lea.skip(4);
/* 1018 */       lea.skip(1);
/* 1019 */       ret.speed = lea.readByte();
/* 1020 */       ret.lastAttackTickCount = lea.readInt();
/* 1021 */       lea.skip(4);
/*      */ 
/* 1032 */       ret.allDamage = new ArrayList();
/*      */ 
/* 1034 */       for (int i = 0; i < ret.targets; i++) {
/* 1035 */         int oid = lea.readInt();
/*      */ 
/* 1042 */         lea.skip(18);
/*      */ 
/* 1044 */         List allDamageNumbers = new ArrayList();
/*      */ 
/* 1046 */         for (int j = 0; j < ret.hits; j++) {
/* 1047 */           int damage = lea.readInt();
/* 1048 */           allDamageNumbers.add(new Pair(Integer.valueOf(damage), Boolean.valueOf(false)));
/*      */         }
/*      */ 
/* 1051 */         lea.skip(4);
/* 1052 */         ret.allDamage.add(new AttackPair(Integer.valueOf(oid).intValue(), allDamageNumbers));
/*      */       }
/* 1054 */       if (lea.available() >= 4L) {
/* 1055 */         ret.position = lea.readPos();
/*      */       }
/* 1057 */       return ret;
/*      */     } catch (Exception e) {
/* 1059 */       e.printStackTrace();
/* 1060 */     }return null;
/*      */   }
/*      */ 
/*      */   public static final AttackInfo parseDmgM(LittleEndianAccessor lea, MapleCharacter chr)
/*      */   {
/* 1066 */     AttackInfo ret = new AttackInfo();
/* 1067 */     lea.skip(1);
/* 1068 */     ret.tbyte = lea.readByte();
/*      */ 
/* 1070 */     ret.targets = (byte)(ret.tbyte >>> 4 & 0xF);
/* 1071 */     ret.hits = (byte)(ret.tbyte & 0xF);
/* 1072 */     ret.skill = lea.readInt();
/* 1073 */     if (ret.skill >= 91000000) {
/* 1074 */       return null;
/*      */     }
/* 1076 */     lea.skip(9);
  switch (ret.skill) {
      case 11101007: // Power Reflection
      case 11101006: // Dawn Warrior - Power Reflection
      case 21101003: // body pressure
       case 2111007:// tele mastery skills
            case 2211007:
            case 12111007:
            case 22161005:
            case 32111010:    
            case 2311007: // bishop tele mastery
                lea.skip(1); // charge = 0
                ret.charge = 0;
                ret.display = lea.readUShort();
                lea.skip(4);// dunno
                ret.speed = (byte)lea.readShort();
                ret.lastAttackTickCount = lea.readInt();
                lea.skip(4);// looks like zeroes
                ret.allDamage = new ArrayList();
                for (int i = 0; i < ret.targets; i++) {
                    int oid = lea.readInt();
                    lea.skip(18);
                    List allDamageNumbers = new ArrayList();
                        for (int j = 0; j < ret.hits; j++) {
                            int damage = lea.readInt();
                            allDamageNumbers.add(new Pair(Integer.valueOf(damage), Boolean.valueOf(false)));
                        }
                    lea.skip(4);
                    ret.allDamage.add(new AttackPair(Integer.valueOf(oid).intValue(), allDamageNumbers));
                }
                ret.position = lea.readPos();
                return ret;
                
       case 24121000:// mille
           // case 24121005://tempest
            //case 5101004: // Corkscrew
            //case 15101003: // Cygnus corkscrew

            case 5201002: // Gernard
            case 14111006: // Poison bomb
            case 4341002:
            case 4341003:
            case 5301001:
            case 5300007:
            case 31001000: // grim scythe
            case 31101000: // soul eater
            case 31111005: // carrion breath
 

                ret.charge = lea.readInt();
                break;
            default:
                ret.charge = 0;
                break;
        }
/*      */ 
/* 1096 */     ret.unk = lea.readByte();
/* 1097 */     ret.display = lea.readUShort();
/* 1098 */     lea.skip(4);
/* 1099 */     lea.skip(1);
/* 1100 */     if ((ret.skill == 5300007) || (ret.skill == 5101012) || (ret.skill == 5081001) || (ret.skill == 15101010)) {
/* 1101 */       lea.readInt();
/*      */     }
               if(ret.skill == 24121005){
                 lea.readInt();
               }
/* 1103 */     ret.speed = lea.readByte();
/* 1104 */     ret.lastAttackTickCount = lea.readInt();
/* 1105 */     lea.skip(8);
/*      */ 
/* 1107 */     ret.allDamage = new ArrayList();
/*      */ 
/* 1109 */     if (ret.skill == 4211006) {
/* 1110 */       return parseMesoExplosion(lea, ret, chr);
/*      */     }
/*      */ 
/* 1114 */     /*if (ret.skill == 24121000) {
/* 1115 */      // lea.readInt();
/*      */    // }
/* 1117 */     for (int i = 0; i < ret.targets; i++) {
/* 1118 */       int oid = lea.readInt();
/*      */ 
/* 1125 */       lea.skip(18);
/*      */ 
/* 1127 */       List allDamageNumbers = new ArrayList();
/*      */ 
/* 1129 */       for (int j = 0; j < ret.hits; j++) {
/* 1130 */         int damage = lea.readInt();
/*      */ 
/* 1132 */         allDamageNumbers.add(new Pair(Integer.valueOf(damage), Boolean.valueOf(false)));
/*      */       }
/* 1134 */       lea.skip(4);
/* 1135 */       ret.allDamage.add(new AttackPair(Integer.valueOf(oid).intValue(), allDamageNumbers));
/*      */     }
/* 1137 */     ret.position = lea.readPos();
/* 1138 */     return ret;
/*      */   }
/*      */ 
/*      */   public static final AttackInfo parseDmgR(LittleEndianAccessor lea, MapleCharacter chr)
/*      */   {
/* 1143 */     AttackInfo ret = new AttackInfo();
/* 1144 */     lea.skip(1);
/* 1145 */     ret.tbyte = lea.readByte();
/*      */ 
/* 1147 */     ret.targets = (byte)(ret.tbyte >>> 4 & 0xF);
/* 1148 */     ret.hits = (byte)(ret.tbyte & 0xF);
/* 1149 */     ret.skill = lea.readInt();
/* 1150 */     if (ret.skill >= 91000000) {
/* 1151 */       return null;
/*      */     }
/* 1153 */     lea.skip(10);
  switch (ret.skill) {
            case 3121004: // Hurricane
            case 3221001: // Pierce
            case 5221004: // Rapidfire
            case 5721001: // Rapidfire
            case 13111002: // Cygnus Hurricane
            case 33121009:
            case 35001001:
            case 5711002:
            case 35101009:
            case 23121000:
            case 5311002:
            case 24121000:
                lea.skip(4); // extra 4 bytes
                break;
        }
/*      */ 
/* 1167 */     ret.charge = -1;
/* 1168 */     ret.unk = lea.readByte();
/* 1169 */     ret.display = lea.readUShort();
/* 1170 */     lea.skip(4);
/* 1171 */     lea.skip(1);
/* 1172 */     if (ret.skill == 23111001) {
/* 1173 */       lea.skip(4);
/* 1174 */       lea.skip(4);
/*      */ 
/* 1176 */       lea.skip(4);
/*      */     }
/* 1178 */     ret.speed = lea.readByte();
/* 1179 */     ret.lastAttackTickCount = lea.readInt();
/* 1180 */     lea.skip(4);
/* 1181 */     ret.slot = (byte)lea.readShort();
/* 1182 */     ret.csstar = (byte)lea.readShort();
/* 1183 */     ret.AOE = lea.readByte();
/*      */ 
/* 1187 */     ret.allDamage = new ArrayList();
/*      */ 
/* 1189 */     for (int i = 0; i < ret.targets; i++) {
/* 1190 */       int oid = lea.readInt();
/*      */ 
/* 1197 */       lea.skip(18);
/*      */ 
/* 1199 */       List allDamageNumbers = new ArrayList();
/* 1200 */       for (int j = 0; j < ret.hits; j++) {
/* 1201 */         int damage = lea.readInt();
/* 1202 */         allDamageNumbers.add(new Pair(Integer.valueOf(damage), Boolean.valueOf(false)));
/*      */       }
/*      */ 
/* 1205 */       lea.skip(4);
/*      */ 
/* 1208 */       ret.allDamage.add(new AttackPair(Integer.valueOf(oid).intValue(), allDamageNumbers));
/*      */     }
/* 1210 */     lea.skip(4);
/* 1211 */     ret.position = lea.readPos();
/*      */ 
/* 1213 */     return ret;
/*      */   }
/*      */ 
/*      */   public static final AttackInfo parseMesoExplosion(LittleEndianAccessor lea, AttackInfo ret, MapleCharacter chr)
/*      */   {
/* 1219 */     if (ret.hits == 0) {
/* 1220 */       lea.skip(4);
/* 1221 */       byte bullets = lea.readByte();
/* 1222 */       for (int j = 0; j < bullets; j++) {
/* 1223 */         ret.allDamage.add(new AttackPair(Integer.valueOf(lea.readInt()).intValue(), null));
/* 1224 */         lea.skip(1);
/*      */       }
/* 1226 */       lea.skip(2);
/* 1227 */       return ret;
/*      */     }
/*      */ 
/* 1232 */     for (int i = 0; i < ret.targets; i++) {
/* 1233 */       int oid = lea.readInt();
/*      */ 
/* 1240 */       lea.skip(12);
/* 1241 */       byte bullets = lea.readByte();
/* 1242 */       List allDamageNumbers = new ArrayList();
/* 1243 */       for (int j = 0; j < bullets; j++) {
/* 1244 */         allDamageNumbers.add(new Pair(Integer.valueOf(lea.readInt()), Boolean.valueOf(false)));
/*      */       }
/* 1246 */       ret.allDamage.add(new AttackPair(Integer.valueOf(oid).intValue(), allDamageNumbers));
/* 1247 */       lea.skip(4);
/*      */     }
/* 1249 */     lea.skip(4);
/* 1250 */     byte bullets = lea.readByte();
/*      */ 
/* 1252 */     for (int j = 0; j < bullets; j++) {
/* 1253 */       ret.allDamage.add(new AttackPair(Integer.valueOf(lea.readInt()).intValue(), null));
/* 1254 */       lea.skip(2);
/*      */     }
/*      */ 
/* 1258 */     return ret;
/*      */   }
/*      */ }

/* Location:           C:\Users\Sjogren\Desktop\lithium.jar
 * Qualified Name:     handling.channel.handler.DamageParse
 * JD-Core Version:    0.6.0
 */