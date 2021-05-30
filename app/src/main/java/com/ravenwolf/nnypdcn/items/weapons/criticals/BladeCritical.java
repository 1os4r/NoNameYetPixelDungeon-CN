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

package com.ravenwolf.nnypdcn.items.weapons.criticals;

import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Bleeding;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;



public class BladeCritical extends Critical {

    public BladeCritical(Weapon weap){
        super(weap);
    }

    public BladeCritical(Weapon weap, boolean betterCriticals,float criticalModifier){
        super(weap,betterCriticals,criticalModifier);
    }

    @Override
    public int proc_crit(Char attacker, Char defender, int damage ) {
        boolean improvedCrit= isBetterCriticals();
        if (defender.isEthereal()) {
            defender.sprite.showStatus(CharSprite.NEGATIVE, "sliced");
            if (improvedCrit)
                damage += weap.min()*1.5f;
            else
                damage += weap.min();
        } else if (defender.HP>damage){
            if (improvedCrit) {
                damage += weap.min();
                BuffActive.addFromDamage(defender, Crippled.class, damage);
            }else {
                damage += weap.min()/2;
                BuffActive.addFromDamage(defender, Bleeding.class, damage);
            }
        }
        return damage;
    }


}
