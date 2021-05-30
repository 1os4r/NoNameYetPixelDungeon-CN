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
package com.ravenwolf.nnypdcn.visuals.windows;

import com.ravenwolf.nnypdcn.Badges;
import com.ravenwolf.nnypdcn.Difficulties;
import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.Rankings;
import com.ravenwolf.nnypdcn.Statistics;
import com.ravenwolf.nnypdcn.actors.hero.Belongings;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.misc.utils.Utils;
import com.ravenwolf.nnypdcn.scenes.PixelScene;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSpriteSheet;
import com.ravenwolf.nnypdcn.visuals.ui.BadgesList;
import com.ravenwolf.nnypdcn.visuals.ui.Icons;
import com.ravenwolf.nnypdcn.visuals.ui.ItemSlot;
import com.ravenwolf.nnypdcn.visuals.ui.RedButton;
import com.ravenwolf.nnypdcn.visuals.ui.ScrollPane;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

import java.util.Locale;

public class WndRanking extends WndTabbed {
	
	private static final String TXT_ERROR		= "信息加载失败";
	
	private static final String TXT_STATS	= "数据";
	private static final String TXT_ITEMS	= "物品";
	private static final String TXT_BADGES	= "徽章";
	
	private static final int WIDTH			= 112;
	private static final int HEIGHT			= 134;
	
	private static final int TAB_WIDTH	= 40;
	
	private Thread thread;
	private String error = null;
	
	private Image busy;
	private Rankings.Record rec;

	public WndRanking( Rankings.Record rec, final String gameFile ) {
		
		super();
		resize(WIDTH, HEIGHT);

        this.rec = rec;
		
		thread = new Thread() {
			@Override
			public void run() {
				try {
					Badges.loadGlobal();
					Dungeon.loadGame( gameFile );

                    if( Dungeon.hero == null ) {
                        throw new Exception();
                    }

				} catch (Exception e ) {
					error = TXT_ERROR;
				}
			}
		};
		thread.start();
		
		busy = Icons.BUSY.get();	
		busy.origin.set( busy.width / 2, busy.height / 2 );
		busy.angularSpeed = 720;
		busy.x = (WIDTH - busy.width) / 2;
		busy.y = (HEIGHT - busy.height) / 2;
		add( busy );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (thread != null && !thread.isAlive()) {
			thread = null;
			if (error == null) {
				remove( busy );
				createControls();
			} else {
				hide();
				Game.scene().add( new WndError( TXT_ERROR ) );
			}
		}
	}
	
	private void createControls() {
		
		String[] labels =
			{
                    TXT_STATS,
                    TXT_ITEMS,
                    TXT_BADGES,
            };
		Group[] pages =
			{
                    new StatsTab(),
                    new ItemsTab(),
                    new BadgesTab(),
            };

		for (int i=0; i < pages.length; i++) {

			add( pages[i] );

			Tab tab = new RankingTab( labels[i], pages[i] );
			tab.setSize( TAB_WIDTH, tabHeight() );
			add( tab );
		}

		select( 0 );
	}

	private class RankingTab extends LabeledTab {
		
		private Group page;
		
		public RankingTab( String label, Group page ) {
			super( label );
			this.page = page;
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}
	}
	
	private class StatsTab extends Group {
		
		private static final int GAP	= 4;
		
		private static final String TXT_TITLE	= "%d级%s";
		
		private static final String TXT_CHALLENGES	= "Challenges";
		
		private static final String TXT_HEALTH	= "生命";
		private static final String TXT_STR		= "力量";

		private static final String TXT_SCORE	= "分数";
		private static final String TXT_DURATION= "游戏时长";
		private static final String TXT_DIFF	= "难度";

		private static final String TXT_VERSION 	= "游戏版本";

		private static final String TXT_DEPTH	= "最高层数";
		private static final String TXT_ENEMIES	= "怪物击杀数";
		private static final String TXT_GOLD	= "金币收集数";
		
		private static final String TXT_FOOD	= "食物消耗量";
		private static final String TXT_ALCHEMY	= "药水酿造数";
		private static final String TXT_ANKHS	= "重生十字架使用量";
		
