/*
	Lakelis - Victoria Road: Kerning City (103000000)
**/

function start() {
    cm.removeAll(4001007);
    cm.removeAll(4001008);
    if (cm.getPlayer().getMapId() != 910340700) {
	cm.sendYesNo("Would you like to move to the Party Quest map?");
	return;
    }
    if (cm.getParty() == null) { // No Party
	cm.sendSimple("How about you and your party members collectively beating a quest? Here you'll find obstacles and problems where you won't be able to beat it without great teamwork.  If you want to try it, please tell the #bleader of your party#k to talk to me.#b\r\n#L0#I want the Fluffy Shoes.#l");
    } else if (!cm.isLeader()) { // Not Party Leader
	cm.sendSimple("If you want to try the quest, please tell the #bleader of your party#k to talk to me.#b\r\n#L0#I want the Fluffy Shoes.#l");
    } else {
	// Check if all party members are within Levels 21-30
	var party = cm.getParty().getMembers();
	var mapId = cm.getMapId();
	var next = true;
	var levelValid = 0;
	var inMap = 0;

	var it = party.iterator();
	while (it.hasNext()) {
	    var cPlayer = it.next();
	    if ((cPlayer.getLevel() >= 20 && cPlayer.getLevel() <= 255) || cPlayer.getJobId() == 900) {
		levelValid += 1;
	    } else {
		next = false;
	    }
	    if (cPlayer.getMapid() == mapId) {
		inMap += (cPlayer.getJobId() == 900 ? 4 : 1);
	    }
	}
	if (party.size() > 6 || inMap < 2) {
	    next = false;
	}
	if (next) {
	    var em = cm.getEventManager("KerningPQ");
	    if (em == null) {
		cm.sendSimple("This PQ is not currently available.#b\r\n#L0#I want the Fluffy Shoes.#l");
	    } else {
		var prop = em.getProperty("state");
		if (prop == null || prop.equals("0")) {
		    em.startInstance(cm.getParty(),cm.getMap(), 70);
			cm.dispose();
		} else {
		    cm.sendSimple("Someone is already attempting on the quest.#b\r\n#L0#I want the Fluffy Shoes.#l");
		}
		cm.removeAll(4001008);
		cm.removeAll(4001007);
	    }
	} else {
	    cm.sendSimple("Your party is not a party of two or more. Please make sure all your members are present and qualified to participate in this quest. I see #b" + levelValid.toString() + "#k members are in the right level range, and #b" + inMap.toString() + "#k are in Kerning. If this seems wrong, #blog out and log back in,#k or reform the party.#b\r\n#L0#I want the Fluffy Shoes.#l");
	}
    }
    
}

function action(mode, type, selection) {
    if (cm.getPlayer().getMapId() != 910340700) {
	    cm.saveLocation("MULUNG_TC");
	    cm.warp(910340700,0);
	} else {
	if (!cm.canHold(1072533,1)) {
	    cm.sendOk("Make room for these shoes.");
	} else if (cm.haveItem(4001531,10)) {
	    cm.gainItem(4001531,-10); //should handle automatically for "have"
	    cm.gainItem(1072533,1);
	} else {
	    cm.sendOk("Come back when you have 10 Smooshy Liquid.");
	}
	}
	    cm.dispose();
}