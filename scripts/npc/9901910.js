var status = -1;


function start() {

    action(1, 0, 0);

}



function action(mode, type, selection) {
    if (mode == 0) {
	cm.dispose();
	return;
    } else {
	status++;
    }

    if (status == 0) {
	cm.sendNext("I am ready to test the video for Phantom.", 1);
    } else if (status == 1) {
	cm.MovieClipIntroUI(true);

	cm.PhantomVideo();
	cm.dispose();
    }
}
