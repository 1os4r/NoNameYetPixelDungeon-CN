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

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.blobs.Blob;
import com.ravenwolf.nnypdcn.actors.blobs.DisruptionField;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.MagicMissile;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfDisruptionField extends WandCombat {

	{
		name = "裂场法杖";
		image = ItemSpriteSheet.WAND_LIFEDRAIN;
		hitChars = false;
	}

	@Override
	public int basePower() {
		return super.basePower() +6;
	}

	protected void cursedProc(Hero hero) {
		int cell=hero.pos;
		GameScene.add(Blob.seed(cell, DisruptionField.SPREAD_STATE, DisruptionField.class));
		DisruptionField blob = (DisruptionField) Dungeon.level.blobs.get(DisruptionField.class);
		if (blob.cur[cell] > 0)
			blob.power[cell] = damageRoll();

	}

	@Override
	protected void onZap( int cell ) {
		GameScene.add( Blob.seed( cell, DisruptionField.INIT_STATE, DisruptionField.class ) );
		DisruptionField blob = (DisruptionField) Dungeon.level.blobs.get( DisruptionField.class );
		if (blob.cur[cell]>0) {
			blob.power[cell] += damageRoll();
			/*if (blob.power[cell] > 0)//there is already a field on the target
				blob.power[cell] += damageRoll()*3/4;//Increase power
			else
				blob.power[cell] = damageRoll();*/
		}
	}

	protected void fx( int cell, Callback callback ) {
		MagicMissile.poison(curUser.sprite.parent, curUser.pos, cell, callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public String desc() {
		return
				"释放这个法杖会制造出一片逐步扩散的分解力场，若对同一位置持续释放则会叠加此力场的强度，到达一定回合后此力场将会造成一定范围内的强烈爆炸";

	}
}
