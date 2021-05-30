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
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class ShadowStrike extends BuffSkill {


    {
        CD = 100f;
    }

    final ArrayList<Mob> targets = new ArrayList<Mob>();
    int intialPos;

    @Override
    public void doAction() {

        Hero hero=Dungeon.hero;
        intialPos=hero.pos;

        MeleeWeapon curWep = Dungeon.hero.belongings.weap1 instanceof MeleeWeapon ? (MeleeWeapon)Dungeon.hero.belongings.weap1 :
                Dungeon.hero.belongings.weap2 instanceof MeleeWeapon ? (MeleeWeapon) Dungeon.hero.belongings.weap2 : null;

        if (curWep == null) {
            GLog.i("你需要装备一个近战武器来使用此技能");
            return;
        }
        Dungeon.hero.currentWeapon = curWep;

        for (Mob mob : Dungeon.level.mobs) {
            if (Level.fieldOfView[mob.pos] && Level.distance(hero.pos,mob.pos)<=6 && mob.hostile) {
                targets.add( mob );
            }
        }

        setCD(getMaxCD());
        doAttack();
        hero.busy();
    }


    private void doAttack(){
        Hero hero = Dungeon.hero;

        if (targets.isEmpty()) {
            appear( hero, intialPos );
            //CharmOfBlink.appear( hero, intialPos );
            hero.spendAndNext( hero.attackDelay() );
            return;
        }

        Mob mob=targets.get(0);

        ///CharmOfBlink.appear( hero, mob.pos );
        appear( hero, mob.pos );

        ((HeroSprite)hero.sprite).attack(hero.currentWeapon,mob.pos, new Callback() {
            @Override
            public void call() {

                Mob mob=targets.remove(0);
                Hero hero = Dungeon.hero;
                int damage = hero.damageRoll();
                //weapon penalty reduce damage efficiency
                float damagePenalty =1- hero.currentWeapon.currentPenalty(hero,hero.currentWeapon.str())*0.025f;

                for (int j = 0; j < 2; j++) {
                    int auxDmg = hero.damageRoll();
                    if (auxDmg > damage)
                        damage = auxDmg;
                }
                damage=(int)(damage*damagePenalty);
                //make them wandering so attack is a backstab
                mob.looseTrack();
                hero.hitEnemy(mob,  damage);
                //curUser.attack( targets.get( this ) );

                doAttack();
            }});

        CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 4 );

    }


    public void appear( Char ch, int pos ) {

        ch.sprite.interruptMotion();

        //ch.move( pos );
        ch.sprite.place( pos );

        if (ch.invisible == 0) {
            ch.sprite.alpha( 0 );
            ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
        }

        //ch.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
        Sample.INSTANCE.play( Assets.SND_TELEPORT );
    }
}
