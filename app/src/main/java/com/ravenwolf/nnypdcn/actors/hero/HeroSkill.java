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


import com.ravenwolf.nnypdcn.actors.buffs.special.skills.*;
import com.watabou.utils.Bundle;

public enum HeroSkill {

    NONE( null, null ),

    HEROIC_LEAP( "英勇跳跃",
            "跳跃到目标位置，对周围造成猛烈地伤害，并击晕附近的敌人。 " +
                    "伤害取决于你的最大生命值", HeroicLeap.class,1 ),
    WAR_CRY( "战争怒吼",
            "你冲着周围大吼了一声，并获得狂暴状态，显著增加你的攻击伤害。当受到伤害时会增加狂暴状态的效果。 " +
                    "还会吸引附近的敌人", WarCry.class,  0 ),
    RELENTLESS_ASSAULT( "毁灭打击",
            "用你的近战武器对敌人进行毁灭性的打击，以特殊的精度和速度连续对同一目标攻击数次。" +
                    "这个攻击不会被闪避，造成的伤害将受到武器影响", Fury.class, 2 ),

    MOLTERN_EARTH( "熔焰地狱",
            "使用大地之力，缠绕并燃烧附近的敌人"
                    ,MolterEarthSkill.class,9 ),
    OVERLOAD( "过载",
            "奥术之力充满了你的全身，短时间加强你的魔法道具并恢复充能"
            , OverloadSkill.class,11 ),
    ARCANE_ORB( "奥术之球",
            "召唤一个聚集了奥术之力的球状体，它会持续的追击敌人，并释放奥术能量，对周围的敌人造成伤害。" +
                    "它的力量基于你的等级和当前所在的区域。" , SummonArcaneOrbSkill.class,10 ),

    SMOKE_BOMB( "烟幕爆炸",
            "在原地制造一片迷雾，并致盲附近的敌人，随后闪现到视野内的指定位置并暂时遁入隐身。" ,SmokeBomb.class,6),
    SHADOW_STRIKE( "暗影突袭",
            "瞬间对周围的所有敌人造成毁灭性的伏击。造成的伤害将受到武器影响。" +
                    "" ,ShadowStrike.class,8),
    CLOAK_OF_SHADOWS( "暗影迷雾",
            //"Grant an invisibility buff. When an enemy is killed, invisibility its restored instantly and the duration of the buff is increased. " +
                    "召唤出浓厚的浓雾掩盖了你的行动。并阻挡了敌人的视线，但你依然可以看透这些迷雾。" , CloakOfShadowsSkill.class,7),


    SPECTRAL_BLADES( "幻影飞刀",
            "召唤出数把幻影之刃，伤害并衰弱附近的敌人，造成的伤害将受到本身精准的影响" ,SpectralBladesSkill.class,3),
    VALKYRIE( "女神祝福",
            "受到女武神的祝福，获得短暂的圣盾和飞行的能力。并且会击飞邻近的敌人",ValkyrieSkill.class,4 ),
    THORN_SPITTER( "荆棘之主",
            "召唤一个荆棘之主，精确地向远处的敌人发射有毒的荆棘。它的力量基于你的等级和当前所在的区域，在草地上召唤时会获得额外的生命值", SummonThornspitterSkill.class,5 );


    private String title;
    private String desc;
    private int icon;
    private Class<? extends BuffSkill> skillClass;

    HeroSkill(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    HeroSkill(String title, String desc, Class<? extends BuffSkill> skillClass, int icon) {
        this.title = title;
        this.desc = desc;
        this.skillClass= skillClass;
        this.icon= icon;
    }

    public String title() {
        return title;
    }

    public String desc() {
        return desc;
    }

    public int icon() {
        return icon;
    }

    public Class<? extends BuffSkill> skillClass() {
        return skillClass;
    }

    private static final String SKILL	= "skill";

    public void storeInBundle( Bundle bundle ) {
        bundle.put( SKILL, toString() );
    }

    public static HeroSkill restoreInBundle(Bundle bundle ) {
        String value = bundle.getString( SKILL );
        try {
            return valueOf( value );
        } catch (Exception e) {
            return NONE;
        }
    }
}
