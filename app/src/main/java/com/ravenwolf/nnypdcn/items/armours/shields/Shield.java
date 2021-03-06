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
package com.ravenwolf.nnypdcn.items.armours.shields;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.special.Guard;
import com.ravenwolf.nnypdcn.actors.buffs.special.Satiety;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.EquipableItem;
import com.ravenwolf.nnypdcn.items.armours.Armour;
import com.ravenwolf.nnypdcn.items.armours.glyphs.Durability;
import com.ravenwolf.nnypdcn.items.armours.glyphs.Featherfall;
import com.ravenwolf.nnypdcn.items.weapons.criticals.BluntCritical;
import com.ravenwolf.nnypdcn.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypdcn.items.weapons.melee.MeleeWeaponHeavyTH;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.CellSelector;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypdcn.visuals.ui.QuickSlot;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public abstract class Shield extends Armour {


    private final int[][] idleFrames = {	{0, 0, 0, 0, 0, 0 ,0, 0, 0, 0 },	//frame
            {4, 4, 4, 4, 4, 4 ,4, 4, 4, 4 },	//x
            {1, 1, 1, 1, 1, 1 ,1, 1, 1, 1 }};//y

    private final int[][] runFrames = {	{0, 1, 1, 0, 0, 0  },	//frame
            {4, 4, 4, 4, 4, 5  },	//x
            {1, 1, 1, 1, 1, 1 }};//y

    private final int[][] flyFrames = {	{0, 0, 0, 0 },	//frame
            {5 },	//x
            {1}};//y

    private final int[][] slamFrames = {	{0, 0, 0, 0 },	//frame
            {4, 3, 6, 4 },	//x
            {1, 1, 1, 1}};//y

    private final int[][] atkFrames = {	{0, 0, 1, 0 },	//frame
            {5, 5, 4, 4 },	//x
            {1, 1, 1, 1}};//y

    private final int[][] stabFrames = {	{0, 1, 1, 0 },	//frame
            {5, 4, 4, 4 },	//x
            {1, 1, 1, 1}};//y

    private final int[][] doubleHitFrames = {	{0, 1, 1, 1, 0 },	//frame
            {5, 4, 4, 3, 4 },	//x
            {1, 1, 1, 1 , 1}};//y


    @Override
    public int[][] getDrawData(int action){
        if (action == HeroSprite.ANIM_IDLE )
            return idleFrames;
        else if (action == HeroSprite.ANIM_RUN)
            return runFrames;
        else if (action == HeroSprite.ANIM_ATTACK || action==HeroSprite.ANIM_CAST || action==HeroSprite.ANIM_BACKSTAB  || action ==  HeroSprite.ANIM_RANGED_ATTACK )
            return atkFrames;
        else if (action == HeroSprite.ANIM_STAB )
            return stabFrames;
        else if (action == HeroSprite.ANIM_DOUBLEHIT )
            return doubleHitFrames;
        else if (action == HeroSprite.ANIM_SLAM)
            return slamFrames;
        else if (action == HeroSprite.ANIM_FLY)
            return flyFrames;
        else
            return null;
    }

    public Shield(int tier) {

        super(tier);
        hitsToKnow/=2;

    }

    private static final String TXT_NOTEQUIPPED = "???????????????????????????";
    private static final String TXT_SLAM_FAIL = "???????????????????????????????????????";
    private static final String TXT_GUARD = "??????";

    private static final String AC_GUARD = "??????";

    private static final String AC_SHIELD_SLAM = "??????";


    @Override
    public String equipAction() {
        return AC_GUARD;
    }

    public float counterMod() {
        return 0.3f+0.03f*bonus;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_SHIELD_SLAM /*AC_UNEQUIP */: AC_EQUIP;
    }

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.add( AC_GUARD );
//        return actions;
//    }

    public int guardTurns(){
        return 3;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_GUARD) {

            if (!isEquipped(hero)) {

                GLog.n(TXT_NOTEQUIPPED);

            }  else {

                hero.buff( Satiety.class ).decrease( (float)str() / hero.STR() );
                Buff.affect( hero, Guard.class).reset(guardTurns());
                hero.spendAndNext( Actor.TICK );

            }

        }else if (action.equals(AC_SHIELD_SLAM)) {

                curUser = hero;
                curItem = this;
                GameScene.selectCell(slamer);

        }else {

            super.execute( hero, action );

        }
    }
	
	@Override
	public boolean doEquip( Hero hero ) {
		detach(hero.belongings.backpack);

        if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap2 == null || !hero.belongings.weap2.isCursed() ) )
            QuickSlot.quickslot1.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap2 == null || !hero.belongings.weap2.isCursed() ) )
            QuickSlot.quickslot2.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap2 == null || !hero.belongings.weap2.isCursed() ) )
            QuickSlot.quickslot3.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if ( ( hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip( hero, true, false ) ) &&
                ( !isCursed() || isCursedKnown() || !detectCursed( this, hero ) ) ) {

			if (hero.belongings.weap1!=null && hero.belongings.weap1.incompatibleWithShield()){
                if (!hero.belongings.weap1.doUnequip(hero, true, false)){
                    QuickSlot.refresh();
                    hero.spendAndNext(time2equip(hero) * 0.5f);

                    if ( !collect( hero.belongings.backpack ) ) {
                        Dungeon.level.drop( this, hero.pos ).sprite.drop();
                    }

                    return false;
                }
            }

			hero.belongings.weap2 = this;

            onEquip( hero );

            QuickSlot.refresh();
			
			hero.spendAndNext(time2equip(hero));
            hero.updateWeaponSprite();
			return true;
			
		} else {

            QuickSlot.refresh();
            hero.spendAndNext(time2equip(hero) * 0.5f);

            if ( !collect( hero.belongings.backpack ) ) {
                Dungeon.level.drop( this, hero.pos ).sprite.drop();
            }

			return false;
			
		}
	}
	
	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			hero.remove(Guard.class);
			hero.belongings.weap2 = null;
			//update if it have a dual wielding weapon
            hero.updateWeaponSprite();
            QuickSlot.refresh();

			return true;
			
		} else {
			
			return false;
			
		}
	}

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.weap2 == this;
    }

    @Override
    public int dr( int bonus ) {
        return 2 + tier * 4
                //+ ( glyph instanceof Durability || bonus >= 0 ? tier * bonus : 0 )
                +tier * bonus
                //+ ( glyph instanceof Durability && bonus >= 0 ? /*2*/ 1+ bonus : 0 ) ;
                + ( glyph instanceof Durability & isCursedKnown()? !isCursed()? 1 + bonus : -tier : 0 ) ;
    }

    /*@Override
    public int currentPenalty(Hero hero, int str) {
        return super.currentPenalty(hero, str) + tier * 8 - 8 ;
    }*/

    @Override
    public int penaltyBase() {
        return super.penaltyBase()+tier * 4;
    }

    @Override
    public int str( int bonus ) {
        return 6 + tier * 4 - ( glyph instanceof Featherfall & isCursedKnown()? !isCursed()? 1 + bonus/2 : -1 : 0 ) - ( bonus+1)/2;
    }

    @Override
    public int strShown( boolean identified ) {
        /*return super.strShown( identified ) + (
                this == Dungeon.hero.belongings.weap2 && incompatibleWith( Dungeon.hero.belongings.weap1 ) ?
                        Dungeon.hero.belongings.weap1.str(
                                Dungeon.hero.belongings.weap1.isIdentified() ?
                                        Dungeon.hero.belongings.weap1.bonus : 0
                        ) : 0 );*/
        return super.strShown( identified ) + (
                this == Dungeon.hero.belongings.weap2 && incompatibleWith( Dungeon.hero.belongings.weap1 ) ?
                        tier : 0 );
    }

    @Override
    public boolean incompatibleWith( EquipableItem item ) { return item instanceof MeleeWeaponHeavyTH; }
	
	@Override
	public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = currentPenalty(Dungeon.hero, strShown(isIdentified())) * 2.5f;
        //float penalty = GameMath.gate(0, currentPenalty(Dungeon.hero, strShown(isIdentified())), 20) * 2.5f;
        int armor = Math.max(0, isIdentified() ? dr() : dr(0) );

        StringBuilder info = new StringBuilder( desc() );

