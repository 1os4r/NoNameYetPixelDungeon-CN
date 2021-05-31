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
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Stun;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.CellSelector;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.BlastWave;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class HeroicLeap extends BuffSkill {

    {
        CD = 100f;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }


    protected CellSelector.Listener striker = new CellSelector.Listener() {

        private GroundSlam weapSlam=new GroundSlam();
        @Override
        public void onSelect(Integer target) {

            if (target != null && Ballistica.cast(Dungeon.hero.pos, target, false, false)==target) {

                //can't occupy the same cell as another char, so move back one.
                if (Actor.findChar( target ) != null && target != Dungeon.hero.pos)
                    target =  Ballistica.trace[ Ballistica.distance-2 ];

                if (target != Dungeon.hero.pos) {
                    Buff.detach( Dungeon.hero, Ensnared.class );
                    HeroicLeap skill = Dungeon.hero.buff(HeroicLeap.class);
                    if (skill!=null)
                        skill.setCD(skill.getMaxCD());
                    final int dest = target;
                    Dungeon.hero.sprite.jump(Dungeon.hero.pos, dest, new Callback() {
                        @Override
                        public void call() {
                            Hero hero = Dungeon.hero;

                            hero.move(dest);
                            Dungeon.hero.currentWeapon = weapSlam;
                            int damage = Dungeon.hero.damageRoll();

                            Dungeon.level.press(dest, hero);
                            Dungeon.observe();
                            for (int n : Level.NEIGHBOURS8) {
                                Char enemy = Actor.findChar(dest + n);
                                if (enemy instanceof Mob) {

                                    int auxDmg=hero.hitEnemy(enemy,  damage);
                                    if(enemy.isAlive()) {
                                        BuffActive.addFromDamage(enemy, Stun.class, auxDmg);
                                        BuffActive.addFromDamage(enemy, Dazed.class, auxDmg * 3);
                                    }
                                }
                            }

                            CellEmitter.center(dest).burst(Speck.factory(Speck.DUST), 8);
                            Camera.main.shake(2, 0.5f);
                            Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, 0.8f );
                            BlastWave.createAtPos( dest );

                            Dungeon.hero.spendAndNext(1f);
                        }
                    });
                }
                GLog.i("你不能跳到那个位置");
                return;
            }
        }

        @Override
        public String prompt() {
            return "选择跳跃的位置";
        }
    };

    public static class GroundSlam extends MeleeWeapon {

        {
            name = "大地冲击";
        }

        public GroundSlam() {
            super( 0 );
        }

        @Override
        public int min( int bonus ) {
            return Dungeon.hero.totalHealthValue()/2;
        }

        @Override
        public int max( int bonus ) {
            return Dungeon.hero.totalHealthValue()*2/3;
        }

        @Override
        public boolean increaseCombo(){
            return false;
        }

        @Override
        protected int getDamageSTBonus(Hero hero){
            //damage is not incrased based on Hero ST. only based on maxhealth
            return 0;
        }
    }
}
