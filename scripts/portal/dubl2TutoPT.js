function enter(pi) {

    if (pi.getQuestStatus(2600) == 0) {

	pi.playerMessage(5, "You can only exit after you accept the quest.");

    } else {
	pi.playPortalSE();

	pi.warp(103050910, 0);

    }

}