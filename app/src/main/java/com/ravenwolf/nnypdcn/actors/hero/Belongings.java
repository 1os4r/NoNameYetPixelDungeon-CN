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
package com.ravenwolf.nnypdcn.actors.hero;

import com.ravenwolf.nnypdcn.Dungeon;
import com.ravenwolf.nnypdcn.items.EquipableItem;
import com.ravenwolf.nnypdcn.items.Item;
import com.ravenwolf.nnypdcn.items.armours.body.BodyArmor;
import com.ravenwolf.nnypdcn.items.bags.Backpack;
import com.ravenwolf.nnypdcn.items.bags.Bag;
import com.ravenwolf.nnypdcn.items.keys.IronKey;
import com.ravenwolf.nnypdcn.items.keys.Key;
import com.ravenwolf.nnypdcn.items.rings.Ring;
import com.ravenwolf.nnypdcn.items.weapons.Weapon;
import com.ravenwolf.nnypdcn.items.weapons.throwing.ThrowingWeapon;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Iterator;

public class Belongings implements Iterable<Item> {

	public static final int BACKPACK_SIZE	= 15;
	
	private Hero owner;
	
	public Bag backpack;

	public Weapon weap1 = null;
	public EquipableItem weap2 = null;
	public EquipableItem ranged = null;
	public Class<? extends ThrowingWeapon> lastEquipedAmmo = null;
	public BodyArmor armor = null;
	public Ring ring1 = null;
	public Ring ring2 = null;


	
	public Belongings( Hero owner ) {
		this.owner = owner;
		
		backpack = new Backpack();
		backpack.owner = owner;
	}
	
	private static final String BACKPACK	= "backpack";
	private static final String WEAP1		= "weap1";
	private static final String WEAP2		= "weap2";
	private static final String RANGED		= "ranged";
	private static final String ARMOR		= "armor";
	private static final String RING1		= "ring1";
	private static final String RING2		= "ring2";
	
	public void storeInBundle( Bundle bundle ) {
		
		backpack.storeInBundle( bundle );

		bundle.put( WEAP1, weap1 );
		bundle.put( WEAP2, weap2 );
		bundle.put( RANGED, ranged);
		bundle.put( ARMOR, armor );
		bundle.put( RING1, ring1 );
		bundle.put( RING2, ring2 );

	}
	
	public void restoreFromBundle( Bundle bundle ) {

		backpack.clear();
		backpack.restoreFromBundle(bundle);

        weap1 = (Weapon)bundle.get( WEAP1 );
        if (weap1 != null) {
            weap1.activate( owner );
        }

        weap2 = (EquipableItem)bundle.get( WEAP2 );
        if (weap2 != null) {
            weap2.activate( owner );
        }

		ranged = (EquipableItem)bundle.get( RANGED );
		if (ranged != null) {
			ranged.activate( owner );
		}

		armor = (BodyArmor)bundle.get( ARMOR );
		
		ring1 = (Ring)bundle.get( RING1 );
		if (ring1 != null) {
			ring1.activate( owner );
		}
		
		ring2 = (Ring)bundle.get( RING2 );
		if (ring2 != null) {
			ring2.activate( owner );
		}
	}
	
	@SuppressWarnings("unchecked")
     public<T extends Item> T getItem( Class<T> itemClass ) {

        for (Item item : this) {
            if (itemClass.isInstance( item )) {
                return (T)item;
            }
        }

        return null;
    }
	
	@SuppressWarnings("unchecked")
	public <T extends Key> T getKey( Class<T> kind, int depth ) {
		
		for (Item item : backpack) {
			if (item.getClass() == kind && ((Key)item).depth == depth) {
				return (T)item;
			}
		}
		
		return null;
	}
	
	public void countIronKeys() {
		
		IronKey.curDepthQuantity = 0;
		
		for (Item item : backpack) {
			if (item instanceof IronKey && ((IronKey)item).depth == Dungeon.depth) {
				IronKey.curDepthQuantity++;
			}
		}
	}
	
	public void identify() {
		for (Item item : this) {
			item.identify();
		}
	}
	
