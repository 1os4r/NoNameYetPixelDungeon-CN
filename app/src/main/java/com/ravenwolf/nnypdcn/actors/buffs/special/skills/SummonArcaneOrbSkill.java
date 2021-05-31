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
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.CellSelector;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.BlastWave;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.particles.LeafParticle;
import com.ravenwolf.nnypdcn.visuals.effects.particles.SparkParticle;
import com.ravenwolf.nnypdcn.visuals.sprites.ArcaneOrbSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;


public class SummonArcaneOrbSkill extends BuffSkill {

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

            if (target != null &&  Dungeon.visible[target] && spawnArcaneOrb( target)) {

                SummonArcaneOrbSkill skill = Dungeon.hero.buff(SummonArcaneOrbSkill.class);
                if (skill!=null)
                    skill.setCD(skill.getMaxCD());

                Dungeon.hero.spendAndNext(1f);
            }else{
                GLog.i("你不能在这里召唤它");
                return;
            }
        }

        @Override
        public String prompt() {
            return "选择召唤的位置";
        }
    };


    protected boolean spawnArcaneOrb( int cell) {

        int stats = 6+Dungeon.hero.lvl;
        int level = Dungeon.chapter() * 2;

        // first we check the targeted tile
        if( ArcaneOrb.spawnAt( stats, level, cell) == null ) {

            // then we check the previous tile
            int prevCell = Ballistica.trace[ Ballistica.distance - 1 ];

            if( ArcaneOrb.spawnAt( stats, level, prevCell) == null ) {

                // and THEN we check all tiles around the targeted
                ArrayList<Integer> candidates = new ArrayList<Integer>();

                for (int n : Level.NEIGHBOURS8) {
                    int pos = cell + n;
                    if( Level.adjacent( pos, prevCell ) && !Level.solid[ pos ] && !Level.chasm[ pos ] && Actor.findChar( pos ) == null ){
                        candidates.add( pos );
                    }
                }

                if ( candidates.size() > 0 ){
                    return ArcaneOrb.spawnAt( stats, level, candidates.get( Random.Int( candidates.size() ) ))!=null;
                } else {
                    return false;
                }
            }
        }

        CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 6 );
        Sample.INSTANCE.play( Assets.SND_PLANT );
        return true;
    }

    public static class ArcaneOrb extends NPC {

        private int stats;
        private int lvl;

        public ArcaneOrb() {
            super();
            name = "奥术之球";
            spriteClass = ArcaneOrbSprite.class;

            resistances.put(Element.Physical.class, Element.Resist.PARTIAL);
            resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
            resistances.put(Element.Body.class, Element.Resist.IMMUNE);

            flying = true;
            baseSpeed = 2f;

            hostile = false;
            friendly = true;
        }

        @Override
        public Element damageType() {
            return Element.ENERGY;
        }

        @Override
        public boolean isMagical() {
            return true;
        }

        public static ArcaneOrb spawnAt(int stats, int lvl, int pos) {
            // cannot spawn on walls, chasms or already occupied tiles
            if (!Level.solid[pos] && !Level.chasm[pos] && Actor.findChar(pos) == null) {

                ArcaneOrb orb = new ArcaneOrb();

                if (Dungeon.level.map[pos] == Terrain.GRASS || Dungeon.level.map[pos] == Terrain.HIGH_GRASS) {
                    lvl += 2;
                }

                orb.pos = pos;
                orb.enemySeen = true;
                orb.state = orb.HUNTING;

                GameScene.add(orb, 1f);
                //Dungeon.level.press(orb.pos, orb);

                orb.sprite.emitter().burst(LeafParticle.LEVEL_SPECIFIC, 5);
                orb.sprite.spawn();

                orb.adjustStats(stats, lvl);
                orb.HP = orb.HT;

                return orb;
            } else {
                return null;
            }
        }

        //lose one HP after each attack
        public void onAttackComplete() {

            for (int n : Level.NEIGHBOURS8) {
                int auxPos = pos + n;

                if (Level.solid[auxPos] || auxPos == enemy.pos){//ignore main target
                    continue;
                }

                Char ch = Actor.findChar(auxPos);
                if (ch != null && !ch.isFriendly() && !(ch instanceof NPC)) {
                    attack( ch );
                }
            }
            if (Level.fieldOfView[pos])
                BlastWave.createAtPos( pos , sprite.blood(), false);

            super.onAttackComplete();
            HP -= 3;
            if (HP <= 0)
                die(this);
        }

        @Override
        protected boolean act() {
            HP -= 1;
            if (HP <= 0) {
                die(this);
                return true;
            }

            //will attempt to stay cloe to hero
            Hero hero=Dungeon.hero;
            if(hero !=null && hero.isAlive()){

                if (enemy==null || !enemy.isAlive()){
                    if(Level.distance(pos,hero.pos)>4)
                        target = hero.pos;
                }
            }

            return super.act();
        }

        protected void adjustStats(int stats, int lvl) {

            HT = stats + lvl * 6;
            armorClass = 0;

            maxDamage = stats * 2 / 3;
            minDamage = stats * 2 / 5;

            accuracy = stats;
            dexterity = stats / 2;

            this.stats = stats;
            this.lvl = lvl;
        }


        @Override
        public void interact() {
            //swap position
            int curPos = pos;

            moveSprite( pos, Dungeon.hero.pos );
            move( Dungeon.hero.pos );

            Dungeon.hero.sprite.move( Dungeon.hero.pos, curPos );
            Dungeon.hero.move( curPos );

            Dungeon.hero.spend( 1 / Dungeon.hero.moveSpeed() );
            Dungeon.hero.busy();

        }

        @Override
        protected Char chooseEnemy() {
            //attack the nearest enemy
            if(enemy!=null && enemy.isAlive() && Level.adjacent(pos,enemy.pos)){
                return enemy;
            }else{
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob.hostile && !mob.isFriendly() && Level.fieldOfView[mob.pos]) {
                        if (enemy==null || !enemy.isAlive() || Level.distance(pos,enemy.pos)>Level.distance(pos,mob.pos))
                            enemy=mob;
                    }
                }
                return enemy;
            }
        }

        @Override
        public String description() {
            return
                    "由奥术能量组成的球状体，可以抵抗部分物理攻击，" + 
                    "这个实体是不可控的，拥有以极快的速度，并且会释放奥术能量攻击周围的敌人。而它会随着时间的流逝而逐渐消失。";
        }

        private static final String STATS	= "stats";
        private static final String CHARGES	= "lvl";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( STATS, stats );
            bundle.put( CHARGES, lvl);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            adjustStats( bundle.getInt( STATS ), bundle.getInt( CHARGES ) );
        }

    }

}
