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
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Withered;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.rings.RingOfAccuracy;
import com.ravenwolf.nnypdcn.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.sprites.MissileSprite;
import com.watabou.utils.Callback;

import java.util.HashMap;


public class SpectralBladesSkill extends BuffSkill {

    {
        CD = 75f;
    }

    private SpectralBlade weapSpectralBlade=new SpectralBlade();

    @Override
    public void doAction(){
        Hero hero=Dungeon.hero;
        int itemSprite= ItemSpriteSheet.SPECTRAL_BLADE;

        final HashMap<Callback, Mob> targets = new HashMap<Callback, Mob>();
        final HashMap<Callback, Mob> targets2 = new HashMap<Callback, Mob>();

        final Hero curUser=hero;
/*
        Weapon curWep = Dungeon.hero.belongings.weap1 instanceof MeleeWeapon ? Dungeon.hero.belongings.weap1 :
                Dungeon.hero.belongings.weap1 instanceof RangedWeapon ? (RangedWeapon) Dungeon.hero.belongings.weap1 :
                        Dungeon.hero.belongings.weap2 instanceof MeleeWeapon ? (MeleeWeapon) Dungeon.hero.belongings.weap2 : null;
        Dungeon.hero.currentWeapon = curWep;*/
        hero.currentWeapon=weapSpectralBlade;

        for (Mob mob : Dungeon.level.mobs) {
            if (mob.hostile && Level.fieldOfView[mob.pos] && Level.distance(curUser.pos,mob.pos)<=8) {

                Callback callback = new Callback() {
                    @Override
                    public void call() {
                        //best of tree rolls
                        int damage = Dungeon.hero.damageRoll();
                        /*for (int j = 0; j < 2; j++) {
                            int auxDmg = Dungeon.hero.damageRoll();
                            if (auxDmg > damage)
                                damage = auxDmg;
                        }
                        //weapon penalty reduce damage efficiency
                        float damagePenalty =1- curUser.currentWeapon.currentPenalty(curUser,curUser.currentWeapon.str())*0.025f;
                        damage=(int)(damage*damagePenalty);

                        Dungeon.hero.hitEnemy(targets.get( this ),  damage);

                        //double hit
                        Dungeon.hero.hitEnemy(targets.get( this ),  damage);*/
                        Mob mob=targets.get( this );
                        int auxDmg=Dungeon.hero.hitEnemy(mob, damage);
                        if(mob.isAlive()) {
                            BuffActive.addFromDamage(mob, Withered.class, auxDmg*2);
                        }

                        targets.remove( this );
                        if (targets.isEmpty()) {
                            curUser.spendAndNext( curUser.attackDelay() );
                        }
                    }
                };

                ((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
                        reset( curUser.pos, mob.pos, itemSprite,1f,new ItemSprite.Glowing(0x000000, 0.6f), callback );

                targets.put( callback, mob );
                targets2.put( callback, mob );
            }
        }

        CellEmitter.get( hero.pos ).burst( Speck.factory( Speck.WOOL ), 4 );
        hero.sprite.cast( curUser.pos );
        curUser.busy();
        setCD(getMaxCD());
    }



    public static class SpectralBlade extends MeleeWeapon {

        {
            name = "幻影飞刀";
        }

        public SpectralBlade() {
            super( 0 );
        }

        private int baseDmg(Hero hero){
            float modifier = hero.ringBuffsHalved( RingOfAccuracy.Accuracy.class );
            return (int)(hero.attackSkill*modifier);
        }

        @Override
        public int min( int bonus ) {
            return baseDmg(Dungeon.hero)/2;
        }

        @Override
        public int max( int bonus ) {
            return baseDmg(Dungeon.hero)*2/3;
        }

        @Override
        public boolean increaseCombo(){
            return false;
        }

        @Override
        protected int getDamageSTBonus(Hero hero){
            //damage is not increased based on Hero ST. only based on accuracy
            return 0;
        }
    }
}
