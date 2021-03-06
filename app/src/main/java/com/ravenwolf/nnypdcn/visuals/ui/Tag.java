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
package com.ravenwolf.nnypdcn.visuals.ui;

import com.ravenwolf.nnypdcn.Chrome;
import com.watabou.noosa.Game;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.ui.Button;

public class Tag extends Button {

	public static final float WIDTH	= 22f;
	public static final float HEIGHT	= 22f;

	private float r;
	private float g;
	private float b;
	protected NinePatch bg;
	
	protected float lightness = 0;

    public Tag( int color ) {

        super();

        this.r = (color >> 16) / 255f;
        this.g = ((color >> 8) & 0xFF) / 255f;
        this.b = (color & 0xFF) / 255f;



        bg.ra = bg.ga = bg.ba = 0;
        bg.rm = this.r;
        bg.gm = this.g;
        bg.bm = this.b;
    }
	
	@Override
	protected void createChildren() {
		
		super.createChildren();
		
		bg = Chrome.get( Chrome.Type.TAG_RIGHT );
		add( bg );
	}
	
	@Override
	protected void layout() {
		
		super.layout();
		
		bg.x = x;
		bg.y = y;
		bg.size( width, height );
	}
	
	public void flash() {
		lightness = 1f;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (visible && lightness > 0.5) {
			if ((lightness -= Game.elapsed) > 0.5) {
				bg.ra = bg.ga = bg.ba = 2 * lightness - 1;
				bg.rm = 2 * r * (1 - lightness);
				bg.gm = 2 * g * (1 - lightness);
				bg.bm = 2 * b * (1 - lightness);
			} else {
				bg.hardlight( r, g, b );
			}
		}
	}
}
