/*var status = -1;
var firstSel = -1;
var slot = Array();

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
	cm.sendSimple("What do you want to change?\r\n#b#L0#Auto HP#l\r\n#L1#Auto MP#l");
    } else if (status == 1) {
	firstSel = selection;
	if (selection == 0) {
	    if (!cm.getPlayer().getStat().hasHP) {
		cm.sendOk("You have no Auto HP Pot.");
		cm.safeDispose();
	    } else {
		cm.sendGetNumber("Enter what % HP you want the Auto Potion to activate. (1-100)", 50, 1, 100);
	    }
	} else {
	    if (!cm.getPlayer().getStat().hasMP) {
		cm.sendOk("You have no Auto MP Pot.");
		cm.safeDispose();
	    } else {
		cm.sendGetNumber("Enter what % MP you want the Auto Potion to activate. (1-100)", 50, 1, 100);
	    }
	}
	
    } else if (status == 2) {
	if (firstSel == 0) {
	    if (selection >= 1 && selection <= 100) {
	        cm.getQuestRecord(122220).setCustomData(selection);
		var inv = cm.getInventory(2);
		var selStr = "";
		for (var i = 0; i <= inv.getSlotLimit(); i++) {
			slot.push(i);
			var it = inv.getItem(i);	
			if (it == null) {
			    continue;
			}
			var itt = cm.getEffect(it.getItemId());
			if (itt == null || itt.getHp() <= 0 || itt.getHpR() <= 0) {
			    continue;
			}
			selStr += "#L" + i + "##v" + itemid + "##t" + itemid + "##l";
		}
		if (selStr.length <= 0) {
		    cm.sendOk("The Percent has been set, but you have no items worth setting to the potion.");
		    cm.safeDispose();
		} else {
		    cm.sendSimple("The Percent has been set. Please choose what item to set as the Potion:\r\n" + selStr);
		}
	    } else {
		cm.sendOk("The number was invalid. (1-100)");
		cm.safeDispose();
	    }
	} else {
	    if (selection >= 1 && selection <= 100) {
	        cm.getQuestRecord(122222).setCustomData(selection);
		var inv = cm.getInventory(2);
		var selStr = "";
		for (var i = 0; i <= inv.getSlotLimit(); i++) {
			slot.push(i);
			var it = inv.getItem(i);	
			if (it == null) {
			    continue;
			}
			var itt = cm.getEffect(it.getItemId());
			if (itt == null || itt.getMp() <= 0 || itt.getMpR() <= 0) {
			    continue;
			}
			selStr += "#L" + i + "##v" + itemid + "##t" + itemid + "##l";
		}
		if (selStr.length <= 0) {
		    cm.sendOk("The Percent has been set, but you have no items worth setting to the potion.");
		    cm.safeDispose();
		} else {
		    cm.sendSimple("The Percent has been set. Please choose what item to set as the Potion:\r\n" + selStr);
		}
	    } else {
		cm.sendOk("The number was invalid. (1-100)");
		cm.safeDispose();
	    }
	}
    } else if (status == 3) {
	var inzz = cm.getInventory(2).getItem(slot[selection]);
	if (inzz == null) {
	    cm.sendOk("Error, please try again.");
	    cm.safeDispose();
	    return;
	}
	var itt = cm.getEffect(inzz.getItemId());
	if (firstSel == 0) {
	    if (itt == null || itt.getHp() <= 0 || itt.getHpR() <= 0) {
	        cm.sendOk("Error, please try again.");
	    } else {
	        cm.getQuestRecord(122221).setCustomData(selection);
		cm.getPlayer().updatePetAuto();
		cm.sendOk("#v" + inzz.getItemId() + "##t" + inz.getItemId() + "# has been set as the potion.");
	    }
	} else {
	    if (itt == null || itt.getMp() <= 0 || itt.getMpR() <= 0) {
	        cm.sendOk("Error, please try again.");
	    } else {
	        cm.getQuestRecord(122223).setCustomData(selection);
		cm.getPlayer().updatePetAuto();
		cm.sendOk("#v" + inzz.getItemId() + "##t" + inz.getItemId() + "# has been set as the potion.");
	    }
	}
	cm.safeDispose();
    }
}*/