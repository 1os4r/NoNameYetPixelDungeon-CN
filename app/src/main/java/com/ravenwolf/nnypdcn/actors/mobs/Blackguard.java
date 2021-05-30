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
import com.ravenwolf.nnypdcn.items.Generator;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.items.weapons.melee.DoubleBlade;
import com.ravenwolf.nnypdcn.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.visuals.sprites.DeathKnightSprite;
import com.watabou.utils.Bundle;

public class Blackguard extends MobHealthy {

    public MeleeWeapon weap;

    public Blackguard() {

        super( 20 );

		name = "死亡骑士";
		spriteClass = DeathKnightSprite.class;

        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );

        //resistances.put( Element.Energy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

        Weapon weapTmp =null;
        while (!(weapTmp instanceof MeleeWeapon) ){
            weapTmp = (Weapon) Generator.random( Generator.Category.WEAPON );
            if (/* weapTmp.cursed ||*/ weapTmp.lootChapter()<3)
                weapTmp=null;
        }

        weap=(MeleeWeapon)weapTmp;
        weap.identify(Item.ENCHANT_KNOWN);
        minDamage=weap.min()+6-weap.lootChapter();
        maxDamage=weap.max();

        //accuracy is affected by weapon penalty
        accuracy = (int)((accuracy+ tier) * (1- weap.penaltyBase()*0.025f));

	}

	public float willpower(){
        return super.willpower()*2;
    }


    protected int meleeAttackRange() {
        return weap.reach();
    }

    @Override
    public float attackSpeed() {
        return super.attackSpeed() *  weap.speedFactor(this);
    }

    protected boolean canAttack( Char enemy ) {
        return super.canAttack(enemy) || Level.distance( pos, enemy.pos ) <=meleeAttackRange()  &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    public boolean isMagical() {
        return true;
    }

	@Override
	public String description() {
		return
			"这个死亡骑士曾经也像你一样，但是现在，它的灵魂被永远的禁锢在这恶魔大厅中。这个死亡骑士正装备着_"+weap.name()+"_.";
	}

    public void onAttackComplete() {

        if(weap instanceof DoubleBlade) {
            for (int n : Level.NEIGHBOURS8) {
                int auxPos = pos + n;

                if (Level.solid[auxPos] || auxPos == enemy.pos) {//ignore main target
                    continue;
                }

                Char ch = Actor.findChar(auxPos);
                if (ch != null && ch.isFriendly()) {
                    attack(ch);
                }
            }
        }

        super.onAttackComplete();
    }

    @Override
    public int attackProc(Char enemy, int damage, boolean blocked ) {
        if ( !blocked) {
            damage=weap.proc(this,enemy,damage);
        }
        return damage;
    }

    @Override
    public void die( Object cause, Element dmg ) {

        super.die( cause, dmg );

        if (weap != null) {
            Dungeon.level.drop( weap, pos ).sprite.drop();
        }
    }
/*
    @Override
    protected boolean canAttack( Char enemy ) {
        runeCD--;
        return ( super.canAttack( enemy ) || runeCD <= 0 && Random.Int(4)==0 && getPosibleRuneTargets(enemy).size()>0 &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos);
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.purpleLight(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);

        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {

        runeCD =Random.IntRange(10,16);
        ArrayList<Integer> candidates = getPosibleRuneTargets(enemy);

        int ammountOfRunes=Math.min(3,candidates.size());

        for (int i =0; i<ammountOfRunes; i++) {

            Integer targetCell = candidates.get(Random.Int(candidates.size()));
            candidates.remove(targetCell);

            FellRune rune = new FellRune();
            rune.setValues( targetCell, damageRoll(), Random.IntRange(4,7) );

            GameScene.add( rune );
            ( (FellRune.RuneSprite) rune.sprite ).appear();
        }

        return true;
    }

    private ArrayList<Integer> getPosibleRuneTargets(Char enemy){

        ArrayList<Integer> candidates = new ArrayList<Integer>();
        for (int i : Level.NEIGHBOURS8) {
            int auxPos=enemy.pos + i;
            if (!Level.solid[auxPos] && !Level.chasm[auxPos]  && Level.distance(pos,auxPos)>=Level.distance(pos,enemy.pos)) {
                candidates.add( auxPos );
            }
        }
        return candidates;

    }
*/

//    @Override
//    protected boolean canAttack( Char enemy ) {
//        return super.canAttack( enemy ) || /*ammo > 0 &&*/ Level.distance(pos, enemy.pos) <= 4
//                && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
//    }
//
//    @Override
//    protected void onRangedAttack( int cell ) {
//        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
//                reset(pos, cell, new Hammers(), new Callback() {
//                    @Override
//                    public void call() {
//                        onAttackComplete();
//                    }
//                });
//       // ammo--;
//        super.onRangedAttack( cell );
//    }
//
//    @Override
//    public int attackProc(Char enemy, int damage, boolean blocked ) {
//        if ( isRanged()) {
//
//            if ( !blocked && Random.Int( enemy.HT ) < damage *2) {
//                BuffActive.addFromDamage( enemy, Dazed.class, damage );
//            }
//        }
//
//        return damage;
//    }

    private static final String WEAPON = "weapon";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(WEAPON, weap );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        weap = (MeleeWeapon)bundle.get(WEAPON);
    }

}
