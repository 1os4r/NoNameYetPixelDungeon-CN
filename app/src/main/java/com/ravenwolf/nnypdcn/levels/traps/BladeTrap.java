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
package com.ravenwolf.nnypdcn.levels.traps;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.Wound;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class BladeTrap extends Trap {

    // FIXME

    public static BladeTrap TRAP = new BladeTrap();

	public static void trigger( int pos, Char ch ) {

		if (ch != null) {

            int power = 10 + Dungeon.chapter() * 5;

			int damage = ch.absorb( Random.IntRange( power / 2 , power ));

            ch.damage( damage, TRAP, Element.PHYSICAL );

            Sample.INSTANCE.play(Assets.SND_HIT);

			Wound.hit( ch );

			if (ch.isAlive() ) {
				BuffActive.addFromDamage(ch, Crippled.class, damage*2);
			}

		} else {

			Wound.hit( pos );

		}
	}



}
