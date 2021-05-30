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
import com.ravenwolf.nnypdcn.items.weapons.criticals.BladeCritical;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;

public class Chakrams extends ThrowingWeaponLight {

    {
        name = "环刃";
        image = ItemSpriteSheet.CHAKRAM;
        critical=new BladeCritical(this, false, 2f);
    }

    public Chakrams() {
        this( 1 );
    }

    public Chakrams(int number) {
        super( 4 );
        quantity = number;
    }

    @Override
    public int baseAmount() {
        return 4;
    }

    public float getBackstabMod() {
        return 0.30f;
    }

    @Override
    public boolean returnOnHit(Char enemy){
        return  true;
    }

    @Override
    public String desc() {
        /*return "This razor-edged missile is made in such curious way that skilled user returns to " +
                "the hands of the thrower on successful hit.";*/
        return "在熟练的人手中，这个边缘带有锋利刀锋的圆盘能够在击中目标后直接返回其手中。"
                + "\n\n这件武器在对付未察觉你的敌人时候更为有效。";

    }
}
