package com.example.hxhmod.client;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.init.ItemInit;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Handles registration of texture-related functionality
 */
@Mod.EventBusSubscriber(modid = HxHMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TextureRegistry {

    /**
     * Register custom item models and render properties
     */
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register custom model predicates

            // Add special properties for Gon's fishing rod
            ItemProperties.register(ItemInit.GONS_FISHING_ROD.get(),
                    new ResourceLocation("cast"), (stack, level, entity, seed) -> {
                        // Implementation to check if the fishing rod is cast
                        if (entity == null) {
                            return 0.0F;
                        }
                        boolean isCast = entity.getMainHandItem() == stack && entity.fishing != null;
                        return isCast ? 1.0F : 0.0F;
                    });

            // Add special properties for Hisoka's cards
            ItemProperties.register(ItemInit.HISOKAS_CARD.get(),
                    new ResourceLocation("throwing"), (stack, level, entity, seed) -> {
                        // Implementation to check if the card is being thrown
                        if (entity == null) {
                            return 0.0F;
                        }
                        // This would need to be implemented based on how card throwing is tracked
                        return 0.0F;
                    });

            // Register more item properties as needed
        });
    }

    /**
     * Register custom item colors
     */
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        // Register color handler for Nen-related items that change color based on Nen type
        event.register((stack, layer) -> {
            // Example implementation - this would need adjustment based on how item data is stored
            if (layer == 0) {
                // Base layer remains unchanged
                return -1;
            } else if (layer == 1) {
                // Get Nen type from item NBT and return appropriate color
                // This is placeholder logic - actual implementation depends on how Nen type is stored on the item
                int nenType = stack.getOrCreateTag().getInt("NenType");
                switch (nenType) {
                    case 0: return 0xE3170D; // Enhancer - Red
                    case 1: return 0xE8D71E; // Transmuter - Yellow
                    case 2: return 0x0F8DE8; // Conjurer - Blue
                    case 3: return 0xF08228; // Emitter - Orange
                    case 4: return 0x26A828; // Manipulator - Green
                    case 5: return 0xA01EB4; // Specialist - Purple
                    default: return -1;
                }
            }
            return -1;
        }, ItemInit.NEN_ABILITY_BOOK.get(), ItemInit.NEN_TRAINING_WEIGHT.get());
    }
}