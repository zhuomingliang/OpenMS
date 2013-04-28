var status = -1;
var imburse = null;

var normal = Array(4001168, 5640000, 5062001, 2340000, 2049100, 5062000, 2022179, 5050000, 2049301, 2049401, 2049400, 2049300, 2049116, 5610000, 5610001, 5520000, 5520001, 5060003, 4170023);
var normal_value = Array(1000000, 320000, 200000, 200000, 20000, 10000, 10000, 8000, 7000, 7000, 13000, 1000000, 2000000, 20000, 30000, 36000, 50000, 40000, 40000);
//ADDING EQUIPS? ADD TO IF STATEMENT AT BOTTOM! lolwtf
var donor = Array(1442057, 3993003, 1122121, 5220013);
var donor_value = Array(3000000, 1500000, 1500000, 150000);

var needed = Array(1112445, 4001168, 1112513, 1112628, 1022097);

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == 0) {
	    cm.dispose();
	    return;
	}
	status++;
	if (status == 0) {
	    cm.sendNext("I am the compensation NPC. If you have any pending reimbursements or items waiting for you from the staff, I can distribute it to you.");
	} else if (status == 1) {
	    imburse = cm.getCompensation();
	    if (imburse == null || imburse.left <= 0 || imburse.mid > 0) {
		cm.sendOk("You have no pending reimbursements.");
		cm.safeDispose();
	    } else if (!cm.canHoldSlots(10)) {
		cm.sendOk("Please make at least 10 inventory space in ALL of your inventories.");
		cm.safeDispose();
	    } else {
		cm.sendYesNo("The database specifies that you have a reimbursement waiting! Would you like to redeem it now?");
	    }
	} else if (status == 2) {
	    var ourValue = imburse.left * 1000000; //billions!
	    var items = Array();
	    var values = Array();
	    for (var i = 0; i < normal.length; i++) {
		if (normal_value[i] <= ourValue) {
		    items.push(normal[i]);
		    values.push(normal_value[i]);
		}
	    }
	    if (imburse.right > 0) {
	        for (var i = 0; i < donor.length; i++) {
		    if (donor_value[i] <= ourValue) {
		        items.push(donor[i]);
		        values.push(donor_value[i]);
		    }
	        }
	    }
	    //we have to make this right in one try
	    for (var i = 0; i < needed.length; i++) {
		if (!cm.haveItem(needed[i], 1, true, true)) {
		    cm.gainItem(needed[i], 1);
		}
	    }
	    cm.deleteCompensation(1); //gg
	    while (cm.canHold() && ourValue >= 1000000 && items.length > 0) {
		var rand_item = java.lang.Math.floor(java.lang.Math.random() * items.length) | 0;
		if (values[rand_item] > ourValue) {
		    if (ourValue < 1000000) {
			break;
		    } else {
		        continue;
		    }
		}
		if ((items[rand_item] == 1442057 || items[rand_item] == 1122121) && cm.haveItem(items[rand_item], 1)) {
		    continue;
		}
		ourValue -= values[rand_item];
		cm.gainItem(items[rand_item], 1);
	    }
	    if (ourValue >= 1000000) {
		cm.gainItem(4001168, (ourValue / 1000000) | 0);
	    }
	    var medal = getMedal();
	    if (medal > 0) {
		cm.gainItem(medal, 1);
	    }
	    cm.gainMeso(((ourValue % 1000000) * 1000) | 0);
	    cm.sendOk("You have been reimbursed. Hope you enjoy~");
	    cm.safeDispose();
	}
}

function getMedal() {
	var highestLevel = 0;
	var highestStat = 0;
	var currentMedal = 0;
	var iter = cm.getPlayer().getCompletedMedals();
	for (var i = 0; i < iter.size(); i++) {
	    var medal = cm.getQuest(iter.get(i)).getMedalItem();
	    if (!cm.haveItem(medal, 1, true, true) && (cm.getReqLevel(medal) > highestLevel || (cm.getReqLevel(medal) == highestLevel && cm.getTotalStat(medal) >= highestStat))) {
		currentMedal = medal;
		highestLevel = cm.getReqLevel(medal);
		highestStat = cm.getTotalStat(medal);
	    }
	}
	return currentMedal;
}