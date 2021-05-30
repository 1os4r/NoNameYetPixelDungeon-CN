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
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypdcn.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;

public class Darkness extends Blob {
	
	@Override
	protected void evolve() {
		super.evolve();

        Blob blob = Dungeon.level.blobs.get( Sunlight.class );

        if (blob != null) {

            int par[] = blob.cur;

            for (int i=0; i < LENGTH; i++) {

                if (cur[i] > 0) {
                    blob.volume -= par[i];
                    par[i] = 0;
                }
            }
        }

		Char ch;
		for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0 && (ch = Actor.findChar(i)) != null) {
                if (ch.hasBuff(Blinded.class) )
                    BuffActive.add( ch, Blinded.class, TICK );
                else
                    BuffActive.add( ch, Blinded.class, TICK*2 );
            }
        }
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );
		
		emitter.pour( Speck.factory( Speck.DARKNESS ), 0.2f );
	}
	
	@Override
	public String tileDesc() {
		return "一片乌黑的浓烟在此盘旋，阻挡着你的视线。";
	}
}
