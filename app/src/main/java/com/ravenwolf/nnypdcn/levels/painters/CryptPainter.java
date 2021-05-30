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

import com.ravenwolf.nnypdcn.actors.hazards.HauntedArmorSleep;
import com.ravenwolf.nnypdcn.items.Generator;
import com.ravenwolf.nnypdcn.items.Heap.Type;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.armours.Armour;
import com.ravenwolf.nnypdcn.items.armours.body.BodyArmor;
import com.ravenwolf.nnypdcn.items.keys.IronKey;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Room;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.watabou.utils.Point;

public class CryptPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill(level, room, Terrain.WALL);
		fill(level, room, 1, Terrain.EMPTY);

		Point c = room.center();
		int cx = c.x;
		int cy = c.y;

		Room.Door entrance = room.entrance();
		entrance.set(Room.Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey());

		if (entrance.x == room.left) {
			set(level, new Point(room.right - 1, room.top + 1), Terrain.STATUE);
			set(level, new Point(room.right - 1, room.bottom - 1), Terrain.STATUE);
			cx = room.right - 2;
		} else if (entrance.x == room.right) {
			set(level, new Point(room.left + 1, room.top + 1), Terrain.STATUE);
			set(level, new Point(room.left + 1, room.bottom - 1), Terrain.STATUE);
			cx = room.left + 2;
		} else if (entrance.y == room.top) {
			set(level, new Point(room.left + 1, room.bottom - 1), Terrain.STATUE);
			set(level, new Point(room.right - 1, room.bottom - 1), Terrain.STATUE);
			cy = room.bottom - 2;
		} else if (entrance.y == room.bottom) {
			set(level, new Point(room.left + 1, room.top + 1), Terrain.STATUE);
			set(level, new Point(room.right - 1, room.top + 1), Terrain.STATUE);
			cy = room.top + 2;
		}

		int pricePos = cx + cy * Level.WIDTH;
		Item prize = prize();
		if (prize instanceof BodyArmor && prize.lootChapter() > 1){//for now only metal armors can be animated
			set(level, pricePos, Terrain.PEDESTAL);
			level.drop(prize, pricePos, true).type = Type.HEAP;
			HauntedArmorSleep hauntedArmor = new HauntedArmorSleep();
			hauntedArmor.setStats(pricePos, (BodyArmor) prize);
			//GameScene.add(hauntedArmor);
			level.hazards.add(hauntedArmor);
		}else
        	level.drop( prize, pricePos, true ).type = Type.TOMB;
	}

    private static Item prize() {

        Armour prize = null;

		for (int i=0; i < 5; i++) {
			Armour another = (Armour)Generator.random( Generator.Category.ARMOR );
			if (prize == null || another.lootLevel()> prize.lootLevel()) {
				prize = another;
			}
		}

        prize.inscribe();

        return prize;
    }
}
