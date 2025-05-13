package com.example.hxhmod.init;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.particle.NenAuraParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registry class for all particles in the mod
 */
public class ParticleInit {
    // Create the deferred register for particles
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, HxHMod.MOD_ID);

    // Register Nen aura particle
    public static final RegistryObject<SimpleParticleType> NEN_AURA =
            PARTICLES.register("nen_aura", () -> new SimpleParticleType(false));

    // Register Jajanken Rock particle
    public static final RegistryObject<SimpleParticleType> JAJANKEN_ROCK =
            PARTICLES.register("jajanken_rock", () -> new SimpleParticleType(false));

    // Register Jajanken Paper particle
    public static final RegistryObject<SimpleParticleType> JAJANKEN_PAPER =
            PARTICLES.register("jajanken_paper", () -> new SimpleParticleType(false));

    // Register Jajanken Scissors particle
    public static final RegistryObject<SimpleParticleType> JAJANKEN_SCISSORS =
            PARTICLES.register("jajanken_scissors", () -> new SimpleParticleType(false));

    // Register Godspeed particle
    public static final RegistryObject<SimpleParticleType> GODSPEED =
            PARTICLES.register("godspeed", () -> new SimpleParticleType(false));

    // Register Emperor Time particle
    public static final RegistryObject<SimpleParticleType> EMPEROR_TIME =
            PARTICLES.register("emperor_time", () -> new SimpleParticleType(false));

    // Register Bungee Gum particle
    public static final RegistryObject<SimpleParticleType> BUNGEE_GUM =
            PARTICLES.register("bungee_gum", () -> new SimpleParticleType(false));

    // Register Water Divination particle
    public static final RegistryObject<SimpleParticleType> WATER_DIVINATION =
            PARTICLES.register("water_divination", () -> new SimpleParticleType(false));

    /**
     * Register this deferred register to the mod event bus
     */
    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }

    /**
     * Client-side handler for registering particle factories
     */
    @Mod.EventBusSubscriber(modid = HxHMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientSetup {
        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            // Register the Nen aura particle factory
            event.register(NEN_AURA.get(), NenAuraParticle.Provider::new);

            // Additional particle registrations would go here
            // Each particle type would need its own provider class similar to NenAuraParticle.Provider
        }
    }
}