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

import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.items.weapons.criticals.BladeCritical;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Bullets;
import com.ravenwolf.nnypdcn.items.weapons.throwing.ThrowingWeaponAmmo;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.layers.RangedWeaponSprite;
import com.watabou.utils.Random;

public class Handcannon extends RangedWeaponFlintlock {

	{
		name = "火铳炮";
		image = ItemSpriteSheet.HANDCANNON;
		drawId= RangedWeaponSprite.HANDCANNON;
		critical=new BladeCritical(this, true, 1f);
	}

	public Handcannon() {
		super( 5 );
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
		return "尽管最初燧发武器是人类的造物，矮人们迅速适应了这个全新概念并在其上加以改进。" + 
		"铳炮本身非常沉重，并且需要大量火药用于装填，但它的杀伤力可谓冠绝任何武器。"+
				"\n\n这种武器的暴击效果更加强大.";
	}


	@Override
	public int proc(Char attacker, Char defender, int damage ) {
		damage=super.proc(attacker, defender, damage);
		if (Random.Int(defender.HT) < damage/2) {
			if (defender != null) {
				defender.knockBack( attacker, damage);
			}
		}
		return damage;
	}

}
