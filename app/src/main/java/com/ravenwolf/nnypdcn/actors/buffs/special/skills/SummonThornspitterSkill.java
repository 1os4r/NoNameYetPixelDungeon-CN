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
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.wands.CharmOfThorns;
import com.ravenwolf.nnypdcn.items.weapons.throwing.PoisonDarts;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.CellSelector;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.particles.LeafParticle;
import com.ravenwolf.nnypdcn.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.ThornSpitterSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;


public class SummonThornspitterSkill extends BuffSkill {

    {
        CD = 100f;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(striker);
    }


    protected CellSelector.Listener striker = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null && Dungeon.visible[target] &&  spawnThornspitter( target)) {

                SummonThornspitterSkill skill = Dungeon.hero.buff(SummonThornspitterSkill.class);
                if (skill!=null)
                    skill.setCD(skill.getMaxCD());

                Dungeon.hero.spendAndNext(1f);
            }else{
                GLog.i("你不能在这里召唤它");
            }
        }

        @Override
        public String prompt() {
            return "选择召唤的位置";
        }
    };


    protected boolean spawnThornspitter( int cell) {

        //int stats = (int)(Dungeon.hero.lvl*1.5f);
        //int level = Dungeon.chapter()*4;
        int stats = 6+Dungeon.hero.lvl;
        int level = 2+Dungeon.chapter()*3;

        // first we check the targeted tile
        if( ThornSpitter.spawnAt( stats, level, cell) == null ) {

            // then we check the previous tile
            int prevCell = Ballistica.trace[ Ballistica.distance - 1 ];

            if( ThornSpitter.spawnAt( stats, level, prevCell) == null ) {

                // and THEN we check all tiles around the targeted
                ArrayList<Integer> candidates = new ArrayList<Integer>();

                for (int n : Level.NEIGHBOURS8) {
                    int pos = cell + n;
                    if( Level.adjacent( pos, prevCell ) && !Level.solid[ pos ] && !Level.chasm[ pos ] && Actor.findChar( pos ) == null ){
                        candidates.add( pos );
                    }
                }

                if ( candidates.size() > 0 ){
                    return ThornSpitter.spawnAt( stats, level, candidates.get( Random.Int( candidates.size() ) ))!=null;
                } else {
                    return false;
                }
            }
        }

        CellEmitter.center( cell ).burst( LeafParticle.GENERAL, 5 );
        Sample.INSTANCE.play( Assets.SND_PLANT );
        return true;
    }

    public static class ThornSpitter extends CharmOfThorns.Thornvine {

        public ThornSpitter() {
            super();
            name = "荆棘之主";
            spriteClass = ThornSpitterSprite.class;
        }

        public static ThornSpitter spawnAt(int stats, int lvl, int pos) {
            // cannot spawn on walls, chasms or already occupied tiles
            if (!Level.solid[pos] && !Level.chasm[pos] && Actor.findChar(pos) == null) {

                ThornSpitter vine = new ThornSpitter();

                if (Dungeon.level.map[pos] == Terrain.GRASS || Dungeon.level.map[pos] == Terrain.HIGH_GRASS) {
                    lvl += 2;
                }

                vine.pos = pos;
                vine.enemySeen = true;
                vine.state = vine.PASSIVE;

                GameScene.add(vine, 0f);
                Dungeon.level.press(vine.pos, vine);

                vine.sprite.emitter().burst(LeafParticle.LEVEL_SPECIFIC, 5);
                vine.sprite.spawn();

                vine.adjustStats(stats, lvl);
                vine.HP = vine.HT;

                return vine;
            } else {
                return null;
            }
        }

        @Override
        protected boolean canAttack( Char enemy ) {
            return super.canAttack( enemy ) || Level.distance( pos, enemy.pos ) <= 3 &&
                    Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
        }

        @Override
        protected void onRangedAttack( int cell ) {
            ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                    reset(pos, cell, new PoisonDarts(), new Callback() {
                        @Override
                        public void call() {
                            onAttackComplete();
                        }
                    });

            super.onRangedAttack( cell );
        }

        @Override
        public int viewDistance() {
            return 3;
        }

        @Override
        protected boolean act() {
            HP -= 1;
            if (HP <= 0) {
                die(this);
                return true;
            }

            return super.act();
        }


        @Override
        public void interact() {
            Dungeon.hero.sprite.operate(pos);

            GLog.i("你回收了荆棘之主");

            SummonThornspitterSkill skill = Dungeon.hero.buff(SummonThornspitterSkill.class);
            if (skill!=null) {
                //reduce the cooldown based on health factor
                float healthFactor=HP/(HT*3f);
                skill.setCD(Math.max(0, skill.getCD() - (int)(skill.getMaxCD() * healthFactor)));
            }

            Sample.INSTANCE.play(Assets.SND_PLANT);

            Dungeon.hero.spend(TICK);
            Dungeon.hero.busy();

            die(this);
        }

        @Override
        public int attackProc(Char enemy, int damage, boolean blocked) {

            // thornvines apply bleed on hit
            if (!blocked) {
               // BuffActive.addFromDamage(enemy, Crippled.class, damage / 2);
                BuffActive.addFromDamage(enemy, Poisoned.class, damage);
            }

            return damage;
        }

        @Override
        protected Char chooseEnemy() {
            // thornvines attack your enemies by default
            if (enemy == null || !enemy.isAlive()) {
                HashSet<Mob> enemies = new HashSet<Mob>();
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob.hostile && !mob.isFriendly() && canAttack( mob )/*Level.fieldOfView[mob.pos]*/) {
                        enemies.add(mob);
                    }
                }
                return enemies.size() > 0 ? Random.element(enemies) : null;
            } else {
                return enemy;
            }
        }

        @Override
        public String description() {
            return
                "荆棘之主是一种有着自主意识的植物，拥有很强的领地意识，会攻击任何企图靠近的单位。它可以精确地向远处的敌人发射有毒的荆棘。不过它们会随着时间的流逝逐渐枯萎，并且非常害怕火焰";
        }

    }

}
