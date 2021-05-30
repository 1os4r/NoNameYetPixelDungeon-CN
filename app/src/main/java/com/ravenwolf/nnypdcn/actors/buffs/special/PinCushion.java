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

package com.ravenwolf.nnypdcn.actors.buffs.special;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypdcn.items.weapons.throwing.ThrowingWeaponAmmo;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;


public class PinCushion extends Buff {

    private ArrayList<ThrowingWeapon> items = new ArrayList<ThrowingWeapon>();

    public void stick(ThrowingWeapon projectile){
        for (Item item : items){
            if (item.getClass()==projectile.getClass()){
                item.quantity(item.quantity() + projectile.quantity());
                return;
            }
        }
        items.add(projectile);
    }

    @Override
    public void detach() {
        for (Item item : items)
            Dungeon.level.drop( item, target.pos).sprite.drop();
        super.detach();
    }


    public int countArrows() {
        int count=0;
        for (Item item : items) {
            if (item instanceof ThrowingWeaponAmmo)
                count+=item.quantity;
        }
        return count;
    }

    private static final String ITEMS = "items";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put( ITEMS , items );
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        items = new ArrayList<ThrowingWeapon>((Collection<ThrowingWeapon>)((Collection<?>)bundle.getCollection( ITEMS )));
        super.restoreFromBundle( bundle );
    }
}
