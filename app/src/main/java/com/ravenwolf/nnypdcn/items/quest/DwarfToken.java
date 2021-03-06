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

public class DwarfToken extends Item {
	
	{
		name = "矮人徽记";
		image = ItemSpriteSheet.TOKEN;
		
		stackable = true;
		unique = true;
	}
	
	@Override
	public String info() {
		return
			"很多矮人和他们的造物都携带着这种小块金属，理由不详。兴许它是装饰品或什么身份识别牌。 矮人都挺奇怪的。";
	}
	
	@Override
	public int price() {
		return 0;
	}
}
