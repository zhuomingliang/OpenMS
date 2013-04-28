/*      */ package tools.packet;
/*      */ 
/*      */ import client.MapleBuffStat;
/*      */ import client.MapleCharacter;
/*      */ import client.MapleClient;
/*      */ import client.MapleKeyLayout;
/*      */ import client.MapleQuestStatus;
/*      */ import client.MonsterFamiliar;
/*      */ import client.PlayerRandomStream;
/*      */ import client.PlayerStats;
/*      */ import client.Skill;
/*      */ import client.SkillMacro;
import client.inventory.Equip;
/*      */ import client.inventory.Equip.ScrollResult;
/*      */ import client.inventory.Item;
/*      */ import client.inventory.MapleAndroid;
/*      */ import client.inventory.MapleInventory;
/*      */ import client.inventory.MapleInventoryType;
/*      */ import client.inventory.MapleMount;
/*      */ import client.inventory.MapleRing;
/*      */ import constants.GameConstants;
/*      */ import handling.SendPacketOpcode;
import handling.channel.handler.PlayerInteractionHandler;
/*      */ import handling.channel.handler.PlayerInteractionHandler.Interaction;
import handling.world.World;
/*      */ import handling.world.World.Guild;
/*      */ import handling.world.guild.MapleGuild;
/*      */ import java.awt.Point;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import scripting.EventInstanceManager;
/*      */ import server.MapleCarnivalParty;
/*      */ import server.MapleDueyActions;
/*      */ import server.MapleShop;
/*      */ import server.MapleStatEffect;
/*      */ import server.MapleTrade;
/*      */ import server.Randomizer;
import server.events.MapleSnowball;
/*      */ import server.events.MapleSnowball.MapleSnowballs;
/*      */ import server.life.MapleNPC;
/*      */ import server.life.MobSkill;
/*      */ import server.maps.MapleDragon;
/*      */ import server.maps.MapleFoothold;
/*      */ import server.maps.MapleFootholdTree;
/*      */ import server.maps.MapleMap;
/*      */ import server.maps.MapleMapItem;
/*      */ import server.maps.MapleMist;
import server.maps.MapleNodes;
/*      */ import server.maps.MapleNodes.MaplePlatform;
/*      */ import server.maps.MapleReactor;
/*      */ import server.maps.MapleSummon;
/*      */ import server.maps.MechDoor;
/*      */ import server.maps.SummonMovementType;
/*      */ import server.movement.LifeMovementFragment;
/*      */ import server.quest.MapleQuest;
/*      */ import tools.AttackPair;
/*      */ import tools.HexTool;
/*      */ import tools.Pair;
/*      */ import tools.Triple;
/*      */ import tools.data.MaplePacketLittleEndianWriter;
/*      */ 
/*      */ public class CField
/*      */ {
/*   70 */   public static int DEFAULT_BUFFMASK = 0;
/*   71 */   public static byte[] NEXON_IP = { 8, 31, 98, 53 };
/*      */ 
/*      */   public static byte[] getPacketFromHexString(String hex)
/*      */   {
/*   84 */     return HexTool.getByteArrayFromHexString(hex);
/*      */   }
/*      */ 
/*      */   public static byte[] getServerIP(MapleClient c, int port, int clientId) {
/*   88 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*   90 */     mplew.writeShort(SendPacketOpcode.SERVER_IP.getValue());
/*   91 */     mplew.writeShort(0);
/*      */ 
/*   99 */     mplew.write(NEXON_IP);
/*  100 */     mplew.writeShort(port);
/*  101 */     mplew.writeInt(clientId);
/*  102 */     mplew.writeInt(0);
/*  103 */     mplew.writeShort(0);
/*  104 */     mplew.writeInt(-1);
/*      */ 
/*  106 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getChannelChange(MapleClient c, int port) {
/*  110 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  112 */     mplew.writeShort(SendPacketOpcode.CHANGE_CHANNEL.getValue());
/*  113 */     mplew.write(1);
/*      */ 
/*  121 */     mplew.write(NEXON_IP);
/*  122 */     mplew.writeShort(port);
/*  123 */     mplew.write(0);
/*      */ 
/*  125 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPType(int type, List<Pair<Integer, String>> players1, int team, boolean enabled, int lvl)
/*      */   {
/* 1082 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1084 */     mplew.writeShort(SendPacketOpcode.PVP_TYPE.getValue());
/* 1085 */     mplew.write(type);
/* 1086 */     mplew.write(lvl);
/* 1087 */     mplew.write(enabled ? 1 : 0);
/* 1088 */     mplew.write(0);
/* 1089 */     if (type > 0) {
/* 1090 */       mplew.write(team);
/* 1091 */       mplew.writeInt(players1.size());
/* 1092 */       for (Pair pl : players1) {
/* 1093 */         mplew.writeInt(((Integer)pl.left).intValue());
/* 1094 */         mplew.writeMapleAsciiString((String)pl.right);
/* 1095 */         mplew.writeShort(2660);
/*      */       }
/*      */     }
/*      */ 
/* 1099 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPTransform(int type) {
/* 1103 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1105 */     mplew.writeShort(SendPacketOpcode.PVP_TRANSFORM.getValue());
/* 1106 */     mplew.write(type);
/*      */ 
/* 1108 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPDetails(List<Pair<Integer, Integer>> players) {
/* 1112 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1114 */     mplew.writeShort(SendPacketOpcode.PVP_DETAILS.getValue());
/* 1115 */     mplew.write(1);
/* 1116 */     mplew.write(0);
/* 1117 */     mplew.writeInt(players.size());
/* 1118 */     for (Pair pl : players) {
/* 1119 */       mplew.writeInt(((Integer)pl.left).intValue());
/* 1120 */       mplew.write(((Integer)pl.right).intValue());
/*      */     }
/*      */ 
/* 1123 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] enablePVP(boolean enabled) {
/* 1127 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1129 */     mplew.writeShort(SendPacketOpcode.PVP_ENABLED.getValue());
/* 1130 */     mplew.write(enabled ? 1 : 2);
/*      */ 
/* 1132 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPScore(int score, boolean kill) {
/* 1136 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1138 */     mplew.writeShort(SendPacketOpcode.PVP_SCORE.getValue());
/* 1139 */     mplew.writeInt(score);
/* 1140 */     mplew.write(kill ? 1 : 0);
/*      */ 
/* 1142 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPResult(List<Pair<Integer, MapleCharacter>> flags, int exp, int winningTeam, int playerTeam) {
/* 1146 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1148 */     mplew.writeShort(SendPacketOpcode.PVP_RESULT.getValue());
/* 1149 */     mplew.writeInt(flags.size());
/* 1150 */     for (Pair f : flags) {
/* 1151 */       mplew.writeInt(((MapleCharacter)f.right).getId());
/* 1152 */       mplew.writeMapleAsciiString(((MapleCharacter)f.right).getName());
/* 1153 */       mplew.writeInt(((Integer)f.left).intValue());
/* 1154 */       mplew.writeShort(((MapleCharacter)f.right).getTeam() + 1);
/* 1155 */       mplew.writeInt(0);
/* 1156 */       mplew.writeInt(0);
/*      */     }
/* 1158 */     mplew.writeZeroBytes(24);
/* 1159 */     mplew.writeInt(exp);
/* 1160 */     mplew.write(0);
/* 1161 */     mplew.writeShort(100);
/* 1162 */     mplew.writeInt(0);
/* 1163 */     mplew.writeInt(0);
/* 1164 */     mplew.write(winningTeam);
/* 1165 */     mplew.write(playerTeam);
/*      */ 
/* 1167 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPTeam(List<Pair<Integer, String>> players) {
/* 1171 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1173 */     mplew.writeShort(SendPacketOpcode.PVP_TEAM.getValue());
/* 1174 */     mplew.writeInt(players.size());
/* 1175 */     for (Pair pl : players) {
/* 1176 */       mplew.writeInt(((Integer)pl.left).intValue());
/* 1177 */       mplew.writeMapleAsciiString((String)pl.right);
/* 1178 */       mplew.writeShort(2660);
/*      */     }
/*      */ 
/* 1181 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPScoreboard(List<Pair<Integer, MapleCharacter>> flags, int type) {
/* 1185 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1187 */     mplew.writeShort(SendPacketOpcode.PVP_SCOREBOARD.getValue());
/* 1188 */     mplew.writeShort(flags.size());
/* 1189 */     for (Pair f : flags) {
/* 1190 */       mplew.writeInt(((MapleCharacter)f.right).getId());
/* 1191 */       mplew.writeMapleAsciiString(((MapleCharacter)f.right).getName());
/* 1192 */       mplew.writeInt(((Integer)f.left).intValue());
/* 1193 */       mplew.write(type == 0 ? 0 : ((MapleCharacter)f.right).getTeam() + 1);
/*      */     }
/*      */ 
/* 1196 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPPoints(int p1, int p2) {
/* 1200 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1202 */     mplew.writeShort(SendPacketOpcode.PVP_POINTS.getValue());
/* 1203 */     mplew.writeInt(p1);
/* 1204 */     mplew.writeInt(p2);
/*      */ 
/* 1206 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPKilled(String lastWords) {
/* 1210 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1212 */     mplew.writeShort(SendPacketOpcode.PVP_KILLED.getValue());
/* 1213 */     mplew.writeMapleAsciiString(lastWords);
/*      */ 
/* 1215 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPMode(int mode) {
/* 1219 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1221 */     mplew.writeShort(SendPacketOpcode.PVP_MODE.getValue());
/* 1222 */     mplew.write(mode);
/*      */ 
/* 1224 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPIceHPBar(int hp, int maxHp) {
/* 1228 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1230 */     mplew.writeShort(SendPacketOpcode.PVP_ICEKNIGHT.getValue());
/* 1231 */     mplew.writeInt(hp);
/* 1232 */     mplew.writeInt(maxHp);
/*      */ 
/* 1234 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getCaptureFlags(MapleMap map) {
/* 1238 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1240 */     mplew.writeShort(SendPacketOpcode.CAPTURE_FLAGS.getValue());
/* 1241 */     mplew.writeRect(map.getArea(0));
/* 1242 */     mplew.writeInt(((Point)((Pair)map.getGuardians().get(0)).left).x);
/* 1243 */     mplew.writeInt(((Point)((Pair)map.getGuardians().get(0)).left).y);
/* 1244 */     mplew.writeRect(map.getArea(1));
/* 1245 */     mplew.writeInt(((Point)((Pair)map.getGuardians().get(1)).left).x);
/* 1246 */     mplew.writeInt(((Point)((Pair)map.getGuardians().get(1)).left).y);
/*      */ 
/* 1248 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getCapturePosition(MapleMap map) {
/* 1252 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1254 */     Point p1 = map.getPointOfItem(2910000);
/* 1255 */     Point p2 = map.getPointOfItem(2910001);
/* 1256 */     mplew.writeShort(SendPacketOpcode.CAPTURE_POSITION.getValue());
/* 1257 */     mplew.write(p1 == null ? 0 : 1);
/* 1258 */     if (p1 != null) {
/* 1259 */       mplew.writeInt(p1.x);
/* 1260 */       mplew.writeInt(p1.y);
/*      */     }
/* 1262 */     mplew.write(p2 == null ? 0 : 1);
/* 1263 */     if (p2 != null) {
/* 1264 */       mplew.writeInt(p2.x);
/* 1265 */       mplew.writeInt(p2.y);
/*      */     }
/*      */ 
/* 1268 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] resetCapture() {
/* 1272 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1274 */     mplew.writeShort(SendPacketOpcode.CAPTURE_RESET.getValue());
/*      */ 
/* 1276 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getMacros(SkillMacro[] macros)
/*      */   {
/* 1378 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1380 */     mplew.writeShort(SendPacketOpcode.SKILL_MACRO.getValue());
/* 1381 */     int count = 0;
/* 1382 */     for (int i = 0; i < 5; i++) {
/* 1383 */       if (macros[i] != null) {
/* 1384 */         count++;
/*      */       }
/*      */     }
/* 1387 */     mplew.write(count);
/* 1388 */     for (int i = 0; i < 5; i++) {
/* 1389 */       SkillMacro macro = macros[i];
/* 1390 */       if (macro != null) {
/* 1391 */         mplew.writeMapleAsciiString(macro.getName());
/* 1392 */         mplew.write(macro.getShout());
/* 1393 */         mplew.writeInt(macro.getSkill1());
/* 1394 */         mplew.writeInt(macro.getSkill2());
/* 1395 */         mplew.writeInt(macro.getSkill3());
/*      */       }
/*      */     }
/*      */ 
/* 1399 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getCharInfo(MapleCharacter chr) {
/* 1403 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1414 */     mplew.writeShort(SendPacketOpcode.WARP_TO_MAP.getValue());
/* 1415 */     mplew.writeShort(2);
/* 1416 */     mplew.writeLong(1L);
/* 1417 */     mplew.writeLong(2L);
/* 1418 */     mplew.writeLong(chr.getClient().getChannel() - 1);
/* 1419 */     mplew.write(0);
/* 1420 */     mplew.write(1);
/* 1421 */     mplew.writeInt(0);
/* 1422 */     mplew.write(1);
/* 1423 */     mplew.writeShort(0);
/* 1424 */     chr.CRand().connectData(mplew);
/* 1425 */     PacketHelper.addCharacterInfo(mplew, chr);
/*      */ 
/* 1434 */     mplew.writeZeroBytes(16);
/* 1435 */     mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
/* 1436 */     mplew.writeInt(100);
/* 1437 */     mplew.write(0);
/* 1438 */     mplew.write(1);
/*      */ 
/* 1440 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */    public static byte[] getWarpToMap(final MapleMap to, final int spawnPoint, final MapleCharacter chr) {

        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.WARP_TO_MAP.getValue());

        mplew.writeShort(2);
        mplew.writeLong(1);
        mplew.writeLong(2);
        mplew.writeLong(chr.getClient().getChannel() - 1);
        mplew.write(0); // todo jump
        mplew.write(2);
        mplew.write(new byte[8]);//v93
        mplew.writeInt(to.getId());
        mplew.write(spawnPoint);
        mplew.writeInt(chr.getStat().getHp()); //bb - int
        mplew.write(0);//?
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeInt(100);
        mplew.writeShort(0);//v93

        return mplew.getPacket();
    }
/*      */ 
/*      */   public static byte[] spawnFlags(List<Pair<String, Integer>> flags) {
/* 1481 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1483 */     mplew.writeShort(SendPacketOpcode.LOGIN_WELCOME.getValue());
/* 1484 */     mplew.write(flags == null ? 0 : flags.size());
/* 1485 */     if (flags != null) {
/* 1486 */       for (Pair f : flags) {
/* 1487 */         mplew.writeMapleAsciiString((String)f.left);
/* 1488 */         mplew.write(((Integer)f.right).intValue());
/*      */       }
/*      */     }
/*      */ 
/* 1492 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] serverBlocked(int type)
/*      */   {
/* 1504 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1506 */     mplew.writeShort(SendPacketOpcode.SERVER_BLOCKED.getValue());
/* 1507 */     mplew.write(type);
/*      */ 
/* 1509 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] pvpBlocked(int type)
/*      */   {
/* 1514 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1517 */     mplew.write(type);
/*      */ 
/* 1519 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showEquipEffect() {
/* 1523 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1525 */     mplew.writeShort(SendPacketOpcode.SHOW_EQUIP_EFFECT.getValue());
/*      */ 
/* 1527 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showEquipEffect(int team) {
/* 1531 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1533 */     mplew.writeShort(SendPacketOpcode.SHOW_EQUIP_EFFECT.getValue());
/* 1534 */     mplew.writeShort(team);
/*      */ 
/* 1536 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] multiChat(String name, String chattext, int mode) {
/* 1540 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1542 */     mplew.writeShort(SendPacketOpcode.MULTICHAT.getValue());
/* 1543 */     mplew.write(mode);
/* 1544 */     mplew.writeMapleAsciiString(name);
/* 1545 */     mplew.writeMapleAsciiString(chattext);
/*      */ 
/* 1547 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getFindReplyWithCS(String target, boolean buddy) {
/* 1551 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1553 */     mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
/* 1554 */     mplew.write(buddy ? 72 : 9);
/* 1555 */     mplew.writeMapleAsciiString(target);
/* 1556 */     mplew.write(2);
/* 1557 */     mplew.writeInt(-1);
/*      */ 
/* 1559 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getFindReplyWithMTS(String target, boolean buddy) {
/* 1563 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1565 */     mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
/* 1566 */     mplew.write(buddy ? 72 : 9);
/* 1567 */     mplew.writeMapleAsciiString(target);
/* 1568 */     mplew.write(0);
/* 1569 */     mplew.writeInt(-1);
/*      */ 
/* 1571 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getWhisper(String sender, int channel, String text) {
/* 1575 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1577 */     mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
/* 1578 */     mplew.write(18);
/* 1579 */     mplew.writeMapleAsciiString(sender);
/* 1580 */     mplew.writeShort(channel - 1);
/* 1581 */     mplew.writeMapleAsciiString(text);
/*      */ 
/* 1583 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getWhisperReply(String target, byte reply) {
/* 1587 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1589 */     mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
/* 1590 */     mplew.write(10);
/* 1591 */     mplew.writeMapleAsciiString(target);
/* 1592 */     mplew.write(reply);
/*      */ 
/* 1594 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getFindReplyWithMap(String target, int mapid, boolean buddy) {
/* 1598 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1600 */     mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
/* 1601 */     mplew.write(buddy ? 72 : 9);
/* 1602 */     mplew.writeMapleAsciiString(target);
/* 1603 */     mplew.write(1);
/* 1604 */     mplew.writeInt(mapid);
/* 1605 */     mplew.writeZeroBytes(8);
/*      */ 
/* 1607 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getFindReply(String target, int channel, boolean buddy) {
/* 1611 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1613 */     mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
/* 1614 */     mplew.write(buddy ? 72 : 9);
/* 1615 */     mplew.writeMapleAsciiString(target);
/* 1616 */     mplew.write(3);
/* 1617 */     mplew.writeInt(channel - 1);
/*      */ 
/* 1619 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static final byte[] MapEff(String path) {
/* 1623 */     return environmentChange(path, 3);
/*      */   }
/*      */ 
/*      */   public static final byte[] MapNameDisplay(int mapid) {
/* 1627 */     return environmentChange("maplemap/enter/" + mapid, 3);
/*      */   }
/*      */ 
/*      */   public static final byte[] Aran_Start() {
/* 1631 */     return environmentChange("Aran/balloon", 4);
/*      */   }
/*      */ 
/*      */   public static byte[] musicChange(String song) {
/* 1635 */     return environmentChange(song, 6);
/*      */   }
/*      */ 
/*      */   public static byte[] showEffect(String effect) {
/* 1639 */     return environmentChange(effect, 3);
/*      */   }
/*      */ 
/*      */   public static byte[] playSound(String sound) {
/* 1643 */     return environmentChange(sound, 4);
/*      */   }
/*      */ 
/*      */   public static byte[] environmentChange(String env, int mode) {
/* 1647 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1649 */     mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
/* 1650 */     mplew.write(mode);
/* 1651 */     mplew.writeMapleAsciiString(env);
/*      */ 
/* 1653 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] trembleEffect(int type, int delay) {
/* 1657 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1659 */     mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
/* 1660 */     mplew.write(1);
/* 1661 */     mplew.write(type);
/* 1662 */     mplew.writeInt(delay);
/*      */ 
/* 1664 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] environmentMove(String env, int mode) {
/* 1668 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1670 */     mplew.writeShort(SendPacketOpcode.MOVE_ENV.getValue());
/* 1671 */     mplew.writeMapleAsciiString(env);
/* 1672 */     mplew.writeInt(mode);
/*      */ 
/* 1674 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getUpdateEnvironment(MapleMap map) {
/* 1678 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1680 */     mplew.writeShort(SendPacketOpcode.UPDATE_ENV.getValue());
/* 1681 */     mplew.writeInt(map.getEnvironment().size());
/* 1682 */     for (Map.Entry mp : map.getEnvironment().entrySet()) {
/* 1683 */       mplew.writeMapleAsciiString((String)mp.getKey());
/* 1684 */       mplew.writeInt(((Integer)mp.getValue()).intValue());
/*      */     }
/*      */ 
/* 1687 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] startMapEffect(String msg, int itemid, boolean active) {
/* 1691 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1693 */     mplew.writeShort(SendPacketOpcode.MAP_EFFECT.getValue());
/* 1694 */     mplew.write(active ? 0 : 1);
/*      */ 
/* 1696 */     mplew.writeInt(itemid);
/* 1697 */     if (active) {
/* 1698 */       mplew.writeMapleAsciiString(msg);
/*      */     }
/* 1700 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeMapEffect() {
/* 1704 */     return startMapEffect(null, 0, false);
/*      */   }
/*      */ 
/*      */   public static byte[] GameMaster_Func(int value) {
/* 1708 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1710 */     mplew.writeShort(SendPacketOpcode.GM_EFFECT.getValue());
/* 1711 */     mplew.write(value);
/* 1712 */     mplew.writeZeroBytes(17);
/*      */ 
/* 1714 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showOXQuiz(int questionSet, int questionId, boolean askQuestion) {
/* 1718 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1720 */     mplew.writeShort(SendPacketOpcode.OX_QUIZ.getValue());
/* 1721 */     mplew.write(askQuestion ? 1 : 0);
/* 1722 */     mplew.write(questionSet);
/* 1723 */     mplew.writeShort(questionId);
/*      */ 
/* 1725 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showEventInstructions() {
/* 1729 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1731 */     mplew.writeShort(SendPacketOpcode.GMEVENT_INSTRUCTIONS.getValue());
/* 1732 */     mplew.write(0);
/*      */ 
/* 1734 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPClock(int type, int time) {
/* 1738 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1740 */     mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
/* 1741 */     mplew.write(3);
/* 1742 */     mplew.write(type);
/* 1743 */     mplew.writeInt(time);
/*      */ 
/* 1745 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getClock(int time) {
/* 1749 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1751 */     mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
/* 1752 */     mplew.write(2);
/* 1753 */     mplew.writeInt(time);
/*      */ 
/* 1755 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getClockTime(int hour, int min, int sec) {
/* 1759 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1761 */     mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
/* 1762 */     mplew.write(1);
/* 1763 */     mplew.write(hour);
/* 1764 */     mplew.write(min);
/* 1765 */     mplew.write(sec);
/*      */ 
/* 1767 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] boatPacket(int effect, int mode) {
/* 1771 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1773 */     mplew.writeShort(SendPacketOpcode.BOAT_MOVE.getValue());
/* 1774 */     mplew.write(effect);
/* 1775 */     mplew.write(mode);
/*      */ 
/* 1780 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] setBoatState(int effect) {
/* 1784 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1786 */     mplew.writeShort(SendPacketOpcode.BOAT_STATE.getValue());
/* 1787 */     mplew.write(effect);
/* 1788 */     mplew.write(1);
/*      */ 
/* 1790 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] stopClock() {
/* 1794 */     return getPacketFromHexString(Integer.toHexString(SendPacketOpcode.STOP_CLOCK.getValue()) + " 00");
/*      */   }
/*      */ 
/*      */   public static byte[] showAriantScoreBoard() {
/* 1798 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1800 */     mplew.writeShort(SendPacketOpcode.ARIANT_SCOREBOARD.getValue());
/*      */ 
/* 1802 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendPyramidUpdate(int amount) {
/* 1806 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1808 */     mplew.writeShort(SendPacketOpcode.PYRAMID_UPDATE.getValue());
/* 1809 */     mplew.writeInt(amount);
/*      */ 
/* 1811 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendPyramidResult(byte rank, int amount) {
/* 1815 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1817 */     mplew.writeShort(SendPacketOpcode.PYRAMID_RESULT.getValue());
/* 1818 */     mplew.write(rank);
/* 1819 */     mplew.writeInt(amount);
/*      */ 
/* 1821 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] quickSlot(String skil) {
/* 1825 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1827 */     mplew.writeShort(SendPacketOpcode.QUICK_SLOT.getValue());
/* 1828 */     mplew.write(skil == null ? 0 : 1);
/* 1829 */     if (skil != null) {
/* 1830 */       String[] slots = skil.split(",");
/* 1831 */       for (int i = 0; i < 8; i++) {
/* 1832 */         mplew.writeInt(Integer.parseInt(slots[i]));
/*      */       }
/*      */     }
/*      */ 
/* 1836 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getMovingPlatforms(MapleMap map) {
/* 1840 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1842 */     mplew.writeShort(SendPacketOpcode.MOVE_PLATFORM.getValue());
/* 1843 */     mplew.writeInt(map.getPlatforms().size());
/* 1844 */     for (MapleNodes.MaplePlatform mp : map.getPlatforms()) {
/* 1845 */       mplew.writeMapleAsciiString(mp.name);
/* 1846 */       mplew.writeInt(mp.start);
/* 1847 */       mplew.writeInt(mp.SN.size());
/* 1848 */       for (int x = 0; x < mp.SN.size(); x++) {
/* 1849 */         mplew.writeInt(((Integer)mp.SN.get(x)).intValue());
/*      */       }
/* 1851 */       mplew.writeInt(mp.speed);
/* 1852 */       mplew.writeInt(mp.x1);
/* 1853 */       mplew.writeInt(mp.x2);
/* 1854 */       mplew.writeInt(mp.y1);
/* 1855 */       mplew.writeInt(mp.y2);
/* 1856 */       mplew.writeInt(mp.x1);
/* 1857 */       mplew.writeInt(mp.y1);
/* 1858 */       mplew.writeShort(mp.r);
/*      */     }
/*      */ 
/* 1861 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendPyramidKills(int amount) {
/* 1865 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1867 */     mplew.writeShort(SendPacketOpcode.PYRAMID_KILL_COUNT.getValue());
/* 1868 */     mplew.writeInt(amount);
/*      */ 
/* 1870 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendPVPMaps() {
/* 1874 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1876 */     mplew.writeShort(SendPacketOpcode.PVP_INFO.getValue());
/* 1877 */     mplew.write(3);
/* 1878 */     for (int i = 0; i < 20; i++) {
/* 1879 */       mplew.writeInt(10);
/*      */     }
/* 1881 */     mplew.writeZeroBytes(124);
/* 1882 */     mplew.writeShort(150);
/* 1883 */     mplew.write(0);
/*      */ 
/* 1885 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] gainForce(int oid, int count, int color) {
/* 1889 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 1890 */     mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
/* 1891 */     mplew.write(1);
/* 1892 */     mplew.writeInt(oid);
/* 1893 */     mplew.writeInt(0);
/* 1894 */     mplew.write(1);
/* 1895 */     mplew.writeInt(2);
/* 1896 */     mplew.writeInt(color);
/* 1897 */     mplew.writeInt(39);
/* 1898 */     mplew.writeInt(5);
/* 1899 */     mplew.writeInt(49);
/* 1900 */     mplew.write(0);
/* 1901 */     return mplew.getPacket();
/*      */   }
/*      */ 
public static byte[] getAndroidTalkStyle(int npc, String talk, int... args) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
        mplew.write(4);
        mplew.writeInt(npc);
        mplew.writeShort(10);
        mplew.writeMapleAsciiString(talk);
        mplew.write(args.length);

        for (int i = 0; i < args.length; i++) {
            mplew.writeInt(args[i]);
        }
        return mplew.getPacket();
    }

/*      */   public static byte[] achievementRatio(int amount) {
/* 1905 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1907 */     mplew.writeShort(SendPacketOpcode.ACHIEVEMENT_RATIO.getValue());
/* 1908 */     mplew.writeInt(amount);
/*      */ 
/* 1910 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPublicNPCInfo() {
/* 1914 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1916 */     mplew.writeShort(SendPacketOpcode.PUBLIC_NPC.getValue());
/* 1917 */     mplew.write(GameConstants.publicNpcIds.length);
/* 1918 */     for (int i = 0; i < GameConstants.publicNpcIds.length; i++) {
/* 1919 */       mplew.writeMapleAsciiString("");
/* 1920 */       mplew.writeInt(GameConstants.publicNpcIds[i]);
/* 1921 */       mplew.writeInt(i);
/* 1922 */       mplew.writeInt(0);
/* 1923 */       mplew.writeMapleAsciiString(GameConstants.publicNpcs[i]);
/*      */     }
/*      */ 
/* 1935 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] spawnPlayerMapobject(MapleCharacter chr) {
/* 1939 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1941 */     mplew.writeShort(SendPacketOpcode.SPAWN_PLAYER.getValue());
/* 1942 */     mplew.writeInt(chr.getId());
/* 1943 */     mplew.write(chr.getLevel());
/* 1944 */     mplew.writeMapleAsciiString(chr.getName());
/* 1945 */     MapleQuestStatus ultExplorer = chr.getQuestNoAdd(MapleQuest.getInstance(111111));
/* 1946 */     if ((ultExplorer != null) && (ultExplorer.getCustomData() != null))
/* 1947 */       mplew.writeMapleAsciiString(ultExplorer.getCustomData());
/*      */     else {
/* 1949 */       mplew.writeMapleAsciiString("");
/*      */     }
/* 1951 */     if (chr.getGuildId() <= 0) {
/* 1952 */       mplew.writeInt(0);
/* 1953 */       mplew.writeInt(0);
/*      */     } else {
/* 1955 */       MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
/* 1956 */       if (gs != null) {
/* 1957 */         mplew.writeMapleAsciiString(gs.getName());
/* 1958 */         mplew.writeShort(gs.getLogoBG());
/* 1959 */         mplew.write(gs.getLogoBGColor());
/* 1960 */         mplew.writeShort(gs.getLogo());
/* 1961 */         mplew.write(gs.getLogoColor());
/*      */       } else {
/* 1963 */         mplew.writeInt(0);
/* 1964 */         mplew.writeInt(0);
/*      */       }
/*      */     }
/*      */ 
/* 1968 */     final List<Pair<Integer, Integer>> buffvalue = new ArrayList<Pair<Integer, Integer>>();
        final int[] mask = new int[GameConstants.MAX_BUFFSTAT];
        mask[0] |= DEFAULT_BUFFMASK;
        mask[1] |= 0xA00000; // 0x200000 + 0x800000
/* 1973 */     if ((chr.getBuffedValue(MapleBuffStat.DARKSIGHT) != null) || (chr.isHidden())) {
/* 1974 */       mask[MapleBuffStat.DARKSIGHT.getPosition(true)] |= MapleBuffStat.DARKSIGHT.getValue();
/*      */     }
/* 1976 */     if (chr.getBuffedValue(MapleBuffStat.SOULARROW) != null) {
/* 1977 */       mask[MapleBuffStat.SOULARROW.getPosition(true)] |= MapleBuffStat.SOULARROW.getValue();
/*      */     }
/* 1979 */     if (chr.getBuffedValue(MapleBuffStat.COMBO) != null) {
/* 1980 */       mask[MapleBuffStat.COMBO.getPosition(true)] |= MapleBuffStat.COMBO.getValue();
/* 1981 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.COMBO).intValue()), Integer.valueOf(1)));
/*      */     }
/* 1983 */     if (chr.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {
/* 1984 */       mask[MapleBuffStat.WK_CHARGE.getPosition(true)] |= MapleBuffStat.WK_CHARGE.getValue();
/* 1985 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.WK_CHARGE).intValue()), Integer.valueOf(2)));
/* 1986 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffSource(MapleBuffStat.WK_CHARGE)), Integer.valueOf(3)));
/*      */     }
/* 1988 */     if (chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null) {
/* 1989 */       mask[MapleBuffStat.SHADOWPARTNER.getPosition(true)] |= MapleBuffStat.SHADOWPARTNER.getValue();
/* 1990 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER).intValue()), Integer.valueOf(2)));
/* 1991 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffSource(MapleBuffStat.SHADOWPARTNER)), Integer.valueOf(3)));
/*      */     }
/*      */ 
/* 1994 */     if (chr.getBuffedValue(MapleBuffStat.MORPH) != null) {
/* 1995 */       mask[MapleBuffStat.MORPH.getPosition(true)] |= MapleBuffStat.MORPH.getValue();
/* 1996 */       buffvalue.add(new Pair(Integer.valueOf(chr.getStatForBuff(MapleBuffStat.MORPH).getMorph(chr)), Integer.valueOf(2)));
/* 1997 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffSource(MapleBuffStat.MORPH)), Integer.valueOf(3)));
/*      */     }
/* 1999 */     if (chr.getBuffedValue(MapleBuffStat.BERSERK_FURY) != null) {
/* 2000 */       mask[MapleBuffStat.BERSERK_FURY.getPosition(true)] |= MapleBuffStat.BERSERK_FURY.getValue();
/*      */     }
/* 2002 */     if (chr.getBuffedValue(MapleBuffStat.DIVINE_BODY) != null) {
/* 2003 */       mask[MapleBuffStat.DIVINE_BODY.getPosition(true)] |= MapleBuffStat.DIVINE_BODY.getValue();
/*      */     }
/*      */ 
/* 2006 */     if (chr.getBuffedValue(MapleBuffStat.WIND_WALK) != null) {
/* 2007 */       mask[MapleBuffStat.WIND_WALK.getPosition(true)] |= MapleBuffStat.WIND_WALK.getValue();
/* 2008 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.WIND_WALK).intValue()), Integer.valueOf(2)));
/* 2009 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.WIND_WALK)), Integer.valueOf(3)));
/*      */     }
/* 2011 */     if (chr.getBuffedValue(MapleBuffStat.PYRAMID_PQ) != null) {
/* 2012 */       mask[MapleBuffStat.PYRAMID_PQ.getPosition(true)] |= MapleBuffStat.PYRAMID_PQ.getValue();
/* 2013 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.PYRAMID_PQ).intValue()), Integer.valueOf(2)));
/* 2014 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.PYRAMID_PQ)), Integer.valueOf(3)));
/*      */     }
/* 2016 */     if (chr.getBuffedValue(MapleBuffStat.SOARING) != null) {
/* 2017 */       mask[MapleBuffStat.SOARING.getPosition(true)] |= MapleBuffStat.SOARING.getValue();
/* 2018 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.SOARING).intValue()), Integer.valueOf(2)));
/* 2019 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.SOARING)), Integer.valueOf(3)));
/*      */     }
/* 2021 */     if (chr.getBuffedValue(MapleBuffStat.OWL_SPIRIT) != null) {
/* 2022 */       mask[MapleBuffStat.OWL_SPIRIT.getPosition(true)] |= MapleBuffStat.OWL_SPIRIT.getValue();
/* 2023 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.OWL_SPIRIT).intValue()), Integer.valueOf(2)));
/* 2024 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.OWL_SPIRIT)), Integer.valueOf(3)));
/*      */     }
/* 2026 */     if (chr.getBuffedValue(MapleBuffStat.FINAL_CUT) != null) {
/* 2027 */       mask[MapleBuffStat.FINAL_CUT.getPosition(true)] |= MapleBuffStat.FINAL_CUT.getValue();
/* 2028 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.FINAL_CUT).intValue()), Integer.valueOf(2)));
/* 2029 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.FINAL_CUT)), Integer.valueOf(3)));
/*      */     }
/*      */ 
/* 2032 */     if (chr.getBuffedValue(MapleBuffStat.TORNADO) != null) {
/* 2033 */       mask[MapleBuffStat.TORNADO.getPosition(true)] |= MapleBuffStat.TORNADO.getValue();
/* 2034 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.TORNADO).intValue()), Integer.valueOf(2)));
/* 2035 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.TORNADO)), Integer.valueOf(3)));
/*      */     }
/* 2037 */     if (chr.getBuffedValue(MapleBuffStat.INFILTRATE) != null) {
/* 2038 */       mask[MapleBuffStat.INFILTRATE.getPosition(true)] |= MapleBuffStat.INFILTRATE.getValue();
/*      */     }
/* 2040 */     if (chr.getBuffedValue(MapleBuffStat.MECH_CHANGE) != null) {
/* 2041 */       mask[MapleBuffStat.MECH_CHANGE.getPosition(true)] |= MapleBuffStat.MECH_CHANGE.getValue();
/* 2042 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.MECH_CHANGE).intValue()), Integer.valueOf(2)));
/* 2043 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.MECH_CHANGE)), Integer.valueOf(3)));
/*      */     }
/* 2045 */     if (chr.getBuffedValue(MapleBuffStat.DARK_AURA) != null) {
/* 2046 */       mask[MapleBuffStat.DARK_AURA.getPosition(true)] |= MapleBuffStat.DARK_AURA.getValue();
/* 2047 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.DARK_AURA).intValue()), Integer.valueOf(2)));
/* 2048 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.DARK_AURA)), Integer.valueOf(3)));
/*      */     }
/* 2050 */     if (chr.getBuffedValue(MapleBuffStat.BLUE_AURA) != null) {
/* 2051 */       mask[MapleBuffStat.BLUE_AURA.getPosition(true)] |= MapleBuffStat.BLUE_AURA.getValue();
/* 2052 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.BLUE_AURA).intValue()), Integer.valueOf(2)));
/* 2053 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.BLUE_AURA)), Integer.valueOf(3)));
/*      */     }
/* 2055 */     if (chr.getBuffedValue(MapleBuffStat.YELLOW_AURA) != null) {
/* 2056 */       mask[MapleBuffStat.YELLOW_AURA.getPosition(true)] |= MapleBuffStat.YELLOW_AURA.getValue();
/* 2057 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.YELLOW_AURA).intValue()), Integer.valueOf(2)));
/* 2058 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.YELLOW_AURA)), Integer.valueOf(3)));
/*      */     }
/* 2060 */     if (chr.getBuffedValue(MapleBuffStat.DIVINE_SHIELD) != null) {
/* 2061 */       mask[MapleBuffStat.DIVINE_SHIELD.getPosition(true)] |= MapleBuffStat.DIVINE_SHIELD.getValue();
/*      */     }
/* 2063 */     if (chr.getBuffedValue(MapleBuffStat.GIANT_POTION) != null) {
/* 2064 */       mask[MapleBuffStat.GIANT_POTION.getPosition(true)] |= MapleBuffStat.GIANT_POTION.getValue();
/* 2065 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.GIANT_POTION).intValue()), Integer.valueOf(2)));
/* 2066 */       buffvalue.add(new Pair(Integer.valueOf(chr.getTrueBuffSource(MapleBuffStat.GIANT_POTION)), Integer.valueOf(3)));
/*      */     }
/*      */ 
/* 2069 */     if (chr.getBuffedValue(MapleBuffStat.WATER_SHIELD) != null) {
/* 2070 */       mask[MapleBuffStat.WATER_SHIELD.getPosition(true)] |= MapleBuffStat.WATER_SHIELD.getValue();
/*      */     }
/* 2072 */     if (chr.getBuffedValue(MapleBuffStat.DARK_METAMORPHOSIS) != null) {
/* 2073 */       mask[MapleBuffStat.DARK_METAMORPHOSIS.getPosition(true)] |= MapleBuffStat.DARK_METAMORPHOSIS.getValue();
/* 2074 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.DARK_METAMORPHOSIS).intValue()), Integer.valueOf(2)));
/* 2075 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffSource(MapleBuffStat.DARK_METAMORPHOSIS)), Integer.valueOf(3)));
/*      */     }
/* 2077 */     if (chr.getBuffedValue(MapleBuffStat.SPIRIT_SURGE) != null) {
/* 2078 */       mask[MapleBuffStat.SPIRIT_SURGE.getPosition(true)] |= MapleBuffStat.SPIRIT_SURGE.getValue();
/* 2079 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.SPIRIT_SURGE).intValue()), Integer.valueOf(2)));
/* 2080 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffSource(MapleBuffStat.SPIRIT_SURGE)), Integer.valueOf(3)));
/*      */     }
/*      */ 
/* 2087 */     if (chr.getBuffedValue(MapleBuffStat.FAMILIAR_SHADOW) != null) {
/* 2088 */       mask[MapleBuffStat.FAMILIAR_SHADOW.getPosition(true)] |= MapleBuffStat.FAMILIAR_SHADOW.getValue();
/* 2089 */       buffvalue.add(new Pair(Integer.valueOf(chr.getBuffedValue(MapleBuffStat.FAMILIAR_SHADOW).intValue()), Integer.valueOf(3)));
/* 2090 */       buffvalue.add(new Pair(Integer.valueOf(chr.getStatForBuff(MapleBuffStat.FAMILIAR_SHADOW).getCharColor()), Integer.valueOf(3)));
/*      */     }
/* 2092 */     for (int i = 0; i < mask.length; i++) {
/* 2093 */       mplew.writeInt(mask[i]);
/*      */     }
/* 2095 */     for (Pair i : buffvalue) {
/* 2096 */       if (((Integer)i.right).intValue() == 3)
/* 2097 */         mplew.writeInt(((Integer)i.left).intValue());
/* 2098 */       else if (((Integer)i.right).intValue() == 2)
/* 2099 */         mplew.writeShort(((Integer)i.left).shortValue());
/* 2100 */       else if (((Integer)i.right).intValue() == 1) {
/* 2101 */         mplew.write(((Integer)i.left).byteValue());
/*      */       }
/*      */     }
/* 2104 */     mplew.writeInt(-1);
/* 2105 */     int CHAR_MAGIC_SPAWN = Randomizer.nextInt();
/*      */ 
/* 2110 */     mplew.writeInt(0);
/* 2111 */     mplew.writeLong(0L);
/* 2112 */     mplew.write(1);
/* 2113 */     mplew.writeInt(CHAR_MAGIC_SPAWN);
/* 2114 */     mplew.writeShort(0);
/* 2115 */     mplew.writeLong(0L);
/* 2116 */     mplew.write(1);
/* 2117 */     mplew.writeInt(CHAR_MAGIC_SPAWN);
/* 2118 */     mplew.writeShort(0);
/* 2119 */     mplew.writeLong(0L);
/* 2120 */     mplew.write(1);
/* 2121 */     mplew.writeInt(CHAR_MAGIC_SPAWN);
/* 2122 */     mplew.writeShort(0);
/* 2123 */     int buffSrc = chr.getBuffSource(MapleBuffStat.MONSTER_RIDING);
/* 2124 */     if (buffSrc > 0) {
/* 2125 */       Item c_mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-118);
/* 2126 */       Item mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-18);
/* 2127 */       if ((GameConstants.getMountItem(buffSrc, chr) == 0) && (c_mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-119) != null))
/* 2128 */         mplew.writeInt(c_mount.getItemId());
/* 2129 */       else if ((GameConstants.getMountItem(buffSrc, chr) == 0) && (mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-19) != null))
/* 2130 */         mplew.writeInt(mount.getItemId());
/*      */       else {
/* 2132 */         mplew.writeInt(GameConstants.getMountItem(buffSrc, chr));
/*      */       }
/* 2134 */       mplew.writeInt(buffSrc);
/*      */     } else {
/* 2136 */       mplew.writeLong(0L);
/*      */     }
/* 2138 */     mplew.write(1);
/* 2139 */     mplew.writeInt(CHAR_MAGIC_SPAWN);
/* 2140 */     mplew.writeLong(0L);
/* 2141 */     mplew.write(1);
/* 2142 */     mplew.writeInt(CHAR_MAGIC_SPAWN);
/* 2143 */     mplew.writeInt(1);
/* 2144 */     mplew.writeLong(0L);
/* 2145 */     mplew.write(0);
/* 2146 */     mplew.writeShort(0);
/* 2147 */     mplew.write(1);
/* 2148 */     mplew.writeInt(CHAR_MAGIC_SPAWN);
/* 2149 */     mplew.writeZeroBytes(16);
/* 2150 */     mplew.write(1);
/* 2151 */     mplew.writeInt(CHAR_MAGIC_SPAWN);
/*      */ 
/* 2153 */     mplew.writeShort(0);
/* 2154 */     mplew.writeShort(chr.getJob());
/* 2155 */     mplew.writeShort(0);
/* 2156 */     PacketHelper.addCharLook(mplew, chr, true);
/* 2157 */     mplew.writeInt(0);
/* 2158 */     mplew.writeInt(0);
/*      */ 
/* 2160 */     mplew.writeInt(Math.min(250, chr.getInventory(MapleInventoryType.CASH).countById(5110000)));
/* 2161 */     mplew.writeInt(0);
/* 2162 */     mplew.writeInt(0);
/*      */ 
/* 2164 */     mplew.writeInt(0);
/* 2165 */     mplew.writeInt(0);
/* 2166 */     mplew.writeInt(0);
/*      */ 
/* 2168 */     MapleQuestStatus stat = chr.getQuestNoAdd(MapleQuest.getInstance(124000));
/* 2169 */     mplew.writeInt((stat != null) && (stat.getCustomData() != null) ? Integer.valueOf(stat.getCustomData()).intValue() : 0);
/* 2170 */     mplew.writeInt(0);
/* 2171 */     mplew.writeInt(chr.getItemEffect());
/* 2172 */     mplew.writeInt(GameConstants.getInventoryType(chr.getChair()) == MapleInventoryType.SETUP ? chr.getChair() : 0);
/* 2173 */     mplew.writeInt(0);
/*      */ 
/* 2175 */     mplew.writePos(chr.getTruePosition());
/* 2176 */     mplew.write(chr.getStance());
/* 2177 */     mplew.writeShort(0);
/* 2178 */     mplew.write(0);
/* 2179 */     mplew.write(0);
/*      */ 
/* 2181 */     mplew.write(1);
/* 2182 */     mplew.write(0);
/*      */ 
/* 2184 */     mplew.writeInt(chr.getMount().getLevel());
/* 2185 */     mplew.writeInt(chr.getMount().getExp());
/* 2186 */     mplew.writeInt(chr.getMount().getFatigue());
/*      */ 
/* 2188 */     PacketHelper.addAnnounceBox(mplew, chr);
/* 2189 */     mplew.write((chr.getChalkboard() != null) && (chr.getChalkboard().length() > 0) ? 1 : 0);
/* 2190 */     if ((chr.getChalkboard() != null) && (chr.getChalkboard().length() > 0)) {
/* 2191 */       mplew.writeMapleAsciiString(chr.getChalkboard());
/*      */     }
/*      */ 
/* 2194 */     Triple rings = chr.getRings(false);
/* 2195 */     addRingInfo(mplew, (List)rings.getLeft());
/* 2196 */     addRingInfo(mplew, (List)rings.getMid());
/* 2197 */     addMRingInfo(mplew, (List)rings.getRight(), chr);
/*      */ 
/* 2199 */     mplew.write(chr.getStat().Berserk ? 1 : 0);
/* 2200 */     mplew.writeInt(0);
/* 2201 */     mplew.write(0);
/*      */ 
/* 2203 */     mplew.writeInt(0);
/* 2204 */     boolean pvp = chr.inPVP();
/* 2205 */     if (pvp) {
/* 2206 */       mplew.write(Integer.parseInt(chr.getEventInstance().getProperty("type")));
/*      */     }
/* 2208 */     if (chr.getCarnivalParty() != null)
/* 2209 */       mplew.write(chr.getCarnivalParty().getTeam());
/* 2210 */     else if (GameConstants.isTeamMap(chr.getMapId())) {
/* 2211 */       mplew.write(chr.getTeam() + (pvp ? 1 : 0));
/*      */     }
/*      */ 
/* 2214 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removePlayerFromMap(int cid) {
/* 2218 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2220 */     mplew.writeShort(SendPacketOpcode.REMOVE_PLAYER_FROM_MAP.getValue());
/* 2221 */     mplew.writeInt(cid);
/*      */ 
/* 2223 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getChatText(int cidfrom, String text, boolean whiteBG, int show) {
/* 2227 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2229 */     mplew.writeShort(SendPacketOpcode.CHATTEXT.getValue());
/* 2230 */     mplew.writeInt(cidfrom);
/* 2231 */     mplew.write(whiteBG ? 1 : 0);
/* 2232 */     mplew.writeMapleAsciiString(text);
/* 2233 */     mplew.write(show);
/*      */ 
/* 2235 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getScrollEffect(int chr, Equip.ScrollResult scrollSuccess, boolean legendarySpirit, boolean whiteScroll) {
/* 2239 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2241 */     mplew.writeShort(SendPacketOpcode.SHOW_SCROLL_EFFECT.getValue());
/* 2242 */     mplew.writeInt(chr);
/* 2243 */     mplew.write(scrollSuccess == Equip.ScrollResult.SUCCESS ? 1 : scrollSuccess == Equip.ScrollResult.CURSE ? 2 : 0);
/* 2244 */     mplew.write(legendarySpirit ? 1 : 0);
/* 2245 */     mplew.writeInt(0);
/* 2246 */     mplew.writeInt(0);
/* 2247 */     mplew.writeInt(0);
/* 2248 */     mplew.write(0);
/* 2249 */     mplew.write(0);
/*      */ 
/* 2251 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showMagnifyingEffect(int chr, short pos) {
/* 2255 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2257 */     mplew.writeShort(SendPacketOpcode.SHOW_MAGNIFYING_EFFECT.getValue());
/* 2258 */     mplew.writeInt(chr);
/* 2259 */     mplew.writeShort(pos);
/*      */ 
/* 2261 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showPotentialReset(boolean fireworks, int chr, boolean success, int itemid) {
/* 2265 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2267 */     mplew.writeShort(fireworks ? SendPacketOpcode.SHOW_FIREWORKS_EFFECT.getValue() : SendPacketOpcode.SHOW_POTENTIAL_RESET.getValue());
/* 2268 */     mplew.writeInt(chr);
/* 2269 */     mplew.write(success ? 1 : 0);
/* 2270 */     mplew.writeInt(itemid);
/*      */ 
/* 2272 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showNebuliteEffect(int chr, boolean success) {
/* 2276 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2278 */     mplew.writeShort(SendPacketOpcode.SHOW_NEBULITE_EFFECT.getValue());
/* 2279 */     mplew.writeInt(chr);
/* 2280 */     mplew.write(success ? 1 : 0);
/* 2281 */     mplew.writeMapleAsciiString(success ? "Successfully mounted Nebulite." : "Failed to mount Nebulite.");
/*      */ 
/* 2283 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] useNebuliteFusion(int cid, int itemId, boolean success) {
/* 2287 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2289 */     mplew.writeShort(SendPacketOpcode.SHOW_FUSION_EFFECT.getValue());
/* 2290 */     mplew.writeInt(cid);
/* 2291 */     mplew.write(success ? 1 : 0);
/* 2292 */     mplew.writeInt(itemId);
/*      */ 
/* 2294 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] pvpAttack(int cid, int playerLevel, int skill, int skillLevel, int speed, int mastery, int projectile, int attackCount, int chargeTime, int stance, int direction, int range, int linkSkill, int linkSkillLevel, boolean movementSkill, boolean pushTarget, boolean pullTarget, List<AttackPair> attack) {
/* 2298 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2300 */     mplew.writeShort(SendPacketOpcode.PVP_ATTACK.getValue());
/* 2301 */     mplew.writeInt(cid);
/* 2302 */     mplew.write(playerLevel);
/* 2303 */     mplew.writeInt(skill);
/* 2304 */     mplew.write(skillLevel);
/* 2305 */     mplew.writeInt(linkSkill != skill ? linkSkill : 0);
/* 2306 */     mplew.write(linkSkillLevel != skillLevel ? linkSkillLevel : 0);
/* 2307 */     mplew.write(direction);
/* 2308 */     mplew.write(movementSkill ? 1 : 0);
/* 2309 */     mplew.write(pushTarget ? 1 : 0);
/* 2310 */     mplew.write(pullTarget ? 1 : 0);
/* 2311 */     mplew.write(0);
/* 2312 */     mplew.writeShort(stance);
/* 2313 */     mplew.write(speed);
/* 2314 */     mplew.write(mastery);
/* 2315 */     mplew.writeInt(projectile);
/* 2316 */     mplew.writeInt(chargeTime);
/* 2317 */     mplew.writeInt(range);
/* 2318 */     mplew.write(attack.size());
/* 2319 */     mplew.write(0);
/* 2320 */     mplew.writeInt(0);
/* 2321 */     mplew.write(attackCount);
/* 2322 */     mplew.write(0);
/* 2323 */     for (AttackPair p : attack) {
/* 2324 */       mplew.writeInt(p.objectid);
/* 2325 */       mplew.writeInt(0);
/* 2326 */       mplew.writePos(p.point);
/* 2327 */       mplew.write(0);
/* 2328 */       mplew.writeInt(0);
/* 2329 */       for (Pair atk : p.attack) {
/* 2330 */         mplew.writeInt(((Integer)atk.left).intValue());
/* 2331 */         mplew.writeInt(0);
/* 2332 */         mplew.write(((Boolean)atk.right).booleanValue() ? 1 : 0);
/* 2333 */         mplew.writeShort(0);
/*      */       }
/*      */     }
/*      */ 
/* 2337 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPMist(int cid, int mistSkill, int mistLevel, int damage) {
/* 2341 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2343 */     mplew.writeShort(SendPacketOpcode.PVP_MIST.getValue());
/* 2344 */     mplew.writeInt(cid);
/* 2345 */     mplew.writeInt(mistSkill);
/* 2346 */     mplew.write(mistLevel);
/* 2347 */     mplew.writeInt(damage);
/* 2348 */     mplew.write(8);
/* 2349 */     mplew.writeInt(1000);
/*      */ 
/* 2351 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] pvpCool(int cid, List<Integer> attack) {
/* 2355 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2357 */     mplew.writeShort(SendPacketOpcode.PVP_COOL.getValue());
/* 2358 */     mplew.writeInt(cid);
/* 2359 */     mplew.write(attack.size());
/* 2360 */     for (Iterator i$ = attack.iterator(); i$.hasNext(); ) { int b = ((Integer)i$.next()).intValue();
/* 2361 */       mplew.writeInt(b);
/*      */     }
/*      */ 
/* 2364 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] teslaTriangle(int cid, int sum1, int sum2, int sum3) {
/* 2368 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2370 */     mplew.writeShort(SendPacketOpcode.TESLA_TRIANGLE.getValue());
/* 2371 */     mplew.writeInt(cid);
/* 2372 */     mplew.writeInt(sum1);
/* 2373 */     mplew.writeInt(sum2);
/* 2374 */     mplew.writeInt(sum3);
/*      */ 
/* 2376 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] followEffect(int initiator, int replier, Point toMap) {
/* 2380 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2382 */     mplew.writeShort(SendPacketOpcode.FOLLOW_EFFECT.getValue());
/* 2383 */     mplew.writeInt(initiator);
/* 2384 */     mplew.writeInt(replier);
/* 2385 */     if (replier == 0) {
/* 2386 */       mplew.write(toMap == null ? 0 : 1);
/* 2387 */       if (toMap != null) {
/* 2388 */         mplew.writeInt(toMap.x);
/* 2389 */         mplew.writeInt(toMap.y);
/*      */       }
/*      */     }
/*      */ 
/* 2393 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showPQReward(int cid) {
/* 2397 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2399 */     mplew.writeShort(SendPacketOpcode.SHOW_PQ_REWARD.getValue());
/* 2400 */     mplew.writeInt(cid);
/* 2401 */     for (int i = 0; i < 6; i++) {
/* 2402 */       mplew.write(0);
/*      */     }
/*      */ 
/* 2405 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] craftMake(int cid, int something, int time) {
/* 2409 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2411 */     mplew.writeShort(SendPacketOpcode.CRAFT_EFFECT.getValue());
/* 2412 */     mplew.writeInt(cid);
/* 2413 */     mplew.writeInt(something);
/* 2414 */     mplew.writeInt(time);
/*      */ 
/* 2416 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] craftFinished(int cid, int craftID, int ranking, int itemId, int quantity, int exp) {
/* 2420 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2422 */     mplew.writeShort(SendPacketOpcode.CRAFT_COMPLETE.getValue());
/* 2423 */     mplew.writeInt(cid);
/* 2424 */     mplew.writeInt(craftID);
/* 2425 */     mplew.writeInt(ranking);
/* 2426 */     mplew.writeInt(itemId);
/* 2427 */     mplew.writeInt(quantity);
/* 2428 */     mplew.writeInt(exp);
/*      */ 
/* 2430 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] harvestResult(int cid, boolean success) {
/* 2434 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2436 */     mplew.writeShort(SendPacketOpcode.HARVESTED.getValue());
/* 2437 */     mplew.writeInt(cid);
/* 2438 */     mplew.write(success ? 1 : 0);
/*      */ 
/* 2440 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] playerDamaged(int cid, int dmg) {
/* 2444 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2446 */     mplew.writeShort(SendPacketOpcode.PLAYER_DAMAGED.getValue());
/* 2447 */     mplew.writeInt(cid);
/* 2448 */     mplew.writeInt(dmg);
/*      */ 
/* 2450 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showPyramidEffect(int chr) {
/* 2454 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2456 */     mplew.writeShort(SendPacketOpcode.NETT_PYRAMID.getValue());
/* 2457 */     mplew.writeInt(chr);
/* 2458 */     mplew.write(1);
/* 2459 */     mplew.writeInt(0);
/* 2460 */     mplew.writeInt(0);
/*      */ 
/* 2462 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] pamsSongEffect(int cid) {
/* 2466 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2468 */     mplew.writeShort(SendPacketOpcode.PAMS_SONG.getValue());
/* 2469 */     mplew.writeInt(cid);
/*      */ 
/* 2471 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] spawnDragon(MapleDragon d) {
/* 2475 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2477 */     mplew.writeShort(SendPacketOpcode.DRAGON_SPAWN.getValue());
/* 2478 */     mplew.writeInt(d.getOwner());
/* 2479 */     mplew.writeInt(d.getPosition().x);
/* 2480 */     mplew.writeInt(d.getPosition().y);
/* 2481 */     mplew.write(d.getStance());
/* 2482 */     mplew.writeShort(0);
/* 2483 */     mplew.writeShort(d.getJobId());
/*      */ 
/* 2485 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeDragon(int chrid) {
/* 2489 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2491 */     mplew.writeShort(SendPacketOpcode.DRAGON_REMOVE.getValue());
/* 2492 */     mplew.writeInt(chrid);
/*      */ 
/* 2494 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] moveDragon(MapleDragon d, Point startPos, List<LifeMovementFragment> moves) {
/* 2498 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2500 */     mplew.writeShort(SendPacketOpcode.DRAGON_MOVE.getValue());
/* 2501 */     mplew.writeInt(d.getOwner());
/* 2502 */     mplew.writePos(startPos);
/* 2503 */     mplew.writeInt(0);
/* 2504 */     PacketHelper.serializeMovementList(mplew, moves);
/*      */ 
/* 2506 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] spawnAndroid(MapleCharacter cid, MapleAndroid android) {
/* 2510 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2512 */     mplew.writeShort(SendPacketOpcode.ANDROID_SPAWN.getValue());
/* 2513 */     mplew.writeInt(cid.getId());
/* 2514 */     mplew.write(android.getItemId() == 1662006 ? 5 : android.getItemId() - 1661999);
/* 2515 */     mplew.writePos(android.getPos());
/* 2516 */     mplew.write(android.getStance());
/* 2517 */     mplew.writeShort(0);
/* 2518 */     mplew.writeShort(0);
/* 2519 */     mplew.writeShort(android.getHair() - 30000);
/* 2520 */     mplew.writeShort(android.getFace() - 20000);
/* 2521 */     mplew.writeMapleAsciiString(android.getName());
/* 2522 */     for (short i = -1200; i > -1207; i = (short)(i - 1)) {
/* 2523 */       Item item = cid.getInventory(MapleInventoryType.EQUIPPED).getItem(i);
/* 2524 */       mplew.writeInt(item != null ? item.getItemId() : 0);
/*      */     }
/*      */ 
/* 2527 */     return mplew.getPacket();
/*      */   }
/*      */ 
  public static byte[] moveAndroid(int cid, Point pos, List<LifeMovementFragment> res) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ANDROID_MOVE.getValue());
        mplew.writeInt(cid);
        mplew.writePos(pos);
        mplew.writeInt(Integer.MAX_VALUE); //time left in milliseconds? this appears to go down...slowly 1377440900
        PacketHelper.serializeMovementList(mplew, res);
        return mplew.getPacket();
    }
