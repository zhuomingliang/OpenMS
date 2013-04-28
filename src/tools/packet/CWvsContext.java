/*      */ package tools.packet;
/*      */ 
/*      */ import client.BuddylistEntry;
/*      */ import client.MapleBuffStat;
/*      */ import client.MapleCharacter;
/*      */ import client.MapleDisease;
/*      */ import client.MapleQuestStatus;
/*      */ import client.MapleStat;
/*      */ import client.MapleStat.Temp;
/*      */ import client.MapleTrait;
/*      */ import client.MapleTrait.MapleTraitType;
/*      */ import client.MonsterBook;
/*      */ import client.MonsterFamiliar;
/*      */ import client.PlayerStats;
/*      */ import client.Skill;
/*      */ import client.SkillEntry;
/*      */ import client.inventory.Item;
/*      */ import client.inventory.MapleImp;
/*      */ import client.inventory.MapleImp.ImpFlag;
/*      */ import client.inventory.MapleInventory;
/*      */ import client.inventory.MapleInventoryType;
/*      */ import client.inventory.MapleMount;
/*      */ import client.inventory.MaplePet;
/*      */ import constants.GameConstants;
/*      */ import handling.SendPacketOpcode;
import handling.channel.MapleGuildRanking;
/*      */ import handling.channel.MapleGuildRanking.GuildRankingInfo;
/*      */ import handling.world.MapleParty;
/*      */ import handling.world.MaplePartyCharacter;
/*      */ import handling.world.PartyOperation;
import handling.world.World;
/*      */ import handling.world.World.Alliance;
/*      */ import handling.world.World.Family;
/*      */ import handling.world.World.Guild;
/*      */ import handling.world.World.Party;
/*      */ import handling.world.exped.ExpeditionType;
/*      */ import handling.world.exped.MapleExpedition;
/*      */ import handling.world.exped.PartySearch;
/*      */ import handling.world.exped.PartySearchType;
/*      */ import handling.world.family.MapleFamily;
/*      */ import handling.world.family.MapleFamilyBuff;
/*      */ import handling.world.family.MapleFamilyCharacter;
/*      */ import handling.world.guild.MapleBBSThread;
/*      */ import handling.world.guild.MapleBBSThread.MapleBBSReply;
/*      */ import handling.world.guild.MapleGuild;
/*      */ import handling.world.guild.MapleGuildAlliance;
/*      */ import handling.world.guild.MapleGuildCharacter;
/*      */ import handling.world.guild.MapleGuildSkill;
/*      */ import java.awt.Point;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import server.MapleItemInformationProvider;
/*      */ import server.MapleStatEffect;
/*      */ import server.StructFamiliar;
/*      */ import server.life.PlayerNPC;
/*      */ import server.maps.MapleMap;
/*      */ import server.quest.MapleQuest;
/*      */ import server.shops.HiredMerchant;
/*      */ import server.shops.MaplePlayerShopItem;
/*      */ import tools.HexTool;
/*      */ import tools.Pair;
/*      */ import tools.StringUtil;
/*      */ import tools.data.MaplePacketLittleEndianWriter;
/*      */ 
/*      */ public class CWvsContext
/*      */ {
/*      */   public static byte[] enableActions()
/*      */   {
/* 2681 */     return updatePlayerStats(new EnumMap(MapleStat.class), true, null);
/*      */   }
/*      */ 
/*      */   public static byte[] updatePlayerStats(Map<MapleStat, Integer> stats, MapleCharacter chr) {
/* 2685 */     return updatePlayerStats(stats, false, chr);
/*      */   }
/*      */ 
/*      */   public static byte[] updatePlayerStats(Map<MapleStat, Integer> mystats, boolean itemReaction, MapleCharacter chr) {
/* 2689 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2691 */     mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
/* 2692 */     mplew.write(itemReaction ? 1 : 0);
/* 2693 */     long updateMask = 0L;
/* 2694 */     for (MapleStat statupdate : mystats.keySet()) {
/* 2695 */       updateMask |= statupdate.getValue();
/*      */     }
/* 2697 */     mplew.writeLong(updateMask);
        for (final Entry<MapleStat, Integer> statupdate : mystats.entrySet()) {
            switch (statupdate.getKey()) {
                case SKIN:
                case LEVEL:
                case FATIGUE:
                case BATTLE_RANK:
                case ICE_GAGE: // not sure..
/* 2705 */         mplew.write(((Integer)statupdate.getValue()).byteValue());
/* 2706 */         break;
                case JOB:
                case STR:
                case DEX:
                case INT:
                case LUK:
                case AVAILABLEAP:
/* 2713 */         mplew.writeShort(((Integer)statupdate.getValue()).shortValue());
/* 2714 */         break;
/*      */        case AVAILABLESP:
/* 2716 */         if (GameConstants.isSeparatedSp(chr.getJob())) {
/* 2717 */           mplew.write(chr.getRemainingSpSize());
/* 2718 */           for (int i = 0; i < chr.getRemainingSps().length; i++)
/* 2719 */             if (chr.getRemainingSp(i) > 0) {
/* 2720 */               mplew.write(i + 1);
/* 2721 */               mplew.write(chr.getRemainingSp(i));
/*      */             }
/*      */         }
/*      */         else {
/* 2725 */           mplew.writeShort(chr.getRemainingSp());
/*      */         }
/* 2727 */         break;
/*      */       case TRAIT_LIMIT:
/* 2729 */         mplew.writeInt(((Integer)statupdate.getValue()).intValue());
/* 2730 */         mplew.writeInt(((Integer)statupdate.getValue()).intValue());
/* 2731 */         mplew.writeInt(((Integer)statupdate.getValue()).intValue());
/* 2732 */         break;
/*      */        case PET:
/* 2734 */         mplew.writeLong(((Integer)statupdate.getValue()).intValue());
/* 2735 */         mplew.writeLong(((Integer)statupdate.getValue()).intValue());
/* 2736 */         mplew.writeLong(((Integer)statupdate.getValue()).intValue());
/* 2737 */         break;
/*      */       default:
/* 2739 */         mplew.writeInt(((Integer)statupdate.getValue()).intValue());
/*      */       }
/*      */     }
/*      */ 
/* 2743 */     if ((updateMask == 0L) && (!itemReaction)) {
/* 2744 */       mplew.write(1);
/*      */     }
/* 2746 */     mplew.write(0);
/* 2747 */     mplew.write(0);
/*      */ 
/* 2749 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] temporaryStats_Aran() {
/* 2753 */     Map stats = new EnumMap(MapleStat.Temp.class);
/*      */ 
/* 2755 */     stats.put(MapleStat.Temp.STR, Integer.valueOf(999));
/* 2756 */     stats.put(MapleStat.Temp.DEX, Integer.valueOf(999));
/* 2757 */     stats.put(MapleStat.Temp.INT, Integer.valueOf(999));
/* 2758 */     stats.put(MapleStat.Temp.LUK, Integer.valueOf(999));
/* 2759 */     stats.put(MapleStat.Temp.WATK, Integer.valueOf(255));
/* 2760 */     stats.put(MapleStat.Temp.ACC, Integer.valueOf(999));
/* 2761 */     stats.put(MapleStat.Temp.AVOID, Integer.valueOf(999));
/* 2762 */     stats.put(MapleStat.Temp.SPEED, Integer.valueOf(140));
/* 2763 */     stats.put(MapleStat.Temp.JUMP, Integer.valueOf(120));
/*      */ 
/* 2765 */     return temporaryStats(stats);
/*      */   }
/*      */ 
/*      */   public static byte[] temporaryStats_Balrog(MapleCharacter chr) {
/* 2769 */     Map stats = new EnumMap(MapleStat.Temp.class);
/*      */ 
/* 2771 */     int offset = 1 + (chr.getLevel() - 90) / 20;
/* 2772 */     stats.put(MapleStat.Temp.STR, Integer.valueOf(chr.getStat().getTotalStr() / offset));
/* 2773 */     stats.put(MapleStat.Temp.DEX, Integer.valueOf(chr.getStat().getTotalDex() / offset));
/* 2774 */     stats.put(MapleStat.Temp.INT, Integer.valueOf(chr.getStat().getTotalInt() / offset));
/* 2775 */     stats.put(MapleStat.Temp.LUK, Integer.valueOf(chr.getStat().getTotalLuk() / offset));
/* 2776 */     stats.put(MapleStat.Temp.WATK, Integer.valueOf(chr.getStat().getTotalWatk() / offset));
/* 2777 */     stats.put(MapleStat.Temp.MATK, Integer.valueOf(chr.getStat().getTotalMagic() / offset));
/*      */ 
/* 2779 */     return temporaryStats(stats);
/*      */   }
/*      */ 
/*      */   public static byte[] temporaryStats(Map<MapleStat.Temp, Integer> mystats) {
/* 2783 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2785 */     mplew.writeShort(SendPacketOpcode.TEMP_STATS.getValue());
/* 2786 */     int updateMask = 0;
/* 2787 */     for (MapleStat.Temp statupdate : mystats.keySet()) {
/* 2788 */       updateMask |= statupdate.getValue();
/*      */     }
/* 2790 */    mplew.writeInt(updateMask);
        for (final Entry<MapleStat.Temp, Integer> statupdate : mystats.entrySet()) {
      switch (statupdate.getKey()) {
                case SPEED:
                case JUMP:
                case UNKNOWN:
/* 2796 */         mplew.write(((Integer)statupdate.getValue()).byteValue());
/* 2797 */         break;
/*      */       default:
/* 2799 */         mplew.writeShort(((Integer)statupdate.getValue()).shortValue());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2804 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] temporaryStats_Reset() {
/* 2808 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2810 */     mplew.writeShort(SendPacketOpcode.TEMP_STATS_RESET.getValue());
/*      */ 
/* 2812 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateSkills(Map<Skill, SkillEntry> update) {
/* 2816 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(7 + update.size() * 20);
/*      */ 
/* 2818 */     mplew.writeShort(SendPacketOpcode.UPDATE_SKILLS.getValue());
/* 2819 */     mplew.write(1);
/* 2820 */     mplew.write(0);
/* 2821 */     mplew.writeShort(update.size());
/* 2822 */     for (Map.Entry z : update.entrySet()) {
/* 2823 */       mplew.writeInt(((Skill)z.getKey()).getId());
/* 2824 */       mplew.writeInt(((SkillEntry)z.getValue()).skillevel);
/* 2825 */       mplew.writeInt(((SkillEntry)z.getValue()).masterlevel);
/* 2826 */       PacketHelper.addExpirationTime(mplew, ((SkillEntry)z.getValue()).expiration);
/*      */     }
/* 2828 */     mplew.write(4);
/*      */ 
/* 2830 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] giveFameErrorResponse(int op) {
/* 2834 */     return OnFameResult(op, null, true, 0);
/*      */   }
/*      */ 
/*      */   public static byte[] OnFameResult(int op, String charname, boolean raise, int newFame) {
/* 2838 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2845 */     mplew.writeShort(SendPacketOpcode.FAME_RESPONSE.getValue());
/* 2846 */     mplew.write(op);
/* 2847 */     if ((op == 0) || (op == 5)) {
/* 2848 */       mplew.writeMapleAsciiString(charname == null ? "" : charname);
/* 2849 */       mplew.write(raise ? 1 : 0);
/* 2850 */       if (op == 0) {
/* 2851 */         mplew.writeInt(newFame);
/*      */       }
/*      */     }
/*      */ 
/* 2855 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] BombLieDetector(boolean error, int mapid, int channel) {
/* 2859 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2861 */     mplew.writeShort(SendPacketOpcode.BOMB_LIE_DETECTOR.getValue());
/* 2862 */     mplew.write(error ? 2 : 1);
/* 2863 */     mplew.writeInt(mapid);
/* 2864 */     mplew.writeInt(channel);
/*      */ 
/* 2866 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] report(int mode) {
/* 2870 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2880 */     mplew.writeShort(SendPacketOpcode.REPORT_RESPONSE.getValue());
/* 2881 */     mplew.write(mode);
/* 2882 */     if (mode == 2) {
/* 2883 */       mplew.write(0);
/* 2884 */       mplew.writeInt(1);
/*      */     }
/*      */ 
/* 2887 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] OnSetClaimSvrAvailableTime(int from, int to) {
/* 2891 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(4);
/*      */ 
/* 2894 */     mplew.writeShort(SendPacketOpcode.REPORT_TIME.getValue());
/* 2895 */     mplew.write(from);
/* 2896 */     mplew.write(to);
/*      */ 
/* 2898 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] OnClaimSvrStatusChanged(boolean enable) {
/* 2902 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
/*      */ 
/* 2904 */     mplew.writeShort(SendPacketOpcode.REPORT_STATUS.getValue());
/* 2905 */     mplew.write(enable ? 1 : 0);
/*      */ 
/* 2907 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateMount(MapleCharacter chr, boolean levelup) {
/* 2911 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2913 */     mplew.writeShort(SendPacketOpcode.UPDATE_MOUNT.getValue());
/* 2914 */     mplew.writeInt(chr.getId());
/* 2915 */     mplew.writeInt(chr.getMount().getLevel());
/* 2916 */     mplew.writeInt(chr.getMount().getExp());
/* 2917 */     mplew.writeInt(chr.getMount().getFatigue());
/* 2918 */     mplew.write(levelup ? 1 : 0);
/*      */ 
/* 2920 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getShowQuestCompletion(int id) {
/* 2924 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2926 */     mplew.writeShort(SendPacketOpcode.SHOW_QUEST_COMPLETION.getValue());
/* 2927 */     mplew.writeShort(id);
/*      */ 
/* 2929 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] useSkillBook(MapleCharacter chr, int skillid, int maxlevel, boolean canuse, boolean success) {
/* 2933 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2935 */     mplew.writeShort(SendPacketOpcode.USE_SKILL_BOOK.getValue());
/* 2936 */     mplew.write(0);
/* 2937 */     mplew.writeInt(chr.getId());
/* 2938 */     mplew.write(1);
/* 2939 */     mplew.writeInt(skillid);
/* 2940 */     mplew.writeInt(maxlevel);
/* 2941 */     mplew.write(canuse ? 1 : 0);
/* 2942 */     mplew.write(success ? 1 : 0);
/*      */ 
/* 2944 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] useAPSPReset(boolean spReset, int cid) {
/* 2948 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2950 */     mplew.writeShort(spReset ? SendPacketOpcode.SP_RESET.getValue() : SendPacketOpcode.AP_RESET.getValue());
/* 2951 */     mplew.write(1);
/* 2952 */     mplew.writeInt(cid);
/* 2953 */     mplew.write(1);
/*      */ 
/* 2955 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] expandCharacterSlots(int mode) {
/* 2959 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2964 */     mplew.writeShort(SendPacketOpcode.EXPAND_CHARACTER_SLOTS.getValue());
/* 2965 */     mplew.writeInt(mode);
/* 2966 */     mplew.write(0);
/*      */ 
/* 2968 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] finishedGather(int type) {
/* 2972 */     return gatherSortItem(true, type);
/*      */   }
/*      */ 
/*      */   public static byte[] finishedSort(int type) {
/* 2976 */     return gatherSortItem(false, type);
/*      */   }
/*      */ 
/*      */   public static byte[] gatherSortItem(boolean gather, int type) {
/* 2980 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2982 */     mplew.writeShort(gather ? SendPacketOpcode.FINISH_GATHER.getValue() : SendPacketOpcode.FINISH_SORT.getValue());
/* 2983 */     mplew.write(1);
/* 2984 */     mplew.write(type);
/*      */ 
/* 2986 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateGender(MapleCharacter chr) {
/* 2990 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2992 */     mplew.writeShort(SendPacketOpcode.UPDATE_GENDER.getValue());
/* 2993 */     mplew.write(chr.getGender());
/*      */ 
/* 2995 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] charInfo(MapleCharacter chr, boolean isSelf) {
/* 2999 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3001 */     mplew.writeShort(SendPacketOpcode.CHAR_INFO.getValue());
/* 3002 */     mplew.writeInt(chr.getId());
/* 3003 */     mplew.write(chr.getLevel());
/* 3004 */     mplew.writeShort(chr.getJob());
/* 3005 */     mplew.writeShort(chr.getSubcategory());
/* 3006 */     mplew.write(chr.getStat().pvpRank);
/* 3007 */     mplew.writeInt(chr.getFame());
/* 3008 */     mplew.write(chr.getMarriageId() > 0 ? 1 : 0);
/* 3009 */     List prof = chr.getProfessions();
/* 3010 */     mplew.write(prof.size());
/* 3011 */     for (Iterator i$ = prof.iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
/* 3012 */       mplew.writeShort(i);
/*      */     }
/* 3014 */     if (chr.getGuildId() <= 0) {
/* 3015 */       mplew.writeMapleAsciiString("-");
/* 3016 */       mplew.writeMapleAsciiString("");
/*      */     } else {
/* 3018 */       MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
/* 3019 */       if (gs != null) {
/* 3020 */         mplew.writeMapleAsciiString(gs.getName());
/* 3021 */         if (gs.getAllianceId() > 0) {
/* 3022 */           MapleGuildAlliance allianceName = World.Alliance.getAlliance(gs.getAllianceId());
/* 3023 */           if (allianceName != null)
/* 3024 */             mplew.writeMapleAsciiString(allianceName.getName());
/*      */           else
/* 3026 */             mplew.writeMapleAsciiString("");
/*      */         }
/*      */         else {
/* 3029 */           mplew.writeMapleAsciiString("");
/*      */         }
/*      */       } else {
/* 3032 */         mplew.writeMapleAsciiString("-");
/* 3033 */         mplew.writeMapleAsciiString("");
/*      */       }
/*      */     }
/*      */ 
/* 3037 */     mplew.write(isSelf ? 1 : 0);
/* 3038 */     mplew.write(0);
/*      */ 
/* 3040 */     byte index = 1;
/* 3041 */     for (MaplePet pet : chr.getPets()) {
/* 3042 */       if (pet.getSummoned()) {
/* 3043 */         mplew.write(index);
/* 3044 */         mplew.writeInt(pet.getPetItemId());
/* 3045 */         mplew.writeMapleAsciiString(pet.getName());
/* 3046 */         mplew.write(pet.getLevel());
/* 3047 */         mplew.writeShort(pet.getCloseness());
/* 3048 */         mplew.write(pet.getFullness());
/* 3049 */         mplew.writeShort(0);
/* 3050 */         Item inv = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short)(byte)(index == 2 ? -130 : index == 1 ? -114 : -138));
/* 3051 */         mplew.writeInt(inv == null ? 0 : inv.getItemId());
/* 3052 */         index = (byte)(index + 1);
/*      */       }
/*      */     }
/* 3055 */     mplew.write(0);
/*      */ 
/* 3057 */     if ((chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-18) != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-19) != null)) {
/* 3058 */       MapleMount mount = chr.getMount();
/* 3059 */       mplew.write(1);
/* 3060 */       mplew.writeInt(mount.getLevel());
/* 3061 */       mplew.writeInt(mount.getExp());
/* 3062 */       mplew.writeInt(mount.getFatigue());
/*      */     } else {
/* 3064 */       mplew.write(0);
/*      */     }
/*      */ 
/* 3067 */     int wishlistSize = chr.getWishlistSize();
/* 3068 */     mplew.write(wishlistSize);
/* 3069 */     if (wishlistSize > 0) {
/* 3070 */       int[] wishlist = chr.getWishlist();
/* 3071 */       for (int x = 0; x < wishlistSize; x++) {
/* 3072 */         mplew.writeInt(wishlist[x]);
/*      */       }
/*      */     }
/*      */ 
/* 3076 */     Item medal = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte)-46);
/* 3077 */     mplew.writeInt(medal == null ? 0 : medal.getItemId());
/* 3078 */     List<Pair<Integer, Long>> medalQuests = chr.getCompletedMedals();
/* 3079 */     mplew.writeShort(medalQuests.size());
/* 3080 */     for (Pair x : medalQuests) {
/* 3081 */       mplew.writeShort(((Integer)x.left).intValue());
/* 3082 */       mplew.writeLong(((Long)x.right).longValue());
/*      */     }
/*      */ 
/* 3085 */     for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {
/* 3086 */       mplew.write(chr.getTrait(t).getLevel());
/*      */     }
/*      */ 
/* 3089 */     List chairs = new ArrayList();
/* 3090 */     for (Item i : chr.getInventory(MapleInventoryType.SETUP).newList()) {
/* 3091 */       if ((i.getItemId() / 10000 == 301) && (!chairs.contains(Integer.valueOf(i.getItemId())))) {
/* 3092 */         chairs.add(Integer.valueOf(i.getItemId()));
/*      */       }
/*      */     }
/* 3095 */     mplew.writeInt(chairs.size());
/* 3096 */     for (Iterator i$ = chairs.iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
/* 3097 */       mplew.writeInt(i);
/*      */     }
/*      */ 
/* 3100 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getMonsterBookInfo(MapleCharacter chr) {
/* 3104 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3106 */     mplew.writeShort(SendPacketOpcode.BOOK_INFO.getValue());
/* 3107 */     mplew.writeInt(chr.getId());
/* 3108 */     mplew.writeInt(chr.getLevel());
/* 3109 */     chr.getMonsterBook().writeCharInfoPacket(mplew);
/*      */ 
/* 3111 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] spawnPortal(int townId, int targetId, int skillId, Point pos) {
/* 3115 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3117 */     mplew.writeShort(SendPacketOpcode.SPAWN_PORTAL.getValue());
/* 3118 */     mplew.writeInt(townId);
/* 3119 */     mplew.writeInt(targetId);
/* 3120 */     if ((townId != 999999999) && (targetId != 999999999)) {
/* 3121 */       mplew.writeInt(skillId);
/* 3122 */       mplew.writePos(pos);
/*      */     }
/*      */ 
/* 3125 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] mechPortal(Point pos) {
/* 3129 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3131 */     mplew.writeShort(SendPacketOpcode.MECH_PORTAL.getValue());
/* 3132 */     mplew.writePos(pos);
/*      */ 
/* 3134 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] echoMegaphone(String name, String message) {
/* 3138 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3140 */     mplew.writeShort(SendPacketOpcode.ECHO_MESSAGE.getValue());
/* 3141 */     mplew.write(0);
/* 3142 */     mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
/* 3143 */     mplew.writeMapleAsciiString(name);
/* 3144 */     mplew.writeMapleAsciiString(message);
/*      */ 
/* 3146 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showQuestMsg(String msg) {
/* 3150 */     return serverNotice(5, msg);
/*      */   }
/*      */ 
/*      */   public static byte[] Mulung_Pts(int recv, int total) {
/* 3154 */     return showQuestMsg(new StringBuilder().append("You have received ").append(recv).append(" training points, for the accumulated total of ").append(total).append(" training points.").toString());
/*      */   }
/*      */ 
/*      */   public static byte[] serverMessage(String message) {
/* 3158 */     return serverMessage(4, 0, message, false);
/*      */   }
/*      */ 
/*      */   public static byte[] serverNotice(int type, String message) {
/* 3162 */     return serverMessage(type, 0, message, false);
/*      */   }
/*      */ 
/*      */   public static byte[] serverNotice(int type, int channel, String message) {
/* 3166 */     return serverMessage(type, channel, message, false);
/*      */   }
/*      */ 
/*      */   public static byte[] serverNotice(int type, int channel, String message, boolean smegaEar) {
/* 3170 */     return serverMessage(type, channel, message, smegaEar);
/*      */   }
/*      */ 
/*      */   private static byte[] serverMessage(int type, int channel, String message, boolean megaEar) {
/* 3174 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3200 */     mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
/* 3201 */     mplew.write(type);
/* 3202 */     if (type == 4) {
/* 3203 */       mplew.write(1);
/*      */     }
/* 3205 */     if ((type != 23) && (type != 24)) {
/* 3206 */       mplew.writeMapleAsciiString(message);
/*      */     }
/* 3208 */     switch (type) {
/*      */     case 3:
/*      */     case 22:
/*      */     case 25:
/*      */     case 26:
/* 3213 */       mplew.write(channel - 1);
/* 3214 */       mplew.write(megaEar ? 1 : 0);
/* 3215 */       break;
/*      */     case 9:
/* 3217 */       mplew.write(channel - 1);
/* 3218 */       break;
/*      */     case 12:
/* 3220 */       mplew.writeInt(channel);
/* 3221 */       break;
/*      */     case 6:
/*      */     case 11:
/*      */     case 20:
/* 3225 */       mplew.writeInt((channel >= 1000000) && (channel < 6000000) ? channel : 0);
/*      */ 
/* 3227 */       break;
/*      */     case 24:
/* 3229 */       mplew.writeShort(0);
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/*      */     case 8:
/*      */     case 10:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 21:
/* 3233 */     case 23: } return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getGachaponMega(String name, String message, Item item, byte rareness, String gacha) {
/* 3237 */     return getGachaponMega(name, message, item, rareness, false, gacha);
/*      */   }
/*      */ 
/*      */   public static byte[] getGachaponMega(String name, String message, Item item, byte rareness, boolean dragon, String gacha) {
/* 3241 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3243 */     mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
/* 3244 */     mplew.write(13);
/* 3245 */     mplew.writeMapleAsciiString(new StringBuilder().append(name).append(message).toString());
/* 3246 */     if (!dragon) {
/* 3247 */       mplew.writeInt(0);
/* 3248 */       mplew.writeInt(item.getItemId());
/*      */     }
/* 3250 */     mplew.writeMapleAsciiString(gacha);
/* 3251 */     PacketHelper.addItemInfo(mplew, item);
/*      */ 
/* 3253 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getAniMsg(int questID, int time) {
/* 3257 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3259 */     mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
/* 3260 */     mplew.write(23);
/* 3261 */     mplew.writeShort(questID);
/* 3262 */     mplew.writeInt(time);
/*      */ 
/* 3264 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] tripleSmega(List<String> message, boolean ear, int channel) {
/* 3268 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3270 */     mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
/* 3271 */     mplew.write(10);
/* 3272 */     if (message.get(0) != null) {
/* 3273 */       mplew.writeMapleAsciiString((String)message.get(0));
/*      */     }
/* 3275 */     mplew.write(message.size());
/* 3276 */     for (int i = 1; i < message.size(); i++) {
/* 3277 */       if (message.get(i) != null) {
/* 3278 */         mplew.writeMapleAsciiString((String)message.get(i));
/*      */       }
/*      */     }
/* 3281 */     mplew.write(channel - 1);
/* 3282 */     mplew.write(ear ? 1 : 0);
/*      */ 
/* 3284 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] itemMegaphone(String msg, boolean whisper, int channel, Item item) {
/* 3288 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3290 */     mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
/* 3291 */     mplew.write(8);
/* 3292 */     mplew.writeMapleAsciiString(msg);
/* 3293 */     mplew.write(channel - 1);
/* 3294 */     mplew.write(whisper ? 1 : 0);
/* 3295 */     PacketHelper.addItemPosition(mplew, item, true, false);
/* 3296 */     if (item != null) {
/* 3297 */       PacketHelper.addItemInfo(mplew, item);
/*      */     }
/*      */ 
/* 3300 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getPeanutResult(int itemId, short quantity, int itemId2, short quantity2, int ourItem) {
/* 3304 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3306 */     mplew.writeShort(SendPacketOpcode.PIGMI_REWARD.getValue());
/* 3307 */     mplew.writeInt(itemId);
/* 3308 */     mplew.writeShort(quantity);
/* 3309 */     mplew.writeInt(ourItem);
/* 3310 */     mplew.writeInt(itemId2);
/* 3311 */     mplew.writeInt(quantity2);
/*      */ 
/* 3313 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getOwlOpen() {
/* 3317 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3319 */     mplew.writeShort(SendPacketOpcode.OWL_OF_MINERVA.getValue());
/* 3320 */     mplew.write(9);
/* 3321 */     mplew.write(GameConstants.owlItems.length);
/* 3322 */     for (int i : GameConstants.owlItems) {
/* 3323 */       mplew.writeInt(i);
/*      */     }
/*      */ 
/* 3326 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getOwlSearched(int itemSearch, List<HiredMerchant> hms) {
/* 3330 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3332 */     mplew.writeShort(SendPacketOpcode.OWL_OF_MINERVA.getValue());
/* 3333 */     mplew.write(8);
/* 3334 */     mplew.writeInt(0);
/* 3335 */     mplew.writeInt(itemSearch);
/* 3336 */     int size = 0;
/*      */ 
 for (HiredMerchant hm : hms) {
            size += hm.searchItem(itemSearch).size();
        }
       
  
/* 3341 */     mplew.writeInt(size);
                  for (HiredMerchant hm : hms) {
/* 3342 */     for (Iterator i = hms.iterator(); i.hasNext(); ) 
                { 
               hm = (HiredMerchant)i.next();
                final List<MaplePlayerShopItem> items = hm.searchItem(itemSearch);
/* 3344 */       for (MaplePlayerShopItem item : items) {
/* 3345 */         mplew.writeMapleAsciiString(hm.getOwnerName());
/* 3346 */         mplew.writeInt(hm.getMap().getId());
/* 3347 */         mplew.writeMapleAsciiString(hm.getDescription());
/* 3348 */         mplew.writeInt(item.item.getQuantity());
/* 3349 */         mplew.writeInt(item.bundles);
/* 3350 */         mplew.writeInt(item.price);
/* 3351 */         switch (2) {
/*      */         case 0:
/* 3353 */           mplew.writeInt(hm.getOwnerId());
/* 3354 */           break;
/*      */         case 1:
/* 3356 */           mplew.writeInt(hm.getStoreId());
/* 3357 */           break;
/*      */         default:
/* 3359 */           mplew.writeInt(hm.getObjectId());
/*      */         }
/*      */ 
/* 3362 */         mplew.write(hm.getFreeSlot() == -1 ? 1 : 0);
/* 3363 */         mplew.write(GameConstants.getInventoryType(itemSearch).getType());
/* 3364 */         if (GameConstants.getInventoryType(itemSearch) == MapleInventoryType.EQUIP)
/* 3365 */           PacketHelper.addItemInfo(mplew, item.item);
/*      */       }
/*      */     }
              }
/* 3370 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getOwlMessage(int msg) {
/* 3374 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
/*      */ 
/* 3386 */     mplew.writeShort(SendPacketOpcode.OWL_RESULT.getValue());
/* 3387 */     mplew.write(msg);
/*      */ 
/* 3389 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendEngagementRequest(String name, int cid) {
/* 3393 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3395 */     mplew.writeShort(SendPacketOpcode.ENGAGE_REQUEST.getValue());
/* 3396 */     mplew.write(0);
/* 3397 */     mplew.writeMapleAsciiString(name);
/* 3398 */     mplew.writeInt(cid);
/*      */ 
/* 3400 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendEngagement(byte msg, int item, MapleCharacter male, MapleCharacter female) {
/* 3404 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3427 */     mplew.writeShort(SendPacketOpcode.ENGAGE_RESULT.getValue());
/* 3428 */     mplew.write(msg);
/* 3429 */     if ((msg == 11) || (msg == 12)) {
/* 3430 */       mplew.writeInt(0);
/* 3431 */       mplew.writeInt(male.getId());
/* 3432 */       mplew.writeInt(female.getId());
/* 3433 */       mplew.writeShort(1);
/* 3434 */       mplew.writeInt(item);
/* 3435 */       mplew.writeInt(item);
/* 3436 */       mplew.writeAsciiString(male.getName(), 13);
/* 3437 */       mplew.writeAsciiString(female.getName(), 13);
/* 3438 */     } else if (msg == 15) {
/* 3439 */       mplew.writeAsciiString("Male", 13);
/* 3440 */       mplew.writeAsciiString("Female", 13);
/* 3441 */       mplew.writeShort(0);
/*      */     }
/*      */ 
/* 3444 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendWeddingGive() {
/* 3448 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3450 */     mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
/* 3451 */     mplew.write(9);
/* 3452 */     mplew.write(0);
/*      */ 
/* 3454 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendWeddingReceive() {
/* 3458 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3460 */     mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
/* 3461 */     mplew.write(10);
/* 3462 */     mplew.writeLong(-1L);
/* 3463 */     mplew.writeInt(0);
/* 3464 */     mplew.write(0);
/*      */ 
/* 3466 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] giveWeddingItem() {
/* 3470 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3472 */     mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
/* 3473 */     mplew.write(11);
/* 3474 */     mplew.write(0);
/* 3475 */     mplew.writeLong(0L);
/* 3476 */     mplew.write(0);
/*      */ 
/* 3478 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] receiveWeddingItem() {
/* 3482 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3484 */     mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
/* 3485 */     mplew.write(15);
/* 3486 */     mplew.writeLong(0L);
/* 3487 */     mplew.write(0);
/*      */ 
/* 3489 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendCashPetFood(boolean success, byte index) {
/* 3493 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3 + (success ? 1 : 0));
/*      */ 
/* 3495 */     mplew.writeShort(SendPacketOpcode.USE_CASH_PET_FOOD.getValue());
/* 3496 */     mplew.write(success ? 0 : 1);
/* 3497 */     if (success) {
/* 3498 */       mplew.write(index);
/*      */     }
/*      */ 
/* 3501 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] yellowChat(String msg) {
/* 3505 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3507 */     mplew.writeShort(SendPacketOpcode.YELLOW_CHAT.getValue());
/* 3508 */     mplew.write(-1);
/* 3509 */     mplew.writeMapleAsciiString(msg);
/*      */ 
/* 3511 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] shopDiscount(int percent) {
/* 3515 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3517 */     mplew.writeShort(SendPacketOpcode.SHOP_DISCOUNT.getValue());
/* 3518 */     mplew.write(percent);
/*      */ 
/* 3520 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] catchMob(int mobid, int itemid, byte success) {
/* 3524 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3526 */     mplew.writeShort(SendPacketOpcode.CATCH_MOB.getValue());
/* 3527 */     mplew.write(success);
/* 3528 */     mplew.writeInt(itemid);
/* 3529 */     mplew.writeInt(mobid);
/*      */ 
/* 3531 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] spawnPlayerNPC(PlayerNPC npc) {
/* 3535 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3537 */     mplew.writeShort(SendPacketOpcode.PLAYER_NPC.getValue());
/* 3538 */     mplew.write(1);
/* 3539 */     mplew.writeInt(npc.getId());
/* 3540 */     mplew.writeMapleAsciiString(npc.getName());
/* 3541 */     PacketHelper.addCharLook(mplew, npc, true);
/*      */ 
/* 3543 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] disabledNPC(List<Integer> ids) {
/* 3547 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3 + ids.size() * 4);
/*      */ 
/* 3549 */     mplew.writeShort(SendPacketOpcode.DISABLE_NPC.getValue());
/* 3550 */     mplew.write(ids.size());
/* 3551 */     for (Integer i : ids) {
/* 3552 */       mplew.writeInt(i.intValue());
/*      */     }
/*      */ 
/* 3555 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getCard(int itemid, int level) {
/* 3559 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3561 */     mplew.writeShort(SendPacketOpcode.GET_CARD.getValue());
/* 3562 */     mplew.write(itemid > 0 ? 1 : 0);
/* 3563 */     if (itemid > 0) {
/* 3564 */       mplew.writeInt(itemid);
/* 3565 */       mplew.writeInt(level);
/*      */     }
/* 3567 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] changeCardSet(int set) {
/* 3571 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3573 */     mplew.writeShort(SendPacketOpcode.CARD_SET.getValue());
/* 3574 */     mplew.writeInt(set);
/*      */ 
/* 3576 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] upgradeBook(Item book, MapleCharacter chr) {
/* 3580 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3582 */     mplew.writeShort(SendPacketOpcode.BOOK_STATS.getValue());
/* 3583 */     mplew.writeInt(book.getPosition());
/* 3584 */     PacketHelper.addItemInfo(mplew, book, chr);
/*      */ 
/* 3586 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getCardDrops(int cardid, List<Integer> drops) {
/* 3590 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3592 */     mplew.writeShort(SendPacketOpcode.CARD_DROPS.getValue());
/* 3593 */     mplew.writeInt(cardid);
/* 3594 */     mplew.writeShort(drops == null ? 0 : drops.size());
/* 3595 */     if (drops != null) {
/* 3596 */       for (Integer de : drops) {
/* 3597 */         mplew.writeInt(de.intValue());
/*      */       }
/*      */     }
/*      */ 
/* 3601 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getFamiliarInfo(MapleCharacter chr) {
/* 3605 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3607 */     mplew.writeShort(SendPacketOpcode.FAMILIAR_INFO.getValue());
/* 3608 */     mplew.writeInt(chr.getFamiliars().size());
/* 3609 */     for (MonsterFamiliar mf : chr.getFamiliars().values()) {
/* 3610 */       mf.writeRegisterPacket(mplew, true);
/*      */     }
/* 3612 */     List<Pair<Integer, Long>> size = new ArrayList<>();
/* 3613 */     for (Item i : chr.getInventory(MapleInventoryType.USE).list()) {
/* 3614 */       if (i.getItemId() / 10000 == 287) {
/* 3615 */         StructFamiliar f = MapleItemInformationProvider.getInstance().getFamiliarByItem(i.getItemId());
/* 3616 */         if (f != null) {
/* 3617 */           size.add(new Pair(Integer.valueOf(f.familiar), Long.valueOf(i.getInventoryId())));
/*      */         }
/*      */       }
/*      */     }
/* 3621 */     mplew.writeInt(size.size());
/* 3622 */     for (Pair s : size) {
/* 3623 */       mplew.writeInt(chr.getId());
/* 3624 */       mplew.writeInt(((Integer)s.left).intValue());
/* 3625 */       mplew.writeLong(((Long)s.right).longValue());
/* 3626 */       mplew.write(0);
/*      */     }
/* 3628 */     size.clear();
/*      */ 
/* 3630 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] MulungEnergy(int energy) {
/* 3634 */     return sendPyramidEnergy("energy", String.valueOf(energy));
/*      */   }
/*      */ 
/*      */   public static byte[] sendPyramidEnergy(String type, String amount) {
/* 3638 */     return sendString(1, type, amount);
/*      */   }
/*      */ 
/*      */   public static byte[] sendGhostPoint(String type, String amount) {
/* 3642 */     return sendString(2, type, amount);
/*      */   }
/*      */ 
/*      */   public static byte[] sendGhostStatus(String type, String amount) {
/* 3646 */     return sendString(3, type, amount);
/*      */   }
/*      */ 
/*      */   public static byte[] sendString(int type, String object, String amount) {
/* 3650 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3652 */     switch (type) {
/*      */     case 1:
/* 3654 */       mplew.writeShort(SendPacketOpcode.SESSION_VALUE.getValue());
/* 3655 */       break;
/*      */     case 2:
/* 3657 */       mplew.writeShort(SendPacketOpcode.PARTY_VALUE.getValue());
/* 3658 */       break;
/*      */     case 3:
/* 3660 */       mplew.writeShort(SendPacketOpcode.MAP_VALUE.getValue());
/*      */     }
/*      */ 
/* 3663 */     mplew.writeMapleAsciiString(object);
/* 3664 */     mplew.writeMapleAsciiString(amount);
/*      */ 
/* 3666 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] fairyPendantMessage(int termStart, int incExpR) {
/* 3670 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(14);
/*      */ 
/* 3672 */     mplew.writeShort(SendPacketOpcode.EXP_BONUS.getValue());
/* 3673 */     mplew.writeInt(17);
/* 3674 */     mplew.writeInt(0);
/*      */ 
/* 3677 */     mplew.writeInt(incExpR);
/*      */ 
/* 3679 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] potionDiscountMessage(int type, int potionDiscR) {
/* 3683 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);
/*      */ 
/* 3685 */     mplew.writeShort(SendPacketOpcode.POTION_BONUS.getValue());
/* 3686 */     mplew.writeInt(type);
/* 3687 */     mplew.writeInt(potionDiscR);
/*      */ 
/* 3689 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendLevelup(boolean family, int level, String name) {
/* 3693 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3695 */     mplew.writeShort(SendPacketOpcode.LEVEL_UPDATE.getValue());
/* 3696 */     mplew.write(family ? 1 : 2);
/* 3697 */     mplew.writeInt(level);
/* 3698 */     mplew.writeMapleAsciiString(name);
/*      */ 
/* 3700 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendMarriage(boolean family, String name) {
/* 3704 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3706 */     mplew.writeShort(SendPacketOpcode.MARRIAGE_UPDATE.getValue());
/* 3707 */     mplew.write(family ? 1 : 0);
/* 3708 */     mplew.writeMapleAsciiString(name);
/*      */ 
/* 3710 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendJobup(boolean family, int jobid, String name) {
/* 3714 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3716 */     mplew.writeShort(SendPacketOpcode.JOB_UPDATE.getValue());
/* 3717 */     mplew.write(family ? 1 : 0);
/* 3718 */     mplew.writeInt(jobid);
/* 3719 */     mplew.writeMapleAsciiString(new StringBuilder().append(!family ? "> " : "").append(name).toString());
/*      */ 
/* 3721 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getAvatarMega(MapleCharacter chr, int channel, int itemId, List<String> text, boolean ear) {
/* 3725 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3727 */     mplew.writeShort(SendPacketOpcode.AVATAR_MEGA.getValue());
/* 3728 */     mplew.writeInt(itemId);
/* 3729 */     mplew.writeMapleAsciiString(chr.getName());
/* 3730 */     for (String i : text) {
/* 3731 */       mplew.writeMapleAsciiString(i);
/*      */     }
/* 3733 */     mplew.writeInt(channel - 1);
/* 3734 */     mplew.write(ear ? 1 : 0);
/* 3735 */     PacketHelper.addCharLook(mplew, chr, true);
/*      */ 
/* 3737 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] GMPoliceMessage(boolean dc) {
/* 3741 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);
/*      */ 
/* 3743 */     mplew.writeShort(SendPacketOpcode.GM_POLICE.getValue());
/* 3744 */     mplew.write(dc ? 10 : 0);
/*      */ 
/* 3746 */     return mplew.getPacket();
/*      */   }
/*      */ 
  public static byte[] pendantSlot(boolean p) { //slot -59
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PENDANT_SLOT.getValue());
        mplew.write(p ? 1 : 0);
        return mplew.getPacket();
    }
