/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client.anticheat;

public enum ReportType {
    Hacking(0, "hack"),
    Botting(1, "bot"),
    Scamming(2, "scam"),
    FakeGM(3, "fake"),
    //Harassment(4, "harass"),
    Advertising(5, "ad");

    public byte i;
    public String theId;
    private ReportType(int i, String theId) {
	this.i = (byte)i;
	this.theId = theId;
    }

    public static ReportType getById(int z) {
	for (ReportType t : ReportType.values()) {
	    if (t.i == z) {
		return t;
	    }
	}
	return null;
    }

    public static ReportType getByString(String z) {
	for (ReportType t : ReportType.values()) {
	    if (z.contains(t.theId)) {
		return t;
	    }
	}
	return null;
    }
}
