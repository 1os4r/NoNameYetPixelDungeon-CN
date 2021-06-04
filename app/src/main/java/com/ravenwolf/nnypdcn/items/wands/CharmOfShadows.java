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
import com.ravenwolf.nnypdcn.actors.blobs.Blob;
import com.ravenwolf.nnypdcn.actors.blobs.Darkness;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.BlastWave;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;


public class CharmOfShadows extends WandUtility {

	{
		name = "暗影魔咒";
		hitChars = false;
		image = ItemSpriteSheet.CHARM_SHADOWS;
	}


	@Override
	protected void cursedProc(Hero hero){
		curCharges = (curCharges+1)/2;
		int dmg=hero.absorb( damageRoll(), true )/2;
		hero.damage(dmg, this, Element.UNHOLY_PERIODIC);
	}

	@Override
	protected void onZap( int cell ) {
		super.onZap(cell);

		if( Level.solid[ cell ] )
			cell = Ballistica.trace[ Ballistica.distance - 1 ];

		for (int i : Level.NEIGHBOURS9) {
			int pos = cell + i;

			Char c = Actor.findChar(pos);
			if (c != null && c != curUser )
				c.damage(c.absorb(damageRoll(), true), this, Element.UNHOLY_PERIODIC);
		}

		GameScene.add( Blob.seed( cell, damageRoll()*12 , Darkness.class ),-Actor.TICK );
		Sample.INSTANCE.play( Assets.SND_GHOST, 1.0f, 1.0f, 0.5f );
		BlastWave.createAtPos( cell,0x331133, true);
		BlastWave.createAtPos( cell,0x331133, false);

	}


	protected void fx( int cell, Callback callback ) {
		curUser.sprite.cast(cell,callback);
	}

	@Override
	public String desc() {
		return 
			"由一些黑曜石制成的魔咒，你能感受到它周围有一些黑雾冒出，释放它对目标位置造成暗影爆炸，伤害范围内的敌人，并阻挡黑雾范围内所有生物的视线。";

	}
	

}
