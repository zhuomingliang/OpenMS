/*
 * This file is part of the OdinMS MapleStory Private Server
 * Copyright (C) 2011 Patrick Huy and Matthias Butz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client;

import constants.GameConstants;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import server.CharacterCardFactory;
import tools.Pair;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;

/**
 *
 * @author AlphaEta
 */
public class MapleCharacterCards {

    private Map<Integer, CardData> cards = new LinkedHashMap<>(); // order
    private List<Pair<Integer, Integer>> skills = new LinkedList<>(); // id, level

    public final Map<Integer, CardData> getCards() {
        return cards;
    }

    public final void setCards(final Map<Integer, CardData> cads) {
        this.cards = cads;
    }

    public final List<Pair<Integer, Integer>> getCardEffects() {
        return skills;
    }

    public final void calculateEffects() {
        skills.clear(); // reset
        int deck1amount = 0, deck2amount = 0;
        int lowD1 = 0, lowD2 = 0;
        List<Integer> cardids1 = new LinkedList<>();
        List<Integer> cardids2 = new LinkedList<>();
        for (Entry<Integer, CardData> x : cards.entrySet()) {
            if (x.getValue().cid > 0) { // exist
                final Triple<Integer, Integer, Integer> skillData = CharacterCardFactory.getInstance().getCardSkill(x.getValue().job, x.getValue().level);
                if (x.getKey() < 4) {
                    if (skillData != null) {
                        cardids1.add(skillData.getLeft());
                        skills.add(new Pair<>(skillData.getMid(), skillData.getRight()));
                    }
                    deck1amount++;
                    if (lowD1 == 0 || lowD1 > x.getValue().level) {
                        lowD1 = x.getValue().level; // take lowest
                    }
                } else {
                    if (skillData != null) {
                        cardids2.add(skillData.getLeft());
                        skills.add(new Pair<>(skillData.getMid(), skillData.getRight()));
                    }
                    deck2amount++;
                    if (lowD2 == 0 || lowD2 > x.getValue().level) {
                        lowD2 = x.getValue().level; // take lowest
                    }
                }
            }
        }
        if (deck1amount == 3 && cardids1.size() == 3) {
            final List<Integer> uid = CharacterCardFactory.getInstance().getUniqueSkills(cardids1);
            for (final Integer ii : uid) {
                skills.add(new Pair<>(ii, GameConstants.getSkillLevel(lowD1))); // we can have more than 1 unique skills
            }
            skills.add(new Pair<>(CharacterCardFactory.getInstance().getRankSkill(lowD1), 1));
        }
        if (deck2amount == 3 && cardids2.size() == 3) {
            final List<Integer> uid = CharacterCardFactory.getInstance().getUniqueSkills(cardids2);
            for (final Integer ii : uid) {
                skills.add(new Pair<>(ii, GameConstants.getSkillLevel(lowD2)));
            }
            skills.add(new Pair<>(CharacterCardFactory.getInstance().getRankSkill(lowD2), 1));
        }
    }

    public final void recalcLocalStats(final MapleCharacter chr) {
        int pos = -1;
        for (Entry<Integer, CardData> x : cards.entrySet()) {
            if (x.getValue().cid == chr.getId()) {
                pos = x.getKey();
                break;
            }
        }
        if (pos != -1) {
            if (!CharacterCardFactory.getInstance().canHaveCard(chr.getLevel(), chr.getJob())) {
                cards.remove(pos); // we don't need to reset pos as its not needed
            } else {
                cards.put(pos, new CardData(chr.getId(), chr.getLevel(), chr.getJob())); // override old			
            }
        }
        calculateEffects(); // recalculate, just incase 
    }

    public final void loadCards(final MapleClient c, final boolean channelserver) throws SQLException {
        cards = CharacterCardFactory.getInstance().loadCharacterCards(c.getAccID(), c.getWorld());
        if (channelserver) {
            calculateEffects();
        }
    }

    public final void connectData(final MaplePacketLittleEndianWriter mplew) {
        if (cards.isEmpty()) { // we don't show for new characters 
            mplew.writeZeroBytes(54); // 9 x 6
            return;
        }
        int poss = 0;
        for (final CardData i : cards.values()) {
            poss++;
            if (poss > 6) {
                break;
            }
            mplew.writeInt(i.cid);
            mplew.write(i.level);
            mplew.writeInt(i.job);
        }
    }
}
