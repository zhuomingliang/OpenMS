function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
	cm.sendNext("Hi, I'm Mia.");
	cm.safeDispose();
}