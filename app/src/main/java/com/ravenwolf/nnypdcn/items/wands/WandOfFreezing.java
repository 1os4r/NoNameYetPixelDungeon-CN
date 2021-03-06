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
package com.ravenwolf.nnypdcn.items.wands;

import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.MagicMissile;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfFreezing extends /*WandUtility*/WandCombat {

	{
		name = "寒冰法杖";
		image = ItemSpriteSheet.WAND_ICEBARRIER;
	}

	@Override
	protected void cursedProc(Hero hero){
		int dmg=hero.absorb( damageRoll(), true )/2;
		hero.damage(dmg, this, Element.FROST);
	}

	@Override
	public int basePower() {
		return super.basePower() -2;
	}

	@Override
	protected void onZap( int cell ) {
		int dmg=damageRoll();
		Char ch = Actor.findChar(cell);
		if (ch != null) {
			ch.damage( ch.absorb(dmg ,true ), curUser, Element.FROST );
			BuffActive.addFromDamage( ch, Chilled.class, dmg);
		}
		CellEmitter.get(cell).burst(Speck.factory(Speck.BLAST_FROST), 5);
	}

	protected void fx( int cell, Callback callback ) {
		MagicMissile.frost(curUser.sprite.parent, curUser.pos, cell, callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"这个法杖可以释放出寒冷的冰霜能量，并且冻伤，甚至于冰冻目标，使用者可以用它对毫无防备的敌人造成更严重的效果，或者用来摆脱一些严重的威胁";
		}
}
