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
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;

public class PierceCritical extends Critical {

    public PierceCritical(Weapon weap){
        super(weap);
    }

    public PierceCritical(Weapon weap, boolean betterCriticals,float criticalModifier){
        super(weap,betterCriticals,criticalModifier);
    }

    @Override
    public int proc_crit(Char attacker, Char defender, int damage ) {
        if (defender.armorClass()!=0) { //cannot proc on enemies without armor
            int pierceDamage = attacker.damageRoll();
            //shitty way to simulate armor pierce
            if (pierceDamage > damage) {
                damage = (pierceDamage+damage)/2;
            }
            defender.sprite.showStatus(CharSprite.NEGATIVE, "穿甲");
            if (isBetterCriticals()) {
                BuffActive.addFromDamage( defender, Bleeding.class, damage );
            }

        }
        return damage;
    }

}
