package com.example.hxhmod.network.packet;

import com.example.hxhmod.capabilities.PlayerNen;
import com.example.hxhmod.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet sent from client to server to change player's Nen state
 */
public class ChangeNenStatePacket {
    // The Nen state to change to
    private final PlayerNen.NenState nenState;

    /**
     * Constructor for creating the packet
     */
    public ChangeNenStatePacket(PlayerNen.NenState nenState) {
        this.nenState = nenState;
    }

    /**
     * Encode packet data to buffer
     */
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(nenState.ordinal());
    }

    /**
     * Decode packet data from buffer
     */
    public static ChangeNenStatePacket decode(FriendlyByteBuf buf) {
        PlayerNen.NenState state = PlayerNen.NenState.values()[buf.readInt()];
        return new ChangeNenStatePacket(state);
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

                // Check if changing to REN and player has enough aura (minimum 10)
                if (nenState == PlayerNen.NenState.REN && playerNen.getAuraPoints() < 10) {
                    player.sendSystemMessage(Component.translatable("hxhmod.message.not_enough_aura"));
                    return;
                }

                // Change the state
                playerNen.setNenState(nenState);

                // Sync the change back to the client
                NetworkHandler.sendToPlayer(new SyncNenDataPacket(playerNen), player);

                // Display appropriate message based on the new state
                String key = switch (nenState) {
                    case TEN -> "hxhmod.message.nen_state.ten";
                    case REN -> "hxhmod.message.nen_state.ren";
                    case ZETSU -> "hxhmod.message.nen_state.zetsu";
                    case HATSU -> "hxhmod.message.nen_ability.activate";
                    default -> ""; // No message for normal state
                };

                if (!key.isEmpty()) {
                    player.sendSystemMessage(Component.translatable(key));
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}