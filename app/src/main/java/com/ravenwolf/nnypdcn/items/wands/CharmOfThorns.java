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
package com.ravenwolf.nnypdcn.items.wands;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.MagicMissile;
import com.ravenwolf.nnypdcn.visuals.effects.particles.LeafParticle;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.ThornvineSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class CharmOfThorns extends WandUtility {

	{
		name = "荆棘魔咒";
        image = ItemSpriteSheet.CHARM_THORN;
	}


    @Override
    protected void cursedProc(Hero hero){
        curCharges = (curCharges+1)/2;
        int dmg=hero.absorb( damageRoll(), true )/2;
        hero.damage(dmg, this, Element.PHYSICAL);
        BuffActive.addFromDamage( hero, Bleeding.class, dmg );
    }

	@Override
	protected void onZap( int cell ) {

        spawnThornvine(cell);
	}

    protected <T extends Thornvine> void spawnThornvine( int cell) {

        int stats = damageRoll();
        //int level = bonus+1;
        int level = curCharges;

        // first we check the targeted tile
        if( Thornvine.spawnAt( stats, level, cell) == null ) {

            // then we check the previous tile
            int prevCell = Ballistica.trace[ Ballistica.distance - 1 ];

            if( Thornvine.spawnAt( stats, level, prevCell) == null ) {

                // and THEN we check all tiles around the targeted
                ArrayList<Integer> candidates = new ArrayList<Integer>();

                for (int n : Level.NEIGHBOURS8) {
                    int pos = cell + n;
                    if( Level.adjacent( pos, prevCell ) && !Level.solid[ pos ] && !Level.chasm[ pos ] && Actor.findChar( pos ) == null ){
                        candidates.add( pos );
                    }
                }

                if ( candidates.size() > 0 ){
                    Thornvine.spawnAt( stats, level, candidates.get( Random.Int( candidates.size() ) ));
                } else {
                    //if cant spawn thornvine but targeted char, deals damage and bleed
                    Char enemy=Actor.findChar( cell );
                    if (enemy!=null){
                        int dmg=damageRoll();
                        enemy.damage(dmg, this, Element.PHYSICAL);
                        BuffActive.addFromDamage( enemy, Bleeding.class, dmg );
                        CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, Random.IntRange(4, 6));
                    }
                    else
                        GLog.i( "什么也没有发生" );
                }
            }
        }

        CellEmitter.center( cell ).burst( LeafParticle.GENERAL, 5 );
        Sample.INSTANCE.play( Assets.SND_PLANT );
    }

	protected void fx( int cell, Callback callback ) {
		MagicMissile.foliage( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public String desc() {
		return
			"这根法杖上寄宿着能唤出森林之力的神秘力量，允许其在地牢之中召唤刺藤。这些植物会猛烈进攻任何试图" +
                "穿过附近的敌人，在被植物覆盖的面上生成的刺藤会更加强大。";
	}

    protected String getEffectDescription(int min , int max, boolean known){

	    int bonusHP=maxCharges(known? bonus : 0)*3;
        return  "这个魔咒召唤出的刺藤" +(known? "":"(可能) ")+"会造成_" + min *2 / 5+ "-" + max * 3 / 4+ "点伤害_，" +
                    "并且拥有_" + (min + bonusHP)+ "-" + (max +bonusHP) + "的血量_";
    }


    public static class Thornvine extends NPC {

        private int stats;
        private int lvl;

        public Thornvine() {

            name = "刺藤";
            spriteClass = ThornvineSprite.class;

            resistances.put(Element.Flame.class, Element.Resist.VULNERABLE);
            resistances.put(Element.Shock.class, Element.Resist.PARTIAL);

            resistances.put(Element.Mind.class, Element.Resist.IMMUNE);


            PASSIVE = new Guarding();
            hostile = false;
            friendly = true;

        }

        public boolean immovable() {
            return true;
        }

        @Override
        public boolean isMagical() {
            return false;
        }

        @Override
        protected boolean getCloser(int target) {
            return true;
        }

        @Override
        protected boolean getFurther(int target) {
            return true;
        }

        @Override
        public int viewDistance() {
            return 1;
        }


        @Override
        protected boolean act() {
            //int drain=Math.max(1,HT/10) + ( HT % 10 > Random.Int(10) ? 1 : 0 ) ;
            HP -= 1;
            if (HP <= 0) {
                die(this);
                return true;
            }

            return super.act();
        }

        @Override
        public boolean add(Buff buff) {
            if (buff instanceof Ensnared)
                return false;
            else
                return super.add(buff);
        }


        @Override
        public void interact() {
            Dungeon.hero.sprite.operate(pos);

            if (Dungeon.hero.belongings.weap2 instanceof CharmOfThorns) {
                // we restore at least one charge less than what was spent on the vine
                ((CharmOfThorns) Dungeon.hero.belongings.weap2).recharge(
                        (int) (((HP * 2 / 3) * lvl / HT) * ((CharmOfThorns) Dungeon.hero.belongings.weap2).rechargeRate())
                );
                GLog.i("你把刺藤回收到了魔咒中");
            } else {
                GLog.i("你没能把刺藤的能量完全回收");
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
                BuffActive.addFromDamage(enemy, Bleeding.class, damage / 2);
            }

            return damage;
        }

        //lose one HP after each attack
        public void onAttackComplete() {
            super.onAttackComplete();
            HP -= 1;
            if (HP <= 0)
                die(this);
        }

        @Override
        protected Char chooseEnemy() {

            // thornvines attack your enemies by default
            if (enemy == null || !enemy.isAlive()) {

                HashSet<Mob> enemies = new HashSet<Mob>();

                for (Mob mob : Dungeon.level.mobs) {
                    if (mob.hostile && !mob.isFriendly() && Level.fieldOfView[mob.pos]) {
                        enemies.add(mob);
                    }
                }
                return enemies.size() > 0 ? Random.element(enemies) : null;

            } else {

                return enemy;

            }
        }

        protected void adjustStats(int stats, int lvl) {

            HT = stats + lvl * 3;
            armorClass = lvl;

            maxDamage = stats * 3 / 4;
            minDamage = stats * 2 / 5;

            accuracy = stats;
            dexterity = stats / 2;

            this.stats = stats;
            this.lvl = lvl;
        }

        @SuppressWarnings("unchecked")
        public static Thornvine spawnAt(int stats, int lvl, int pos) {

            // cannot spawn on walls, chasms or already occupied tiles
            if (!Level.solid[pos] && !Level.chasm[pos] && Actor.findChar(pos) == null) {

                Thornvine vine = new Thornvine();

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

        public void knockBack(int sourcePos, int damage, int amount, final Callback callback) {
            //kill thronvine if knockBack
            die(null);
            if (callback != null)
                callback.call();
            return;
        }


        private class Guarding extends Passive {

            @Override
            public boolean act( boolean enemyInFOV, boolean justAlerted ){

                if (enemyInFOV && canAttack( enemy ) && enemy != Dungeon.hero ) {

                    return doAttack( enemy );

                } else {
                    //reset enemy if cannot attack
                    resetEnemy();
                    spend( TICK );
                    return true;

                }
            }

            @Override
            public String status(){
                return "防守";
            }
        }

        @Override
        public String description() {
            return
                "刺藤是一种有着自主意识的植物，会攻击任何企图靠近的东西。它们锋利尖刺可以造成严重的伤害，" + 
				"不过它们会随着时间的流逝逐渐枯萎，并且非常害怕火焰";
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
