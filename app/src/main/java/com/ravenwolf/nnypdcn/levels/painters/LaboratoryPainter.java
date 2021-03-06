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
package com.ravenwolf.nnypdcn.levels.painters;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.blobs.Alchemy;
import com.ravenwolf.nnypdcn.items.Generator;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.keys.IronKey;
import com.ravenwolf.nnypdcn.items.potions.Potion;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Room;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class LaboratoryPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill(level, room, 1, Terrain.EMPTY_SP);
        fill(level, room.left + 1, room.top + 1, room.width() - 1, 1, Terrain.PEDESTAL);
		
		Room.Door entrance = room.entrance();
		
		Point pot = null;
		if (entrance.x == room.left) {
			pot = new Point( room.right-1, Random.Int( 2 ) == 0 ? room.top + 1 : room.bottom - 1 );
            set( level, entrance.x + 1, entrance.y, Terrain.EMPTY_SP );
		} else if (entrance.x == room.right) {
			pot = new Point( room.left+1, Random.Int( 2 ) == 0 ? room.top + 1 : room.bottom - 1 );
            set( level, entrance.x - 1, entrance.y, Terrain.EMPTY_SP );
		} else if (entrance.y == room.top) {
			pot = new Point( Random.Int( 2 ) == 0 ? room.left + 1 : room.right - 1, room.bottom-1 );
            set(level, entrance.x, entrance.y + 1, Terrain.EMPTY_SP);
		} else if (entrance.y == room.bottom) {
			pot = new Point( Random.Int( 2 ) == 0 ? room.left + 1 : room.right - 1, room.top+1 );
            set(level, entrance.x, entrance.y - 1, Terrain.EMPTY_SP);
		}

		set( level, pot, Terrain.ALCHEMY );
		
		Alchemy alchemy = new Alchemy();
		alchemy.seed( pot.x + Level.WIDTH * pot.y, 1 );
		level.blobs.put( Alchemy.class, alchemy );
		
		int n = 1 + Random.Int( ( Dungeon.chapter() + 1 ) / 2 );
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = room.random();
			} while ( level.map[pos] != Terrain.PEDESTAL );

            level.drop( i == 0 ? prize( level ) : Generator.random( Generator.Category.POTION ), pos, true );
		}
		
		entrance.set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
	}
	
	private static Item prize( Level level ) {
		
		Item prize = level.itemToSpawnAsPrize( Potion.class );
        if (prize != null) {
			return prize;
		}
		
		return Generator.random( Generator.Category.POTION );
	}
}
