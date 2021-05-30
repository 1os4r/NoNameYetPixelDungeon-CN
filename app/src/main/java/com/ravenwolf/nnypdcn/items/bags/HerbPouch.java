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
package com.ravenwolf.nnypdcn.items.bags;

import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.herbs.Herb;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.ui.Icons;

public class HerbPouch extends Bag {

	{
		name = "药草包";
		image = ItemSpriteSheet.POUCH;
		
		size = 14;
        visible = false;
        unique = true;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Herb;
	}
	
	@Override
    public Icons icon() {
        return Icons.HERB_POUCH;
    }

	@Override
	public int price() {
		return 50;
	}
	
	@Override
	public String info() {
		return
			"这个丝织小袋能够让你在其中存放任意数量的草药，非常方便。";
	}

    @Override
    public boolean doPickUp( Hero hero ) {

        return hero.belongings.getItem( HerbPouch.class ) == null && super.doPickUp( hero ) ;

    }
}
