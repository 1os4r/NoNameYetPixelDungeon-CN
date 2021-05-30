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
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;
import com.ravenwolf.nnypdcn.visuals.ui.BuffIndicator;

public class Frozen extends Debuff {

    @Override
    public Element buffType() {
        return Element.FROST;
    }

    @Override
    public String toString() {
        return "冰冻";
    }

    @Override
    public String statusMessage() { return "冰冻"; }

    @Override
    public String playerMessage() { return "你被冻住了！"; }

    @Override
    public int icon() {
        return BuffIndicator.FROZEN;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.FROZEN );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.FROZEN );
    }

    @Override
    public String description() {
        return "你被严寒冰冻了，在此期间你不能做任何行动，直至寒冰破碎，不过这些冰块依旧会在你受到物理伤害时碎裂开。火焰也会融化它。";
    }


    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            Stun.freeze(target);
            Buff.detach( target, Burning.class );
            if( target instanceof Hero ){
                Hero hero = (Hero)target;
                Chilled.freezeHeroBackpack(hero);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean attachOnLoad( Char target ) {
        target.stunned = true;
        return super.attachOnLoad(target);
    }

    @Override
    public void detach() {
        super.detach();
        Chilled buffChilled = target.buff(Chilled.class);
        if(buffChilled!=null)
            buffChilled.resetFrezzeCounter();
        Stun.unfreeze( target );
    }
}
