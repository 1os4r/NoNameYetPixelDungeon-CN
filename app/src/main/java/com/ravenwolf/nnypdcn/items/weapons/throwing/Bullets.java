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

import com.ravenwolf.nnypdcn.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;

public class Bullets extends ThrowingWeaponAmmo {

	{
		name = "铅弹";
		image = ItemSpriteSheet.PELLET;
		critical=new BluntCritical(this);
	}

	public Bullets() {
		this( 1 );
	}

	public Bullets(int number) {
        super( 1 );
		quantity = number;
	}

    @Override
    public int image() {
        return quantity > 1 ? ItemSpriteSheet.PELLETS : image;
//        return (isEquipped( Dungeon.hero ) || Dungeon.hero.belongings.backpack.items.contains(this) ) ? ItemSpriteSheet.PELLETS : image;
    }
	
	@Override
	public String desc() {
		return 
			"这些小巧的铅制弹药非常适合当做投索或火器的弹药。";
	}
}
