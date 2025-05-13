package com.example.hxhmod.network.packet;

import com.example.hxhmod.capabilities.PlayerNen;
import com.example.hxhmod.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet sent from client to server to toggle Gyo state
 */
public class ToggleGyoPacket {
    /**
     * Default constructor (no parameters needed)
     */
    public ToggleGyoPacket() {
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
    public static ToggleGyoPacket decode(FriendlyByteBuf buf) {
        return new ToggleGyoPacket();
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

                // Check if player is in Zetsu state (cannot use Gyo)
                if (playerNen.getNenState() == PlayerNen.NenState.ZETSU) {
                    player.sendSystemMessage(Component.translatable("hxhmod.message.cannot_use_in_zetsu"));
                    return;
                }

                // Check if player has enough aura (minimum 5) to activate Gyo
                if (!playerNen.isGyoActive() && playerNen.getAuraPoints() < 5) {
                    player.sendSystemMessage(Component.translatable("hxhmod.message.not_enough_aura"));
                    return;
                }

                // Toggle Gyo state
                playerNen.toggleGyo();

                // If Gyo was activated, consume some aura
                if (playerNen.isGyoActive()) {
                    playerNen.useAura(5);
                }

                // Sync the change back to the client
                NetworkHandler.sendToPlayer(new SyncNenDataPacket(playerNen), player);

                // Send confirmation message
                player.sendSystemMessage(Component.translatable(
                        playerNen.isGyoActive() ? "hxhmod.message.gyo.activate" : "hxhmod.message.gyo.deactivate"));
            });
        });
        ctx.get().setPacketHandled(true);
    }
}