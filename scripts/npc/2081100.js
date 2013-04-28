/*  NPC : Harmonia
	Warrior 4th job advancement
	Forest of the priest (240010501)
*/

var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;

    if (status == 0) {
	if (!(cm.getJob() == 111 || cm.getJob() == 121 || cm.getJob() == 131 || cm.getJob() == 2111)) {
	    cm.sendOk("Why do you want to see me? There is nothing you want to ask me.");
	    cm.dispose();
	    return;
	} else if (cm.getPlayerStat("LVL") < 120) {
	    cm.sendOk("You're still weak to go to warrior extreme road. If you get stronger, come back to me.");
	    cm.dispose();
	    return;
	} else {
	    if (cm.getQuestStatus(6904) == 2 || cm.getJob() == 2111) {
		if (cm.getJob() == 111)
		    cm.sendSimple("You're qualified to be a true warrior. \r\nDo you want job advancement?\r\n#b#L0# I want to advance to Hero.#l\r\n#b#L1#  Let me think for a while.#l");
		else if (cm.getJob() == 121)
		    cm.sendSimple("You're qualified to be a true warrior. \r\nDo you want job advancement?\r\n#b#L0# I want to advance to Paladin.#l\r\n#b#L1#  Let me think for a while.#l");
		else if (cm.getJob() == 131)
		    cm.sendSimple("You're qualified to be a true warrior. \r\nDo you want job advancement?\r\n#b#L0# I want to advance to Dark Knight.#l\r\n#b#L1#  Let me think for a while.#l");
		else {
		    if (cm.haveItem(4031348)) {
		        cm.sendSimple("You're qualified to be a true warrior. \r\nDo you want job advancement?\r\n#b#L0# I want to advance to Aran.#l\r\n#b#L1#  Let me think for a while.#l");
		    } else {
			cm.sendNext("You need the Secret Scroll for 10 million meso.");
			cm.dispose();
			return;
		    }
		}
	    } else {
		cm.sendOk("You're not ready to make 4th job advancement. When you're ready, talk to me.");
		cm.dispose();
		return;
	    }
	}
    } else if (status == 1) {
	if (selection == 1) {
	    cm.sendOk("You don't have to hesitate to be the best Warrior..Whenever you make your decision, talk to me. If you're ready, I'll let you make the 4th job advancement.");
	    cm.dispose();
	    return;
	}
	if (cm.getPlayerStat("RSP") > cm.getPlayerStat("LVL") * 3) { //player have too much SP means they havent assigned to their skills
	    if (cm.getPlayer().getAllSkillLevels() > ((cm.getPlayerStat("LVL") - 9) * 3)) { //player used too much SP means they have assigned to their skills.. conflict
		cm.sendOk("It appears that you have a great number of SP yet you have used enough SP on your skills already. Your SP has been reset. #ePlease talk to me again to make the job advancement.#n");
		cm.getPlayer().resetSP((cm.getPlayerStat("LVL") - 120) * 3);
	    } else {
	    	cm.sendOk("Hmm...You have too many #bSP#k. You can't make the 4th job advancement with too many SP left.");
	    }
	    cm.dispose();
	    return;
	} else {
		if (cm.getJob() == 111) {
		    cm.changeJob(112);
		    cm.sendNext("You have become the best of warriors, my #bHero#k.You will gain the #bRush#k Skill which makes you attack mutiple enemies and give you indomitable will along with #bStance#k and #bAchilles#k");
		} else if (cm.getJob() == 121) {
		    cm.changeJob(122);
		    cm.sendNext("You have become the best of warriors, my #bPaladint#k.You will gain the #bRush#k Skill which makes you attack mutiple enemies and give you indomitable will along with #bStance#k and #bAchilles#k");
		} else if (cm.getJob() == 131) {
		    cm.changeJob(132);
		    cm.sendNext("You have become the best of warriors, my #bDark Knight#k.You will gain the #bRush#k Skill which makes you attack mutiple enemies and give you indomitable with along with #bStance#k and #bAchilles#k.");
		} else {
		    cm.gainItem(4031348, -1);
		    cm.changeJob(2112);
		if (cm.canHold(1142132,1)) {
		    cm.forceCompleteQuest(29927);
		    cm.gainItem(1142132,1); //temp fix
		}
		    cm.sendNext("You have become the best of warriors, my #bAran#k.You will gain the #bOverswing#k Skill which makes you attack mutiple enemies and give you indomitable with along with #bAggression#k and #bFreezing Posture#k.");
		}
	}
    } else if (status == 2) {
	cm.sendNextPrev("Don't forget that it all depends on how much you train.");
	cm.dispose();
    }
}