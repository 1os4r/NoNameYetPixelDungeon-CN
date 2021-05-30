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
package com.ravenwolf.nnypdcn.scenes;

import com.ravenwolf.nnypdcn.Badges;
import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.DungeonTilemap;
import com.ravenwolf.nnypdcn.FogOfWar;
import com.ravenwolf.nnypdcn.Statistics;
import com.ravenwolf.nnypdcn.NoNameYetPixelDungeon;
import com.ravenwolf.nnypdcn.actors.Actor;
import com.ravenwolf.nnypdcn.actors.blobs.Blob;
import com.ravenwolf.nnypdcn.actors.hazards.Hazard;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.items.Heap;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.potions.Potion;
import com.ravenwolf.nnypdcn.items.wands.CharmOfBlink;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.ravenwolf.nnypdcn.levels.features.Chasm;
import com.ravenwolf.nnypdcn.levels.features.Door;
import com.ravenwolf.nnypdcn.misc.utils.GLog;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.ravenwolf.nnypdcn.visuals.effects.BannerSprites;
import com.ravenwolf.nnypdcn.visuals.effects.BlobEmitter;
import com.ravenwolf.nnypdcn.visuals.effects.EmoIcon;
import com.ravenwolf.nnypdcn.visuals.effects.Flare;
import com.ravenwolf.nnypdcn.visuals.effects.FloatingText;
import com.ravenwolf.nnypdcn.visuals.effects.Ripple;
import com.ravenwolf.nnypdcn.visuals.effects.SpellSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.CharSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.DiscardedItemSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.HazardSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.HeroSprite;
import com.ravenwolf.nnypdcn.visuals.sprites.ItemSprite;
import com.ravenwolf.nnypdcn.visuals.ui.Banner;
import com.ravenwolf.nnypdcn.visuals.ui.BuffIndicator;
import com.ravenwolf.nnypdcn.visuals.ui.BusyIndicator;
import com.ravenwolf.nnypdcn.visuals.ui.GameLog;
import com.ravenwolf.nnypdcn.visuals.ui.HealthIndicator;
import com.ravenwolf.nnypdcn.visuals.ui.QuickSlot;
import com.ravenwolf.nnypdcn.visuals.ui.StatusPane;
import com.ravenwolf.nnypdcn.visuals.ui.Toast;
import com.ravenwolf.nnypdcn.visuals.ui.Toolbar;
import com.ravenwolf.nnypdcn.visuals.ui.Window;
import com.ravenwolf.nnypdcn.visuals.windows.WndBag;
import com.ravenwolf.nnypdcn.visuals.windows.WndBag.Mode;
import com.ravenwolf.nnypdcn.visuals.windows.WndGame;
import com.ravenwolf.nnypdcn.visuals.windows.WndStory;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class GameScene extends PixelScene {
	
	private static final String TXT_WELCOME			= "\n欢迎来到像素地牢的第%d层！";
	private static final String TXT_WELCOME_BACK	= "\n欢迎回到像素地牢的第%d层！";
//	private static final String TXT_NIGHT_MODE		= "Be cautious, since the dungeon is even more dangerous at night!";
	
//	private static final String TXT_CHASM	= "Your steps echo across the dungeon.";
	private static final String TXT_WATER	= "你听到周围水花四溅的声音。";
	private static final String TXT_GRASS	= "空气中弥漫着浓郁的植物清香。";
	private static final String TXT_TRAPS	= "本层的气氛似乎暗示着其下隐藏的众多秘密。";
//	private static final String TXT_HAUNT	= "Eerie feeling sends shivers down your spine.";
	private static final String TXT_HAUNT	= "浓雾笼罩着这层楼。诡异的气息顺着脊梁传遍了你的全身。";

//	private static final String TXT_SECRETS	= "The atmosphere hints that this floor hides many secrets.";
	
	static GameScene scene;
	
	private SkinnedBlock water;
	private DungeonTilemap tiles;
	private FogOfWar fog;
	private HeroSprite hero;
	
	private GameLog log;
	
	private BusyIndicator busy;
	
	private static CellSelector cellSelector;
	
	private Group terrain;
	private Group ripples;
	private Group plants;
	private Group hazards;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;
	
	private Toolbar toolbar;
	private Toast prompt;
	
	@Override
	public void create() {

        Music.INSTANCE.play( Dungeon.level.currentTrack(), true );
		Music.INSTANCE.volume( 1f );
		
		NoNameYetPixelDungeon.lastClass(Dungeon.hero.heroClass.ordinal());
		
		super.create();
		Camera.main.zoom( defaultZoom + NoNameYetPixelDungeon.zoom() );
		
		scene = this;
		
		terrain = new Group();
		add( terrain );
		
		water = new SkinnedBlock( 
			Level.WIDTH * DungeonTilemap.SIZE, 
			Level.HEIGHT * DungeonTilemap.SIZE,
			Dungeon.level.waterTex() );
		terrain.add( water );
		
		ripples = new Group();
		terrain.add( ripples );
		
		tiles = new DungeonTilemap();
		terrain.add( tiles );
		
		Dungeon.level.addVisuals( this );
		/*
		plants = new Group();
		add( plants );
		
		int size = Dungeon.level.plants.size();
		for (int i=0; i < size; i++) {
			addPlantSprite(Dungeon.level.plants.valueAt(i));
		}
		*/
		heaps = new Group();
		add( heaps );
		
		int size = Dungeon.level.heaps.size();
		for (int i=0; i < size; i++) {
			addHeapSprite(Dungeon.level.heaps.valueAt(i));
		}


		emitters = new Group();
		effects = new Group();
		emoicons = new Group();

        hazards = new Group();
        add( hazards );

        for (Hazard hazard : Dungeon.level.hazards) {
            addHazardSprite( hazard );
        }

        sortHazards();

		mobs = new Group();
		add( mobs );
		
		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite(mob);
			if (Statistics.amuletObtained) {
				mob.beckon( Dungeon.hero.pos );
			}
		}
		

		gases = new Group();
		add( gases );
		
		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}
		
        add( emitters );
        add( effects );
        add( emoicons );
		fog = new FogOfWar( Level.WIDTH, Level.HEIGHT );
		fog.updateVisibility(Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped);
		add(fog);
		
		brightness( NoNameYetPixelDungeon.brightness() );
		
		spells = new Group();
		add( spells );

		statuses = new Group();
		add(statuses);
		

		
		hero = new HeroSprite();
		hero.place(Dungeon.hero.pos);
		hero.updateArmor();
        Dungeon.hero.updateSpriteState();
        mobs.add(hero);

		add(new HealthIndicator());
		
		add(cellSelector = new CellSelector(tiles));
		
		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect(0, uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height());
		add( toolbar );

        StatusPane sb = new StatusPane( (int)( toolbar.top() /*- toolbar.btnQuick0.height()*/ ) );
        sb.camera = uiCamera;
        sb.setSize(uiCamera.width, 0);
        add(sb);

		log = new GameLog();
		log.camera = uiCamera;
		log.setRect( 0, toolbar.top(), uiCamera.width - toolbar.btnQuick0.width(),  0 );
		add( log );
		
		if (Dungeon.depth < Statistics.deepestFloor) {
			GLog.i( TXT_WELCOME_BACK, Dungeon.depth );
		} else {
			GLog.i( TXT_WELCOME, Dungeon.depth );
			Sample.INSTANCE.play( Assets.SND_DESCEND );
		}
		switch (Dungeon.level.feeling) {
//		case CHASM:
//			GLog.w( TXT_CHASM );
//			break;
		case WATER:
			GLog.w( TXT_WATER );
			break;
		case GRASS:
			GLog.w( TXT_GRASS );
			break;
        case TRAPS:
            GLog.w( TXT_TRAPS );
            break;
        case HAUNT:
            GLog.w( TXT_HAUNT );
            break;
		default:
		}

