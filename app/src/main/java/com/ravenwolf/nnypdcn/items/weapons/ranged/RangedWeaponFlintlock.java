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
package com.ravenwolf.nnypdcn.items.weapons.ranged;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.DungeonTilemap;
import com.ravenwolf.nnypdcn.actors.buffs.special.Satiety;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.misc.Explosives;
import com.ravenwolf.nnypdcn.items.weapons.enchantments.Tempered;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.Spark;
import com.ravenwolf.nnypdcn.visuals.effects.particles.SmokeParticle;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;

public abstract class RangedWeaponFlintlock extends RangedWeapon {

    public boolean loaded = false;

	public RangedWeaponFlintlock(int tier) {

		super( tier );

	}

    protected int[][] weapRangedAtk() {
        return new int[][]{	{1, 2, 2/*, 2, 0*/ },	//frame
                {3, 3, 2/*, 3, 3 */},	//x
                {0, 0, 0/*, 0, 0*/}};
    }
    protected int[][] weapAtkEnd() {
        return new int[][]{	{2, 2, 0 },	//frame
                {2, 3, 3 },	//x
                {0, 0, 0}};
    }


    public int[][] getDrawData(int action){
        if (action == HeroSprite.ANIM_RANGED_ATTACK)
            return weapRangedAtk();
        else if (action == HeroSprite.ANIM_RANGED_ATTACK_END)
            return weapAtkEnd();
        else
            return super.getDrawData(action);
    }

    public static final String AC_RELOAD = "装弹";

    protected static final String TXT_POWDER_NEEDED = "你没有足够的弹药为其进行装填。";
    protected static final String TXT_NOT_LOADED = "这把武器没有装填";
    protected static final String TXT_ALREADY_LOADED = "这把武器已经完成装填";
    protected static final String TXT_RELOADING = "装填中...";

    @Override
    public int min( int bonus ) {
        return tier + 5 + bonus + ( enchantment instanceof Tempered && isCursedKnown()? !isCursed()? 1 + bonus : -1 : 0 ) ;
    }

    @Override
    public int max( int bonus ) {
        //return tier * 4 + state * dmgMod() + 4
        //return 7+ tier * (state+4)
        return 10+ tier * 6
                //+ ( enchantment instanceof Tempered || bonus >= 0 ? bonus * dmgMod() : 0 )
                +  bonus * dmgMod()
                //+ ( enchantment instanceof Tempered && bonus >= 0 ? 1 + bonus : 0 ) ;
                + ( enchantment instanceof Tempered && isCursedKnown()? !isCursed()? tier + bonus*2 : -dmgMod() : 0 );
    }

    public int dmgMod() {
        return tier + 1;
    }

    @Override
    public int str(int bonus) {
        return 8 + tier * 2 - strMod(bonus);
    }

    @Override
    public int penaltyBase() {
        return super.penaltyBase()+tier * 4 - 4;
    }


    @Override
    public float breakingRateWhenShot() {
        return 0.1f;
    }

    @Override
    public String descType() {
        return "燧发";
    }

//    @Override
//    public String status() {
//        return isEquipped( Dungeon.hero ) ? (
//                ( ammunition().isInstance( Dungeon.hero.belongings.weap2 ) ? Integer.toString( Dungeon.hero.belongings.weap2.quantity ) : "-" )
//                + "/" + ( Dungeon.hero.belongings.getItem( Gunpowder.class ) != null ? Dungeon.hero.belongings.getItem( Gunpowder.class ).quantity : "-" )
//        ) : null ;
//    }

    @Override
    public String equipAction() {
        return loaded ? AC_SHOOT : AC_RELOAD ;
    }

    @Override
    public boolean checkAmmo( Hero hero, boolean showMessage ) {

        if (!isEquipped(hero)) {

            if( showMessage ) {
                GLog.n(TXT_NOTEQUIPPED);
            }

        } else if ( !loaded ) {

//            if( showMessage ) {
//                GLog.n(TXT_NOT_LOADED);
//            }

            execute( hero, AC_RELOAD );

        } else  if (ammunition() == null || !ammunition().isInstance( hero.belongings.ranged )) {

            if( showMessage ) {
                GLog.n(TXT_AMMO_NEEDED);
            }

        } else {

            return true;

        }

        return false;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_SHOOT) {

            curUser = hero;
            curItem = this;

            if( !loaded ){

                execute( hero, AC_RELOAD );

            } else if ( checkAmmo( hero, true ) ){

                GameScene.selectCell(shooter);

            }

        } else if (action == AC_RELOAD) {

            curUser = hero;

            if( reload(hero ) ) {

                curUser.sprite.operate( curUser.pos );

                curUser.spend( curUser.attackDelay() * 0.5f );

                hero.buff( Satiety.class ).decrease( (float)str() / hero.STR() * 0.5f );

                hero.busy();

            } else if (!isEquipped(hero)) {

                GLog.n( TXT_NOTEQUIPPED );
                hero.ready();

            } else if ( loaded ) {

                GLog.n( TXT_ALREADY_LOADED );
                hero.ready();

            } else {

                GLog.n( TXT_POWDER_NEEDED );
                hero.ready();

            }

        } else {

            super.execute( hero, action );

        }
    }

    protected int requiredPowder(){
        return (tier+1)/2;
    }

    public boolean reload( Hero hero){

        curItem = this;

        Item powder = Dungeon.hero.belongings.getItem( Explosives.Gunpowder.class );

        if ( isEquipped(hero) && !loaded && powder != null && requiredPowder() <= powder.quantity ) {

            loaded = true;

            if( powder.quantity <= requiredPowder() ){

                powder.detachAll( Dungeon.hero.belongings.backpack );

            } else {
                powder.quantity -= requiredPowder();
            }

            curItem.updateQuickslot();

            Sample.INSTANCE.play( Assets.SND_TRAP, 0.6f, 0.6f, 0.5f );

            hero.sprite.showStatus( CharSprite.DEFAULT, TXT_RELOADING );

            return true;

        } else {

            return false;

        }
    }
