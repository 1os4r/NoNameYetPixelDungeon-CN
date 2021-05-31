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
package com.ravenwolf.nnypdcn.items.weapons.throwing;

import com.ravenwolf.nnypdcn.items.weapons.criticals.BladeCritical;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;

public class Tomahawks extends ThrowingWeaponHeavy {

	{
		name = "飞斧";
		image = ItemSpriteSheet.TOMAHAWK;
		critical=new BladeCritical(this, false, 3f);
	}
	
	public Tomahawks() {
		this( 1 );
	}
	
	public Tomahawks(int number) {
        super( 2 );
		quantity = number;
	}
	
	@Override
	public String desc() {
		return 
			"这件飞斧并不是很重，但是依然需要强大的力量才能正常使用"
					+"\n\n这种武器更擅长于暴击敌人。";
	}
}
