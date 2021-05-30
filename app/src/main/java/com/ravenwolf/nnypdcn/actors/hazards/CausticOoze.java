package com.ravenwolf.nnypdcn.actors.hazards;
//
//
//import com.ravenwolf.nnypdcn.actors.Actor;
//import com.ravenwolf.nnypdcn.actors.Char;
//import com.ravenwolf.nnypdcn.actors.buffs.BuffActive;
//import com.ravenwolf.nnypdcn.actors.buffs.debuffs.Corrosion;
//import com.ravenwolf.nnypdcn.levels.Level;
//import com.ravenwolf.nnypdcn.scenes.GameScene;
//import com.ravenwolf.nnypdcn.visuals.Assets;
//import com.ravenwolf.nnypdcn.visuals.effects.CellEmitter;
//import com.ravenwolf.nnypdcn.visuals.effects.Speck;
//import com.ravenwolf.nnypdcn.visuals.effects.particles.AcidParticle;
//import com.ravenwolf.nnypdcn.visuals.sprites.HazardSprite;
//import com.watabou.noosa.Game;
//import com.watabou.noosa.particles.Emitter;
//import com.watabou.noosa.tweeners.AlphaTweener;
//
//import com.watabou.utils.Bundle;
//
//
///*
// * Pixel Dungeon
// * Copyright (C) 2012-2015 Oleg Dolya
// *
// * Yet Another Pixel Dungeon
// * Copyright (C) 2015-2016 Considered Hamster
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/>
// */
//public class CausticOoze extends Hazard {
//
//    private static int MAX_LEVEL = 4;
//    private int duration;
//
//    public CausticOoze() {
//        super();
//
//        this.pos = 0;
//        this.duration = 0;
//
//        spriteClass = OozeSprite.class;
//        var = 0;
//
//    }
//
//    public void setValues( int pos, int duration ) {
//
//        this.pos = pos;
//        this.duration = duration;
//
//    }
//
//    @Override
//    public void press( int cell, Char ch ){
//        if( ch != null && !ch.flying ){
//
//            BuffActive.add( ch, Corrosion.class, TICK * 2 );
//            CellEmitter.center( cell ).burst( AcidParticle.BURST, 4 );
//
//        } else if( ch == null ) {
//
//            CellEmitter.center( cell ).burst( AcidParticle.BURST, 4 );
//
//        }
//    }
//
//    @Override
//    protected boolean act(){
//
//        if( --duration > 0 ){
//
//            Char ch;
//            if ( ( ch = Actor.findChar( pos )) != null && !ch.flying ) {
//                BuffActive.add( ch, Corrosion.class, TICK * 2 );
//            }
//
//            updateSprite();
//            spend( TICK );
//
//        } else {
//            ((CausticOoze.OozeSprite)sprite).disappear();
//            destroy();
//        }
//
//        return true;
//    }
//
//    public void updateSprite() {
//        var = Math.min( duration / 3, MAX_LEVEL );
//        sprite.changeFrame( var );
//    }
//
//    public static void spawn( int cell, int duration ) {
//
//        if( !Level.water[ cell ] && Level.passable[ cell ] ){
//
//            CausticOoze ooze = Hazard.findHazard( cell, CausticOoze.class );
//
//            if( ooze == null ){
//
//                ooze = new CausticOoze();
//                ooze.setValues( cell, duration );
//
//                GameScene.add( ooze );
//                ooze.updateSprite();
//                ( (CausticOoze.OozeSprite) ooze.sprite ).appear();
//
//            } else {
//
//                ooze.duration += duration;
//
//            }
//        }
//    }
//
//    public static class OozeSprite extends HazardSprite {
//
//        private static float ANIM_TIME = 0.5f;
//
//        private float time;
//        private float size;
//
//        protected Emitter vapours;
//
//        public OozeSprite(){
//
//            super();
//            time = 0.0f;
//            size = 1.0f;
//
//        }
//
//        @Override
//        protected String asset(){
//            return Assets.HAZ_OOZE;
//        }
//
//        @Override
//        public int spritePriority(){
//            return 1;
//        }
//
//        @Override
//        public void link( Hazard hazard ) {
//
//            super.link( hazard );
//
//            vapours = GameScene.emitter();
//
//            if( vapours != null ){
//                vapours.pos( this );
//                vapours.pour( Speck.factory( Speck.CAUSTIC ), 1.0f );
//            }
//
//            parent.add( vapours );
//        }
//
//        @Override
//        public void update() {
//            super.update();
//
//            time += Game.elapsed * 2;
//            scale.set( 0.95f + (float)Math.sin( time ) * 0.05f );
//
//            if (vapours != null) {
//                vapours.visible = visible;
//            }
//
//        }
//
//        public void appear( ) {
//
//            alpha( 0f );
//
//            parent.add( new AlphaTweener( this, 0.75f, ANIM_TIME ) {
//                @Override
//                protected void onComplete() {
//                    parent.erase( this );
//                }
//            });
//        }
//
//        public void disappear( ) {
//
//            if (vapours != null) {
//                vapours.on = false;
//                vapours = null;
//            }
//
//            parent.add( new AlphaTweener( this, 0.0f, ANIM_TIME ) {
//                @Override
//                protected void onComplete() {
//                    OozeSprite.this.killAndErase();
//                    parent.erase( this );
//                }
//            });
//        }
//    }
//
//
//    private static final String DURATION = "duration";
//
//    @Override
//    public void storeInBundle( Bundle bundle ) {
//
//        super.storeInBundle( bundle );
//
//        bundle.put( DURATION, duration );
//
//    }
//
//    @Override
//    public void restoreFromBundle( Bundle bundle ) {
//
//        super.restoreFromBundle( bundle );
//
//        duration = bundle.getInt( DURATION );
//
//    }
//}
