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
package com.ravenwolf.nnypdcn.items.armours.body;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.buffs.special.Guard;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.items.armours.Armour;
import com.ravenwolf.nnypdcn.items.armours.glyphs.Durability;
import com.ravenwolf.nnypdcn.items.armours.glyphs.Featherfall;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypdcn.visuals.ui.QuickSlot;

public abstract class BodyArmor extends Armour {

	public int appearance;

    public BodyArmor( int tier ) {

        super(tier);

    }

    public int getHauntedIndex(){
        return 0;
    }
	
	@Override
	public boolean doEquip( Hero hero ) {
		
		detach(hero.belongings.backpack);
		
		if ( ( hero.belongings.armor == null || hero.belongings.armor.doUnequip( hero, true, false ) ) &&
                ( !isCursed() || isCursedKnown() || !detectCursed( this, hero ) ) ) {

			hero.belongings.armor = this;

            ((HeroSprite)hero.sprite).updateArmor();

            onEquip( hero );

			hero.spendAndNext(time2equip(hero));

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
			hero.belongings.armor = null;

			((HeroSprite)hero.sprite).updateArmor();
			
			return true;
			
		} else {
			
			return false;
			
		}
	}

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.armor == this;
    }

    @Override
    protected float time2equip( Hero hero ) {
        return super.speedFactor( hero ) * 3;
    }

    @Override
    public int dr( int bonus ) {
        //return tier * ( /*3*/2 + state ) +1
        return tier * 4/*( state +1)*/ +2
                //+ ( glyph instanceof Durability || bonus >= 0 ? tier * bonus : 0 )
                +tier * bonus
                //+ ( glyph instanceof Durability && bonus >= 0 ? /*2*/1 + bonus : 0 ) ;
                + ( glyph instanceof Durability & isCursedKnown()? !isCursed()? 1 + bonus : -tier : 0 ) ;
    }
    
    //@Override
    public int minDr( int bonus ) {
        return tier-1 +bonus;
    }

    public int minDr() {
        return minDr(bonus) ;
    }

    @Override
    public int str(int bonus) {
        //return 9 + tier * 4 - ( glyph instanceof Featherfall ? bonus : 0 );
        return 8 + tier * 4 - ( glyph instanceof Featherfall & isCursedKnown()? !isCursed()? 1 + bonus/2 : -1 : 0 ) - ( bonus+1)/2;
    }
	
	@Override
	public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = currentPenalty(Dungeon.hero, strShown(isIdentified())) * 2.5f;
        int armor = Math.max(0, isIdentified() ? dr() : dr(0) );
        int minArmor = Math.max(0, isIdentified() ? minDr() : minDr(0) );

        StringBuilder info = new StringBuilder( desc() );

//        if( !descType().isEmpty() ) {
//
//            info.append( p );
//
//            info.append( descType() );
//        }

        info.append( p );

        if (isIdentified()) {
            info.append( "这个_" + tier + "阶" + ( !descType().isEmpty() ? descType() + " " : "" )  + "护甲_需要_" + itemStr + "点力量_才能正常使用" +

                    "并且可以抵挡_ " +minArmor+ "-" + armor + "点伤害_。");

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "由于你的力量不足，装备该护甲时会使你的潜行和灵巧_降低" + penalty + "%_，同时还会降低你_" + (int)(100 - 10000 / (100 + penalty)) + "%的移动速度_。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "由于你拥有额外的力量，所以装备该护甲时你的潜行和灵巧" + ( penalty > 0 ? "只会_降低" + penalty + "%_" : "_不会降低_" ) + "。"); //" " +
                               //"and your armor class will be increased by _" + ((float)(heroStr - itemStr) / 2) + " bonus points_ on average." );
            } else {
                info.append(
                        "当你装备该护甲时，你的潜行和灵巧" + ( penalty > 0 ? "会_降低 " + penalty + "%_, " +
                                "当你拥有额外的力量时，将减轻这种惩罚" : "_不会降低_" ) + "。" );
            }
        } else {
            info.append(  "通常这个_" + tier + "阶" + ( !descType().isEmpty() ? descType() + " " : "" )  + "护甲_需要_" + itemStr + "点力量_才能正常使用" +
                    "并且可以抵挡_" + minArmor+ "-" + armor+ "点伤害_。" );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "由于你的力量不足，装备该护甲时可能会使你的潜行和灵巧_降低" + penalty + "%_，同时还会降低你_" + (int)(100 - 10000 / (100 + penalty)) + "%的移动速度_。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "由于你拥有额外的力量，所以装备该护甲时你的潜行和灵巧可能" + ( penalty > 0 ? "只会_降低" + penalty + "%_" : "_不会降低_" ) + "。"); //" " +
                                 //"and your armor class will be increased by _" + ((float)(heroStr - itemStr) / 2) + " bonus points_ on average." );
            } else {
                info.append(
                        "当你装备该护甲时，你的潜行和灵巧可能" +
                                ( penalty > 0 ? "会_降低" + penalty + "%_" : "_不会降低_" ) +
                                ", 除非你的力量不同于护甲的力量需求。" );
            }
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append( "你正装备着" + name + "。" );

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "这件" + name + "正在你的背包里。" );

        } else {

            info.append( "这件" + name + "正在地面上。" );

        }

        info.append( s );

        if( isIdentified() && bonus > 0 ) {
            info.append( "他似乎已被_升级_。" );
        } else if( isCursedKnown() ) {
            info.append( !isCursed() ? "它看起来并_没有被诅咒_。" :
                    "你能感受到它似乎充满了_恶意_的魔力"/* + name */ + "。" );
        } else {
            info.append( "这件" + name + "尚_未被鉴定_。" );
        }

        info.append( s );

        if( isEnchantKnown() && glyph != null ) {
            info.append( "" + ( isIdentified() && bonus != 0 ? "同时" : "不过" ) + "，它携带着_" + glyph.desc(this) + "_的附魔。" );
        }

        info.append( "这是一件_" + lootChapterAsString() +"_护甲。" );

        return info.toString();

	}

}
