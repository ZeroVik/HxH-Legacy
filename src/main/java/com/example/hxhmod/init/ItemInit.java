package com.example.hxhmod.init;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.item.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registry class for all items in the mod
 */
public class ItemInit {
    // Create the deferred register for items
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HxHMod.MOD_ID);

    // Register the Hunter License item
    public static final RegistryObject<Item> HUNTER_LICENSE =
            ITEMS.register("hunter_license", HunterLicenseItem::new);

    // Register the Water Divination test item
    public static final RegistryObject<Item> WATER_DIVINATION =
            ITEMS.register("water_divination", WaterDivinationItem::new);

   /** // Register Gon's Fishing Rod
    public static final RegistryObject<Item> GONS_FISHING_ROD =
            ITEMS.register("gons_fishing_rod", () -> new EnhancedFishingRodItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(250)));

    // Register Killua's Yo-Yos
    public static final RegistryObject<Item> KILLUAS_YOYO =
            ITEMS.register("killuas_yoyo", () -> new YoYoItem(new Item.Properties()
                    .stacksTo(2)
                    .durability(150)));

    // Register Ben's Knife
    public static final RegistryObject<Item> BENS_KNIFE =
            ITEMS.register("bens_knife", () -> new BensKnifeItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(200)));

    // Register Hisoka's Playing Cards
    public static final RegistryObject<Item> HISOKAS_CARD =
            ITEMS.register("hisokas_card", () -> new HisokasCardItem(new Item.Properties()
                    .stacksTo(16)));

    // Register Greed Island Ring
    public static final RegistryObject<Item> GREED_ISLAND_RING =
            ITEMS.register("greed_island_ring", () -> new GreedIslandRingItem(new Item.Properties()
                    .stacksTo(1)));

    // Register Nen Awakening Scroll
    public static final RegistryObject<Item> NEN_AWAKENING_SCROLL =
            ITEMS.register("nen_awakening_scroll", () -> new NenAwakeningScrollItem(new Item.Properties()
                    .stacksTo(1)));

    // Register items for Nen training
    public static final RegistryObject<Item> NEN_TRAINING_WEIGHT =
            ITEMS.register("nen_training_weight", () -> new NenTrainingItem(new Item.Properties()
                    .stacksTo(16)));

    // Register Nen ability book
    public static final RegistryObject<Item> NEN_ABILITY_BOOK =
            ITEMS.register("nen_ability_book", () -> new NenAbilityBookItem(new Item.Properties()
                    .stacksTo(1)));

    /**
     * Register this deferred register to the mod event bus
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}