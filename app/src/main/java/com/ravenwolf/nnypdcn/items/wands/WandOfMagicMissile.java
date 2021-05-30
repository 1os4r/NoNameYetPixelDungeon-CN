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
package com.ravenwolf.nnypdcn.items.wands;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.visuals.effects.Splash;
import com.ravenwolf.nnypdcn.visuals.effects.particles.SparkParticle;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfMagicMissile extends WandCombat {


	{
		name = "魔弹法杖";
        //shortName = "Ma";
		image = ItemSpriteSheet.WAND_MAGICMISSILE;
	}

    @Override
    public int basePower() {
        return super.basePower() + Dungeon.hero.magicSkill() / 3;
    }

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		return actions;
	}

    @Override
    protected void cursedProc(Hero hero){
        int dmg=hero.absorb( damageRoll())/2;
        hero.damage(dmg, this, Element.ENERGY);
    }

	@Override
	protected void onZap( int cell ) {

        int power = curUser.magicSkill() / 3;

        Splash.at( cell, 0x33FFFFFF, (int) Math.sqrt(power) + 2 );

        Char ch = Actor.findChar( cell );

        if (ch != null) {

            //if( Char.hit( curUser, ch, true, true ) ) {

                ch.damage(
                        ch.absorb(damageRoll()), curUser, Element.ENERGY
                );

                ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, Random.IntRange( 2 + power / 10 , 4 + power / 5 ) );
//                ch.sprite.burst( 0xFF99CCFF, (int)Math.sqrt( power ) );

//                if (ch == curUser && !ch.isAlive()) {
//                    Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.depth ) );
//                    GLog.n( "You killed yourself with your own Wand of Magic Missile..." );
//                }

            /*} else {

                Sample.INSTANCE.play(Assets.SND_MISS);
                ch.missed();

            }*/
		}
	}

	@Override
	public String desc() {
		return
			"这个法杖的效果十分简单，它会释放出更纯粹的法术能量。它的效果很大程度上取决于你的意志和魔能，使它在一个强大的法师手里会变得更加强大";
	}

}
