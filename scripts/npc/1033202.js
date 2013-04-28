var status = -1;

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (cm.isQuestActive(24000) || cm.isQuestFinished(24000)) {
		cm.sendNext("Please, save us.");
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendPlayerToNpc("Elders! You're okay! But... but the village...!");
	} else if (status == 1) {
		cm.sendNextNoESC("A fierce, frozen curse has fallen upon the town. Your Highness, I see it has fallen upon you as well.");
	} else if (status == 2) {
		cm.sendNextNoESC("I sense it from you most of all! Is this the power of the Black Mage?!", 1033203);
	} else if (status == 3) {
		cm.sendNextNoESC("The children are already trapped in ice, and soon, the adults will follow them. It takes longer to freeze the stronger Elves, which is why we are still all right, but our time is limited...", 1033204);
	} else if (status == 4) {
		cm.sendPlayerToNpc("This is my fault. We sealed the Black Mage, but he managed to #rcurse#k us anyway.");
	} else if (status == 5) {
		cm.sendNextNoESC("So it is his doing?!", 1033203);
	} else if (status == 6) {
		cm.sendNextNoESC("I knew this was his doing...", 1033204);
	} else if (status == 7) {
		cm.sendNextNoESC("The Black Mage has cursed our sovereign, and the curse has spread to all Elves...");
	} else if (status == 8) {
		cm.sendPlayerToNpc("I should have been more careful. Please, I didn't mean for this to happen...");
	} else if (status == 9) {
		cm.sendNextNoESC("What a fearful being, this Black Mage. Even from beyond the seal, he wields such power... It is a miracle we were able to seal him at all.");
	} else if (status == 10) {
		cm.sendNextNoESC("There was no way you could stop this, Your Majesty. Nobody could have.", 1033204);
	} else if (status == 11) {
		cm.sendNextNoESC("That's right! It's not your fault, My Liege! You sealed him! YOU'RE the hero!", 1033203);
	} else if (status == 12) {
		cm.sendPlayerToNpc("I shouldn't have fought the Black Mage in the first place! If I'd let him be, this wouldn't have happened to the Elves. I've failed my people!");
	} else if (status == 13) {
		cm.sendNextNoESC("Don't say such things, Your Highness! Even if you'd let him be, the Black Mage would have come for us sooner or later.", 1033204);
	} else if (status == 14) {
		cm.sendNextNoESC("It's our fault. We are your council. We should have better prepared you to face the Black Mage.");
	} else if (status == 15) {
		cm.sendNextNoESC("I'm supposed to be the Elder of War, but even I was too weak to help join the fight. I'm the one who failed you, Your Highness...", 1033203);
	} else if (status == 16) {
		cm.sendPlayerToNpc("No, this isn't your fault! I'm the one who decided to face the Black Mage. I don't regret fighting... I regret failing to protect my people.");
	} else if (status == 17) {
		cm.sendNextNoESC("In that case, we all regret failing to do this, Your Majesty.");
	} else if (status == 18) {
		cm.sendNextNoESC("This is not your burden alone. The decision to fight the Black Mage was the decision of the Elves, and so we will all share in the results, whatever they may be.", 1033204);
	} else if (status == 19) {
		cm.sendNextNoESC("No one blames you, Your Highness!", 1033203);
	} else if (status == 20) {
		cm.sendPlayerToNpc("Everyone...");
	} else if (status == 21) {
		cm.sendNextNoESC("Regardless of this wicked curse, we will survive. We will overcome this together.");
	} else if (status == 22) {
		cm.sendNextNoESC("Long as Your Highness is safe, the hope for the Elves lives on.");
	} else if (status == 23) {
		cm.sendPlayerToNpc("Is there a way?");
	} else if (status == 24) {
		cm.sendNextNoESC("We can't stop the curse now. But we are the Elves. We may outlive it.");
	} else if (status == 25) {
		cm.sendNextNoESC("Your Highness, we should seal Elluel before the curse spread beyond the village. We cannot avoid it, but we can keep it from spreading beyond the Elves. #bWe Elves will all slumber here, undisturbed by the outside world.#k");
	} else if (status == 26) {
		cm.sendNextNoESC("We don't know how long the curse will last, but time is on our side. Your Highness, we've nothing to worry about.", 1033204);
	} else if (status == 27) {
		cm.sendNextNoESC("Eventually we will awaken together, and the Black Mage will be a distant memory!", 1033203);
	} else if (status == 28) {
		cm.sendNextNoESC("Not even the curse of the Black Mage can last forever. In the end, we will be the victors.");
	} else if (status == 29) {
		cm.sendPlayerToNpc("Yes! We will win!");
	} else if (status == 30) {
		cm.sendNextNoESC("Of course we will. Ah... I'm growing weak. Your Highness, it is time to seal the village. It is the only way we can rest in peace.");
	} else if (status == 31) {
		cm.sendNextNoESC("There are some things we should take care of first. I believe #bAstilda#k wants to speak with you.");
	} else if (status == 32) {
		cm.forceStartQuest(24007, "1");
		cm.dispose();
	}
}