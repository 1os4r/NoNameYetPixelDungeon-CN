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
package com.ravenwolf.nnypdcn.actors.buffs.debuffs;

import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;
import com.ravenwolf.nnypdcn.visuals.ui.BuffIndicator;

public class Tormented extends Debuff {
	
	public final static String TXT_CANNOT_ATTACK = "你现在很恐慌，导致无法进行攻击";

    @Override
    public Element buffType() {
        return Element.MIND;
    }

    @Override
    public String toString() {
        return "放逐";
    }

    @Override
    public String statusMessage() { return "放逐"; }

    @Override
    public String playerMessage() { return "你的精神正被恐惧和痛苦感占领！"; }

    @Override
    public int icon() {
        return BuffIndicator.TERROR;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.TORMENTED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.TORMENTED );
    }

    @Override
    public String description() {
        return "令人恐惧的魔法直接进入了你的精神之中，制造着痛苦与恐惧的感觉。" +
                "你的远程攻击几乎无法瞄准，近战攻击敌人更不可能。你现在要做的就是，逃跑！";
    }

    @Override
    public boolean act() {

        target.damage( (int) Math.sqrt( target.totalHealthValue() *1.2f)-(int) Math.sqrt( target.currentHealthValue()*0.8f)  + 1, this, Element.MIND );

        return super.act();

    }

//    private static final String OBJECT	= "object";
//
//    @Override
//    public void storeInBundle( Bundle bundle ) {
//        super.storeInBundle( bundle );
//        bundle.put( OBJECT, object );
//
//    }
//
//    @Override
//    public void restoreFromBundle( Bundle bundle ) {
//        super.restoreFromBundle(bundle);
//        object = bundle.getInt( OBJECT );
//    }
}
