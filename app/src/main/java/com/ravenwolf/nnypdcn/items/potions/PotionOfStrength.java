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
package com.ravenwolf.nnypdcn.items.potions;

import com.ravenwolf.nnypdcn.Badges;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;
import com.ravenwolf.nnypdcn.visuals.ui.QuickSlot;

public class PotionOfStrength extends Potion {

	{
		name = "力量药剂";
        shortName = "St";
		icon=10;
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();

        hero.STR++;
        hero.strBonus++;

		int hpBonus = 2 ;

        int restore = hero.HT - hero.HP;

        hero.HP = hero.HT += hpBonus;

        if( restore > 0 ) {
            hero.sprite.showStatus(CharSprite.POSITIVE, "%+dHP", restore);
        }

        hero.sprite.showStatus( CharSprite.POSITIVE, "+1力量 +%d生命上限", hpBonus );

        hero.sprite.emitter().burst(Speck.factory(Speck.MASTERY), 12);

        Buff.detach(hero, Withered.class);

        GLog.p("新生的力量在你的体内喷薄而出" );

        QuickSlot.refresh();

		Badges.validateStrengthAttained();
	}
	
	@Override
	public String desc() {
		return
			"这瓶强力的药剂会洗刷你的肌肉，永久性的增强你的力量，并恢复所有的生命值";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 75 * quantity : super.price();
	}
}
