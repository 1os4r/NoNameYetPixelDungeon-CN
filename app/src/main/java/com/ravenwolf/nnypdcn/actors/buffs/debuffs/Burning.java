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

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.blobs.Blob;
import com.ravenwolf.nnypdcn.actors.blobs.Fire;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.items.Heap;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.food.ChargrilledMeat;
import com.ravenwolf.nnypdcn.items.food.MysteryMeat;
import com.ravenwolf.nnypdcn.items.herbs.Herb;
import com.ravenwolf.nnypdcn.items.scrolls.Scroll;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;
import com.ravenwolf.nnypdcn.visuals.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Burning extends Debuff {

	private static final String TXT_BURNS_UP		= "%s被烧毁了！";

    @Override
    public Element buffType() {
        return Element.FLAME;
    }

    @Override
    public String toString() {
        return "燃烧";
    }

    @Override
    public String statusMessage() { return "燃烧"; }

    @Override
    public String playerMessage() { return "你身上着火了！快去找水！"; }

    @Override
    public int icon() {
        return BuffIndicator.BURNING;
    }

    @Override
    public void applyVisual() {

        if (target.sprite.visible) {
            Sample.INSTANCE.play( Assets.SND_BURNING );
        }

        target.sprite.add( CharSprite.State.BURNING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BURNING );
    }

    @Override
    public String description() {
        return "火焰覆盖了你的身体! 在燃烧的状态下你会持续受到伤害，并且它还可能会烧毁你身上的易燃物品，敌人同样也更容易发现你";
    }
	
	@Override
	public boolean act() {
        Invisibility.dispel(target);
        target.damage(
                Random.Int( (int)Math.sqrt(
                        target.totalHealthValue() * 1.5f
                ) ) + 1, this, Element.FLAME_PERIODIC
        );

        Blob blob = Dungeon.level.blobs.get( Burning.class );
//            Blob blob2 = Dungeon.level.blobs.get( Miasma.class );

        if (Level.flammable[target.pos] && ( blob == null || blob.cur[ target.pos ] <= 0 )) {
//            if (Level.flammable[target.pos] || blob1 != null && blob1.cur[target.pos] > 0 || blob2 != null && blob2.cur[target.pos] > 0) {
            GameScene.add(Blob.seed(target.pos, 1, Fire.class));
        }

        if ( target instanceof Hero ) {

            Item item = ((Hero) target).belongings.randomUnequipped();

            if (item instanceof Scroll || item instanceof Herb) {

                item = item.detach(((Hero) target).belongings.backpack);
                GLog.w(TXT_BURNS_UP, item.toString());

                Heap.burnFX(target.pos);

            } else if (item instanceof MysteryMeat) {

                item = item.detach(((Hero) target).belongings.backpack);
                ChargrilledMeat steak = new ChargrilledMeat();
                if (!steak.collect(((Hero) target).belongings.backpack)) {
                    Dungeon.level.drop(steak, target.pos).sprite.drop();
                }
                GLog.w(TXT_BURNS_UP, item.toString());

                Heap.burnFX(target.pos);

            }
        }

        if ( !target.isAlive() || Level.water[ target.pos ] && !target.flying ) {
            detach();
            return true;
        }

		return super.act();
	}

    @Override
    public boolean attachTo( Char target ) {

        if (super.attachTo( target )) {

            Invisibility.dispel(target);
            Buff.detach( target, Ensnared.class );
            Buff.detach( target, Chilled.class );

            return true;

        } else {

            return false;

        }
    }

}
