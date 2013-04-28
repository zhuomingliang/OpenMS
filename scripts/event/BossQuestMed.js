var eventmapid = 689013010;
var returnmap = 689010000;
var monster = Array(9500327,9500328,9500329,9500330,9500331,9500354,0, 4);

function init() {
// After loading, ChannelServer
}

function setup(partyid) {
    var instanceName = "BossQuest" + partyid;

    var eim = em.newInstance(instanceName);
    // If there are more than 1 map for this, you'll need to do mapid + instancename
    var map = eim.createInstanceMapS(eventmapid);
    map.toggleDrops();
    map.spawnNpc(9250156, new java.awt.Point(258, 50));

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
			monsterid = 2500360;
			eim.setProperty("n_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("n_spawn")) == 1) {
			monsterid = 2500700;
			eim.setProperty("n_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("n_spawn")) == 2) {
			monsterid = 2500701;
			eim.setProperty("n_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("n_spawn")) == 3) {
			monsterid = 2500702;
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
		if (parseInt(eim.getProperty("f_spawn")) == 0) {
			monsterid = 9400409;
			eim.setProperty("f_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 1) {
			monsterid = 9400406;
			eim.setProperty("f_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 2) {
			monsterid = 9400406;
			eim.setProperty("f_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 3) {
			monsterid = 9400406;
			eim.setProperty("f_spawn", "4");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 4) {
			monsterid = 9400409;
		}
        } else if (monsterid == 3) {
		if (parseInt(eim.getProperty("f_spawn")) == 0) {
			monsterid = 8840000;
			eim.setProperty("f_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 1) {
			monsterid = 8840001;
			eim.setProperty("f_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 2) {
			monsterid = 8840002;
			eim.setProperty("f_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 3) {
			monsterid = 8840003;
			eim.setProperty("f_spawn", "4");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 4) {
			monsterid = 8840000;
		}
        } else if (monsterid == 3) {
		if (parseInt(eim.getProperty("f_spawn")) == 0) {
			monsterid = 8870100;
			eim.setProperty("f_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 1) {
			monsterid = 8870104;
			eim.setProperty("f_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 2) {
			monsterid = 8870104;
			eim.setProperty("f_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 3) {
			monsterid = 8870103;
			eim.setProperty("f_spawn", "4");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 4) {
			monsterid = 8870103;
		}
	} else if (monsterid == 4) {
		if (parseInt(eim.getProperty("c_spawn")) == 0) {
			monsterid = 9420062; //stronger versions
			eim.setProperty("c_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 1) {
			monsterid = 9420040;
			eim.setProperty("c_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 2) {
			monsterid = 9420041;
			eim.setProperty("c_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 3) {
			monsterid = 9420051;
			eim.setProperty("c_spawn", "4");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 4) {
			monsterid = 9420054;
		}
	}
    var mob = em.getMonster(monsterid);
var modified = em.newMonsterStats();
modified.setOMp(mob.getMobMaxMp());
    switch (monsterid) {
	case 8870100:
	    modified.setOExp(mob.getMobExp() * 1.8);
	    modified.setOHp(mob.getMobMaxHp() *1); 
	    break;
	case 2500843:
	    modified.setOExp(mob.getMobExp() * 2);
	    modified.setOHp(mob.getMobMaxHp() * 8.2);
	    break;
	case 9400300:
	    modified.setOExp(mob.getMobExp() * 1.5); 
	    modified.setOHp(mob.getMobMaxHp() * 8); 
	    break;
	case 9400589:
	case 9400590:
	case 9400591:
	case 9400592:
	case 8840000:
	    modified.setOExp(mob.getMobExp() * 1.1);
	    modified.setOHp(mob.getMobMaxHp() * 1.2); 
	    break;
	case 8850005:
	case 8850006:
	case 8850007:
	case 8850008:
	case 8850009:
	    modified.setOExp(mob.getMobExp() * 1.1);
	    modified.setOHp(mob.getMobMaxHp() * 2.3);
	    break;
	case 9420040:
	case 9420041:
	case 9420042:
	case 9420051:
	case 9420054:
	    modified.setOExp(mob.getMobExp() * 1.1);
	    modified.setOHp(mob.getMobMaxHp() * 4.9); 
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
    var num = mobnum * 35;
    var totalp = parseInt(eim.getProperty("points")) + num;

    eim.setProperty("points", totalp);

    eim.broadcastPlayerMsg(5, "Your team've gained "+num+" points! With a total of "+totalp+".");
    
    eim.saveBossQuest(num);

    if (mobnum < monster.length) {
	eim.broadcastPlayerMsg(6, "Prepare! The next boss will appear in a glimpse of an eye!");
    } else {
	eim.saveBossQuest(1000);
	eim.saveNX(2000);
	eim.broadcastPlayerMsg(5, "Your team've beaten the Normal mode and have gained an extra 1,000 points and extra 1,000 Cash!");
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