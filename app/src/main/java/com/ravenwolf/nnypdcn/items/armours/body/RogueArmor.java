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
package com.ravenwolf.nnypdcn.items.armours.body;

import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;

public class RogueArmor extends BodyArmorCloth {
	
//	private static final String TXT_FOV 		= "You can only jump to an empty location in your field of view";
//	private static final String TXT_NOT_ROGUE	= "Only rogues can use this armor!";
//
//	private static final String AC_SPECIAL = "SMOKE BOMB";
	
	{
		name = "盗贼风衣";
		image = ItemSpriteSheet.ARMOR_ROGUE;

        appearance = 2;
	}

    public RogueArmor() {
        super( 1 );
    }

    @Override
    public String desc() {
        return "这件衣物比起抵挡敌人攻击更适合御寒。不过它昏暗的颜色可让使用者在地牢的黑暗中更难被察觉。";
    }

//    public RogueArmor() {
//        super( 1 );
//    }

//	@Override
//    public String special() {
//        return AC_SPECIAL;
//    }
//
//    @Override
//    public void doSpecial() {
//        GameScene.selectCell( teleporter );
//    }
//
//    @Override
//    public boolean doEquip( Hero hero ) {
//        if (hero.heroClass == HeroClass.BRIGAND) {
//            return super.doEquip( hero );
//        } else {
//            GLog.w( TXT_NOT_ROGUE );
//            return false;
//        }
//    }
	
//	@Override
//	public String desc() {
//		return
//			"Wearing this dark garb, a rogue can perform a trick, that is called \"smoke bomb\" " +
//			"(though no real explosives are used): he blinds enemies who could see him and jumps aside.";
//	}
	
//	protected static CellSelector.Listener teleporter = new  CellSelector.Listener() {
//
//		@Override
//		public void onSelect( Integer target ) {
//			if (target != null) {
//
//				if (!Level.fieldOfView[target] ||
//					!(Level.passable[target] || Level.avoid[target]) ||
//					Actor.findChar( target ) != null) {
//
//					GLog.w( TXT_FOV );
//					return;
//				}
//
//				curUser.HP -= (curUser.HP / 3);
//
//				for (Mob mob : Dungeon.bonus.mobs) {
//					if (Level.fieldOfView[mob.pos]) {
//						Buff.prolong( mob, Blindness.class, 2 );
//						mob.state = mob.WANDERING;
//						mob.sprite.emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
//					}
//				}
//
//				WandOfBlink.appear( curUser, target );
//				CellEmitter.get( target ).burst( Speck.factory( Speck.WOOL ), 10 );
//				Sample.INSTANCE.play( Assets.SND_PUFF );
//				Dungeon.bonus.press( target, curUser );
//				Dungeon.observe();
//
//				curUser.spendAndNext( Actor.TICK );
//			}
//		}
//
//		@Override
//		public String prompt() {
//			return "Choose a location to jump to";
//		}
//	};
}