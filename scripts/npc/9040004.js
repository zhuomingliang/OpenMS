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
	cm.sendSimple("#b#L0#Pokemon Rankings (by Wins)#l\r\n#L1#Pokemon Rankings (by Caught)#l\r\n#L2#Pokemon Rankings (by Ratio)#l\r\n");
    } else if (status == 1) {
	if (selection == 0) {
	    cm.sendNext(cm.getPokemonRanking());
	} else if (selection == 1) {
	    cm.sendNext(cm.getPokemonRanking_Caught());
	} else if (selection == 2) {
	    cm.sendNext(cm.getPokemonRanking_Ratio());
	}
	cm.dispose();
    }
}