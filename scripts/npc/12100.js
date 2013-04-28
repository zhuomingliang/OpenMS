/*
	NPC Name: 		Mai
	Description: 		Quest - Job Advancement explorer
*/

var status = -1;
var sel = 0;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
        qm.sendNext("Hmm, you're making good progress with your leveling. Have you decided on which job you want to take? You could be a Warrior with great strength and high HP, a Magician with many spells, a Bowman that shoots arrows from afar, a Thief that uses quick, sneaky attacks, or a Pirate with all kinds of flashy chain skills... There are so many!");
    } else if (status == 1) {
        qm.sendSimple("If you go to Victoria Island, you can advance to the job of your choice by going to the right Job Instructor. But before that, lemme know which one you're interested in, and I'll send #bthem#k a letter of recommendation. That will make it easier for you to advance! So, which job will you choose?\r\n#b#L0#I want to be a mighty Warrior!#l\r\n#b#L1#I want to be a mystical Magician!#l\r\n#b#L2#I want to be a sharp-shooting Bowman!#l\r\n#b#L3#I want to be a sneaky Thief!#l\r\n#b#L4#I want to be a swashbuckling Pirate!#l");
    } else if (status == 2) {
        sel = selection;
	if (selection == 0) {
            qm.sendNext("A Warrior, huh? Boy, you're going to get really strong! They can take tons of damage, and dish plenty out, too. Okay, I'll send my recommendation to #bDances with Balrog#k, the Warrior Job Instructor.");
        } else if (selection == 1) {
        } else if (selection == 2) {
        } else if (selection == 3) {
        } else if (selection == 4) {
        }
    } else if (status == 3) {
        if (sel == 0) {
            qm.sendNextPrev("He will contact when you reach Lv. 10. Become a great Warrior!");
        } else if (sel == 1) {
        } else if (sel == 2) {
        } else if (sel == 3) {
        } else if (sel == 4) {
        }
    } else if (status == 4) {
        // Start the quest - talk to job instructor (1041 for warrior)
        qm.dispose();
    }
}