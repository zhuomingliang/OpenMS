function start() {
    if (cm.getPlayerStat("LVL") <= 10) {
	cm.dispose();
	return;
    }
/* TODO JUMP
98 = astaroth
99 = egg leafre
520 = thailand floating
*/
    if (cm.isGMS()) {
	cm.askMapSelection("#1# Mu Lung Training Center#2# Secret BossPQ 1#3# Monster Carnival 2#4# Dual Raid#5# Nett's Pyramid#6# Kerning Subway#7# Happyville#8# Golden Temple#9# Moon Bunny#10# First Time Together#11# Dimensional Crack #12# Forest of Poison Haze #13# Remnant of the Goddess #14# Lord Pirate #15# Romeo and Juliet #16# Resurrection of the Hoblin King #17# Dragon's Nest #98# Astaroth #21# Kenta in Danger #22# Escape #23# Ice Knight Curse");
    } else {
        cm.askMapSelection("#1# Mu Lung Training Center#2# Secret BossPQ 1#3# Monster Carnival 2#4# Dual Raid#5# Nett's Pyramid#6# Kerning Subway#7# Happyville#8# Golden Temple#9# Moon Bunny#10# First Time Together#11# Dimensional Crack #12# Forest of Poison Haze #13# Remnant of the Goddess #14# Lord Pirate #15# Romeo and Juliet #16# Resurrection of the Hoblin King #17# Dragon's Nest #19# Haunted Mansion #522# Crimsonwood Party Quest #523# Zipangu #524# New Leaf City #521# Event Map");
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
	switch (selection) {
	    case 1:
		if (cm.getPlayerStat("LVL") >= 25) {
			cm.saveReturnLocation("MULUNG_TC");
			cm.warp(925020000, 0);
		}
		break;
	    case 2:
		if (cm.getPlayerStat("LVL") >= 70) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(689010000, 0);
		}
		break;
	    case 600:
		if (cm.getPlayerStat("LVL") >= 51) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(980030000, 4);
		}
		break;
	    case 601:
		if (cm.getPlayerStat("LVL") >= 60) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(923020000, 0);
		}
		break;
	    case 602:
		if (cm.getPlayerStat("LVL") >= 40) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(926010000, 4);
		}
		break;
	    case 603:
		if (cm.getPlayerStat("LVL") >= 25 && cm.getPlayerStat("LVL") <= 30) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(910320000, 2);
		}
		break;
	    case 604:
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(209000000, 0);
		    break;
	    case 8:
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(950100000, 9);
		    break;
	    case 605:
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(910010500, 0);
		break;
	    case 606:
		if (cm.getPlayerStat("LVL") >= 20) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(910340700, 0);
		}
		break;
	    case 607:
		if (cm.getPlayerStat("LVL") >= 30) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(221023300, 0);
		}
		break;
	    case 12:
		if (cm.getPlayerStat("LVL") >= 40) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(300030100, 0);
		}
		break;
	    case 13:
		if (cm.getPlayerStat("LVL") >= 50) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(200080101, 0);
		}
		break;
	    case 14:
		if (cm.getPlayerStat("LVL") >= 60) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(251010404, 0);
		}
		break;
	    case 15:
		if (cm.getPlayerStat("LVL") >= 70) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(261000021, 0);
		}
		break;
	    case 16:
		if (cm.getPlayerStat("LVL") >= 80) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(211000002, 0);
		}
		break;
	    case 17:
		if (cm.getPlayerStat("LVL") >= 100) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(240080000, 0);
		}
		break;
	    case 19:
		if (!cm.isGMS()) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(229010000, 0);
		}
		break;
	    case 21:
		if (cm.getPlayerStat("LVL") >= 120 && cm.isGMS()) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(923040000, 0);
		}
		break;
	    case 22:
		if (cm.getPlayerStat("LVL") >= 120 && cm.isGMS()) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(921160000, 0);
		}
		break;
	    case 22:
		if (cm.getPlayerStat("LVL") >= 70 && cm.isGMS()) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(689010000, 0);
		}
		break;
	    case 98:
		if (cm.getPlayerStat("LVL") >= 25 && cm.isGMS()) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(677000010,0);
		}
		break;
	    case 521:
		if (!cm.isGMS()) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(689010000, 0);
		}
		break;
	    case 522:
		if (!cm.isGMS()) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(610010000, 0);
			}
		break;
	    case 523:
		if (!cm.isGMS()) {
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(800000000, 0);
		}
		break;
	    case 22:
		if (!cm.isGMS()) { 
		    cm.saveReturnLocation("MULUNG_TC"); // h4x
		    cm.warp(689010000, 0);
		}
		break;
	}
    }
    cm.dispose();
}