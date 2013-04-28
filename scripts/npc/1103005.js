function action(mode, type, selection) {
    if (cm.getPlayer().getLevel() >= 10) {
        cm.warp(130000000, 0);
    } else {
        cm.warp(130030000, 0);
    }
    cm.dispose();
}