//		if (Dungeon.bonus instanceof RegularLevel &&
//			((RegularLevel)Dungeon.bonus).secretDoors > Random.IntRange( 3, 4 )) {
//			GLog.w( TXT_SECRETS );
//		}

//		if (Dungeon.nightMode && !Dungeon.bossLevel()) {
//			GLog.w( TXT_NIGHT_MODE );
//		}
		
		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = sb.bottom() + 1;
		add( busy );
		
		switch (InterlevelScene.mode) {
		case RESURRECT:
			CharmOfBlink.appear( Dungeon.hero, Dungeon.hero.pos );
			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f ) ;
            Sample.INSTANCE.play( Assets.SND_TELEPORT );
			break;
		case RETURN:
			CharmOfBlink.appear(  Dungeon.hero, Dungeon.hero.pos );
			break;
		case FALL:
			Chasm.heroLand();
			break;
		case DESCEND:
			switch (Dungeon.depth) {
			case 1:
				WndStory.showChapter( WndStory.ID_SEWERS );
				break;
			case 7:
				WndStory.showChapter( WndStory.ID_PRISON);
				break;
			case 13:
				WndStory.showChapter( WndStory.ID_CAVES );
				break;
			case 19:
				WndStory.showChapter( WndStory.ID_METROPOLIS );
				break;
			case 26:
				WndStory.showChapter( WndStory.ID_HALLS );
				break;
			}