//        if( !descType().isEmpty() ) {
//
//            info.append( p );
//
//            info.append( descType() );
//        }

        info.append( p );

        if (isIdentified()) {
            info.append( "??????_" + tier + "?????????_??????_" + itemStr + "?????????_??????????????????" +
                    //( isRepairable() ? ", given its _" + stateToString( state ) + " condition_, " : " " ) +
                    //"will occasionally increase your _armor class by " + armor + " points_.");
                    "?????????????????????????????????_" + armor + "_???????????????" );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "????????????????????????????????????????????????????????????????????????_?????? " + penalty + "%_????????????????????????_" + (int)(100 - 10000 / (100 + penalty)) + "%???????????????_???" );
            } else if (itemStr < heroStr) {
                info.append(
                        "??????????????????????????????????????????????????????????????????????????????"
						 + ( penalty > 0 ? "??????_??????" + penalty + "%_" : "_????????????_" ) + "???");//+ " " +
                                //"and your armor class will be increased by _" + ((float)(heroStr - itemStr) / 2) + " bonus points_ on average." );
            } else {
                info.append(
                        "????????????????????????????????????????????????" + ( penalty > 0 ? "???_??????" + penalty + "%_, " +
                                "??????????????????????????????????????????????????????" : "_????????????_" ) + "???" );
            }
        } else {
            info.append(  "????????????_" + tier + "?????????_??????_" + itemStr + "?????????_??????????????????" +
                    //( isRepairable() ? ", when in _" + stateToString( state ) + " condition_, " : " " ) +
                    //"will occasionally increase your _armor class by " + armor + " points_." );
                    "?????????????????????????????????_" + armor + "_???????????????" );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "??????????????????????????????????????????????????????????????????????????????_??????" + penalty + "%_????????????????????????_" + (int)(100 - 10000 / (100 + penalty)) + "%???????????????_???" );
            } else if (itemStr < heroStr) {
                info.append(
                        "????????????????????????????????????????????????????????????????????????????????????" 
						+ ( penalty > 0 ? "??????_??????" + penalty + "%_" : "_????????????_" ) + "???");// + " " +
                                //"and your armor class will be increased by _" + ((float)(heroStr - itemStr) / 2) + " bonus points_ on average." );
            } else {
                info.append(
                        "??????????????????????????????????????????????????????" + ( penalty > 0 ? "???_??????" + penalty + "%_, " +
                                "??????????????????????????????????????????????????????" : "_????????????_" ) + "???" );
            }
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append( "???????????????" + name + "???" );

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "??????" + name + "????????????????????????" );

        } else {

            info.append( "??????" + name + "??????????????????" );

        }

        info.append( s );

        if( isIdentified() && bonus > 0 ) {
            info.append( "???????????????_??????_???" );
        } else if( isCursedKnown() ) {
            info.append( !isCursed() ? "???????????????_???????????????_???" :
                    "?????????????????????????????????_??????_????????????" );
        } else {
            info.append( "??????" + name + "???_????????????_???" );
        }

        info.append( s );

        if( isEnchantKnown() && glyph != null ) {
            info.append( "" + ( isIdentified() && bonus != 0 ? "??????" : "??????" ) + "???????????????_" + glyph.desc(this) + "_????????????" );
        }

        info.append( "????????????_" + lootChapterAsString() +"_?????????" );

        return info.toString();

	}

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }
	
	@Override
	public int price() {
        return price(30*lootChapter());
	}

    protected static CellSelector.Listener slamer = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {

            if (target != null) {

                Ballistica.cast(curUser.pos, target, false, true);

                int cell = Ballistica.trace[0];

                if (Ballistica.distance >= 1) {
                    cell = Ballistica.trace[1];
                }

                final Char enemy = Actor.findChar(cell);

                if( !(enemy instanceof Mob)) {
                    GLog.i(TXT_SLAM_FAIL);
                    return;
                }


                ((HeroSprite)curUser.sprite).shieldSlam(cell, new Callback() {
                    @Override
                    public void call() {
                        curUser.onSlamComplete(enemy,curItem);

                    }
                });
                curUser.busy();
                curUser.spend(Actor.TICK);
            }
        }

        @Override
        public String prompt() {
            return "?????????????????????????????????";
        }
    };

    public static class ShieldSlam extends MeleeWeapon {
        {
            name = "??????";
            critical=new BluntCritical(this, false, 2f);
        }
        Shield shield;

        public ShieldSlam(Shield shield) {
            super( shield.tier );
            this.shield=shield;
        }

        @Override
        public int str(int bonus) {
            return shield.str(bonus);
        }

        @Override
        public int penaltyBase() {
            return shield.penaltyBase();
        }
/*
        @Override
        public boolean increaseCombo(){
            return false;
        }
        */
        @Override
        public int min( int bonus ) {
            return shield.dr() / 2;
        }

        @Override
        public int max( int bonus ) {
            return shield.dr();
        }


        @Override
        public int proc( Char attacker, Char defender, int damage ) {
            damage=super.proc(attacker, defender, damage);
            /*boolean exposed =defender.isExposedTo(attacker);
            if (exposed || Random.Int(defender.totalHealthValue()) < attacker.awareness() * damage) {
                defender.knockBack(attacker, damage);
                defender.spend(1f);
            }
*/
            Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, 0.5f );
            Camera.main.shake(2, 0.1f);

            return damage;
        }

    }

}