		public StatsTab() {
			super();
			
			String heroClass = Dungeon.hero.heroClass.cname();
			
			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar( Dungeon.hero.heroClass, Dungeon.hero.appearance() ) );
			title.label( Utils.format( TXT_TITLE, Dungeon.hero.lvl, heroClass ).toUpperCase( Locale.ENGLISH ) );
			title.setRect( 0, 0, WIDTH, 0 );
			add( title );
			
			float pos = title.bottom();
			
			if (Dungeon.challenges > 0) {
				RedButton btnCatalogus = new RedButton( TXT_CHALLENGES ) {
					@Override
					protected void onClick() {
						Game.scene().add( new WndChallenges( Dungeon.challenges, false ) );
					}
				};
				btnCatalogus.setRect( 0, pos + GAP, btnCatalogus.reqWidth() + 2, btnCatalogus.reqHeight() + 2 );
				add( btnCatalogus );
				
				pos = btnCatalogus.bottom();
			}
			
			pos += GAP + GAP;

            pos = statSlot( this, TXT_HEALTH, Integer.toString( Dungeon.hero.HT ), pos );
            pos = statSlot( this, TXT_STR, Integer.toString( Dungeon.hero.STR ), pos );

            pos += GAP;

            pos = statSlot( this, TXT_DEPTH, Integer.toString( Statistics.deepestFloor ), pos );
            pos = statSlot( this, TXT_ENEMIES, Integer.toString( Statistics.enemiesSlain ), pos );
            pos = statSlot( this, TXT_GOLD, Integer.toString( Statistics.goldCollected ), pos );

            pos += GAP;

            pos = statSlot( this, TXT_FOOD, Integer.toString( Statistics.foodEaten ), pos );
            pos = statSlot( this, TXT_ALCHEMY, Integer.toString( Statistics.potionsCooked ), pos );
            pos = statSlot( this, TXT_ANKHS, Integer.toString( Statistics.ankhsUsed ), pos );

            pos += GAP;

            pos = statSlot( this, TXT_DURATION, Integer.toString( (int)Statistics.duration ), pos );
            pos = statSlot( this, TXT_SCORE, Integer.toString( rec.score ), pos );
            pos = statSlot( this, TXT_DIFF, Difficulties.NAMES[ rec.diff ], pos );

            pos += GAP;

            pos = statSlot( this, TXT_VERSION, !rec.version.isEmpty() ? rec.version : "版本过低", pos );

            pos += GAP;

            resize(WIDTH, (int)pos);

        }
		
