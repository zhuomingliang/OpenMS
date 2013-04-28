/*
	Researcher Apears - Pink Zakum Raid (689013010)
*/

var status = -1;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	cm.sendYesNo("OAWK OAWK you can make your escape to a safer place through me. Noob? would you like to leave this place?");
    } else if (status == 1) {
	cm.warp(689010000);
	if (cm.getPlayerCount(689010000) == 0) {
		cm.getMap(689010000).resetReactors();
	}
	cm.dispose();
    } else {
	cm.dispose();
	}
}
