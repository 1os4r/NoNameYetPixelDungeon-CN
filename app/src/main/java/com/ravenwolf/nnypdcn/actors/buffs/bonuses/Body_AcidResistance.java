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
package com.ravenwolf.nnypdcn.actors.buffs.bonuses;

import com.ravenwolf.nnypdcn.visuals.ui.BuffIndicator;

public class Body_AcidResistance extends Bonus {

    @Override
    public int icon() {
        return BuffIndicator.BODY_RESISIT;
    }

    @Override
    public String toString() {
        return "增加抗性";
    }

    @Override
    public String statusMessage() { return "增加抗性"; }

    @Override
    public String playerMessage() { return "你感觉很清爽！"; }


    @Override
    public String description() {
        return "改善了你的新陈代谢，并且增加你对酸蚀类的抗性。";
    }

}