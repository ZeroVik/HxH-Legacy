package com.example.hxhmod.nen;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.capabilities.PlayerNen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for all Nen abilities
 */
public abstract class NenAbility {
    // Registry of all Nen abilities
    private static final Map<ResourceLocation, NenAbility> ABILITIES = new HashMap<>();

    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final NenType primaryType;
    private final int baseCost;
    private final int cooldown; // in ticks
    private final int level; // ability level/tier

    public NenAbility(ResourceLocation id, String name, String description, NenType primaryType,
                      int baseCost, int cooldown, int level) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.primaryType = primaryType;
        this.baseCost = baseCost;
        this.cooldown = cooldown;
        this.level = level;
    }

    /**
     * Get the ability's unique identifier
     */
    public ResourceLocation getId() {
        return id;
    }

    /**
     * Get the ability's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the ability's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the primary Nen type for this ability
     */
    public NenType getPrimaryType() {
        return primaryType;
    }

    /**
     * Get the base aura cost for using this ability
     */
    public int getBaseCost() {
        return baseCost;
    }

    /**
     * Get the cooldown time in ticks
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Get the ability's level/tier
     */
    public int getLevel() {
        return level;
    }

    /**
     * Calculate the actual aura cost based on player's Nen type
     */
    public int calculateCost(Player player) {
        PlayerNen playerNen = PlayerNen.getNen(player);
        NenType playerType = playerNen.getNenType();

        if (playerType == null) {
            return baseCost; // Default cost if player hasn't determined their type
        }

        // Calculate efficiency percentage based on Nen type compatibility
        int efficiency = playerType.getEfficiencyFor(primaryType);

        // Apply mastery level discount (5% per level)
        int masteryDiscount = playerNen.getNenMasteryLevel() * 5;

        // Calculate final cost (higher efficiency = lower cost)
        return Math.max(1, baseCost * (100 - efficiency + masteryDiscount) / 100);
    }

    /**
     * Activate the ability for a player
     * @return true if activation was successful
     */
    public boolean activate(Player player) {
        PlayerNen playerNen = PlayerNen.getNen(player);

        // Check if player has awakened Nen
        if (!playerNen.isNenAwakened()) {
            player.sendSystemMessage(Component.translatable("hxhmod.message.nen_not_awakened"));
            return false;
        }

        // Calculate aura cost
        int cost = calculateCost(player);

        // Check if player has enough aura
        if (!playerNen.useAura(cost)) {
            player.sendSystemMessage(Component.translatable("hxhmod.message.not_enough_aura"));
            return false;
        }

        // Set ability cooldown
        setCooldown(player);

        // Execute ability effect
        return executeAbility(player);
    }

    /**
     * Execute the ability's effect
     * @return true if execution was successful
     */
    protected abstract boolean executeAbility(Player player);

    /**
     * Set cooldown for this ability
     */
    protected void setCooldown(Player player) {
        // Implementation will depend on the mod's cooldown system
    }

    /**
     * Serialize the ability to NBT
     */
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Id", id.toString());
        return tag;
    }

    /**
     * Deserialize an ability from NBT
     */
    public static NenAbility fromNBT(CompoundTag tag) {
        ResourceLocation id = new ResourceLocation(tag.getString("Id"));
        return ABILITIES.get(id);
    }

    /**
     * Register a new ability
     */
    public static void register(NenAbility ability) {
        ABILITIES.put(ability.getId(), ability);
    }

    /**
     * Get an ability by ID
     */
    public static NenAbility getAbility(ResourceLocation id) {
        return ABILITIES.get(id);
    }
}