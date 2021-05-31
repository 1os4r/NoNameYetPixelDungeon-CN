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
package com.ravenwolf.nnypdcn.items.weapons.melee;


import com.ravenwolf.nnypdcn.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.layers.WeaponSprite;

public class Flail extends MeleeWeaponHeavyOH {

	{
		name = "链枷";
		image = ItemSpriteSheet.FLAIL;
		drawId= WeaponSprite.FLAIL;
		critical=new BluntCritical(this, true, 1.5f);
	}

	protected int[][] weapRun() {
        return new int[][]{	{4, 4, 1, 1, 4, 4  },	//frame
                {2, 3, 5, 3, 1, 1  },	//x
                {0, 0, 0, 0, 0, 0 }};
	}
	protected int[][] weapAtk() {
        return new int[][]{	{1, 2, 3, 0 },	//frame
                {1, 1, 8, 3 },	//x
                {0, 0, 0, 0}};
	}

	public Flail() {
		super( 4 );
	}


	@Override
	public int min( int bonus ) {
		return super.min(bonus) +2;
	}


	//decreased block power
	@Override
	public int guardStrength(int bonus){
		return super.guardStrength(bonus)/2;
	}

	//cannot backstab
	@Override
	public float getBackstabMod(){
		return -1;
	}

	//reduced counter dmg
	@Override
	public float counterBonusDmg(){//have better counter damage
		return 0.15f;
	}


	@Override
	public String desc() {
		return "铁链上附着一个带刺的钢球，笨拙难用，如果能命中的话威力会很大。"
				+"\n\n这种武器更擅长于暴击敌人，并且会造成额外的效果。但是它不能进行偷袭";
	}

	@Override
	public Type weaponType() {
		return Type.M_BLUNT;
	}
}
