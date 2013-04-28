/**
	Tangyoon - Nautilus Cook
**/

function start() {
    if (cm.isQuestActive(2180)) {
	cm.warp(912000100);
    } else {
        cm.sendOk("The stable is off-limits to everyone else, and I'm afraid I can't let you go there as well.");
    }
    cm.dispose();
}