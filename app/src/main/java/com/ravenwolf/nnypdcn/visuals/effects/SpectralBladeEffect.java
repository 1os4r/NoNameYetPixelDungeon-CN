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

package com.ravenwolf.nnypdcn.visuals.effects;

import com.ravenwolf.nnypdcn.DungeonTilemap;
import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSprite;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.PointF;


public class SpectralBladeEffect extends ItemSprite implements Tweener.Listener {

    private static final int SIZE	= 16;
    public static final float MAX_DISTANCE	= 12f;

    private enum Phase {
        FADE_IN, STATIC, FADE_OUT
    }

    private static final float FADE_IN_TIME		= 0.1f;
    private static final float STATIC_TIME		= 0.4f;
    private static final float FADE_OUT_TIME	= 0.2f;

    private static final float ALPHA	= 0.9f;

    private int color;

    private Char source;
    private Char target;

    private Phase phase;
    private float duration;
    private float passed;

    public SpectralBladeEffect(Item item) {
        super( item.image(), null );
        originToCenter();


        phase = Phase.FADE_IN;
        duration = FADE_IN_TIME;
        passed = 0;
    }

    @Override
    public void update() {
        super.update();

        switch (phase) {
            case FADE_IN:

                alpha( passed / duration * ALPHA );
                //scale.set( 0.2f );
                break;
            case STATIC:
                tint( color, passed / duration * 0.8f );
                //scale.set( 0.8f );
                break;
            case FADE_OUT:
                alpha( (1 - passed / duration) * ALPHA );
                //scale.set(  0.2f );
                break;
        }

        if ((passed += Game.elapsed) > duration) {
            switch (phase) {
                case FADE_IN:
                    phase = Phase.STATIC;
                    duration = STATIC_TIME;
                    break;
                case STATIC:
                    phase = Phase.FADE_OUT;
                    duration = FADE_OUT_TIME;
                    break;
                case FADE_OUT:
                    kill();
                    break;
            }

            passed = 0;
        }
    }

    private void rotateEffect()
    {
        PointF src = DungeonTilemap.tileToWorld( source.pos );
        PointF dest = DungeonTilemap.tileToWorld( target.pos );
        PointF d = PointF.diff( dest, src );
        angle = 135 - (float)(Math.atan2( d.x, d.y ) / 3.1415926 * 180);
        //angularSpeed = 70;

        x = source.sprite.x;
        y = source.sprite.y;

        //  if(d.x > 0)
        //      x += 2 * SIZE / 3;
        //  else  if(d.x < 0)
        //      x -= 2 * SIZE / 3;

        //if(d.y > 0)
        //     y += 2 * SIZE / 3;
        // else  if(d.y < 0)
        //    y -= 2 * SIZE / 3;

        int distance=Level.distance(source.pos,target.pos);
        //float speed=distance/MAX_DISTANCE;
        float speed=distance*0.1f;

        PosTweener tweener = new PosTweener( this, dest, speed );
        tweener.listener = this;
        source.sprite.parent.add( tweener );
    }

    public static void show(Char ch, Char ch2, Item item ) {

        if (!ch.sprite.visible) {
            return;
        }

        SpectralBladeEffect sprite = new SpectralBladeEffect( item );
        sprite.source = ch;
        sprite.target = ch2;
        sprite.rotateEffect();
        ch.sprite.parent.add( sprite );
    }

    @Override
    public void onComplete( Tweener tweener ) {
        kill();
    }
}