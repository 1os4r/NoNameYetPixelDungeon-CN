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
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.CellSelector;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.watabou.utils.Callback;

public class Fury extends BuffSkill {

    {
        CD = 75f;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }

    protected static CellSelector.Listener striker = new CellSelector.Listener() {

        private int count;
        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                Hero hero = Dungeon.hero;
                MeleeWeapon curWep = hero.belongings.weap1 instanceof MeleeWeapon ? (MeleeWeapon) hero.belongings.weap1 :
                        hero.belongings.weap2 instanceof MeleeWeapon ? (MeleeWeapon) hero.belongings.weap2 : null;

                if (curWep == null) {
                    GLog.i("你需要装备一个近战武器来使用此技能");
                    return;
                }
                hero.currentWeapon = curWep;
                final Char enemy = Actor.findChar(target);

                if (hero == enemy) {
                    GLog.i("你不能攻击自己");
                    return;
                }
                if (enemy != null && (Level.adjacent(enemy.pos, hero.pos) ||
                        Level.distance(enemy.pos, hero.pos) <= curWep.reach() && Ballistica.cast(hero.pos, enemy.pos, false, true)==enemy.pos)) {

                    Fury skill = hero.buff(Fury.class);
                    if (skill != null)
                        skill.setCD(skill.getMaxCD());

                    count = (int) (5 * curWep.speedFactor(hero));
                    ((HeroSprite) hero.sprite).attack(hero.currentWeapon, enemy.pos, new Callback() {
                        @Override
                        public void call() {
                            doAttack(enemy);
                        }
                    });

                } else
                    GLog.i("请选择一个有效的敌人");
            }
            GLog.i("请瞄准附近的敌人");

        }

        private void doAttack(final Char enemy){

            Hero hero = Dungeon.hero;

            int damage = hero.damageRoll();
            //weapon penalty reduce damage efficiency
            float damagePenalty =1- hero.currentWeapon.currentPenalty(hero,hero.currentWeapon.str())*0.025f;
            damage=(int)(damage*damagePenalty);
            count--;
            hero.hitEnemy(enemy, damage);
            CellEmitter.get(enemy.pos).start(Speck.factory(Speck.BLAST_FIRE, true), 0.05f, 2);

            if (enemy.isAlive() && count>0) {
                ((HeroSprite)hero.sprite).attack(hero.currentWeapon,enemy.pos, new Callback() {
                    @Override
                    public void call() {
                        doAttack(enemy);
                    }});

            }else {

                Dungeon.hero.sprite.idle();
                Dungeon.hero.spendAndNext(1f);
            }

        }

        @Override
        public String prompt() {
            return "选择要攻击的敌人";
        }
    };
}
