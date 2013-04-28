/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2005 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DatabaseConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import tools.FileoutputUtil;

public class RankingWorker {

    private final static Map<Integer, List<RankingInformation>> rankings = new HashMap<Integer, List<RankingInformation>>();
    private final static Map<String, Integer> jobCommands = new HashMap<String, Integer>();
    private final static List<PokemonInformation> pokemon = new ArrayList<PokemonInformation>();
    private final static List<PokedexInformation> pokemon_seen = new ArrayList<PokedexInformation>();
    private final static List<PokebattleInformation> pokemon_ratio = new ArrayList<PokebattleInformation>();

    public final static Integer getJobCommand(final String job) {
        return jobCommands.get(job);
    }

    public final static Map<String, Integer> getJobCommands() {
        return jobCommands;
    }

    public final static List<RankingInformation> getRankingInfo(final int job) {
        return rankings.get(job);
    }

    public final static List<PokemonInformation> getPokemonInfo() {
        return pokemon;
    }

    public final static List<PokedexInformation> getPokemonCaught() {
        return pokemon_seen;
    }

    public final static List<PokebattleInformation> getPokemonRatio() {
        return pokemon_ratio;
    }

    public final static void run() {
        System.out.println("Loading Rankings::");
        long startTime = System.currentTimeMillis();
        loadJobCommands();
        try {
            Connection con = DatabaseConnection.getConnection();
            updateRanking(con);
            updatePokemon(con);
            updatePokemonRatio(con);
            updatePokemonCaught(con);
        } catch (Exception ex) {
            ex.printStackTrace();
	    FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
            System.err.println("Could not update rankings");
        }
        System.out.println("Done loading Rankings in " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds :::"); //keep
    }

    private static void updateRanking(Connection con) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT c.id, c.job, c.exp, c.level, c.name, c.jobRank, c.rank, c.fame");
        sb.append(" FROM characters AS c LEFT JOIN accounts AS a ON c.accountid = a.id WHERE c.gm = 0 AND a.banned = 0 AND c.level >= 30");
        sb.append(" ORDER BY c.level DESC , c.exp DESC , c.fame DESC , c.rank ASC");

        PreparedStatement charSelect = con.prepareStatement(sb.toString());
        ResultSet rs = charSelect.executeQuery();
        PreparedStatement ps = con.prepareStatement("UPDATE characters SET jobRank = ?, jobRankMove = ?, rank = ?, rankMove = ? WHERE id = ?");
        int rank = 0; //for "all"
        final Map<Integer, Integer> rankMap = new LinkedHashMap<Integer, Integer>();
        for (int i : jobCommands.values()) {
            rankMap.put(i, 0); //job to rank
            rankings.put(i, new ArrayList<RankingInformation>());
        }
        while (rs.next()) {
            int job = rs.getInt("job");
            if (!rankMap.containsKey(job / 100)) { //not supported.
                continue;
            }
            int jobRank = rankMap.get(job / 100) + 1;
            rankMap.put(job / 100, jobRank);
            rank++;
            rankings.get(-1).add(new RankingInformation(rs.getString("name"), job, rs.getInt("level"), rs.getInt("exp"), rank, rs.getInt("fame")));
            rankings.get(job / 100).add(new RankingInformation(rs.getString("name"), job, rs.getInt("level"), rs.getInt("exp"), jobRank, rs.getInt("fame")));
            ps.setInt(1, jobRank);
            ps.setInt(2, rs.getInt("jobRank") - jobRank);
            ps.setInt(3, rank);
            ps.setInt(4, rs.getInt("rank") - rank);
            ps.setInt(5, rs.getInt("id"));
            ps.addBatch();
        }
        ps.executeBatch(); //Batch update should be faster.
        rs.close();
        charSelect.close();
        ps.close();
    }

    private static void updatePokemon(Connection con) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT count(distinct m.id) AS mc, c.name, c.totalWins, c.totalLosses ");
        sb.append(" FROM characters AS c LEFT JOIN accounts AS a ON c.accountid = a.id");
        sb.append(" RIGHT JOIN monsterbook AS m ON m.charid = a.id WHERE c.gm = 0 AND a.banned = 0");
        sb.append(" ORDER BY c.totalWins DESC, c.totalLosses DESC, mc DESC LIMIT 50");

