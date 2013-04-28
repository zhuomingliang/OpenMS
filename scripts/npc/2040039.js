/*
	Lime Balloon - LudiPQ 4th stage NPC
*/

var exp = 3360;

function action(mode, type, selection) {
    var eim = cm.getEventInstance();
    var stage4status = eim.getProperty("stage4status");

    if (stage4status == null) {
	if (cm.isLeader()) { // Leader
	    var stage4leader = eim.getProperty("stage4leader");
	    if (stage4leader == "done") {

		if (cm.getMap(922010401).getAllMonstersThreadsafe().size() == 0 && cm.getMap(922010402).getAllMonstersThreadsafe().size() == 0 && cm.getMap(922010403).getAllMonstersThreadsafe().size() == 0 && cm.getMap(922010404).getAllMonstersThreadsafe().size() == 0 && cm.getMap(922010405).getAllMonstersThreadsafe().size() == 0) { // Clear stage
		    cm.sendNext("Congratulations! You've passed the 2nd stage. Hurry on now, to the 3rd stage.");
		    cm.removeAll(4001022);
		    clear(4,eim,cm);
		    cm.givePartyExp(exp);
		} else { // Not done yet
		    cm.sendNext("Are you sure you've killed all the monsters? Please check again.");
		}
		cm.safeDispose();
	    } else {
		cm.sendOk("Welcome to the 2nd stage. Go around, and collect #rPasses of Dimension#k from the monsters in the dark maps. Once you're done, get your party members to hand all the #rPasses#k to you, then talk to me again.");
		eim.setProperty("stage4leader","done");
		cm.safeDispose();
	    }
	} else { // Members
	    cm.sendNext("Welcome to the 2nd stage. Go around, and collect #rPasses of Dimension#k from the monsters in the dark maps. Once you're done, hand all the #rPasses#k to your party leader.");
	    cm.safeDispose();
	}
    } else {
	cm.sendNext("Congratulations! You've passed the 2nd stage. Hurry on now, to the 3rd stage.");
	cm.safeDispose();
    }
}

function clear(stage, eim, cm) {
    eim.setProperty("stage" + stage.toString() + "status","clear");

    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
}