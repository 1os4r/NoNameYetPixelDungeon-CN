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

import com.ravenwolf.nnypdcn.Badges;
import com.ravenwolf.nnypdcn.items.armours.body.HuntressArmor;
import com.ravenwolf.nnypdcn.items.armours.body.MageArmor;
import com.ravenwolf.nnypdcn.items.armours.body.RogueArmor;
import com.ravenwolf.nnypdcn.items.armours.body.StuddedArmor;
import com.ravenwolf.nnypdcn.items.armours.shields.RoundShield;
import com.ravenwolf.nnypdcn.items.bags.Baldric;
import com.ravenwolf.nnypdcn.items.bags.Keyring;
import com.ravenwolf.nnypdcn.items.food.RationMedium;
import com.ravenwolf.nnypdcn.items.misc.OilLantern;
import com.ravenwolf.nnypdcn.items.misc.Waterskin;
import com.ravenwolf.nnypdcn.items.potions.PotionOfOvergrowth;
import com.ravenwolf.nnypdcn.items.rings.RingOfShadows;
import com.ravenwolf.nnypdcn.items.scrolls.ScrollOfRecharging;
import com.ravenwolf.nnypdcn.items.wands.Wand;
import com.ravenwolf.nnypdcn.items.wands.WandOfMagicMissile;
import com.ravenwolf.nnypdcn.items.weapons.melee.Dagger;
import com.ravenwolf.nnypdcn.items.weapons.melee.Quarterstaff;
import com.ravenwolf.nnypdcn.items.weapons.melee.Shortsword;
import com.ravenwolf.nnypdcn.items.weapons.ranged.ShortBow;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Arrows;
import com.ravenwolf.nnypdcn.items.weapons.throwing.Knives;
import com.ravenwolf.nnypdcn.visuals.Assets;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;


public enum HeroClass {

	WARRIOR( "warrior","??????" ), BRIGAND( "brigand","??????" ), SCHOLAR( "scholar" ,"??????"), ACOLYTE( "acolyte","??????" );

	private String title;
	private String cname;
	
	private HeroClass( String title, String cname ) {
		this.title = title;
		this.cname = cname;
	}

    public static final String[] WAR_ABOUT = {
//            "Your ancestors were many a man. Mad warlords and ruthless mercenaries, some. Noble knights and pious crusaders, others. Cowards? None of them.",
//            "Now, your family is broken and ruined, but blood of your fathers still flows strong in your veins. There is a way to remind all the world about your clan's former glory!",
//            "A way as simple as retrieving a single lost treasure hidden down there, below this city. How hard can that be, after all?",

            //"Warrior's main advantage is his greater physique - his amount of health is greater than anyone else's and he gets bonus strength with levels.",
            "?????????????????????????????????????????????-?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
            "?????????????????????????????????????????????????????????????????????????????????????????????",
    };

    public static final String[] ROG_ABOUT = {
//            "King's decree was simple - swift death at the hands of executioner, or a chance to redeem yourself by stealing one valuable trinket - a familiar work for someone like you.",
//            "Should've been a simple choice, normally. But... this one is in the Dungeon! There is a lots of rumors about this place - evil magics and stuff. You know how it goes with all these spellcasting bastards and their experiments.",
//            "Argh, to hell with that! You have always been a gambler. Nobody would say that you have let the Reaper claim you the easy way. And, who knows... After all of this is done, maybe you'll keep this trinket for yourself?",

            "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
            "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
    };

    public static final String[] MAG_ABOUT = {
//            "Your weary eyes stare down into the abyss. Fingers nervously clutching quarterstaff, searching for calmness in the familiar touch of its wood. This is it.",
//            "All threads lead here. All these years you've spent on seeking the Amulet weren't in vain. The key to all power imaginable, to all knowledge obtainable is hidden in this darkness.",
//            "You only need to brace yourself and make your first step. Your search has ended here. And here, it has only began.",

            "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
            "????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
    };

