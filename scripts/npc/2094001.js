var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	cm.removeAll(4001117);
	cm.removeAll(4001120);
	cm.removeAll(4001121);
	cm.removeAll(4001122);
	cm.sendSimple("#b#L0#Get me out of here.#l\r\n#L1#Give me Pirate Hat.#l#k");
    } else if (status == 1) {
	if (selection == 0) {
		if (!cm.canHold(4001455, 1)) {
			cm.sendOk("Please make room in ETC.");
			cm.dispose();
			return;
		}
		cm.gainItem(4001455, 1);
	    cm.gainNX(1000);
	    cm.addTrait("will", 15);
	    cm.gainExp_PQ(120, 2.0);
	    cm.getPlayer().endPartyQuest(1204);
	    cm.warp(251010404,0);
	} else { //TODO JUMP
		if (cm.haveItem(cm.isGMS() ? 1003267 : 1002573, 1)) {
			cm.sendOk("You have the best hat.");
		} else if (cm.haveItem(1002573, 1)) {
		    if (cm.haveItem(4001455, 20)) {	
				if (cm.canHold(1003267,1)) {
					cm.gainItem(1002573, -1);
					cm.gainItem(4001455, -20);
		    	    cm.gainItem(1003267,1);
					cm.sendOk("I have given you the hat.");
		    	} else {
					cm.sendOk("Please make room.");
		        } 
		    } else {
				cm.sendOk("You need 20 Pirate PQ to get the next hat.");
		    }
		} else if (cm.haveItem(1002572, 1)) {
		    if (cm.haveItem(4001455, 20)) {	
				if (cm.canHold(1002573,1)) {
					cm.gainItem(1002572, -1);
					cm.gainItem(4001455, -20);
		    	    cm.gainItem(1002573,1);
					cm.sendOk("I have given you the hat.");
		    	} else {
					cm.sendOk("Please make room.");
		        } 
		    } else {
				cm.sendOk("You need 20 Pirate PQ to get the next hat.");
		    }
		} else {
		    if (cm.haveItem(4001455, 20)) {	
				if (cm.canHold(1002572,1)) {
					cm.gainItem(4001455, -20);
		    	    cm.gainItem(1002572,1);
					cm.sendOk("I have given you the hat.");
		    	} else {
					cm.sendOk("Please make room.");
		        } 
		    } else {
				cm.sendOk("You need 20 Pirate PQ to get the next hat.");
		    }
		}
	}
	cm.dispose();
    }
}