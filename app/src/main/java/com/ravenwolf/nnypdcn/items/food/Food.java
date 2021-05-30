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
package com.ravenwolf.nnypdcn.items.food;

import com.ravenwolf.nnypdcn.Badges;
import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Statistics;
import com.ravenwolf.nnypdcn.actors.buffs.special.Satiety;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.CarrionSwarm;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.SpellSprite;
import com.ravenwolf.nnypdcn.visuals.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public abstract class Food extends Item {

    private static final String TXT_STUFFED		= "你吃的太多了..不能再吃了";

    private static final String TXT_NOT_THAT_HUNGRY = "不要浪费食物！";

    private static final String TXT_R_U_SURE =
        "你的饱食度无法超过100%，所以在吃下这份食物前最好能再多等一阵。你确定要食用它吗？";

    private static final String TXT_YES			= "是的，我知道我在做什么";
    private static final String TXT_NO			= "不，我改变主意了";
	
	public static final String AC_EAT	= "食用";

	public float time;
	public float energy;
	public String message;

	{
		stackable = true;
        time = 3f;
	}

    @Override
    public String quickAction() {
        return AC_EAT;
    }
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute( final Hero hero, String action ) {

		if ( action.equals( AC_EAT ) && hero != null ) {

            final Satiety hunger = hero.buff(Satiety.class);

                if( hunger.energy() + energy > Satiety.MAXIMUM ){

                    GameScene.show(
                        new WndOptions( TXT_NOT_THAT_HUNGRY, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
                            @Override
                            protected void onSelect( int index ){
                                if( index == 0 ){
                                    consume( hunger, hero );
                                }
                            }
                        }
                    );

                } else {
                    consume( hunger, hero );
                    }
		} else {
			super.execute( hero, action );
		}
	}

    private void consume( Satiety hunger, Hero hero ) {

        hunger.increase(energy);
        detach(hero.belongings.backpack);
        onConsume( hero );

        hero.sprite.operate( hero.pos );
        hero.busy();
        SpellSprite.show( hero, SpellSprite.FOOD );
        Sample.INSTANCE.play( Assets.SND_EAT );

        hero.spend( time );

        for (Mob mob : Dungeon.level.mobs) {
            if ( mob instanceof CarrionSwarm ) {
                mob.beckon( hero.pos );
            }
        }

        Statistics.foodEaten++;
        Badges.validateFoodEaten();
        updateQuickslot();
    }
	
	@Override
	public String desc() {
		return 
			"这里没什么有意思的东西：肉干和一些饼干--之类的东西";
	}
	
	@Override
	public int price() {
		return 30 * quantity;
	}

    public void onConsume( Hero hero ) {
        GLog.i( message );
    }

    @Override
    public String info() {
        return desc() + "\n\n" +
            "吃掉它会消耗 _" + (int)time + "_回合，并回复" +
            "_" + (int)( energy / 10 ) + "%_饱食度.";
    }

}