/*      */ 
/*      */   public static byte[] showAndroidEmotion(int cid, int animation) {
/* 2543 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2545 */     mplew.writeShort(SendPacketOpcode.ANDROID_EMOTION.getValue());
/* 2546 */     mplew.writeInt(cid);
/* 2547 */     mplew.write(0);
/* 2548 */     mplew.write(animation);
/*      */ 
/* 2550 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateAndroidLook(boolean itemOnly, MapleCharacter cid, MapleAndroid android) {
/* 2554 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2556 */     mplew.writeShort(SendPacketOpcode.ANDROID_UPDATE.getValue());
/* 2557 */     mplew.writeInt(cid.getId());
/* 2558 */     mplew.write(itemOnly ? 1 : 0);
/* 2559 */     if (itemOnly) {
/* 2560 */       for (short i = -1200; i > -1207; i = (short)(i - 1)) {
/* 2561 */         Item item = cid.getInventory(MapleInventoryType.EQUIPPED).getItem(i);
/* 2562 */         mplew.writeInt(item != null ? item.getItemId() : 0);
/*      */       }
/*      */     } else {
/* 2565 */       mplew.writeShort(0);
/* 2566 */       mplew.writeShort(android.getHair() - 30000);
/* 2567 */       mplew.writeShort(android.getFace() - 20000);
/* 2568 */       mplew.writeMapleAsciiString(android.getName());
/*      */     }
/*      */ 
/* 2571 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] deactivateAndroid(int cid) {
/* 2575 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2577 */     mplew.writeShort(SendPacketOpcode.ANDROID_DEACTIVATED.getValue());
/* 2578 */     mplew.writeInt(cid);
/*      */ 
/* 2580 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeFamiliar(int cid) {
/* 2584 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2586 */     mplew.writeShort(SendPacketOpcode.SPAWN_FAMILIAR.getValue());
/* 2587 */     mplew.writeInt(cid);
/* 2588 */     mplew.writeShort(0);
/* 2589 */     mplew.write(0);
/*      */ 
/* 2591 */     return mplew.getPacket();
/*      */   }
/*      */ 
  public static byte[] spawnFamiliar(MonsterFamiliar mf, boolean spawn, boolean respawn) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(respawn ? SendPacketOpcode.RESPAWN_FAMILIAR.getValue() : SendPacketOpcode.SPAWN_FAMILIAR.getValue());
        mplew.writeInt(mf.getCharacterId());
        mplew.write(spawn ? 1 : 0);
        mplew.write(respawn ? 1 : 0);
        mplew.write(0);
        if (spawn) {
            mplew.writeInt(mf.getFamiliar());
            mplew.writeInt(mf.getFatigue());
            mplew.writeInt(mf.getVitality() * 300); //max fatigue
            mplew.writeMapleAsciiString(mf.getName());
            mplew.writePos(mf.getTruePosition());
            mplew.write(mf.getStance());
            mplew.writeShort(mf.getFh());
        }

        return mplew.getPacket();
    }
  public static byte[] addStolenSkill(int jobNum, int index, int skill, int level) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_STOLEN_SKILLS.getValue());
        mplew.write(1);
        mplew.write(0);
        mplew.writeInt(jobNum);
        mplew.writeInt(index);
        mplew.writeInt(skill);
        mplew.writeInt(level);
        mplew.writeInt(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] removeStolenSkill(int jobNum, int index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_STOLEN_SKILLS.getValue());
        mplew.write(1);
        mplew.write(3);
        mplew.writeInt(jobNum);
        mplew.writeInt(index);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] replaceStolenSkill(int base, int skill) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REPLACE_SKILLS.getValue());
        mplew.write(1);
        mplew.write(skill > 0 ? 1 : 0);
        mplew.writeInt(base);
        mplew.writeInt(skill);

        return mplew.getPacket();
    }
  
