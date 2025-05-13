package com.example.hxhmod.event;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.capabilities.PlayerNen;
import com.example.hxhmod.config.HxHModConfig;
import com.example.hxhmod.network.NetworkHandler;
import com.example.hxhmod.network.packet.SyncNenDataPacket;
import com.example.hxhmod.particle.NenAuraParticle;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handler for player-related Nen events and updates
 */
@Mod.EventBusSubscriber(modid = HxHMod.MOD_ID)
public class PlayerNenHandler {
    // Track aura regeneration ticks for each player
    private static final Map<UUID, Integer> auraRegenCounters = new HashMap<>();

    // Track En pulse ticks for each player
    private static final Map<UUID, Integer> enPulseCounters = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Only process on server side and at the end of the tick
        if (event.phase != TickEvent.Phase.END || event.player.level.isClientSide) return;

        Player player = event.player;
        UUID playerUUID = player.getUUID();

        player.getCapability(PlayerNen.CAPABILITY).ifPresent(playerNen -> {
            // Skip processing if Nen is not awakened
            if (!playerNen.isNenAwakened()) return;

            // Initialize counters if needed
            if (!auraRegenCounters.containsKey(playerUUID)) {
                auraRegenCounters.put(playerUUID, 0);
            }
            if (!enPulseCounters.containsKey(playerUUID)) {
                enPulseCounters.put(playerUUID, 0);
            }

            // Process aura regeneration
            int regenCounter = auraRegenCounters.get(playerUUID);
            int regenInterval = HxHModConfig.COMMON.auraRegenInterval.get();

            regenCounter++;
            if (regenCounter >= regenInterval) {
                // Calculate regen amount based on Nen state
                int regenAmount = HxHModConfig.COMMON.auraRegenRate.get();

                switch (playerNen.getNenState()) {
                    case ZETSU:
                        // Zetsu increases aura regen rate
                        regenAmount *= 3;
                        break;
                    case TEN:
                        // Ten slightly increases aura regen rate
                        regenAmount *= 1.5;
                        break;
                    case REN:
                        // Ren actually drains aura instead of regenerating it
                        regenAmount = -2;
                        break;
                    case HATSU:
                        // Active abilities drain aura
                        regenAmount = -1;
                        break;
                    default:
                        // Normal state has standard regen
                        break;
                }

                // Apply regeneration or drain
                if (regenAmount > 0) {
                    playerNen.addAuraPoints(regenAmount);
                } else {
                    playerNen.useAura(-regenAmount);

                    // If player runs out of aura in REN or HATSU, revert to normal state
                    if (playerNen.getAuraPoints() <= 0 &&
                            (playerNen.getNenState() == PlayerNen.NenState.REN ||
                                    playerNen.getNenState() == PlayerNen.NenState.HATSU)) {
                        playerNen.setNenState(PlayerNen.NenState.NORMAL);
                    }
                }

                // Reset counter
                regenCounter = 0;
            }
            auraRegenCounters.put(playerUUID, regenCounter);

            // Apply effects based on Nen state
            switch (playerNen.getNenState()) {
                case TEN:
                    // TEN provides minor defense boost
                    if (!player.hasEffect(MobEffects.DAMAGE_RESISTANCE) ||
                            player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() < 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 0, false, false, true));
                    }
                    break;
                case REN:
                    // REN provides strength boost
                    if (!player.hasEffect(MobEffects.DAMAGE_BOOST) ||
                            player.getEffect(MobEffects.DAMAGE_BOOST).getAmplifier() < 1) {
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 1, false, false, true));
                    }
                    break;
                case ZETSU:
                    // ZETSU makes player more vulnerable
                    if (!player.hasEffect(MobEffects.WEAKNESS) ||
                            player.getEffect(MobEffects.WEAKNESS).getAmplifier() < 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0, false, false, true));
                    }
                    break;
                default:
                    // No special effects for NORMAL state
                    break;
            }

            // Process En if active
            if (playerNen.isEnActive()) {
                int enCounter = enPulseCounters.get(playerUUID);

                // En constantly drains aura
                if (enCounter % 20 == 0) { // Once per second
                    // Drain more aura for larger En radius
                    int enCost = 1 + (playerNen.getEnRadius() / 5);

                    if (!playerNen.useAura(enCost)) {
                        // Not enough aura to maintain En
                        playerNen.toggleEn();
                    }
                }

                // En "pulse" effect every 2 seconds
                if (enCounter >= 40) {
                    // This would detect entities within the En radius
                    // Implementation would depend on how entity detection is handled

                    enCounter = 0;
                }

                enPulseCounters.put(playerUUID, enCounter + 1);
            }

            // Sync Nen data to client if needed (every second or when state changes)
            if (player instanceof ServerPlayer && player.tickCount % 20 == 0) {
                NetworkHandler.sendToPlayer(new SyncNenDataPacket(playerNen), (ServerPlayer) player);
            }
        });
    }
}