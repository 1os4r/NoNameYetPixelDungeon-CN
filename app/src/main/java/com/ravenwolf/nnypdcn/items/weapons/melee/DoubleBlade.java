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
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.layers.WeaponSprite;

public class DoubleBlade extends MeleeWeaponLightTH {

	private boolean increaseCombo=true;

	{
		//name = "double blade";
		name = "双刃剑";
		image = ItemSpriteSheet.DOUBLE_BLADE;
		drawId= WeaponSprite.DOUBLE_BLADE;
		critical=new BladeCritical(this);
	}

	protected int[][] weapRun() {
        return new int[][]{	{0, 0, 1, 1, 0, 0  },	//frame
                {-4, -3, -1, -3, -5, -5  },	//x
                {0, 0, 0, 0, 0, 0 }};
	}

	protected int[][] weapIddle() {
        return new int[][]{	{0, 0, 0, 0, 0, 0 ,0,0 },	//frame
                {-3, -3, -3, -3, -3, -3 ,-3, -3 },	//x
                {0, 0, 0, 0, 0, 0 ,0, 0 }};
	}
	protected int[][] weapAtk() {
        return new int[][]{	{1, 2, 3, 0 },	//frame
                {-5, -2, 4, 3 },	//x
                {0, -2, 0, 0}};
	}
	protected int[][] weapFly() {
        return new int[][]{	{0 },	//frame
                {-3},	//x
                {0}};
	}
	protected int[][] shieldSlam() {
        return new int[][]{	{0, 0, 0, 0, },	//frame
                {-3, -3, -5, -3, },	//x
                {0, 0, 0, 0, }};
	}
	protected int[][] weapDoubleAtk() {
        return new int[][]{	{1, 2, 3, 4, 0 },	//frame
                {-5, -2, 4, 3, 3 },	//x
                {0, -2, 0, 0, 0}};
	}

	public int[][] getDrawData(int action){
		if (action == HeroSprite.ANIM_DOUBLEHIT)
			return weapDoubleAtk();
		else
			return super.getDrawData(action);
	}

	@Override
	public boolean increaseCombo(){
		return increaseCombo;
	}

	public void setIncreaseCombo(boolean value){
		increaseCombo=value;
	}


	public DoubleBlade() {
		super( 3 );
	}

	@Override
	public Type weaponType() {
		return Type.M_POLEARM;
	}

	@Override
	public float counterBonusDmg(){//have better counter damage
		return 0.60f;
	}


	//AoE damage
	/*@Override
	public int proc(Char attacker, Char defender, int damage ) {
		damage=super.proc(attacker, defender, damage);

		int pos;
		for(int n : Level.NEIGHBOURS8) {
			pos = attacker.pos + n;

			boolean affectCell = true;
			if (Level.solid[pos] || pos == defender.pos){//ignore main target again
				continue;
			}


			if (affectCell) {

				Char ch = Actor.findChar(pos);
				if (ch != null) {
					//if (Char.hit(attacker, ch, false, true)) {

					int dmg = Char.absorb(damage / 2, ch.armorClass());
					critical.proc(attacker,ch,dmg);
					ch.sprite.flash();

					ch.damage(dmg, attacker, null);

				}
			}
		}
		return damage;
	}*/

	@Override
	public String desc() {
		return "双刃剑使用了一种特殊的设计，使得每次都会攻击到周围的敌人。非常适合拿来对付多个敌人"
		+"\n\n这种武器对周围的单位造成伤害，并且非常适合拿来弹反敌人.";
	}
}