public static byte[] gainCardStack(int oid, int runningId, int color, int skillid, int damage, int times) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

    mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
    mplew.write(0);
    mplew.writeInt(oid);
    mplew.writeInt(1);
    mplew.writeInt(damage);
    mplew.writeInt(skillid);
    for (int i = 0; i < times; ++i) {
      mplew.write(1);
      mplew.writeInt((damage == 0) ? runningId + i : runningId);
      mplew.writeInt(color);
      mplew.writeInt(Randomizer.rand(15, 29));
      mplew.writeInt(Randomizer.rand(7, 11));
      mplew.writeInt(Randomizer.rand(0, 9));
    }
    mplew.write(0);

    return mplew.getPacket();
  }
    
  
  public static byte[] getCarteAnimation(int cid, int oid, int job, int total, int numDisplay) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(0);
        mplew.writeInt(cid);
        mplew.writeInt(1);

        mplew.writeInt(oid);
        mplew.writeInt(job == 2412 ? 24120002 : 24100003);
        mplew.write(1);
        for (int i = 1; i <= numDisplay; i++) {
            mplew.writeInt(total - (numDisplay - i));
            mplew.writeInt(job == 2412 ? 2 : 0);

            mplew.writeInt(15 + Randomizer.nextInt(15));
            mplew.writeInt(7 + Randomizer.nextInt(5));
            mplew.writeInt(Randomizer.nextInt(4));

            mplew.write(i == numDisplay ? 0 : 1);
        }

        return mplew.getPacket();
    }