	public void observe(){
        if( weap1 != null ){
            weap1.identify();
//            Badges.validateItemLevelAcquired(weap1);
        }
        if( weap2 != null ){
            weap2.identify();
//            Badges.validateItemLevelAcquired(weap2);
        }
        if( armor != null ){
            armor.identify();
//			Badges.validateItemLevelAcquired(armor);
        }
//		if (ring1 != null) {
//			ring1.identify();
//			Badges.validateItemLevelAcquired(ring1);
//		}
//		if (ring2 != null) {
//			ring2.identify();
//			Badges.validateItemLevelAcquired(ring2);
//		}
        for( Item item : backpack ){
            item.identify();
        }
    }

    public Item randomUnequipped() {
        return Random.element( backpack.items );
    }

    public Item randomVisibleUnequipped() {

        if( backpack.countVisibleItems() > 0 ) {

            Item item = null;

            while ( item == null || !item.visible ) {
                item = Random.element(backpack.items);
            }

            return item;

        } else {

            return null;

        }
    }
	
//	public void resurrect( int depth ) {
//
//        for (Item item : backpack.items.toArray( new Item[0])) {
//			if (item instanceof Key) {
//				if (((Key)item).depth == depth) {
//					item.detachAll( backpack );
//				}
//			} else if (item.unique) {
//            Keep unique items
//			} else if (!item.isEquipped( owner )) {
//				item.detachAll( backpack );
//			}
//		}
//
//		for (Item item : backpack.items.toArray( new Item[0])) {
//            if( item.isUpgradeable() ) {
//                item.cursed = false;
//                item.cursedKnown = true;
//            }
//
//            if( item instanceof Wand) {
//                ((Wand) item).activate( owner );
//            }
//		}
//
//		if (weapon != null) {
//			weapon.cursed = false;
//			weapon.cursedKnown = true;
//			weapon.activate( owner );
//        }
//
//        if (armor != null) {
//            armor.cursed = false;
//            armor.cursedKnown = true;
//        }
//
//        if (ring1 != null) {
//            ring1.cursed = false;
//            ring1.cursedKnown = true;
//            ring1.activate( owner );
//        }
//        if (ring2 != null) {
//            ring2.cursed = false;
//            ring2.cursedKnown = true;
//            ring2.activate( owner );
//		}
//	}
	/*
	public int charge( boolean full ) {
		
		int count = 0;
		
		for (Item item : this) {
			if (item instanceof Wand) {
				Wand wand = (Wand)item;
				if (wand.curCharges < wand.maxCharges()) {
					wand.curCharges = full ? wand.maxCharges() : wand.curCharges + 1;
					count++;
					
					wand.updateQuickslot();
				}
			}
		}
		
		return count;
	}
	
	public int discharge() {
		
		int count = 0;
		
		for (Item item : this) {
			if (item instanceof Wand) {
				Wand wand = (Wand)item;
				if (wand.curCharges > 0) {
					wand.curCharges--;
					count++;
					
					wand.updateQuickslot();
				}
			}
		}
		
		return count;
	}
*/
	@Override
	public Iterator<Item> iterator() {
		return new ItemIterator(); 
	}
	
	private class ItemIterator implements Iterator<Item> {

		private int index = 0;
		
		private Iterator<Item> backpackIterator = backpack.iterator();
		
		private Item[] equipped = {weap1, weap2, armor, ring1, ring2, ranged};
		private int backpackIndex = equipped.length;
		
		@Override
		public boolean hasNext() {
			
			for (int i=index; i < backpackIndex; i++) {
				if (equipped[i] != null) {
					return true;
				}
			}
			
			return backpackIterator.hasNext();
		}

		@Override
		public Item next() {
			
			while (index < backpackIndex) {
				Item item = equipped[index++];
				if (item != null) {
					return item;
				}
			}
			
			return backpackIterator.next();
		}

		@Override
		public void remove() {
			switch (index) {
			case 0:
				equipped[0] = weap1 = null;
				break;
            case 1:
                equipped[1] = weap2 = null;
                break;
			case 2:
				equipped[2] = armor = null;
				break;
			case 3:
				equipped[3] = ring1 = null;
				break;
			case 4:
				equipped[4] = ring2 = null;
				break;
			case 5:
				equipped[5] = ranged = null;
				break;
			default:
				backpackIterator.remove();
			}
		}
	}
}
