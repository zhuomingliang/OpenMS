/*
This file is part of the ZeroFusion MapleStory Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>
ZeroFusion organized by "RMZero213" <RMZero213@hotmail.com>

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
package server.events;

import java.util.concurrent.ScheduledFuture;
import client.MapleCharacter;
import server.Randomizer;
import server.Timer.EventTimer;
import tools.packet.CField;
import server.maps.MapleMap;
import server.maps.SavedLocationType;

public class MapleOla extends MapleSurvival { //survival/ola so similar.
    private int[] stages = new int[3];
    //stg1 = ch00-ch04 = 5 ports
    //stg2 = ch00-ch07 = 8 ports
    //stg3 = ch00-ch15 = 16 ports

    public MapleOla(final int channel, final MapleEventType type) {
	super(channel,type);
    }

    @Override
    public void finished(final MapleCharacter chr) {
        givePrize(chr);
        chr.finishAchievement(21);
    }

    @Override
    public void reset() {
        super.reset();
        stages = new int[]{0, 0, 0};
    }

    @Override
    public void unreset() {
        super.unreset();
        stages = new int[]{Randomizer.nextInt(5), Randomizer.nextInt(8), Randomizer.nextInt(15)};
	if (stages[0] == 2) {
	    stages[0] = 3; //hack check; 2nd portal cant be access
	}
    }

    public boolean isCharCorrect(String portalName, int mapid) {
        final int st = stages[(mapid % 10) - 1];
        return portalName.equals("ch" + (st < 10 ? "0" : "") + st);
    }
}
