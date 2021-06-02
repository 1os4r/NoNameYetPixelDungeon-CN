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

import com.ravenwolf.nnypdcn.NoNameYetPixelDungeon;
import com.ravenwolf.nnypdcn.scenes.PixelScene;
import com.ravenwolf.nnypdcn.visuals.ui.RenderedTextMultiline;
import com.ravenwolf.nnypdcn.visuals.ui.ScrollPane;
import com.ravenwolf.nnypdcn.visuals.ui.Window;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

public class WndChangelog extends Window {

	private static final int WIDTH_P	= 128;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 160;
	private static final int HEIGHT_L	= 128;

	private static final String TXT_TITLE	= "无名的像素地牢 中文版";

    private static final String TXT_DESCR =


        "初步汉化的无名地牢，_基于0.3.1版本_，作者RavenWolf已授权。\n" + 
		"目前大部分内容都已汉化，由于时间较紧，可能会存在诸多问题。" + 
		"如有任何意见和建议，_欢迎前来指正_！\n\n请将主要问题描述和截图发送至邮箱：\n_1os4r21@gmail.com_\n" +
        "或者QQ:_1643444316_\n" +
        "Github:\n_https://github.com/1os4r/NoNameYetPixelDungeon-CN_\n\n" +
		"那么，祝你玩得开心！\n初始解锁了所有模式（顺带一提简单模式是真的简单！）\n" +
		"\nv0.3 修正了部分描述，修复了某些情况下不记录回合的bug" +
        "\nv0.2 修正了部分描述错误及缺失问题，稍微优化了卡顿问题" +
        "\nv0.1 无名地牢的初步汉化版本，解锁了全部难度模式，制造了N多bug";


	private RenderedText txtTitle;
	private ScrollPane list;

	public WndChangelog() {
		
		super();
		
		if (NoNameYetPixelDungeon.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
            resize( WIDTH_P, HEIGHT_P );
		}
		
		txtTitle = PixelScene.renderText( TXT_TITLE, 8 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		PixelScene.align(txtTitle);
        txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width() ) / 2 );
        add( txtTitle );

        list = new ScrollPane( new ChangelogItem( TXT_DESCR, width, txtTitle.height() ) );
        add( list );

        list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );
        list.scrollTo( 0, 0 );

	}

    private static class ChangelogItem extends Component {

        private final int GAP = 4;

        private RenderedTextMultiline normal;
//        private BitmapTextMultiline highlighted;

        public ChangelogItem( String text, int width, float offset ) {
            super();

//            label.text( text );
//            label.maxWidth = width;
//            label.measure();

//            Highlighter hl = new Highlighter( text );

//            normal = PixelScene.createMultiline( hl.text, 6 );
            normal.text(text);
            normal.maxWidth(width);
            PixelScene.align(normal);
//            normal.x = 0;
//            normal.y = offset;
//            add( normal );

//            if (hl.isHighlighted()) {
//                normal.mask = hl.inverted();

//                highlighted = PixelScene.createMultiline( hl.text, 6 );
//                highlighted.text( hl.text );
//                highlighted.maxWidth = normal.maxWidth;
//                highlighted.measure();
//                highlighted.x = normal.x;
//                highlighted.y = normal.y;
//                add( highlighted );

//                highlighted.mask = hl.mask;
//                highlighted.hardlight( TITLE_COLOR );
//            }

//            height = normal.height() + GAP;
        }

        @Override
        protected void createChildren() {
            normal = PixelScene.renderMultiline( 5 );
            add( normal );
//            highlighted = PixelScene.createMultiline( 6 );
//            add( highlighted );
        }

        @Override
        protected void layout() {
            y = PixelScene.align( y + GAP );
//            highlighted.y = PixelScene.align( y + GAP );
        }
    }
}
