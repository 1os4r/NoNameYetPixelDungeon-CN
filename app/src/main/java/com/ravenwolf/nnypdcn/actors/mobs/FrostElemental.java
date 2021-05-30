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
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.blobs.Blob;
import com.ravenwolf.nnypdcn.actors.blobs.FrigidVapours;
import com.ravenwolf.nnypdcn.actors.buffs.Buff;
import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Burning;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Chilled;
import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Ensnared;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.mechanics.Ballistica;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.MagicMissile;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.FrostElementalSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class FrostElemental extends MobPrecise {

    private static String TXT_ENSNARED = "元素轻易挣脱了缠绕！";
    private static String TXT_BURN = "烈焰致命的伤害了它！";

    public FrostElemental() {

        super( 14 );

        name = "冰元素";
        spriteClass = FrostElementalSprite.class;

        flying = true;
        armorClass = 0;

        resistances.put( Element.Shock.class, Element.Resist.PARTIAL );
        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Flame.class, Element.Resist.VULNERABLE );

    }

    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean isEthereal() {
        return true;
    }

    @Override
    public Element damageType() {
        return Element.FROST;
    }

    @Override
    public int damageRoll() {
        return super.damageRoll() * 3/4 ;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || Level.distance( pos, enemy.pos ) <= 2 &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos && !isCharmedBy( enemy );
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.frost(sprite.parent, pos, cell,
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
        return castBolt(enemy,damageRoll(),false,Element.FROST);
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if ( type == Element.FROST ) {

            if (HP < HT) {
                int reg = Math.min( dmg / 2, HT - HP );
                if (reg > 0) {
                    HP += reg;
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(reg));
                }
            }

        } else {
            super.damage(dmg, src, type);
        }
    }

    public boolean castBolt( Char enemy , int damageRoll, boolean ignoresArmor,  Element type ) {
        boolean result=super.castBolt(enemy,damageRoll,ignoresArmor,type);
        if ( result && Random.Int( 3 ) ==0 ) {
            BuffActive.addFromDamage( enemy, Chilled.class, damageRoll );
        }
        return result;
    }

    @Override
    public int attackProc(Char enemy, int damage, boolean blocked ) {
        if ( !blocked && Random.Int( 4 ) ==0) {
            BuffActive.addFromDamage( enemy, Chilled.class, damage );
        }
        return damage;
    }
	
	@Override
	public boolean add( Buff buff ) {
		if (buff instanceof Chilled) {
			if (HP < HT) {
                int reg = Math.max( 1, (HT - HP) / 5 );
                if (reg > 0) {
                    HP += reg;
                    sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                    sprite.emitter().burst( Speck.factory( Speck.HEALING ), (int)Math.sqrt( reg ) );
                }
			}
            return false;
		} else if (buff instanceof Burning) {

            damage( Random.NormalIntRange( HT / 6 , HT/4 ), null, null );

            if(Dungeon.visible[pos] ) {
                GLog.w( TXT_BURN );
            }
            return false;

        } else if (buff instanceof Ensnared) {
            if(Dungeon.visible[pos] ) {
                GLog.w( TXT_ENSNARED );
            }
            return false;

        } else {
            return super.add( buff );
        }
	}

    @Override
    public void die( Object cause, Element dmg ) {


        GameScene.add(Blob.seed(pos, 60, FrigidVapours.class));

        super.die( cause, dmg );
    }
	
	@Override
	public String description() {
		return
			"冰元素是一种召唤强大存在时出现的副产物，这些元素的本质太过混乱，即使是强大的恶魔术士也不能将其完全控制";
	}

}
