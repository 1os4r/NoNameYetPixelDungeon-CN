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
package com.ravenwolf.nnypdcn.actors.blobs;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypdcn.visuals.sprites.GooSprite;
import com.watabou.utils.Random;

public class Miasma extends Blob {

    public Miasma() {
        super();

        name = "暗瘴";
    }

	@Override
	protected void evolve() {
		super.evolve();

		Char ch;
		for (int i=0; i < LENGTH; i++) {
			if (cur[i] > 0 && (ch = Actor.findChar( i )) != null) {

                if( !ch.isMagical() ) {

                    int effect = (int)Math.sqrt( ch.totalHealthValue() );

                    Withered debuff = ch.buff( Withered.class );

                    if( debuff != null ) {
                        effect += debuff.getDuration()/2;
                    }
                    ch.damage( Random.NormalIntRange(1, effect ) , this, Element.UNHOLY );

                }

                if ( ch.buff( Burning.class ) != null) {
                    GameScene.add(Blob.seed(ch.pos, 2, Fire.class));
                }
			}
		}

        Blob blob = Dungeon.level.blobs.get( Fire.class );
        if (blob != null) {

            for (int pos=0; pos < LENGTH; pos++) {

                if ( cur[pos] > 0 && blob.cur[ pos ] < 2 ) {

                    int flammability = 0;

                    for (int n : Level.NEIGHBOURS8) {
                        if ( blob.cur[ pos + n ] > 0 ) {
                            flammability++;
                        }
                    }

                    if( Random.Int( 4 ) < flammability ) {

                        blob.volume += ( blob.cur[ pos ] = 2 );

                        volume -= ( cur[pos] / 2 );
                        cur[pos] -=( cur[pos] / 2 );

                    }

                }
            }
        }
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );

		emitter.pour( GooSprite.GooParticle.FACTORY, 0.04f );
	}
	
	@Override
	public String tileDesc() {
		return "一团令人窒息的深色暗瘴正在此处盘旋。";
	}
	
//	@Override
//	public void onDeath() {
//
//		Badges.validateDeathFromGas();
//
//		Dungeon.fail( Utils.format( ResultDescriptions.GAS, Dungeon.depth ) );
//		GLog.n( "You died from a toxic gas.." );
//	}
}
