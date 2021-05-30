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
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Room;
import com.ravenwolf.nnypdcn.levels.Terrain;

public class ExitPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );
/*
        for (Room.Door door : room.connected.values()) {
            //door.set( Dungeon.depth != 2 ? Room.Door.Type.REGULAR : Room.Door.Type.HIDDEN );
			door.set( (Dungeon.depth+1) % 5==0 ? Room.Door.Type.HIDDEN : Room.Door.Type.REGULAR );
        }*/

		if (!Dungeon.isPathwayLvl()) {
			level.exit = room.random(1);
			set(level, level.exit, Terrain.EXIT);
		}
	}
	
}
