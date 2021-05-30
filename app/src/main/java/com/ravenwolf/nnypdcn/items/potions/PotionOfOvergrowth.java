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
package com.ravenwolf.nnypdcn.items.potions;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.blobs.Blob;
import com.ravenwolf.nnypdcn.actors.blobs.Overgrowth;
import com.ravenwolf.nnypdcn.items.wands.CharmOfThorns;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PotionOfOvergrowth extends Potion {

    public static final int BASE_VAL	= 250;
    public static final int MODIFIER	= 25;

	{
		name = "再生药剂";
        shortName = "Ov";
        harmful = true;
        icon=12;
	}

    @Override
    public void shatter( int cell ) {

        GameScene.add(Blob.seed(cell, BASE_VAL + MODIFIER * alchemySkill(), Overgrowth.class));


        boolean mapUpdated = false;

        for (int n : Level.NEIGHBOURS5) {

            if( n == 0 || Random.Float() < 0.75f ) {

                int i = cell + n;
                int c = Dungeon.level.map[i];

                switch (c) {
                    case Terrain.EMPTY:
                    case Terrain.EMPTY_DECO:
                    case Terrain.EMBERS:

                        Level.set(i, Terrain.GRASS);
                        mapUpdated = true;
                        break;

                    case Terrain.GRASS:

                        Level.set(i, Terrain.HIGH_GRASS);
                        mapUpdated = true;
                        break;

                }
            }
        }

        if (mapUpdated) {
            GameScene.updateMap();
            Dungeon.observe();
        }

        if (Dungeon.visible[cell]) {
            setKnown();
            splash(cell);
            Sample.INSTANCE.play(Assets.SND_SHATTER);
        }

        int stats = 6+Dungeon.chapter()*4;
        int level = Dungeon.chapter()*2;

        // first we check the targeted tile
        if( CharmOfThorns.Thornvine.spawnAt( stats, level, cell ) == null ) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();

            for (int n : Level.NEIGHBOURS8) {
                int pos = cell + n;
                if(  !Level.solid[ pos ] && !Level.chasm[ pos ] && Actor.findChar( pos ) == null ){
                    candidates.add( pos );
                }
            }

            if ( candidates.size() > 0 )
                CharmOfThorns.Thornvine.spawnAt( stats, level, candidates.get( Random.Int( candidates.size() ) ) );
        }
    }
	
	@Override
	public String desc() {
		return
			"扔出这瓶神奇的混合药剂会使目标位置的草生植物快速的生长，可以拿来缠绕周围的敌人，并阻挡视野，同时还会生成刺藤来阻挡敌人的前进";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 45 * quantity : super.price();
	}
}
