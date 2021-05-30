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

package com.ravenwolf.nnypdcn.actors.buffs.special.skills;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.bonuses.Levitation;
import com.ravenwolf.nnypdcn.actors.buffs.bonuses.Shielding;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.NPC;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.BlastWave;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.particles.ShaftParticle;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;


public class ValkyrieSkill extends BuffSkill {

    {
        CD = 100f;
    }

    @Override
    public void doAction(){
        Hero hero=Dungeon.hero;
        int cell=hero.pos;

    /*    if (hero.belongings.weap2 instanceof Shield) {
            Buff.affect(hero, Guard.class).reset(((Shield) hero.belongings.weap2).guardTurns());
        }else if (hero.belongings.weap1 instanceof Weapon || hero.belongings.weap2 instanceof Weapon ) {
            Buff.affect(hero, Guard.class).reset(2);
        }
*/
        Camera.main.shake( 3, 0.1f );
        Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, 0.8f );
        if( Dungeon.visible[ cell ] ){
            BlastWave.createAtPos( cell );
        }
        //first knock enemies at two range
        for (int i : Level.NEIGHBOURS16) {
            try {
                Char n = Actor.findChar(cell + i);
                //has path to the enemy (no wall or other mob in between)
                if ( n!=null && !(n instanceof NPC) && n.pos== Ballistica.cast(cell, n.pos, false, false)) {
                    n.knockBack(cell, (int) Math.sqrt(n.totalHealthValue() )*3, 2);
                    n.delay(Actor.TICK);
                }

            }catch (ArrayIndexOutOfBoundsException e){}//could be searching beyond the map limits
        }

        Char n;
        for (int i : Level.NEIGHBOURS8) {
            n = Actor.findChar(cell + i);
            if (n!=null && !(n instanceof NPC)) {
                n.knockBack(cell, (int)Math.sqrt( n.totalHealthValue())*3, 3);
                n.delay(Actor.TICK);
            }
        }
        Camera.main.shake(2, 0.5f);
        Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, 0.8f );
        BlastWave.createAtPos( cell );

        //BuffActive.add( hero, ValkyrieBlessing.class, 20);
        BuffActive.add( hero, Levitation.class, 25);
        BuffActive.add( hero, Shielding.class, 25);
        CellEmitter.center(hero.pos).burst(ShaftParticle.FACTORY, 4);
        Sample.INSTANCE.play(Assets.SND_EVOKE,1,1,0.5f);
        Sample.INSTANCE.play(Assets.SND_TELEPORT,1,1,1.5f);

        Dungeon.hero.spendAndNext(1f);
        setCD(getMaxCD());
    }
}
