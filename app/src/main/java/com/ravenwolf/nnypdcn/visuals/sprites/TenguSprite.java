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
package com.ravenwolf.nnypdcn.visuals.sprites;

import com.ravenwolf.nnypdcn.visuals.Assets;
import com.watabou.noosa.TextureFilm;

public class TenguSprite extends MobSprite {

	Animation catatonic;
	Animation catatonicLoop;

	public TenguSprite() {
		super();
		
		texture( Assets.TENGU );
		
		TextureFilm frames = new TextureFilm( texture, 14, 16 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1 );
		
		run = new Animation( 15, false );
		run.frames( frames, 2, 3, 4, 5, 0 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 6, 7, 7, 0 );
		
//		cast = attack.clone();
		
		die = new Animation( 8, false );
		die.frames( frames, 8, 9, 10, 10, 10, 10, 10, 10 );

		catatonic = new Animation( 8, false );
		catatonic.frames( frames, 8, 9, 10 );

		catatonicLoop = new Animation( 8, true );
		catatonicLoop.frames( frames,  10 );
		
		play( run.clone() );
	}

	public void catatonic( ){
		play(catatonic);
	}

	@Override
	public void move( int from, int to ) {
		
		place( to );
		
		play( run );
		turnTo( from , to );
		
		isMoving = true;
		
//		if (Level.water[to] && visible) {
//			GameScene.ripple( to );
//		}
		
		ch.onMotionComplete();
	}
	
//	@Override
//	public void attack( int cell ) {
//		if (!Level.adjacent( cell, ch.pos )) {
//
//			((MissileSprite)parent.recycle( MissileSprite.class )).
//				fail( ch.pos, cell, new Shurikens(), new Callback() {
//					@Override
//					public void call() {
//						ch.onAttackComplete();
//					}
//				} );
//
//			play( cast );
//			turnTo( ch.pos , cell );
//
//		} else {
//
//			super.attack( cell );
//
//		}
//	}

//    @Override
//    public void attack( int cell ) {
//        if (!Level.adjacent(cell, ch.pos)) {
//
//            cellToAttack = cell;
//            turnTo( ch.pos , cell );
//            play(cast);
//
//        } else {
//
//            super.attack(cell);
//
//        }
//    }

//    @Override
//    public void onComplete( Animation anim ) {
//        if (anim == cast) {
//            idle();
//
//            ((MissileSprite)parent.recycle( MissileSprite.class )).
//                    fail(ch.pos, cellToAttack, new Shurikens(), new Callback() {
//                        @Override
//                        public void call() {
//                            ch.onAttackComplete();
//                        }
//                    });
//        } else {
//            super.onComplete(anim);
//        }
//    }
//
	@Override
	public void onComplete( Animation anim ) {
		if (anim == run) {
			isMoving = false;
			idle();
		} else if (anim == catatonic) {
			play(catatonicLoop);
		}else{
			super.onComplete( anim );
		}
	}
}
