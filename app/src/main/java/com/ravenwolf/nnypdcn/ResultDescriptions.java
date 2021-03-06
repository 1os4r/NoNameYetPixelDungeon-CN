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
package com.ravenwolf.nnypdcn;

import com.ravenwolf.nnypdcn.actors.blobs.Blob;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Corrosion;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Crippled;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Poisoned;
import com.ravenwolf.nnypdcn.actors.buffs.special.Satiety;
import com.ravenwolf.nnypdcn.actors.mobs.Bestiary;
import com.ravenwolf.nnypdcn.actors.mobs.DwarfMonk;
import com.ravenwolf.nnypdcn.actors.mobs.GnollBrute;
import com.ravenwolf.nnypdcn.actors.mobs.Golem;
import com.ravenwolf.nnypdcn.actors.mobs.Mimic;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.actors.mobs.Piranha;
import com.ravenwolf.nnypdcn.actors.mobs.Rat;
import com.ravenwolf.nnypdcn.items.armours.Armour;
import com.ravenwolf.nnypdcn.items.wands.Wand;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.levels.features.Chasm;
import com.ravenwolf.nnypdcn.levels.traps.BoulderTrap;
import com.ravenwolf.nnypdcn.levels.traps.Trap;
import com.ravenwolf.nnypdcn.misc.utils.Utils;

public abstract class ResultDescriptions {

//    public static final String FAIL	= "%s";
    public static final String WIN	= "??????Yendor?????????";

    public static String generateResult( Object killedBy, Element killedWith ) {

        return Utils.capitalize( killedBy == Dungeon.hero ? "???????????????" :
                "?????????" + killedBy( killedBy ) );
    }

    public static String generateMessage( Object killedBy, Element killedWith ) {

        return ( killedBy == Dungeon.hero ? "????????????????????????????????????" :
                killedBy( killedBy ) + killedWith( killedBy, killedWith ) +  "??????" ) + "...";
    }

    private static String killedWith( Object killedBy, Element killedWith ) {

        String result = "??????";

        if( killedWith == null ) {

            if( killedBy instanceof Mob) {

                Mob mob = (Mob)killedBy;

                if( Bestiary.isBoss( mob ) || mob instanceof Rat ) {

                    result = "??????";

                } else if ( mob instanceof GnollBrute ) {

                    result = "???????????????";

                } else if ( mob instanceof DwarfMonk ) {

                    result = "???????????????";

                } else if ( mob instanceof Golem ) {

                    result = "???????????????";

                } else if ( mob instanceof Piranha ) {

                    result = "????????????";

                } else if ( mob instanceof Mimic ) {

                    result = "????????????";

                }

            } if( killedBy instanceof BoulderTrap ) {

                result = "??????";

            }


//        } else if( killedWith instanceof DamageType.Flame) {
//            result = "burned to crisp";
//        } else if( killedWith instanceof DamageType.Frost) {
//            result = "chilled to death";
        } else if( killedWith instanceof Element.Shock) {
            result = "??????";
        } else if( killedWith instanceof Element.Acid) {
            result = "??????";
//        } else if( killedWith instanceof DamageType.Mind) {
//            result = "";
//        } else if( killedWith instanceof DamageType.Body) {
//            result = "drained";
//        } else if( killedWith instanceof DamageType.Unholy) {
//            result = "withered";
//        } else if( killedWith instanceof DamageType.Energy) {
//            result = "disintegrated";
        }

        return result;
    }

    private static String killedBy( Object killedBy ) {

        String result = "???????????????";

        if( killedBy instanceof Mob ) {
            Mob mob = ((Mob)killedBy);
            result = ( !Bestiary.isBoss( mob ) ? Utils.indefinite( mob.name ) : mob.name );
        } else if( killedBy instanceof Blob ) {
            Blob blob = ((Blob)killedBy);
            result = Utils.indefinite( blob.name );
        } else if( killedBy instanceof Weapon.Enchantment ) {
            result = "????????????";
        } else if( killedBy instanceof Armour.Glyph ) {
            result = "????????????";
        } else if( killedBy instanceof Wand) {
            result = "????????????";
        } else if( killedBy instanceof Buff ) {
            if( killedBy instanceof Crippled ) {
                result = "??????";
            } else if( killedBy instanceof Poisoned ) {
                result = "??????";
            } else if( killedBy instanceof Satiety ) {
                result = "??????";
            } else if( killedBy instanceof Burning ) {
                result = "??????";
            } else if( killedBy instanceof Corrosion ) {
                result = "????????????";
            }
        } else if( killedBy instanceof Trap ) {
            result = "??????";
        } else if( killedBy instanceof Chasm ) {
            result = "??????";
        }

        return result;
    }

}
