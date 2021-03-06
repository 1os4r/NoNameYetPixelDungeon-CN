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


import com.ravenwolf.nnypdcn.items.weapons.criticals.PierceCritical;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.layers.WeaponSprite;


public class AssassinBlade extends MeleeWeaponLightOH{

	{
		name = "ζζδΉε";
		image = ItemSpriteSheet.ASSASSIN_BLADE;
		drawId= WeaponSprite.ASSASSIN_BLADE;
		critical=new PierceCritical(this, true, 1f);
	}

	protected int[][] weapAtk() {
        return new int[][]{	{4, 2, 3, 0 },	//frame
                {-2, 2, 5, 2 },	//x
                {0, 0, 0, 0}};
	}

	protected int[][] weapBackstab() {
		return new int[][]{	{1, 2,  3, 4 },	//frame
				{2, 2,  5, -1 },	//x
				{0, 0, 0, 0}};
	}

	protected int[][] weapIddle() {
		return new int[][]{	{4, 4, 4, 4, 4, 4 ,4, 4 },	//frame
				{-1, -1, -1, -1, -1, -1 ,-1, -1 },	//x
				{0, 0, 0, 0, 0, 0 ,0, 0 }};
	}

	protected int[][] weapRun() {
		return new int[][]{	{4, 1, 1, 1, 4, 4  },	//frame
				{-1, 2, 4, 2, -1, -2  },	//x
				{0, 0, 0, 0, 0, 0 }};
	}

	protected int[][] weapFly() {
		return new int[][]{	{4 },	//frame
				{-1},	//x
				{0}};
	}

	public int[][] getDrawData(int action){

		if (action == HeroSprite.ANIM_BACKSTAB)
			return weapBackstab();
		else
			return super.getDrawData(action);
	}

	@Override
	public int min( int bonus ) {
		return super.min(bonus) -1;
	}

	@Override
	public int max( int bonus ) {
		return super.max(bonus) -2;
	}

	public AssassinBlade() {
		super(3 );
	}


	@Override
	public float getBackstabMod(){
		return 0.9f;
	}

	@Override
	public Type weaponType() {
		return Type.M_SWORD;
	}

	@Override
	public String desc() {
		return "θΏζ―δΈζεΆδ½η²Ύθ―ηει¦οΌζ³’ζ΅ͺηΆηεεδ½Ώε?θ½ε€ζ΄ε₯½ηη©ΏιζδΊΊηζ€η², ε¨ε―Ήδ»ζͺε―θ§δ½ ηζδΊΊζΆδΌζ΄ε ηθ΄ε½γ"
				+"\n\nθΏδ»Άζ­¦ε¨δΌι ζζ΄εΌΊε€§ηζ΄ε»ζζοΌεΉΆδΈε¨ε―Ήδ»ζͺε―θ§δ½ ηζδΊΊζΆζ΄δΈΊζζ";
		//return "A fine crafted sacrificial blade. These blades are made from an enchanted material that offer better control to those who have magical abilities, its also a deadly weapon against unaware foes.";
	}
}
