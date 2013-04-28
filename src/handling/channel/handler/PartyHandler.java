/*     */ package handling.channel.handler;
/*     */ 
/*     */ import client.MapleCharacter;
/*     */ import client.MapleClient;
/*     */ import constants.GameConstants;
/*     */ import handling.channel.ChannelServer;
/*     */ import handling.channel.PlayerStorage;
/*     */ import handling.world.MapleParty;
/*     */ import handling.world.MaplePartyCharacter;
/*     */ import handling.world.PartyOperation;
/*     */ import handling.world.World;
/*     */ import handling.world.World.Find;
/*     */ import handling.world.World.Party;
/*     */ import handling.world.exped.ExpeditionType;
/*     */ import handling.world.exped.MapleExpedition;
/*     */ import handling.world.exped.PartySearch;
/*     */ import handling.world.exped.PartySearchType;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.mina.common.IoSession;
/*     */ import scripting.EventInstanceManager;
/*     */ import server.maps.Event_DojoAgent;
/*     */ import server.maps.Event_PyramidSubway;
/*     */ import server.maps.FieldLimitType;
/*     */ import server.maps.MapleMap;
/*     */ import server.quest.MapleQuest;
/*     */ import tools.data.LittleEndianAccessor;
import tools.packet.CWvsContext;
/*     */ import tools.packet.CWvsContext.ExpeditionPacket;
/*     */ import tools.packet.CWvsContext.PartyPacket;
/*     */ 
/*     */ public class PartyHandler
/*     */ {
/*     */   public static final void DenyPartyRequest(LittleEndianAccessor slea, MapleClient c)
/*     */   {
/*  47 */     int action = slea.readByte();
/*  48 */     if ((action == 50) && (GameConstants.GMS)) {
/*  49 */       MapleCharacter chr = c.getPlayer().getMap().getCharacterById(slea.readInt());
/*  50 */       if ((chr != null) && (chr.getParty() == null) && (c.getPlayer().getParty() != null) && (c.getPlayer().getParty().getLeader().getId() == c.getPlayer().getId()) && (c.getPlayer().getParty().getMembers().size() < 6) && (c.getPlayer().getParty().getExpeditionId() <= 0) && (chr.getQuestNoAdd(MapleQuest.getInstance(122901)) == null) && (c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(122900)) == null)) {
/*  51 */         chr.setParty(c.getPlayer().getParty());
/*  52 */         World.Party.updateParty(c.getPlayer().getParty().getId(), PartyOperation.JOIN, new MaplePartyCharacter(chr));
/*  53 */         chr.receivePartyMemberHP();
/*  54 */         chr.updatePartyMemberHP();
/*     */       }
/*  56 */       return;
/*     */     }
/*  58 */     int partyid = slea.readInt();
/*  59 */     if ((c.getPlayer().getParty() == null) && (c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(122901)) == null)) {
/*  60 */       MapleParty party = World.Party.getParty(partyid);
/*  61 */       if (party != null) {
/*  62 */         if (party.getExpeditionId() > 0) {
/*  63 */           c.getPlayer().dropMessage(5, "You may not do party operations while in a raid.");
/*  64 */           return;
/*     */         }
/*  66 */         if (action == (GameConstants.GMS ? 31 : 27)) {
/*  67 */           if (party.getMembers().size() < 6) {
/*  68 */             c.getPlayer().setParty(party);
/*  69 */             World.Party.updateParty(partyid, PartyOperation.JOIN, new MaplePartyCharacter(c.getPlayer()));
/*  70 */             c.getPlayer().receivePartyMemberHP();
/*  71 */             c.getPlayer().updatePartyMemberHP();
/*     */           } else {
/*  73 */             c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(22, null));
/*     */           }
/*  75 */         } else if (action != (GameConstants.GMS ? 30 : 22)) {
/*  76 */           MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterById(party.getLeader().getId());
/*  77 */           if (cfrom != null)
/*  78 */             cfrom.getClient().getSession().write(CWvsContext.PartyPacket.partyStatusMessage(23, c.getPlayer().getName()));
/*     */         }
/*     */       }
/*     */       else {
/*  82 */         c.getPlayer().dropMessage(5, "The party you are trying to join does not exist");
/*     */       }
/*     */     } else {
/*  85 */       c.getPlayer().dropMessage(5, "You can't join the party as you are already in one");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void PartyOperation(LittleEndianAccessor slea, MapleClient c)
/*     */   {
/*  91 */     int operation = slea.readByte();
/*  92 */     MapleParty party = c.getPlayer().getParty();
/*  93 */     MaplePartyCharacter partyplayer = new MaplePartyCharacter(c.getPlayer());
/*     */ 
/*  95 */     switch (operation) {
/*     */     case 1:
/*  97 */       if (party == null) {
/*  98 */         party = World.Party.createParty(partyplayer);
/*  99 */         c.getPlayer().setParty(party);
/* 100 */         c.getSession().write(CWvsContext.PartyPacket.partyCreated(party.getId()));
/*     */       }
/*     */       else {
/* 103 */         if (party.getExpeditionId() > 0) {
/* 104 */           c.getPlayer().dropMessage(5, "You may not do party operations while in a raid.");
/* 105 */           return;
/*     */         }
/* 107 */         if ((partyplayer.equals(party.getLeader())) && (party.getMembers().size() == 1))
/* 108 */           c.getSession().write(CWvsContext.PartyPacket.partyCreated(party.getId()));
/*     */         else {
/* 110 */           c.getPlayer().dropMessage(5, "You can't create a party as you are already in one");
/*     */         }
/*     */       }
/* 113 */       break;
/*     */     case 2:
/* 115 */       if (party == null) break;
/* 116 */       if (party.getExpeditionId() > 0) {
/* 117 */         c.getPlayer().dropMessage(5, "You may not do party operations while in a raid.");
/* 118 */         return;
/*     */       }
/* 120 */       if (partyplayer.equals(party.getLeader())) {
/* 121 */         if (GameConstants.isDojo(c.getPlayer().getMapId())) {
/* 122 */           Event_DojoAgent.failed(c.getPlayer());
/*     */         }
/* 124 */         if (c.getPlayer().getPyramidSubway() != null) {
/* 125 */           c.getPlayer().getPyramidSubway().fail(c.getPlayer());
/*     */         }
/* 127 */         World.Party.updateParty(party.getId(), PartyOperation.DISBAND, partyplayer);
/* 128 */         if (c.getPlayer().getEventInstance() != null)
/* 129 */           c.getPlayer().getEventInstance().disbandParty();
/*     */       }
/*     */       else {
/* 132 */         if (GameConstants.isDojo(c.getPlayer().getMapId())) {
/* 133 */           Event_DojoAgent.failed(c.getPlayer());
/*     */         }
/* 135 */         if (c.getPlayer().getPyramidSubway() != null) {
/* 136 */           c.getPlayer().getPyramidSubway().fail(c.getPlayer());
/*     */         }
/* 138 */         World.Party.updateParty(party.getId(), PartyOperation.LEAVE, partyplayer);
/* 139 */         if (c.getPlayer().getEventInstance() != null) {
/* 140 */           c.getPlayer().getEventInstance().leftParty(c.getPlayer());
/*     */         }
/*     */       }
/* 143 */       c.getPlayer().setParty(null); break;
/*     */     case 3:
/* 147 */       int partyid = slea.readInt();
/* 148 */       if (party == null) {
/* 149 */         party = World.Party.getParty(partyid);
/* 150 */         if (party != null) {
/* 151 */           if (party.getExpeditionId() > 0) {
/* 152 */             c.getPlayer().dropMessage(5, "You may not do party operations while in a raid.");
/* 153 */             return;
/*     */           }
/* 155 */           if ((party.getMembers().size() < 6) && (c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(122901)) == null)) {
/* 156 */             c.getPlayer().setParty(party);
/* 157 */             World.Party.updateParty(party.getId(), PartyOperation.JOIN, partyplayer);
/* 158 */             c.getPlayer().receivePartyMemberHP();
/* 159 */             c.getPlayer().updatePartyMemberHP();
/*     */           } else {
/* 161 */             c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(22, null));
/*     */           }
/*     */         } else {
/* 164 */           c.getPlayer().dropMessage(5, "The party you are trying to join does not exist");
/*     */         }
/*     */       } else {
/* 167 */         c.getPlayer().dropMessage(5, "You can't join the party as you are already in one");
/*     */       }
/* 169 */       break;
/*     */     case 4:
/* 171 */       if (party == null) {
/* 172 */         party = World.Party.createParty(partyplayer);
/* 173 */         c.getPlayer().setParty(party);
/* 174 */         c.getSession().write(CWvsContext.PartyPacket.partyCreated(party.getId()));
/*     */       }
/*     */ 
/* 177 */       String theName = slea.readMapleAsciiString();
/* 178 */       int theCh = World.Find.findChannel(theName);
/* 179 */       if (theCh > 0) {
/* 180 */         MapleCharacter invited = ChannelServer.getInstance(theCh).getPlayerStorage().getCharacterByName(theName);
/* 181 */         if ((invited != null) && (invited.getParty() == null) && (invited.getQuestNoAdd(MapleQuest.getInstance(122901)) == null)) {
/* 182 */           if (party.getExpeditionId() > 0) {
/* 183 */             c.getPlayer().dropMessage(5, "You may not do party operations while in a raid.");
/* 184 */             return;
/*     */           }
/* 186 */           if (party.getMembers().size() < 6) {
/* 187 */             c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(26, invited.getName()));
/* 188 */             invited.getClient().getSession().write(CWvsContext.PartyPacket.partyInvite(c.getPlayer()));
/*     */           } else {
/* 190 */             c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(22, null));
/*     */           }
/*     */         } else {
/* 193 */           c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(21, null));
/*     */         }
/*     */       } else {
/* 196 */         c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(17, null));
/*     */       }
/* 198 */       break;
/*     */     case 5:
/* 200 */       if ((party == null) || (partyplayer == null) || (!partyplayer.equals(party.getLeader()))) break;
/* 201 */       if (party.getExpeditionId() > 0) {
/* 202 */         c.getPlayer().dropMessage(5, "You may not do party operations while in a raid.");
/* 203 */         return;
/*     */       }
/* 205 */       MaplePartyCharacter expelled = party.getMemberById(slea.readInt());
/* 206 */       if (expelled != null) {
/* 207 */         if ((GameConstants.isDojo(c.getPlayer().getMapId())) && (expelled.isOnline())) {
/* 208 */           Event_DojoAgent.failed(c.getPlayer());
/*     */         }
/* 210 */         if ((c.getPlayer().getPyramidSubway() != null) && (expelled.isOnline())) {
/* 211 */           c.getPlayer().getPyramidSubway().fail(c.getPlayer());
/*     */         }
/* 213 */         World.Party.updateParty(party.getId(), PartyOperation.EXPEL, expelled);
/* 214 */         if (c.getPlayer().getEventInstance() != null)
/*     */         {
/* 218 */           if (expelled.isOnline()) {
/* 219 */             c.getPlayer().getEventInstance().disbandParty();
/*     */           }
/*     */         }
/*     */       }
/* 223 */       break;
/*     */     case 6:
/* 226 */       if (party == null) break;
/* 227 */       if (party.getExpeditionId() > 0) {
/* 228 */         c.getPlayer().dropMessage(5, "You may not do party operations while in a raid.");
/* 229 */         return;
/*     */       }
/* 231 */       MaplePartyCharacter newleader = party.getMemberById(slea.readInt());
/* 232 */       if ((newleader != null) && (partyplayer.equals(party.getLeader()))) {
/* 233 */         World.Party.updateParty(party.getId(), PartyOperation.CHANGE_LEADER, newleader);
/*     */       }
/* 235 */       break;
/*     */     case 7:
/* 238 */       if (party != null) {
/* 239 */         if ((c.getPlayer().getEventInstance() != null) || (c.getPlayer().getPyramidSubway() != null) || (party.getExpeditionId() > 0) || (GameConstants.isDojo(c.getPlayer().getMapId()))) {
/* 240 */           c.getPlayer().dropMessage(5, "You may not do party operations while in a raid.");
/* 241 */           return;
/*     */         }
/* 243 */         if (partyplayer.equals(party.getLeader()))
/* 244 */           World.Party.updateParty(party.getId(), PartyOperation.DISBAND, partyplayer);
/*     */         else {
/* 246 */           World.Party.updateParty(party.getId(), PartyOperation.LEAVE, partyplayer);
/*     */         }
/* 248 */         c.getPlayer().setParty(null);
/*     */       }
/* 250 */       int partyid_ = slea.readInt();
/* 251 */       if (!GameConstants.GMS)
/*     */         break;
/* 253 */       party = World.Party.getParty(partyid_);
/* 254 */       if ((party == null) || (party.getMembers().size() >= 6)) break;
/* 255 */       if (party.getExpeditionId() > 0) {
/* 256 */         c.getPlayer().dropMessage(5, "You may not do party operations while in a raid.");
/* 257 */         return;
/*     */       }
/* 259 */       MapleCharacter cfrom = c.getPlayer().getMap().getCharacterById(party.getLeader().getId());
/* 260 */       if ((cfrom != null) && (cfrom.getQuestNoAdd(MapleQuest.getInstance(122900)) == null)) {
/* 261 */         c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(50, c.getPlayer().getName()));
/* 262 */         cfrom.getClient().getSession().write(CWvsContext.PartyPacket.partyRequestInvite(c.getPlayer()));
/*     */       } else {
/* 264 */         c.getPlayer().dropMessage(5, "Player was not found or player is not accepting party requests.");
/*     */       }
/* 266 */       break;
/*     */     case 8:
/* 270 */       if (slea.readByte() > 0)
/* 271 */         c.getPlayer().getQuestRemove(MapleQuest.getInstance(122900));
/*     */       else {
/* 273 */         c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122900));
/*     */       }
/* 275 */       break;
/*     */     default:
/* 277 */       System.out.println("Unhandled Party function." + operation);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void AllowPartyInvite(LittleEndianAccessor slea, MapleClient c)
/*     */   {
/* 283 */     if (slea.readByte() > 0)
/* 284 */       c.getPlayer().getQuestRemove(MapleQuest.getInstance(122901));
/*     */     else
/* 286 */       c.getPlayer().getQuestNAdd(MapleQuest.getInstance(122901));
/*     */   }
/*     */ 
/*     */   public static final void MemberSearch(LittleEndianAccessor slea, MapleClient c)
/*     */   {
/* 291 */     if ((c.getPlayer().isInBlockedMap()) || (FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit()))) {
/* 292 */       c.getPlayer().dropMessage(5, "You may not do party search here.");
/* 293 */       return;
/*     */     }
/* 295 */     c.getSession().write(CWvsContext.PartyPacket.showMemberSearch(c.getPlayer().getMap().getCharactersThreadsafe()));
/*     */   }
/*     */ 
/*     */   public static final void PartySearch(LittleEndianAccessor slea, MapleClient c) {
/* 299 */     if ((c.getPlayer().isInBlockedMap()) || (FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit()))) {
/* 300 */       c.getPlayer().dropMessage(5, "You may not do party search here.");
/* 301 */       return;
/*     */     }
/* 303 */     List parties = new ArrayList();
/* 304 */     for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
/* 305 */       if ((chr.getParty() != null) && 
/* 306 */         (chr.getParty().getId() != c.getPlayer().getParty().getId()) && (!parties.contains(chr.getParty()))) {
/* 307 */         parties.add(chr.getParty());
/*     */       }
/*     */     }
/*     */ 
/* 311 */     c.getSession().write(CWvsContext.PartyPacket.showPartySearch(parties));
/*     */   }
/*     */ 
/*     */   public static final void PartyListing(LittleEndianAccessor slea, MapleClient c) {
/* 315 */     int mode = slea.readByte();
/*     */     PartySearchType pst;
/* 318 */     switch (mode) {
/*     */     case -105:
/*     */     case -97:
/*     */     case 81:
/*     */     case 159:
/* 323 */       pst = PartySearchType.getById(slea.readInt());
/* 324 */       if ((pst == null) || (c.getPlayer().getLevel() > pst.maxLevel) || (c.getPlayer().getLevel() < pst.minLevel)) {
/* 325 */         return;
/*     */       }
/* 327 */       if ((c.getPlayer().getParty() == null) && (World.Party.searchParty(pst).size() < 10)) {
/* 328 */         MapleParty party = World.Party.createParty(new MaplePartyCharacter(c.getPlayer()), pst.id);
/* 329 */         c.getPlayer().setParty(party);
/* 330 */         c.getSession().write(CWvsContext.PartyPacket.partyCreated(party.getId()));
/* 331 */         PartySearch ps = new PartySearch(slea.readMapleAsciiString(), pst.exped ? party.getExpeditionId() : party.getId(), pst);
/* 332 */         World.Party.addSearch(ps);
/* 333 */         if (pst.exped) {
/* 334 */           c.getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(World.Party.getExped(party.getExpeditionId()), true, false));
/*     */         }
/* 336 */         c.getSession().write(CWvsContext.PartyPacket.partyListingAdded(ps));
/*     */       } else {
/* 338 */         c.getPlayer().dropMessage(1, "Unable to create. Please leave the party.");
/*     */       }
/* 340 */       break;
/*     */     case -103:
/*     */     case -95:
/*     */     case 83:
/*     */     case 161:
/* 345 */       pst = PartySearchType.getById(slea.readInt());
/* 346 */       if ((pst == null) || (c.getPlayer().getLevel() > pst.maxLevel) || (c.getPlayer().getLevel() < pst.minLevel)) {
/* 347 */         return;
/*     */       }
/* 349 */       c.getSession().write(CWvsContext.PartyPacket.getPartyListing(pst));
/* 350 */       break;
/*     */     case -102:
/*     */     case -94:
/*     */     case 84:
/*     */     case 162:
/* 355 */       break;
/*     */     case -101:
/*     */     case -93:
/*     */     case 85:
/*     */     case 163:
/* 360 */       MapleParty party = c.getPlayer().getParty();
/* 361 */       MaplePartyCharacter partyplayer = new MaplePartyCharacter(c.getPlayer());
/* 362 */       if (party != null) break;
/* 363 */       int theId = slea.readInt();
/* 364 */       party = World.Party.getParty(theId);
/* 365 */       if (party != null) {
/* 366 */         PartySearch ps = World.Party.getSearchByParty(party.getId());
/* 367 */         if ((ps != null) && (c.getPlayer().getLevel() <= ps.getType().maxLevel) && (c.getPlayer().getLevel() >= ps.getType().minLevel) && (party.getMembers().size() < 6)) {
/* 368 */           c.getPlayer().setParty(party);
/* 369 */           World.Party.updateParty(party.getId(), PartyOperation.JOIN, partyplayer);
/* 370 */           c.getPlayer().receivePartyMemberHP();
/* 371 */           c.getPlayer().updatePartyMemberHP();
/*     */         } else {
/* 373 */           c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(21, null));
/*     */         }
/*     */       } else {
/* 376 */         MapleExpedition exped = World.Party.getExped(theId);
/* 377 */         if (exped != null) {
/* 378 */           PartySearch ps = World.Party.getSearchByExped(exped.getId());
/* 379 */           if ((ps != null) && (c.getPlayer().getLevel() <= ps.getType().maxLevel) && (c.getPlayer().getLevel() >= ps.getType().minLevel) && (exped.getAllMembers() < exped.getType().maxMembers)) {
/* 380 */             int partyId = exped.getFreeParty();
/* 381 */             if (partyId < 0) {
/* 382 */               c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(21, null));
/* 383 */             } else if (partyId == 0) {
/* 384 */               party = World.Party.createPartyAndAdd(partyplayer, exped.getId());
/* 385 */               c.getPlayer().setParty(party);
/* 386 */               c.getSession().write(CWvsContext.PartyPacket.partyCreated(party.getId()));
/* 387 */               c.getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(exped, true, false));
/* 388 */               World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionJoined(c.getPlayer().getName()), null);
/* 389 */               World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionUpdate(exped.getIndex(party.getId()), party), null);
/*     */             } else {
/* 391 */               c.getPlayer().setParty(World.Party.getParty(partyId));
/* 392 */               World.Party.updateParty(partyId, PartyOperation.JOIN, partyplayer);
/* 393 */               c.getPlayer().receivePartyMemberHP();
/* 394 */               c.getPlayer().updatePartyMemberHP();
/* 395 */               c.getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(exped, true, false));
/* 396 */               World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionJoined(c.getPlayer().getName()), null);
/*     */             }
/*     */           } else {
/* 399 */             c.getSession().write(CWvsContext.ExpeditionPacket.expeditionError(0, c.getPlayer().getName()));
/*     */           }
/*     */         }
/*     */       }
/* 403 */       break;
/*     */     default:
/* 406 */       if (!c.getPlayer().isGM()) break;
/* 407 */       System.out.println("Unknown PartyListing : " + mode + "\n" + slea);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final void Expedition(LittleEndianAccessor slea, MapleClient c)
/*     */   {
/* 414 */     if ((c.getPlayer() == null) || (c.getPlayer().getMap() == null)) {
/* 415 */       return;
/*     */     }
/* 417 */     int mode = slea.readByte();
/*     */     String name;
/*     */     MapleParty part;
/*     */     MapleExpedition exped;
/*     */     int cid;
/*     */     Iterator i$;

/* 420 */     switch (mode)
/*     */     {
/*     */     case 64:
/*     */     case 134:
/* 424 */       ExpeditionType et = ExpeditionType.getById(slea.readInt());
/* 425 */       if ((et != null) && (c.getPlayer().getParty() == null) && (c.getPlayer().getLevel() <= et.maxLevel) && (c.getPlayer().getLevel() >= et.minLevel)) {
/* 426 */         MapleParty party = World.Party.createParty(new MaplePartyCharacter(c.getPlayer()), et.exped);
/* 427 */         c.getPlayer().setParty(party);
/* 428 */         c.getSession().write(CWvsContext.PartyPacket.partyCreated(party.getId()));
/* 429 */         c.getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(World.Party.getExped(party.getExpeditionId()), true, false));
/*     */       } else {
/* 431 */         c.getSession().write(CWvsContext.ExpeditionPacket.expeditionError(0, ""));
/*     */       }
/* 433 */       break;
/*     */     case 65:
/*     */     case 135:
/* 437 */       name = slea.readMapleAsciiString();
/* 438 */       int theCh = World.Find.findChannel(name);
/* 439 */       if (theCh > 0) {
/* 440 */         MapleCharacter invited = ChannelServer.getInstance(theCh).getPlayerStorage().getCharacterByName(name);
/* 441 */         MapleParty party = c.getPlayer().getParty();
/* 442 */         if ((invited != null) && (invited.getParty() == null) && (party != null) && (party.getExpeditionId() > 0)) {
/* 443 */           MapleExpedition me = World.Party.getExped(party.getExpeditionId());
/* 444 */           if ((me != null) && (me.getAllMembers() < me.getType().maxMembers) && (invited.getLevel() <= me.getType().maxLevel) && (invited.getLevel() >= me.getType().minLevel)) {
/* 445 */             c.getSession().write(CWvsContext.ExpeditionPacket.expeditionError(7, invited.getName()));
/* 446 */             invited.getClient().getSession().write(CWvsContext.ExpeditionPacket.expeditionInvite(c.getPlayer(), me.getType().exped));
/*     */           } else {
/* 448 */             c.getSession().write(CWvsContext.ExpeditionPacket.expeditionError(3, invited.getName()));
/*     */           }
/*     */         } else {
/* 451 */           c.getSession().write(CWvsContext.ExpeditionPacket.expeditionError(2, name));
/*     */         }
/*     */       } else {
/* 454 */         c.getSession().write(CWvsContext.ExpeditionPacket.expeditionError(0, name));
/*     */       }
/* 456 */       break;
/*     */     case 66:
/*     */     case 136:
/* 460 */       name = slea.readMapleAsciiString();
/* 461 */       int action = slea.readInt();
/* 462 */       int theChh = World.Find.findChannel(name);
/* 463 */       if (theChh <= 0) break;
/* 464 */       MapleCharacter cfrom = ChannelServer.getInstance(theChh).getPlayerStorage().getCharacterByName(name);
/* 465 */       if ((cfrom != null) && (cfrom.getParty() != null) && (cfrom.getParty().getExpeditionId() > 0)) {
/* 466 */         MapleParty party = cfrom.getParty();
/* 467 */         exped = World.Party.getExped(party.getExpeditionId());
/* 468 */         if ((exped != null) && (action == 8)) {
/* 469 */           if ((c.getPlayer().getLevel() <= exped.getType().maxLevel) && (c.getPlayer().getLevel() >= exped.getType().minLevel) && (exped.getAllMembers() < exped.getType().maxMembers)) {
/* 470 */             int partyId = exped.getFreeParty();
/* 471 */             if (partyId < 0) {
/* 472 */               c.getSession().write(CWvsContext.PartyPacket.partyStatusMessage(21, null));
/* 473 */             } else if (partyId == 0) {
/* 474 */               party = World.Party.createPartyAndAdd(new MaplePartyCharacter(c.getPlayer()), exped.getId());
/* 475 */               c.getPlayer().setParty(party);
/* 476 */               c.getSession().write(CWvsContext.PartyPacket.partyCreated(party.getId()));
/* 477 */               c.getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(exped, true, false));
/* 478 */               World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionJoined(c.getPlayer().getName()), null);
/* 479 */               World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionUpdate(exped.getIndex(party.getId()), party), null);
/*     */             } else {
/* 481 */               c.getPlayer().setParty(World.Party.getParty(partyId));
/* 482 */               World.Party.updateParty(partyId, PartyOperation.JOIN, new MaplePartyCharacter(c.getPlayer()));
/* 483 */               c.getPlayer().receivePartyMemberHP();
/* 484 */               c.getPlayer().updatePartyMemberHP();
/* 485 */               c.getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(exped, false, false));
/* 486 */               World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionJoined(c.getPlayer().getName()), null);
/*     */             }
/*     */           } else {
/* 489 */             c.getSession().write(CWvsContext.ExpeditionPacket.expeditionError(3, cfrom.getName()));
/*     */           }
/* 491 */         } else if (action == 9) {
/* 492 */           cfrom.getClient().getSession().write(CWvsContext.PartyPacket.partyStatusMessage(23, c.getPlayer().getName()));
/*     */         }
/*     */       }
/* 495 */       break;
/*     */     case 67:
/*     */     case 137:
/* 500 */       part = c.getPlayer().getParty();
/* 501 */       if ((part == null) || (part.getExpeditionId() <= 0)) break;
/* 502 */      exped = World.Party.getExped(part.getExpeditionId());
/* 503 */       if (exped != null) {
/* 504 */         if (GameConstants.isDojo(c.getPlayer().getMapId())) {
/* 505 */           Event_DojoAgent.failed(c.getPlayer());
/*     */         }
/* 507 */         if (exped.getLeader() == c.getPlayer().getId()) {
/* 508 */           World.Party.disbandExped(exped.getId());
/* 509 */           if (c.getPlayer().getEventInstance() != null)
/* 510 */             c.getPlayer().getEventInstance().disbandParty();
/*     */         }
/* 512 */         else if (part.getLeader().getId() == c.getPlayer().getId()) {
/* 513 */           World.Party.updateParty(part.getId(), PartyOperation.DISBAND, new MaplePartyCharacter(c.getPlayer()));
/* 514 */           if (c.getPlayer().getEventInstance() != null) {
/* 515 */             c.getPlayer().getEventInstance().disbandParty();
/*     */           }
/* 517 */           World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionLeft(c.getPlayer().getName()), null);
/*     */         } else {
/* 519 */           World.Party.updateParty(part.getId(), PartyOperation.LEAVE, new MaplePartyCharacter(c.getPlayer()));
/* 520 */           if (c.getPlayer().getEventInstance() != null) {
/* 521 */             c.getPlayer().getEventInstance().leftParty(c.getPlayer());
/*     */           }
/* 523 */           World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionLeft(c.getPlayer().getName()), null);
/*     */         }
/* 525 */         if (c.getPlayer().getPyramidSubway() != null) {
/* 526 */           c.getPlayer().getPyramidSubway().fail(c.getPlayer());
/*     */         }
/* 528 */         c.getPlayer().setParty(null);
/*     */       }
/* 530 */       break;
/*     */     case 68:
/*     */     case 138:
/* 535 */       part = c.getPlayer().getParty();
/* 536 */       if ((part == null) || (part.getExpeditionId() <= 0)) break;
/* 537 */       exped = World.Party.getExped(part.getExpeditionId());
/* 538 */       if ((exped != null) && (exped.getLeader() == c.getPlayer().getId())) {
/* 539 */         cid = slea.readInt();
/* 540 */         for (i$ = exped.getParties().iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
/* 541 */           MapleParty par = World.Party.getParty(i);
/* 542 */           if (par != null) {
/* 543 */             MaplePartyCharacter expelled = par.getMemberById(cid);
/* 544 */             if (expelled != null) {
/* 545 */               if ((expelled.isOnline()) && (GameConstants.isDojo(c.getPlayer().getMapId()))) {
/* 546 */                 Event_DojoAgent.failed(c.getPlayer());
/*     */               }
/* 548 */               World.Party.updateParty(i, PartyOperation.EXPEL, expelled);
/* 549 */               if ((c.getPlayer().getEventInstance() != null) && 
/* 550 */                 (expelled.isOnline())) {
/* 551 */                 c.getPlayer().getEventInstance().disbandParty();
/*     */               }
/*     */ 
/* 554 */               if ((c.getPlayer().getPyramidSubway() != null) && (expelled.isOnline())) {
/* 555 */                 c.getPlayer().getPyramidSubway().fail(c.getPlayer());
/*     */               }
/* 557 */               World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionLeft(expelled.getName()), null);
/* 558 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 563 */       break;
/*     */     case 69:
/*     */     case 139:
/* 568 */       part = c.getPlayer().getParty();
/* 569 */       if ((part == null) || (part.getExpeditionId() <= 0)) break;
/* 570 */       exped = World.Party.getExped(part.getExpeditionId());
/* 571 */       if ((exped != null) && (exped.getLeader() == c.getPlayer().getId())) {
/* 572 */         MaplePartyCharacter newleader = part.getMemberById(slea.readInt());
/* 573 */         if (newleader != null) {
/* 574 */           World.Party.updateParty(part.getId(), PartyOperation.CHANGE_LEADER, newleader);
/* 575 */           exped.setLeader(newleader.getId());
/* 576 */           World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionLeaderChanged(0), null);
/*     */         }
/*     */       }
/* 579 */       break;
/*     */     case 70:
/*     */     case 140:
/* 584 */       part = c.getPlayer().getParty();
/* 585 */       if ((part == null) || (part.getExpeditionId() <= 0)) break;
/* 586 */        exped = World.Party.getExped(part.getExpeditionId());
/* 587 */       if ((exped != null) && (exped.getLeader() == c.getPlayer().getId())) {
/* 588 */         cid = slea.readInt();
/* 589 */         for (i$ = exped.getParties().iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
/* 590 */           MapleParty par = World.Party.getParty(i);
/* 591 */           if (par != null) {
/* 592 */             MaplePartyCharacter newleader = par.getMemberById(cid);
/* 593 */             if ((newleader != null) && (par.getId() != part.getId())) {
/* 594 */               World.Party.updateParty(par.getId(), PartyOperation.CHANGE_LEADER, newleader);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 599 */       break;
/*     */     case 71:
/*     */     case 141:
/* 604 */       part = c.getPlayer().getParty();
/* 605 */       if ((part == null) || (part.getExpeditionId() <= 0)) break;
/* 606 */       exped = World.Party.getExped(part.getExpeditionId());
/* 607 */       if ((exped != null) && (exped.getLeader() == c.getPlayer().getId())) {
/* 608 */         int partyIndexTo = slea.readInt();
/* 609 */         if ((partyIndexTo < exped.getType().maxParty) && (partyIndexTo <= exped.getParties().size())) {
/* 610 */           cid = slea.readInt();
/* 611 */           for (i$ = exped.getParties().iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
/* 612 */             MapleParty par = World.Party.getParty(i);
/* 613 */             if (par != null) {
/* 614 */               MaplePartyCharacter expelled = par.getMemberById(cid);
/* 615 */               if ((expelled != null) && (expelled.isOnline())) {
/* 616 */                 MapleCharacter chr = World.getStorage(expelled.getChannel()).getCharacterById(expelled.getId());
/* 617 */                 if (chr == null) {
/*     */                   break;
/*     */                 }
/* 620 */                 if (partyIndexTo < exped.getParties().size()) {
/* 621 */                   MapleParty party = World.Party.getParty(((Integer)exped.getParties().get(partyIndexTo)).intValue());
/* 622 */                   if ((party == null) || (party.getMembers().size() >= 6)) {
/* 623 */                     c.getPlayer().dropMessage(5, "Invalid party.");
/* 624 */                     break;
/*     */                   }
/*     */                 }
/* 627 */                 if (GameConstants.isDojo(c.getPlayer().getMapId())) {
/* 628 */                   Event_DojoAgent.failed(c.getPlayer());
/*     */                 }
/* 630 */                 World.Party.updateParty(i, PartyOperation.EXPEL, expelled);
/* 631 */                 if (partyIndexTo < exped.getParties().size()) {
/* 632 */                   MapleParty party = World.Party.getParty(((Integer)exped.getParties().get(partyIndexTo)).intValue());
/* 633 */                   if ((party != null) && (party.getMembers().size() < 6)) {
/* 634 */                     World.Party.updateParty(party.getId(), PartyOperation.JOIN, expelled);
/* 635 */                     chr.receivePartyMemberHP();
/* 636 */                     chr.updatePartyMemberHP();
/* 637 */                     chr.getClient().getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(exped, true, false));
/*     */                   }
/*     */                 } else {
/* 640 */                   MapleParty party = World.Party.createPartyAndAdd(expelled, exped.getId());
/* 641 */                   chr.setParty(party);
/* 642 */                   chr.getClient().getSession().write(CWvsContext.PartyPacket.partyCreated(party.getId()));
/* 643 */                   chr.getClient().getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(exped, true, false));
/* 644 */                   World.Party.expedPacket(exped.getId(), CWvsContext.ExpeditionPacket.expeditionUpdate(exped.getIndex(party.getId()), party), null);
/*     */                 }
/* 646 */                 if ((c.getPlayer().getEventInstance() != null) && 
/* 647 */                   (expelled.isOnline())) {
/* 648 */                   c.getPlayer().getEventInstance().disbandParty();
/*     */                 }
/*     */ 
/* 651 */                 if (c.getPlayer().getPyramidSubway() == null) break;
/* 652 */                 c.getPlayer().getPyramidSubway().fail(c.getPlayer()); break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 660 */       break;
/*     */     default:
/* 663 */       if (!c.getPlayer().isGM()) break;
/* 664 */       System.out.println("Unknown Expedition : " + mode + "\n" + slea);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Sjögren\Desktop\lithium.jar
 * Qualified Name:     handling.channel.handler.PartyHandler
 * JD-Core Version:    0.6.0
 */