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
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.visuals.effects.Lightning;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Shocking extends Weapon.Enchantment {

    private ArrayList<Char> affected = new ArrayList<Char>();

    private int[] points = new int[20];
    private int nPoints;

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    @Override
    protected String name_p() {
        return "电击%s";
    }

    @Override
    protected String name_n() {
        return "触电%s";
    }

    @Override
    protected String desc_p() {
        return "攻击时会电击敌人";
    }

    @Override
    protected String desc_n() {
        return "攻击时会电击自己";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        points[0] = attacker.pos;
        nPoints = 1;

        affected.clear();
        affected.add(attacker);

        hit( defender, Random.IntRange(damage / 3, damage / 2) );

        attacker.sprite.parent.add(new Lightning(points, nPoints, null));

        return true;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {
        attacker.damage(Random.IntRange(damage / 4, damage / 3), this, Element.SHOCK);
        return true;
    }

    private void hit( Char ch, int damage ) {

        if (damage < 1) {
            return;
        }

        affected.add(ch);
        ch.damage(damage, this, Element.SHOCK);
        points[nPoints++] = ch.pos;

        HashSet<Char> ns = new HashSet<Char>();
        for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
            Char n = Actor.findChar( ch.pos + Level.NEIGHBOURS8[i] );
            if (n != null && !affected.contains( n )) {
                ns.add( n );
            }
        }

        if (ns.size() > 0) {
            hit(  Random.element( ns ), Random.IntRange( damage / 2, damage ) );
        }
    }
}
