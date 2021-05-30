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
package com.ravenwolf.nnypdcn.items.quest;

import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;

public class DriedRose extends Item {
	
	{
		name = "干枯玫瑰";
		image = ItemSpriteSheet.ROSE;
		
		unique = true;
	}
	
	@Override
	public String info() {
		return
			"这朵玫瑰很久以前就已经彻底干枯，但不知为何其上的花瓣仍然齐整。";
	}
}
