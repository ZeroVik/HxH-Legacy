package com.example.hxhmod.network.packet;

import com.example.hxhmod.capabilities.PlayerNen;
import com.example.hxhmod.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet sent from client to server to toggle En state
 */
public class ToggleEnPacket {
    /**
     * Default constructor (no parameters needed)
     */
    public ToggleEnPacket() {
        // Empty constructor - no data needs to be sent
    }

    /**
     * Encode packet data to buffer (no data needed)
     */
    public void encode(FriendlyByteBuf buf) {
        // Nothing to encode
    }

    /**
     * Decode packet data from buffer (no data needed)
     */
    public static ToggleEnPacket decode(FriendlyByteBuf buf) {
        return new ToggleEnPacket();
    }

    /**
     * Handle the packet on the receiving side (server)
     */
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Make sure we're on the server
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            player.getCapability(PlayerNen.CAPABILITY).ifPresent(playerNen -> {
                // Verify the player has awakened Nen
                if (!playerNen.isNenAwakened()) {
                    player.sendSystemMessage(Component.translatable("hxhmod.message.nen_not_awakened"));
                    return;
                }

                // Check if player is in Zetsu state (cannot use En)
                if (playerNen.getNenState() == PlayerNen.NenState.ZETSU) {
                    player.sendSystemMessage(Component.translatable("hxhmod.message.cannot_use_in_zetsu"));
                    return;
                }

                // En initial cost depends on radius - 10 aura points per 10 blocks
                int enCost = 10 + (playerNen.getEnRadius() / 10) * 10;

                // Check if player has enough aura to activate En
                if (!playerNen.isEnActive() && playerNen.getAuraPoints() < enCost) {
                    player.sendSystemMessage(Component.translatable("hxhmod.message.not_enough_aura"));
                    return;
                }

                // Toggle En state
                playerNen.toggleEn();

                // If En was activated, consume some aura for initial activation
                if (playerNen.isEnActive()) {
                    playerNen.useAura(enCost);
                }

                // Sync the change back to the client
                NetworkHandler.sendToPlayer(new SyncNenDataPacket(playerNen), player);

                // Send confirmation message
                player.sendSystemMessage(Component.translatable(
                        playerNen.isEnActive() ? "hxhmod.message.en.activate" : "hxhmod.message.en.deactivate"));

                // If En is active, send a message with the current radius
                if (playerNen.isEnActive()) {
                    player.sendSystemMessage(Component.translatable("hxhmod.message.en.radius", playerNen.getEnRadius()));
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}