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
package com.ravenwolf.nnypdcn.levels;

import com.ravenwolf.nnypdcn.Bones;
import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.mobs.Bestiary;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.Heap;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.keys.IronKey;
import com.ravenwolf.nnypdcn.items.keys.SkeletonKey;
import com.ravenwolf.nnypdcn.levels.Room.Type;
import com.ravenwolf.nnypdcn.levels.painters.Painter;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.watabou.noosa.Scene;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.List;

public class PrisonBossLevel extends RegularLevel {

    private static final int BOSS_ISHIDDEN = 0;
    private static final int BOSS_APPEARED = 1;
    private static final int BOSS_DEFEATED = 2;

    private int progress = 0;


	private Room anteroom;
	private int arenaDoor;
	

	
	@Override
	protected boolean build() {
		
		initRooms();

		int distance;
		int retry = 0;

		do {
			
			if (retry++ > 10) {
				return false;
			}
			
			int innerRetry = 0;
			do {
				if (innerRetry++ > 10) {
					return false;
				}
				roomEntrance = Random.element( rooms );
			} while (roomEntrance.width() < 4 || roomEntrance.height() < 4 ||
                    roomEntrance.top == 0);
			
			innerRetry = 0;
			do {
				if (innerRetry++ > 10) {
					return false;
				}
				roomExit = Random.element( rooms );
			} while (
				roomExit == roomEntrance || 
				roomExit.width() < 7 || 
				roomExit.height() < 7 || 
				roomExit.top == 0);
	
			Graph.buildDistanceMap( rooms, roomExit );
			distance = Graph.buildPath( rooms, roomEntrance, roomExit ).size();
			
		} while (distance < 3);
		
		roomEntrance.type = Type.ENTRANCE;
		roomExit.type = Type.STANDARD;
		
		List<Room> path = Graph.buildPath( rooms, roomEntrance, roomExit );	
		Graph.setPrice( path, roomEntrance.distance );
		
		Graph.buildDistanceMap( rooms, roomExit );
		path = Graph.buildPath( rooms, roomEntrance, roomExit );
		
		anteroom = path.get( path.size() - 2 );
		anteroom.type = Type.STANDARD;
		
		Room room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
		}
		
		for (Room r : rooms) {
			if (r.type == Type.NULL && r.connected.size() > 0) {
				r.type = Type.PASSAGE; 
			}
		}
		
		paint();

		Room r = (Room)roomEntrance.connected.keySet().toArray()[0];
		if (roomEntrance.connected.get( r ).y == roomEntrance.top) {
			return false;
		}

        exit = roomEntrance.top * Level.WIDTH + (roomEntrance.left + roomEntrance.right) / 2;
        Painter.set( this, exit, Terrain.LOCKED_EXIT );
		
		paintWater();
		paintGrass();
		
		placeTraps();
//		placeSign();

