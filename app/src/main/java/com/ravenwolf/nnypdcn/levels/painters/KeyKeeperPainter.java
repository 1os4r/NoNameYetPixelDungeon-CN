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
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.actors.mobs.PrisonGuard;
import com.ravenwolf.nnypdcn.actors.mobs.Robot;
import com.ravenwolf.nnypdcn.items.Heap;
import com.ravenwolf.nnypdcn.items.keys.IronKey;
import com.ravenwolf.nnypdcn.items.keys.SkeletonKey;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Room;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class KeyKeeperPainter extends Painter {

	public static void paint( Level level, Room room ) {

        fill(level, room, Terrain.WALL);
        fill(level, room, 1, Terrain.EMPTY);

//		Point c = room.center();
//		int cx = c.x;
//		int cy = c.y;

        Room.Door door = room.entrance();

        door.set(Room.Door.Type.LOCKED);
        level.addItemToSpawn(new IronKey());

        int x = -1;
        int y = -1;

        //Room.Door entrance = room.entrance();
        Point a = null;
        Point exitA = null;

        Point b = null;
        Point exitB = null;
/*
        if (door.x == room.left) {

            x = room.right - 1;
            y = room.top + room.height() / 2;

            a = new Point( x, y-2 );
            b = new Point( x, y+2 );
            exitA = new Point( room.right, y-1 );
            exitB = new Point( room.right, y+1 );


        } else if (door.x == room.right) {

            x = room.left + 1;
            y = room.top + room.height() / 2;

            a = new Point( x, y-2 );
            b = new Point( x, y+2 );

            exitA = new Point( room.left, y-1 );
            exitB = new Point( room.left, y+1 );


        } else if (door.y == room.top) {

            x = room.left + room.width() / 2;
            y = room.bottom - 1;

            a = new Point( x+2, y );
            b = new Point( x-2, y );

            exitA = new Point( x+1, room.bottom  );
            exitB = new Point( x-1, room.bottom );


        } else if (door.y == room.bottom) {
*/
        x = room.left + room.width() / 2;
        y = room.top + 1;

        a = new Point(x + 2, y);
        b = new Point(x - 2, y);

        exitA = new Point(x + 1, room.top);
        exitB = new Point(x - 1, room.top);


        //}
        if (a != null /*&& level.map[a.x + a.y * Level.WIDTH] == Terrain.EMPTY*/) {
            set(level, a, Terrain.PEDESTAL);
        }
        if (b != null /*&& level.map[b.x + b.y * Level.WIDTH] == Terrain.EMPTY*/) {
            set(level, b, Terrain.STATUE);
        }

        int pos = x + y * Level.WIDTH;


        level.drop(new SkeletonKey(), pos, true).type = Heap.Type.CHEST;


        Mob enemy = null;
        if (Dungeon.depth == Dungeon.CAVES_PATHWAY)
            enemy = new Robot();
        if (Dungeon.depth == Dungeon.PRISON_PATHWAY)
            enemy = new PrisonGuard();
        if (enemy != null){
            enemy.pos = pos;
            //enemy.special = true;//?

            level.mobs.add(enemy);
                enemy.state=enemy.PASSIVE;
            Actor.occupyCell(enemy);
        }

        level.exit = exitA.x+exitA.y * Level.WIDTH;
        level.exitAlternative = exitB.x+exitB.y * Level.WIDTH;

        level.map[level.exit] = Terrain.LOCKED_EXIT;
        level.map[level.exitAlternative] = Terrain.LOCKED_EXIT;
        if(Random.Int(3)>0){
                if(Random.Int(2)==0)
                        level.map[level.exit] = Terrain.WALL_SIGN;
                else
                        level.map[level.exitAlternative] = Terrain.WALL_SIGN;
        }



/*
        if (door.x == room.left || door.x == room.right) {

            statue1.pos = pos + Level.WIDTH;
            statue2.pos = pos - Level.WIDTH;

        } else if (door.y == room.top || door.y == room.bottom) {

            statue1.pos = pos + 1;
            statue2.pos = pos - 1;

        }
*/



	}

}
