var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (cm.getQuestStatus(6410) != 1) {
	cm.sendNext("...");
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("Would you like to protect Deli?");
    } else if (status == 1) {
	cm.warp(925010000,0);
	cm.dispose();
    }
}