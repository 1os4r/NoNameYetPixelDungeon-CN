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
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.watabou.utils.Random;

public abstract class Critical {

    private boolean betterCriticals=false;
    private float criticalModifier=1f;
    protected Weapon weap;

    public Critical(Weapon weap){
        this.weap=weap;
    }

    public Critical(Weapon weap,boolean betterCriticals,float criticalModifier ){
        this.weap=weap;
        this.betterCriticals=betterCriticals;
        this.criticalModifier=criticalModifier;
    }


    public int proc(Char attacker, Char defender, int damage ){
        if (crit(attacker, defender, damage)) {

            return proc_crit(attacker, defender, damage);
        }
        return damage;
    }

    public abstract int proc_crit(Char attacker, Char defender, int damage );


    protected boolean crit(Char attacker, Char defender, int damage){

        int weapValue=(int)(4* criticalModifier)+1*weap.bonus;
        float bonus=attacker.awareness();

        float critChance=weapValue*bonus;
        critChance=critChance/(46+critChance);
        //GLog.i("Chance: %.3f",critChance);
        return Random.Float() < critChance;

    }


    public boolean isBetterCriticals() {
        return betterCriticals;
    }


    public float getCriticalModifier() {
        return criticalModifier;
    }

}