//			if (Dungeon.hero.isAlive() && Dungeon.depth != 25) {
//				Badges.validateNoKilling();
//			}
			break;
		default:
		}
		
		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell( false, true );
				if (item instanceof Potion) {
					((Potion) item).shatter(pos);
				/*} else if (item instanceof Plant.Seed) {
					Dungeon.level.plant( (Plant.Seed)item, pos );*/
				}else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}
		
		Camera.main.target = hero;
		Dungeon.hero.updateSpriteState();
		Camera.main.target = hero;
		fadeIn();
	}
	
	public void destroy() {
		
		scene = null;
		Badges.saveGlobal();
		
		super.destroy();
	}
	
	@Override
	public synchronized void pause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
		} catch (IOException e) {
			//
		}
	}
	
	@Override
	public synchronized void update() {
		if (Dungeon.hero == null) {
			return;
		}
			
		super.update();

        if( water != null ) {
            water.offset(0, -5 * Game.elapsed);
        }
		
		Actor.process();
		
		if (Dungeon.hero.ready && !Dungeon.hero.stunned) {
			log.newLine();
            BuffIndicator.refreshHero();
		}

        if( cellSelector != null ) {
            cellSelector.enabled = Dungeon.hero.ready;
        }
	}
	
	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add(new WndGame());
		}
	}
	
	@Override
	protected void onMenuPressed() {
		if (Dungeon.hero.ready) {
			selectItem( null, WndBag.Mode.ALL, null );
		}
	}
	
	public void brightness( boolean value ) {
		water.rm = water.gm = water.bm = 
		tiles.rm = tiles.gm = tiles.bm = 
			value ? 1.5f : 1.0f;
		if (value) {
			fog.am = +2f;
			fog.aa = -1f;
		} else {
			fog.am = +1f;
			fog.aa =  0f;
		}
	}
	
	private void addHeapSprite( Heap heap ) {
		ItemSprite sprite = heap.sprite = (ItemSprite)heaps.recycle( ItemSprite.class );
		sprite.revive();
		sprite.link( heap );
		heaps.add( sprite );
	}
	
	private void addDiscardedSprite( Heap heap ) {
		heap.sprite = (DiscardedItemSprite)heaps.recycle( DiscardedItemSprite.class );
		heap.sprite.revive();
		heap.sprite.link( heap );
		heaps.add(heap.sprite);
	}
	/*
	private void addPlantSprite( Plant plant ) {
		(plant.sprite = (PlantSprite)plants.recycle( PlantSprite.class )).reset( plant );
	}
	*/
private void addHazardSprite( Hazard hazard ) {
        HazardSprite sprite = hazard.sprite();
        sprite.visible = Dungeon.visible[hazard.pos];
        hazards.add( sprite );
        sprite.link( hazard );
	}
	private void addBlobSprite( final Blob gas ) {
		if (gas.emitter == null) {
			gases.add( new BlobEmitter( gas ) );
		}
	}

    public void addMobSprite( Mob mob ) {
        CharSprite sprite = mob.sprite();
        sprite.visible = Dungeon.visible[mob.pos];
        mobs.add( sprite );
        sprite.link( mob );
        mob.updateSpriteState();
	}
	
	private void prompt( String text ) {
		
		if (prompt != null) {
			prompt.killAndErase();
			prompt = null;
		}
		
		if (text != null) {
			prompt = new Toast( text ) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos( (uiCamera.width - prompt.width()) / 2, uiCamera.height - 60 );
			add( prompt );
		}
	}
	
	private void showBanner( Banner banner ) {
		banner.camera = uiCamera;
		banner.x = align( uiCamera, (uiCamera.width - banner.width) / 2 );
		banner.y = align( uiCamera, (uiCamera.height - banner.height) / 3 );
		add( banner );
	}
	
	// -------------------------------------------------------
	/*
	public static void add( Plant plant ) {
		if (scene != null) {
			scene.addPlantSprite(plant);
		}
	}*/
