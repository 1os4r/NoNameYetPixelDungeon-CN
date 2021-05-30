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

import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;


public class Boomerangs extends ThrowingWeaponSpecial {

	{
		name = "回旋镖";
		image = ItemSpriteSheet.BOOMERANG;
	}

	public Boomerangs() {
		this( 1 );
	}

	public Boomerangs(int number) {
        super( 3 );
		quantity = number;
	}

	@Override
	protected boolean returnOnMiss(){
		return true;
	}

    @Override
    public int proc( Char attacker, Char defender, int damage ) {
        damage=super.proc(attacker, defender, damage);

		BuffActive.addFromDamage(defender, Dazed.class, damage*2);

        return damage;
    }

	@Override
	public String desc() {
		return 
			"在没有命中目标时这些弯曲的回旋镖会自动飞回投掷者手中。因它的设计并不锋利" + 
            "所以他并不能造成大量的伤害，但精准的命中敌人时有可能会让敌人陷入眩晕之中。";
	}
}