/*      */ 
/*      */   public static byte[] moveFamiliar(int cid, Point startPos, List<LifeMovementFragment> moves) {
/* 2615 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2617 */     mplew.writeShort(SendPacketOpcode.MOVE_FAMILIAR.getValue());
/* 2618 */     mplew.writeInt(cid);
/* 2619 */     mplew.write(0);
/* 2620 */     mplew.writePos(startPos);
/* 2621 */     mplew.writeInt(0);
/* 2622 */     PacketHelper.serializeMovementList(mplew, moves);
/*      */ 
/* 2624 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] touchFamiliar(int cid, byte unk, int objectid, int type, int delay, int damage) {
/* 2628 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2630 */     mplew.writeShort(SendPacketOpcode.TOUCH_FAMILIAR.getValue());
/* 2631 */     mplew.writeInt(cid);
/* 2632 */     mplew.write(0);
/* 2633 */     mplew.write(unk);
/* 2634 */     mplew.writeInt(objectid);
/* 2635 */     mplew.writeInt(type);
/* 2636 */     mplew.writeInt(delay);
/* 2637 */     mplew.writeInt(damage);
/*      */ 
/* 2639 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */  public static byte[] familiarAttack(int cid, byte unk, List<Triple<Integer, Integer, List<Integer>>> attackPair) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ATTACK_FAMILIAR.getValue());
        mplew.writeInt(cid);
        mplew.write(0);// familiar id?
        mplew.write(unk);
        mplew.write(attackPair.size());
        for (Triple<Integer, Integer, List<Integer>> s : attackPair) {
            mplew.writeInt(s.left);
            mplew.write(s.mid);
            mplew.write(s.right.size());
            for (int damage : s.right) {
                mplew.writeInt(damage);
            }
        }

        return mplew.getPacket();
    }
