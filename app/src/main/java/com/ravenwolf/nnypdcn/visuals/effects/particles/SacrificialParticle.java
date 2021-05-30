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
package com.ravenwolf.nnypdcn.visuals.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class SacrificialParticle extends PixelParticle {

    public static final Emitter.Factory FACTORY = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((SacrificialParticle)emitter.recycle( SacrificialParticle.class )).reset( x, y );
        }
        @Override
        public boolean lightMode() {
            return true;
        }
    };

    public static final Emitter.Factory CURSE = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((SacrificialParticle)emitter.recycle( SacrificialParticle.class )).resetCurse( x, y );
        }
    };

    public SacrificialParticle() {
        super();

        //color( 0x4488EE );
        lifespan = 0.4f;

        acc.set( 0, -80 );
    }

    public void reset( float x, float y ) {
        revive();

        this.x = x;
        this.y = y - 4;

        left = lifespan;

        size = 4;
        speed.set( 0 );
    }

    public void resetCurse( float x, float y ) {
        revive();

        size = 8;

        speed.polar( Random.Float( PointF.PI2 ), Random.Float( 16, 32 ) );
        this.x = x - speed.x * lifespan;
        this.y = y - speed.y * lifespan;
    }

    @Override
    public void update() {
        super.update();
        float p = left / lifespan;
        color( ColorMath.interpolate( 0x3366AA, 0x4488EE, p ) );
        am = p > 0.75f ? (1 - p) * 4 : 1;
    }
}