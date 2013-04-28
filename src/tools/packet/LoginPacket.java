/*     */ package tools.packet;
/*     */ 
/*     */ import client.MapleCharacter;
/*     */ import client.MapleClient;
/*     */ import handling.SendPacketOpcode;
/*     */ import handling.login.LoginServer;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import server.Randomizer;
/*     */ import tools.HexTool;
/*     */ import tools.data.MaplePacketLittleEndianWriter;
/*     */ 
/*     */ public class LoginPacket
/*     */ {
/*     */   public static final byte[] getHello(short mapleVersion, byte[] sendIv, byte[] recvIv)
/*     */   {
/*  42 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(15 + "1".length());
/*     */ 
/*  44 */     mplew.writeShort(13 + "1".length());
/*  45 */     mplew.writeShort(mapleVersion);
/*  46 */     mplew.writeMapleAsciiString("2");
/*  47 */     mplew.write(recvIv);
/*  48 */     mplew.write(sendIv);
/*  49 */     mplew.write(8);
/*     */ 
/*  51 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getPing() {
/*  55 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(2);
/*     */ 
/*  57 */     mplew.writeShort(SendPacketOpcode.PING.getValue());
/*     */ 
/*  59 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getAuthSuccessRequest(MapleClient client) {
/*  63 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/*  65 */     mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
/*  66 */     mplew.writeZeroBytes(6);
/*  67 */     mplew.writeInt(client.getAccID());
/*  68 */     mplew.write(0);
/*  69 */     mplew.write(0);
/*  70 */     mplew.writeShort(0);
/*  71 */     mplew.write(0);
/*  72 */     mplew.writeMapleAsciiString(client.getAccountName());
/*  73 */     mplew.write(2);
/*  74 */     mplew.write(0);
/*  75 */     mplew.writeLong(0L);
/*  76 */     mplew.write(0);
/*  77 */     mplew.writeLong(0L);
/*  78 */     mplew.writeInt(0);
/*  79 */     mplew.writeShort(257);
/*  80 */     mplew.writeInt(0);
/*  81 */     mplew.writeInt(0);
/*  82 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getLoginFailed(int reason) {
/*  86 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
/*     */ 
/* 102 */     mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
/* 103 */     mplew.write(reason);
/* 104 */     mplew.write(0);
/* 105 */     mplew.writeInt(0);
/*     */ 
/* 107 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getPermBan(byte reason) {
/* 111 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);
/*     */ 
/* 113 */     mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
/* 114 */     mplew.writeShort(2);
/* 115 */     mplew.writeInt(0);
/* 116 */     mplew.writeShort(reason);
/* 117 */     mplew.write(HexTool.getByteArrayFromHexString("01 01 01 01 00"));
/*     */ 
/* 119 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getTempBan(long timestampTill, byte reason) {
/* 123 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(17);
/*     */ 
/* 128 */     mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
/* 129 */     mplew.write(2);
/* 130 */     mplew.write(0);
/* 131 */     mplew.writeInt(0);
/* 132 */     mplew.write(reason);
/* 133 */     mplew.writeLong(timestampTill);
/*     */ 
/* 135 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getSecondAuthSuccess(MapleClient client) {
/* 139 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 141 */     mplew.writeShort(SendPacketOpcode.LOGIN_SECOND.getValue());
/* 142 */     mplew.write(0);
/* 143 */     mplew.writeInt(client.getAccID());
/* 144 */     mplew.writeZeroBytes(5);
/* 145 */     mplew.writeMapleAsciiString(client.getAccountName());
/* 146 */     mplew.writeLong(2L);
/* 147 */     mplew.writeZeroBytes(3);
/* 148 */     mplew.writeInt(Randomizer.nextInt());
/* 149 */     mplew.writeInt(Randomizer.nextInt());
/* 150 */     mplew.writeInt(28);
/* 151 */     mplew.writeInt(Randomizer.nextInt());
/* 152 */     mplew.writeInt(Randomizer.nextInt());
/* 153 */     mplew.write(1);
/*     */ 
/* 155 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] deleteCharResponse(int cid, int state) {
/* 159 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 161 */     mplew.writeShort(SendPacketOpcode.DELETE_CHAR_RESPONSE.getValue());
/* 162 */     mplew.writeInt(cid);
/* 163 */     mplew.write(state);
/*     */ 
/* 165 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] secondPwError(byte mode) {
/* 169 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
/*     */ 
/* 175 */     mplew.writeShort(SendPacketOpcode.SECONDPW_ERROR.getValue());
/* 176 */     mplew.write(0);
/*     */ 
/* 178 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] enableRecommended() {
/* 182 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 183 */     mplew.writeShort(SendPacketOpcode.ENABLE_RECOMMENDED.getValue());
/* 184 */     mplew.writeInt(0);
/* 185 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] sendRecommended(int world, String message) {
/* 189 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 190 */     mplew.writeShort(SendPacketOpcode.SEND_RECOMMENDED.getValue());
/* 191 */     mplew.write(message != null ? 1 : 0);
/* 192 */     if (message != null) {
/* 193 */       mplew.writeInt(world);
/* 194 */       mplew.writeMapleAsciiString(message);
/*     */     }
/* 196 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getServerList(int serverId, Map<Integer, Integer> channelLoad) {
/* 200 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 202 */     mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
/* 203 */     mplew.write(serverId);
/* 204 */     String worldName = LoginServer.getTrueServerName();
/* 205 */     mplew.writeMapleAsciiString(worldName);
/* 206 */     mplew.write(LoginServer.getFlag());
/* 207 */     mplew.writeMapleAsciiString(LoginServer.getEventMessage());
/* 208 */     mplew.writeShort(100);
/* 209 */     mplew.writeShort(100);
/* 210 */     mplew.write(0);
/* 211 */     int lastChannel = 1;
/* 212 */     Set channels = channelLoad.keySet();
/* 213 */     for (int i = 30; i > 0; i--) {
/* 214 */       if (channels.contains(Integer.valueOf(i))) {
/* 215 */         lastChannel = i;
/* 216 */         break;
/*     */       }
/*     */     }
/* 219 */     mplew.write(lastChannel);
/*     */ 
/* 221 */     for (int i = 1; i <= lastChannel; i++)
/*     */     {
/*     */       int load;

/* 222 */       if (channels.contains(Integer.valueOf(i)))
/* 223 */         load = ((Integer)channelLoad.get(Integer.valueOf(i))).intValue();
/*     */       else {
/* 225 */         load = 1200;
/*     */       }
/* 227 */       mplew.writeMapleAsciiString(worldName + "-" + i);
/* 228 */       mplew.writeInt(load);
/* 229 */       mplew.write(serverId);
/* 230 */       mplew.writeShort(i - 1);
/*     */     }
/* 232 */     mplew.writeShort(0);
/* 233 */     mplew.writeInt(0);
/*     */ 
/* 235 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getEndOfServerList() {
/* 239 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 241 */     mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
/* 242 */     mplew.writeShort(255);
/*     */ 
/* 244 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getLoginWelcome() {
/* 248 */     List flags = new LinkedList();
/*     */ 
/* 251 */     return CField.spawnFlags(flags);
/*     */   }
/*     */ 
/*     */   public static final byte[] getServerStatus(int status) {
/* 255 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 260 */     mplew.writeShort(SendPacketOpcode.SERVERSTATUS.getValue());
/* 261 */     mplew.writeShort(status);
/*     */ 
/* 263 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getChannelSelected() {
/* 267 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 269 */     mplew.writeShort(SendPacketOpcode.CHANNEL_SELECTED.getValue());
/* 270 */     mplew.writeZeroBytes(3);
/*     */ 
/* 272 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] getCharList(String secondpw, List<MapleCharacter> chars, int charslots) {
/* 276 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 278 */     mplew.writeShort(SendPacketOpcode.CHARLIST.getValue());
/* 279 */     mplew.write(0);
/* 280 */     mplew.write(chars.size());
/* 281 */     for (MapleCharacter chr : chars) {
/* 282 */       addCharEntry(mplew, chr, (!chr.isGM()) && (chr.getLevel() >= 30), false);
/*     */     }
/* 284 */     mplew.write(secondpw != null && secondpw.length() > 0 ? 1 : (secondpw != null && secondpw.length() <= 0 ? 2 : 0)); // second pw request
              mplew.write(0);
/* 285 */     mplew.writeInt(charslots);
/* 286 */     mplew.writeInt(0);
/* 287 */     mplew.writeInt(0);
/* 288 */     mplew.writeInt(0);
/* 289 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] addNewCharEntry(MapleCharacter chr, boolean worked) {
/* 293 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 295 */     mplew.writeShort(SendPacketOpcode.ADD_NEW_CHAR_ENTRY.getValue());
/* 296 */     mplew.write(worked ? 0 : 1);
/* 297 */     addCharEntry(mplew, chr, false, false);
/*     */ 
/* 299 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] charNameResponse(String charname, boolean nameUsed) {
/* 303 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 305 */     mplew.writeShort(SendPacketOpcode.CHAR_NAME_RESPONSE.getValue());
/* 306 */     mplew.writeMapleAsciiString(charname);
/* 307 */     mplew.write(nameUsed ? 1 : 0);
/*     */ 
/* 309 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   private static final void addCharEntry(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, boolean ranking, boolean viewAll) {
/* 313 */     PacketHelper.addCharStats(mplew, chr);
/* 314 */     PacketHelper.addCharLook(mplew, chr, true);
/* 315 */     if (!viewAll) {
/* 316 */       mplew.write(0);
/*     */     }
/* 318 */     mplew.write(ranking ? 1 : 0);
/* 319 */     if (ranking) {
/* 320 */       mplew.writeInt(chr.getRank());
/* 321 */       mplew.writeInt(chr.getRankMove());
/* 322 */       mplew.writeInt(chr.getJobRank());
/* 323 */       mplew.writeInt(chr.getJobRankMove());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] showAllCharacter(int chars) {
/* 328 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 329 */     mplew.writeShort(SendPacketOpcode.ALL_CHARLIST.getValue());
/* 330 */     mplew.write(1);
/* 331 */     mplew.writeInt(chars);
/* 332 */     mplew.writeInt(chars + (3 - chars % 3));
/* 333 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] showAllCharacterInfo(int worldid, List<MapleCharacter> chars, String pic) {
/* 337 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 338 */     mplew.writeShort(SendPacketOpcode.ALL_CHARLIST.getValue());
/* 339 */     mplew.write(chars.size() == 0 ? 5 : 0);
/* 340 */     mplew.write(worldid);
/* 341 */     mplew.write(chars.size());
/* 342 */     for (MapleCharacter chr : chars) {
/* 343 */       addCharEntry(mplew, chr, true, true);
/*     */     }
/* 345 */     mplew.write(pic.equals("") ? 2 : pic == null ? 0 : 1);
/* 346 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static byte[] enableSpecialCreation(int accid, boolean enable) {
/* 350 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 352 */     mplew.writeShort(SendPacketOpcode.SPECIAL_CREATION.getValue());
/* 353 */     mplew.writeInt(accid);
/* 354 */     mplew.write(enable ? 0 : 1);
/* 355 */     mplew.write(0);
/*     */ 
/* 357 */     return mplew.getPacket();
/*     */   }
/*     */ }

/* Location:           C:\Users\Sjögren\Desktop\lithium.jar
 * Qualified Name:     tools.packet.LoginPacket
 * JD-Core Version:    0.6.0
 */