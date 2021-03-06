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

import com.ravenwolf.nnypdcn.scenes.PixelScene;
import com.ravenwolf.nnypdcn.visuals.ui.RedButton;
import com.ravenwolf.nnypdcn.visuals.ui.Window;
import com.watabou.noosa.BitmapTextMultiline;
import com.ravenwolf.nnypdcn.visuals.ui.RenderedTextMultiline;

import java.util.ArrayList;

public class WndOptions extends Window {

    private static final int DISABLED_COLOR	= 0xCACFC2;

	private static final int WIDTH			= 120;
	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 20;

    public ArrayList<Integer> disabled = new ArrayList<>();

	public WndOptions( String title, String message, String... options ) {
		super();

        this.disabled = disabled();

		RenderedTextMultiline tfTitle = PixelScene.renderMultiline( title, 9 );
		tfTitle.hardlight( TITLE_COLOR );
		tfTitle.setPos(MARGIN,MARGIN);
		PixelScene.align(tfTitle);
		add( tfTitle );
		
		RenderedTextMultiline tfMessage = PixelScene.renderMultiline( message, 7 );
		tfMessage.maxWidth(WIDTH - MARGIN * 2);
		PixelScene.align(tfMessage);
		tfMessage.setPos(MARGIN, MARGIN + tfTitle.height() + MARGIN);
		add( tfMessage );
		
		float pos = MARGIN + tfTitle.height() + MARGIN + tfMessage.height() + MARGIN;
		
		for (int i=0; i < options.length; i++) {
			final int index = i;
			RedButton btn = new RedButton( options[i] ) {
				@Override
				protected void onClick() {
					hide();
					onSelect( index );
				}
			};

            if( disabled != null && disabled.contains( index ) ) {
                btn.textColor( DISABLED_COLOR );
            }

			btn.setRect( MARGIN, pos, WIDTH - MARGIN * 2, BUTTON_HEIGHT );

			add( btn );
			
			pos += BUTTON_HEIGHT + MARGIN;
		}
		
		resize( WIDTH, (int)pos );
	}
	
	protected void onSelect( int index ) {};
	protected ArrayList<Integer> disabled() {
        return null;
    };
}
