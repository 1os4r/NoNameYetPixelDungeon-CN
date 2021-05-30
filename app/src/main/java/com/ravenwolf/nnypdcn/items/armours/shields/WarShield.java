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
package com.ravenwolf.nnypdcn.items.armours.shields;

import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.layers.ShieldSprite;


public class WarShield extends Shield {

	{
		name = "战盾";
		image = ItemSpriteSheet.SHIELD_WAR;
		drawId= ShieldSprite.SHIELD_WAR;
	}

	public WarShield() { super( 2 ); }

	@Override
	public int dr( int bonus ) {
		return super.dr(bonus)-2;
	}

	public float counterMod() {
		return 0.5f+0.03f*bonus;
	}


	@Override
	public String desc() {
		//return "This shield is smaller than a kite shield, offering more maneuverability while blocking and making easier to leave your opponent in a bad position.";
		return "这种护盾设计在两边都有凹痕，在格挡的同时提供了更多的机动性，并且更适合弹反敌人";

	}
}
