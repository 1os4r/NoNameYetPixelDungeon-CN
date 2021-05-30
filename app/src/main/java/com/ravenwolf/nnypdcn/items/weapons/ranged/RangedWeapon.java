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
package com.ravenwolf.nnypdcn.items.weapons.ranged;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.bonuses.Invisibility;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypdcn.actors.buffs.special.Satiety;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.items.weapons.throwing.ThrowingWeaponAmmo;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.scenes.CellSelector;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.MissileSprite;
import com.ravenwolf.nnypdcn.visuals.ui.QuickSlot;
import com.ravenwolf.nnypdcn.visuals.ui.TagAttack;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public abstract class RangedWeapon extends Weapon {

	public RangedWeapon(int tier) {

		super();
		
		this.tier = tier;
	}

    //weapon animations
    private static final int[][] weapRun = {	{0, 0, 1, 1, 0, 0  },	//frame
            {3, 4, 5, 3, 2, 2  },	//x
            {0, 0, 0, 0, 0, 0 }};//y


    public int[][] getDrawData(int action){
        if (action == HeroSprite.ANIM_RUN)
            return weapRun;
        else
            return super.getDrawData(action);
    }

    protected static final String AC_SHOOT = "射击";
    protected static final String TXT_SELF_TARGET	= "你不能瞄准自己";
    protected static final String TXT_TARGET_CHARMED	= "You can't bring yourself to harm someone so... charming.";
    protected static final String TXT_NOTEQUIPPED = "你必须先装备该武器。";
    protected static final String TXT_AMMO_NEEDED = "你必须装备该武器所适配的弹药。";

    public Class<? extends ThrowingWeaponAmmo> ammunition() {
        return null;
    }

    public boolean checkAmmo( Hero hero, boolean showMessage ) {
        return false;
    }

    @Override
    public boolean increaseCombo(){
        return false;
    }

    @Override
    public String status() {
        return isEquipped( Dungeon.hero ) && ammunition().isInstance( Dungeon.hero.belongings.ranged ) ? Integer.toString( Dungeon.hero.belongings.ranged.quantity ) : null;
    }

    @Override
    public String equipAction() {
        return AC_SHOOT;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_UNEQUIP : AC_EQUIP;
    }

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.add( AC_SHOOT );
//        return actions;
//    }

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
//		super.random();
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

    protected float bulletSpeed(){
        return 2f;
    }

    protected void afterShoot( int cell ) {
        //do nothing
    }

    protected void onShoot(int cell) {
        //do nothing
    }

    public static CellSelector.Listener shooter = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                final RangedWeapon curWeap = (RangedWeapon) curItem;

                if (curUser.buff(Dazed.class) != null) {
                    target += Level.NEIGHBOURS8[Random.Int(8)];
                }

                final int cell = Ballistica.cast(curUser.pos, target, false, true);

                Char ch = Actor.findChar(cell);

                if (ch != null && curUser != ch && Dungeon.visible[cell]) {

//                    if ( curUser.isCharmedBy( ch ) ) {
//                        GLog.i( TXT_TARGET_CHARMED );
//                        return;
//                    }

                    QuickSlot.target(curItem, ch);
                    TagAttack.target((Mob) ch);
                }

                ((HeroSprite) curUser.sprite).shoot(cell, curWeap, new Callback() {
                    @Override
                    public void call() {

                        curUser.busy();

                        ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                                reset(curUser.pos, cell, curUser.belongings.ranged, curWeap.bulletSpeed(), new Callback() {
                                    @Override
                                    public void call() {
                                        ((ThrowingWeaponAmmo) curUser.belongings.ranged).onShoot(cell, curWeap);
                                    }
                                });

                        curUser.buff(Satiety.class).decrease((float) curWeap.str() / curUser.STR());
                        curWeap.use(2);

                        curWeap.afterShoot(cell);

                    }
                });

                curWeap.onShoot(cell);

                Invisibility.dispel();

            }

        }

        @Override
        public String prompt() {
            return "选择射击目标";
        }
    };

}
