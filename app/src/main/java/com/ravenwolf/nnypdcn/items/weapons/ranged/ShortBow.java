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
package com.ravenwolf.nnypdcn.items.weapons.ranged;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Arrows;
import com.ravenwolf.nnypdcn.items.weapons.throwing.ThrowingWeaponAmmo;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.layers.AdditionalSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.layers.RangedWeaponSprite;

public class ShortBow extends RangedWeaponMissile {


	public int getAdditionalDrawId(){
		if ( Dungeon.hero.belongings.ranged instanceof Arrows)
			return AdditionalSprite.ADD_QUIVER;
		else
			return super.getAdditionalDrawId();
	}


	{
		name = "短弓";
		image = ItemSpriteSheet.BOW;
		drawId= RangedWeaponSprite.BOW;
		critical=new PierceCritical(this);
	}

	public ShortBow() {
		super( 1 );
	}

	@Override
	public int str(int bonus) {
		return super.str(bonus)-1;
	}

	@Override
	public boolean incompatibleWithShield() {
		return true;
	}

    @Override
    public Class<? extends ThrowingWeaponAmmo> ammunition() {
        return Arrows.class;
    }

	@Override
	public Type weaponType() {
		return Type.R_MISSILE;
	}

	@Override
	public String desc() {
		return "这种武器在一个熟练的弓箭手手中便可以发挥它最大的效果.";
	}
}
