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
package com.ravenwolf.nnypdcn.items.weapons.enchantments;

import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Blinded;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Disrupted;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.particles.PurpleParticle;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Disrupting extends Weapon.Enchantment {

    @Override
    public Glowing glowing() {
        return YELLOW;
    }

    @Override
    protected String name_p() {
        return "扰乱%s";
    }

    @Override
    protected String name_n() {
        return "迷乱%s";
    }

    @Override
    protected String desc_p() {
        return "赋予武器扰乱敌人行动的魔法";
    }

    @Override
    protected String desc_n() {
        return "攻击时会对自己释放强烈的闪光";
    }

	@Override
	protected boolean proc_p( Char attacker, Char defender, int damage ) {

        int dmg=Random.IntRange(damage / 3, damage / 2);
        if (dmg>0) {
            CellEmitter.center(defender.pos).burst(PurpleParticle.BURST, dmg);
            defender.damage(dmg, this, Element.ENERGY);
            if (defender.isAlive() && defender.isMagical())
                BuffActive.addFromDamage(defender, Disrupted.class, damage * 2);
            return true;
        }else
            return false;
	}

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {
        int dmg=Random.IntRange(damage / 4, damage / 3);
        attacker.damage(dmg, this, Element.ENERGY);
        CellEmitter.center( attacker.pos ).burst( PurpleParticle.BURST,dmg );
        BuffActive.addFromDamage( attacker, Blinded.class, damage * 2 );
        return true;
    }
}
