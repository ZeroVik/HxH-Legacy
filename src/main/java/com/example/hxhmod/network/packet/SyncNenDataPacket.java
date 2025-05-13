package com.example.hxhmod.network.packet;

import com.example.hxhmod.capabilities.PlayerNen;
import com.example.hxhmod.nen.NenType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet to sync Nen data from server to client
 */
public class SyncNenDataPacket {
    // Nen awakened status
    private final boolean nenAwakened;

    // Current and max aura points
    private final int auraPoints;
    private final int maxAuraPoints;

    // Current Nen state
    private final int nenState;

    // Nen type (can be null)
    private final int nenType;

    // Mastery level
    private final int nenMasteryLevel;

    // Gyo and En status
    private final boolean gyoActive;
    private final boolean enActive;
    private final int enRadius;

    /**
     * Constructor for creating the packet from player Nen data
     */
    public SyncNenDataPacket(PlayerNen playerNen) {
        this.nenAwakened = playerNen.isNenAwakened();
        this.auraPoints = playerNen.getAuraPoints();
        this.maxAuraPoints = playerNen.getMaxAuraPoints();
        this.nenState = playerNen.getNenState().ordinal();
        this.nenType = playerNen.getNenType() != null ? playerNen.getNenType().ordinal() : -1;
        this.nenMasteryLevel = playerNen.getNenMasteryLevel();
        this.gyoActive = playerNen.isGyoActive();
        this.enActive = playerNen.isEnActive();
        this.enRadius = playerNen.getEnRadius();
    }

    /**
     * Constructor for decoding the packet
     */
    public SyncNenDataPacket(boolean nenAwakened, int auraPoints, int maxAuraPoints, int nenState,
                             int nenType, int nenMasteryLevel, boolean gyoActive, boolean enActive, int enRadius) {
        this.nenAwakened = nenAwakened;
        this.auraPoints = auraPoints;
        this.maxAuraPoints = maxAuraPoints;
        this.nenState = nenState;
        this.nenType = nenType;
        this.nenMasteryLevel = nenMasteryLevel;
        this.gyoActive = gyoActive;
        this.enActive = enActive;
        this.enRadius = enRadius;
    }

    /**
     * Encode packet data to buffer
     */
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(nenAwakened);
        buf.writeInt(auraPoints);
        buf.writeInt(maxAuraPoints);
        buf.writeInt(nenState);
        buf.writeInt(nenType);
        buf.writeInt(nenMasteryLevel);
        buf.writeBoolean(gyoActive);
        buf.writeBoolean(enActive);
        buf.writeInt(enRadius);
    }

    /**
     * Decode packet data from buffer
     */
    public static SyncNenDataPacket decode(FriendlyByteBuf buf) {
        boolean nenAwakened = buf.readBoolean();
        int auraPoints = buf.readInt();
        int maxAuraPoints = buf.readInt();
        int nenState = buf.readInt();
        int nenType = buf.readInt();
        int nenMasteryLevel = buf.readInt();
        boolean gyoActive = buf.readBoolean();
        boolean enActive = buf.readBoolean();
        int enRadius = buf.readInt();

        return new SyncNenDataPacket(nenAwakened, auraPoints, maxAuraPoints, nenState,
                nenType, nenMasteryLevel, gyoActive, enActive, enRadius);
    }

    /**
     * Handle the packet on the receiving side
     */
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Make sure we're on the client
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleOnClient());
        });
        ctx.get().setPacketHandled(true);
    }

    /**
     * Client-side handler
     */
    private void handleOnClient() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        player.getCapability(PlayerNen.CAPABILITY).ifPresent(playerNen -> {
            // Update Nen awakened status
            if (nenAwakened && !playerNen.isNenAwakened()) {
                playerNen.awakenNen();
            }

            // Update aura points
            playerNen.setAuraPoints(auraPoints);
            playerNen.setMaxAuraPoints(maxAuraPoints);

            // Update Nen state
            playerNen.setNenState(PlayerNen.NenState.values()[nenState]);

            // Update Nen type
            if (nenType >= 0 && nenType < NenType.values().length) {
                playerNen.setNenType(NenType.values()[nenType]);
            }

            // Update mastery level
            while (playerNen.getNenMasteryLevel() < nenMasteryLevel) {
                playerNen.increaseNenMasteryLevel();
            }

            // Update Gyo and En
            if (gyoActive != playerNen.isGyoActive()) {
                playerNen.toggleGyo();
            }

            if (enActive != playerNen.isEnActive()) {
                playerNen.toggleEn();
            }

            playerNen.setEnRadius(enRadius);
        });
    }
}