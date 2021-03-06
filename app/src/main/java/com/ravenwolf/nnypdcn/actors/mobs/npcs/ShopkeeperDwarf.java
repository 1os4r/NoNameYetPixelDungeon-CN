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
package com.ravenwolf.nnypdcn.actors.mobs.npcs;

import com.ravenwolf.nnypdcn.misc.utils.Utils;
import com.ravenwolf.nnypdcn.visuals.sprites.ShopkeeperDwarfSprite;

public class ShopkeeperDwarf extends Shopkeeper {

	private static final String TXT_GREETINGS = "虽然我看不惯你们这些人类，\n不过钱我还是欢迎的。";
	
	{
		name = "矮人店主";
		spriteClass = ShopkeeperDwarfSprite.class;
	}

    @Override
    protected void greetings() {
        yell( Utils.format(TXT_GREETINGS) );
    }
	
	@Override
	public String description() {
		return 
			"看起来这名矮人在这开了家商店。他很惊讶于你的突然来访，不过比起惊讶他脸上展现的更多是不满。";
	}
}
