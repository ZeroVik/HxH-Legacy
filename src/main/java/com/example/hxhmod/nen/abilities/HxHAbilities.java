package com.example.hxhmod.nen.abilities;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.nen.NenAbility;
import com.example.hxhmod.nen.NenType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

/**
 * Collection of implemented Nen abilities from Hunter x Hunter
 */
public class HxHAbilities {

    // Gon's Jajanken Rock ability
    public static class JajankenRock extends NenAbility {
        public JajankenRock() {
            super(
                    HxHMod.location("jajanken_rock"),
                    "Jajanken: Rock",
                    "Focuses aura into a powerful punch with enhanced strength",
                    NenType.ENHANCER,
                    20,
                    400, // 20 second cooldown
                    1
            );
        }

        @Override
        protected boolean executeAbility(Player player) {
            // Grant strength effect and damage boost
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 3));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0));

            // Visual effect (particle handling would be in another class)
            // particleHandler.spawnJajankenRockParticles(player);

            return true;
        }
    }

    // Gon's Jajanken Paper ability
    public static class JajankenPaper extends NenAbility {
        public JajankenPaper() {
            super(
                    HxHMod.location("jajanken_paper"),
                    "Jajanken: Paper",
                    "Emits aura as a long-range projectile attack",
                    NenType.EMITTER,
                    15,
                    300, // 15 second cooldown
                    1
            );
        }

        @Override
        protected boolean executeAbility(Player player) {
            // Spawn a projectile entity in the direction player is looking
            // Implementation would require a custom projectile entity
            // JajankenProjectile projectile = new JajankenProjectile(player);
            // player.level.addFreshEntity(projectile);

            return true;
        }
    }

    // Gon's Jajanken Scissors ability
    public static class JajankenScissors extends NenAbility {
        public JajankenScissors() {
            super(
                    HxHMod.location("jajanken_scissors"),
                    "Jajanken: Scissors",
                    "Creates a blade of aura that can cut through materials",
                    NenType.TRANSMUTER,
                    18,
                    300, // 15 second cooldown
                    1
            );
        }

        @Override
        protected boolean executeAbility(Player player) {
            // This would give the player an effect that changes their attack type
            // and increases range
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1));

            // Visual effect for the aura blade
            // particleHandler.spawnJajankenScissorsParticles(player);

            return true;
        }
    }

    // Killua's Godspeed ability
    public static class Godspeed extends NenAbility {
        public Godspeed() {
            super(
                    HxHMod.location("godspeed"),
                    "Godspeed",
                    "Transmutes aura into electricity to drastically enhance speed and reflexes",
                    NenType.TRANSMUTER,
                    35,
                    1200, // 60 second cooldown
                    3
            );
        }

        @Override
        protected boolean executeAbility(Player player) {
            // Grant extreme speed and reflexes
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 4));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 2));
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 200, 2));

            // Visual electrical effect
            // particleHandler.spawnElectricityParticles(player);

            return true;
        }
    }

    // Kurapika's Emperor Time ability
    public static class EmperorTime extends NenAbility {
        public EmperorTime() {
            super(
                    HxHMod.location("emperor_time"),
                    "Emperor Time",
                    "Allows 100% efficiency in all Nen categories when eyes turn scarlet",
                    NenType.SPECIALIST,
                    40,
                    2400, // 2 minute cooldown
                    4
            );
        }

        @Override
        protected boolean executeAbility(Player player) {
            // Grant multiple buffs representing mastery of all Nen types
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 2)); // Enhancement
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 1)); // Enhancement
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1)); // Enhancement
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0)); // Specialist

            // This would also temporarily change player's efficiency with all abilities
            // Implementation would require modifying the PlayerNen capability

            return true;
        }
    }

    // Hisoka's Bungee Gum ability
    public static class BungeeGum extends NenAbility {
        public BungeeGum() {
            super(
                    HxHMod.location("bungee_gum"),
                    "Bungee Gum",
                    "Transmutes aura to have properties of both rubber and gum",
                    NenType.TRANSMUTER,
                    15,
                    100, // 5 second cooldown
                    2
            );
        }

        @Override
        protected boolean executeAbility(Player player) {
            // This would need custom implementation for attaching to entities/blocks
            // and pulling them towards the player or vice versa

            // For now, just give jump boost and slow falling as placeholder effects
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 200, 2));
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0));

            return true;
        }
    }

    // Netero's 100-Type Guanyin Bodhisattva
    public static class GuanyinBodhisattva extends NenAbility {
        public GuanyinBodhisattva() {
            super(
                    HxHMod.location("guanyin_bodhisattva"),
                    "100-Type Guanyin Bodhisattva",
                    "Conjures a gigantic multi-armed statue that can attack at incredible speeds",
                    NenType.CONJURER,
                    100,
                    6000, // 5 minute cooldown
                    5
            );
        }

        @Override
        protected boolean executeAbility(Player player) {
            // This would summon a massive entity that attacks nearby enemies
            // Implementation would require a complex custom entity

            // For now, just give massive strength and temporary invulnerability
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 9));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 4));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 300, 2));

            return true;
        }
    }

    // Chrollo's Skill Hunter ability
    public static class SkillHunter extends NenAbility {
        public SkillHunter() {
            super(
                    HxHMod.location("skill_hunter"),
                    "Skill Hunter",
                    "Conjures a book that allows stealing and using others' abilities",
                    NenType.SPECIALIST,
                    50,
                    3600, // 3 minute cooldown
                    5
            );
        }

        @Override
        protected boolean executeAbility(Player player) {
            // This would be an extremely complex ability to implement fully
            // For now, just give a random selection of effects representing stolen abilities

            int randomEffect = player.level.random.nextInt(5);
            switch (randomEffect) {
                case 0:
                    // "Stolen" Enhancer ability
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 3));
                    break;
                case 1:
                    // "Stolen" Transmuter ability
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 3));
                    break;
                case 2:
                    // "Stolen" Conjurer ability
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 2));
                    break;
                case 3:
                    // "Stolen" Emitter ability
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 400, 0));
                    break;
                case 4:
                    // "Stolen" Manipulator ability
                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 400, 0));
                    break;
            }

            return true;
        }
    }
}