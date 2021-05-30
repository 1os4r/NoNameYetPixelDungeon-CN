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
package com.ravenwolf.nnypdcn.actors.mobs;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.DungeonTilemap;
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.DeathRay;
import com.ravenwolf.nnypdcn.visuals.effects.particles.PurpleParticle;
import com.ravenwolf.nnypdcn.visuals.sprites.EyeSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class EvilEye extends MobRanged {

    public EvilEye() {

        super( 11 );

		name = "邪恶之眼";
		spriteClass = EyeSprite.class;
		
		flying = true;
        //loot = new MysteryMeat();
        //lootChance = 0.35f;

        resistances.put(Element.Energy.class, Element.Resist.PARTIAL);

	}

    @Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING && HP <= HT/2/*HP >= HT*/ && (enemySeen || enemy != null && detected( enemy ))) {
			return getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}

    @Override
    protected boolean canAttack( Char enemy ) {

        //return ( HP > HT/2 || !Level.adjacent( pos, enemy.pos ) ) && super.canAttack(enemy);
                //!isCharmedBy( enemy ) && Ballistica.cast( pos, enemy.pos, false, false ) == enemy.pos;
        return ( HP > HT/2 || !Level.adjacent( pos, enemy.pos ) ) && !isCharmedBy( enemy ) && Ballistica.cast( pos, enemy.pos, false, false ) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {

        Sample.INSTANCE.play(Assets.SND_RAY);

        sprite.parent.add( new DeathRay( sprite.center(), DungeonTilemap.tileCenterToWorld( cell ) ) );

        onCastComplete();

        super.onRangedAttack( cell );

    }

    @Override
    public boolean cast( Char enemy ) {

        boolean terrainAffected = false;

        for (int i=1; i < Ballistica.distance ; i++) {

            int pos = Ballistica.trace[i];

            int terr = Dungeon.level.map[pos];

            if (terr == Terrain.DOOR_CLOSED) {

                Level.set(pos, Terrain.EMBERS);
                GameScene.updateMap(pos);
                terrainAffected = true;

            } else if (terr == Terrain.HIGH_GRASS) {

                Level.set( pos, Terrain.GRASS );
                GameScene.updateMap( pos );
                terrainAffected = true;

            }

//            Heap heap = Dungeon.level.heaps.get( pos );
//            if (heap != null) {
//                heap.disintegrate( true );
//            }

            Char ch = Actor.findChar( pos );

            if (ch != null) {

                if (hit(this, ch, false, true)) {

                    //TODO: use the castBolt method and make it return true if hit the target

                    ch.damage( ch.absorb( damageRoll(), true ), this, Element.ENERGY );

                    if (Dungeon.visible[pos]) {
                        ch.sprite.flash();
                        CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                    }


//                    if (!ch.isAlive() && ch == Dungeon.hero) {
//                        Dungeon.fail(Utils.format(ResultDescriptions.MOB, Utils.indefinite(name), Dungeon.depth));
//                        GLog.n(TXT_DEATHGAZE_KILLED, name);
//                    }
                } else {
                    enemy.missed();
                }
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        return true;
    }

	
	@Override
	public String description() {
		return
			"这种生物有着另一个名字：\"憎恨之珠\"，当它看到敌人时，它会不顾一切地使用死亡凝视，经常忽视并击中它的友军。";
	}
}
