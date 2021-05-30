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
//
//import com.ravenwolf.nnypdcn.Dungeon;
//import com.ravenwolf.nnypdcn.DungeonTilemap;
//import com.ravenwolf.nnypdcn.actors.hazards.Plant;
//import com.ravenwolf.nnypdcn.levels.Level;
//import com.ravenwolf.nnypdcn.visuals.Assets;
//import com.watabou.noosa.Game;
//import com.watabou.noosa.Image;
//import com.watabou.noosa.TextureFilm;
//
//public class PlantSprite extends Image {
//
//	private static final float DELAY = 0.2f;
//
//	private enum State {
//		GROWING, NORMAL, WITHERING
//	}
//	private State state = State.NORMAL;
//	private float time;
//
//	private static TextureFilm frames;
//
//	private int pos = -1;
//
//	public PlantSprite() {
//		super( Assets.PLANTS );
//
//		if (frames == null) {
//			frames = new TextureFilm( texture, 16, 16 );
//		}
//
//		origin.set( 8, 12 );
//	}
//
//	public PlantSprite( int image ) {
//		this();
//		reset( image );
//	}
//
//	public void reset( Plant plant ) {
//
//		revive();
//
//		reset( plant.image );
//		alpha( 1f );
//
//		pos = plant.pos;
//		x = (pos % Level.WIDTH) * DungeonTilemap.SIZE;
//		y = (pos / Level.WIDTH) * DungeonTilemap.SIZE;
//
//		state = State.GROWING;
//		time = DELAY;
//	}
//
//	public void reset( int image ) {
//		frame( frames.get( image ) );
//	}
//
//	@Override
//	public void update() {
//		super.update();
//
//		visible = pos == -1 || Dungeon.visible[pos];
//
//		switch (state) {
//		case GROWING:
//			if ((time -= Game.elapsed) <= 0) {
//				state = State.NORMAL;
//				scale.set( 1 );
//			} else {
//				scale.set( 1 - time / DELAY );
//			}
//			break;
//		case WITHERING:
//			if ((time -= Game.elapsed) <= 0) {
//				super.kill();
//			} else {
//				alpha( time / DELAY );
//			}
//			break;
//		default:
//		}
//	}
//
//	@Override
//	public void kill() {
//		state = State.WITHERING;
//		time = DELAY;
//	}
//}
