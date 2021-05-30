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
package com.ravenwolf.nnypdcn.actors.mobs;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.visuals.effects.Chains;
import com.ravenwolf.nnypdcn.visuals.effects.Effects;
import com.ravenwolf.nnypdcn.visuals.effects.Pushing;
import com.ravenwolf.nnypdcn.visuals.sprites.FrogSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.MissileSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;


public class GiantFrog extends MobHealthy {

    private int tongueCooldown=0;

    public GiantFrog() {

        super( 12 );

		name = "巨型青蛙";
		spriteClass = FrogSprite.class;

        resistances.put(Element.Acid.class, Element.Resist.PARTIAL);
	}

    @Override
    public String getTribe() {
        return TRIBE_BEAST;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        tongueCooldown--;
        return super.canAttack( enemy ) || tongueCooldown<1 && Level.distance(pos, enemy.pos) <= 3
                && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    protected void onRangedAttack( int cell ) {
        sprite.parent.add(new Chains(pos, cell, false, Effects.Type.TONGUE));
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, ItemSpriteSheet.PELLET, new Callback() {
                    @Override
                    public void call() {
                        chain(enemy);
                        next();
                    }
                });

        super.onRangedAttack( cell );
    }

    private boolean chain(Char enemy){
        tongueCooldown=6;
        int targetPos=enemy.pos;
        if (enemy.immovable())
            return false;


        int ballistica=Ballistica.cast(pos, targetPos, false, true);

        if (ballistica!=targetPos)
            return false;
        else {
            int newPos = Ballistica.trace[1];

            Actor.addDelayed(new Pushing(enemy, enemy.pos, newPos), -1);

            Actor.freeCell(enemy.pos);
            enemy.pos = newPos;
            Actor.occupyCell(enemy);

            Dungeon.level.press(newPos, enemy);

            spend( -1 / attackSpeed() );


        }
        return true;
    }

	@Override
	public String description() {
		return
			"这些两栖动物经常捕食蝙蝠和其他中小型动物。但是像它这样的巨型青蛙，也许会以大型动物和人类为食。";
	}

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( "COOLDOWN", tongueCooldown );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        tongueCooldown = bundle.getInt( "COOLDOWN" );
    }

}
