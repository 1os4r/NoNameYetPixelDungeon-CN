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
package com.ravenwolf.nnypdcn.items.herbs;

import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.bonuses.Mending;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.items.potions.PotionOfMending;
import com.ravenwolf.nnypdcn.items.rings.RingOfSatiety;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;


public class SungrassHerb extends Herb {
    {
        name = "阳春草";
        image = ItemSpriteSheet.HERB_SUNGRASS;

        alchemyClass = PotionOfMending.class;
        message = "尝起来酸酸甜甜的";
    }

    @Override
    public void onConsume( Hero hero ) {


        BuffActive.add( hero, Mending.class, 6*(hero.ringBuffs( RingOfSatiety.Satiety.class )));

        super.onConsume( hero );
    }

    @Override
    public String desc() {
        return "阳春草拥有不错的治疗效果，动物们通常会吃掉它来清除体内的毒素。他通常被用来炼制治疗药剂。";
    }
}
