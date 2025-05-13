package com.example.hxhmod.network;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.network.packet.ChangeNenStatePacket;
import com.example.hxhmod.network.packet.SyncNenDataPacket;
import com.example.hxhmod.network.packet.ToggleEnPacket;
import com.example.hxhmod.network.packet.ToggleGyoPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

/**
 * Network handler for the mod's packet system
 */
public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HxHMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    /**
     * Register all network messages
     */
    public static void registerMessages() {
        // Server -> Client packets
        INSTANCE.registerMessage(id++, SyncNenDataPacket.class,
                SyncNenDataPacket::encode,
                SyncNenDataPacket::decode,
                SyncNenDataPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        // Client -> Server packets
        INSTANCE.registerMessage(id++, ChangeNenStatePacket.class,
                ChangeNenStatePacket::encode,
                ChangeNenStatePacket::decode,
                ChangeNenStatePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        INSTANCE.registerMessage(id++, ToggleGyoPacket.class,
                ToggleGyoPacket::encode,
                ToggleGyoPacket::decode,
                ToggleGyoPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        INSTANCE.registerMessage(id++, ToggleEnPacket.class,
                ToggleEnPacket::encode,
                ToggleEnPacket::decode,
                ToggleEnPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        // Register more packets as needed
    }

    /**
     * Send a packet to the server
     */
    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    /**
     * Send a packet to a specific player
     */
    public static void sendToPlayer(Object packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    /**
     * Send a packet to all players
     */
    public static void sendToAll(Object packet) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
    }

    /**
     * Send a packet to all players near a point
     */
    public static void sendToAllAround(Object packet, PacketDistributor.TargetPoint point) {
        INSTANCE.send(PacketDistributor.NEAR.with(() -> point), packet);
    }
}