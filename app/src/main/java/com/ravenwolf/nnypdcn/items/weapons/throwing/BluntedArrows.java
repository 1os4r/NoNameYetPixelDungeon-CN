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
package com.ravenwolf.nnypdcn.items.weapons.throwing;

import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;

public class BluntedArrows extends Arrows {

	{
		name = "钝化箭矢";
		image = ItemSpriteSheet.BLUNTED_ARROW;
		critical=new BluntCritical(this);
	}

	public BluntedArrows() {
		this( 1 );
	}

	public boolean stick(Char enemy){
		return false;
	}

	public BluntedArrows(int number) {
        super( 1 );
		quantity = number;
	}

	@Override
	public int baseAmount() {
		return 14;
	}

	@Override
	public String desc() {
		return 
			"最初的设计是为了在不过多损害鸟的肉和羽毛的情况下击打鸟类。" +
			"现在，这种钝箭成为了小偷和暴徒们的主要武器，它们非常喜欢从远处击杀它们的猎物。";
	}
}
