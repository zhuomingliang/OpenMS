/*     */ package tools.packet;
/*     */ 
/*     */ import client.MapleCharacter;
/*     */ import client.MapleStat;
/*     */ import client.inventory.Item;
/*     */ import client.inventory.MaplePet;
/*     */ import constants.GameConstants;
/*     */ import handling.SendPacketOpcode;
/*     */ import java.awt.Point;
/*     */ import java.util.List;
/*     */ import server.movement.LifeMovementFragment;
/*     */ import tools.data.MaplePacketLittleEndianWriter;
/*     */ 
/*     */ public class PetPacket
/*     */ {
/*     */   public static final byte[] updatePet(MaplePet pet, Item item, boolean active)
/*     */   {
/*  39 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/*  41 */     mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  42 */     mplew.write(0);
/*  43 */     mplew.write(2);
/*  44 */     mplew.write(3);
/*  45 */     mplew.write(5);
/*  46 */     mplew.writeShort(pet.getInventoryPosition());
/*  47 */     mplew.write(0);
/*  48 */     mplew.write(5);
/*  49 */     mplew.writeShort(pet.getInventoryPosition());
/*  50 */     mplew.write(3);
/*  51 */     mplew.writeInt(pet.getPetItemId());
/*  52 */     mplew.write(1);
/*  53 */     mplew.writeLong(pet.getUniqueId());
/*  54 */     PacketHelper.addPetItemInfo(mplew, item, pet, active);
/*  55 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] showPet(MapleCharacter chr, MaplePet pet, boolean remove, boolean hunger) {
/*  59 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/*  61 */     mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
/*  62 */     mplew.writeInt(chr.getId());
/*  63 */     mplew.write(chr.getPetIndex(pet));
/*  64 */     if (remove) {
/*  65 */       mplew.write(0);
/*  66 */       mplew.write(hunger ? 1 : 0);
/*     */     } else {
/*  68 */       mplew.write(1);
/*  69 */       mplew.write(0);
/*  70 */       mplew.writeInt(pet.getPetItemId());
/*  71 */       mplew.writeMapleAsciiString(pet.getName());
/*  72 */       mplew.writeLong(pet.getUniqueId());
/*  73 */       mplew.writeShort(pet.getPos().x);
/*  74 */       mplew.writeShort(pet.getPos().y - 20);
/*  75 */       mplew.write(pet.getStance());
/*  76 */       mplew.writeInt(pet.getFh());
/*     */     }
/*     */ 
/*  79 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] removePet(int cid, int index) {
/*  83 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/*  85 */     mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
/*  86 */     mplew.writeInt(cid);
/*  87 */     mplew.write(index);
/*  88 */     mplew.writeShort(0);
/*     */ 
/*  90 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] movePet(int cid, int pid, byte slot, List<LifeMovementFragment> moves) {
/*  94 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/*  96 */     mplew.writeShort(SendPacketOpcode.MOVE_PET.getValue());
/*  97 */     mplew.writeInt(cid);
/*  98 */     mplew.write(slot);
/*  99 */     mplew.writeLong(pid);
/* 100 */     PacketHelper.serializeMovementList(mplew, moves);
/*     */ 
/* 102 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] petChat(int cid, int un, String text, byte slot) {
/* 106 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 108 */     mplew.writeShort(SendPacketOpcode.PET_CHAT.getValue());
/* 109 */     mplew.writeInt(cid);
/* 110 */     mplew.write(slot);
/* 111 */     mplew.write(un);
/* 112 */     mplew.write(0);
/* 113 */     mplew.writeMapleAsciiString(text);
/* 114 */     mplew.write(0);
/*     */ 
/* 116 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] commandResponse(int cid, byte command, byte slot, boolean success, boolean food) {
/* 120 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 122 */     mplew.writeShort(SendPacketOpcode.PET_COMMAND.getValue());
/* 123 */     mplew.writeInt(cid);
/* 124 */     mplew.write(slot);
/* 125 */     mplew.write(command == 1 ? 1 : 0);
/* 126 */     mplew.write(command);
/* 127 */     mplew.write(success ? 1 : command == 1 ? 0 : 0);
/* 128 */     mplew.write(0);
/*     */ 
/* 130 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] showPetLevelUp(MapleCharacter chr, byte index) {
/* 134 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 136 */     mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
/* 137 */     mplew.writeInt(chr.getId());
/* 138 */     mplew.write(6);
/* 139 */     mplew.write(0);
/* 140 */     mplew.writeInt(index);
/*     */ 
/* 142 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] showPetUpdate(MapleCharacter chr, int uniqueId, byte index) {
/* 146 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 148 */     mplew.writeShort(SendPacketOpcode.PET_EXCEPTION_LIST.getValue());
/* 149 */     mplew.writeInt(chr.getId());
/* 150 */     mplew.write(index);
/* 151 */     mplew.writeLong(uniqueId);
/* 152 */     mplew.write(0);
/*     */ 
/* 154 */     return mplew.getPacket();
/*     */   }
/*     */ 
/*     */   public static final byte[] petStatUpdate(MapleCharacter chr) {
/* 158 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*     */ 
/* 160 */     mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
/* 161 */     mplew.write(0);
/* 162 */     if (GameConstants.GMS)
/* 163 */       mplew.writeLong(MapleStat.PET.getValue());
/*     */     else {
/* 165 */       mplew.writeInt((int)MapleStat.PET.getValue());
/*     */     }
/*     */ 
/* 169 */     byte count = 0;
/* 170 */     for (MaplePet pet : chr.getPets()) {
/* 171 */       if (pet.getSummoned()) {
/* 172 */         mplew.writeLong(pet.getUniqueId());
/* 173 */         count = (byte)(count + 1);
/*     */       }
/*     */     }
/* 176 */     while (count < 3) {
/* 177 */       mplew.writeZeroBytes(8);
/* 178 */       count = (byte)(count + 1);
/*     */     }
/* 180 */     mplew.write(0);
/* 181 */     mplew.writeShort(0);
/*     */ 
/* 183 */     return mplew.getPacket();
/*     */   }
/*     */ }

/* Location:           C:\Users\Sjögren\Desktop\lithium.jar
 * Qualified Name:     tools.packet.PetPacket
 * JD-Core Version:    0.6.0
 */