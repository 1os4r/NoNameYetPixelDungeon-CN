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
package com.ravenwolf.nnypdcn.actors.blobs;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Bestiary;
import com.ravenwolf.nnypdcn.actors.mobs.Elemental;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.Lightning;
import com.ravenwolf.nnypdcn.visuals.effects.particles.RainParticle;
import com.ravenwolf.nnypdcn.visuals.effects.particles.SparkParticle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Random;

public class Thunderstorm extends Blob {

    public Thunderstorm() {
        super();

        name = "雷暴";
    }

	@Override
	protected void evolve() {

        super.evolve();

        if (volume > 0) {

            boolean mapUpdated = false;

            Blob blob = Dungeon.level.blobs.get( Fire.class );

            if (blob != null) {

                int par[] = blob.cur;

                for (int i=0; i < LENGTH; i++) {

                    if (cur[i] > 0) {
                        blob.volume -= par[i];
                        par[i] = 0;
                    }
                }
            }

            int count = 0;
            int humidity[] = new int[LENGTH];

            for (int i=0; i < LENGTH; i++) {

                if (cur[i] > 0) {

                    humidity[i] = 2;

                    for (int n : Level.NEIGHBOURS8) {
                        if (Dungeon.level.map[i + n] == Terrain.WATER) {
                            humidity[i]++;
                        }
                    }
                }
            }

            for (int i=0; i < LENGTH; i++) {

                if (cur[i] > 0) {

                    if (Random.Int(20) < humidity[i]
                            && !Level.water[i]
                            && !Level.important[i]
                            && !Level.solid[i]
                            && !Level.chasm[i]
                            ) {

                        int oldTile = Dungeon.level.map[i];
                        Level.set(i, Terrain.WATER);

//                        if (Dungeon.visible[i]) {
                            mapUpdated = true;
//                            GameScene.updateMap( i );
                            GameScene.discoverTile( i, oldTile );
//                        }
                    }

                    Char ch = Actor.findChar(i);

                    if (ch != null) {
                        if( ch instanceof Elemental ) {
                            ch.damage( Random.IntRange( 1, (int)Math.sqrt( ch.HT / 2 + 1 ) ), this, null );
                        } else {
                            Buff.detach(ch, Burning.class);
                        }
                    }

                    count++;

                }
            }

            if (mapUpdated) {
                GameScene.updateMap();
                Dungeon.observe();
            }

            int amount = count / 5;

            boolean viewed = false;
            boolean heared = false;
            boolean affected[] = new boolean[LENGTH];

            for (int i=0; i < amount; i++) {

                int cell = Random.Int( LENGTH );

                if( cur[cell] > 0 && !affected[cell] && !Level.solid[cell] && !Level.chasm[cell] ) {

                    amount--;

                    thunderstrike( cell, this );

                    for( int n : Level.NEIGHBOURS9 ) {
                        affected[ cell + n ] = true;
                    }

                    if( Dungeon.visible[ cell ] ) {

                        viewed = true;

                    } else if ( Level.distance( cell, Dungeon.hero.pos ) <= 4 ) {

                        heared = true;

                    }
                }
            }

            if ( viewed && Dungeon.hero.isAlive() ) {

                viewed();

            } else if ( heared && Dungeon.hero.isAlive() ) {

                listen();

            }
        }
	}

    public static void viewed() {
        GameScene.flash(0x888888);
        Sample.INSTANCE.play(Assets.SND_BLAST);
        Camera.main.shake(3, 0.3f);
        Dungeon.hero.interrupt();
    }

    public static void listen() {
        GLog.i("你听见了不远处响起的雷声。");
        Sample.INSTANCE.play(Assets.SND_BLAST, 1, 1, Random.Float(1.8f, 2.25f));
        Camera.main.shake(2, 0.2f);
    }

    public static void thunderstrike( int cell, Blob blob ) {

        Emitter emitter = CellEmitter.get( cell );

        boolean visible = false;
        boolean cross = Random.Int( 2 ) == 0;

        int[] tiles = cross ? Level.NEIGHBOURS5 : Level.NEIGHBOURSX ;

        for( int n : tiles ) {

            Char ch = Actor.findChar( cell + n );

            if (ch != null) {

                int power = Random.IntRange(ch.HT / 3, ch.HT * 2 / 3);

                if( Bestiary.isBoss(ch) ) {
                    power = power / 4;
                } else if ( ch instanceof Hero ) {
                    power = power / 2;
                }


                ch.damage(n == 0 ? power : power / 2, blob, Element.SHOCK);

            }

            visible = visible || Dungeon.visible[ cell ];

        }

        if( visible ) {

            int[] points1 = new int[2];
            int[] points2 = new int[2];

            if( cross ) {

                points1[0] = cell - Level.WIDTH;
                points1[1] = cell + Level.WIDTH;

                points2[0] = cell - 1;
                points2[1] = cell + 1;

            } else {

                points1[0] = cell - Level.WIDTH - 1;
                points1[1] = cell + Level.WIDTH + 1;

                points2[0] = cell - Level.WIDTH + 1;
                points2[1] = cell + Level.WIDTH - 1;

            }

            emitter.parent.add( new Lightning( points1, 2, null ) );
            emitter.parent.add( new Lightning( points2, 2, null ) );

            emitter.burst( SparkParticle.FACTORY, Random.Int( 4, 6 ) );

        }

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.distance( cell, mob.pos ) <= 4 ) {
                mob.beckon(cell);
            }
        }
    }
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use(emitter);
		emitter.start( RainParticle.FACTORY, 0.5f, 0 );

	}
	
	@Override
	public String tileDesc() {
		return "雷雨云正漂浮在这片区域的上方，对其降下暴雨和阵雷。";
	}
}