/*      */ 
/*      */   public static byte[] followRequest(int chrid) {
/* 3759 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3761 */     mplew.writeShort(SendPacketOpcode.FOLLOW_REQUEST.getValue());
/* 3762 */     mplew.writeInt(chrid);
/*      */ 
/* 3764 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getTopMsg(String msg) {
/* 3768 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3770 */     mplew.writeShort(SendPacketOpcode.TOP_MSG.getValue());
/* 3771 */     mplew.writeMapleAsciiString(msg);
/*      */ 
/* 3773 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showMidMsg(String s, int l) {
/* 3777 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3779 */     mplew.writeShort(SendPacketOpcode.MID_MSG.getValue());
/* 3780 */     mplew.write(l);
/* 3781 */     mplew.writeMapleAsciiString(s);
/* 3782 */     mplew.write(s.length() > 0 ? 0 : 1);
/*      */ 
/* 3784 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getMidMsg(String msg, boolean keep, int index) {
/* 3788 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3790 */     mplew.writeShort(SendPacketOpcode.MID_MSG.getValue());
/* 3791 */     mplew.write(index);
/* 3792 */     mplew.writeMapleAsciiString(msg);
/* 3793 */     mplew.write(keep ? 0 : 1);
/*      */ 
/* 3795 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] clearMidMsg() {
/* 3799 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3801 */     mplew.writeShort(SendPacketOpcode.CLEAR_MID_MSG.getValue());
/*      */ 
/* 3803 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateJaguar(MapleCharacter from) {
/* 3807 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3809 */     mplew.writeShort(SendPacketOpcode.UPDATE_JAGUAR.getValue());
/* 3810 */     PacketHelper.addJaguarInfo(mplew, from);
/*      */ 
/* 3812 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] loadInformation() {
/* 3816 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3818 */     mplew.writeShort(SendPacketOpcode.YOUR_INFORMATION.getValue());
/* 3819 */     mplew.write(2);
/* 3820 */     mplew.write(0);
/* 3821 */     mplew.write(0);
/* 3822 */     mplew.writeShort(0);
/* 3823 */     mplew.writeInt(0);
/* 3824 */     mplew.writeInt(0);
/*      */ 
/* 3830 */     mplew.writeInt(0);
/*      */ 
/* 3850 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] saveInformation(boolean fail) {
/* 3854 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3856 */     mplew.writeShort(SendPacketOpcode.YOUR_INFORMATION.getValue());
/* 3857 */     mplew.write(4);
/* 3858 */     mplew.write(fail ? 0 : 1);
/*      */ 
/* 3860 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] myInfoResult() {
/* 3864 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3866 */     mplew.writeShort(SendPacketOpcode.FIND_FRIEND.getValue());
/* 3867 */     mplew.write(6);
/* 3868 */     mplew.writeInt(0);
/* 3869 */     mplew.writeInt(0);
/*      */ 
/* 3871 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] findFriendResult(List<MapleCharacter> friends) {
/* 3875 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3877 */     mplew.writeShort(SendPacketOpcode.FIND_FRIEND.getValue());
/* 3878 */     mplew.write(8);
/* 3879 */     mplew.writeShort(friends.size());
/* 3880 */     for (MapleCharacter mc : friends) {
/* 3881 */       mplew.writeInt(mc.getId());
/* 3882 */       mplew.writeMapleAsciiString(mc.getName());
/* 3883 */       mplew.write(mc.getLevel());
/* 3884 */       mplew.writeShort(mc.getJob());
/* 3885 */       mplew.writeInt(0);
/* 3886 */       mplew.writeInt(0);
/*      */     }
/*      */ 
/* 3889 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] friendFinderError() {
/* 3893 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3895 */     mplew.writeShort(SendPacketOpcode.FIND_FRIEND.getValue());
/* 3896 */     mplew.write(9);
/* 3897 */     mplew.write(12);
/*      */ 
/* 3899 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] friendCharacterInfo(MapleCharacter chr) {
/* 3903 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3905 */     mplew.writeShort(SendPacketOpcode.FIND_FRIEND.getValue());
/* 3906 */     mplew.write(11);
/* 3907 */     mplew.writeInt(chr.getId());
/* 3908 */     PacketHelper.addCharLook(mplew, chr, true);
/*      */ 
/* 3910 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] showBackgroundEffect(String eff, int value) {
/* 3914 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3916 */     mplew.writeShort(SendPacketOpcode.VISITOR.getValue());
/* 3917 */     mplew.writeMapleAsciiString(eff);
/* 3918 */     mplew.write(value);
/*      */ 
/* 3920 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] sendPinkBeanChoco() {
/* 3924 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3926 */     mplew.writeShort(SendPacketOpcode.PINKBEAN_CHOCO.getValue());
/* 3927 */     mplew.writeInt(0);
/* 3928 */     mplew.write(1);
/* 3929 */     mplew.write(0);
/* 3930 */     mplew.write(0);
/* 3931 */     mplew.writeInt(0);
/*      */ 
/* 3935 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] changeChannelMsg(int channel, String msg) {
/* 3939 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(8 + msg.length());
/*      */ 
/* 3941 */     mplew.writeShort(SendPacketOpcode.AUTO_CC_MSG.getValue());
/* 3942 */     mplew.writeInt(channel);
/* 3943 */     mplew.writeMapleAsciiString(msg);
/*      */ 
/* 3945 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] pamSongUI() {
/* 3949 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3951 */     mplew.writeShort(SendPacketOpcode.PAM_SONG.getValue());
/*      */ 
/* 3953 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] ultimateExplorer() {
/* 3957 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3959 */     mplew.writeShort(SendPacketOpcode.ULTIMATE_EXPLORER.getValue());
/*      */    
/* 3961 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] professionInfo(String skil, int level1, int level2, int chance) {
/* 3965 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3967 */     mplew.writeShort(SendPacketOpcode.PROFESSION_INFO.getValue());
/* 3968 */     mplew.writeMapleAsciiString(skil);
/* 3969 */     mplew.writeInt(level1);
/* 3970 */     mplew.writeInt(level2);
/* 3971 */     mplew.write(1);
/* 3972 */     mplew.writeInt((skil.startsWith("9200")) || (skil.startsWith("9201")) ? 100 : chance);
/*      */ 
/* 3974 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateImpTime() {
/* 3978 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3980 */     mplew.writeShort(SendPacketOpcode.UPDATE_IMP_TIME.getValue());
/* 3981 */     mplew.writeInt(0);
/* 3982 */     mplew.writeLong(0L);
/*      */ 
/* 3984 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] updateImp(MapleImp imp, int mask, int index, boolean login) {
/* 3988 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 3990 */     mplew.writeShort(SendPacketOpcode.ITEM_POT.getValue());
/* 3991 */     mplew.write(login ? 0 : 1);
/* 3992 */     mplew.writeInt(index + 1);
/* 3993 */     mplew.writeInt(mask);
/* 3994 */     if ((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) {
/* 3995 */       Pair i = MapleItemInformationProvider.getInstance().getPot(imp.getItemId());
/* 3996 */       if (i == null) {
/* 3997 */         return enableActions();
/*      */       }
/* 3999 */       mplew.writeInt(((Integer)i.left).intValue());
/* 4000 */       mplew.write(imp.getLevel());
/*      */     }
/* 4002 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.STATE.getValue()) != 0)) {
/* 4003 */       mplew.write(imp.getState());
/*      */     }
/* 4005 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.FULLNESS.getValue()) != 0)) {
/* 4006 */       mplew.writeInt(imp.getFullness());
/*      */     }
/* 4008 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.CLOSENESS.getValue()) != 0)) {
/* 4009 */       mplew.writeInt(imp.getCloseness());
/*      */     }
/* 4011 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.CLOSENESS_LEFT.getValue()) != 0)) {
/* 4012 */       mplew.writeInt(1);
/*      */     }
/* 4014 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MINUTES_LEFT.getValue()) != 0)) {
/* 4015 */       mplew.writeInt(0);
/*      */     }
/* 4017 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.LEVEL.getValue()) != 0)) {
/* 4018 */       mplew.write(1);
/*      */     }
/* 4020 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.FULLNESS_2.getValue()) != 0)) {
/* 4021 */       mplew.writeInt(imp.getFullness());
/*      */     }
/* 4023 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.UPDATE_TIME.getValue()) != 0)) {
/* 4024 */       mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
/*      */     }
/* 4026 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.CREATE_TIME.getValue()) != 0)) {
/* 4027 */       mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
/*      */     }
/* 4029 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.AWAKE_TIME.getValue()) != 0)) {
/* 4030 */       mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
/*      */     }
/* 4032 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.SLEEP_TIME.getValue()) != 0)) {
/* 4033 */       mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
/*      */     }
/* 4035 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_CLOSENESS.getValue()) != 0)) {
/* 4036 */       mplew.writeInt(100);
/*      */     }
/* 4038 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_DELAY.getValue()) != 0)) {
/* 4039 */       mplew.writeInt(1000);
/*      */     }
/* 4041 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_FULLNESS.getValue()) != 0)) {
/* 4042 */       mplew.writeInt(1000);
/*      */     }
/* 4044 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_ALIVE.getValue()) != 0)) {
/* 4045 */       mplew.writeInt(1);
/*      */     }
/* 4047 */     if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_MINUTES.getValue()) != 0)) {
/* 4048 */       mplew.writeInt(10);
/*      */     }
/* 4050 */     mplew.write(0);
/*      */ 
/* 4052 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getMulungRanking() {
/* 4056 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 4058 */     mplew.writeShort(SendPacketOpcode.MULUNG_DOJO_RANKING.getValue());
/* 4059 */     mplew.writeInt(1);
/*      */ 
/* 4061 */     mplew.writeShort(1);
/* 4062 */     mplew.writeMapleAsciiString("hi");
/* 4063 */     mplew.writeLong(2L);
/*      */ 
/* 4065 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */   public static byte[] getMulungMessage(boolean dc, String msg) {
/* 4069 */     MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 4071 */     mplew.writeShort(SendPacketOpcode.MULUNG_MESSAGE.getValue());
/* 4072 */     mplew.write(dc ? 1 : 0);
/* 4073 */     mplew.writeMapleAsciiString(msg);
/*      */ 
/* 4075 */     return mplew.getPacket();
/*      */   }
/*      */ 
/*      */  
/*      */   public static byte[] showCardDeck(int cardAmount)
/*      */   {
/* 4155 */     MaplePacketLittleEndianWriter writer = new MaplePacketLittleEndianWriter();
/* 4156 */     //writer.writeShort(SendPacketOpcode.UPDATE_CARD.getValue());
/* 4157 */     writer.write(cardAmount);
/* 4158 */     return writer.getPacket();
/*      */   }
/*      */ 
/*      */   public static class AlliancePacket
/*      */   {
/*      */     public static byte[] getAllianceInfo(MapleGuildAlliance alliance)
/*      */     {
/* 2406 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2408 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2409 */       mplew.write(12);
/* 2410 */       mplew.write(alliance == null ? 0 : 1);
/* 2411 */       if (alliance != null) {
/* 2412 */         addAllianceInfo(mplew, alliance);
/*      */       }
/*      */ 
/* 2415 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     private static void addAllianceInfo(MaplePacketLittleEndianWriter mplew, MapleGuildAlliance alliance) {
/* 2419 */       mplew.writeInt(alliance.getId());
/* 2420 */       mplew.writeMapleAsciiString(alliance.getName());
/* 2421 */       for (int i = 1; i <= 5; i++) {
/* 2422 */         mplew.writeMapleAsciiString(alliance.getRank(i));
/*      */       }
/* 2424 */       mplew.write(alliance.getNoGuilds());
/* 2425 */       for (int i = 0; i < alliance.getNoGuilds(); i++) {
/* 2426 */         mplew.writeInt(alliance.getGuildId(i));
/*      */       }
/* 2428 */       mplew.writeInt(alliance.getCapacity());
/* 2429 */       mplew.writeMapleAsciiString(alliance.getNotice());
/*      */     }
/*      */ 
/*      */     public static byte[] getGuildAlliance(MapleGuildAlliance alliance) {
/* 2433 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2435 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2436 */       mplew.write(13);
/* 2437 */       if (alliance == null) {
/* 2438 */         mplew.writeInt(0);
/* 2439 */         return mplew.getPacket();
/*      */       }
/* 2441 */       int noGuilds = alliance.getNoGuilds();
/* 2442 */       MapleGuild[] g = new MapleGuild[noGuilds];
/* 2443 */       for (int i = 0; i < alliance.getNoGuilds(); i++) {
/* 2444 */         g[i] = World.Guild.getGuild(alliance.getGuildId(i));
/* 2445 */         if (g[i] == null) {
/* 2446 */           return CWvsContext.enableActions();
/*      */         }
/*      */       }
/* 2449 */       mplew.writeInt(noGuilds);
/* 2450 */       for (MapleGuild gg : g) {
/* 2451 */         CWvsContext.GuildPacket.getGuildInfo(mplew, gg);
/*      */       }
/* 2453 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] allianceMemberOnline(int alliance, int gid, int id, boolean online) {
/* 2457 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2459 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2460 */       mplew.write(14);
/* 2461 */       mplew.writeInt(alliance);
/* 2462 */       mplew.writeInt(gid);
/* 2463 */       mplew.writeInt(id);
/* 2464 */       mplew.write(online ? 1 : 0);
/*      */ 
/* 2466 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] removeGuildFromAlliance(MapleGuildAlliance alliance, MapleGuild expelledGuild, boolean expelled) {
/* 2470 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2472 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2473 */       mplew.write(16);
/* 2474 */       addAllianceInfo(mplew, alliance);
/* 2475 */       CWvsContext.GuildPacket.getGuildInfo(mplew, expelledGuild);
/* 2476 */       mplew.write(expelled ? 1 : 0);
/*      */ 
/* 2478 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] addGuildToAlliance(MapleGuildAlliance alliance, MapleGuild newGuild) {
/* 2482 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2484 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2485 */       mplew.write(18);
/* 2486 */       addAllianceInfo(mplew, alliance);
/* 2487 */       mplew.writeInt(newGuild.getId());
/* 2488 */       CWvsContext.GuildPacket.getGuildInfo(mplew, newGuild);
/* 2489 */       mplew.write(0);
/*      */ 
/* 2491 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] sendAllianceInvite(String allianceName, MapleCharacter inviter) {
/* 2495 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2497 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2498 */       mplew.write(3);
/* 2499 */       mplew.writeInt(inviter.getGuildId());
/* 2500 */       mplew.writeMapleAsciiString(inviter.getName());
/* 2501 */       mplew.writeMapleAsciiString(allianceName);
/*      */ 
/* 2503 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getAllianceUpdate(MapleGuildAlliance alliance) {
/* 2507 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2509 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2510 */       mplew.write(23);
/* 2511 */       addAllianceInfo(mplew, alliance);
/*      */ 
/* 2513 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] createGuildAlliance(MapleGuildAlliance alliance) {
/* 2517 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2519 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2520 */       mplew.write(15);
/* 2521 */       addAllianceInfo(mplew, alliance);
/* 2522 */       int noGuilds = alliance.getNoGuilds();
/* 2523 */       MapleGuild[] g = new MapleGuild[noGuilds];
/* 2524 */       for (int i = 0; i < alliance.getNoGuilds(); i++) {
/* 2525 */         g[i] = World.Guild.getGuild(alliance.getGuildId(i));
/* 2526 */         if (g[i] == null) {
/* 2527 */           return CWvsContext.enableActions();
/*      */         }
/*      */       }
/* 2530 */       for (MapleGuild gg : g) {
/* 2531 */         CWvsContext.GuildPacket.getGuildInfo(mplew, gg);
/*      */       }
/* 2533 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateAlliance(MapleGuildCharacter mgc, int allianceid) {
/* 2537 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2539 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2540 */       mplew.write(24);
/* 2541 */       mplew.writeInt(allianceid);
/* 2542 */       mplew.writeInt(mgc.getGuildId());
/* 2543 */       mplew.writeInt(mgc.getId());
/* 2544 */       mplew.writeInt(mgc.getLevel());
/* 2545 */       mplew.writeInt(mgc.getJobId());
/*      */ 
/* 2547 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateAllianceLeader(int allianceid, int newLeader, int oldLeader) {
/* 2551 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2553 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2554 */       mplew.write(25);
/* 2555 */       mplew.writeInt(allianceid);
/* 2556 */       mplew.writeInt(oldLeader);
/* 2557 */       mplew.writeInt(newLeader);
/*      */ 
/* 2559 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] allianceRankChange(int aid, String[] ranks) {
/* 2563 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2565 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 2566 */       mplew.write(26);
/* 2567 */       mplew.writeInt(aid);
/* 2568 */       for (String r : ranks) {
/* 2569 */         mplew.writeMapleAsciiString(r);
/*      */       }
/*      */ 
/* 2572 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateAllianceRank(MapleGuildCharacter mgc) {
/* 2576 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2578 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2579 */       mplew.write(27);
/* 2580 */       mplew.writeInt(mgc.getId());
/* 2581 */       mplew.write(mgc.getAllianceRank());
/*      */ 
/* 2583 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] changeAllianceNotice(int allianceid, String notice) {
/* 2587 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2589 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2590 */       mplew.write(28);
/* 2591 */       mplew.writeInt(allianceid);
/* 2592 */       mplew.writeMapleAsciiString(notice);
/*      */ 
/* 2594 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] disbandAlliance(int alliance) {
/* 2598 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2600 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2601 */       mplew.write(29);
/* 2602 */       mplew.writeInt(alliance);
/*      */ 
/* 2604 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] changeAlliance(MapleGuildAlliance alliance, boolean in) {
/* 2608 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2610 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2611 */       mplew.write(1);
/* 2612 */       mplew.write(in ? 1 : 0);
/* 2613 */       mplew.writeInt(in ? alliance.getId() : 0);
/* 2614 */       int noGuilds = alliance.getNoGuilds();
/* 2615 */       MapleGuild[] g = new MapleGuild[noGuilds];
/* 2616 */       for (int i = 0; i < noGuilds; i++) {
/* 2617 */         g[i] = World.Guild.getGuild(alliance.getGuildId(i));
/* 2618 */         if (g[i] == null) {
/* 2619 */           return CWvsContext.enableActions();
/*      */         }
/*      */       }
/* 2622 */       mplew.write(noGuilds);
/* 2623 */       for (int i = 0; i < noGuilds; i++) {
/* 2624 */         mplew.writeInt(g[i].getId());
/*      */ 
/* 2626 */          Collection<MapleGuildCharacter> members = g[i].getMembers();
/* 2627 */         mplew.writeInt(members.size());
/* 2628 */         for (MapleGuildCharacter mgc : members) {
/* 2629 */           mplew.writeInt(mgc.getId());
/* 2630 */           mplew.write(in ? mgc.getAllianceRank() : 0);
/*      */         }
/*      */       }
/*      */ 
/* 2634 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] changeAllianceLeader(int allianceid, int newLeader, int oldLeader) {
/* 2638 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2640 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2641 */       mplew.write(2);
/* 2642 */       mplew.writeInt(allianceid);
/* 2643 */       mplew.writeInt(oldLeader);
/* 2644 */       mplew.writeInt(newLeader);
/*      */ 
/* 2646 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] changeGuildInAlliance(MapleGuildAlliance alliance, MapleGuild guild, boolean add) {
/* 2650 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2652 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2653 */       mplew.write(4);
/* 2654 */       mplew.writeInt(add ? alliance.getId() : 0);
/* 2655 */       mplew.writeInt(guild.getId());
/* 2656 */           Collection<MapleGuildCharacter> members = guild.getMembers();
/* 2657 */       mplew.writeInt(members.size());
/* 2658 */       for (MapleGuildCharacter mgc : members) {
/* 2659 */         mplew.writeInt(mgc.getId());
/* 2660 */         mplew.write(add ? mgc.getAllianceRank() : 0);
/*      */       }
/*      */ 
/* 2663 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] changeAllianceRank(int allianceid, MapleGuildCharacter player) {
/* 2667 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2669 */       mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
/* 2670 */       mplew.write(5);
/* 2671 */       mplew.writeInt(allianceid);
/* 2672 */       mplew.writeInt(player.getId());
/* 2673 */       mplew.writeInt(player.getAllianceRank());
/*      */ 
/* 2675 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class FamilyPacket
/*      */   {
/*      */     public static byte[] getFamilyData()
/*      */     {
/* 2071 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2073 */       mplew.writeShort(SendPacketOpcode.FAMILY.getValue());
/* 2074 */       MapleFamilyBuff[] entries = MapleFamilyBuff.values();
/* 2075 */       mplew.writeInt(entries.length);
/*      */ 
/* 2077 */       for (MapleFamilyBuff entry : entries) {
/* 2078 */         mplew.write(entry.type);
/* 2079 */         mplew.writeInt(entry.rep);
/* 2080 */         mplew.writeInt(1);
/* 2081 */         mplew.writeMapleAsciiString(entry.name);
/* 2082 */         mplew.writeMapleAsciiString(entry.desc);
/*      */       }
/* 2084 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getFamilyInfo(MapleCharacter chr) {
/* 2088 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2090 */       mplew.writeShort(SendPacketOpcode.OPEN_FAMILY.getValue());
/* 2091 */       mplew.writeInt(chr.getCurrentRep());
/* 2092 */       mplew.writeInt(chr.getTotalRep());
/* 2093 */       mplew.writeInt(chr.getTotalRep());
/* 2094 */       mplew.writeShort(chr.getNoJuniors());
/* 2095 */       mplew.writeShort(2);
/* 2096 */       mplew.writeShort(chr.getNoJuniors());
/* 2097 */       MapleFamily family = World.Family.getFamily(chr.getFamilyId());
/* 2098 */       if (family != null) {
/* 2099 */         mplew.writeInt(family.getLeaderId());
/* 2100 */         mplew.writeMapleAsciiString(family.getLeaderName());
/* 2101 */         mplew.writeMapleAsciiString(family.getNotice());
/*      */       } else {
/* 2103 */         mplew.writeLong(0L);
/*      */       }
/* 2105 */       List b = chr.usedBuffs();
/* 2106 */       mplew.writeInt(b.size());
/* 2107 */       for (Iterator i$ = b.iterator(); i$.hasNext(); ) { int ii = ((Integer)i$.next()).intValue();
/* 2108 */         mplew.writeInt(ii);
/* 2109 */         mplew.writeInt(1);
/*      */       }
/*      */ 
/* 2112 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static void addFamilyCharInfo(MapleFamilyCharacter ldr, MaplePacketLittleEndianWriter mplew)
/*      */     {
/* 2129 */       mplew.writeInt(ldr.getId());
/* 2130 */       mplew.writeInt(ldr.getSeniorId());
/* 2131 */       mplew.writeShort(ldr.getJobId());
/* 2132 */       mplew.writeShort(0);
/* 2133 */       mplew.write(ldr.getLevel());
/* 2134 */       mplew.write(ldr.isOnline() ? 1 : 0);
/* 2135 */       mplew.writeInt(ldr.getCurrentRep());
/* 2136 */       mplew.writeInt(ldr.getTotalRep());
/* 2137 */       mplew.writeInt(ldr.getTotalRep());
/* 2138 */       mplew.writeInt(ldr.getTotalRep());
/* 2139 */       mplew.writeInt(Math.max(ldr.getChannel(), 0));
/* 2140 */       mplew.writeInt(0);
/* 2141 */       mplew.writeMapleAsciiString(ldr.getName());
/*      */     }
/*      */ 
/*      */     public static byte[] getFamilyPedigree(MapleCharacter chr) {
/* 2145 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 2146 */       mplew.writeShort(SendPacketOpcode.SEND_PEDIGREE.getValue());
/* 2147 */       mplew.writeInt(chr.getId());
/* 2148 */       MapleFamily family = World.Family.getFamily(chr.getFamilyId());
/*      */ 
/* 2154 */       int descendants = 2; int gens = 0; int generations = 0;
/* 2155 */       if (family == null) {
/* 2156 */         mplew.writeInt(2);
/* 2157 */         addFamilyCharInfo(new MapleFamilyCharacter(chr, 0, 0, 0, 0), mplew);
/*      */       } else {
/* 2159 */         mplew.writeInt(family.getMFC(chr.getId()).getPedigree().size() + 1);
/* 2160 */         addFamilyCharInfo(family.getMFC(family.getLeaderId()), mplew);
/*      */ 
/* 2162 */         if (chr.getSeniorId() > 0) {
/* 2163 */           MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
/* 2164 */           if (senior != null) {
/* 2165 */             if (senior.getSeniorId() > 0) {
/* 2166 */               addFamilyCharInfo(family.getMFC(senior.getSeniorId()), mplew);
/*      */             }
/* 2168 */             addFamilyCharInfo(senior, mplew);
/*      */           }
/*      */         }
/*      */       }
/* 2172 */       addFamilyCharInfo(chr.getMFC() == null ? new MapleFamilyCharacter(chr, 0, 0, 0, 0) : chr.getMFC(), mplew);
/* 2173 */       if (family != null) {
/* 2174 */         if (chr.getSeniorId() > 0) {
/* 2175 */           MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
/* 2176 */           if (senior != null) {
/* 2177 */             if ((senior.getJunior1() > 0) && (senior.getJunior1() != chr.getId()))
/* 2178 */               addFamilyCharInfo(family.getMFC(senior.getJunior1()), mplew);
/* 2179 */             else if ((senior.getJunior2() > 0) && (senior.getJunior2() != chr.getId())) {
/* 2180 */               addFamilyCharInfo(family.getMFC(senior.getJunior2()), mplew);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2191 */         if (chr.getJunior1() > 0) {
/* 2192 */           MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
/* 2193 */           if (junior != null) {
/* 2194 */             addFamilyCharInfo(junior, mplew);
/*      */           }
/*      */         }
/* 2197 */         if (chr.getJunior2() > 0) {
/* 2198 */           MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
/* 2199 */           if (junior != null) {
/* 2200 */             addFamilyCharInfo(junior, mplew);
/*      */           }
/*      */         }
/* 2203 */         if (chr.getJunior1() > 0) {
/* 2204 */           MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
/* 2205 */           if (junior != null) {
/* 2206 */             if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
/* 2207 */               gens++;
/* 2208 */               addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
/*      */             }
/* 2210 */             if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
/* 2211 */               gens++;
/* 2212 */               addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
/*      */             }
/*      */           }
/*      */         }
/* 2216 */         if (chr.getJunior2() > 0) {
/* 2217 */           MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
/* 2218 */           if (junior != null) {
/* 2219 */             if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
/* 2220 */               gens++;
/* 2221 */               addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
/*      */             }
/* 2223 */             if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
/* 2224 */               gens++;
/* 2225 */               addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
/*      */             }
/*      */           }
/*      */         }
/* 2229 */         generations = family.getMemberSize();
/*      */       }
/* 2231 */       mplew.writeLong(gens);
/* 2232 */       mplew.writeInt(0);
/* 2233 */       mplew.writeInt(-1);
/* 2234 */       mplew.writeInt(generations);
/*      */ 
/* 2240 */       if (family != null) {
/* 2241 */         if (chr.getJunior1() > 0) {
/* 2242 */           MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
/* 2243 */           if (junior != null) {
/* 2244 */             if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
/* 2245 */               mplew.writeInt(junior.getJunior1());
/* 2246 */               mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
/*      */             } else {
/* 2248 */               mplew.writeInt(0);
/*      */             }
/* 2250 */             if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
/* 2251 */               mplew.writeInt(junior.getJunior2());
/* 2252 */               mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
/*      */             } else {
/* 2254 */               mplew.writeInt(0);
/*      */             }
/*      */           }
/*      */         }
/* 2258 */         if (chr.getJunior2() > 0) {
/* 2259 */           MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
/* 2260 */           if (junior != null) {
/* 2261 */             if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
/* 2262 */               mplew.writeInt(junior.getJunior1());
/* 2263 */               mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
/*      */             } else {
/* 2265 */               mplew.writeInt(0);
/*      */             }
/* 2267 */             if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
/* 2268 */               mplew.writeInt(junior.getJunior2());
/* 2269 */               mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
/*      */             } else {
/* 2271 */               mplew.writeInt(0);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2277 */       List b = chr.usedBuffs();
/* 2278 */       mplew.writeInt(b.size());
/* 2279 */       for (Iterator i$ = b.iterator(); i$.hasNext(); ) { int ii = ((Integer)i$.next()).intValue();
/* 2280 */         mplew.writeInt(ii);
/* 2281 */         mplew.writeInt(1);
/*      */       }
/* 2283 */       mplew.writeShort(2);
/*      */ 
/* 2285 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getFamilyMsg(byte type, int meso) {
/* 2289 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2309 */       mplew.writeShort(SendPacketOpcode.FAMILY_MESSAGE.getValue());
/* 2310 */       mplew.writeInt(type);
/* 2311 */       mplew.writeInt(meso);
/*      */ 
/* 2313 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] sendFamilyInvite(int cid, int otherLevel, int otherJob, String inviter) {
/* 2317 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2323 */       mplew.writeShort(SendPacketOpcode.FAMILY_INVITE.getValue());
/* 2324 */       mplew.writeInt(cid);
/* 2325 */       mplew.writeInt(otherLevel);
/* 2326 */       mplew.writeInt(otherJob);
/* 2327 */       mplew.writeInt(0);
/* 2328 */       mplew.writeMapleAsciiString(inviter);
/* 2329 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] sendFamilyJoinResponse(boolean accepted, String added) {
/* 2333 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2335 */       mplew.writeShort(SendPacketOpcode.FAMILY_JUNIOR.getValue());
/* 2336 */       mplew.write(accepted ? 1 : 0);
/* 2337 */       mplew.writeMapleAsciiString(added);
/*      */ 
/* 2339 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getSeniorMessage(String name) {
/* 2343 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2345 */       mplew.writeShort(SendPacketOpcode.SENIOR_MESSAGE.getValue());
/* 2346 */       mplew.writeMapleAsciiString(name);
/*      */ 
/* 2348 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] changeRep(int r, String name) {
/* 2352 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2354 */       mplew.writeShort(SendPacketOpcode.REP_INCREASE.getValue());
/* 2355 */       mplew.writeInt(r);
/* 2356 */       mplew.writeMapleAsciiString(name);
/*      */ 
/* 2358 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] familyLoggedIn(boolean online, String name) {
/* 2362 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2364 */       mplew.writeShort(SendPacketOpcode.FAMILY_LOGGEDIN.getValue());
/* 2365 */       mplew.write(online ? 1 : 0);
/* 2366 */       mplew.writeMapleAsciiString(name);
/*      */ 
/* 2368 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] familyBuff(int type, int buffnr, int amount, int time) {
/* 2372 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 2373 */       mplew.writeShort(SendPacketOpcode.FAMILY_BUFF.getValue());
/* 2374 */       mplew.write(type);
/* 2375 */       if ((type >= 2) && (type <= 4)) {
/* 2376 */         mplew.writeInt(buffnr);
/*      */ 
/* 2378 */         mplew.writeInt(type == 3 ? 0 : amount);
/* 2379 */         mplew.writeInt(type == 2 ? 0 : amount);
/* 2380 */         mplew.write(0);
/* 2381 */         mplew.writeInt(time);
/*      */       }
/* 2383 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] cancelFamilyBuff() {
/* 2387 */       return familyBuff(0, 0, 0, 0);
/*      */     }
/*      */ 
/*      */     public static byte[] familySummonRequest(String name, String mapname) {
/* 2391 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2393 */       mplew.writeShort(SendPacketOpcode.FAMILY_USE_REQUEST.getValue());
/* 2394 */       mplew.writeMapleAsciiString(name);
/* 2395 */       mplew.writeMapleAsciiString(mapname);
/*      */ 
/* 2397 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class BuddylistPacket
/*      */   {
/*      */     public static byte[] updateBuddylist(Collection<BuddylistEntry> buddylist)
/*      */     {
/* 1986 */       return updateBuddylist(buddylist, 7);
/*      */     }
/*      */ 
/*      */     public static byte[] updateBuddylist(Collection<BuddylistEntry> buddylist, int deleted) {
/* 1990 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1992 */       mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
/* 1993 */       mplew.write(deleted);
/* 1994 */       mplew.write(buddylist.size());
/* 1995 */       for (BuddylistEntry buddy : buddylist) {
/* 1996 */         mplew.writeInt(buddy.getCharacterId());
/* 1997 */         mplew.writeAsciiString(buddy.getName(), 13);
/* 1998 */         mplew.write(buddy.isVisible() ? 0 : 1);
/* 1999 */         mplew.writeInt(buddy.getChannel() == -1 ? -1 : buddy.getChannel() - 1);
/* 2000 */         mplew.writeAsciiString(buddy.getGroup(), 17);
/*      */       }
/* 2002 */       for (int x = 0; x < buddylist.size(); x++) {
/* 2003 */         mplew.writeInt(0);
/*      */       }
/*      */ 
/* 2006 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateBuddyChannel(int characterid, int channel) {
/* 2010 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2012 */       mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
/* 2013 */       mplew.write(20);
/* 2014 */       mplew.writeInt(characterid);
/* 2015 */       mplew.write(0);
/* 2016 */       mplew.writeInt(channel);
/*      */ 
/* 2018 */       return mplew.getPacket();
/*      */     }
/*      */ 
 public static byte[] requestBuddylistAdd(int cidFrom, String nameFrom, int levelFrom, int jobFrom) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
        mplew.write(9);
        mplew.writeInt(cidFrom);
        mplew.writeMapleAsciiString(nameFrom);
        mplew.writeInt(levelFrom);
        mplew.writeInt(jobFrom);
        mplew.writeInt(0);//v115
        mplew.writeInt(cidFrom);
        mplew.writeAsciiString(nameFrom, 13);
        mplew.write(1);
        mplew.writeInt(0);
        mplew.writeAsciiString("ETC", 16);
        mplew.writeShort(1);

        return mplew.getPacket();
    }
/*      */     public static byte[] updateBuddyCapacity(int capacity) {
/* 2041 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2043 */       mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
/* 2044 */       mplew.write(21);
/* 2045 */       mplew.write(capacity);
/*      */ 
/* 2047 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] buddylistMessage(byte message) {
/* 2051 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 2059 */       mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
/* 2060 */       mplew.write(message);
/*      */ 
/* 2062 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
  public static byte[] giveKilling(int x) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
        PacketHelper.writeSingleMask(mplew, MapleBuffStat.KILL_COUNT);
        mplew.writeShort(0);
        mplew.write(0);
        mplew.writeInt(x);
        return mplew.getPacket();
    } 

/*      */   public static class ExpeditionPacket
/*      */   {
/*      */     public static byte[] expeditionStatus(MapleExpedition me, boolean created, boolean silent)
/*      */     {
/* 1861 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1863 */       mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
/* 1864 */       mplew.write(created ? 74 : silent ? 72 : 76);
/* 1865 */       mplew.writeInt(me.getType().exped);
/* 1866 */       mplew.writeInt(0);
/* 1867 */       for (int i = 0; i < 6; i++) {
/* 1868 */         if (i < me.getParties().size()) {
/* 1869 */           MapleParty party = World.Party.getParty(((Integer)me.getParties().get(i)).intValue());
/*      */ 
/* 1871 */           CWvsContext.PartyPacket.addPartyStatus(-1, party, mplew, false, true);
/*      */         }
/*      */         else
/*      */         {
/* 1878 */           CWvsContext.PartyPacket.addPartyStatus(-1, null, mplew, false, true);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1883 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] expeditionError(int errcode, String name) {
/* 1887 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1897 */       mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
/* 1898 */       mplew.write(88);
/* 1899 */       mplew.writeInt(errcode);
/* 1900 */       mplew.writeMapleAsciiString(name);
/*      */ 
/* 1902 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] expeditionMessage(int code) {
/* 1906 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1916 */       mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
/* 1917 */       mplew.write(code);
/*      */ 
/* 1919 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] expeditionJoined(String name) {
/* 1923 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1925 */       mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
/* 1926 */       mplew.write(75);
/* 1927 */       mplew.writeMapleAsciiString(name);
/*      */ 
/* 1929 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] expeditionLeft(String name) {
/* 1933 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1935 */       mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
/* 1936 */       mplew.write(79);
/* 1937 */       mplew.writeMapleAsciiString(name);
/*      */ 
/* 1940 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] expeditionLeaderChanged(int newLeader) {
/* 1944 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1946 */       mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
/* 1947 */       mplew.write(84);
/* 1948 */       mplew.writeInt(newLeader);
/*      */ 
/* 1950 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] expeditionUpdate(int partyIndex, MapleParty party) {
/* 1954 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 1955 */       mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
/* 1956 */       mplew.write(85);
/* 1957 */       mplew.writeInt(0);
/* 1958 */       mplew.writeInt(partyIndex);
/*      */ 
/* 1962 */       CWvsContext.PartyPacket.addPartyStatus(-1, party, mplew, false, true);
/*      */ 
/* 1964 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] expeditionInvite(MapleCharacter from, int exped) {
/* 1968 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1970 */       mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
/* 1971 */       mplew.write(87);
/* 1972 */       mplew.writeInt(from.getLevel());
/* 1973 */       mplew.writeInt(from.getJob());
/* 1974 */       mplew.writeMapleAsciiString(from.getName());
/* 1975 */       mplew.writeInt(exped);
/* 1976 */       mplew.writeInt(0);
/* 1977 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class PartyPacket
/*      */   {
/*      */     public static byte[] partyCreated(int partyid)
/*      */     {
/* 1525 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1527 */       mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
/* 1528 */       mplew.write(12);
/* 1529 */       mplew.writeInt(partyid);
/* 1530 */       mplew.writeInt(999999999);
/* 1531 */       mplew.writeInt(999999999);
/* 1532 */       mplew.writeInt(0);
/* 1533 */       mplew.writeShort(0);
/* 1534 */       mplew.writeShort(0);
/* 1535 */       mplew.write(0);
/* 1536 */       mplew.write(1);
/*      */ 
/* 1538 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] partyInvite(MapleCharacter from) {
/* 1542 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1551 */       mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
/* 1552 */       mplew.write(4);
/* 1553 */       mplew.writeInt(from.getParty() == null ? 0 : from.getParty().getId());
/* 1554 */       mplew.writeMapleAsciiString(from.getName());
/* 1555 */       mplew.writeInt(from.getLevel());
/* 1556 */       mplew.writeInt(from.getJob());
/* 1557 */       mplew.write(0);
/* 1558 */       mplew.writeInt(0);
/* 1559 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] partyRequestInvite(MapleCharacter from) {
/* 1563 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1565 */       mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
/* 1566 */       mplew.write(7);
/* 1567 */       mplew.writeInt(from.getId());
/* 1568 */       mplew.writeMapleAsciiString(from.getName());
/* 1569 */       mplew.writeInt(from.getLevel());
/* 1570 */       mplew.writeInt(from.getJob());
/*      */ 
/* 1572 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] partyStatusMessage(int message, String charname) {
/* 1576 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1595 */       mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
/* 1596 */       mplew.write(message);
/* 1597 */       if ((message == 26) || (message == 52))
/* 1598 */         mplew.writeMapleAsciiString(charname);
/* 1599 */       else if (message == 45) {
/* 1600 */         mplew.write(0);
/*      */       }
/*      */ 
/* 1603 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static void addPartyStatus(int forchannel, MapleParty party, MaplePacketLittleEndianWriter lew, boolean leaving) {
/* 1607 */       addPartyStatus(forchannel, party, lew, leaving, false);
/*      */     }
/*      */ 
/*      */     public static void addPartyStatus(int forchannel, MapleParty party, MaplePacketLittleEndianWriter lew, boolean leaving, boolean exped)
/*      */     {
/*      */       List<MaplePartyCharacter> partymembers;
/* 1613 */       if (party == null)
/* 1614 */         partymembers = new ArrayList();
/*      */       else {
/* 1616 */         partymembers = new ArrayList(party.getMembers());
/*      */       }
/* 1618 */       while (partymembers.size() < 6) {
/* 1619 */         partymembers.add(new MaplePartyCharacter());
/*      */       }
/* 1621 */       for (MaplePartyCharacter partychar : partymembers) {
/* 1622 */         lew.writeInt(partychar.getId());
/*      */       }
/* 1624 */       for (MaplePartyCharacter partychar : partymembers) {
/* 1625 */         lew.writeAsciiString(partychar.getName(), 13);
/*      */       }
/* 1627 */       for (MaplePartyCharacter partychar : partymembers) {
/* 1628 */         lew.writeInt(partychar.getJobId());
/*      */       }
/* 1630 */       for (MaplePartyCharacter partychar : partymembers) {
/* 1631 */         lew.writeInt(0);
/*      */       }
/* 1633 */       for (MaplePartyCharacter partychar : partymembers) {
/* 1634 */         lew.writeInt(partychar.getLevel());
/*      */       }
/* 1636 */       for (MaplePartyCharacter partychar : partymembers) {
/* 1637 */         lew.writeInt(partychar.isOnline() ? partychar.getChannel() - 1 : -2);
/*      */       }
/* 1639 */       for (MaplePartyCharacter partychar : partymembers) {
/* 1640 */         lew.writeInt(0);
/*      */       }
/*      */ 
/* 1667 */       lew.writeInt(party == null ? 0 : party.getLeader().getId());
/* 1668 */       if (exped) {
/* 1669 */         return;
/*      */       }
/* 1671 */       for (MaplePartyCharacter partychar : partymembers) {
/* 1672 */         lew.writeInt(partychar.getChannel() == forchannel ? partychar.getMapid() : 999999999);
/*      */       }
/* 1674 */       for (MaplePartyCharacter partychar : partymembers) {
/* 1675 */         if ((partychar.getChannel() == forchannel) && (!leaving)) {
/* 1676 */           lew.writeInt(partychar.getDoorTown());
/* 1677 */           lew.writeInt(partychar.getDoorTarget());
/* 1678 */           lew.writeInt(partychar.getDoorSkill());
/* 1679 */           lew.writeInt(partychar.getDoorPosition().x);
/* 1680 */           lew.writeInt(partychar.getDoorPosition().y);
/*      */         } else {
/* 1682 */           lew.writeInt(leaving ? 999999999 : 0);
/* 1683 */           lew.writeLong(leaving ? 999999999L : 0L);
/* 1684 */           lew.writeLong(leaving ? -1L : 0L);
/*      */         }
/*      */       }
/* 1687 */       lew.write(1);
/*      */     }
/*      */ 
/*      */     public static byte[] updateParty(int forChannel, MapleParty party, PartyOperation op, MaplePartyCharacter target) {
/* 1691 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1693 */       mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
/* 1694 */      switch (op) {
            case DISBAND:
                case EXPEL:
                case LEAVE:
/* 1698 */         mplew.write(16);
/* 1699 */         mplew.writeInt(party.getId());
/* 1700 */         mplew.writeInt(target.getId());
/* 1701 */         mplew.write(op == PartyOperation.DISBAND ? 0 : 1);
/* 1702 */         if (op == PartyOperation.DISBAND) break;
/* 1703 */         mplew.write(op == PartyOperation.EXPEL ? 1 : 0);
/* 1704 */         mplew.writeMapleAsciiString(target.getName());
/* 1705 */         addPartyStatus(forChannel, party, mplew, op == PartyOperation.LEAVE); break;
/*      */         case JOIN:
/* 1709 */         mplew.write(19);
/* 1710 */         mplew.writeInt(party.getId());
/* 1711 */         mplew.writeMapleAsciiString(target.getName());
/* 1712 */         addPartyStatus(forChannel, party, mplew, false);
/* 1713 */         break;
/*      */        case SILENT_UPDATE:
                case LOG_ONOFF:
/* 1716 */         mplew.write(11);
/* 1717 */         mplew.writeInt(party.getId());
/* 1718 */         addPartyStatus(forChannel, party, mplew, op == PartyOperation.LOG_ONOFF);
/* 1719 */         break;
/*      */         case CHANGE_LEADER:
                case CHANGE_LEADER_DC:
/* 1722 */         mplew.write(35);
/* 1723 */         mplew.writeInt(target.getId());
/* 1724 */         mplew.write(op == PartyOperation.CHANGE_LEADER_DC ? 1 : 0);
/*      */       }
/*      */ 
/* 1728 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] partyPortal(int townId, int targetId, int skillId, Point position, boolean animation) {
/* 1732 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1734 */       mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
/* 1735 */       mplew.write(61);
/* 1736 */       mplew.write(animation ? 0 : 1);
/* 1737 */       mplew.writeInt(townId);
/* 1738 */       mplew.writeInt(targetId);
/* 1739 */       mplew.writeInt(skillId);
/* 1740 */       mplew.writePos(position);
/*      */ 
/* 1742 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getPartyListing(PartySearchType pst)
/*      */     {
/* 1747 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 1748 */       mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
/* 1749 */       mplew.write(GameConstants.GMS ? 147 : 77);
/* 1750 */       mplew.writeInt(pst.id);
/* 1751 */       final List<PartySearch> parties = World.Party.searchParty(pst);
/* 1752 */       mplew.writeInt(parties.size());
/* 1753 */       for (PartySearch party : parties) {
/* 1754 */         mplew.writeInt(0);
/* 1755 */         mplew.writeInt(2);
/* 1756 */         if (pst.exped) {
/* 1757 */           MapleExpedition me = World.Party.getExped(party.getId());
/* 1758 */           mplew.writeInt(me.getType().maxMembers);
/* 1759 */           mplew.writeInt(party.getId());
/* 1760 */           mplew.writeAsciiString(party.getName(), 48);
/* 1761 */           for (int i = 0; i < 5; i++)
/* 1762 */             if (i < me.getParties().size()) {
/* 1763 */               MapleParty part = World.Party.getParty(((Integer)me.getParties().get(i)).intValue());
/* 1764 */               if (part != null)
/* 1765 */                 addPartyStatus(-1, part, mplew, false, true);
/*      */               else
/* 1767 */                 mplew.writeZeroBytes(202);
/*      */             }
/*      */             else {
/* 1770 */               mplew.writeZeroBytes(202);
/*      */             }
/*      */         }
/*      */         else {
/* 1774 */           mplew.writeInt(0);
/* 1775 */           mplew.writeInt(party.getId());
/* 1776 */           mplew.writeAsciiString(party.getName(), 48);
/* 1777 */           addPartyStatus(-1, World.Party.getParty(party.getId()), mplew, false, true);
/*      */         }
/*      */ 
/* 1780 */         mplew.writeShort(0);
/*      */       }
/*      */ 
/* 1783 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] partyListingAdded(PartySearch ps) {
/* 1787 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 1788 */       mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
/* 1789 */       mplew.write(93);
/* 1790 */       mplew.writeInt(ps.getType().id);
/* 1791 */       mplew.writeInt(0);
/* 1792 */       mplew.writeInt(1);
/* 1793 */       if (ps.getType().exped) {
/* 1794 */         MapleExpedition me = World.Party.getExped(ps.getId());
/* 1795 */         mplew.writeInt(me.getType().maxMembers);
/* 1796 */         mplew.writeInt(ps.getId());
/* 1797 */         mplew.writeAsciiString(ps.getName(), 48);
/* 1798 */         for (int i = 0; i < 5; i++)
/* 1799 */           if (i < me.getParties().size()) {
/* 1800 */             MapleParty party = World.Party.getParty(((Integer)me.getParties().get(i)).intValue());
/* 1801 */             if (party != null)
/* 1802 */               addPartyStatus(-1, party, mplew, false, true);
/*      */             else
/* 1804 */               mplew.writeZeroBytes(202);
/*      */           }
/*      */           else {
/* 1807 */             mplew.writeZeroBytes(202);
/*      */           }
/*      */       }
/*      */       else {
/* 1811 */         mplew.writeInt(0);
/* 1812 */         mplew.writeInt(ps.getId());
/* 1813 */         mplew.writeAsciiString(ps.getName(), 48);
/* 1814 */         addPartyStatus(-1, World.Party.getParty(ps.getId()), mplew, false, true);
/*      */       }
/* 1816 */       mplew.writeShort(0);
/*      */ 
/* 1818 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showMemberSearch(List<MapleCharacter> chr) {
/* 1822 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 1823 */       mplew.writeShort(SendPacketOpcode.MEMBER_SEARCH.getValue());
/* 1824 */       mplew.write(chr.size());
/* 1825 */       for (MapleCharacter c : chr) {
/* 1826 */         mplew.writeInt(c.getId());
/* 1827 */         mplew.writeMapleAsciiString(c.getName());
/* 1828 */         mplew.writeShort(c.getJob());
/* 1829 */         mplew.write(c.getLevel());
/*      */       }
/* 1831 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showPartySearch(List<MapleParty> chr) {
/* 1835 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/* 1836 */       mplew.writeShort(SendPacketOpcode.PARTY_SEARCH.getValue());
/* 1837 */       mplew.write(chr.size());
/* 1838 */       for (MapleParty c : chr) {
/* 1839 */         mplew.writeInt(c.getId());
/* 1840 */         mplew.writeMapleAsciiString(c.getLeader().getName());
/* 1841 */         mplew.write(c.getLeader().getLevel());
/* 1842 */         mplew.write(c.getLeader().isOnline() ? 1 : 0);
/* 1843 */         mplew.write(c.getMembers().size());
/* 1844 */         for (MaplePartyCharacter ch : c.getMembers()) {
/* 1845 */           mplew.writeInt(ch.getId());
/* 1846 */           mplew.writeMapleAsciiString(ch.getName());
/* 1847 */           mplew.writeShort(ch.getJobId());
/* 1848 */           mplew.write(ch.getLevel());
/* 1849 */           mplew.write(ch.isOnline() ? 1 : 0);
/*      */         }
/*      */       }
/* 1852 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class GuildPacket
/*      */   {
/*      */     public static byte[] guildInvite(int gid, String charName, int levelFrom, int jobFrom)
/*      */     {
/* 1126 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1128 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1129 */       mplew.write(5);
/* 1130 */       mplew.writeInt(gid);
/* 1131 */       mplew.writeMapleAsciiString(charName);
/* 1132 */       mplew.writeInt(levelFrom);
/* 1133 */       mplew.writeInt(jobFrom);
/* 1134 */       mplew.writeInt(0);
/* 1135 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showGuildInfo(MapleCharacter c) {
/* 1139 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1141 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1142 */       mplew.write(32);
/* 1143 */       if ((c == null) || (c.getMGC() == null)) {
/* 1144 */         mplew.write(0);
/* 1145 */         return mplew.getPacket();
/*      */       }
/* 1147 */       MapleGuild g = World.Guild.getGuild(c.getGuildId());
/* 1148 */       if (g == null) {
/* 1149 */         mplew.write(0);
/* 1150 */         return mplew.getPacket();
/*      */       }
/* 1152 */       mplew.write(1);
/* 1153 */       getGuildInfo(mplew, g);
/*      */ 
/* 1155 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static void getGuildInfo(MaplePacketLittleEndianWriter mplew, MapleGuild guild) {
/* 1159 */       mplew.writeInt(guild.getId());
/* 1160 */       mplew.writeMapleAsciiString(guild.getName());
/* 1161 */       for (int i = 1; i <= 5; i++) {
/* 1162 */         mplew.writeMapleAsciiString(guild.getRankTitle(i));
/*      */       }
/* 1164 */       guild.addMemberData(mplew);
/* 1165 */       mplew.writeInt(guild.getCapacity());
/* 1166 */       mplew.writeShort(guild.getLogoBG());
/* 1167 */       mplew.write(guild.getLogoBGColor());
/* 1168 */       mplew.writeShort(guild.getLogo());
/* 1169 */       mplew.write(guild.getLogoColor());
/* 1170 */       mplew.writeMapleAsciiString(guild.getNotice());
/* 1171 */       mplew.writeInt(guild.getGP());
/* 1172 */       mplew.writeInt(guild.getGP());
/* 1173 */       mplew.writeInt(guild.getAllianceId() > 0 ? guild.getAllianceId() : 0);
/* 1174 */       mplew.write(guild.getLevel());
/* 1175 */       mplew.writeShort(0);
/* 1176 */       mplew.writeShort(guild.getSkills().size());
/* 1177 */       for (MapleGuildSkill i : guild.getSkills()) {
/* 1178 */         mplew.writeInt(i.skillID);
/* 1179 */         mplew.writeShort(i.level);
/* 1180 */         mplew.writeLong(PacketHelper.getTime(i.timestamp));
/* 1181 */         mplew.writeMapleAsciiString(i.purchaser);
/* 1182 */         mplew.writeMapleAsciiString(i.activator);
/*      */       }
/*      */     }
/*      */ 
/*      */     public static byte[] newGuildInfo(MapleCharacter c)
/*      */     {
/* 1188 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1190 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1191 */       mplew.write(38);
/* 1192 */       if ((c == null) || (c.getMGC() == null)) {
/* 1193 */         return genericGuildMessage((byte)37);
/*      */       }
/* 1195 */       MapleGuild g = World.Guild.getGuild(c.getGuildId());
/* 1196 */       if (g == null) {
/* 1197 */         return genericGuildMessage((byte)37);
/*      */       }
/* 1199 */       getGuildInfo(mplew, g);
/*      */ 
/* 1201 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] newGuildMember(MapleGuildCharacter mgc) {
/* 1205 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1207 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1208 */       mplew.write(45);
/* 1209 */       mplew.writeInt(mgc.getGuildId());
/* 1210 */       mplew.writeInt(mgc.getId());
/* 1211 */       mplew.writeAsciiString(mgc.getName(), 13);
/* 1212 */       mplew.writeInt(mgc.getJobId());
/* 1213 */       mplew.writeInt(mgc.getLevel());
/* 1214 */       mplew.writeInt(mgc.getGuildRank());
/* 1215 */       mplew.writeInt(mgc.isOnline() ? 1 : 0);
/* 1216 */       mplew.writeInt(mgc.getAllianceRank());
/* 1217 */       mplew.writeInt(mgc.getGuildContribution());
/*      */ 
/* 1219 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] memberLeft(MapleGuildCharacter mgc, boolean bExpelled) {
/* 1223 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1225 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1226 */       mplew.write(bExpelled ? 53 : 50);
/* 1227 */       mplew.writeInt(mgc.getGuildId());
/* 1228 */       mplew.writeInt(mgc.getId());
/* 1229 */       mplew.writeMapleAsciiString(mgc.getName());
/*      */ 
/* 1231 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] guildDisband(int gid)
/*      */     {
/* 1236 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1238 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1239 */       mplew.write(56);
/* 1240 */       mplew.writeInt(gid);
/* 1241 */       mplew.write(1);
/*      */ 
/* 1243 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] guildCapacityChange(int gid, int capacity)
/*      */     {
/* 1248 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1250 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1251 */       mplew.write(64);
/* 1252 */       mplew.writeInt(gid);
/* 1253 */       mplew.write(capacity);
/*      */ 
/* 1255 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] guildContribution(int gid, int cid, int c) {
/* 1259 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1261 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1262 */       mplew.write(72);
/* 1263 */       mplew.writeInt(gid);
/* 1264 */       mplew.writeInt(cid);
/* 1265 */       mplew.writeInt(c);
/*      */ 
/* 1267 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] changeRank(MapleGuildCharacter mgc) {
/* 1271 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1273 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1274 */       mplew.write(70);
/* 1275 */       mplew.writeInt(mgc.getGuildId());
/* 1276 */       mplew.writeInt(mgc.getId());
/* 1277 */       mplew.write(mgc.getGuildRank());
/*      */ 
/* 1279 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] rankTitleChange(int gid, String[] ranks) {
/* 1283 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1285 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1286 */       mplew.write(68);
/* 1287 */       mplew.writeInt(gid);
/* 1288 */       for (String r : ranks) {
/* 1289 */         mplew.writeMapleAsciiString(r);
/*      */       }
/*      */ 
/* 1292 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] guildEmblemChange(int gid, short bg, byte bgcolor, short logo, byte logocolor) {
/* 1296 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1298 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1299 */       mplew.write(73);
/* 1300 */       mplew.writeInt(gid);
/* 1301 */       mplew.writeShort(bg);
/* 1302 */       mplew.write(bgcolor);
/* 1303 */       mplew.writeShort(logo);
/* 1304 */       mplew.write(logocolor);
/*      */ 
/* 1306 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateGP(int gid, int GP, int glevel) {
/* 1310 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1312 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1313 */       mplew.write(79);
/* 1314 */       mplew.writeInt(gid);
/* 1315 */       mplew.writeInt(GP);
/* 1316 */       mplew.writeInt(glevel);
/*      */ 
/* 1318 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] guildNotice(int gid, String notice) {
/* 1322 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1324 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1325 */       mplew.write(75);
/* 1326 */       mplew.writeInt(gid);
/* 1327 */       mplew.writeMapleAsciiString(notice);
/*      */ 
/* 1329 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] guildMemberLevelJobUpdate(MapleGuildCharacter mgc) {
/* 1333 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1335 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1336 */       mplew.write(66);
/* 1337 */       mplew.writeInt(mgc.getGuildId());
/* 1338 */       mplew.writeInt(mgc.getId());
/* 1339 */       mplew.writeInt(mgc.getLevel());
/* 1340 */       mplew.writeInt(mgc.getJobId());
/*      */ 
/* 1342 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] guildMemberOnline(int gid, int cid, boolean bOnline) {
/* 1346 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1348 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1349 */       mplew.write(67);
/* 1350 */       mplew.writeInt(gid);
/* 1351 */       mplew.writeInt(cid);
/* 1352 */       mplew.write(bOnline ? 1 : 0);
/*      */ 
/* 1354 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showGuildRanks(int npcid, List<MapleGuildRanking.GuildRankingInfo> all) {
/* 1358 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1360 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1361 */       mplew.write(80);
/* 1362 */       mplew.writeInt(npcid);
/* 1363 */       mplew.writeInt(all.size());
/* 1364 */       for (MapleGuildRanking.GuildRankingInfo info : all) {
/* 1365 */         mplew.writeShort(0);
/* 1366 */         mplew.writeMapleAsciiString(info.getName());
/* 1367 */         mplew.writeInt(info.getGP());
/* 1368 */         mplew.writeInt(info.getLogo());
/* 1369 */         mplew.writeInt(info.getLogoColor());
/* 1370 */         mplew.writeInt(info.getLogoBg());
/* 1371 */         mplew.writeInt(info.getLogoBgColor());
/*      */       }
/*      */ 
/* 1374 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] guildSkillPurchased(int gid, int sid, int level, long expiration, String purchase, String activate) {
/* 1378 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1380 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1381 */       mplew.write(85);
/* 1382 */       mplew.writeInt(gid);
/* 1383 */       mplew.writeInt(sid);
/* 1384 */       mplew.writeShort(level);
/* 1385 */       mplew.writeLong(PacketHelper.getTime(expiration));
/* 1386 */       mplew.writeMapleAsciiString(purchase);
/* 1387 */       mplew.writeMapleAsciiString(activate);
/*      */ 
/* 1389 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] guildLeaderChanged(int gid, int oldLeader, int newLeader, int allianceId) {
/* 1393 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1395 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1396 */       mplew.write(89);
/* 1397 */       mplew.writeInt(gid);
/* 1398 */       mplew.writeInt(oldLeader);
/* 1399 */       mplew.writeInt(newLeader);
/* 1400 */       mplew.write(1);
/* 1401 */       mplew.writeInt(allianceId);
/*      */ 
/* 1403 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] denyGuildInvitation(String charname) {
/* 1407 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1409 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1410 */       mplew.write(61);
/* 1411 */       mplew.writeMapleAsciiString(charname);
/*      */ 
/* 1413 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] genericGuildMessage(byte code) {
/* 1417 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1419 */       mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
/* 1420 */       mplew.write(code);
/* 1421 */       if (code == 87) {
/* 1422 */         mplew.writeInt(0);
/*      */       }
/* 1424 */       if ((code == 3) || (code == 59) || (code == 60) || (code == 61) || (code == 84) || (code == 87)) {
/* 1425 */         mplew.writeMapleAsciiString("");
/*      */       }
/*      */ 
/* 1450 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] BBSThreadList(List<MapleBBSThread> bbs, int start) {
/* 1454 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1456 */       mplew.writeShort(SendPacketOpcode.BBS_OPERATION.getValue());
/* 1457 */       mplew.write(6);
/* 1458 */       if (bbs == null) {
/* 1459 */         mplew.write(0);
/* 1460 */         mplew.writeLong(0L);
/* 1461 */         return mplew.getPacket();
/*      */       }
/* 1463 */       int threadCount = bbs.size();
/* 1464 */       MapleBBSThread notice = null;
/* 1465 */       for (MapleBBSThread b : bbs) {
/* 1466 */         if (b.isNotice()) {
/* 1467 */           notice = b;
/* 1468 */           break;
/*      */         }
/*      */       }
/* 1471 */       mplew.write(notice == null ? 0 : 1);
/* 1472 */       if (notice != null) {
/* 1473 */         addThread(mplew, notice);
/*      */       }
/* 1475 */       if (threadCount < start) {
/* 1476 */         start = 0;
/*      */       }
/* 1478 */       mplew.writeInt(threadCount);
/* 1479 */       int pages = Math.min(10, threadCount - start);
/* 1480 */       mplew.writeInt(pages);
/* 1481 */       for (int i = 0; i < pages; i++) {
/* 1482 */         addThread(mplew, (MapleBBSThread)bbs.get(start + i));
/*      */       }
/*      */ 
/* 1485 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     private static void addThread(MaplePacketLittleEndianWriter mplew, MapleBBSThread rs) {
/* 1489 */       mplew.writeInt(rs.localthreadID);
/* 1490 */       mplew.writeInt(rs.ownerID);
/* 1491 */       mplew.writeMapleAsciiString(rs.name);
/* 1492 */       mplew.writeLong(PacketHelper.getKoreanTimestamp(rs.timestamp));
/* 1493 */       mplew.writeInt(rs.icon);
/* 1494 */       mplew.writeInt(rs.getReplyCount());
/*      */     }
/*      */ 
/*      */     public static byte[] showThread(MapleBBSThread thread) {
/* 1498 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1500 */       mplew.writeShort(SendPacketOpcode.BBS_OPERATION.getValue());
/* 1501 */       mplew.write(7);
/* 1502 */       mplew.writeInt(thread.localthreadID);
/* 1503 */       mplew.writeInt(thread.ownerID);
/* 1504 */       mplew.writeLong(PacketHelper.getKoreanTimestamp(thread.timestamp));
/* 1505 */       mplew.writeMapleAsciiString(thread.name);
/* 1506 */       mplew.writeMapleAsciiString(thread.text);
/* 1507 */       mplew.writeInt(thread.icon);
/* 1508 */       mplew.writeInt(thread.getReplyCount());
/* 1509 */       for (MapleBBSThread.MapleBBSReply reply : thread.replies.values()) {
/* 1510 */         mplew.writeInt(reply.replyid);
/* 1511 */         mplew.writeInt(reply.ownerID);
/* 1512 */         mplew.writeLong(PacketHelper.getKoreanTimestamp(reply.timestamp));
/* 1513 */         mplew.writeMapleAsciiString(reply.content);
/*      */       }
/*      */ 
/* 1516 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class InfoPacket
/*      */   {
/*      */     public static byte[] showMesoGain(int gain, boolean inChat)
/*      */     {
/*  777 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  779 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  780 */       if (!inChat) {
/*  781 */         mplew.write(0);
/*  782 */         mplew.write(1);
/*  783 */         mplew.write(0);
/*  784 */         mplew.writeInt(gain);
/*  785 */         mplew.writeShort(0);
/*      */       } else {
/*  787 */         mplew.write(6);
/*  788 */         mplew.writeInt(gain);
/*  789 */         mplew.writeInt(-1);
/*      */       }
/*      */ 
/*  792 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getShowInventoryStatus(int mode) {
/*  796 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  798 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  799 */       mplew.write(0);
/*  800 */       mplew.write(mode);
/*  801 */       mplew.writeInt(0);
/*  802 */       mplew.writeInt(0);
/*      */ 
/*  804 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getShowItemGain(int itemId, short quantity) {
/*  808 */       return getShowItemGain(itemId, quantity, false);
/*      */     }
/*      */ 
/*      */     public static byte[] getShowItemGain(int itemId, short quantity, boolean inChat) {
/*  812 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  814 */       if (inChat) {
/*  815 */         mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
/*  816 */         mplew.write(5);
/*  817 */         mplew.write(1);
/*  818 */         mplew.writeInt(itemId);
/*  819 */         mplew.writeInt(quantity);
/*      */       } else {
/*  821 */         mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  822 */         mplew.writeShort(0);
/*  823 */         mplew.writeInt(itemId);
/*  824 */         mplew.writeInt(quantity);
/*      */       }
/*      */ 
/*  827 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateQuest(MapleQuestStatus quest) {
/*  831 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  833 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  834 */       mplew.write(1);
/*  835 */       mplew.writeShort(quest.getQuest().getId());
/*  836 */       mplew.write(quest.getStatus());
/*  837 */       switch (quest.getStatus()) {
/*      */       case 0:
/*  839 */         mplew.write(0);
/*  840 */         break;
/*      */       case 1:
/*  842 */         mplew.writeMapleAsciiString(quest.getCustomData() != null ? quest.getCustomData() : "");
/*  843 */         break;
/*      */       case 2:
/*  845 */         mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
/*      */       }
/*      */ 
/*  849 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateQuestMobKills(MapleQuestStatus status) {
/*  853 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  855 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  856 */       mplew.write(1);
/*  857 */       mplew.writeShort(status.getQuest().getId());
/*  858 */       mplew.write(1);
/*  859 */       StringBuilder sb = new StringBuilder();
/*  860 */       for (Iterator i$ = status.getMobKills().values().iterator(); i$.hasNext(); ) { int kills = ((Integer)i$.next()).intValue();
/*  861 */         sb.append(StringUtil.getLeftPaddedStr(String.valueOf(kills), '0', 3));
/*      */       }
/*  863 */       mplew.writeMapleAsciiString(sb.toString());
/*  864 */       mplew.writeLong(0L);
/*      */ 
/*  866 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] itemExpired(int itemid) {
/*  870 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  872 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  873 */       mplew.write(2);
/*  874 */       mplew.writeInt(itemid);
/*      */ 
/*  876 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] GainEXP_Monster(int gain, boolean white, int partyinc, int Class_Bonus_EXP, int Equipment_Bonus_EXP, int Premium_Bonus_EXP) {
/*  880 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  882 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  883 */       mplew.write(3);
/*  884 */       mplew.write(white ? 1 : 0);
/*  885 */       mplew.writeInt(gain);
/*  886 */       mplew.write(0);
/*  887 */       mplew.writeInt(0);
/*  888 */       mplew.write(0);
/*  889 */       mplew.write(0);
/*  890 */       mplew.writeInt(0);
/*  891 */       mplew.write(0);
/*  892 */       mplew.writeInt(partyinc);
/*      */ 
/*  894 */       mplew.writeInt(Equipment_Bonus_EXP);
/*  895 */       mplew.writeInt(0);
/*  896 */       mplew.writeInt(0);
/*  897 */       mplew.write(0);
/*  898 */       mplew.writeInt(0);
/*  899 */       mplew.writeInt(0);
/*  900 */       mplew.writeInt(0);
/*      */ 
/*  903 */       mplew.writeInt(0);
/*  904 */       mplew.writeInt(0);
/*  905 */       mplew.writeInt(0);
/*  906 */       mplew.writeInt(Premium_Bonus_EXP);
/*  907 */       mplew.writeInt(0);
/*  908 */       mplew.writeInt(0);
/*  909 */       mplew.writeInt(0);
/*  910 */       mplew.writeInt(0);
/*  911 */       mplew.writeInt(0);
/*  912 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] GainEXP_Others(int gain, boolean inChat, boolean white) {
/*  916 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  940 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  941 */       mplew.write(3);
/*  942 */       mplew.write(white ? 1 : 0);
/*  943 */       mplew.writeInt(gain);
/*  944 */       mplew.write(inChat ? 1 : 0);
/*  945 */       mplew.writeInt(0);
/*  946 */       mplew.writeInt(0);
/*  947 */       mplew.writeInt(0);
/*  948 */       mplew.writeInt(0);
/*  949 */       mplew.writeInt(0);
/*  950 */       mplew.writeInt(0);
/*  951 */       mplew.writeInt(0);
/*  952 */       if (inChat) {
/*  953 */         mplew.writeLong(0L);
/*      */       } else {
/*  955 */         mplew.writeShort(0);
/*  956 */         mplew.write(0);
/*      */       }
/*  958 */       mplew.writeInt(0);
/*  959 */       mplew.writeInt(0);
/*  960 */       mplew.writeInt(0);
/*  961 */       mplew.writeInt(0);
/*  962 */       mplew.writeInt(0);
/*  963 */       mplew.writeInt(0);
/*  964 */       mplew.writeInt(0);
/*  965 */       mplew.writeInt(0);
/*  966 */       mplew.write(0);
/*  967 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getSPMsg(byte sp, short job) {
/*  971 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  973 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  974 */       mplew.write(4);
/*  975 */       mplew.writeShort(job);
/*  976 */       mplew.write(sp);
/*      */ 
/*  978 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getShowFameGain(int gain) {
/*  982 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  984 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  985 */       mplew.write(5);
/*  986 */       mplew.writeInt(gain);
/*      */ 
/*  988 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getGPMsg(int itemid) {
/*  992 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  994 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/*  995 */       mplew.write(7);
/*  996 */       mplew.writeInt(itemid);
/*      */ 
/*  998 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getGPContribution(int itemid) {
/* 1002 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1004 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1005 */       mplew.write(8);
/* 1006 */       mplew.writeInt(itemid);
/*      */ 
/* 1008 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getStatusMsg(int itemid) {
/* 1012 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1014 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1015 */       mplew.write(9);
/* 1016 */       mplew.writeInt(itemid);
/*      */ 
/* 1018 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateInfoQuest(int quest, String data) {
/* 1022 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1024 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1025 */       mplew.write(12);
/* 1026 */       mplew.writeShort(quest);
/* 1027 */       mplew.writeMapleAsciiString(data);
/*      */ 
/* 1029 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showItemReplaceMessage(List<String> message) {
/* 1033 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1035 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1036 */       mplew.write(14);
/* 1037 */       mplew.write(message.size());
/* 1038 */       for (String x : message) {
/* 1039 */         mplew.writeMapleAsciiString(x);
/*      */       }
/*      */ 
/* 1042 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showTraitGain(MapleTrait.MapleTraitType trait, int amount) {
/* 1046 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1048 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1049 */       mplew.write(16);
/* 1050 */       mplew.writeLong(trait.getStat().getValue());
/* 1051 */       mplew.writeInt(amount);
/*      */ 
/* 1053 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showTraitMaxed(MapleTrait.MapleTraitType trait) {
/* 1057 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1059 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1060 */       mplew.write(17);
/* 1061 */       mplew.writeLong(trait.getStat().getValue());
/*      */ 
/* 1063 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getBPMsg(int amount) {
/* 1067 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1069 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1070 */       mplew.write(21);
/* 1071 */       mplew.writeInt(amount);
/* 1072 */       mplew.writeInt(0);
/*      */ 
/* 1074 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showExpireMessage(byte type, List<Integer> item) {
/* 1078 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(4 + item.size() * 4);
/*      */ 
/* 1081 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1082 */       mplew.write(type);
/* 1083 */       mplew.write(item.size());
/* 1084 */       for (Integer it : item) {
/* 1085 */         mplew.writeInt(it.intValue());
/*      */       }
/*      */ 
/* 1088 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showStatusMessage(int mode, String info, String data) {
/* 1092 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1096 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1097 */       mplew.write(mode);
/* 1098 */       if (mode == 22) {
/* 1099 */         mplew.writeMapleAsciiString(info);
/* 1100 */         mplew.writeMapleAsciiString(data);
/*      */       }
/*      */ 
/* 1103 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] showReturnStone(int act) {
/* 1107 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/* 1113 */       mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
/* 1114 */       mplew.write(23);
/* 1115 */       mplew.write(act);
/*      */ 
/* 1117 */       return mplew.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class BuffPacket
/*      */   {
/*      */        public static byte[] giveDice(int buffid, int skillid, int duration, Map<MapleBuffStat, Integer> statups) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
        PacketHelper.writeBuffMask(mplew, statups);

        mplew.writeShort(Math.max(buffid / 100, Math.max(buffid / 10, buffid % 10))); // 1-6

        mplew.writeInt(skillid); // skillid
        mplew.writeInt(duration);
        mplew.writeShort(0);
        mplew.write(0);
	mplew.writeInt(GameConstants.getDiceStat(buffid, 3));
	mplew.writeInt(GameConstants.getDiceStat(buffid, 3));
	mplew.writeInt(GameConstants.getDiceStat(buffid, 4));
	mplew.writeZeroBytes(20); //idk
	mplew.writeInt(GameConstants.getDiceStat(buffid, 2));
	mplew.writeZeroBytes(12); //idk
	mplew.writeInt(GameConstants.getDiceStat(buffid, 5));
	mplew.writeZeroBytes(16); //idk
	mplew.writeInt(GameConstants.getDiceStat(buffid, 6));
	mplew.writeZeroBytes(16);
        mplew.write(1);
        mplew.write(4); // Total buffed times
        mplew.write(0);//v112
        return mplew.getPacket();
    }
/*      */ 
/*      */       public static byte[] giveHoming(int skillid, int mobid, int x) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
        PacketHelper.writeSingleMask(mplew, MapleBuffStat.HOMING_BEACON);
        mplew.writeShort(0);
        mplew.write(0);
        mplew.writeInt(1);
        mplew.writeLong(skillid);
        mplew.write(0);
        mplew.writeLong(mobid);
        mplew.writeShort(0);
        mplew.writeShort(0);
        mplew.write(0);
        mplew.write(0);//v112
        return mplew.getPacket();
    }
/*      */ 
/*      */     public static byte[] giveMount(int buffid, int skillid, Map<MapleBuffStat, Integer> statups) {
/*  438 */       return showMonsterRiding(-1, statups, buffid, skillid);
/*      */     }
/*      */ 
/*      */     public static byte[] showMonsterRiding(int cid, Map<MapleBuffStat, Integer> statups, int buffid, int skillId) {
/*  442 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  444 */       if (cid == -1) {
/*  445 */         mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
/*      */       } else {
/*  447 */         mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
/*  448 */         mplew.writeInt(cid);
/*      */       }
/*  450 */       PacketHelper.writeBuffMask(mplew, statups);
/*  451 */       mplew.writeShort(0);
/*  452 */       mplew.write(0);
/*  453 */       mplew.writeInt(buffid);
/*  454 */       mplew.writeInt(skillId);
/*  455 */       mplew.writeInt(0);
/*  456 */       mplew.writeShort(0);
/*  457 */       mplew.write(1);
/*  458 */       mplew.write(4);
/*  459 */       mplew.write(0);
/*  460 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] givePirate(Map<MapleBuffStat, Integer> statups, int duration, int skillid) {
/*  464 */       return giveForeignPirate(statups, duration, -1, skillid);
/*      */     }
/*      */ 
    public static byte[] giveForeignPirate(Map<MapleBuffStat, Integer> statups, int duration, int cid, int skillid) {
        final boolean infusion = skillid == 5121009 || skillid == 15111005;
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
        mplew.writeInt(cid);
        PacketHelper.writeBuffMask(mplew, statups);
        mplew.writeShort(0);
        mplew.write(0);
        for (Integer stat : statups.values()) {
            mplew.writeInt(stat.intValue());
            mplew.writeLong(skillid);
            mplew.writeZeroBytes(infusion ? 6 : 1);
            mplew.writeShort(duration);//duration... seconds
        }
        mplew.writeShort(0);
        mplew.writeShort(0);
        mplew.write(1);
        mplew.write(1);
        return mplew.getPacket();
    }

/*      */ 
/*      */        public static byte[] giveArcane(Map<Integer, Integer> statups, int duration) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
        PacketHelper.writeSingleMask(mplew, MapleBuffStat.ARCANE_AIM);

        mplew.writeShort(0);
        mplew.write(0);
        mplew.writeInt(statups.size());
        for (Entry<Integer, Integer> stat : statups.entrySet()) {
            mplew.writeInt(stat.getKey());
            mplew.writeLong(stat.getValue());
            mplew.writeInt(duration);
        }
        mplew.writeShort(0);
        mplew.writeShort(0);
        mplew.write(1);
        mplew.write(1);
        mplew.write(0);//v112
        return mplew.getPacket();
    }
/*      */ 
/*      */     public static byte[] giveEnergyChargeTest(int bar, int bufflength) {
/*  518 */       return giveEnergyChargeTest(-1, bar, bufflength);
/*      */     }
/*      */ 
/*      */     public static byte[] giveEnergyChargeTest(int cid, int bar, int bufflength) {
/*  522 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  524 */       if (cid == -1) {
/*  525 */         mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
/*      */       } else {
/*  527 */         mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
/*  528 */         mplew.writeInt(cid);
/*      */       }
/*  530 */       PacketHelper.writeSingleMask(mplew, MapleBuffStat.ENERGY_CHARGE);
/*  531 */       mplew.writeShort(0);
/*  532 */       mplew.write(0);
/*  533 */       mplew.writeInt(Math.min(bar, 10000));
/*  534 */       mplew.writeLong(0L);
/*  535 */       mplew.write(0);
/*      */ 
/*  537 */       mplew.writeInt(bar >= 10000 ? bufflength : 0);
/*  538 */       mplew.write(0);
/*  539 */       mplew.write(6);
/*  540 */       return mplew.getPacket();
/*      */     }
/*      */ 
   
        public static byte[] giveAriaBuff(int bufflevel, int buffid, int bufflength) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            
            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 00 00 00 80 00 00 00 00 00 00 00 00 00 00 40 00 00 00 00 00 00 00 00 00 00 00 00 00"));
            for (int i = 0; i < 2; i++) {
            mplew.writeShort(bufflevel); // it work now :DDDD also lets put in my packetwriteconsole? O_O after this test :D kk
            mplew.writeInt(buffid);
            mplew.writeInt(bufflength);
            }
            mplew.writeZeroBytes(3);
            mplew.writeShort(0); // not sure..
            mplew.write(0);
            //System.out.println("ARIRA_BUFF PACKET: " + mplew.toString());
            return mplew.getPacket();
        }

/*      */     public static byte[] giveBuff(int buffid, int bufflength, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect) {
/*  544 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  558 */       mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
/*  559 */       PacketHelper.writeBuffMask(mplew, statups);
/*  560 */       boolean stacked = false;
/*  561 */       boolean isAura = false;
/*  562 */       for (Map.Entry stat : statups.entrySet()) {
/*  563 */         if ((stat.getKey() == MapleBuffStat.YELLOW_AURA) || (stat.getKey() == MapleBuffStat.BLUE_AURA) || (stat.getKey() == MapleBuffStat.DARK_AURA)) {
/*  564 */           isAura = true;
/*      */         }
/*  566 */         if (((MapleBuffStat)stat.getKey()).canStack()) {
/*  567 */           if (!stacked) {
/*  568 */             mplew.writeZeroBytes(3);
/*  569 */             stacked = true;
/*      */           }
/*  571 */           mplew.writeInt(1);
/*  572 */           mplew.writeInt(buffid);
/*  573 */           mplew.writeLong(((Integer)stat.getValue()).longValue());
/*      */         } else {
                     if (statups.containsKey(MapleBuffStat.BODY_BOOST) || statups.containsKey(MapleBuffStat.JUDGMENT_DRAW)) {
                     mplew.writeInt(0);
                     }
/*  575 */           if (stat.getKey() == MapleBuffStat.SPIRIT_SURGE)
/*  576 */             mplew.writeInt(((Integer)stat.getValue()).intValue());
/*      */           else {
/*  578 */             mplew.writeShort(((Integer)stat.getValue()).intValue());
/*      */           }
/*  580 */           mplew.writeInt(buffid);
/*      */         }
/*  582 */         mplew.writeInt(bufflength);
/*      */       }
/*  584 */       if (!isAura) {
/*  585 */         mplew.writeShort(0);
/*  586 */         if ((effect != null) && (effect.isDivineShield()))
/*  587 */           mplew.writeInt(effect.getEnhancedWatk());
/*  588 */         else if ((effect != null) && (effect.getCharColor() > 0))
/*  589 */           mplew.writeInt(effect.getCharColor());
/*  590 */         else if ((effect != null) && (effect.isInflation())) {
/*  591 */           mplew.writeInt(effect.getInflation());
/*      */         }
/*      */       }
               
/*  594 */       mplew.writeShort(1000);
/*  595 */       mplew.writeShort(0);
/*  596 */       mplew.write(0);
/*  597 */       mplew.write(0);
/*  598 */       mplew.write((effect != null) && (effect.isShadow()) ? 1 : 3);
/*  599 */       if (isAura) {
/*  600 */         mplew.writeInt(0);
/*      */       }
/*      */ 
/*  603 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] giveDebuff(MapleDisease statups, int x, int skillid, int level, int duration) {
/*  607 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  610 */       mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
/*  611 */       PacketHelper.writeSingleMask(mplew, statups);
/*  612 */       mplew.writeShort(x);
/*  613 */       mplew.writeShort(skillid);
/*  614 */       mplew.writeShort(level);
/*  615 */       mplew.writeInt(duration);
/*  616 */       mplew.writeShort(0);
/*  617 */       mplew.writeShort(0);
/*  618 */       mplew.write(1);
/*  619 */       mplew.write(0);
/*  620 */       mplew.write(1);
/*  621 */       System.out.println(HexTool.toString(mplew.getPacket()));
/*  622 */       return mplew.getPacket();
/*      */     }
/*      */ 
    public static byte[] cancelBuff(List<MapleBuffStat> statups) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

        PacketHelper.writeMask(mplew, statups);
        for (MapleBuffStat z : statups) {
            if (z.canStack()) {
                mplew.writeInt(0); //amount of buffs still in the stack? dunno mans
            }
        }
        mplew.write(3);
        mplew.write(1);
        mplew.writeLong(0);
        mplew.writeLong(0);
        mplew.writeLong(0);
        mplew.write(0);//v112
        return mplew.getPacket();
    }
/*      */ 
/*      */      public static byte[] cancelDebuff(MapleDisease mask) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

        PacketHelper.writeSingleMask(mplew, mask);
        mplew.write(3);
        mplew.write(1);
        mplew.writeLong(0);
        mplew.write(0);//v112
        return mplew.getPacket();
    }
/*      */ 
    public static byte[] cancelHoming() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

        PacketHelper.writeSingleMask(mplew, MapleBuffStat.HOMING_BEACON);
        mplew.write(0);//v112

        return mplew.getPacket();
    }

/*      */ 


/*      */   //  public static byte[] giveForeignBuff(int cid, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect) {
/*  662 */   //    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  664 */    //   mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
/*  665 */     //  mplew.writeInt(cid);
/*  666 */      // PacketHelper.writeBuffMask(mplew, statups);
/*  667 */     //  for (Map.Entry statup : statups.entrySet()) {
/*  668 */      //   if ((statup.getKey() == MapleBuffStat.SHADOWPARTNER) || (statup.getKey() == MapleBuffStat.MECH_CHANGE) || (statup.getKey() == MapleBuffStat.DARK_AURA) || (statup.getKey() == MapleBuffStat.YELLOW_AURA) || (statup.getKey() == MapleBuffStat.BLUE_AURA) || (statup.getKey() == MapleBuffStat.GIANT_POTION) || (statup.getKey() == MapleBuffStat.SPIRIT_LINK) || (statup.getKey() == MapleBuffStat.PYRAMID_PQ) || (statup.getKey() == MapleBuffStat.WK_CHARGE) || (statup.getKey() == MapleBuffStat.SPIRIT_SURGE) || (statup.getKey() == MapleBuffStat.MORPH) || (statup.getKey() == MapleBuffStat.DARK_METAMORPHOSIS)) {
/*  669 */       //    mplew.writeShort(((Integer)statup.getValue()).shortValue());
/*  670 */         //  mplew.writeInt(effect.isSkill() ? effect.getSourceId() : -effect.getSourceId());
/*  671 */      //   } else if (statup.getKey() == MapleBuffStat.FAMILIAR_SHADOW) {
/*  672 */        //   mplew.writeInt(((Integer)statup.getValue()).intValue());
/*  673 */        ///   mplew.writeInt(effect.getCharColor());
/*      */    //     } else {
/*  675 */         //  mplew.writeShort(((Integer)statup.getValue()).shortValue());
/*      */      //   }
/*      */      // /}
/*  678 */    //   mplew.writeShort(0);
/*  679 */       //mplew.writeShort(0);
/*  680 */     //  mplew.write(1);
/*  681 */       //mplew.write(1);
/*      */ 
/*  683 */     //  return mplew.getPacket();
/*      */    // }


  public static byte[] giveForeignBuff(int cid, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
        mplew.writeInt(cid);

        PacketHelper.writeBuffMask(mplew, statups);
        for (Entry<MapleBuffStat, Integer> statup : statups.entrySet()) {
            if (statup.getKey() == MapleBuffStat.SHADOWPARTNER || statup.getKey() == MapleBuffStat.MECH_CHANGE || statup.getKey() == MapleBuffStat.DARK_AURA || statup.getKey() == MapleBuffStat.YELLOW_AURA || statup.getKey() == MapleBuffStat.BLUE_AURA || statup.getKey() == MapleBuffStat.GIANT_POTION || statup.getKey() == MapleBuffStat.SPIRIT_LINK || statup.getKey() == MapleBuffStat.PYRAMID_PQ || statup.getKey() == MapleBuffStat.WK_CHARGE || statup.getKey() == MapleBuffStat.SPIRIT_SURGE || statup.getKey() == MapleBuffStat.MORPH || statup.getKey() == MapleBuffStat.WATER_SHIELD) {
                mplew.writeShort(statup.getValue().shortValue());
                mplew.writeInt(effect.isSkill() ? effect.getSourceId() : -effect.getSourceId());
            } else if (statup.getKey() == MapleBuffStat.FAMILIAR_SHADOW) {
                mplew.writeInt(statup.getValue().intValue());
                mplew.writeInt(effect.getCharColor());
            } else {
                mplew.writeShort(statup.getValue().shortValue());
            }
        }
        mplew.writeShort(0); // same as give_buff
        mplew.writeShort(0);
        mplew.write(1);
        mplew.write(1);
        mplew.write(0);//v112

        return mplew.getPacket();
    }

/*      */ 
/*      */      public static byte[] giveForeignDebuff(int cid, final MapleDisease statups, int skillid, int level, int x) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
        mplew.writeInt(cid);

        PacketHelper.writeSingleMask(mplew, statups);
        if (skillid == 125) {
            mplew.writeShort(0);
            mplew.write(0); //todo test
        }
        mplew.writeShort(x);
        mplew.writeShort(skillid);
        mplew.writeShort(level);
        mplew.writeShort(0); // same as give_buff
        mplew.writeShort(0); //Delay
        mplew.write(1);
        mplew.write(1);
        mplew.write(0);//v112
        return mplew.getPacket();
    }

/*      */ 
    public static byte[] cancelForeignBuff(int cid, List<MapleBuffStat> statups) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CANCEL_FOREIGN_BUFF.getValue());
        mplew.writeInt(cid);
        PacketHelper.writeMask(mplew, statups);
        mplew.write(3);
        mplew.write(1);
        mplew.write(0);//v112

        return mplew.getPacket();
    }
/*      */ 
/*      */         public static byte[] cancelForeignDebuff(int cid, MapleDisease mask) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CANCEL_FOREIGN_BUFF.getValue());
        mplew.writeInt(cid);

        PacketHelper.writeSingleMask(mplew, mask);
        mplew.write(3);
        mplew.write(1);
        mplew.write(0);//v112
        return mplew.getPacket();
    }

/*      */ 
/*      */    
/*      */ 
/*      */     public static byte[] giveCard(int cid, int oid, int skillid)
/*      */     {
/*  754 */       MaplePacketLittleEndianWriter writer = new MaplePacketLittleEndianWriter();
/*  755 */       writer.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
/*  756 */       writer.write(0);
/*  757 */       writer.writeInt(cid);
/*  758 */       writer.writeInt(1);
/*  759 */       writer.writeInt(oid);
/*  760 */       writer.writeInt(skillid);
/*  761 */       writer.write(1);
/*  762 */       writer.writeInt(2);
/*  763 */       writer.writeInt(1);
/*  764 */       writer.writeInt(21);
/*  765 */       writer.writeInt(8);
/*  766 */       writer.writeInt(8);
/*  767 */       writer.write(0);
/*  768 */       return writer.getPacket();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class InventoryPacket
/*      */   {
/*      */     public static byte[] addInventorySlot(MapleInventoryType type, Item item)
/*      */     {
/*   80 */       return addInventorySlot(type, item, false);
/*      */     }
/*      */ 
/*      */     public static byte[] addInventorySlot(MapleInventoryType type, Item item, boolean fromDrop) {
/*   84 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*   86 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*   87 */       mplew.write(fromDrop ? 1 : 0);
/*   88 */       mplew.write(1);
/*   89 */       mplew.write(1);
/*      */ 
/*   91 */       mplew.write(GameConstants.isInBag(item.getPosition(), type.getType()) ? 9 : 0);
/*   92 */       mplew.write(type.getType());
/*   93 */       mplew.writeShort(item.getPosition());
/*   94 */       PacketHelper.addItemInfo(mplew, item);
/*   95 */       mplew.write(0);
/*      */ 
/*   97 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateInventorySlot(MapleInventoryType type, Item item, boolean fromDrop) {
/*  101 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  103 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  104 */       mplew.write(fromDrop ? 1 : 0);
/*  105 */       mplew.write(1);
/*  106 */       mplew.write(0);
/*      */ 
/*  108 */       mplew.write(GameConstants.isInBag(item.getPosition(), type.getType()) ? 6 : 1);
/*  109 */       mplew.write(type.getType());
/*  110 */       mplew.writeShort(item.getPosition());
/*  111 */       mplew.writeShort(item.getQuantity());
/*  112 */       mplew.write(0);
/*      */ 
/*  114 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] moveInventoryItem(MapleInventoryType type, short src, short dst, boolean bag, boolean bothBag) {
/*  118 */       return moveInventoryItem(type, src, dst, (byte)-1, bag, bothBag);
/*      */     }
/*      */ 
/*      */     public static byte[] moveInventoryItem(MapleInventoryType type, short src, short dst, short equipIndicator, boolean bag, boolean bothBag) {
/*  122 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  124 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  125 */       mplew.write(1);
/*  126 */       mplew.write(1);
/*  127 */       mplew.write(0);
/*      */ 
/*  129 */       mplew.write(bag ? 5 : bothBag ? 8 : 2);
/*  130 */       mplew.write(type.getType());
/*  131 */       mplew.writeShort(src);
/*  132 */       mplew.writeShort(dst);
/*  133 */       if (bag) {
/*  134 */         mplew.writeShort(0);
/*      */       }
/*  136 */       if (equipIndicator != -1) {
/*  137 */         mplew.write(equipIndicator);
/*      */       }
/*      */ 
/*  140 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] moveAndMergeInventoryItem(MapleInventoryType type, short src, short dst, short total, boolean bag, boolean switchSrcDst, boolean bothBag) {
/*  144 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  146 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  147 */       mplew.write(1);
/*  148 */       mplew.write(2);
/*  149 */       mplew.write(0);
/*      */ 
/*  151 */       mplew.write((bag) && ((switchSrcDst) || (bothBag)) ? 7 : 3);
/*  152 */       mplew.write(type.getType());
/*  153 */       mplew.writeShort(src);
/*      */ 
/*  155 */       mplew.write((bag) && ((!switchSrcDst) || (bothBag)) ? 6 : 1);
/*  156 */       mplew.write(type.getType());
/*  157 */       mplew.writeShort(dst);
/*  158 */       mplew.writeShort(total);
/*      */ 
/*  160 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] moveAndMergeWithRestInventoryItem(MapleInventoryType type, short src, short dst, short srcQ, short dstQ, boolean bag, boolean switchSrcDst, boolean bothBag) {
/*  164 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  166 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  167 */       mplew.write(1);
/*  168 */       mplew.write(2);
/*  169 */       mplew.write(0);
/*      */ 
/*  171 */       mplew.write((bag) && ((switchSrcDst) || (bothBag)) ? 6 : 1);
/*  172 */       mplew.write(type.getType());
/*  173 */       mplew.writeShort(src);
/*  174 */       mplew.writeShort(srcQ);
/*      */ 
/*  176 */       mplew.write((bag) && ((!switchSrcDst) || (bothBag)) ? 6 : 1);
/*  177 */       mplew.write(type.getType());
/*  178 */       mplew.writeShort(dst);
/*  179 */       mplew.writeShort(dstQ);
/*      */ 
/*  181 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] clearInventoryItem(MapleInventoryType type, short slot, boolean fromDrop) {
/*  185 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  187 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  188 */       mplew.write(fromDrop ? 1 : 0);
/*  189 */       mplew.write(1);
/*  190 */       mplew.write(0);
/*      */ 
/*  192 */       mplew.write((slot > 100) && (type == MapleInventoryType.ETC) ? 7 : 3);
/*  193 */       mplew.write(type.getType());
/*  194 */       mplew.writeShort(slot);
/*      */ 
/*  196 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateSpecialItemUse(Item item, byte invType, MapleCharacter chr) {
/*  200 */       return updateSpecialItemUse(item, invType, item.getPosition(), false, chr);
/*      */     }
/*      */ 
/*      */     public static byte[] updateSpecialItemUse(Item item, byte invType, short pos, boolean theShort, MapleCharacter chr) {
/*  204 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  206 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  207 */       mplew.write(0);
/*  208 */       mplew.write(2);
/*  209 */       mplew.write(0);
/*      */ 
/*  212 */       mplew.write(GameConstants.isInBag(pos, invType) ? 7 : 3);
/*  213 */       mplew.write(invType);
/*  214 */       mplew.writeShort(pos);
/*      */ 
/*  216 */       mplew.write(0);
/*  217 */       mplew.write(invType);
/*  218 */       if ((item.getType() == 1) || (theShort))
/*  219 */         mplew.writeShort(pos);
/*      */       else {
/*  221 */         mplew.write(pos);
/*      */       }
/*  223 */       PacketHelper.addItemInfo(mplew, item, chr);
/*  224 */       if (pos < 0) {
/*  225 */         mplew.write(2);
/*      */       }
/*      */ 
/*  228 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] updateSpecialItemUse_(Item item, byte invType, MapleCharacter chr) {
/*  232 */       return updateSpecialItemUse_(item, invType, item.getPosition(), chr);
/*      */     }
/*      */ 
/*      */     public static byte[] updateSpecialItemUse_(Item item, byte invType, short pos, MapleCharacter chr) {
/*  236 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  238 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  239 */       mplew.write(0);
/*  240 */       mplew.write(1);
/*  241 */       mplew.write(0);
/*      */ 
/*  243 */       mplew.write(0);
/*  244 */       mplew.write(invType);
/*  245 */       if (item.getType() == 1)
/*  246 */         mplew.writeShort(pos);
/*      */       else {
/*  248 */         mplew.write(pos);
/*      */       }
/*  250 */       PacketHelper.addItemInfo(mplew, item, chr);
/*  251 */       if (pos < 0) {
/*  252 */         mplew.write(1);
/*      */       }
/*      */ 
/*  255 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] scrolledItem(Item scroll, Item item, boolean destroyed, boolean potential) {
/*  259 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  261 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  262 */       mplew.write(1);
/*  263 */       mplew.write(destroyed ? 2 : 3);
/*  264 */       mplew.write(0);
/*      */ 
/*  266 */       mplew.write(scroll.getQuantity() > 0 ? 1 : 3);
/*  267 */       mplew.write(GameConstants.getInventoryType(scroll.getItemId()).getType());
/*  268 */       mplew.writeShort(scroll.getPosition());
/*  269 */       if (scroll.getQuantity() > 0) {
/*  270 */         mplew.writeShort(scroll.getQuantity());
/*      */       }
/*      */ 
/*  273 */       mplew.write(3);
/*  274 */       mplew.write(MapleInventoryType.EQUIP.getType());
/*  275 */       mplew.writeShort(item.getPosition());
/*  276 */       if (!destroyed) {
/*  277 */         mplew.write(0);
/*  278 */         mplew.write(MapleInventoryType.EQUIP.getType());
/*  279 */         mplew.writeShort(item.getPosition());
/*  280 */         PacketHelper.addItemInfo(mplew, item);
/*      */       }
/*  282 */       if (!potential) {
/*  283 */         mplew.write(1);
/*      */       }
/*      */ 
/*  286 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] moveAndUpgradeItem(MapleInventoryType type, Item item, short oldpos, short newpos, MapleCharacter chr) {
/*  290 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*  291 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  292 */       mplew.write(1);
/*  293 */       mplew.write(3);
/*  294 */       mplew.write(0);
/*      */ 
/*  296 */       mplew.write(GameConstants.isInBag(newpos, type.getType()) ? 7 : 3);
/*  297 */       mplew.write(type.getType());
/*  298 */       mplew.writeShort(oldpos);
/*      */ 
/*  300 */       mplew.write(0);
/*  301 */       mplew.write(1);
/*  302 */       mplew.writeShort(oldpos);
/*  303 */       PacketHelper.addItemInfo(mplew, item, chr);
/*      */ 
/*  305 */       mplew.write(2);
/*  306 */       mplew.write(type.getType());
/*  307 */       mplew.writeShort(oldpos);
/*  308 */       mplew.writeShort(newpos);
/*  309 */       mplew.write(0);
/*      */ 
/*  311 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] dropInventoryItem(MapleInventoryType type, short src) {
/*  315 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  317 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  318 */       mplew.write(1);
/*  319 */       mplew.write(1);
/*  320 */       mplew.write(0);
/*      */ 
/*  322 */       mplew.write(3);
/*  323 */       mplew.write(type.getType());
/*  324 */       mplew.writeShort(src);
/*  325 */       if (src < 0) {
/*  326 */         mplew.write(1);
/*      */       }
/*      */ 
/*  329 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] dropInventoryItemUpdate(MapleInventoryType type, Item item) {
/*  333 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  335 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  336 */       mplew.write(1);
/*  337 */       mplew.write(1);
/*  338 */       mplew.write(0);
/*      */ 
/*  340 */       mplew.write(1);
/*  341 */       mplew.write(type.getType());
/*  342 */       mplew.writeShort(item.getPosition());
/*  343 */       mplew.writeShort(item.getQuantity());
/*      */ 
/*  345 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getInventoryFull() {
/*  349 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  351 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  352 */       mplew.write(1);
/*  353 */       mplew.write(0);
/*  354 */       mplew.write(0);
/*      */ 
/*  356 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getInventoryStatus() {
/*  360 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  362 */       mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
/*  363 */       mplew.write(0);
/*  364 */       mplew.write(0);
/*  365 */       mplew.write(0);
/*      */ 
/*  367 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getSlotUpdate(byte invType, byte newSlots) {
/*  371 */       MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
/*      */ 
/*  373 */       mplew.writeShort(SendPacketOpcode.INVENTORY_GROW.getValue());
/*  374 */       mplew.write(invType);
/*  375 */       mplew.write(newSlots);
/*      */ 
/*  377 */       return mplew.getPacket();
/*      */     }
/*      */ 
/*      */     public static byte[] getShowInventoryFull() {
/*  381 */       return CWvsContext.InfoPacket.getShowInventoryStatus(255);
/*      */     }
/*      */ 
/*      */     public static byte[] showItemUnavailable() {
/*  385 */       return CWvsContext.InfoPacket.getShowInventoryStatus(254);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Users\Sjögren\Desktop\lithium.jar
 * Qualified Name:     tools.packet.CWvsContext
 * JD-Core Version:    0.6.0
 */