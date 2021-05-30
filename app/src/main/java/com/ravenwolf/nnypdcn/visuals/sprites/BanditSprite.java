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

public class BanditSprite extends MobSprite {


	public BanditSprite() {
		super();
		
		texture( Assets.THIEF );
		TextureFilm film = new TextureFilm( texture, 12, 13 );
		
		idle = new Animation( 1, true );
		idle.frames( film, 21, 21, 21, 22, 21, 21, 21, 21, 22 );
		
		run = new Animation( 15, true );
		run.frames( film, 21, 21, 23, 24, 24, 25 );
		
		die = new Animation( 10, false );
		die.frames( film, 26, 27, 28, 29, 30);
		
		attack = new Animation( 12, false );
		attack.frames( film, 31, 32, 33, 21 );

		sleep = new Animation( 1, true );
		sleep.frames( film, 35, 36, 35, 36, 35, 37 );

        cast = attack.clone();
		
		idle();
	}
}
