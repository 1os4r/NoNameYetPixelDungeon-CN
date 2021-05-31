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

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.special.Guard;
import com.ravenwolf.nnypdcn.actors.buffs.special.Satiety;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;

public abstract class MeleeWeapon extends Weapon {

	public MeleeWeapon(int tier) {

		super();
		this.tier = tier;
	}

    protected int[][] weapAtk() {
        return new int[][]{	{1, 2, 3, 0 },	//frame
                {1, 2, 5, 3 },	//x
                {0, 0, 0, 0}};
    }
    protected int[][] weapStab() {
        return new int[][]{	{1, 2, 3, 0 },	//frame
                {1, 2, 5, 3 },	//x
                {0, 0, 0, 0}};
    }

    public int[][] getDrawData(int action){
         if (action == HeroSprite.ANIM_ATTACK)
            return weapAtk();
        else if (action == HeroSprite.ANIM_STAB)
            return weapStab();
        else
            return super.getDrawData(action);
    }

    private static final String TXT_NOTEQUIPPED = "你要先装备这个武器";
    private static final String TXT_CANNOTGUARD = "你只能用近战武器或盾牌来格挡!";
    private static final String TXT_GUARD = "guard";

    private static final String AC_GUARD = "格挡";

    @Override
    public String equipAction() {
        return AC_GUARD;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_UNEQUIP : AC_EQUIP;
    }


    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_GUARD) {

            if (!isEquipped(hero)) {
                GLog.n(TXT_NOTEQUIPPED);
            } else {
                hero.buff( Satiety.class ).decrease( (float)str() / hero.STR() );
                Buff.affect( hero, Guard.class).reset(2);
                hero.spendAndNext( Actor.TICK );
            }

        } else {

            super.execute( hero, action );

        }
    }

    public int reach(){
        return 1;
    }

    @Override
    public int min( int bonus ) {
        return Math.max( 0, 2 + tier + bonus /*+ ( enchantment instanceof Tempered && isCursedKnown()? !isCursed()? 1 + bonus : -1 : 0 )*/ ) ;
    }

    @Override
    public int max( int bonus ) {
        return Math.max( 0, 6+ 4*tier
                +  bonus * dmgMod()
               // + ( enchantment instanceof Tempered && isCursedKnown()? !isCursed()? tier + bonus : -tier : 0 )
        ) ;
    }

    public int dmgMod() {
        return tier +1 ;
    }

    @Override
    public int str(int bonus) {
        return 8 + tier * 2 - strMod(bonus);
    }

    @Override
    public int penaltyBase() {
        return 2*tier;
    }


    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return known >= UPGRADE_KNOWN;
    }

    @Override
    public boolean isEnchantKnown() {
        return known >= ENCHANT_KNOWN;
    }

    @Override
    public boolean isCursedKnown() {
        return known >= CURSED_KNOWN;
    }

//	@Override
//	public Item random() {
//
//        bonus = Random.NormalIntRange( -3, +3 );
//
//		if (Random.Int( 7 + bonus ) == 0) {
//			enchant();
//		}
//
//        randomize_state();
//
//		return this;
//	}


}
