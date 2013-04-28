/*
	Violet Balloon - LudiPQ Crack on the Wall NPC
**/

var status;
			
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == -1 && cm.isLeader()) {
	var eim = cm.getEventInstance();

	if (eim.getProperty("crackLeaderPreamble") == null) {
	    eim.setProperty("crackLeaderPreamble", "done");
	    cm.sendNext("This is the final stage; it'll be a final test of your strength. Give the #rKey of Dimension#k that he drops to me, and you will have succeeded. Good luck!");
	    cm.dispose();
	} else {
	    if (cm.haveItem(4001023)) {
		status = 0;
		cm.sendNext("Congratulations! You have defeated the boss, #bAlishar#k. Would you like to go to the reward stage now?");
	    } else {
		cm.sendNext("Please bring me the #bKeys of Dimension#k by defeating #bAlishar#k.");
		cm.dispose();
	    }
	}
    } else if (status == -1 && !cm.isLeader()) {
	cm.sendNext("Get the leader of your party to hand the #rKey of Dimension#k that #bAlishar#k drops to me, and you will have succeeded. Good luck!");
	cm.dispose();
    } else if (status == 0 && cm.isLeader()) {
	var eim = cm.getEventInstance();
	clear(9,eim,cm);
	cm.gainItem(4001023,-1);

	var players = eim.getPlayers();
	cm.givePartyExp_PQ(70, 1.0, players);
	eim.setProperty("cleared", "true"); //set determine
	eim.restartEventTimer(60000);
	var bonusmap = cm.getMap(922011100);
	for (var i = 0; i < players.size(); i++) {
	    players.get(i).changeMap(bonusmap, bonusmap.getPortal(0));
	}
	cm.dispose();
    } else {
	cm.dispose();
    }
}

function clear(stage, eim) {
    eim.setProperty("stage" + stage.toString() + "status","clear");

    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
}