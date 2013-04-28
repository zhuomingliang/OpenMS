/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
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
package client.inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.Randomizer;

public class PetDataFactory {

    private static MapleDataProvider dataRoot = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Item.wz"));
    private static Map<Integer, List<PetCommand>> petCommands = new HashMap<Integer, List<PetCommand>>();
    private static Map<Integer, Integer> petHunger = new HashMap<Integer, Integer>();

    public static final PetCommand getRandomPetCommand(final int petId) {
	if (getPetCommand(petId, 0) == null) {
	    return null;
	} //loaded, and checked if it existed
	final List<PetCommand> gg = petCommands.get(Integer.valueOf(petId));
	return gg.get(Randomizer.nextInt(gg.size()));
    }

    public static final PetCommand getPetCommand(final int petId, final int skillId) {
	List<PetCommand> gg = petCommands.get(Integer.valueOf(petId));
        if (gg != null) {
	    if (gg.size() > skillId && gg.size() > 0) {
                return gg.get(skillId);
	    }
	    return null;
        }
        final MapleData skillData = dataRoot.getData("Pet/" + petId + ".img");
	int theSkill = 0;
        gg = new ArrayList<PetCommand>();
	while (skillData != null) {
	    MapleData dd = skillData.getChildByPath("interact/" + theSkill);
	    if (dd == null) {
		break;
	    }
            PetCommand retr = new PetCommand(petId, skillId, MapleDataTool.getInt("prob", dd, 0), MapleDataTool.getInt("inc", dd, 0));
            gg.add(retr);
	    theSkill++;
	}
	petCommands.put(Integer.valueOf(petId), gg);
	if (gg.size() <= skillId && gg.size() > 0) {
            return gg.get(skillId);
	}
	return null;
    }

    public static final int getHunger(final int petId) {
        Integer ret = petHunger.get(Integer.valueOf(petId));
        if (ret != null) {
            return ret;
        }
        final MapleData hungerData = dataRoot.getData("Pet/" + petId + ".img").getChildByPath("info/hungry");
        ret = Integer.valueOf(MapleDataTool.getInt(hungerData, 1));
        petHunger.put(petId, ret);

        return ret;
    }
}
