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
package com.ravenwolf.nnypdcn.items.armours.glyphs;

import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypdcn.items.armours.Armour;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSprite.Glowing;

public class Protection extends Armour.Glyph {


	@Override
	public Glowing glowing() {
		return CYAN;
	}

    @Override
    protected String name_p() {
        return "庇护%s";
    }

    @Override
    protected String name_n() {
        return "衰败%s";
    }

    @Override
    protected String desc_p() {
        return "被击中时会赋予你圣光庇护";
    }

    @Override
    protected String desc_n() {
        return "被击中时会衰弱自己";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {
        if (isShield)
            BuffActive.addFromDamage(defender, Shielding.class, damage*2);
        else
            BuffActive.addFromDamage(defender, Shielding.class, damage);
        return true;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {
        BuffActive.addFromDamage( attacker, Shielding.class, damage );
        return true;
    }


}
