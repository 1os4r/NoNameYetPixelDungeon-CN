/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2019 Considered Hamster
 *
 * No Name Yet Pixel Dungeon
 * Copyright (C) 2018-2019 RavenWolf
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.ravenwolf.nnypdcn.actors.buffs.special.skills;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.wands.CharmOfBlink;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.CellSelector;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;

public class SmokeBomb extends BuffSkill {

    {
        CD = 75f;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }


    protected CellSelector.Listener striker = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {

            if (target != null && Ballistica.cast(Dungeon.hero.pos, target, false, false)==target) {

                //can't occupy the same cell as another char, so move back one.
                if (Actor.findChar( target ) != null && target != Dungeon.hero.pos)
                    target =  Ballistica.trace[ Ballistica.distance-2 ];

                Hero hero = Dungeon.hero;

                if (Actor.findChar(target) != null || Dungeon.level.solid[target] || !Level.fieldOfView[target]) {
                    GLog.w("你只能闪现到视野内的位置");
                }

                CellEmitter.get(hero.pos).burst(Speck.factory(Speck.WOOL), 4);
                //new Flare(5, 32, true, true,Flare.NO_ANGLE).color(0xFFFFFF, true).show(hero.pos, 2f);
                for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
                    if (Level.distance(hero.pos, mob.pos) <= 6 &&
                            mob.pos == Ballistica.cast(hero.pos, mob.pos, true, true)) {
                        BuffActive.add(mob, Blinded.class, 6f);
                    }
                }

                BuffActive.add(hero, Invisibility.class, 10f);
                Buff.detach(hero, Ensnared.class);
                hero.sprite.turnTo(hero.pos, target);
                CharmOfBlink.appear(hero, target);
                CellEmitter.get(hero.pos).burst(Speck.factory(Speck.WOOL), 2);

                SmokeBomb skill = Dungeon.hero.buff(SmokeBomb.class);
                if (skill != null)
                    skill.setCD(skill.getMaxCD());
                hero.busy();
                hero.spendAndNext(Actor.TICK);
            }

        }

        @Override
        public String prompt() {
            return "选择闪现的位置";
        }
    };
}
