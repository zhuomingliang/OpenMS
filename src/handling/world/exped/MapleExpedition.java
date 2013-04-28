package handling.world.exped;

import handling.world.MapleParty;
import handling.world.World;
import java.util.ArrayList;
import java.util.List;

public class MapleExpedition {
    private List<Integer> parties;
    private ExpeditionType et;
    private int leaderId, id;

    public MapleExpedition(ExpeditionType ett, int leaderId, int id) {
	this.et = ett;
	this.id = id;
	this.leaderId = leaderId;
	this.parties = new ArrayList<Integer>(ett.maxParty);
    }

    public ExpeditionType getType() {
	return et;
    }

    public int getLeader() {
	return leaderId;
    }

    public List<Integer> getParties() {
	return parties;
    }

    public int getId() {
	return id;
    }

    public int getAllMembers() {
	int ret = 0;
	for (int i = 0; i < parties.size(); i++) {
	    MapleParty pp = World.Party.getParty(parties.get(i));
	    if (pp == null) {
		parties.remove(i);
	    } else {
		ret += pp.getMembers().size();
	    }
	}
	return ret;
    } 

    public int getFreeParty() {
	for (int i = 0; i < parties.size(); i++) {
	    MapleParty pp = World.Party.getParty(parties.get(i));
	    if (pp == null) {
		parties.remove(i);
	    } else if (pp.getMembers().size() < 6) {
		return pp.getId();
	    }
	}
	if (parties.size() < et.maxParty) {
	    return 0;
	}
	return -1;
    }

    public int getIndex(final int partyId) {
	for (int i = 0; i < parties.size(); i++) {
	    if (parties.get(i) == partyId) {
		return i;
	    }
	}
	return -1;
    }

    public void setLeader(int newLead) {
	this.leaderId = newLead;
    }
}