/*
    public static CellSelector.Listener shooter = new CellSelector.Listener() {

        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                final RangedWeaponFlintlock curWeap = (RangedWeaponFlintlock)RangedWeaponFlintlock.curItem;

                if( curUser.buff( Dazed.class ) != null ) {
                    target += Level.NEIGHBOURS8[Random.Int( 8 )];
                }

                final int cell = Ballistica.cast(curUser.pos, target, false, true);

                Char ch = Actor.findChar( cell );

                if( ch != null && curUser != ch && Dungeon.visible[ cell ] ) {

//                    if ( curUser.isCharmedBy( ch ) ) {
//                        GLog.i( TXT_TARGET_CHARMED );
//                        return;
//                    }

                    QuickSlot.target(curItem, ch);
                    TagAttack.target( (Mob)ch );
                }

                ((HeroSprite)curUser.sprite).shoot(cell, curWeap, new Callback() {
                    @Override
                    public void call() {

                    curUser.busy();

                    ((MissileSprite) curUser.sprite.parent.recycle(MissileSprite.class)).
                            reset(curUser.pos, cell, curUser.belongings.ranged, 3.0f, new Callback() {
                                @Override
                                public void call() {
                                    ((ThrowingWeaponAmmo) curUser.belongings.ranged).onShoot(cell, curWeap);
                                }
                            });

                    curUser.buff( Satiety.class ).decrease( (float)curWeap.str() / curUser.STR() );
                    //curWeap.loaded = false;
                    curWeap.use( 2 );

                    curWeap.afterShoot(cell);

//                    Sample.INSTANCE.play(Assets.SND_BLAST,curWeap.tier);
//                    //Sample.INSTANCE.play(Assets.SND_BLAST, 0.4f + curWeap.tier * 0.2f, 0.4f + curWeap.tier * 0.2f, 1.55f - curWeap.tier * 0.15f);
//                    Camera.main.shake(curWeap.tier, 0.1f);
//
//                    PointF pf = DungeonTilemap.tileCenterToWorld(curUser.pos);
//                    PointF pt = DungeonTilemap.tileCenterToWorld(cell);
//
//                    curUser.sprite.emitter().burst(SmokeParticle.FACTORY, 3 + curWeap.tier);
//                    Spark.at(pf, PointF.angle(pf, pt), 3.1415926f / 12, 0xEE7722, 3 + curWeap.tier);
//                    //play end animation
//                    ((HeroSprite) curUser.sprite).shootEnd(curWeap);
                    }
                });

                for (Mob mob : Dungeon.level.mobs) {
                    if ( Level.distance( curUser.pos, mob.pos ) <= 3 + curWeap.tier && mob.pos != cell ) {
                        mob.beckon( curUser.pos );
                    }
                }

                Invisibility.dispel();

            }
        }

        @Override
        public String prompt() {
            return "Choose target to shoot at";
        }
    };*/

    @Override
    protected float bulletSpeed(){
        return 3f;
    }

    @Override
    protected void onShoot(int cell) {
        int power=(tier+1)/2;
        for (Mob mob : Dungeon.level.mobs) {
            if ( Level.distance( curUser.pos, mob.pos ) <= 3 + power && mob.pos != cell ) {
                mob.beckon( curUser.pos );
            }
        }
        loaded = false;
    }

    @Override
    protected void afterShoot(int cell ) {
        int power=(tier+1)/2;

        Sample.INSTANCE.play(Assets.SND_BLAST,power);
        //Sample.INSTANCE.play(Assets.SND_BLAST, 0.4f + curWeap.tier * 0.2f, 0.4f + curWeap.tier * 0.2f, 1.55f - curWeap.tier * 0.15f);
        Camera.main.shake(power, 0.1f);

        PointF pf = DungeonTilemap.tileCenterToWorld(curUser.pos);
        PointF pt = DungeonTilemap.tileCenterToWorld(cell);

        curUser.sprite.emitter().burst(SmokeParticle.FACTORY, 3 + power);
        Spark.at(pf, PointF.angle(pf, pt), 3.1415926f / 12, 0xEE7722, 3 + power);
        //play end animation
        ((HeroSprite) curUser.sprite).shootEnd(this);

    }

    private static final String LOADED	= "loaded";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LOADED, loaded );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        loaded = bundle.getBoolean( LOADED );
    }
}