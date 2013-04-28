function action(mode, type, selection) {
    if (cm.isQuestActive(2166)) {
	cm.forceCompleteQuest(2166);
	cm.sendOk("You felt the power of the stone.");
    }
    cm.dispose();
}