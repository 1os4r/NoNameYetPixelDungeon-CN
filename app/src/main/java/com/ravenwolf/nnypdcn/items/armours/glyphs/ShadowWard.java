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
package com.ravenwolf.nnypdcn.items.armours.glyphs;

import com.ravenwolf.nnypdcn.actors.Char;
import com.ravenwolf.nnypdcn.actors.blobs.Blob;
import com.ravenwolf.nnypdcn.actors.blobs.Darkness;
import com.ravenwolf.nnypdcn.actors.blobs.ShroudingFog;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.items.armours.Armour;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSprite.Glowing;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ShadowWard extends Armour.Glyph {

    @Override
    public Glowing glowing() {
        return BLACK;
    }


    @Override
    protected String name_p() {
        return "暗影%s";
    }

    @Override
    protected String name_n() {
        return "阴暗%s";
    }

    @Override
    protected String desc_p() {
        return "被击中时会释放出暗影来阻挡敌人的视线，暗影不会对使用者生效";
    }

    @Override
    protected String desc_n() {
        return "被击中时会释放出暗影来阻挡敌人的视线，暗影同样对使用者生效";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage, boolean isShield ) {
        //Melee attacks have 50% chance to proc
        if (!Level.adjacent(attacker.pos, defender.pos) || Random.Int(2)==0) {
            Sample.INSTANCE.play(Assets.SND_GHOST);
            int ammount=80;
            if (isShield)
                ammount=120;
            if (attacker instanceof Hero) //for haunted armors effect
                GameScene.add(Blob.seed(defender.pos, ammount, Darkness.class));
            else
                GameScene.add(Blob.seed(defender.pos, ammount, ShroudingFog.class));
            return true;
        }
        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage, boolean isShield ) {
        //defender.sprite.centerEmitter().start( Speck.factory( Speck.DARKNESS ), 0.3f, 5 );
        Sample.INSTANCE.play( Assets.SND_GHOST );
        GameScene.add( Blob.seed( defender.pos, 60, Darkness.class ) );
        return true;
    }
}
