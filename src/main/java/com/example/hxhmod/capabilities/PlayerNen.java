package com.example.hxhmod.capabilities;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.nen.NenAbility;
import com.example.hxhmod.nen.NenType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

public class PlayerNen implements INBTSerializable<CompoundTag> {
    public static final Capability<PlayerNen> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static final ResourceLocation IDENTIFIER = HxHMod.location("player_nen");

    // Player's Nen type (determined through Water Divination)
    private NenType nenType = null;

    // Whether the player has awakened their Nen
    private boolean nenAwakened = false;

    // Current aura points
    private int auraPoints = 0;

    // Maximum aura points (increases with training)
    private int maxAuraPoints = 100;

    // Current Nen state (Ten, Ren, Zetsu, Hatsu)
    private NenState nenState = NenState.NORMAL;

    // List of abilities the player has learned
    private final List<NenAbility> abilities = new ArrayList<>();

    // Nen mastery level (affects efficiency)
    private int nenMasteryLevel = 0;

    // Whether Gyo is active (seeing hidden aura)
    private boolean gyoActive = false;

    // Whether En is active (sensing aura around player)
    private boolean enActive = false;

    // En radius in blocks
    private int enRadius = 10;

    public enum NenState {
        NORMAL,  // Normal state, no special effects
        TEN,     // Basic defensive aura flow, slow aura regeneration
        REN,     // Expanded aura output, increases power but drains aura
        ZETSU,   // Complete aura suppression, faster aura regeneration but vulnerable
        HATSU    // Active ability use state
    }

    // Get player's Nen type
    public NenType getNenType() {
        return nenType;
    }

    // Set player's Nen type (usually determined through Water Divination)
    public void setNenType(NenType nenType) {
        this.nenType = nenType;
    }

    // Check if player has awakened their Nen
    public boolean isNenAwakened() {
        return nenAwakened;
    }

    // Awaken player's Nen (happens through storyline or special ritual)
    public void awakenNen() {
        this.nenAwakened = true;
        this.auraPoints = this.maxAuraPoints / 2;
    }

    // Get current aura points
    public int getAuraPoints() {
        return auraPoints;
    }

    // Set current aura points
    public void setAuraPoints(int auraPoints) {
        this.auraPoints = Math.min(auraPoints, maxAuraPoints);
    }

    // Add aura points
    public void addAuraPoints(int points) {
        this.auraPoints = Math.min(this.auraPoints + points, maxAuraPoints);
    }

    // Use aura points for abilities
    public boolean useAura(int points) {
        if (auraPoints >= points) {
            auraPoints -= points;
            return true;
        }
        return false;
    }

    // Get maximum aura points
    public int getMaxAuraPoints() {
        return maxAuraPoints;
    }

    // Set maximum aura points (increases with training)
    public void setMaxAuraPoints(int maxAuraPoints) {
        this.maxAuraPoints = maxAuraPoints;
    }

    // Get current Nen state
    public NenState getNenState() {
        return nenState;
    }

    // Set Nen state
    public void setNenState(NenState nenState) {
        this.nenState = nenState;
    }

    // Get all learned abilities
    public List<NenAbility> getAbilities() {
        return new ArrayList<>(abilities);
    }

    // Add a new ability
    public void addAbility(NenAbility ability) {
        if (!abilities.contains(ability)) {
            abilities.add(ability);
        }
    }

    // Get Nen mastery level
    public int getNenMasteryLevel() {
        return nenMasteryLevel;
    }

    // Increase Nen mastery level
    public void increaseNenMasteryLevel() {
        this.nenMasteryLevel++;
        // Increase max aura points when leveling up
        this.maxAuraPoints += 25;
    }

    // Toggle Gyo state
    public void toggleGyo() {
        this.gyoActive = !this.gyoActive;
    }

    // Check if Gyo is active
    public boolean isGyoActive() {
        return gyoActive;
    }

    // Toggle En state
    public void toggleEn() {
        this.enActive = !this.enActive;
    }

    // Check if En is active
    public boolean isEnActive() {
        return enActive;
    }

    // Get En radius
    public int getEnRadius() {
        return enRadius;
    }

    // Set En radius
    public void setEnRadius(int enRadius) {
        this.enRadius = enRadius;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        // Save basic Nen properties
        tag.putBoolean("NenAwakened", nenAwakened);
        tag.putInt("AuraPoints", auraPoints);
        tag.putInt("MaxAuraPoints", maxAuraPoints);
        tag.putInt("NenState", nenState.ordinal());
        tag.putInt("NenMasteryLevel", nenMasteryLevel);
        tag.putBoolean("GyoActive", gyoActive);
        tag.putBoolean("EnActive", enActive);
        tag.putInt("EnRadius", enRadius);

        // Save Nen type if determined
        if (nenType != null) {
            CompoundTag nenTypeTag = new CompoundTag();
            nenType.toNBT(nenTypeTag);
            tag.put("NenType", nenTypeTag);
        }

        // Save abilities
        ListTag abilitiesTag = new ListTag();
        for (NenAbility ability : abilities) {
            abilitiesTag.add(ability.serializeNBT());
        }
        tag.put("Abilities", abilitiesTag);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        // Load basic Nen properties
        nenAwakened = tag.getBoolean("NenAwakened");
        auraPoints = tag.getInt("AuraPoints");
        maxAuraPoints = tag.getInt("MaxAuraPoints");
        nenState = NenState.values()[tag.getInt("NenState")];
        nenMasteryLevel = tag.getInt("NenMasteryLevel");
        gyoActive = tag.getBoolean("GyoActive");
        enActive = tag.getBoolean("EnActive");
        enRadius = tag.getInt("EnRadius");

        // Load Nen type if saved
        if (tag.contains("NenType")) {
            nenType = NenType.fromNBT(tag.getCompound("NenType"));
        }

        // Load abilities
        abilities.clear();
        ListTag abilitiesTag = tag.getList("Abilities", Tag.TAG_COMPOUND);
        for (int i = 0; i < abilitiesTag.size(); i++) {
            CompoundTag abilityTag = abilitiesTag.getCompound(i);
            NenAbility ability = NenAbility.fromNBT(abilityTag);
            if (ability != null) {
                abilities.add(ability);
            }
        }
    }

    /**
     * Event handler class for the PlayerNen capability
     */
    @Mod.EventBusSubscriber(modid = HxHMod.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerNen.class);
        }

        @SubscribeEvent
        public static void attachCapability(AttachCapabilitiesEvent<Player> event) {
            if (!(event.getObject() instanceof Player)) return;

            Player player = (Player) event.getObject();
            event.addCapability(IDENTIFIER, new PlayerNenProvider());
        }

        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            // When player dies or changes dimension, copy Nen data
            event.getOriginal().getCapability(CAPABILITY).ifPresent(oldNen -> {
                event.getEntity().getCapability(CAPABILITY).ifPresent(newNen -> {
                    newNen.deserializeNBT(oldNen.serializeNBT());
                });
            });
        }
    }

    /**
     * Helper method to get PlayerNen capability from player
     */
    public static PlayerNen getNen(Player player) {
        return player.getCapability(CAPABILITY)
                .orElseThrow(() -> new IllegalStateException("Player does not have Nen capability!"));
    }
}