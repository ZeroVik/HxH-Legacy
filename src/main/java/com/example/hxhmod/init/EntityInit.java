package com.example.hxhmod.init;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.entity.ChimeraAntEntity;
import com.example.hxhmod.entity.PhantomTroupeMemberEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = HxHMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityInit {
    // Create the deferred register for entities
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HxHMod.MOD_ID);

    // Register Chimera Ant entity
    public static final RegistryObject<EntityType<ChimeraAntEntity>> CHIMERA_ANT =
            ENTITIES.register("chimera_ant",
                    () -> EntityType.Builder.of(ChimeraAntEntity::new, MobCategory.MONSTER)
                            .sized(0.8F, 1.8F)  // Width, height
                            .clientTrackingRange(8)
                            .build(new ResourceLocation(HxHMod.MOD_ID, "chimera_ant").toString()));

    // Register Phantom Troupe Member entity (placeholder - implementation would be similar to Chimera Ant)
    public static final RegistryObject<EntityType<PhantomTroupeMemberEntity>> PHANTOM_TROUPE_MEMBER =
            ENTITIES.register("phantom_troupe_member",
                    () -> EntityType.Builder.of(PhantomTroupeMemberEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)  // Width, height
                            .clientTrackingRange(8)
                            .build(new ResourceLocation(HxHMod.MOD_ID, "phantom_troupe_member").toString()));

    // Add more entity registrations here...

    /**
     * Register this deferred register to the mod event bus
     */
    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    /**
     * Set up entity attributes
     */
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CHIMERA_ANT.get(), ChimeraAntEntity.createAttributes().build());
        event.put(PHANTOM_TROUPE_MEMBER.get(), PhantomTroupeMemberEntity.createAttributes().build());

        // Add more attribute registrations here...
    }

    /**
     * Set up entity spawning
     */
    public static void setupEntitySpawning(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Set up Chimera Ant spawning
            SpawnPlacements.register(CHIMERA_ANT.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    ChimeraAntEntity::checkMonsterSpawnRules);

            // Set up Phantom Troupe Member spawning
            SpawnPlacements.register(PHANTOM_TROUPE_MEMBER.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    PhantomTroupeMemberEntity::checkMonsterSpawnRules);

            // Add more spawn registrations here...
        });
    }
}