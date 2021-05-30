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
package com.ravenwolf.nnypdcn.levels.painters;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.actors.mobs.Mob;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.Shopkeeper;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.ShopkeeperDemon;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.ShopkeeperDwarf;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.ShopkeeperGhost;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.ShopkeeperPagueDoc;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.ShopkeeperRobot;
import com.ravenwolf.nnypdcn.actors.mobs.npcs.ShopkeeperTroll;
import com.ravenwolf.nnypdcn.items.Generator;
import com.ravenwolf.nnypdcn.items.Heap;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.armours.Armour;
import com.ravenwolf.nnypdcn.items.armours.body.BodyArmorCloth;
import com.ravenwolf.nnypdcn.items.armours.body.DiscArmor;
import com.ravenwolf.nnypdcn.items.armours.body.HuntressArmor;
import com.ravenwolf.nnypdcn.items.armours.body.MageArmor;
import com.ravenwolf.nnypdcn.items.armours.body.MailArmor;
import com.ravenwolf.nnypdcn.items.armours.body.PlateArmor;
import com.ravenwolf.nnypdcn.items.armours.body.RogueArmor;
import com.ravenwolf.nnypdcn.items.armours.body.ScaleArmor;
import com.ravenwolf.nnypdcn.items.armours.body.SplintArmor;
import com.ravenwolf.nnypdcn.items.armours.body.StuddedArmor;
import com.ravenwolf.nnypdcn.items.armours.shields.GreatShield;
import com.ravenwolf.nnypdcn.items.armours.shields.KiteShield;
import com.ravenwolf.nnypdcn.items.armours.shields.RoundShield;
import com.ravenwolf.nnypdcn.items.armours.shields.TowerShield;
import com.ravenwolf.nnypdcn.items.armours.shields.WarShield;
import com.ravenwolf.nnypdcn.items.bags.Bag;
import com.ravenwolf.nnypdcn.items.bags.HerbPouch;
import com.ravenwolf.nnypdcn.items.bags.PotionSash;
import com.ravenwolf.nnypdcn.items.bags.ScrollHolder;
import com.ravenwolf.nnypdcn.items.bags.WandHolster;
import com.ravenwolf.nnypdcn.items.food.Pasty;
import com.ravenwolf.nnypdcn.items.misc.ArmorerKit;
import com.ravenwolf.nnypdcn.items.misc.Battery;
import com.ravenwolf.nnypdcn.items.misc.CraftingKit;
import com.ravenwolf.nnypdcn.items.misc.Explosives;
import com.ravenwolf.nnypdcn.items.misc.OilLantern;
import com.ravenwolf.nnypdcn.items.misc.Waterskin;
import com.ravenwolf.nnypdcn.items.misc.Whetstone;
import com.ravenwolf.nnypdcn.items.potions.PotionOfMending;
import com.ravenwolf.nnypdcn.items.potions.PotionOfStrength;
import com.ravenwolf.nnypdcn.items.rings.Ring;
import com.ravenwolf.nnypdcn.items.scrolls.ScrollOfIdentify;
import com.ravenwolf.nnypdcn.items.scrolls.ScrollOfUpgrade;
import com.ravenwolf.nnypdcn.items.wands.Wand;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.items.weapons.melee.AssassinBlade;
import com.ravenwolf.nnypdcn.items.weapons.melee.Axe;
import com.ravenwolf.nnypdcn.items.weapons.melee.Greataxe;
import com.ravenwolf.nnypdcn.items.weapons.melee.Battleaxe;
import com.ravenwolf.nnypdcn.items.weapons.melee.Broadsword;
import com.ravenwolf.nnypdcn.items.weapons.melee.Dagger;
import com.ravenwolf.nnypdcn.items.weapons.melee.DeerHornBlade;
import com.ravenwolf.nnypdcn.items.weapons.melee.DoubleBlade;
import com.ravenwolf.nnypdcn.items.weapons.melee.Flail;
import com.ravenwolf.nnypdcn.items.weapons.melee.Glaive;
import com.ravenwolf.nnypdcn.items.weapons.melee.Greatsword;
import com.ravenwolf.nnypdcn.items.weapons.melee.Halberd;
import com.ravenwolf.nnypdcn.items.weapons.melee.Knuckles;
import com.ravenwolf.nnypdcn.items.weapons.melee.LongStaff;
import com.ravenwolf.nnypdcn.items.weapons.melee.Mace;
import com.ravenwolf.nnypdcn.items.weapons.melee.MeleeWeapon;
import com.ravenwolf.nnypdcn.items.weapons.melee.ObsidianBlade;
import com.ravenwolf.nnypdcn.items.weapons.melee.Quarterstaff;
import com.ravenwolf.nnypdcn.items.weapons.melee.Scimitar;
import com.ravenwolf.nnypdcn.items.weapons.melee.Shortsword;
import com.ravenwolf.nnypdcn.items.weapons.melee.Spear;
import com.ravenwolf.nnypdcn.items.weapons.melee.Warhammer;
import com.ravenwolf.nnypdcn.items.weapons.ranged.Arbalest;
import com.ravenwolf.nnypdcn.items.weapons.ranged.LongBow;
import com.ravenwolf.nnypdcn.items.weapons.ranged.CompositeBow;
import com.ravenwolf.nnypdcn.items.weapons.ranged.LigthCrossbow;
import com.ravenwolf.nnypdcn.items.weapons.ranged.RangedWeapon;
import com.ravenwolf.nnypdcn.items.weapons.ranged.ShortBow;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Arrows;
import com.ravenwolf.nnypdcn.items.weapons.throwing.BluntedArrows;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Bolas;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Boomerangs;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Chakrams;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Hammers;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Harpoons;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Javelins;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Knives;
import com.ravenwolf.nnypdcn.items.weapons.throwing.MoonGlaive;
import com.ravenwolf.nnypdcn.items.weapons.throwing.PoisonDarts;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Quarrels;
import com.ravenwolf.nnypdcn.items.weapons.throwing.ThrowingSpears;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Shurikens;
import com.ravenwolf.nnypdcn.items.weapons.throwing.ThrowingWeapon;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Tomahawks;
import com.ravenwolf.nnypdcn.levels.CavesLevel;
import com.ravenwolf.nnypdcn.levels.CityLevel;
import com.ravenwolf.nnypdcn.levels.LastShopLevel;
import com.ravenwolf.nnypdcn.levels.Level;
import com.ravenwolf.nnypdcn.levels.PrisonLevel;
import com.ravenwolf.nnypdcn.levels.Room;
import com.ravenwolf.nnypdcn.levels.SewerLevel;
import com.ravenwolf.nnypdcn.levels.Terrain;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class ShopPainter extends Painter {

	private static int pasWidth;
	private static int pasHeight;

    private static ArrayList<Item> kits;
    private static ArrayList<Item> ammo1;
    private static ArrayList<Item> ammo2;

    private static Item[] defaultKits = { new Whetstone(), new ArmorerKit(), new CraftingKit(), new Battery() };
    private static Item[] defaultAmmo1 = { new Arrows(), new Arrows(), new Quarrels(), new BluntedArrows(),  new Quarrels() };
    //private static Item[] defaultAmmo1 = { new Arrows(), new Arrows(), new Quarrels(), new Quarrels() };
    //private static Item[] defaultAmmo2 = { new Bullets(), new Bullets(), new Explosives.Gunpowder(), new Explosives.Gunpowder() };

    private static final String KITS		= "shops_kits";
    private static final String AMMO1		= "shops_ammo1";
    private static final String AMMO2		= "shops_ammo2";

    public static void initAssortment() {

        kits = new ArrayList<>();
        kits.addAll( Arrays.asList( defaultKits ) );

        ammo1 = new ArrayList<>();
        ammo1.addAll( Arrays.asList( defaultAmmo1 ) );

        //ammo2 = new ArrayList<>();
        //ammo2.addAll( Arrays.asList( defaultAmmo2 ) );

    }

    public static void saveAssortment( Bundle bundle) {

        bundle.put( KITS, kits );
        bundle.put( AMMO1, ammo1 );
        //bundle.put( AMMO2, ammo2 );

    }

    public static void loadAssortment( Bundle bundle ) {

        kits = new ArrayList<>();
        for (Bundlable item : bundle.getCollection( KITS )) {
            if( item != null ){
                kits.add( (Item)item );
            }
        }

        ammo1 = new ArrayList<>();
        for (Bundlable item : bundle.getCollection( AMMO1 )) {
            if( item != null ){
                ammo1.add( (Item)item );
            }
        }

        ammo2 = new ArrayList<>();
        for (Bundlable item : bundle.getCollection( AMMO2 )) {
            if( item != null ){
                ammo2.add( (Item)item );
            }
        }
    }

	public static void paint( Level level, Room room ) {
		
		fill(level, room, Terrain.WALL);
		fill(level, room, 1, Terrain.EMPTY_SP);

		pasWidth = room.width() - 2;
		pasHeight = room.height() - 2;
		int per = pasWidth * 2 + pasHeight * 2;
		
		Item[] range = range( level );
		
		int pos = xy2p( room, room.entrance() ) + (per - range.length) / 2;
		for (int i=0; i < range.length; i++) {
			
			Point xy = p2xy( room, (pos + per) % per );
			int cell = xy.x + xy.y * Level.WIDTH;
			
			if (level.heaps.get( cell ) != null) {
				do {
					cell = room.random();
				} while (level.heaps.get( cell ) != null);
			}
			
			level.drop( range[i], cell, true ).type = Heap.Type.FOR_SALE;
			
			pos++;
		}
		
		placeShopkeeper( level, room );
		
		for (Room.Door door : room.connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}
	}
	
	private static Item[] range( Level level ) {
		
		ArrayList<Item> items = new ArrayList<Item>();

        if ( level instanceof LastShopLevel) {

            int questsCompleted = Dungeon.questsCompleted();

            Weapon weap1= Random.oneOf(
                    new Knuckles(), new Dagger(), new Quarterstaff(),
                    new LongBow(), new Shortsword(), new DeerHornBlade(),new Spear(),
                    new LongStaff(), new Axe(), new AssassinBlade(), new Scimitar(), new LigthCrossbow()
            ).enchant();
            weap1.upgrade(6-weap1.lootChapter());
            weap1.identify();
            items.add(weap1);

            Weapon weap2= Random.oneOf(
                    new Mace(), new Arbalest(),  new Flail(), new ObsidianBlade(),
                    new Glaive(), new Greataxe(), new Broadsword(), new Battleaxe(), new DoubleBlade(),
                    new Halberd(), new Warhammer(), new Greatsword(), /*new Arquebuse(), new Handcannon(),*/ new CompositeBow()
            ).enchant();
            weap2.upgrade(6-weap2.lootChapter());
            weap2.identify();
            items.add(weap2);

            Armour armor= Random.oneOf(
                   /* new StuddedArmor(),*/ new DiscArmor(), /*new RoundShield(),*/
                    new MailArmor(), new SplintArmor(), new KiteShield(),
                    new ScaleArmor(), new PlateArmor(), new TowerShield(), new WarShield(), new GreatShield()
            ).inscribe();
            armor.upgrade(6-armor.lootChapter());
            armor.identify();
            items.add(armor);

            items.add( Random.oneOf(
                    new PoisonDarts(), new Bolas(), new Boomerangs(), new ThrowingSpears(), new Hammers(),
                    /*new Knives(), */new Shurikens(), new Chakrams(), new MoonGlaive(),
                    /*new Javelins(), */new Tomahawks(), new Harpoons()
            ).random());

            items.add(Random.oneOf(
                    /*new Bullets()*/new BluntedArrows(), new Arrows(), new Quarrels(), new Quarrels()
            ).random());

            items.add(Random.oneOf(/*
                    new Explosives.Gunpowder(),*/ new Explosives.BombStick(), new Explosives.BombBundle()
            ).random());

            items.add(Generator.random(Generator.Category.RING).uncurse().upgrade(2));
            items.add(Generator.random(Generator.Category.WAND).uncurse().upgrade(2));

            items.add(new PotionOfMending());
            items.add(Random.Int(5 - questsCompleted) == 0 ? new PotionOfStrength() : Generator.random(Generator.Category.POTION));

            items.add(new ScrollOfIdentify());
            items.add(Random.Int(5 - questsCompleted) == 0 ? new ScrollOfUpgrade() : Generator.random(Generator.Category.SCROLL));
            //items.add(new ScrollOfEnchantment());

            //items.add(Random.oneOf(new Whetstone(), new ArmorerKit(), new Battery(), new CraftingKit()));
            //items.add( new RepairKit() );
            items.add(new Pasty());

            items.add(new Waterskin());
            items.add(new OilLantern.OilFlask());

        } else {
            int bonus=0;
            Bag bag = null;
            Weapon weapon = null;
            Armour armour = null;
            Item ranged = null;
            ThrowingWeapon thrown = null;
            Item extra = null;

            if (level instanceof SewerLevel) {
                bonus=1;
                bag = new HerbPouch();

                weapon = Random.oneOf(new Dagger(), new Knuckles(), new Quarterstaff(), new Shortsword(), new Axe());
                //armour = Random.oneOf(new MageArmor(), new RogueArmor(), new HuntressArmor());
                extra = Random.oneOf(new MageArmor(), new RogueArmor(), new HuntressArmor(), new StuddedArmor());
                ranged = Random.oneOf(/*new Sling()*/new ShortBow(), new Bolas());
                thrown = Random.oneOf(new PoisonDarts(), new Knives());

            } else if (level instanceof PrisonLevel || Dungeon.chapter()==2) {

                bag = new PotionSash();

                weapon = Random.oneOf(new Spear(), new Mace(), new Scimitar(), new LongStaff());
                armour = Random.oneOf(new StuddedArmor(), new DiscArmor()/*, new RoundShield()*/);
                ranged = Random.oneOf(new LongBow(),new LongBow(), new LigthCrossbow()/*,new Pistole()*/);
                thrown = Random.oneOf(new Javelins(), new Boomerangs(), new Shurikens());
                extra = Random.oneOf(new RoundShield(), new RoundShield(), new RoundShield(), new MageArmor(), new RogueArmor(), new HuntressArmor(),new Dagger(), new Knuckles(), new Quarterstaff(), new Shortsword(), new Axe());

            } else if (level instanceof CavesLevel) {

                bag = new ScrollHolder();

                weapon = Random.oneOf(new Glaive(), new Greataxe(), new Broadsword(), new DoubleBlade(), new ObsidianBlade());
                armour = Random.oneOf(new MailArmor(), new SplintArmor(), new StuddedArmor(), new DiscArmor()/*, new KiteShield()*/);
                //ranged = Random.oneOf(new Arbalest(), new Arquebuse(), new Bow());
                ranged = Random.oneOf(new LigthCrossbow(), new LigthCrossbow(), /*new Arquebuse(),*/ new CompositeBow());
                thrown = Random.oneOf( new Tomahawks(), new Hammers(), new MoonGlaive());
                extra = Random.oneOf( new KiteShield(),new KiteShield(),new WarShield(), new Spear(), new Mace(), new DeerHornBlade(), new Scimitar(), new LongStaff(), new AssassinBlade());

            } else if (level instanceof CityLevel || Dungeon.chapter()==4) {

                bag = new WandHolster();

                weapon = Random.oneOf(new Halberd(), new Warhammer(), new Greatsword(), new Flail());
                armour = Random.oneOf(new ScaleArmor(), new PlateArmor(), new MailArmor(), new SplintArmor()/*, new TowerShield()*/);
                ranged = Random.oneOf(/*new Handcannon(),*/ new Arbalest(), new Arbalest()/*new Explosives.BombStick()*/, new CompositeBow());
                thrown = Random.oneOf(new Harpoons(), new Chakrams(), new ThrowingSpears());
                extra = Random.oneOf(new TowerShield(),new TowerShield(),new GreatShield(), new AssassinBlade(), new Glaive(), new Greataxe(), new Broadsword(), new DoubleBlade());

            }

            if( bag != null ) {
                items.add(bag);
            }

            //int maxUpgradeLvl=Dungeon.chapter()+1/2;
            int maxUpgradeLvl=Math.min(Dungeon.chapter(),2);

            if( extra != null ) {
                extra.identify();

                if( extra instanceof BodyArmorCloth) {
                    if (Random.Int(6)+Dungeon.chapter()>3)
                        ((BodyArmorCloth) extra).inscribe();
                }
                //except the first chapter extra item have at least one upgrade
                extra.upgrade(Random.Int(1-bonus,maxUpgradeLvl+1));
                if (extra instanceof MeleeWeapon){
                    //((MeleeWeapon)extra).material=Material.getMaterial(Random.Int(upgradeBonus-bonus,Dungeon.chapter()+upgradeBonus));
                    if (Random.Int(6)+Dungeon.chapter()+bonus>5) {
                        ((MeleeWeapon)extra).enchant();
                        //weapon.upgrade(Random.Int(Dungeon.chapter()));
                    }
                }
                items.add(extra);
            }

            if( weapon != null ) {
                weapon.identify();
                //weapon.repair().fix().upgrade(Random.Int(Dungeon.chapter()));
                weapon.upgrade(Random.Int(maxUpgradeLvl));
                //weapon.dmgBonus=Random.Int(Dungeon.chapter());
                //weapon.material=Material.getMaterial(Random.Int(Dungeon.chapter()));
                if (Random.Int(6)+Dungeon.chapter()+bonus>5) {
                    weapon.enchant();
                    //weapon.upgrade(Random.Int(Dungeon.chapter()));
                    //int enchantValue = Random.IntRange( 0, Random.Int( Dungeon.chapter() ));
                    //weapon.enchantment.lvl=enchantValue;
                }
                items.add(weapon);
            }

            if( armour != null ) {
                armour.identify();
                armour.upgrade( Random.Int( maxUpgradeLvl ) );
                if (Random.Int(6)+Dungeon.chapter()>5)
                    armour.inscribe();
                items.add(armour);
            }

            if( ranged instanceof RangedWeapon ) {
                ranged.identify();
                ranged.upgrade(Random.Int(maxUpgradeLvl));
                if (Random.Int(6)+Dungeon.chapter()>5) {
                    ((RangedWeapon) ranged).enchant();
                    Random.IntRange( 0, Random.Int( Dungeon.chapter() ));
                }
                items.add(ranged);
            } else if( ranged instanceof ThrowingWeapon || ranged instanceof Explosives) {
                ranged.random();
                items.add(ranged);
            }

            if( thrown != null ) {
                thrown.random();
                items.add(thrown);
            }

            items.add( generateAmmo1() );
            //items.add( generateAmmo2() );

            Ring ring = (Ring)Generator.random(Generator.Category.RING);
            if( ring != null) {
                ring.bonus = Random.Int( Dungeon.chapter() );
                ring.uncurse(3);
                items.add(ring);
            }

            Wand wand = (Wand)Generator.random(Generator.Category.WAND);
            if( wand != null ) {
                wand.bonus = Random.Int( Dungeon.chapter() );
                wand.uncurse(3);
                wand.initialCharges();
                items.add(wand);
            }

            items.add(new PotionOfMending());
            items.add(Generator.random(Generator.Category.POTION));

            items.add(new ScrollOfIdentify());
            //items.add(new ScrollOfUpgrade());
            items.add(Generator.random(Generator.Category.SCROLL));

            //items.add( generateKits() );
            /*if(Dungeon.chapter()==1 || Dungeon.chapter()==3)
                items.add( new RepairKit() );*/

            items.add(new Pasty());
            items.add(new Waterskin());
            items.add(new OilLantern.OilFlask());
        }
		
		Item[] range = items.toArray( new Item[0] );
//		Random.shuffle( range );
		
		return range;
	}

    private static Item generateAmmo1() {
        if( !ammo1.isEmpty() ) {
            return ammo1.remove( Random.Int( ammo1.size() ) ).random();
        } else {
            return Random.oneOf( defaultAmmo1 ).random();
        }
    }
/*
    private static Item generateAmmo2() {
        if( !ammo2.isEmpty() ) {
            return ammo2.remove( Random.Int( ammo2.size() ) ).random();
        } else {
            return Random.oneOf( defaultAmmo2 ).random();
        }
    }
*/
    private static Item generateKits() {
        if( !kits.isEmpty() ) {
            return kits.remove( Random.Int( kits.size() ) );
        } else {
            return Random.oneOf( defaultKits );
        }
    }
	
	private static void placeShopkeeper( Level level, Room room ) {

        int pos;
        do {
            pos = room.random(1);
        } while (level.heaps.get(pos) != null);

        Mob shopkeeper;

        if (level instanceof LastShopLevel) {
            shopkeeper = new ShopkeeperDemon();
        } else if( level instanceof CityLevel) {
            shopkeeper = new ShopkeeperDwarf();
        } else if( level instanceof CavesLevel) {
            if(Dungeon.cavesOption==Dungeon.MINES_OPTION)
                shopkeeper = new ShopkeeperRobot();
            else
                shopkeeper = new ShopkeeperTroll();
        } else if( level instanceof PrisonLevel) {
            if(Dungeon.prisonOption==Dungeon.GRAVEYARD_OPTION)
                shopkeeper = new ShopkeeperGhost();
            else
                shopkeeper = new ShopkeeperPagueDoc();
        } else {
            shopkeeper = new Shopkeeper();
        }

        shopkeeper.pos = pos;
        level.mobs.add( shopkeeper );

		if (level instanceof LastShopLevel) {
			for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
				int p = shopkeeper.pos + Level.NEIGHBOURS9[i];
				if (level.map[p] == Terrain.EMPTY_SP) {
					level.map[p] = Terrain.WATER;
				}
			}
		}
	}
	
	private static int xy2p( Room room, Point xy ) {
		if (xy.y == room.top) {
			
			return (xy.x - room.left - 1);
			
		} else if (xy.x == room.right) {
			
			return (xy.y - room.top - 1) + pasWidth;
			
		} else if (xy.y == room.bottom) {
			
			return (room.right - xy.x - 1) + pasWidth + pasHeight;
			
		} else /*if (xy.x == room.left)*/ {
			
			if (xy.y == room.top + 1) {
				return 0;
			} else {
				return (room.bottom - xy.y - 1) + pasWidth * 2 + pasHeight;
			}
			
		}
	}
	
	private static Point p2xy( Room room, int p ) {
		if (p < pasWidth) {
			
			return new Point( room.left + 1 + p, room.top + 1);
			
		} else if (p < pasWidth + pasHeight) {
			
			return new Point( room.right - 1, room.top + 1 + (p - pasWidth) );
			
		} else if (p < pasWidth * 2 + pasHeight) {
			
			return new Point( room.right - 1 - (p - (pasWidth + pasHeight)), room.bottom - 1 );
			
		} else {

			return new Point( room.left + 1, room.bottom - 1 - (p - (pasWidth * 2 + pasHeight)) );
			
		}
	}
}
