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
package handling.channel.handler;

import java.util.List;
import java.awt.Point;

import client.Skill;
import constants.GameConstants;
import client.MapleCharacter;
import client.SkillFactory;
import server.MapleStatEffect;
import server.AutobanManager;
import tools.AttackPair;

public class AttackInfo {

    public int skill, charge, lastAttackTickCount;
    public List<AttackPair> allDamage;
    public Point position;
	public int display;
    public byte hits, targets, tbyte, speed, csstar, AOE, slot, unk;
    public boolean real = true;

    public final MapleStatEffect getAttackEffect(final MapleCharacter chr, int skillLevel, final Skill skill_) {
        if (GameConstants.isMulungSkill(skill) || GameConstants.isPyramidSkill(skill) || GameConstants.isInflationSkill(skill)) {
            skillLevel = 1;
        } else if (skillLevel <= 0) {
            return null;
        }
		int dd = ((display & 0x8000) != 0 ? (display - 0x8000) : display);
        if (GameConstants.isLinkedAranSkill(skill)) {
            final Skill skillLink = SkillFactory.getSkill(skill);
			
            if (dd > SkillFactory.Delay.magic6.i && dd != SkillFactory.Delay.shot.i && dd != SkillFactory.Delay.fist.i) {
                if (skillLink.getAnimation() == -1 || Math.abs(skillLink.getAnimation() - dd) > 0x10) {
					if (skillLink.getAnimation() == -1) {
						chr.dropMessage(5, "Please report this: animation for skill " + skillLink.getId() + " doesn't exist");
					} else {
						AutobanManager.getInstance().autoban(chr.getClient(), "No delay hack, SkillID : " + skillLink.getId() + ", animation: " + dd + ", expected: " + skillLink.getAnimation());
					}
                    return null;
                }
            }
            return skillLink.getEffect(skillLevel);
        } // i'm too lazy to calculate the new skill types =.=
        /*if (dd > SkillFactory.Delay.magic6.i && dd != SkillFactory.Delay.shot.i && dd != SkillFactory.Delay.fist.i) {
            if (skill_.getAnimation() == -1 || Math.abs(skill_.getAnimation() - dd) > 0x10) {
				if (skill_.getAnimation() == -1) {
					chr.dropMessage(5, "Please report this: animation for skill " + skill_.getId() + " doesn't exist");
				} else {
					AutobanManager.getInstance().autoban(chr.getClient(), "No delay hack, SkillID : " + skill_.getId() + ", animation: " + dd + ", expected: " + skill_.getAnimation());
				}
                return null;
            }
        }*/
        return skill_.getEffect(skillLevel);
    }
}
