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

import com.ravenwolf.nnypdcn.items.weapons.criticals.BladeCritical;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.layers.WeaponSprite;

public class Greataxe extends MeleeWeaponHeavyTH {

	{
		name = "重斧";
		image = ItemSpriteSheet.GREAT_AXE;
		drawId= WeaponSprite.GREAT_AXE;
		critical=new BladeCritical(this, false, 2f);
	}
	
	public Greataxe() {
		super( 4 );
	}


	@Override
	public Type weaponType() {
		return Type.M_BLUNT;
	}
	
	@Override
	public String desc() {
		return "这把沉重的斧头每次攻击都会造成相当大的伤害"
				+"\n\n这种武器更擅长于暴击敌人";
	}
}