public static void add( Hazard hazard ) {
        Dungeon.level.hazards.add( hazard );
        Actor.add( hazard );
        scene.addHazardSprite( hazard );
        sortHazards();
	}

	public static void sortHazards() {
        // let's sort hazard sprites according to their priority
        // it could've been done better, but i'd rather not mess with watabou's libraries yet

        HashSet<Hazard> hazards = (HashSet<Hazard>)Dungeon.level.hazards.clone();

        for( int i = 0 ; i < Dungeon.level.hazards.size() ; i++ ){

            Hazard selected = null;

            for( Hazard current : hazards ){
                if( selected == null || selected.sprite.spritePriority() < current.sprite.spritePriority() ) {
                    selected = current;
                }
            }

            scene.hazards.sendToBack( selected.sprite );
            hazards.remove( selected );
        }
    }
	
	public static void add( Blob gas ) {
		Actor.add( gas );
		if (scene != null) {
			scene.addBlobSprite(gas);
		}
	}
	public static void add( Blob gas, float delay ) {
		Actor.addDelayed(gas, delay);
		if (scene != null) {
			scene.addBlobSprite(gas);
		}
	}
	
	public static void add( Heap heap ) {
		if (scene != null) {
			scene.addHeapSprite(heap);
		}
	}
	
	public static void discard( Heap heap ) {
		if (scene != null) {
			scene.addDiscardedSprite(heap);
		}
	}
	
	public static void add( Mob mob ) {
		Dungeon.level.mobs.add( mob );
		Actor.add(mob);
		Actor.occupyCell( mob );
		scene.addMobSprite( mob );
	}
	
	public static void add( Mob mob, float delay ) {
		Dungeon.level.mobs.add( mob );
		Actor.addDelayed(mob, delay);
		Actor.occupyCell( mob );
		scene.addMobSprite(mob);
	}
	
	public static void add( EmoIcon icon ) {
		scene.emoicons.add(icon);
	}
	
	public static void effect( Visual effect ) {
		scene.effects.add(effect);
	}
	
	public static Ripple ripple( int pos ) {
		Ripple ripple = (Ripple)scene.ripples.recycle( Ripple.class );
		ripple.reset(pos);
		return ripple;
	}
	
	public static SpellSprite spellSprite() {
		return (SpellSprite)scene.spells.recycle( SpellSprite.class );
	}
	
	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.emitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}
	
	public static FloatingText status() {
		return scene != null ? (FloatingText)scene.statuses.recycle( FloatingText.class ) : null;
	}
	
	public static void pickUp( Item item ) {
		scene.toolbar.pickup( item );
	}
	
	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updated.set(0, 0, Level.WIDTH, Level.HEIGHT);
		}
	}
	
	public static void updateMap( int cell ) {
		if (scene != null) {
			scene.tiles.updated.union(cell % Level.WIDTH, cell / Level.WIDTH);
		}
	}

    public static void updateMap( boolean[] visible ) {
        if (scene != null) {
            for( int i = 0 ; i < visible.length ; i++ ) {
                if( visible[i] ) {
                    scene.tiles.updated.union(i % Level.WIDTH, i / Level.WIDTH);
                }
            }
        }
    }
	
	public static void discoverTile( int pos, int oldValue ) {
		if (scene != null) {
			scene.tiles.discover(pos, oldValue);
		}
	}
	
	public static void show( Window wnd ) {
		cancelCellSelector();
		scene.add( wnd );
	}
	
	public static void afterObserve() {
		if (scene != null && scene.fog != null) {
			scene.fog.updateVisibility( Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped );
			
			for (Mob mob : Dungeon.level.mobs) {
                if( mob.sprite.visible = Dungeon.visible[mob.pos] ){
                    if( Dungeon.level.map[ mob.pos ] == Terrain.DOOR_ILLUSORY ){
                        Door.discover( mob.pos );
                    }
                }
            }


            updateMap(Dungeon.visible);
		}
	}
	
	public static void flash( int color ) {
		scene.fadeIn( 0xFF000000 | color, true );
	}
	
	public static void gameOver() {
		Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
		gameOver.show( 0x000000, 1f );
		scene.showBanner( gameOver );
		
		Sample.INSTANCE.play(Assets.SND_DEATH);
	}
	
	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
			bossSlain.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( bossSlain );
			
			Sample.INSTANCE.play( Assets.SND_BOSS );
		}
	}
	
	public static void handleCell( int cell ) {
		cellSelector.select( cell );
	}
	
	public static void selectCell( CellSelector.Listener listener ) {
		cellSelector.listener = listener;
		scene.prompt(listener.prompt());
	}

    public static boolean checkListener( CellSelector.Listener listener ) {
        return cellSelector.listener == listener;
    }

    public static boolean checkListener() {
        return cellSelector.listener == null || cellSelector.listener == defaultCellListener;
    }

    public static boolean cancelCellSelector() {
		if (cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}

	
	public static WndBag selectItem( WndBag.Listener listener, WndBag.Mode mode, String title ) {
		cancelCellSelector();
		
		WndBag wnd = mode == Mode.HERB ?
			WndBag.herbPouch(listener, mode, title) :
			WndBag.lastBag( listener, mode, title );
		scene.add( wnd );
		
		return wnd;
	}

	static boolean cancel() {

		if (Dungeon.hero.curAction != null || Dungeon.hero.restoreHealth) {
			
			Dungeon.hero.curAction = null;
			Dungeon.hero.interrupt();
			return true;
			
		} else {
			
			return cancelCellSelector();
			
		}
	}
	
	public static void ready() {
		selectCell( defaultCellListener );
		QuickSlot.cancel();
	}
	
	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if (Dungeon.hero.handle( cell )) {
				Dungeon.hero.next();
			}
		}
		@Override
		public String prompt() {
			return null;
		}
	};
}
