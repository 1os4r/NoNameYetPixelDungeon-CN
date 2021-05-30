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

import com.ravenwolf.nnypdcn.items.weapons.criticals.BladeCritical;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Bullets;
import com.ravenwolf.nnypdcn.items.weapons.throwing.ThrowingWeaponAmmo;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.layers.RangedWeaponSprite;

public class Arquebuse extends RangedWeaponFlintlock {

	{
		name = "火绳枪";
		image = ItemSpriteSheet.ARQUEBUS;
		drawId= RangedWeaponSprite.ARQUEBUS;
		critical=new BladeCritical(this, true, 1.5f);
	}

	public Arquebuse() {
		super( 3 );
	}

	@Override
	public boolean incompatibleWithShield() {
		return true;
	}

    @Override
    public Class<? extends ThrowingWeaponAmmo> ammunition() {
        return Bullets.class;
    }

	@Override
	public Type weaponType() {
		return Type.R_FLINTLOCK;
	}
	
	@Override
	public String desc() {
		return "曾作为军中的常备武器，但如今却十分稀缺。其穿透护甲的射击可使任何敌人都为之胆寒。"
				+"\n\n这种武器更擅长于暴击敌人，并且会造成额外的效果。";
	}
}
