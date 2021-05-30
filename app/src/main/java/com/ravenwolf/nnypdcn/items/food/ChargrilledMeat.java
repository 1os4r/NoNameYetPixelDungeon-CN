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
package com.ravenwolf.nnypdcn.items.food;

import com.ravenwolf.nnypdcn.actors.buffs.special.Satiety;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;

public class ChargrilledMeat extends Food {

	{
		name = "烤肉";
		image = ItemSpriteSheet.STEAK;
		energy = Satiety.MAXIMUM * 0.25f;
        message = "吃起来不错！";
	}
	
	@Override
	public String desc() {
		return "看起来像快上等的肉排。";
	}
	
	@Override
	public int price() {
		return 10 * quantity;
	}
	
	public static Food cook( MysteryMeat ingredient ) {
		ChargrilledMeat result = new ChargrilledMeat();
		result.quantity = ingredient.quantity();
		return result;
	}
}
