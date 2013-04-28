function start() {
	if (cm.getPlayer().getMapId() == 950101100) {
		cm.warp(950100000,0);
		cm.dispose();
		return;
	}
	if (cm.getPlayer().getMapId() == 809061100) {
		cm.warp(809060000,0);
		cm.dispose();
		return;
	}
    cm.sendYesNo("If you leave now, you'll have to start over. Are you sure you want to leave?");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(cm.getMapId() / 10000 == 80906 ? 809061100 : 950101100);
    }
    cm.dispose();
}