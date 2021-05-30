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
package com.ravenwolf.nnypdcn.items.misc;

import com.ravenwolf.nnypdcn.Badges;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.hero.HeroSubClass;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.misc.utils.Utils;
import com.ravenwolf.nnypdcn.scenes.GameScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.Speck;
import com.ravenwolf.nnypdcn.visuals.effects.SpellSprite;
import com.ravenwolf.nnypdcn.visuals.windows.WndChooseWay;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class TomeOfMastery extends Item {

	private static final String TXT_BLINDED	= "你不能在失明时阅读书籍。";
	
	public static final float TIME_TO_READ = 10;
	
	public static final String AC_READ	= "阅读";
	
	{
		stackable = false;
		name = "精通之书";
//		image = ItemSpriteSheet.MASTERY;
		
		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {

			curUser = hero;
			
			HeroSubClass way1 = null;
			HeroSubClass way2 = null;
			switch (hero.heroClass) {
			case WARRIOR:
				way1 = HeroSubClass.GLADIATOR;
				way2 = HeroSubClass.BERSERKER;
				break;
			case SCHOLAR:
				way1 = HeroSubClass.BATTLEMAGE;
				way2 = HeroSubClass.WARLOCK;
				break;
			case BRIGAND:
				way1 = HeroSubClass.FREERUNNER;
				way2 = HeroSubClass.ASSASSIN;
				break;
			case ACOLYTE:
				way1 = HeroSubClass.SNIPER;
				way2 = HeroSubClass.WARDEN;
				break;
			}
			GameScene.show( new WndChooseWay( this, way1, way2 ) );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean doPickUp( Hero hero ) {
		Badges.validateMastery();
		return super.doPickUp( hero );
	}
	
	@Override
	public String info() {
		return 
			"这本破旧的书籍没多厚，但是你能隐约感觉到自己能丛总学到不少东西，不过，阅读它可能需要耗费一些时间";
	}
	
	public void choose( HeroSubClass way ) {
		
		detach( curUser.belongings.backpack );
		
		curUser.spend( TomeOfMastery.TIME_TO_READ );
		curUser.busy();
		
		curUser.subClass = way;
		
		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.SND_MASTERY );
		
		SpellSprite.show( curUser, SpellSprite.MASTERY );
		curUser.sprite.emitter().burst( Speck.factory( Speck.MASTERY ), 12 );
		GLog.w( "你选择了走上%s的道路！", Utils.capitalize( way.title() ) );
		
//		if (way == HeroSubClass.BERSERKER && curUser.HP <= curUser.HT * Fury.LEVEL) {
//			Buff.affect( curUser, Fury.class );
//		}
	}
}
