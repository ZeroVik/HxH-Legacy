package com.example.hxhmod;

import com.example.hxhmod.config.HxHModConfig;
import com.example.hxhmod.init.*;
import com.example.hxhmod.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HxHMod.MOD_ID)
public class HxHMod {
    public static final String MOD_ID = "hxhmod";
    public static final Logger LOGGER = LogManager.getLogger();

    public HxHMod() {
        // Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, HxHModConfig.COMMON_SPEC);

        // Register content
        ItemInit.register(modEventBus);
        BlockInit.register(modEventBus);
        EntityInit.register(modEventBus);
        EnchantmentInit.register(modEventBus);
        SoundInit.register(modEventBus);
        ParticleInit.register(modEventBus);
        EffectInit.register(modEventBus);
        NenTypeInit.register(modEventBus);

        // Register mod events
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);

        // Register ourselves for server and other game events
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.registerMessages();
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientSetup.init();
        });
    }

    // Helper method to create mod resource locations
    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}