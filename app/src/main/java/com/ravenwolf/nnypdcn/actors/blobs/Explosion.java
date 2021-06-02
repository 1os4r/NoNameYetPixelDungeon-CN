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
package com.ravenwolf.nnypdcn.actors.blobs;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Element;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Dazed;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Bestiary;
import com.ravenwolf.nnypdcn.actors.mobs.Robot;
import com.ravenwolf.nnypdcn.items.Heap;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.NagaBossLevel;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;
import com.ravenwolf.nnypdcn.visuals.effects.particles.BlastParticle;
import com.ravenwolf.nnypdcn.visuals.effects.particles.SmokeParticle;
import com.watabou.utils.Random;

public class Explosion {

	// Returns true, if this cell is visible
	public static boolean affect( int c, int r, int radius, int damage, Object source ) {

        boolean terrainAffected = false;

        if (Dungeon.visible[c]) {
            CellEmitter.get(c).burst( BlastParticle.FACTORY, 12 / ( r + 1 ) );
            CellEmitter.get(c).burst( SmokeParticle.FACTORY, 6 / ( r + 1 ) );
        }

        if (Level.flammable[c]) {
            Level.set(c, Terrain.EMBERS);
            GameScene.updateMap(c);
            terrainAffected = true;
        }

        if (NagaBossLevel.isDestructibleStatue(c)) {
            Level.set(c, Terrain.EMPTY_DECO);
            GameScene.updateMap(c);
            CellEmitter.get( c ).start(Speck.factory(Speck.ROCK), 0.07f, 6);
            terrainAffected = true;
        }


        Char ch = Actor.findChar(c);

        if (ch != null && ch.isAlive()) {

            int mod = ch.HT * damage /
                ( Bestiary.isBoss(ch) ? 200 : ch instanceof Hero ? 100 : 50 );

            int dmg = Random.IntRange( mod / 2, mod );
            dmg += Random.IntRange( damage / 2, damage );
            dmg *= ( radius - r + 2 );
            dmg /= ( radius + 2 );

            if (dmg > 0) {
                ch.damage(ch.absorb(dmg, true ), source, Element.PHYSICAL);
                if (ch.isAlive()) {
                    BuffActive.addFromDamage( ch, Dazed.class, damage );
                }
            }
        }

        if( Dungeon.hero.isAlive() ) {
            Heap heap = Dungeon.level.heaps.get(c);
            if (heap != null) {
                if (source instanceof Robot)
                    heap.shatter("爆炸");
                else
                    heap.blast("爆炸");
            }
        }

        Dungeon.level.press(c, null);

        return terrainAffected;

	}
}