		return true;
	}
		
	protected void paintDoors( Room r ) {
		for (Room n : r.connected.keySet()) {
			
			if (r.type == Type.NULL) {
				continue;
			}
			
			Point door = r.connected.get( n );
			
			if (r.type == Room.Type.PASSAGE && n.type == Room.Type.PASSAGE) {
				
				Painter.set( this, door, Terrain.EMPTY );
				
			} else {
				
				Painter.set( this, door, Terrain.DOOR_CLOSED);
				
			}
			
		}
	}
	
	@Override
	protected void placeTraps() {

		int nTraps = nTraps();

		for (int i=0; i < nTraps; i++) {
			
			int trapPos = Random.Int( LENGTH );
			
			if (map[trapPos] == Terrain.EMPTY) {
				map[trapPos] = Terrain.BLADE_TRAP;
			}
		}
	}
	
	@Override
	protected void decorate() {	
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
			if (map[i] == Terrain.EMPTY) { 
				
				float c = 0.15f;
				if (map[i + 1] == Terrain.WALL && map[i + WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i + WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i + 1] == Terrain.WALL && map[i - WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i - WIDTH] == Terrain.WALL) {
					c += 0.2f;
				}
				
				if (Random.Float() < c) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		for (int i=0; i < WIDTH; i++) {
			if (map[i] == Terrain.WALL &&  
				(map[i + WIDTH] == Terrain.EMPTY || map[i + WIDTH] == Terrain.EMPTY_SP) &&
				Random.Int( 4 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.WALL && 
				map[i - WIDTH] == Terrain.WALL && 
				(map[i + WIDTH] == Terrain.EMPTY || map[i + WIDTH] == Terrain.EMPTY_SP) &&
				Random.Int( 2 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
//		while (true) {
//			int pos = roomEntrance.random();
//			if (pos != entrance) {
//				map[pos] = Terrain.SIGN;
//				break;
//			}
//		}
		
		Point door = roomExit.entrance();
		arenaDoor = door.x + door.y * WIDTH;
		Painter.set( this, arenaDoor, Terrain.LOCKED_DOOR );

		Painter.fill(this,
				roomExit.left + 2,
				roomExit.top + 2,
				roomExit.width() - 3,
				roomExit.height() - 3,
				Terrain.INACTIVE_TRAP);
	}
	
	@Override
	protected void createMobs() {
	}
	
	public Actor respawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		int keyPos = anteroom.random();
		while (!passable[keyPos]) {
			keyPos = anteroom.random();
		}
		drop( new IronKey(), keyPos ).type = Heap.Type.CHEST;
		
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = roomEntrance.random();
			} while (pos == entrance || map[pos] == Terrain.SIGN);
			drop( item, pos ).type = Heap.Type.BONES;
		}
	}

	@Override
	public void press( int cell, Char ch ) {
		
		super.press( cell, ch );
		
		if (ch == Dungeon.hero && progress == BOSS_ISHIDDEN && roomExit.inside( cell )) {

            progress = BOSS_APPEARED;
		
			int pos;
			do {
				pos = roomExit.random();
			} while (pos == cell || Actor.findChar( pos ) != null);
			
			Mob boss = Bestiary.mob( Dungeon.depth );
			boss.state = boss.HUNTING;
			boss.pos = pos;
			GameScene.add( boss, 2f );
			boss.notice();
			
			press( pos, boss );
			
			set( arenaDoor, Terrain.LOCKED_DOOR );
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();

            Music.INSTANCE.play( currentTrack(), true );
		}
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if ( progress == BOSS_APPEARED && item instanceof SkeletonKey) {

            progress = BOSS_DEFEATED;
			
			set( arenaDoor, Terrain.DOOR_CLOSED);
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();

            Music.INSTANCE.play( currentTrack(), true );
		}
		
		return super.drop( item, cell );
	}
	
	@Override
    public int randomRespawnCell( boolean ignoreTraps, boolean ignoreView ) {

        int cell;

        // FIXME

        if( progress == BOSS_ISHIDDEN ) {
            do {
                cell = Random.Int(LENGTH);
            } while (!mob_passable[cell] || Actor.findChar(cell) != null || roomExit.inside(cell) );
        } else if( progress==BOSS_APPEARED ) {
            do {
                cell = Random.Int(LENGTH);
            } while (!mob_passable[cell] || Actor.findChar(cell) != null || !roomExit.inside(cell) );
        } else {
            do {
                cell = Random.Int(LENGTH);
            } while (!mob_passable[cell] || Actor.findChar(cell) != null );
        }

        return cell;
    }

    @Override
    public boolean noTeleport() {
        return progress==BOSS_APPEARED;
    }
	
	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}

    @Override
    public String tileName( int tile ) {
        return PrisonLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return PrisonLevel.tileDescs(tile);
    }

	@Override
	public void addVisuals( Scene scene ) {
		PrisonLevel.addVisuals( this, scene );
	}

	protected boolean[] water() {
		return Patch.generate( 0.45f, 5 );
	}

	protected boolean[] grass() {
		return Patch.generate( 0.30f, 4 );
	}

    @Override
    public int nMobs() {
        return 0;
    }

    @Override
    public String currentTrack() {
        return progress == BOSS_APPEARED ? Assets.TRACK_BOSS_LOOP : super.currentTrack();
    }

    private static final String ARENA	    = "arena";
    private static final String DOOR	    = "door";
    private static final String PROGRESS    = "progress";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( ARENA, roomExit );
        bundle.put( DOOR, arenaDoor );
        bundle.put( PROGRESS, progress );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        roomExit = (Room)bundle.get( ARENA );
        arenaDoor = bundle.getInt( DOOR );
        progress = bundle.getInt( PROGRESS );
    }
}