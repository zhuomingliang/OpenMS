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
    if (status == 0) {
	if (cm.getMap().getAllMonstersThreadsafe().size() <= 0) {
	    cm.sendOk("There are no monsters in this map.");
	    cm.dispose();
	    return;
	}
	var selStr = "Select which monster you wish to check.\r\n\r\n#b";
	var iz = cm.getMap().getAllUniqueMonsters().iterator();
	while (iz.hasNext()) {
	    var zz = iz.next();
	    selStr += "#L" + zz + "##o" + zz + "##l\r\n";
	} 
	cm.sendSimple(selStr);
    } else if (status == 1) {
	cm.sendNext(cm.checkDrop(selection));
	cm.dispose();
    }
}