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

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.hero.Hero;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.Shopkeeper;
import com.ravenwolf.nnypdcn.items.EquipableItem;
import com.ravenwolf.nnypdcn.items.Heap;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.misc.Gold;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.misc.utils.Utils;
import com.ravenwolf.nnypdcn.scenes.PixelScene;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypdcn.visuals.ui.ItemSlot;
import com.ravenwolf.nnypdcn.visuals.ui.RedButton;
import com.ravenwolf.nnypdcn.visuals.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.ravenwolf.nnypdcn.visuals.ui.RenderedTextMultiline;

public class WndTradeItem extends Window {
	
	private static final float GAP		= 2;
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 16;
	
	private static final String TXT_SALE		= "待售: %s - %dg";
	private static final String TXT_BUY			= "花%dg金币购买";
	private static final String TXT_SELL		= "以%dg金币卖出";
	private static final String TXT_SELL_1		= "以%dg金币卖出1个";
	private static final String TXT_SELL_ALL	= "以%dg金币卖出全部";
	private static final String TXT_CANCEL		= "算了";
	
	private static final String TXT_SOLD	= "你卖出了%s获得%dg金币";
	private static final String TXT_BOUGHT	= "你花%s金币购买了%dg";
	
	private WndBag owner;

    private RenderedTextMultiline normal;
    private BitmapTextMultiline highlighted;
	
	public WndTradeItem( final Item item, WndBag owner ) {
		
		super();
		
		this.owner = owner; 
		
		float pos = createDescription( item, false );
		
		if (item.quantity() == 1) {
			
			RedButton btnSell = new RedButton( Utils.format( TXT_SELL, item.price() ) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSell.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			add( btnSell );
			
			pos = btnSell.bottom();
			
		} else {
			
			int priceAll= item.price();
			RedButton btnSell1 = new RedButton( Utils.format( TXT_SELL_1, priceAll / item.quantity() ) ) {
				@Override
				protected void onClick() {
					sellOne( item );
					hide();
				}
			};
			btnSell1.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			add( btnSell1 );
			RedButton btnSellAll = new RedButton( Utils.format( TXT_SELL_ALL, priceAll ) ) {
				@Override
				protected void onClick() {
					sell( item );
					hide();
				}
			};
			btnSellAll.setRect( 0, btnSell1.bottom() + GAP, WIDTH, BTN_HEIGHT );
			add( btnSellAll );
			
			pos = btnSellAll.bottom();
			
		}
		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
	
	public WndTradeItem( final Heap heap, boolean canBuy ) {
		
		super();
		
		Item item = heap.peek();

        if( !item.isIdentified() )
            item.identify();
		
		float pos = createDescription( item, true );
		
		int price = price( item );
		
		if (canBuy) {
			
			RedButton btnBuy = new RedButton( Utils.format( TXT_BUY, price ) ) {
				@Override
				protected void onClick() {
					hide();
					buy( heap );
				}
			};
			btnBuy.setRect( 0, pos + GAP, WIDTH, BTN_HEIGHT );
			btnBuy.enable( price <= Dungeon.gold );
			add( btnBuy );
			
			RedButton btnCancel = new RedButton( TXT_CANCEL ) {
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel.setRect( 0, btnBuy.bottom() + GAP, WIDTH, BTN_HEIGHT );
			add( btnCancel );
			
			resize( WIDTH, (int)btnCancel.bottom() );
			
		} else {
			
			resize( WIDTH, (int)pos );
			
		}
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		if (owner != null) {
			owner.hide();
			Shopkeeper.sell();
		}
	}
	
	private float createDescription( Item item, boolean forSale ) {
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item.image(), item.glowing() ) );
		titlebar.label( forSale ? 
			Utils.format( TXT_SALE, item.toString(), price( item ) ) : 
			Utils.capitalize( item.toString() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		if (item.isIdentified() && !item.isCursed()) {
			titlebar.color( ItemSlot.UPGRADED );	
		} else if (item.isIdentified() && item.isCursed()) {
			titlebar.color( ItemSlot.DEGRADED );	
		}

        Highlighter hl = new Highlighter( item.info() );

        normal = PixelScene.renderMultiline( item.info(), 6 );
        normal.maxWidth(WIDTH);
        PixelScene.align(normal);
        float x = titlebar.left();
        float y = titlebar.bottom() + GAP;
        normal.setPos(x,y);
        add( normal );

//        if (hl.isHighlighted()) {
//            normal.mask = hl.inverted();
//
//            highlighted = PixelScene.createMultiline( hl.text, 6 );
//            highlighted.maxWidth = normal.maxWidth;
//            highlighted.measure();
//            highlighted.x = normal.x;
//            highlighted.y = normal.y;
//            add( highlighted );
//
//            highlighted.mask = hl.mask;
//            highlighted.hardlight( TITLE_COLOR );
//        }
		
//		BitmapTextMultiline info = PixelScene.createMultiline( item.info(), 6 );
//		info.maxWidth = WIDTH;
//		info.measure();
//		info.x = titlebar.left();
//		info.y = titlebar.bottom() + GAP;
//		add( info );
//
//		return info.y + info.height();
		return y + normal.height();
	}
	
	private void sell( Item item ) {
		
		Hero hero = Dungeon.hero;
		
		if (item.isEquipped( hero ) && !((EquipableItem)item).doUnequip( hero, false, false )) {
			return;
		}

		item.detachAll( hero.belongings.backpack );
		
		int price = item.price();
		
		new Gold( price ).doPickUp( hero );
		GLog.i( TXT_SOLD, item.name(), price );
	}
	
	private void sellOne( Item item ) {
		
		if (item.quantity() <= 1) {
			sell( item );
		} else {
			
			Hero hero = Dungeon.hero;
			
			item = item.detach( hero.belongings.backpack );
			int price = item.price();
			
			new Gold( price ).doPickUp( hero );
			GLog.i( TXT_SOLD, item.name(), price );
		}
	}
	
	private int price( Item item ) {

        int price = item.price() * item.priceModifier() * Dungeon.chapter();
//		if (Dungeon.hero.buff( RingOfFortune.Fortune.class ) != null && price >= 2) {
//			price /= 2;
//		}
		return price;
	}
	
	private void buy( Heap heap ) {
		
		Hero hero = Dungeon.hero;
		Item item = heap.pickUp();
		
		int price = price( item );
		Dungeon.gold -= price;
		
		GLog.i( TXT_BOUGHT, item.name(), price );
		
		if (!item.doPickUp( hero )) {
			Dungeon.level.drop( item, heap.pos ).sprite.drop();
		}
	}
}