        PreparedStatement charSelect = con.prepareStatement(sb.toString());
        ResultSet rs = charSelect.executeQuery();
        int rank = 0; //for "all"
        while (rs.next()) {
            rank++;
            pokemon.add(new PokemonInformation(rs.getString("name"), rs.getInt("totalWins"), rs.getInt("totalLosses"), rs.getInt("mc"), rank));
        }
        rs.close();
        charSelect.close();
    }

    private static void updatePokemonRatio(Connection con) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT (c.totalWins / c.totalLosses) AS mc, c.name, c.totalWins, c.totalLosses ");
        sb.append(" FROM characters AS c LEFT JOIN accounts AS a ON c.accountid = a.id");
        sb.append(" WHERE c.gm = 0 AND a.banned = 0 AND c.totalWins > 10 AND c.totalLosses > 0");
        sb.append(" ORDER BY mc DESC, c.totalWins DESC, c.totalLosses ASC LIMIT 50");

        PreparedStatement charSelect = con.prepareStatement(sb.toString());
        ResultSet rs = charSelect.executeQuery();
        int rank = 0; //for "all"
        while (rs.next()) {
            rank++;
            pokemon_ratio.add(new PokebattleInformation(rs.getString("name"), rs.getInt("totalWins"), rs.getInt("totalLosses"), rs.getDouble("mc"), rank));
        }
        rs.close();
        charSelect.close();
    }

    private static void updatePokemonCaught(Connection con) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT count(distinct m.id) AS mc, c.name ");
        sb.append(" FROM characters AS c LEFT JOIN accounts AS a ON c.accountid = a.id");
        sb.append(" RIGHT JOIN monsterbook AS m ON m.charid = a.id WHERE c.gm = 0 AND a.banned = 0");
        sb.append(" ORDER BY mc DESC LIMIT 50");

        PreparedStatement charSelect = con.prepareStatement(sb.toString());
        ResultSet rs = charSelect.executeQuery();
        int rank = 0; //for "all"
        while (rs.next()) {
            rank++;
            pokemon_seen.add(new PokedexInformation(rs.getString("name"), rs.getInt("mc"), rank));
        }
        rs.close();
        charSelect.close();
    }

    public final static void loadJobCommands() {
        //messy, cleanup
        jobCommands.put("all", -1);
        jobCommands.put("beginner", 0);
        jobCommands.put("warrior", 1);
        jobCommands.put("magician", 2);
        jobCommands.put("bowman", 3);
        jobCommands.put("thief", 4);
        jobCommands.put("pirate", 5);
        jobCommands.put("nobless", 10);
        jobCommands.put("soulmaster", 11);
        jobCommands.put("flamewizard", 12);
        jobCommands.put("windbreaker", 13);
        jobCommands.put("nightwalker", 14);
        jobCommands.put("striker", 15);
        jobCommands.put("legend", 20);
        jobCommands.put("aran", 21);
        jobCommands.put("evan", 22);
        jobCommands.put("citizen", 30);
        jobCommands.put("battlemage", 32);
        jobCommands.put("wildhunter", 33);
        jobCommands.put("mechanic", 35);
    }

    public static class RankingInformation {

        public String toString;
        public int rank;

        public RankingInformation(String name, int job, int level, int exp, int rank, int fame) {
            this.rank = rank;
            final StringBuilder builder = new StringBuilder("Rank ");
            builder.append(rank);
            builder.append(" : ");
            builder.append(name);
            builder.append(" - Level ");
            builder.append(level);
            builder.append(" ");
            builder.append(MapleCarnivalChallenge.getJobNameById(job));
            builder.append(" | ");
            builder.append(exp);
            builder.append(" EXP, ");
	    builder.append(fame);
	    builder.append(" Fame");
            this.toString = builder.toString(); //Rank 1 : KiDALex - Level 200 Blade Master | 0 EXP, 30000 Fame
        }

        @Override
        public String toString() {
            return toString;
        }
    }

    public static class PokemonInformation {

        public String toString;

        public PokemonInformation(String name, int totalWins, int totalLosses, int caught, int rank) {
            final StringBuilder builder = new StringBuilder("Rank ");
            builder.append(rank);
            builder.append(" : #e");
            builder.append(name);
            builder.append("#n - #r");
            builder.append(totalWins);
            builder.append(" Wins, #b");
            builder.append(totalLosses);
            builder.append(" Losses, #k");
            builder.append(caught);
            builder.append(" Caught\r\n");
            this.toString = builder.toString(); //Rank 1 : Phoenix - 200 Wins, 0 Losses, 650 Caught
        }

        @Override
        public String toString() {
            return toString;
        }
    }

    public static class PokedexInformation {

        public String toString;

        public PokedexInformation(String name, int caught, int rank) {
            final StringBuilder builder = new StringBuilder("Rank ");
            builder.append(rank);
            builder.append(" : #e");
            builder.append(name);
            builder.append("#n - #r");
            builder.append(caught);
            builder.append(" Caught\r\n");
            this.toString = builder.toString(); //Rank 1 : Phoenix - 650 Caught
        }

        @Override
        public String toString() {
            return toString;
        }
    }

    public static class PokebattleInformation {

        public String toString;

        public PokebattleInformation(String name, int totalWins, int totalLosses, double caught, int rank) {
            final StringBuilder builder = new StringBuilder("Rank ");
            builder.append(rank);
            builder.append(" : #e");
            builder.append(name);
            builder.append("#n - #r");
            builder.append(caught);
            builder.append(" Ratio\r\n");
            this.toString = builder.toString(); //Rank 1 : Phoenix - 200 Wins, 0 Losses, 200 Ratio
        }

        @Override
        public String toString() {
            return toString;
        }
    }
}
