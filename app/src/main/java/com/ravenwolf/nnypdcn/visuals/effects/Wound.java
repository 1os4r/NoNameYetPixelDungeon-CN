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
package com.ravenwolf.nnypdcn.visuals.effects;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.DungeonTilemap;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.levels.Level;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class Wound extends Image {

	private static final float TIME_TO_FADE = 0.8f;
	
	private float time;
	
	public Wound() {
		super( Effects.get( Effects.Type.WOUND ) );
		origin.set( width / 2, height / 2 );
	}
	
	public void reset( int p ) {
		revive();
		
		x = (p % Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - width) / 2;
		y = (p / Level.WIDTH) * DungeonTilemap.SIZE + (DungeonTilemap.SIZE - height) / 2;
		
		time = TIME_TO_FADE;
	}
	
	@Override
	public void update() {
		super.update();
		
		if ((time -= Game.elapsed) <= 0) {
			kill();
		} else {
			float p = time / TIME_TO_FADE;
			alpha( p );
			scale.x = 1 + p;
		}
	}
	
	public static void hit( Char ch ) {
		hit( ch, Random.Float( -30, 30 ) );
	}

	public static void doubleHit( Char ch ) {
		hit( ch, Random.Float( 30, 60 ) );
		hit( ch, Random.Float( -10, -40 ) );
	}
	
	public static void hit( Char ch, float angle ) {
		Wound w = (Wound)ch.sprite.parent.recycle( Wound.class );
		ch.sprite.parent.bringToFront( w );
		w.reset( ch.pos );
		w.angle = angle;
	}
	
	public static void hit( int pos ) {
		hit( pos, 0 );
	}
	
	public static void hit( int pos, float angle ) {
		Group parent = Dungeon.hero.sprite.parent;
		Wound w = (Wound)parent.recycle( Wound.class );
		parent.bringToFront( w );
		w.reset( pos );
		w.angle = angle;
	}
}
