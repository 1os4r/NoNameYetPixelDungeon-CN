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

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.DungeonTilemap;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.Blacksmith;
import com.ravenwolf.nnypdcn.levels.Room.Type;
import com.ravenwolf.nnypdcn.levels.painters.Painter;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class CavesLevel extends RegularLevel {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
		
//		viewDistance = 6;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}

    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 6 );
    }

    protected boolean[] grass() {
        return Patch.generate( feeling == Feeling.GRASS ? 0.55f : 0.35f, 3 );
    }
	
	@Override
	protected void assignRoomType() {
		super.assignRoomType();
		
		Blacksmith.Quest.spawn( rooms );
	}
	
	@Override
	protected void decorate() {
		
		for (Room room : rooms) {
			if (room.type != Room.Type.STANDARD) {
				continue;
			}
			
			if (room.width() <= 3 || room.height() <= 3) {
				continue;
			}
			
			int s = room.square();
			
			if (Random.Int( s ) > 4) {
				int corner = (room.left + 1) + (room.top + 1) * WIDTH;
				if (heaps.get( corner ) == null && map[corner - 1] == Terrain.WALL && map[corner - WIDTH] == Terrain.WALL) {
					map[corner] = Terrain.WALL;
				}
			}
			
			if (Random.Int( s ) > 4) {
				int corner = (room.right - 1) + (room.top + 1) * WIDTH;
				if (heaps.get( corner ) == null && map[corner + 1] == Terrain.WALL && map[corner - WIDTH] == Terrain.WALL) {
					map[corner] = Terrain.WALL;
				}
			}
			
			if (Random.Int( s ) > 4) {
				int corner = (room.left + 1) + (room.bottom - 1) * WIDTH;
				if (heaps.get( corner ) == null && map[corner - 1] == Terrain.WALL && map[corner + WIDTH] == Terrain.WALL) {
					map[corner] = Terrain.WALL;
				}
			}
			
			if (Random.Int( s ) > 4) {
				int corner = (room.right - 1) + (room.bottom - 1) * WIDTH;
				if (heaps.get( corner ) == null && map[corner + 1] == Terrain.WALL && map[corner + WIDTH] == Terrain.WALL) {
					map[corner] = Terrain.WALL;
				}
			}

			for (Room n : room.connected.keySet()) {
				if ((n.type == Room.Type.STANDARD || n.type == Room.Type.TUNNEL) && Random.Int( 3 ) == 0) {
					Painter.set( this, room.connected.get( n ), Terrain.EMPTY_DECO );
				}
			}
		}
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.EMPTY) {
				int n = 0;
				if (map[i+1] == Terrain.WALL) {
					n++;
				}
				if (map[i-1] == Terrain.WALL) {
					n++;
				}
				if (map[i+WIDTH] == Terrain.WALL) {
					n++;
				}
				if (map[i-WIDTH] == Terrain.WALL) {
					n++;
				}
				if (Random.Int( 6 ) <= n) {
					if(Random.Int( 8 ) <= n && Dungeon.depth > Dungeon.CAVES_PATHWAY && Dungeon.MINES_OPTION.equals(Dungeon.cavesOption)) {
						map[i] = Terrain.INACTIVE_TRAP;
					}else
						map[i] = Terrain.EMPTY_DECO;
				}

			}
		}
		
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.WALL && Random.Int( 12 ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}
		
//		while (true) {
//			int pos = roomEntrance.random_top();
//			if (map[pos] == Terrain.WALL) {
//				map[pos] = Terrain.WALL_SIGN;
//				break;
//			}
//		}
		
//		if (Dungeon.bossLevel( Dungeon.depth + 1 )) {
//			return;
//		}

		//Pathway level dont have chasms
		if (Dungeon.CAVES_PATHWAY==Dungeon.depth)
			return;
		
		for (Room r : rooms) {
			if (r.type == Type.STANDARD) {
				for (Room n : r.neighbours) {
					if (n.type == Type.STANDARD && !r.connected.containsKey( n )) {
						Rect w = r.intersect( n );
						if (w.left == w.right && w.bottom - w.top >= 5) {
							
							w.top += 2;
							w.bottom -= 1;
							
							w.right++;
							
							Painter.fill( this, w.left, w.top, 1, w.height(), Terrain.CHASM );
							
						} else if (w.top == w.bottom && w.right - w.left >= 5) {
							
							w.left += 2;
							w.right -= 1;
							
							w.bottom++;
							
							Painter.fill( this, w.left, w.top, w.width(), 1, Terrain.CHASM );
						}
					}
				}
			}
		}
	}

    @Override
    public String tileName( int tile ) {
        return CavesLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return CavesLevel.tileDescs(tile);
    }
	
//	@Override
	public static String tileNames( int tile ) {
		switch (tile) {
		case Terrain.GRASS:
			return "????????????";
		case Terrain.HIGH_GRASS:
			return "????????????";
		case Terrain.WATER:
			return "?????????????????????";
		case Terrain.STATUE_FROG:
		case Terrain.STATUE_SHAMAN:
		case Terrain.STATUE_BRUTE:
			return "?????????????????????????????????...???????????????";
		default:
			return Level.tileNames(tile);
		}
	}
	
//	@Override
	public static String tileDescs( int tile ) {
		switch (tile) {
		case Terrain.ENTRANCE:
			return "???????????????????????????";
		case Terrain.EXIT:
			return "???????????????????????????";
		case Terrain.HIGH_GRASS:
			return "??????????????????????????????????????????";
		case Terrain.WALL_DECO:
			return "??????????????????????????????????????????????????????????????????";
		case Terrain.BOOKSHELF:
			return "?????????????????????????????????????????????????????????????????????????????????";
        case Terrain.SHELF_EMPTY:
            return "?????????????????????????????????????????????";
		default:
			return Level.tileDescs(tile);
		}
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals( scene );
		addVisuals( this, scene );
	}
	
	public static void addVisuals( Level level, Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				scene.add( new Vein( i ) );
			}
		}
	}
	
	private static class Vein extends Group {
		
		private int pos;
		
		private float delay;
		
		public Vein( int pos ) {
			super();
			
			this.pos = pos;
			
			delay = Random.Float( 2 );
		}
		
		@Override
		public void update() {
			
			if (visible = Dungeon.visible[pos]) {
				
				super.update();
				
				if ((delay -= Game.elapsed) <= 0) {
					
					delay = Random.Float();
					
					PointF p = DungeonTilemap.tileToWorld( pos );
					((Sparkle)recycle( Sparkle.class )).reset( 
						p.x + Random.Float( DungeonTilemap.SIZE ), 
						p.y + Random.Float( DungeonTilemap.SIZE ) );
				}
			}
		}
	}
	
	public static final class Sparkle extends PixelParticle {
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan = 0.5f;
		}
		
		@Override
		public void update() {
			super.update();
			
			float p = left / lifespan;
			size( (am = p < 0.5f ? p * 2 : (1 - p) * 2) * 2 );
		}
	}
}