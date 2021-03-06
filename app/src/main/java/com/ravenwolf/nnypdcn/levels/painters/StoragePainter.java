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
import com.ravenwolf.nnypdcn.items.Generator;
import com.ravenwolf.nnypdcn.items.Heap;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.food.Food;
import com.ravenwolf.nnypdcn.items.potions.PotionOfLiquidFlame;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Room;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.watabou.utils.Random;

public class StoragePainter extends Painter {

	public static void paint( Level level, Room room ) {
		
		final int floor = Terrain.EMPTY_SP;
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, floor );

        Room.Door entrance = room.entrance();

        if (entrance.x == room.left) {
            fill( level, room.right - 1, room.top + 1, 1, room.height() - 1 , Terrain.EMPTY );
            fill( level, room.right - 2, room.top + 1, 1, room.height() - 1 , Terrain.SHELF_EMPTY );
        } else if (entrance.x == room.right) {
            fill( level, room.left + 1, room.top + 1, 1, room.height() - 1 , Terrain.EMPTY );
            fill( level, room.left + 2, room.top + 1, 1, room.height() - 1 , Terrain.SHELF_EMPTY );
        } else if (entrance.y == room.top) {
            fill( level, room.left + 1, room.bottom - 1, room.width() - 1, 1 , Terrain.EMPTY );
            fill( level, room.left + 1, room.bottom - 2, room.width() - 1, 1 , Terrain.SHELF_EMPTY );
        } else if (entrance.y == room.bottom) {
            fill( level, room.left + 1, room.top + 1, room.width() - 1, 1 , Terrain.EMPTY );
            fill( level, room.left + 1, room.top + 2, room.width() - 1, 1 , Terrain.SHELF_EMPTY );
        }
		
		int n = 1 + Random.Int( ( Dungeon.chapter() + 1 ) / 2 );
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = room.random();
			} while (level.map[pos] != Terrain.EMPTY);
			level.drop( prizeBonus(), pos, true ).type = Heap.Type.BONES;
		}

        int pos;
        do {
            pos = room.random();
        } while (level.map[pos] != floor);

        level.drop( prizeMain(level), pos, true ).type = Heap.Type.CHEST;

		room.entrance().set( Room.Door.Type.REGULAR );
        if (1+ Random.Int(5)>=Dungeon.chapter())
            level.addItemToSpawn( new PotionOfLiquidFlame() );
	}

    private static Item prizeMain( Level level ) {

        Item prize = level.itemToSpawnAsPrize( Food.class );

        if (prize != null) {
            return prize;
        }

        return prizeBonus();
    }
	
	private static Item prizeBonus() {
		
		return Generator.random( Generator.Category.MISC );
	}
}