		private float statSlot( Group parent, String label, String value, float pos ) {
			
			RenderedText txt = PixelScene.renderText( label, 6 );
			txt.y = pos;
			parent.add( txt );
			
			txt = PixelScene.renderText( value, 5 );
			txt.x = PixelScene.align( WIDTH * 0.65f );
			txt.y = pos;
			parent.add( txt );
			
			return pos + GAP + txt.baseLine();
		}
	}
	
	private class ItemsTab extends Group {
		
		private int count;
		private float pos;

        private static final int GAP = 1;
		
		public ItemsTab() {
			super();
			
			Belongings stuff = Dungeon.hero.belongings;
			if (stuff.weap1 != null) {
				addItem( stuff.weap1);
			} else {
                addItem( new Placeholder(ItemSpriteSheet.WEAP1) );
            }

            if (stuff.weap2 != null) {
                addItem( stuff.weap2);
            } else {
                addItem( new Placeholder(ItemSpriteSheet.WEAP2) );
            }

			if (stuff.armor != null) {
				addItem( stuff.armor );
			} else {
                addItem( new Placeholder(ItemSpriteSheet.ARMOR) );
            }

			if (stuff.ring1 != null) {
				addItem( stuff.ring1 );
			} else {
                addItem( new Placeholder(ItemSpriteSheet.RING) );
            }

			if (stuff.ring2 != null) {
				addItem( stuff.ring2 );
			} else {
                addItem( new Placeholder(ItemSpriteSheet.RING) );
            }

			if (stuff.ranged != null) {
				addItem( stuff.ranged );
			} else {
				addItem( new Placeholder(ItemSpriteSheet.RANGED) );
			}

			resize(WIDTH, (int)pos);

//			Item setAsQuickSlot1 = getQuickslot( QuickSlot.quickslotValue_1 );
//			Item quickslot2 = getQuickslot( QuickSlot.quickslotValue_2 );
//
//			if (count >= 4 && setAsQuickSlot1 != null && quickslot2 != null) {
//
//				float size = ItemButton.SIZE;
//
//				ItemButton slot = new ItemButton( setAsQuickSlot1 );
//				slot.setRect( 0, pos, size, size );
//				add( slot );
//
//				slot = new ItemButton( quickslot2 );
//				slot.setRect( size + 1, pos, size, size );
//				add( slot );
//			} else {
//				if (setAsQuickSlot1 != null) {
//					addItem( setAsQuickSlot1 );
//				}
//				if (quickslot2 != null) {
//					addItem( quickslot2 );
//				}
//			}
		}
		
		private void addItem( Item item ) {
			LabelledItemButton slot = new LabelledItemButton( item );
			slot.setRect( 0, pos, width, LabelledItemButton.SIZE );
			add( slot );
			
			pos += slot.height() + GAP;
			count++;
		}
		
		private Item getQuickslot( Object value ) {
			if (value instanceof Item && Dungeon.hero.belongings.backpack.contains( (Item)value )) {
					
					return (Item)value;
					
			} else if (value instanceof Class){
				
				@SuppressWarnings("unchecked")
				Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)value );
				if (item != null) {
					return item;
				}
			}
			
			return null;
		}
	}
	
	private class BadgesTab extends Group {
		
		public BadgesTab() {
			super();
			
			camera = WndRanking.this.camera;
			
			ScrollPane list = new BadgesList( false );
			add( list );
			
			list.setSize( WIDTH, WndRanking.this.height );
		}
	}
	
	private class ItemButton extends Button {
		
		public static final int SIZE	= 26;
		
		protected Item item;
		
		protected ItemSlot slot;
		private ColorBlock bg;
		
		public ItemButton( Item item ) {
			
			super();

			this.item = item;
			
			slot.item( item );
			if (item.isCursed() && item.isCursedKnown()) {
				bg.ra = +0.2f;
				bg.ga = -0.1f;
			} else if (!item.isIdentified()) {
				bg.ra = 0.1f;
				bg.ba = 0.1f;
			}
		}
		
		@Override
		protected void createChildren() {	
			
			bg = new ColorBlock( SIZE, SIZE, 0xFF4A4D44 );
			add( bg );
			
			slot = new ItemSlot();
			add( slot );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			slot.setRect( x, y, SIZE, SIZE );
			
			super.layout();
		}
		
		@Override
		protected void onTouchDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
		};
		
		protected void onTouchUp() {
			bg.brightness( 1.0f );
		};
		
		@Override
		protected void onClick() {
			if( !( item instanceof Placeholder )) {
                Game.scene().add(new WndItem(null, item));
            }
		}
	}

	private class LabelledItemButton extends ItemButton {
		private RenderedText name;
		
		public LabelledItemButton( Item item ) {
			super(item);
		}
		
		@Override
		protected void createChildren() {	
			super.createChildren();
			
			name = PixelScene.renderText( "?", 7 );
			add(name);
		}
		
		@Override
		protected void layout() {
			
			super.layout();
			
			name.x = slot.right() + 2;
			name.y = y + (height - name.baseLine()) / 2;
			
			String str = Utils.capitalize( item.name() );
			name.text( str );
			PixelScene.align(name);
			if (name.width() > width - name.x) {
				do {
					str = str.substring( 0, str.length() - 1 );
					name.text( str + "..." );
					PixelScene.align(name);
				} while (name.width() > width - name.x);
			}
		}
	}

    private static class Placeholder extends Item {
        {
            name = " ";
        }

        public Placeholder( int image ) {
            this.image = image;
        }

        @Override
        public boolean isEquipped( Hero hero ) {
            return true;
        }
    }
}
