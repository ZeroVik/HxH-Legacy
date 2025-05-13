package com.example.hxhmod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Configuration class for the HxH Mod
 */
public class HxHModConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final CommonConfig COMMON;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
                .configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class CommonConfig {
        // General settings
        public final ForgeConfigSpec.BooleanValue enableWelcomeMessage;
        public final ForgeConfigSpec.BooleanValue enableDebugMode;

        // Nen system settings
        public final ForgeConfigSpec.BooleanValue enableNenSystem;
        public final ForgeConfigSpec.IntValue baseAuraPoints;
        public final ForgeConfigSpec.IntValue auraRegenRate;
        public final ForgeConfigSpec.IntValue auraRegenInterval;
        public final ForgeConfigSpec.DoubleValue nenAbilityCostMultiplier;

        // Entity settings
        public final ForgeConfigSpec.BooleanValue enableChimeraAnts;
        public final ForgeConfigSpec.IntValue chimeraAntSpawnWeight;
        public final ForgeConfigSpec.IntValue chimeraAntMinGroupSize;
        public final ForgeConfigSpec.IntValue chimeraAntMaxGroupSize;

        public final ForgeConfigSpec.BooleanValue enablePhantomTroupe;
        public final ForgeConfigSpec.IntValue phantomTroupeSpawnWeight;
        public final ForgeConfigSpec.IntValue phantomTroupeMinGroupSize;
        public final ForgeConfigSpec.IntValue phantomTroupeMaxGroupSize;

        // World generation settings
        public final ForgeConfigSpec.BooleanValue enableHunterAssociationStructure;
        public final ForgeConfigSpec.IntValue hunterAssociationStructureSpacing;
        public final ForgeConfigSpec.IntValue hunterAssociationStructureSeparation;

        public final ForgeConfigSpec.BooleanValue enableHeavensArenaStructure;
        public final ForgeConfigSpec.IntValue heavensArenaStructureSpacing;
        public final ForgeConfigSpec.IntValue heavensArenaStructureSeparation;

        public final ForgeConfigSpec.BooleanValue enableZoldyckMansion;
        public final ForgeConfigSpec.IntValue zoldyckMansionSpacing;
        public final ForgeConfigSpec.IntValue zoldyckMansionSeparation;

        // Dimension settings
        public final ForgeConfigSpec.BooleanValue enableGreedIslandDimension;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Hunter x Hunter Mod Configuration")
                    .push("general");

            enableWelcomeMessage = builder
                    .comment("Enable welcome message when joining a world")
                    .define("enableWelcomeMessage", true);

            enableDebugMode = builder
                    .comment("Enable debug mode for advanced logging and testing features")
                    .define("enableDebugMode", false);

            builder.pop();

            builder.comment("Nen System Settings")
                    .push("nen_system");

            enableNenSystem = builder
                    .comment("Enable the Nen system")
                    .define("enableNenSystem", true);

            baseAuraPoints = builder
                    .comment("Base maximum aura points for new players")
                    .defineInRange("baseAuraPoints", 100, 10, 1000);

            auraRegenRate = builder
                    .comment("Amount of aura points regenerated per interval")
                    .defineInRange("auraRegenRate", 1, 1, 50);

            auraRegenInterval = builder
                    .comment("Interval in ticks between aura regeneration (20 ticks = 1 second)")
                    .defineInRange("auraRegenInterval", 40, 1, 200);

            nenAbilityCostMultiplier = builder
                    .comment("Multiplier for Nen ability aura costs (higher = more expensive)")
                    .defineInRange("nenAbilityCostMultiplier", 1.0, 0.1, 10.0);

            builder.pop();

            builder.comment("Entity Settings")
                    .push("entities");

            enableChimeraAnts = builder
                    .comment("Enable Chimera Ant spawning")
                    .define("enableChimeraAnts", true);

            chimeraAntSpawnWeight = builder
                    .comment("Spawn weight for Chimera Ants (higher = more common)")
                    .defineInRange("chimeraAntSpawnWeight", 20, 1, 100);

            chimeraAntMinGroupSize = builder
                    .comment("Minimum Chimera Ant group size")
                    .defineInRange("chimeraAntMinGroupSize", 1, 1, 10);

            chimeraAntMaxGroupSize = builder
                    .comment("Maximum Chimera Ant group size")
                    .defineInRange("chimeraAntMaxGroupSize", 4, 1, 20);

            enablePhantomTroupe = builder
                    .comment("Enable Phantom Troupe spawning")
                    .define("enablePhantomTroupe", true);

            phantomTroupeSpawnWeight = builder
                    .comment("Spawn weight for Phantom Troupe members (higher = more common)")
                    .defineInRange("phantomTroupeSpawnWeight", 5, 1, 100);

            phantomTroupeMinGroupSize = builder
                    .comment("Minimum Phantom Troupe group size")
                    .defineInRange("phantomTroupeMinGroupSize", 1, 1, 5);

            phantomTroupeMaxGroupSize = builder
                    .comment("Maximum Phantom Troupe group size")
                    .defineInRange("phantomTroupeMaxGroupSize", 3, 1, 13);

            builder.pop();

            builder.comment("World Generation Settings")
                    .push("world_generation");

            enableHunterAssociationStructure = builder
                    .comment("Enable Hunter Association HQ structure generation")
                    .define("enableHunterAssociationStructure", true);

            hunterAssociationStructureSpacing = builder
                    .comment("Hunter Association HQ structure spacing")
                    .defineInRange("hunterAssociationStructureSpacing", 32, 8, 128);

            hunterAssociationStructureSeparation = builder
                    .comment("Hunter Association HQ structure separation")
                    .defineInRange("hunterAssociationStructureSeparation", 8, 4, 64);

            enableHeavensArenaStructure = builder
                    .comment("Enable Heaven's Arena structure generation")
                    .define("enableHeavensArenaStructure", true);

            heavensArenaStructureSpacing = builder
                    .comment("Heaven's Arena structure spacing")
                    .defineInRange("heavensArenaStructureSpacing", 40, 8, 128);

            heavensArenaStructureSeparation = builder
                    .comment("Heaven's Arena structure separation")
                    .defineInRange("heavensArenaStructureSeparation", 16, 4, 64);

            enableZoldyckMansion = builder
                    .comment("Enable Zoldyck Mansion structure generation")
                    .define("enableZoldyckMansion", true);

            zoldyckMansionSpacing = builder
                    .comment("Zoldyck Mansion structure spacing")
                    .defineInRange("zoldyckMansionSpacing", 64, 8, 128);

            zoldyckMansionSeparation = builder
                    .comment("Zoldyck Mansion structure separation")
                    .defineInRange("zoldyckMansionSeparation", 24, 4, 64);

            builder.pop();

            builder.comment("Dimension Settings")
                    .push("dimensions");

            enableGreedIslandDimension = builder
                    .comment("Enable Greed Island dimension")
                    .define("enableGreedIslandDimension", true);

            builder.pop();
        }
    }
}