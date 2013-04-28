/* Rene
	Bowman 3rd job advancement
	El Nath: Chief's Residence (211000001)

	Custom Quest 100100, 100102
*/

var status = 0;
var job;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 1) {
	cm.sendOk("Make up your mind and visit me again.");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (!(cm.getJob() == 310 || cm.getJob() == 320)) {
	    cm.sendOk("May the Gods be with you!");
	    cm.dispose();
	    return;
	}
	if ((cm.getJob() == 310 || cm.getJob() == 320) && cm.getPlayerStat("LVL") >= 70) {
	    if (cm.getPlayerStat("RSP") > (cm.getPlayerStat("LVL") - 70) * 3) {
	        if (cm.getPlayer().getAllSkillLevels() > cm.getPlayerStat("LVL") * 3) { //player used too much SP means they have assigned to their skills.. conflict
		    cm.sendOk("It appears that you have a great number of SP yet you have used enough SP on your skills already. Your SP has been reset. #ePlease talk to me again to make the job advancement.#n");
		    cm.getPlayer().resetSP((cm.getPlayerStat("LVL") - 70) * 3);
	        } else {
	    	    cm.sendOk("Hmm...You have too many #bSP#k. You can't make the job advancement with too many SP left.");
	        }
		cm.safeDispose();
	    } else {
	        cm.sendNext("You are indeed a strong one.");
	    }
	} else {
	    cm.sendOk("Please make sure that you are eligible for the job advancement. (level 70+)");
	    cm.safeDispose();
	}
    } else if (status == 1) {
	    if (cm.getPlayerStat("LVL") >= 70 && cm.getPlayerStat("RSP") <= (cm.getPlayerStat("LVL") - 70) * 3) {
	    if (cm.getJob() == 310) { // HUNTER
		cm.changeJob(311); // RANGER
		cm.sendOk("You are now a #bRanger#k.");
		cm.dispose();
	    } else if (cm.getJob() == 320) { // CROSSBOWMAN
		cm.changeJob(321); // SNIPER
		cm.sendOk("You are now a #bSniper#k.");
		cm.dispose();
	    }
	    } else {
		cm.sendOk("Come back when you are level 70 and used SP.");
		cm.dispose();
	    }
    }
}