    public static final String[] HUN_ABOUT = {
//            "Holy mothers say that fey blood in your veins is a curse for your body but a blessing for your spirit. Frail in build, you always relied on your senses, intuition and faith in the Goddesses.",
//            "Beautiful Artemis, proud Athena and wise Gaia - they have always guided you, sending you insights and prophetic dreams. But, as time went, predictions started to became dark and foreboding.",
//            "They are crystal-clear now - something grows down there, under this City. Something wicked. And it must be nipped in the bud as soon as possible, or else... No gods would save us.",

            "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
            "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
    };

    public static final String[] WAR_DETAILS = {
            "?? ??????",
            "?? ??????",
            "?? ??????",
         //   "\u007F armorer's kit",
            "",
            "+????????????",
            "+??????",
            "",
            "-??????",
            "-??????",
    };

    public static final String[] ROG_DETAILS = {
            "?? ??????",
            "?? ??????x10",
            "?? ????????????",
           // "\u007F whetstone",
            "?? ????????????",
            "",
            "+ ??????",
            "+ ??????",
            "",
            "- ??????",
            "- ??????",
    };

    public static final String[] MAG_DETAILS = {
            "?? ??????",
            "?? ????????????",
            "?? ????????????",
         //   "\u007F arcane battery",
            "?? ????????????",
            "",
            "+ ??????",
            "+ ??????",
            "",
            "- ??????",
            "- ??????",
    };

    public static final String[] HUN_DETAILS = {
            "?? ??????",
            "?? ??????x30",
            "?? ??????",
            "?? ????????????",
        //    "\u007F crafting kit",
            "?? ????????????",
            "",
            "+ ??????",
            "+ ??????",
            "",
            "- ????????????",
            "- ??????",
    };

    public  ArrayList<HeroSkill>  randomizeSkills( HeroSkill ...skills  ) {
        ArrayList<HeroSkill> availableSkills=new ArrayList<>();
        while (skills.length!=availableSkills.size()) {
            HeroSkill next = Random.element(skills);
            if (!availableSkills.contains(next))
                availableSkills.add(next);
        }
        return availableSkills;
    }

	public void initHero( Hero hero ) {
		
		hero.heroClass = this;

		initCommon(hero);


		
		switch (this) {
            case WARRIOR:
                initWarrior( hero );
                hero.availableSkills=randomizeSkills(HeroSkill.HEROIC_LEAP,HeroSkill.WAR_CRY,HeroSkill.RELENTLESS_ASSAULT);
                break;

            case BRIGAND:
                initRogue( hero );
                hero.availableSkills=randomizeSkills(HeroSkill.SMOKE_BOMB,HeroSkill.SHADOW_STRIKE,HeroSkill.CLOAK_OF_SHADOWS);
                break;

            case SCHOLAR:
                initMage( hero );
                hero.availableSkills=randomizeSkills(HeroSkill.MOLTERN_EARTH,HeroSkill.OVERLOAD,HeroSkill.ARCANE_ORB);
                break;

            case ACOLYTE:
                initHuntress( hero );
                hero.availableSkills=randomizeSkills(HeroSkill.SPECTRAL_BLADES,HeroSkill.VALKYRIE,HeroSkill.THORN_SPITTER);
                break;
		}
	}
	
	private static void initCommon( Hero hero ) {

		new Keyring().collect();
        new RationMedium().collect();

        new Waterskin().setLimit( 5 ).fill().collect();
        new OilLantern().collect();

        new Baldric().collect();


    }
	
	public Badges.Badge masteryBadge() {
		switch (this) {
		case WARRIOR:
			return Badges.Badge.MASTERY_WARRIOR;
        case BRIGAND:
            return Badges.Badge.MASTERY_BRIGAND;
		case SCHOLAR:
			return Badges.Badge.MASTERY_SCHOLAR;
		case ACOLYTE:
			return Badges.Badge.MASTERY_ACOLYTE;
		}
		return null;
	}


