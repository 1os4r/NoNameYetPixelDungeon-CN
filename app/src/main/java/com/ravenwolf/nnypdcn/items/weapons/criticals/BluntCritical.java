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
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Stun;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;


public class BluntCritical extends Critical {

    public BluntCritical(Weapon weap){
        super(weap);
    }

    public BluntCritical(Weapon weap, boolean betterCriticals,float criticalModifier){
        super(weap,betterCriticals,criticalModifier);
    }


    @Override
    public int proc_crit(Char attacker, Char defender, int damage ) {

        if(defender.isSolid()){
            defender.sprite.showStatus( CharSprite.NEGATIVE, "重击" );
            if (isBetterCriticals())
                damage += weap.min()*1.5f;
            else
                damage += weap.min();
        }
        else if (!defender.isEthereal() && defender.HP>damage){
            damage += weap.min()/2;
            if (isBetterCriticals() && !defender.hasBuff(Stun.class)){//prevent stunlock
                BuffActive.add( defender, Stun.class, 1f);
            }
            BuffActive.addFromDamage( defender, Dazed.class, damage *2);
        }
        return damage;
    }

    protected boolean crit(Char attacker, Char defender, int damage){
       boolean crit=super.crit(attacker, defender, damage);
       //double roll against solid enemies
       if (defender.isSolid() && !crit)
           return super.crit(attacker, defender, damage);
       return crit;
    }

}
