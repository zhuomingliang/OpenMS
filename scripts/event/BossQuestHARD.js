var eventmapid = 689013040;
var returnmap = 689010000;
var monster = Array(8300006,8300007,8220005,8220006,9400121,9400405,9420549,9420544,9500392,9420059);

function init() {
// After loading, ChannelServer
}

function setup(partyid) {
    var instanceName = "BossQuest" + partyid;

    var eim = em.newInstance(instanceName);
    // If there are more than 1 map for this, you'll need to do mapid + instancename
    var map = eim.createInstanceMapS(eventmapid);
    map.toggleDrops();
    map.spawnNpc(9250156, new java.awt.Point(248, 51));

    eim.setProperty("points", 0);
    eim.setProperty("monster_number", 0);
   eim.setProperty("n_spawn", 0);
   eim.setProperty("f_spawn", 0);
   eim.setProperty("c_spawn", 0);
    beginQuest(eim);
    return eim;
}

function beginQuest(eim) { // Custom function
    if (eim != null) {
    	eim.startEventTimer(5000); // After 5 seconds -> scheduledTimeout()
    }
}

function monsterSpawn(eim) { // Custom function
    var monsterid = monster[parseInt(eim.getProperty("monster_number"))];
	if (monsterid == 0) {
		if (parseInt(eim.getProperty("n_spawn")) == 0) {
			monsterid = 8870100;
			eim.setProperty("n_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("n_spawn")) == 1) {
			monsterid = 8870103;
			eim.setProperty("n_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("n_spawn")) == 2) {
			monsterid = 8870104;
			eim.setProperty("n_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("n_spawn")) == 3) {
			monsterid = 8870104;
			eim.setProperty("n_spawn", "4");
		}
	} else if (monsterid == 1) {
		if (parseInt(eim.getProperty("f_spawn")) == 0) {
			monsterid = 9400590;
			eim.setProperty("f_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 1) {
			monsterid = 9400591;
			eim.setProperty("f_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 2) {
			monsterid = 9400592;
			eim.setProperty("f_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 3) {
			monsterid = 9400593;
			eim.setProperty("f_spawn", "4");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 4) {
			monsterid = 9400589;
		}
	} else if (monsterid == 2) {
		if (parseInt(eim.getProperty("c_spawn")) == 0) {
			monsterid = 8850005; //stronger versions
			eim.setProperty("c_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 1) {
			monsterid = 8850006;
			eim.setProperty("c_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 2) {
			monsterid = 8850007;
			eim.setProperty("c_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 3) {
			monsterid = 8850008;
			eim.setProperty("c_spawn", "4");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 4) {
			monsterid = 8850009;
		}
	}
    var mob = em.getMonster(monsterid);
var modified = em.newMonsterStats();
modified.setOMp(mob.getMobMaxMp());
    switch (monsterid) {
	case 8300006:
	case 8300007:
	    modified.setOExp(mob.getMobExp() * 1.8);
	    modified.setOHp(mob.getMobMaxHp() * 2); 
	    break;
	case 8220005:
	case 8220006:
	case 9400121:
	case 9400405:
	    modified.setOExp(mob.getMobExp() * 2);
	    modified.setOHp(mob.getMobMaxHp() * 2.2); 
	    break;
	case 9420544:
	case 9420059:
	case 9500392:
	    modified.setOExp(mob.getMobExp() * 1.5); //goes stack overflow over 2.1b if too high
	    modified.setOHp(mob.getMobMaxHp() * 4); //goes stack overflow over 1.6b if too high
	    break;
	case 9400589:
	case 9400590:
	case 9400591:
	case 9400592:
	case 9400593:
	    modified.setOExp(mob.getMobExp() * 1.1);
	    modified.setOHp(mob.getMobMaxHp() * 1.2); //1.4b total
	    break;
	case 8850005:
	case 8850006:
	case 8850007:
	case 8850008:
	case 8850009:
	    modified.setOExp(mob.getMobExp() * 1.1);
	    modified.setOHp(mob.getMobMaxHp() * 2.3); //1.4b total
	    break;
	case 8870103:
	case 8870104:
	    modified.setOExp(mob.getMobExp() * 1.1);
	    modified.setOHp(mob.getMobMaxHp() * 5.9); //goes stack overflow over 2.1b if too high
 	    break;
    }
	mob.setOverrideStats(modified);
    eim.registerMonster(mob);

    var map = eim.getMapInstance(0);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(0, 276));
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function changedMap(eim, player, mapid) {
    if (mapid != eventmapid) {
	eim.unregisterPlayer(player);

	eim.disposeIfPlayerBelow(0, 0);
    }
}

function scheduledTimeout(eim) {
    var num = parseInt(eim.getProperty("monster_number"));
    if (num < monster.length) {
	monsterSpawn(eim);
	eim.setProperty("monster_number", num + 1);
    } else {
	eim.disposeIfPlayerBelow(100, returnmap);
    }
// When event timeout..

// restartEventTimer(long time)
// stopEventTimer()
// startEventTimer(long time)
// isTimerStarted()
}

function allMonstersDead(eim) {
    eim.restartEventTimer(3000);

    var mobnum = parseInt(eim.getProperty("monster_number"));
    var num = mobnum * 85;
    var totalp = parseInt(eim.getProperty("points")) + num;

    eim.setProperty("points", totalp);

    eim.broadcastPlayerMsg(5, "Your team've gained "+num+" points! With a total of "+totalp+".");
    
    eim.saveBossQuest(num);

    if (mobnum < monster.length) {
	eim.broadcastPlayerMsg(6, "Prepare! The next boss will appear in a glimpse of an eye!");
    } else {
	eim.saveBossQuest(2500);
	eim.saveNX(5000);
	eim.broadcastPlayerMsg(5, "Your team've beaten the HARD mode and have gained an extra 2,500 points and extra 2,500 Cash!");
	eim.giveAchievement(0);
	}
// When invoking unregisterMonster(MapleMonster mob) OR killed
// Happens only when size = 0
}

function playerDead(eim, player) {
// Happens when player dies
}

function playerRevive(eim, player) {
    return true;
// Happens when player's revived.
// @Param : returns true/false
}

function playerDisconnected(eim, player) {
    return 0;
// return 0 - Deregister player normally + Dispose instance if there are zero player left
// return x that is > 0 - Deregister player normally + Dispose instance if there x player or below
// return x that is < 0 - Deregister player normally + Dispose instance if there x player or below, if it's leader = boot all
}

function monsterValue(eim, mobid) {
    if (mobid == 8820002 || mobid == 8820015 || mobid == 8820016 || mobid == 8820017 || mobid == 8820018) { //ariel
	eim.getMapInstance(0).killMonster(8820019);
    }
    return 0;
// Invoked when a monster that's registered has been killed
// return x amount for this player - "Saved Points"
}

function leftParty(eim, player) {
    // Happens when a player left the party
    eim.unregisterPlayer(player);

    var map = em.getMapFactory().getMap(returnmap);
    player.changeMap(map, map.getPortal(0));

    eim.disposeIfPlayerBelow(0, 0);
}

function disbandParty(eim, player) {
    // Boot whole party and end
    eim.disposeIfPlayerBelow(100, returnmap);
}

function clearPQ(eim) {
// Happens when the function EventInstanceManager.finishPQ() is invoked by NPC/Reactor script
}

function removePlayer(eim, player) {
    eim.dispose();
// Happens when the funtion NPCConversationalManager.removePlayerFromInstance() is invoked
}

function registerCarnivalParty(eim, carnivalparty) {
// Happens when carnival PQ is started. - Unused for now.
}

function onMapLoad(eim, player) {
// Happens when player change map - Unused for now.
}

function cancelSchedule() {
}