    public Badges.Badge victoryBadge() {
        switch (this) {
            case WARRIOR:
                return Badges.Badge.VICTORY_WARRIOR;
            case BRIGAND:
                return Badges.Badge.VICTORY_BRIGAND;
            case SCHOLAR:
                return Badges.Badge.VICTORY_SCHOLAR;
            case ACOLYTE:
                return Badges.Badge.VICTORY_ACOLYTE;

        }
        return null;
    }
	
	private static void initWarrior( Hero hero ) {

		hero.STR++;

        hero.HP = (hero.HT += 4);
        hero.defenseSkill -= 4;

		(hero.belongings.weap1 = new Shortsword()).identify();
        (hero.belongings.weap2 = new RoundShield()).identify();
        (hero.belongings.armor = new StuddedArmor()).identify();

        hero.belongings.weap1.bones=false;
        hero.belongings.weap2.bones=false;
        hero.belongings.armor.bones=false;

    }

    private static void initRogue( Hero hero ) {

        hero.defenseSkill += 4;
        hero.magicSkill -= 4;

        (hero.belongings.weap1 = new Dagger()).identify();
        (hero.belongings.ranged = new Knives()).quantity(6).activate(hero);
        (hero.belongings.armor = new RogueArmor()).identify();
        (hero.belongings.ring1 = new RingOfShadows()).identify();

        hero.belongings.ring1.activate( hero );

        hero.belongings.weap1.bones=false;
        hero.belongings.ranged.bones=false;
        hero.belongings.armor.bones=false;
        hero.belongings.ring1.bones=false;

    }
	
	private static void initMage( Hero hero ) {

        hero.magicSkill += 4;
        hero.attackSkill -= 4;

		(hero.belongings.weap1 = new Quarterstaff()).identify();
		(hero.belongings.weap2 = new WandOfMagicMissile()).identify();
        (hero.belongings.armor = new MageArmor()).identify();

        ((Wand)hero.belongings.weap2).initialCharges().charge(hero);

        hero.belongings.weap1.bones=false;
        hero.belongings.weap2.bones=false;
        hero.belongings.armor.bones=false;

        new ScrollOfRecharging().identify().collect();

	}
	
	private static void initHuntress( Hero hero ) {

        hero.STR--;

        hero.HP = (hero.HT -= 4);
        hero.attackSkill += 4;

        //(hero.belongings.weap1 = new Sling()).identify();
        (hero.belongings.weap1 = new ShortBow()).identify();
        (hero.belongings.weap2 = new Dagger()).identify();
        (hero.belongings.ranged = new Arrows()).quantity( 30 ).activate(hero);
        (hero.belongings.armor = new HuntressArmor()).identify();

        hero.belongings.weap1.bones=false;
        hero.belongings.weap2.bones=false;
        hero.belongings.ranged.bones=false;
        hero.belongings.armor.bones=false;

        new PotionOfOvergrowth().identify().collect();

    }
	
	public String title() {
		return title;
	}

	public String cname(){
	    return cname;
    }

	public String spritesheet() {
		
		switch (this) {
            case WARRIOR:
                return Assets.WARRIOR;
            case BRIGAND:
                return Assets.BRIGAND;
            case SCHOLAR:
                return Assets.SCHOLAR;
            case ACOLYTE:
                return Assets.ACOLYTE;
		}
		
		return null;
	}

    public String[] history() {

        switch (this) {
            case WARRIOR:
                return WAR_ABOUT;
            case BRIGAND:
                return ROG_ABOUT;
            case SCHOLAR:
                return MAG_ABOUT;
            case ACOLYTE:
                return HUN_ABOUT;
        }

        return null;
    }

	public String[] details() {
		
		switch (this) {
            case WARRIOR:
                return WAR_DETAILS;
            case BRIGAND:
                return ROG_DETAILS;
            case SCHOLAR:
                return MAG_DETAILS;
            case ACOLYTE:
                return HUN_DETAILS;
        }
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : WARRIOR;
	}
}