/*      */ 
/*      */   public static byte[] renameFamiliar(MonsterFamiliar mf) {
/* 2663 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2665 */     mplew.writeShort(SendPacketOpcode.RENAME_FAMILIAR.getValue());
/* 2666 */     mplew.writeInt(mf.getCharacterId());
/* 2667 */     mplew.write(0);
/* 2668 */     mplew.writeInt(mf.getFamiliar());
/* 2669 */     mplew.writeMapleAsciiString(mf.getName());
/*      */ 
/* 2671 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateFamiliar(MonsterFamiliar mf) {
/* 2675 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2677 */     mplew.writeShort(SendPacketOpcode.UPDATE_FAMILIAR.getValue());
/* 2678 */     mplew.writeInt(mf.getCharacterId());
/* 2679 */     mplew.writeInt(mf.getFamiliar());
/* 2680 */     mplew.writeInt(mf.getFatigue());
/* 2681 */     mplew.writeLong(PacketHelper.getTime(mf.getVitality() >= 3 ? System.currentTimeMillis() : -2L));
/*      */ 
/* 2683 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] movePlayer(int cid, List<LifeMovementFragment> moves, Point startPos) {
/* 2687 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2689 */     mplew.writeShort(SendPacketOpcode.MOVE_PLAYER.getValue());
/* 2690 */     mplew.writeInt(cid);
/* 2691 */     mplew.writePos(startPos);
/* 2692 */     mplew.writeInt(0);
/* 2693 */     PacketHelper.serializeMovementList(mplew, moves);
/*      */ 
/* 2695 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] closeRangeAttack(int cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, boolean energy, int lvl, byte mastery, byte unk, int charge) {
/* 2699 */     return addAttackInfo(energy ? 4 : 0, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, 0, null, 0);
/*      */   }
/*      */ 
/*      */   public static byte[] rangedAttack(int cid, byte tbyte, int skill, int level, int display, byte speed, int itemid, List<AttackPair> damage, Point pos, int lvl, byte mastery, byte unk) {
/* 2703 */     return addAttackInfo(1, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, itemid, pos, 0);
/*      */   }
/*      */ 
/*      */   public static byte[] strafeAttack(int cid, byte tbyte, int skill, int level, int display, byte speed, int itemid, List<AttackPair> damage, Point pos, int lvl, byte mastery, byte unk, int ultLevel) {
/* 2707 */     return addAttackInfo(2, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, itemid, pos, ultLevel);
/*      */   }
/*      */ 
/*      */   public static byte[] magicAttack(int cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, int charge, int lvl, byte unk) {
/* 2711 */     return addAttackInfo(3, cid, tbyte, skill, level, display, speed, damage, lvl, (byte)0, unk, charge, null, 0);
/*      */   }
/*      */ 
/*      */   public static byte[] addAttackInfo(int type, int cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, int lvl, byte mastery, byte unk, int charge, Point pos, int ultLevel)
/*      */   {
/* 2716 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2718 */     if (type == 0)
/* 2719 */       mplew.writeShort(SendPacketOpcode.CLOSE_RANGE_ATTACK.getValue());
/* 2720 */     else if ((type == 1) || (type == 2))
/* 2721 */       mplew.writeShort(SendPacketOpcode.RANGED_ATTACK.getValue());
/* 2722 */     else if (type == 3)
/* 2723 */       mplew.writeShort(SendPacketOpcode.MAGIC_ATTACK.getValue());
/*      */     else {
/* 2725 */       mplew.writeShort(SendPacketOpcode.ENERGY_ATTACK.getValue());
/*      */     }
/*      */ 
/* 2728 */     mplew.writeInt(cid);
/* 2729 */     mplew.write(tbyte);
/* 2730 */     mplew.write(lvl);
/* 2731 */     if ((skill > 0) || (type == 3)) {
/* 2732 */       mplew.write(level);
/* 2733 */       mplew.writeInt(skill);
/*      */     } else {
/* 2735 */       mplew.write(0);
/*      */     }
/*      */ 
/* 2738 */     if (type == 2) {
/* 2739 */       mplew.write(ultLevel);
/* 2740 */       if (ultLevel > 0) {
/* 2741 */         mplew.writeInt(3220010);
/*      */       }
/*      */     }
/* 2744 */     mplew.write(unk);
/* 2745 */     mplew.writeShort(display);
/* 2746 */     mplew.write(speed);
/* 2747 */     mplew.write(mastery);
/* 2748 */     mplew.writeInt(charge);
/* 2749 */     for (AttackPair oned : damage) {
/* 2750 */       if (oned.attack != null) {
/* 2751 */         mplew.writeInt(oned.objectid);
/* 2752 */         mplew.write(7);
/* 2753 */         if (skill == 4211006) {
/* 2754 */           mplew.write(oned.attack.size());
/* 2755 */           for (Pair eachd : oned.attack)
/* 2756 */             mplew.writeInt(((Integer)eachd.left).intValue());
/*      */         }
/*      */         else {
/* 2759 */           for (Pair eachd : oned.attack) {
/* 2760 */             mplew.write(((Boolean)eachd.right).booleanValue() ? 1 : 0);
/* 2761 */             mplew.writeInt(((Integer)eachd.left).intValue());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 2766 */     if ((type == 1) || (type == 2))
/* 2767 */       mplew.writePos(pos);
/* 2768 */     else if ((type == 3) && (charge > 0)) {
/* 2769 */       mplew.writeInt(charge);
/*      */     }
/*      */ 
/* 2772 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] skillEffect(MapleCharacter from, int skillId, byte level, short display, byte unk) {
/* 2776 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2778 */     mplew.writeShort(SendPacketOpcode.SKILL_EFFECT.getValue());
/* 2779 */     mplew.writeInt(from.getId());
/* 2780 */     mplew.writeInt(skillId);
/* 2781 */     mplew.write(level);
/* 2782 */     mplew.writeShort(display);
/* 2783 */     mplew.write(unk);
/*      */ 
/* 2785 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] skillCancel(MapleCharacter from, int skillId) {
/* 2789 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2791 */     mplew.writeShort(SendPacketOpcode.CANCEL_SKILL_EFFECT.getValue());
/* 2792 */     mplew.writeInt(from.getId());
/* 2793 */     mplew.writeInt(skillId);
/*      */ 
/* 2795 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] damagePlayer(int cid, int type, int damage, int monsteridfrom, byte direction, int skillid, int pDMG, boolean pPhysical, int pID, byte pType, Point pPos, byte offset, int offset_d, int fake) {
/* 2799 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2801 */     mplew.writeShort(SendPacketOpcode.DAMAGE_PLAYER.getValue());
/* 2802 */     mplew.writeInt(cid);
/* 2803 */     mplew.write(type);
/* 2804 */     mplew.writeInt(damage);
/* 2805 */     mplew.write(0);
/* 2806 */     if (type >= -1) {
/* 2807 */       mplew.writeInt(monsteridfrom);
/* 2808 */       mplew.write(direction);
/* 2809 */       mplew.writeInt(skillid);
/* 2810 */       mplew.writeInt(pDMG);
/* 2811 */       mplew.write(0);
/* 2812 */       if (pDMG > 0) {
/* 2813 */         mplew.write(pPhysical ? 1 : 0);
/* 2814 */         mplew.writeInt(pID);
/* 2815 */         mplew.write(pType);
/* 2816 */         mplew.writePos(pPos);
/*      */       }
/* 2818 */       mplew.write(offset);
/* 2819 */       if (offset == 1) {
/* 2820 */         mplew.writeInt(offset_d);
/*      */       }
/*      */     }
/* 2823 */     mplew.writeInt(damage);
/* 2824 */     if ((damage <= 0) || (fake > 0)) {
/* 2825 */       mplew.writeInt(fake);
/*      */     }
/*      */ 
/* 2828 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] facialExpression(MapleCharacter from, int expression) {
/* 2832 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2834 */     mplew.writeShort(SendPacketOpcode.FACIAL_EXPRESSION.getValue());
/* 2835 */     mplew.writeInt(from.getId());
/* 2836 */     mplew.writeInt(expression);
/* 2837 */     mplew.writeInt(-1);
/* 2838 */     mplew.write(0);
/*      */ 
/* 2840 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] itemEffect(int characterid, int itemid) {
/* 2844 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2846 */     mplew.writeShort(SendPacketOpcode.SHOW_ITEM_EFFECT.getValue());
/* 2847 */     mplew.writeInt(characterid);
/* 2848 */     mplew.writeInt(itemid);
/*      */ 
/* 2850 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showTitle(int characterid, int itemid) {
/* 2854 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2856 */     mplew.writeShort(SendPacketOpcode.SHOW_TITLE.getValue());
/* 2857 */     mplew.writeInt(characterid);
/* 2858 */     mplew.writeInt(itemid);
/*      */ 
/* 2860 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showChair(int characterid, int itemid) {
/* 2864 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2866 */     mplew.writeShort(SendPacketOpcode.SHOW_CHAIR.getValue());
/* 2867 */     mplew.writeInt(characterid);
/* 2868 */     mplew.writeInt(itemid);
/* 2869 */     mplew.writeInt(0);
/*      */ 
/* 2871 */     return mplew.getPacket();
/*      */   }
/*      */ 
    public static byte[] updateCharLook(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_LOOK.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(1);
        PacketHelper.addCharLook(mplew, chr, false);
        Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> rings = chr.getRings(false);
        addRingInfo(mplew, rings.getLeft());
        addRingInfo(mplew, rings.getMid());
        addMRingInfo(mplew, rings.getRight(), chr);
        mplew.writeInt(0); // -> charid to follow (4)
        return mplew.getPacket();
    }
/*      */ 
/*      */   public static byte[] updatePartyMemberHP(int cid, int curhp, int maxhp) {
/* 2891 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2893 */     mplew.writeShort(SendPacketOpcode.UPDATE_PARTYMEMBER_HP.getValue());
/* 2894 */     mplew.writeInt(cid);
/* 2895 */     mplew.writeInt(curhp);
/* 2896 */     mplew.writeInt(maxhp);
/*      */ 
/* 2898 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] loadGuildName(MapleCharacter chr) {
/* 2902 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2904 */     mplew.writeShort(SendPacketOpcode.LOAD_GUILD_NAME.getValue());
/* 2905 */     mplew.writeInt(chr.getId());
/* 2906 */     if (chr.getGuildId() <= 0) {
/* 2907 */       mplew.writeShort(0);
/*      */     } else {
/* 2909 */       MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
/* 2910 */       if (gs != null)
/* 2911 */         mplew.writeMapleAsciiString(gs.getName());
/*      */       else {
/* 2913 */         mplew.writeShort(0);
/*      */       }
/*      */     }
/*      */ 
/* 2917 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] loadGuildIcon(MapleCharacter chr) {
/* 2921 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2923 */     mplew.writeShort(SendPacketOpcode.LOAD_GUILD_ICON.getValue());
/* 2924 */     mplew.writeInt(chr.getId());
/* 2925 */     if (chr.getGuildId() <= 0) {
/* 2926 */       mplew.writeZeroBytes(6);
/*      */     } else {
/* 2928 */       MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
/* 2929 */       if (gs != null) {
/* 2930 */         mplew.writeShort(gs.getLogoBG());
/* 2931 */         mplew.write(gs.getLogoBGColor());
/* 2932 */         mplew.writeShort(gs.getLogo());
/* 2933 */         mplew.write(gs.getLogoColor());
/*      */       } else {
/* 2935 */         mplew.writeZeroBytes(6);
/*      */       }
/*      */     }
/*      */ 
/* 2939 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] changeTeam(int cid, int type) {
/* 2943 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2945 */     mplew.writeShort(SendPacketOpcode.LOAD_TEAM.getValue());
/* 2946 */     mplew.writeInt(cid);
/* 2947 */     mplew.write(type);
/*      */ 
/* 2949 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showHarvesting(MapleClient c, int tool) {
/* 2953 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2955 */       mplew.writeShort(SendPacketOpcode.SHOW_HARVEST.getValue());
/* 2956 */       mplew.writeInt(c.getPlayer().getId());
/* 2958 */       mplew.writeInt(1);
/* 2959 */       mplew.writeInt(tool);
/* 2961 */       mplew.writeInt(0);

/* 2964 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPHPBar(int cid, int hp, int maxHp) {
/* 2968 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2970 */     mplew.writeShort(SendPacketOpcode.PVP_HP.getValue());
/* 2971 */     mplew.writeInt(cid);
/* 2972 */     mplew.writeInt(hp);
/* 2973 */     mplew.writeInt(maxHp);
/*      */ 
/* 2975 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] cancelChair(int id) {
/* 2979 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2981 */     mplew.writeShort(SendPacketOpcode.CANCEL_CHAIR.getValue());
/* 2982 */     if (id == -1) {
/* 2983 */       mplew.write(0);
/*      */     } else {
/* 2985 */       mplew.write(1);
/* 2986 */       mplew.writeShort(id);
/*      */     }
/*      */ 
/* 2989 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] instantMapWarp(byte portal) {
/* 2993 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2995 */     mplew.writeShort(SendPacketOpcode.CURRENT_MAP_WARP.getValue());
/* 2996 */     mplew.write(0);
/* 2997 */     mplew.write(portal);
/*      */ 
/* 2999 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateQuestInfo(MapleCharacter c, int quest, int npc, byte progress) {
/* 3003 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3005 */     mplew.writeShort(SendPacketOpcode.UPDATE_QUEST_INFO.getValue());
/* 3006 */     mplew.write(progress);
/* 3007 */     mplew.writeShort(quest);
/* 3008 */     mplew.writeInt(npc);
/* 3009 */     mplew.writeInt(0);
/*      */ 
/* 3011 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateQuestFinish(int quest, int npc, int nextquest) {
/* 3015 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3017 */     mplew.writeShort(SendPacketOpcode.UPDATE_QUEST_INFO.getValue());
/* 3018 */     mplew.write(10);
/* 3019 */     mplew.writeShort(quest);
/* 3020 */     mplew.writeInt(npc);
/* 3021 */     mplew.writeInt(nextquest);
/*      */ 
/* 3023 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendHint(String hint, int width, int height) {
/* 3027 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3029 */     mplew.writeShort(SendPacketOpcode.PLAYER_HINT.getValue());
/* 3030 */     mplew.writeMapleAsciiString(hint);
/* 3031 */     mplew.writeShort(width < 1 ? Math.max(hint.length() * 10, 40) : width);
/* 3032 */     mplew.writeShort(Math.max(height, 5));
/* 3033 */     mplew.write(1);
/*      */ 
/* 3035 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] testCombo(int value) {
/* 3039 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3041 */     mplew.writeShort(SendPacketOpcode.ARAN_COMBO.getValue());
/* 3042 */     mplew.writeInt(value);
/*      */ 
/* 3044 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] rechargeCombo(int value) {
/* 3048 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3050 */     mplew.writeShort(SendPacketOpcode.ARAN_COMBO_RECHARGE.getValue());
/* 3051 */     mplew.writeInt(value);
/*      */ 
/* 3053 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getFollowMessage(String msg) {
/* 3057 */     return getGameMessage(msg, true);
/*      */   }
/*      */ 
/*      */   public static byte[] getGameMessage(String msg, boolean white) {
/* 3061 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3077 */     mplew.writeShort(SendPacketOpcode.GAME_MESSAGE.getValue());
/* 3078 */     mplew.writeShort(white ? 11 : 6);
/* 3079 */     mplew.writeMapleAsciiString(msg);
/*      */ 
/* 3081 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getBuffZoneEffect(int itemId) {
/* 3085 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3087 */     mplew.writeShort(SendPacketOpcode.BUFF_ZONE_EFFECT.getValue());
/* 3088 */     mplew.writeInt(itemId);
/*      */ 
/* 3090 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getTimeBombAttack() {
/* 3094 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3096 */     mplew.writeShort(SendPacketOpcode.TIME_BOMB_ATTACK.getValue());
/* 3097 */     mplew.writeInt(0);
/* 3098 */     mplew.writeInt(0);
/* 3099 */     mplew.writeInt(0);
/* 3100 */     mplew.writeInt(10);
/* 3101 */     mplew.writeInt(6);
/*      */ 
/* 3103 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] moveFollow(Point otherStart, Point myStart, Point otherEnd, List<LifeMovementFragment> moves) {
/* 3107 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3109 */     mplew.writeShort(SendPacketOpcode.FOLLOW_MOVE.getValue());
/* 3110 */     mplew.writePos(otherStart);
/* 3111 */     mplew.writePos(myStart);
/* 3112 */     PacketHelper.serializeMovementList(mplew, moves);
/* 3113 */     mplew.write(17);
/* 3114 */     for (int i = 0; i < 8; i++) {
/* 3115 */       mplew.write(0);
/*      */     }
/* 3117 */     mplew.write(0);
/* 3118 */     mplew.writePos(otherEnd);
/* 3119 */     mplew.writePos(otherStart);
/*      */ 
/* 3121 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getFollowMsg(int opcode) {
/* 3125 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3127 */     mplew.writeShort(SendPacketOpcode.FOLLOW_MSG.getValue());
/* 3128 */     mplew.writeLong(opcode);
/*      */ 
/* 3130 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] registerFamiliar(MonsterFamiliar mf) {
/* 3134 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3136 */     mplew.writeShort(SendPacketOpcode.REGISTER_FAMILIAR.getValue());
/* 3137 */     mplew.writeLong(mf.getId());
/* 3138 */     mf.writeRegisterPacket(mplew, false);
/* 3139 */     mplew.writeShort(mf.getVitality() >= 3 ? 1 : 0);
/*      */ 
/* 3141 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] createUltimate(int amount) {
/* 3145 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3147 */     mplew.writeShort(SendPacketOpcode.CREATE_ULTIMATE.getValue());
/* 3148 */     mplew.writeInt(amount);
/*      */ 
/* 3150 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] harvestMessage(int oid, int msg) {
/* 3154 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3156 */     mplew.writeShort(SendPacketOpcode.HARVEST_MESSAGE.getValue());
/* 3157 */     mplew.writeInt(oid);
/* 3158 */     mplew.writeInt(msg);
/*      */ 
/* 3160 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] openBag(int index, int itemId, boolean firstTime) {
/* 3164 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3166 */     mplew.writeShort(SendPacketOpcode.OPEN_BAG.getValue());
/* 3167 */     mplew.writeInt(index);
/* 3168 */     mplew.writeInt(itemId);
/* 3169 */     mplew.writeShort(firstTime ? 1 : 0);
/*      */ 
/* 3171 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] dragonBlink(int portalId) {
/* 3175 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3177 */     mplew.writeShort(SendPacketOpcode.DRAGON_BLINK.getValue());
/* 3178 */     mplew.write(portalId);
/*      */ 
/* 3180 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPVPIceGage(int score) {
/* 3184 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3186 */     mplew.writeShort(SendPacketOpcode.PVP_ICEGAGE.getValue());
/* 3187 */     mplew.writeInt(score);
/*      */ 
/* 3189 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] skillCooldown(int sid, int time) {
/* 3193 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3195 */     mplew.writeShort(SendPacketOpcode.COOLDOWN.getValue());
/* 3196 */     mplew.writeInt(sid);
/* 3197 */     mplew.writeInt(time);
/*      */ 
/* 3199 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] dropItemFromMapObject(MapleMapItem drop, Point dropfrom, Point dropto, byte mod) {
/* 3203 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3205 */     mplew.writeShort(SendPacketOpcode.DROP_ITEM_FROM_MAPOBJECT.getValue());
/* 3206 */     mplew.write(mod);
/* 3207 */     mplew.writeInt(drop.getObjectId());
/* 3208 */     mplew.write(drop.getMeso() > 0 ? 1 : 0);
/* 3209 */     mplew.writeInt(drop.getItemId());
/* 3210 */     mplew.writeInt(drop.getOwner());
/* 3211 */     mplew.write(drop.getDropType());
/* 3212 */     mplew.writePos(dropto);
/* 3213 */     mplew.writeInt(0);
/* 3214 */     if (mod != 2) {
/* 3215 */       mplew.writePos(dropfrom);
/* 3216 */       mplew.writeShort(0);
/*      */     }
/* 3218 */     if (drop.getMeso() == 0) {
/* 3219 */       PacketHelper.addExpirationTime(mplew, drop.getItem().getExpiration());
/*      */     }
/* 3221 */     mplew.writeShort(drop.isPlayerDrop() ? 0 : 1);
/*      */ 
/* 3223 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] explodeDrop(int oid) {
/* 3227 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3229 */     mplew.writeShort(SendPacketOpcode.REMOVE_ITEM_FROM_MAP.getValue());
/* 3230 */     mplew.write(4);
/* 3231 */     mplew.writeInt(oid);
/* 3232 */     mplew.writeShort(655);
/*      */ 
/* 3234 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeItemFromMap(int oid, int animation, int cid) {
/* 3238 */     return removeItemFromMap(oid, animation, cid, 0);
/*      */   }
/*      */ 
/*      */   public static byte[] removeItemFromMap(int oid, int animation, int cid, int slot) {
/* 3242 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3244 */     mplew.writeShort(SendPacketOpcode.REMOVE_ITEM_FROM_MAP.getValue());
/* 3245 */     mplew.write(animation);
/* 3246 */     mplew.writeInt(oid);
/* 3247 */     if (animation >= 2) {
/* 3248 */       mplew.writeInt(cid);
/* 3249 */       if (animation == 5) {
/* 3250 */         mplew.writeInt(slot);
/*      */       }
/*      */     }
/* 3253 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] spawnMist(MapleMist mist) {
/* 3257 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3259 */     mplew.writeShort(SendPacketOpcode.SPAWN_MIST.getValue());
/* 3260 */     mplew.writeInt(mist.getObjectId());
/* 3261 */     mplew.writeInt(mist.isMobMist() ? 0 : mist.isPoisonMist());
/* 3262 */     mplew.writeInt(mist.getOwnerId());
/* 3263 */     if (mist.getMobSkill() == null)
/* 3264 */       mplew.writeInt(mist.getSourceSkill().getId());
/*      */     else {
/* 3266 */       mplew.writeInt(mist.getMobSkill().getSkillId());
/*      */     }
/* 3268 */     mplew.write(mist.getSkillLevel());
/* 3269 */     mplew.writeShort(mist.getSkillDelay());
/* 3270 */     mplew.writeRect(mist.getBox());
/* 3271 */     mplew.writeLong(0L);
/* 3272 */     mplew.writeInt(0);
/*      */ 
/* 3274 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeMist(int oid, boolean eruption) {
/* 3278 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3280 */     mplew.writeShort(SendPacketOpcode.REMOVE_MIST.getValue());
/* 3281 */     mplew.writeInt(oid);
/* 3282 */     mplew.write(eruption ? 1 : 0);
/*      */ 
/* 3284 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] spawnDoor(int oid, Point pos, boolean animation) {
/* 3288 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3290 */     mplew.writeShort(SendPacketOpcode.SPAWN_DOOR.getValue());
/* 3291 */     mplew.write(animation ? 0 : 1);
/* 3292 */     mplew.writeInt(oid);
/* 3293 */     mplew.writePos(pos);
/*      */ 
/* 3295 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeDoor(int oid, boolean animation) {
/* 3299 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 3300 */     mplew.writeShort(SendPacketOpcode.REMOVE_DOOR.getValue());
/* 3301 */     mplew.write(animation ? 0 : 1);
/* 3302 */     mplew.writeInt(oid);
/*      */ 
/* 3304 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] spawnMechDoor(MechDoor md, boolean animated) {
/* 3308 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3310 */     mplew.writeShort(SendPacketOpcode.MECH_DOOR_SPAWN.getValue());
/* 3311 */     mplew.write(animated ? 0 : 1);
/* 3312 */     mplew.writeInt(md.getOwnerId());
/* 3313 */     mplew.writePos(md.getTruePosition());
/* 3314 */     mplew.write(md.getId());
/* 3315 */     mplew.writeInt(md.getPartyId());
/* 3316 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeMechDoor(MechDoor md, boolean animated) {
/* 3320 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3322 */     mplew.writeShort(SendPacketOpcode.MECH_DOOR_REMOVE.getValue());
/* 3323 */     mplew.write(animated ? 0 : 1);
/* 3324 */     mplew.writeInt(md.getOwnerId());
/* 3325 */     mplew.write(md.getId());
/*      */ 
/* 3327 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] triggerReactor(MapleReactor reactor, int stance) {
/* 3331 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3333 */     mplew.writeShort(SendPacketOpcode.REACTOR_HIT.getValue());
/* 3334 */     mplew.writeInt(reactor.getObjectId());
/* 3335 */     mplew.write(reactor.getState());
/* 3336 */     mplew.writePos(reactor.getTruePosition());
/* 3337 */     mplew.writeInt(stance);
/* 3338 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] spawnReactor(MapleReactor reactor) {
/* 3342 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3344 */     mplew.writeShort(SendPacketOpcode.REACTOR_SPAWN.getValue());
/* 3345 */     mplew.writeInt(reactor.getObjectId());
/* 3346 */     mplew.writeInt(reactor.getReactorId());
/* 3347 */     mplew.write(reactor.getState());
/* 3348 */     mplew.writePos(reactor.getTruePosition());
/* 3349 */     mplew.write(reactor.getFacingDirection());
/* 3350 */     mplew.writeMapleAsciiString(reactor.getName());
/*      */ 
/* 3352 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] destroyReactor(MapleReactor reactor) {
/* 3356 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3358 */     mplew.writeShort(SendPacketOpcode.REACTOR_DESTROY.getValue());
/* 3359 */     mplew.writeInt(reactor.getObjectId());
/* 3360 */     mplew.write(reactor.getState());
/* 3361 */     mplew.writePos(reactor.getPosition());
/*      */ 
/* 3363 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] makeExtractor(int cid, String cname, Point pos, int timeLeft, int itemId, int fee) {
/* 3367 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3369 */     mplew.writeShort(SendPacketOpcode.SPAWN_EXTRACTOR.getValue());
/* 3370 */     mplew.writeInt(cid);
/* 3371 */     mplew.writeMapleAsciiString(cname);
/* 3372 */     mplew.writeInt(pos.x);
/* 3373 */     mplew.writeInt(pos.y);
/* 3374 */     mplew.writeShort(timeLeft);
/* 3375 */     mplew.writeInt(itemId);
/* 3376 */     mplew.writeInt(fee);
/*      */ 
/* 3378 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeExtractor(int cid) {
/* 3382 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3384 */     mplew.writeShort(SendPacketOpcode.REMOVE_EXTRACTOR.getValue());
/* 3385 */     mplew.writeInt(cid);
/* 3386 */     mplew.writeInt(1);
/*      */ 
/* 3388 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] rollSnowball(int type, MapleSnowball.MapleSnowballs ball1, MapleSnowball.MapleSnowballs ball2) {
/* 3392 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3394 */     mplew.writeShort(SendPacketOpcode.ROLL_SNOWBALL.getValue());
/* 3395 */     mplew.write(type);
/* 3396 */     mplew.writeInt(ball1 == null ? 0 : ball1.getSnowmanHP() / 75);
/* 3397 */     mplew.writeInt(ball2 == null ? 0 : ball2.getSnowmanHP() / 75);
/* 3398 */     mplew.writeShort(ball1 == null ? 0 : ball1.getPosition());
/* 3399 */     mplew.write(0);
/* 3400 */     mplew.writeShort(ball2 == null ? 0 : ball2.getPosition());
/* 3401 */     mplew.writeZeroBytes(11);
/*      */ 
/* 3403 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] enterSnowBall() {
/* 3407 */     return rollSnowball(0, null, null);
/*      */   }
/*      */ 
/*      */   public static byte[] hitSnowBall(int team, int damage, int distance, int delay) {
/* 3411 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3413 */     mplew.writeShort(SendPacketOpcode.HIT_SNOWBALL.getValue());
/* 3414 */     mplew.write(team);
/* 3415 */     mplew.writeShort(damage);
/* 3416 */     mplew.write(distance);
/* 3417 */     mplew.write(delay);
/*      */ 
/* 3419 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] snowballMessage(int team, int message) {
/* 3423 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3425 */     mplew.writeShort(SendPacketOpcode.SNOWBALL_MESSAGE.getValue());
/* 3426 */     mplew.write(team);
/* 3427 */     mplew.writeInt(message);
/*      */ 
/* 3429 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] leftKnockBack() {
/* 3433 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3435 */     mplew.writeShort(SendPacketOpcode.LEFT_KNOCK_BACK.getValue());
/*      */ 
/* 3437 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] hitCoconut(boolean spawn, int id, int type) {
/* 3441 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3443 */     mplew.writeShort(SendPacketOpcode.HIT_COCONUT.getValue());
/* 3444 */     mplew.writeInt(spawn ? 32768 : id);
/* 3445 */     mplew.write(spawn ? 0 : type);
/*      */ 
/* 3447 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] coconutScore(int[] coconutscore) {
/* 3451 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3453 */     mplew.writeShort(SendPacketOpcode.COCONUT_SCORE.getValue());
/* 3454 */     mplew.writeShort(coconutscore[0]);
/* 3455 */     mplew.writeShort(coconutscore[1]);
/*      */ 
/* 3457 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateAriantScore(List<MapleCharacter> players) {
/* 3461 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3463 */     mplew.writeShort(SendPacketOpcode.ARIANT_SCORE_UPDATE.getValue());
/* 3464 */     mplew.write(players.size());
/* 3465 */     for (MapleCharacter i : players) {
/* 3466 */       mplew.writeMapleAsciiString(i.getName());
/* 3467 */       mplew.writeInt(0);
/*      */     }
/*      */ 
/* 3470 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sheepRanchInfo(byte wolf, byte sheep) {
/* 3474 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3476 */     mplew.writeShort(SendPacketOpcode.SHEEP_RANCH_INFO.getValue());
/* 3477 */     mplew.write(wolf);
/* 3478 */     mplew.write(sheep);
/*      */ 
/* 3480 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sheepRanchClothes(int cid, byte clothes) {
/* 3484 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3486 */     mplew.writeShort(SendPacketOpcode.SHEEP_RANCH_CLOTHES.getValue());
/* 3487 */     mplew.writeInt(cid);
/* 3488 */     mplew.write(clothes);
/*      */ 
/* 3490 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateWitchTowerKeys(int keys) {
/* 3494 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3496 */     mplew.writeShort(SendPacketOpcode.WITCH_TOWER.getValue());
/* 3497 */     mplew.write(keys);
/*      */ 
/* 3499 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showChaosZakumShrine(boolean spawned, int time) {
/* 3503 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3505 */     mplew.writeShort(SendPacketOpcode.CHAOS_ZAKUM_SHRINE.getValue());
/* 3506 */     mplew.write(spawned ? 1 : 0);
/* 3507 */     mplew.writeInt(time);
/*      */ 
/* 3509 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showChaosHorntailShrine(boolean spawned, int time) {
/* 3513 */     return showHorntailShrine(spawned, time);
/*      */   }
/*      */ 
/*      */   public static byte[] showHorntailShrine(boolean spawned, int time) {
/* 3517 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3519 */     mplew.writeShort(SendPacketOpcode.HORNTAIL_SHRINE.getValue());
/* 3520 */     mplew.write(spawned ? 1 : 0);
/* 3521 */     mplew.writeInt(time);
/*      */ 
/* 3523 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getRPSMode(byte mode, int mesos, int selection, int answer) {
/* 3527 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3529 */     mplew.writeShort(SendPacketOpcode.RPS_GAME.getValue());
/* 3530 */     mplew.write(mode);
/* 3531 */     switch (mode) {
/*      */     case 6:
/* 3533 */       if (mesos == -1) break;
/* 3534 */       mplew.writeInt(mesos); break;
/*      */     case 8:
/* 3539 */       mplew.writeInt(9000019);
/* 3540 */       break;
/*      */     case 11:
/* 3543 */       mplew.write(selection);
/* 3544 */       mplew.write(answer);
/*      */     }
/*      */ 
/* 3548 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] messengerInvite(String from, int messengerid) {
/* 3552 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3554 */     mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
/* 3555 */     mplew.write(3);
/* 3556 */     mplew.writeMapleAsciiString(from);
/* 3557 */     mplew.write(0);
/* 3558 */     mplew.writeInt(messengerid);
/* 3559 */     mplew.write(0);
/*      */ 
/* 3561 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] addMessengerPlayer(String from, MapleCharacter chr, int position, int channel) {
/* 3565 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3567 */     mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
/* 3568 */     mplew.write(0);
/* 3569 */     mplew.write(position);
/* 3570 */     PacketHelper.addCharLook(mplew, chr, true);
/* 3571 */     mplew.writeMapleAsciiString(from);
/* 3572 */     mplew.write(channel);
/* 3573 */     mplew.write(0);
/*      */ 
/* 3575 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeMessengerPlayer(int position) {
/* 3579 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3581 */     mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
/* 3582 */     mplew.write(2);
/* 3583 */     mplew.write(position);
/*      */ 
/* 3585 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateMessengerPlayer(String from, MapleCharacter chr, int position, int channel) {
/* 3589 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3591 */     mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
/* 3592 */     mplew.write(7);
/* 3593 */     mplew.write(position);
/* 3594 */     PacketHelper.addCharLook(mplew, chr, true);
/* 3595 */     mplew.writeMapleAsciiString(from);
/* 3596 */     mplew.writeShort(channel);
/*      */ 
/* 3598 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] joinMessenger(int position) {
/* 3602 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3604 */     mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
/* 3605 */     mplew.write(1);
/* 3606 */     mplew.write(position);
/*      */ 
/* 3608 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] messengerChat(String charname, String text) {
/* 3612 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3614 */     mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
/* 3615 */     mplew.write(6);
/* 3616 */     mplew.writeMapleAsciiString(charname);
/* 3617 */     mplew.writeMapleAsciiString(text);
/*      */ 
/* 3619 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] messengerNote(String text, int mode, int mode2) {
/* 3623 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3625 */     mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
/* 3626 */     mplew.write(mode);
/* 3627 */     mplew.writeMapleAsciiString(text);
/* 3628 */     mplew.write(mode2);
/*      */ 
/* 3630 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] removeItemFromDuey(boolean remove, int Package) {
/* 3634 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3636 */     mplew.writeShort(SendPacketOpcode.DUEY.getValue());
/* 3637 */     mplew.write(24);
/* 3638 */     mplew.writeInt(Package);
/* 3639 */     mplew.write(remove ? 3 : 4);
/*      */ 
/* 3641 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendDuey(byte operation, List<MapleDueyActions> packages) {
/* 3645 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3647 */     mplew.writeShort(SendPacketOpcode.DUEY.getValue());
/* 3648 */     mplew.write(operation);
/*      */ 
/* 3650 */     switch (operation) {
/*      */     case 9:
/* 3652 */       mplew.write(1);
/*      */ 
/* 3654 */       break;
/*      */     case 10:
/* 3657 */       mplew.write(0);
/* 3658 */       mplew.write(packages.size());
/*      */ 
/* 3660 */       for (MapleDueyActions dp : packages) {
/* 3661 */         mplew.writeInt(dp.getPackageId());
/* 3662 */         mplew.writeAsciiString(dp.getSender(), 13);
/* 3663 */         mplew.writeInt(dp.getMesos());
/* 3664 */         mplew.writeLong(PacketHelper.getTime(dp.getSentTime()));
/* 3665 */         mplew.writeZeroBytes(205);
/*      */ 
/* 3667 */         if (dp.getItem() != null) {
/* 3668 */           mplew.write(1);
/* 3669 */           PacketHelper.addItemInfo(mplew, dp.getItem());
/*      */         } else {
/* 3671 */           mplew.write(0);
/*      */         }
/*      */       }
/*      */ 
/* 3675 */       mplew.write(0);
/*      */     }
/*      */ 
/* 3679 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getKeymap(MapleKeyLayout layout) {
/* 3683 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3685 */     mplew.writeShort(SendPacketOpcode.KEYMAP.getValue());
/*      */ 
/* 3687 */     layout.writeData(mplew);
/*      */ 
/* 3689 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] petAutoHP(int itemId) {
/* 3693 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3695 */     mplew.writeShort(SendPacketOpcode.PET_AUTO_HP.getValue());
/* 3696 */     mplew.writeInt(itemId);
/*      */ 
/* 3698 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] petAutoMP(int itemId) {
/* 3702 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3704 */     mplew.writeShort(SendPacketOpcode.PET_AUTO_MP.getValue());
/* 3705 */     mplew.writeInt(itemId);
/*      */ 
/* 3707 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */  
    public static void addRingInfo(MaplePacketLittleEndianWriter mplew, List<MapleRing> rings) {
        mplew.write(rings.size());
        for (MapleRing ring : rings) {
            mplew.writeInt(1);
            mplew.writeLong(ring.getRingId());
            mplew.writeLong(ring.getPartnerRingId());
            mplew.writeInt(ring.getItemId());
        }
    }

    public static void addMRingInfo(MaplePacketLittleEndianWriter mplew, List<MapleRing> rings, MapleCharacter chr) {
        mplew.write(rings.size());
        for (MapleRing ring : rings) {
            mplew.writeInt(1);
            mplew.writeInt(chr.getId());
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeInt(ring.getItemId());
        }
    }

/*      */ 
/*      */   public static byte[] getBuffBar(long millis) {
/* 3729 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3731 */     mplew.writeShort(SendPacketOpcode.BUFF_BAR.getValue());
/* 3732 */     mplew.writeLong(millis);
/*      */ 
/* 3734 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getBoosterFamiliar(int cid, int familiar, int id) {
/* 3738 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3740 */     mplew.writeShort(SendPacketOpcode.BOOSTER_FAMILIAR.getValue());
/* 3741 */     mplew.writeInt(cid);
/* 3742 */     mplew.writeInt(familiar);
/* 3743 */     mplew.writeLong(id);
/* 3744 */     mplew.write(0);
/*      */ 
/* 3746 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   74 */     DEFAULT_BUFFMASK |= MapleBuffStat.ENERGY_CHARGE.getValue();
/*   75 */     DEFAULT_BUFFMASK |= MapleBuffStat.DASH_SPEED.getValue();
/*   76 */     DEFAULT_BUFFMASK |= MapleBuffStat.DASH_JUMP.getValue();
/*   77 */     DEFAULT_BUFFMASK |= MapleBuffStat.MONSTER_RIDING.getValue();
/*   78 */     DEFAULT_BUFFMASK |= MapleBuffStat.SPEED_INFUSION.getValue();
/*   79 */     DEFAULT_BUFFMASK |= MapleBuffStat.HOMING_BEACON.getValue();
/*   80 */     DEFAULT_BUFFMASK |= MapleBuffStat.DEFAULT_BUFFSTAT.getValue();
/*      */   }

public static byte[] updateCardStack(int total) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

    mplew.writeShort(SendPacketOpcode.PHANTOM_CARD.getValue());
    mplew.write(total);

    return mplew.getPacket();
  }


public static byte[] viewSkills(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TARGET_SKILL.getValue());
        List skillz = new ArrayList();
        for (Skill sk : chr.getSkills().keySet()) {
            if ((sk.canBeLearnedBy(chr.getJob())) && (GameConstants.canSteal(sk)) && (!skillz.contains(Integer.valueOf(sk.getId())))) {
                skillz.add(Integer.valueOf(sk.getId()));
            }
        }
        mplew.write(1);
        mplew.writeInt(chr.getId());
        mplew.writeInt(skillz.isEmpty() ? 2 : 4);
        mplew.writeInt(chr.getJob());
        mplew.writeInt(skillz.size());
        for (Iterator i$ = skillz.iterator(); i$.hasNext();) {
            int i = ((Integer) i$.next()).intValue();
            mplew.writeInt(i);
        }

        return mplew.getPacket();
    }
/*      */ 
/*      */   public static class InteractionPacket
/*      */   {
/*      */     public static byte[] getTradeInvite(MapleCharacter c)
/*      */     {
/* 1282 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1284 */       mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
/* 1285 */       mplew.write(PlayerInteractionHandler.Interaction.INVITE_TRADE.action);
/* 1286 */       mplew.write(3);
/* 1287 */       mplew.writeMapleAsciiString(c.getName());
/* 1288 */       mplew.writeInt(c.getLevel());
/* 1289 */       mplew.writeInt(c.getJob());
/* 1290 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getTradeMesoSet(byte number, int meso) {
/* 1294 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1296 */       mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
/* 1297 */       mplew.write(PlayerInteractionHandler.Interaction.SET_MESO.action);
/* 1298 */       mplew.write(number);
/* 1299 */       mplew.writeInt(meso);
/*      */ 
/* 1301 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getTradeItemAdd(byte number, Item item) {
/* 1305 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1307 */       mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
/* 1308 */       mplew.write(PlayerInteractionHandler.Interaction.SET_ITEMS.action);
/* 1309 */       mplew.write(number);
/* 1310 */       mplew.write(item.getPosition());
/* 1311 */       PacketHelper.addItemInfo(mplew, item);
/*      */ 
/* 1313 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getTradeStart(MapleClient c, MapleTrade trade, byte number) {
/* 1317 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1319 */       mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
/* 1320 */       mplew.write(10);
/* 1321 */       mplew.write(3);
/* 1322 */       mplew.write(2);
/* 1323 */       mplew.write(number);
/*      */ 
/* 1325 */       if (number == 1) {
/* 1326 */         mplew.write(0);
/* 1327 */         PacketHelper.addCharLook(mplew, trade.getPartner().getChr(), false);
/* 1328 */         mplew.writeMapleAsciiString(trade.getPartner().getChr().getName());
/* 1329 */         mplew.writeShort(trade.getPartner().getChr().getJob());
/*      */       }
/* 1331 */       mplew.write(number);
/* 1332 */       PacketHelper.addCharLook(mplew, c.getPlayer(), false);
/* 1333 */       mplew.writeMapleAsciiString(c.getPlayer().getName());
/* 1334 */       mplew.writeShort(c.getPlayer().getJob());
/* 1335 */       mplew.write(255);
/*      */ 
/* 1337 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getTradeConfirmation() {
/* 1341 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1343 */       mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
/* 1344 */       mplew.write(PlayerInteractionHandler.Interaction.CONFIRM_TRADE.action);
/*      */ 
/* 1346 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] TradeMessage(byte UserSlot, byte message) {
/* 1350 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1352 */       mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
/* 1353 */       mplew.write(GameConstants.GMS ? 18 : 10);
/* 1354 */       mplew.write(UserSlot);
/* 1355 */       mplew.write(message);
/*      */ 
/* 1362 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getTradeCancel(byte UserSlot, int unsuccessful) {
/* 1366 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1368 */       mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
/* 1369 */       mplew.write(PlayerInteractionHandler.Interaction.EXIT.action);
/* 1370 */       mplew.write(UserSlot);
/* 1371 */       mplew.write(2);
/*      */ 
/* 1373 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class NPCPacket
/*      */   {
/*      */     public static byte[] spawnNPC(MapleNPC life, boolean show)
/*      */     {
/*  770 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  772 */       mplew.writeShort(SendPacketOpcode.SPAWN_NPC.getValue());
/*  773 */       mplew.writeInt(life.getObjectId());
/*  774 */       mplew.writeInt(life.getId());
/*  775 */       mplew.writeShort(life.getPosition().x);
/*  776 */       mplew.writeShort(life.getCy());
/*  777 */       mplew.write(life.getF() == 1 ? 0 : 1);
/*  778 */       mplew.writeShort(life.getFh());
/*  779 */       mplew.writeShort(life.getRx0());
/*  780 */       mplew.writeShort(life.getRx1());
/*  781 */       mplew.write(show ? 1 : 0);
/*      */ 
/*  783 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] removeNPC(int objectid) {
/*  787 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  789 */       mplew.writeShort(SendPacketOpcode.REMOVE_NPC.getValue());
/*  790 */       mplew.writeInt(objectid);
/*      */ 
/*  792 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] removeNPCController(int objectid) {
/*  796 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  798 */       mplew.writeShort(SendPacketOpcode.SPAWN_NPC_REQUEST_CONTROLLER.getValue());
/*  799 */       mplew.write(0);
/*  800 */       mplew.writeInt(objectid);
/*      */ 
/*  802 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] spawnNPCRequestController(MapleNPC life, boolean MiniMap) {
/*  806 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  808 */       mplew.writeShort(SendPacketOpcode.SPAWN_NPC_REQUEST_CONTROLLER.getValue());
/*  809 */       mplew.write(1);
/*  810 */       mplew.writeInt(life.getObjectId());
/*  811 */       mplew.writeInt(life.getId());
/*  812 */       mplew.writeShort(life.getPosition().x);
/*  813 */       mplew.writeShort(life.getCy());
/*  814 */       mplew.write(life.getF() == 1 ? 0 : 1);
/*  815 */       mplew.writeShort(life.getFh());
/*  816 */       mplew.writeShort(life.getRx0());
/*  817 */       mplew.writeShort(life.getRx1());
/*  818 */       mplew.write(MiniMap ? 1 : 0);
/*      */ 
/*  820 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] setNPCScriptable(List<Pair<Integer, String>> npcs) {
/*  824 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*  825 */       mplew.writeShort(SendPacketOpcode.NPC_SCRIPTABLE.getValue());
/*  826 */       mplew.write(npcs.size());
/*  827 */       for (Pair s : npcs) {
/*  828 */         mplew.writeInt(((Integer)s.left).intValue());
/*  829 */         mplew.writeMapleAsciiString((String)s.right);
/*  830 */         mplew.writeInt(0);
/*  831 */         mplew.writeInt(2147483647);
/*      */       }
/*  833 */       return mplew.getPacket();
/*      */     }
/*      */ 
        public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type) {
            return getNPCTalk(npc, msgType, talk, endBytes, type, npc);
        }

        public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type, int diffNPC) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4);
            mplew.writeInt(npc);
            mplew.write(msgType);
            mplew.write(type); // mask; 1 = no ESC, 2 = playerspeaks, 4 = diff NPC 8 = something, ty KDMS
            if ((type & 0x4) != 0) {
                mplew.writeInt(diffNPC);
            }
            mplew.writeMapleAsciiString(talk);
            mplew.write(HexTool.getByteArrayFromHexString(endBytes));

            return mplew.getPacket();
        }


/*      */ 
/*      */     public static byte[] getMapSelection(int npcid, String sel) {
/*  858 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  860 */       mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
/*  861 */       mplew.write(4);
/*  862 */       mplew.writeInt(npcid);
/*  863 */       mplew.writeShort(GameConstants.GMS ? 17 : 16);
/*  864 */       mplew.writeInt(npcid == 2083006 ? 1 : 0);
/*  865 */       mplew.writeInt(npcid == 9010022 ? 1 : 0);
/*  866 */       mplew.writeMapleAsciiString(sel);
/*      */ 
/*  868 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getNPCTalkStyle(int npc, String talk, int[] args) {
/*  872 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  874 */       mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
/*  875 */       mplew.write(4);
/*  876 */       mplew.writeInt(npc);
/*  877 */       mplew.writeShort(9);
/*  878 */       mplew.writeMapleAsciiString(talk);
/*  879 */       mplew.write(args.length);
/*      */ 
/*  881 */       for (int i = 0; i < args.length; i++) {
/*  882 */         mplew.writeInt(args[i]);
/*      */       }
/*  884 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getNPCTalkNum(int npc, String talk, int def, int min, int max) {
/*  888 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  890 */       mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
/*  891 */       mplew.write(4);
/*  892 */       mplew.writeInt(npc);
/*  893 */       mplew.writeShort(4);
/*  894 */       mplew.writeMapleAsciiString(talk);
/*  895 */       mplew.writeInt(def);
/*  896 */       mplew.writeInt(min);
/*  897 */       mplew.writeInt(max);
/*  898 */       mplew.writeInt(0);
/*      */ 
/*  900 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getNPCTalkText(int npc, String talk) {
/*  904 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  906 */       mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
/*  907 */       mplew.write(4);
/*  908 */       mplew.writeInt(npc);
/*  909 */       mplew.writeShort(3);
/*  910 */       mplew.writeMapleAsciiString(talk);
/*  911 */       mplew.writeInt(0);
/*  912 */       mplew.writeInt(0);
/*      */ 
/*  914 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getSelfTalkText(String text)
/*      */     {
/*  919 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*  920 */       mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
/*  921 */       mplew.write(3);
/*  922 */       mplew.writeInt(0);
/*  923 */       mplew.write(0);
/*  924 */       mplew.write(17);
/*  925 */       mplew.writeMapleAsciiString(text);
/*  926 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getNPCTutoEffect(String effect) {
/*  930 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*  931 */       mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
/*  932 */       mplew.write(3);
/*  933 */       mplew.writeInt(0);
/*  934 */       mplew.write(0);
/*  935 */       mplew.write(1);
/*  936 */       mplew.writeShort(257);
/*  937 */       mplew.writeMapleAsciiString(effect);
/*  938 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getEvanTutorial(String data) {
/*  942 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  944 */       mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
/*      */ 
/*  946 */       mplew.write(8);
/*  947 */       mplew.writeInt(0);
/*  948 */       mplew.write(1);
/*  949 */       mplew.write(1);
/*  950 */       mplew.write(1);
/*  951 */       mplew.writeMapleAsciiString(data);
/*      */ 
/*  953 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getNPCShop(int sid, MapleShop shop, MapleClient c) {
/*  957 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  960 */       mplew.writeShort(SendPacketOpcode.OPEN_NPC_SHOP.getValue());
/*  961 */       mplew.writeInt(0);
/*  962 */       mplew.writeInt(sid);
/*  963 */       PacketHelper.addShopInfo(mplew, shop, c);
/*      */ 
/*  965 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] confirmShopTransaction(byte code, MapleShop shop, MapleClient c, int indexBought) {
/*  969 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  971 */       mplew.writeShort(SendPacketOpcode.CONFIRM_SHOP_TRANSACTION.getValue());
/*  972 */       mplew.write(code);
/*  973 */       if (code == 4) {
/*  974 */         mplew.writeInt(0);
/*  975 */         mplew.writeInt(shop.getNpcId());
/*  976 */         PacketHelper.addShopInfo(mplew, shop, c);
/*      */       } else {
/*  978 */         mplew.write(indexBought >= 0 ? 1 : 0);
/*  979 */         if (indexBought >= 0) {
/*  980 */           mplew.writeInt(indexBought);
/*      */         }
/*      */       }
/*      */ 
/*  984 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getStorage(int npcId, byte slots, Collection<Item> items, int meso) {
/*  988 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  990 */       mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
/*  991 */       mplew.write(22);
/*  992 */       mplew.writeInt(npcId);
/*  993 */       mplew.write(slots);
/*  994 */       mplew.writeShort(126);
/*  995 */       mplew.writeShort(0);
/*  996 */       mplew.writeInt(0);
/*  997 */       mplew.writeInt(meso);
/*  998 */       mplew.writeShort(0);
/*  999 */       mplew.write((byte)items.size());
/* 1000 */       for (Item item : items) {
/* 1001 */         PacketHelper.addItemInfo(mplew, item);
/*      */       }
/* 1003 */       mplew.writeShort(0);
/* 1004 */       mplew.write(0);
/*      */ 
/* 1006 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getStorageFull() {
/* 1010 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1012 */       mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
/* 1013 */       mplew.write(17);
/*      */ 
/* 1015 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] mesoStorage(byte slots, int meso) {
/* 1019 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1021 */       mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
/* 1022 */       mplew.write(19);
/* 1023 */       mplew.write(slots);
/* 1024 */       mplew.writeShort(2);
/* 1025 */       mplew.writeShort(0);
/* 1026 */       mplew.writeInt(0);
/* 1027 */       mplew.writeInt(meso);
/*      */ 
/* 1029 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] arrangeStorage(byte slots, Collection<Item> items, boolean changed) {
/* 1033 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1035 */       mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
/* 1036 */       mplew.write(15);
/* 1037 */       mplew.write(slots);
/* 1038 */       mplew.write(124);
/* 1039 */       mplew.writeZeroBytes(10);
/* 1040 */       mplew.write(items.size());
/* 1041 */       for (Item item : items) {
/* 1042 */         PacketHelper.addItemInfo(mplew, item);
/*      */       }
/* 1044 */       mplew.write(0);
/* 1045 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] storeStorage(byte slots, MapleInventoryType type, Collection<Item> items) {
/* 1049 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1051 */       mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
/* 1052 */       mplew.write(13);
/* 1053 */       mplew.write(slots);
/* 1054 */       mplew.writeShort(type.getBitfieldEncoding());
/* 1055 */       mplew.writeShort(0);
/* 1056 */       mplew.writeInt(0);
/* 1057 */       mplew.write(items.size());
/* 1058 */       for (Item item : items) {
/* 1059 */         PacketHelper.addItemInfo(mplew, item);
/*      */       }
/* 1061 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] takeOutStorage(byte slots, MapleInventoryType type, Collection<Item> items) {
/* 1065 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1067 */       mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
/* 1068 */       mplew.write(9);
/* 1069 */       mplew.write(slots);
/* 1070 */       mplew.writeShort(type.getBitfieldEncoding());
/* 1071 */       mplew.writeShort(0);
/* 1072 */       mplew.writeInt(0);
/* 1073 */       mplew.write(items.size());
/* 1074 */       for (Item item : items) {
/* 1075 */         PacketHelper.addItemInfo(mplew, item);
/*      */       }
/* 1077 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class SummonPacket
/*      */   {
/*      */     public static byte[] spawnSummon(MapleSummon summon, boolean animated)
/*      */     {
/*  609 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  611 */       mplew.writeShort(SendPacketOpcode.SPAWN_SUMMON.getValue());
/*  612 */       mplew.writeInt(summon.getOwnerId());
/*  613 */       mplew.writeInt(summon.getObjectId());
/*  614 */       mplew.writeInt(summon.getSkill());
/*  615 */       mplew.write(summon.getOwnerLevel() - 1);
/*  616 */       mplew.write(summon.getSkillLevel());
/*  617 */       mplew.writePos(summon.getPosition());
/*  618 */       mplew.write((summon.getSkill() == 32111006) || (summon.getSkill() == 33101005) ? 5 : 4);
/*  619 */       if ((summon.getSkill() == 35121003) && (summon.getOwner().getMap() != null))
/*  620 */         mplew.writeShort(summon.getOwner().getMap().getFootholds().findBelow(summon.getPosition()).getId());
/*      */       else {
/*  622 */         mplew.writeShort(0);
/*      */       }
/*  624 */       mplew.write(summon.getMovementType().getValue());
/*  625 */       mplew.write(summon.getSummonType());
/*  626 */       mplew.write(animated ? 1 : 0);
/*  627 */       mplew.write(1);
/*  628 */       MapleCharacter chr = summon.getOwner();
/*  629 */       mplew.write((summon.getSkill() == 4341006) && (chr != null) ? 1 : 0);
/*  630 */       if ((summon.getSkill() == 4341006) && (chr != null)) {
/*  631 */         PacketHelper.addCharLook(mplew, chr, true);
/*      */       }
/*  633 */       if (summon.getSkill() == 35111002) {
/*  634 */         mplew.write(0);
/*      */       }
/*      */ 
/*  637 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] removeSummon(int ownerId, int objId) {
/*  641 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  643 */       mplew.writeShort(SendPacketOpcode.REMOVE_SUMMON.getValue());
/*  644 */       mplew.writeInt(ownerId);
/*  645 */       mplew.writeInt(objId);
/*  646 */       mplew.write(10);
/*      */ 
/*  648 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] removeSummon(MapleSummon summon, boolean animated) {
/*  652 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  654 */       mplew.writeShort(SendPacketOpcode.REMOVE_SUMMON.getValue());
/*  655 */       mplew.writeInt(summon.getOwnerId());
/*  656 */       mplew.writeInt(summon.getObjectId());
/*  657 */       if (animated)
/*  658 */         switch (summon.getSkill()) {
/*      */         case 35121003:
/*  660 */           mplew.write(10);
/*  661 */           break;
/*      */         case 33101008:
/*      */         case 35111001:
/*      */         case 35111002:
/*      */         case 35111005:
/*      */         case 35111009:
/*      */         case 35111010:
/*      */         case 35111011:
/*      */         case 35121009:
/*      */         case 35121010:
/*      */         case 35121011:
/*  672 */           mplew.write(5);
/*  673 */           break;
/*      */         default:
/*  675 */           mplew.write(4);
/*  676 */           break;
/*      */         }
/*      */       else {
/*  679 */         mplew.write(1);
/*      */       }
/*      */ 
/*  682 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] moveSummon(int cid, int oid, Point startPos, List<LifeMovementFragment> moves) {
/*  686 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  688 */       mplew.writeShort(SendPacketOpcode.MOVE_SUMMON.getValue());
/*  689 */       mplew.writeInt(cid);
/*  690 */       mplew.writeInt(oid);
/*  691 */       mplew.writePos(startPos);
/*  692 */       mplew.writeInt(0);
/*  693 */       PacketHelper.serializeMovementList(mplew, moves);
/*      */ 
/*  695 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] summonAttack(int cid, int summonSkillId, byte animation, List<Pair<Integer, Integer>> allDamage, int level, boolean darkFlare) {
/*  699 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  701 */       mplew.writeShort(SendPacketOpcode.SUMMON_ATTACK.getValue());
/*  702 */       mplew.writeInt(cid);
/*  703 */       mplew.writeInt(summonSkillId);
/*  704 */       mplew.write(level - 1);
/*  705 */       mplew.write(animation);
/*  706 */       mplew.write(allDamage.size());
/*  707 */       for (Pair attackEntry : allDamage) {
/*  708 */         mplew.writeInt(((Integer)attackEntry.left).intValue());
/*  709 */         mplew.write(7);
/*  710 */         mplew.writeInt(((Integer)attackEntry.right).intValue());
/*      */       }
/*  712 */       mplew.write(darkFlare ? 1 : 0);
/*      */ 
/*  714 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] pvpSummonAttack(int cid, int playerLevel, int oid, int animation, Point pos, List<AttackPair> attack) {
/*  718 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  720 */       mplew.writeShort(SendPacketOpcode.PVP_SUMMON.getValue());
/*  721 */       mplew.writeInt(cid);
/*  722 */       mplew.writeInt(oid);
/*  723 */       mplew.write(playerLevel);
/*  724 */       mplew.write(animation);
/*  725 */       mplew.writePos(pos);
/*  726 */       mplew.writeInt(0);
/*  727 */       mplew.write(attack.size());
/*  728 */       for (AttackPair p : attack) {
/*  729 */         mplew.writeInt(p.objectid);
/*  730 */         mplew.writePos(p.point);
/*  731 */         mplew.write(p.attack.size());
/*  732 */         mplew.write(0);
/*  733 */         for (Pair atk : p.attack) {
/*  734 */           mplew.writeInt(((Integer)atk.left).intValue());
/*      */         }
/*      */       }
/*      */ 
/*  738 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] summonSkill(int cid, int summonSkillId, int newStance) {
/*  742 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  744 */       mplew.writeShort(SendPacketOpcode.SUMMON_SKILL.getValue());
/*  745 */       mplew.writeInt(cid);
/*  746 */       mplew.writeInt(summonSkillId);
/*  747 */       mplew.write(newStance);
/*      */ 
/*  749 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] damageSummon(int cid, int summonSkillId, int damage, int unkByte, int monsterIdFrom) {
/*  753 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  755 */       mplew.writeShort(SendPacketOpcode.DAMAGE_SUMMON.getValue());
/*  756 */       mplew.writeInt(cid);
/*  757 */       mplew.writeInt(summonSkillId);
/*  758 */       mplew.write(unkByte);
/*  759 */       mplew.writeInt(damage);
/*  760 */       mplew.writeInt(monsterIdFrom);
/*  761 */       mplew.write(0);
/*      */ 
/*  763 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class UIPacket
/*      */   {
/*      *///     public static byte[] getDirectionStatus(boolean enable)
/*      *///     {
/*  422 *///       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */// 
/*  424 *///       mplew.writeShort(SendPacketOpcode.DIRECTION_STATUS.getValue());
/*  425 *///       mplew.write(enable ? 1 : 0);
/*      */// 
/*  427 */ //      return mplew.getPacket();
/*      *///     }

               public static byte[] getDirectionStatus(boolean enable) {
                   MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
                   mplew.writeShort(SendPacketOpcode.DIRECTION_STATUS.getValue());
                   mplew.write(enable ? 1 : 0);
                   
                   return mplew.getPacket();
               }
               public static byte[] IntroEnableUI(int wtf) {
                   MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
                   
                   mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_ENABLE_UI.getValue());
                   mplew.write(wtf > 0 ? 1 : 0);
                   if (wtf > 0) {
                       mplew.writeShort(wtf);
                   }
                   return mplew.getPacket();
               }
/*      */ 
/*      */     public static byte[] openUI(int type) {
/*  431 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
/*      */ 
/*  473 */       mplew.writeShort(SendPacketOpcode.OPEN_UI.getValue());
/*  474 */       mplew.write(type);
/*      */ 
/*  476 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] sendRepairWindow(int npc) {
/*  480 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);
/*      */ 
/*  484 */       mplew.writeShort(SendPacketOpcode.OPEN_UI_OPTION.getValue());
/*  485 */       mplew.writeInt(33);
/*  486 */       mplew.writeInt(npc);
/*      */ 
/*  488 */       return mplew.getPacket();
/*      */     }

           public static byte[] startAzwan(int npc) {
             MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);
             mplew.writeShort(SendPacketOpcode.ENTER_AZWAN.getValue());
             mplew.write(HexTool.getByteArrayFromHexString("01 61 63 FF 01 01 7F 96 98 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 C0 27 00 00 00 00 00 88 00 00 00 00 00 00 00 00 47 80 00 00 00 00 00 00 00 00 47 80 00 00 00 00 00 00 00 00 47 80 00 00 00 00 00 00 00 00 47 80 00 00 00 00 00 4A 0A 84 00 04 08 00 08 00 FF FF 0F 00 00 00 00 00 00 00 00 00 00 00 FF"));
             return mplew.getPacket();
           }


            public static byte[] sendRedLeaf(int npc) {
/*  480 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);
/*      */ 
/*  484 */       mplew.writeShort(SendPacketOpcode.OPEN_UI_OPTION.getValue());
/*  485 */       mplew.writeInt(66); // 70 azwan
/*  486 */       mplew.writeInt(npc);
/*      */ 
/*  488 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] IntroLock(boolean enable) {
/*  492 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  494 */       mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_LOCK.getValue());
/*  495 */       mplew.write(enable ? 1 : 0);
/*  496 */       mplew.writeInt(0);
/*      */ 
/*  498 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      *///     public static byte[] IntroEnableUI(int wtf) {
/*  502 *///       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */// 
/*  504 *///       mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_ENABLE_UI.getValue());
/*  505 *///       mplew.write(wtf > 0 ? 1 : 0);
/*  506 *///       if (wtf > 0) {
/*  507 *///         mplew.writeShort(wtf);
/*      *///       }
/*      */// 
/*  510 *///       return mplew.getPacket();
/*      *///     }
/*      */ 
/*      */     public static byte[] IntroDisableUI(boolean enable) {
/*  514 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  516 */       mplew.writeShort(SendPacketOpcode.CYGNUS_INTRO_DISABLE_UI.getValue());
/*  517 */       mplew.write(enable ? 1 : 0);
/*      */ 
/*  519 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] summonHelper(boolean summon) {
/*  523 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  525 */       mplew.writeShort(SendPacketOpcode.SUMMON_HINT.getValue());
/*  526 */       mplew.write(summon ? 1 : 0);
/*      */ 
/*  528 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] summonMessage(int type) {
/*  532 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  534 */       mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
/*  535 */       mplew.write(1);
/*  536 */       mplew.writeInt(type);
/*  537 */       mplew.writeInt(7000);
/*      */ 
/*  539 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] summonMessage(String message) {
/*  543 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  545 */       mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
/*  546 */       mplew.write(0);
/*  547 */       mplew.writeMapleAsciiString(message);
/*  548 */       mplew.writeInt(200);
/*  549 */       mplew.writeShort(0);
/*  550 */       mplew.writeInt(10000);
/*      */ 
/*  552 */       return mplew.getPacket();
/*      */     }

               public static byte[] getDirectionInfoTest(byte type, int value) {
                   MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
                   mplew.writeShort(SendPacketOpcode.DIRECTION_INFO.getValue());
                   mplew.write(type);
                   mplew.writeInt(value);
                   return mplew.getPacket();
               }

/*      */ 
/*      */     public static byte[] getDirectionInfo(int type, int value) {
/*  556 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  558 */       mplew.writeShort(SendPacketOpcode.DIRECTION_INFO.getValue());
/*  559 */       mplew.write(type);
/*  560 */       mplew.writeLong(value);
/*      */ 
/*  562 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getDirectionInfo(String data, int value, int x, int y, int pro) {
/*  566 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  568 */       mplew.writeShort(SendPacketOpcode.DIRECTION_INFO.getValue());
/*  569 */       mplew.write(2);
/*  570 */       mplew.writeMapleAsciiString(data);
/*  571 */       mplew.writeInt(value);
/*  572 */       mplew.writeInt(x);
/*  573 */       mplew.writeInt(y);
/*  574 */       mplew.writeShort(pro);
                 if (pro > 0) {
/*  575 */       mplew.writeInt(0);
                 }
/*      */ 
/*  577 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] reissueMedal(int itemId, int type) {
/*  581 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  583 */       mplew.writeShort(SendPacketOpcode.REISSUE_MEDAL.getValue());
/*  584 */       mplew.write(type);
/*  585 */       mplew.writeInt(itemId);
/*      */ 
/*  592 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static final byte[] playMovie(String data, boolean show) {
/*  596 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  598 */       mplew.writeShort(SendPacketOpcode.PLAY_MOVIE.getValue());
/*  599 */       mplew.writeMapleAsciiString(data);
/*  600 */       mplew.write(show ? 1 : 0);
/*      */ 
/*  602 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class EffectPacket
/*      */   {
/*      */     public static byte[] showForeignEffect(int effect)
/*      */     {
/*  145 */       return showForeignEffect(-1, effect);
/*      */     }
/*      */ 
/*      */     public static byte[] showForeignEffect(int cid, int effect) {
/*  149 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  151 */       if (cid == -1) {
/*  152 */         mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*      */       } else {
/*  154 */         mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
/*  155 */         mplew.writeInt(cid);
/*      */       }
/*  157 */       mplew.write(effect);
/*      */ 
/*  159 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showItemLevelupEffect() {
/*  163 */       return showForeignEffect(18);
/*      */     }
/*      */ 
/*      */     public static byte[] showForeignItemLevelupEffect(int cid) {
/*  167 */       return showForeignEffect(cid, 18);
/*      */     }
/*      */ 
/*      */     public static byte[] showOwnDiceEffect(int skillid, int effectid, int effectid2, int level) {
/*  171 */       return showDiceEffect(-1, skillid, effectid, effectid2, level);
/*      */     }
/*      */ 
/*      */     public static byte[] showDiceEffect(int cid, int skillid, int effectid, int effectid2, int level) {
/*  175 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  177 */       if (cid == -1) {
/*  178 */         mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*      */       } else {
/*  180 */         mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
/*  181 */         mplew.writeInt(cid);
/*      */       }
/*  183 */       mplew.write(3);
/*  184 */       mplew.writeInt(effectid);
/*  185 */       mplew.writeInt(effectid2);
/*  186 */       mplew.writeInt(skillid);
/*  187 */       mplew.write(level);
/*  188 */       mplew.write(0);
/*      */ 
/*  190 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] useCharm(byte charmsleft, byte daysleft, boolean safetyCharm) {
/*  194 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  198 */       mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*  199 */       mplew.write(8);
/*  200 */       mplew.write(safetyCharm ? 1 : 0);
/*  201 */       mplew.write(charmsleft);
/*  202 */       mplew.write(daysleft);
/*  203 */       if (!safetyCharm) {
/*  204 */         mplew.writeInt(0);
/*      */       }
/*      */ 
/*  207 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] Mulung_DojoUp2() {
/*  211 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  213 */       mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*  214 */       mplew.write(10);
/*      */ 
/*  216 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showOwnHpHealed(int amount) {
/*  220 */       return showHpHealed(-1, amount);
/*      */     }
/*      */ 
/*      */     public static byte[] showHpHealed(int cid, int amount) {
/*  224 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  226 */       if (cid == -1) {
/*  227 */         mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*      */       } else {
/*  229 */         mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
/*  230 */         mplew.writeInt(cid);
/*      */       }
/*  232 */       mplew.write(30);
/*  233 */       mplew.writeInt(amount);
/*      */ 
/*  235 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showRewardItemAnimation(int itemId, String effect) {
/*  239 */       return showRewardItemAnimation(itemId, effect, -1);
/*      */     }
/*      */ 
/*      */     public static byte[] showRewardItemAnimation(int itemId, String effect, int from_playerid) {
/*  243 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  245 */       if (from_playerid == -1) {
/*  246 */         mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*      */       } else {
/*  248 */         mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
/*  249 */         mplew.writeInt(from_playerid);
/*      */       }
/*  251 */       mplew.write(17);
/*  252 */       mplew.writeInt(itemId);
/*  253 */       mplew.write((effect != null) && (effect.length() > 0) ? 1 : 0);
/*  254 */       if ((effect != null) && (effect.length() > 0)) {
/*  255 */         mplew.writeMapleAsciiString(effect);
/*      */       }
/*      */ 
/*  258 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showCashItemEffect(int itemId) {
/*  262 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  264 */       mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*  265 */       mplew.write(23);
/*  266 */       mplew.writeInt(itemId);
/*      */ 
/*  268 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] ItemMaker_Success() {
/*  272 */       return ItemMaker_Success_3rdParty(-1);
/*      */     }
/*      */ 
/*      */     public static byte[] ItemMaker_Success_3rdParty(int from_playerid) {
/*  276 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  278 */       if (from_playerid == -1) {
/*  279 */         mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*      */       } else {
/*  281 */         mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
/*  282 */         mplew.writeInt(from_playerid);
/*      */       }
/*  284 */       mplew.write(19);
/*  285 */       mplew.writeInt(0);
/*      */ 
/*  287 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] useWheel(byte charmsleft) {
/*  291 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  293 */       mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*  294 */       mplew.write(24);
/*  295 */       mplew.write(charmsleft);
/*      */ 
/*  297 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showOwnBuffEffect(int skillid, int effectid, int playerLevel, int skillLevel) {
/*  301 */       return showBuffeffect(-1, skillid, effectid, playerLevel, skillLevel, (byte)3);
/*      */     }
/*      */ 
  

/*      */     public static byte[] showOwnBuffEffect(int skillid, int effectid, int playerLevel, int skillLevel, byte direction) {
/*  305 */       return showBuffeffect(-1, skillid, effectid, playerLevel, skillLevel, direction);
/*      */     }
/*      */ 
/*      */     public static byte[] showBuffeffect(int cid, int skillid, int effectid, int playerLevel, int skillLevel) {
/*  309 */       return showBuffeffect(cid, skillid, effectid, playerLevel, skillLevel, (byte)3);
/*      */     }
/*      */ 
/*      */     public static byte[] showBuffeffect(int cid, int skillid, int effectid, int playerLevel, int skillLevel, byte direction) {
/*  313 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  315 */       if (cid == -1) {
/*  316 */         mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*      */       } else {
/*  318 */         mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
/*  319 */         mplew.writeInt(cid);
/*      */       }
/*  321 */       mplew.write(effectid);
/*  322 */       mplew.writeInt(skillid);
/*  323 */       mplew.write(playerLevel - 1);
/*  324 */       if ((effectid == 2) && (skillid == 31111003)) {
/*  325 */         mplew.writeInt(0);
/*      */       }
/*  327 */       mplew.write(skillLevel);
/*  328 */       if ((direction != 3) || (skillid == 1320006) || (skillid == 30001062) || (skillid == 30001061)) {
/*  329 */         mplew.write(direction);
/*      */       }
/*      */ 
/*  334 */       if (skillid == 30001062) {
/*  335 */         mplew.writeInt(0);
/*      */       }
/*  337 */       mplew.writeZeroBytes(10);
/*      */ 
/*  339 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] ShowWZEffect(String data) {
/*  343 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  345 */       mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*  346 */       mplew.write(21);
/*  347 */       mplew.writeMapleAsciiString(data);
/*      */ 
/*  349 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showOwnCraftingEffect(String effect, int time, int mode) {
/*  353 */       return showCraftingEffect(-1, effect, time, mode);
/*      */     }
/*      */ 
/*      */     public static byte[] showCraftingEffect(int cid, String effect, int time, int mode) {
/*  357 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  359 */       if (cid == -1) {
/*  360 */         mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*      */       } else {
/*  362 */         mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
/*  363 */         mplew.writeInt(cid);
/*      */       }
/*  365 */       mplew.write(32);
/*  366 */       mplew.writeMapleAsciiString(effect);
/*  367 */       mplew.write(0);
/*  368 */       mplew.writeInt(time);
/*  369 */       mplew.writeInt(mode);
/*  370 */       if (mode == 2) {
/*  371 */         mplew.writeInt(0);
/*      */       }
/*      */ 
/*  374 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] AranTutInstructionalBalloon(String data) {
/*  378 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  380 */       mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*  381 */       mplew.write(26);
/*  382 */       mplew.writeMapleAsciiString(data);
/*  383 */       mplew.writeInt(1);
/*      */ 
/*  385 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showOwnPetLevelUp(byte index) {
/*  389 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  391 */       mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*  392 */       mplew.write(6);
/*  393 */       mplew.write(0);
/*  394 */       mplew.write(index);
/*      */ 
/*  396 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showOwnChampionEffect() {
/*  400 */       return showChampionEffect(-1);
/*      */     }
/*      */ 
/*      */     public static byte[] showChampionEffect(int from_playerid) {
/*  404 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  406 */       if (from_playerid == -1) {
/*  407 */         mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*      */       } else {
/*  409 */         mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
/*  410 */         mplew.writeInt(from_playerid);
/*      */       }
/*  412 */       mplew.write(34);
/*  413 */       mplew.writeInt(30000);
/*      */ 
/*  415 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Users\Sjögren\Desktop\lithium.jar
 * Qualified Name:     tools.packet.CField
 * JD-Core Version:    0.6.0
 */