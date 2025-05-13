package com.example.hxhmod.client.keybinding;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.capabilities.PlayerNen;
import com.example.hxhmod.network.NetworkHandler;
import com.example.hxhmod.network.packet.ChangeNenStatePacket;
import com.example.hxhmod.network.packet.ToggleEnPacket;
import com.example.hxhmod.network.packet.ToggleGyoPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = HxHMod.MOD_ID, value = Dist.CLIENT)
public class KeyBindings {
    // Define key bindings
    public static final KeyMapping KEY_NEN_TOGGLE = new KeyMapping(
            "key.hxhmod.nen_toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "key.categories.hxhmod"
    );

    public static final KeyMapping KEY_NEN_STATE_TEN = new KeyMapping(
            "key.hxhmod.nen_state_ten",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "key.categories.hxhmod"
    );

    public static final KeyMapping KEY_NEN_STATE_REN = new KeyMapping(
            "key.hxhmod.nen_state_ren",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.categories.hxhmod"
    );

    public static final KeyMapping KEY_NEN_STATE_ZETSU = new KeyMapping(
            "key.hxhmod.nen_state_zetsu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.categories.hxhmod"
    );

    public static final KeyMapping KEY_NEN_ABILITY = new KeyMapping(
            "key.hxhmod.nen_ability",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.categories.hxhmod"
    );

    public static final KeyMapping KEY_TOGGLE_GYO = new KeyMapping(
            "key.hxhmod.toggle_gyo",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.hxhmod"
    );

    public static final KeyMapping KEY_TOGGLE_EN = new KeyMapping(
            "key.hxhmod.toggle_en",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.hxhmod"
    );

    /**
     * Register key bindings
     */
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KEY_NEN_TOGGLE);
        event.register(KEY_NEN_STATE_TEN);
        event.register(KEY_NEN_STATE_REN);
        event.register(KEY_NEN_STATE_ZETSU);
        event.register(KEY_NEN_ABILITY);
        event.register(KEY_TOGGLE_GYO);
        event.register(KEY_TOGGLE_EN);
    }

    /**
     * Handle key inputs
     */
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        // Get player's Nen capability
        minecraft.player.getCapability(PlayerNen.CAPABILITY).ifPresent(playerNen -> {
            // Only process keys if Nen is awakened
            if (!playerNen.isNenAwakened()) return;

            // Check which key was pressed
            if (KEY_NEN_TOGGLE.consumeClick()) {
                // Toggle between normal state and last used state
                if (playerNen.getNenState() == PlayerNen.NenState.NORMAL) {
                    // Set to TEN state by default
                    NetworkHandler.sendToServer(new ChangeNenStatePacket(PlayerNen.NenState.TEN));
                } else {
                    // Return to normal state
                    NetworkHandler.sendToServer(new ChangeNenStatePacket(PlayerNen.NenState.NORMAL));
                }
            }

            // Handle TEN state key
            if (KEY_NEN_STATE_TEN.consumeClick()) {
                NetworkHandler.sendToServer(new ChangeNenStatePacket(PlayerNen.NenState.TEN));
                minecraft.player.displayClientMessage(
                        Component.translatable("hxhmod.message.nen_state.ten"), true);
            }

            // Handle REN state key
            if (KEY_NEN_STATE_REN.consumeClick()) {
                NetworkHandler.sendToServer(new ChangeNenStatePacket(PlayerNen.NenState.REN));
                minecraft.player.displayClientMessage(
                        Component.translatable("hxhmod.message.nen_state.ren"), true);
            }

            // Handle ZETSU state key
            if (KEY_NEN_STATE_ZETSU.consumeClick()) {
                NetworkHandler.sendToServer(new ChangeNenStatePacket(PlayerNen.NenState.ZETSU));
                minecraft.player.displayClientMessage(
                        Component.translatable("hxhmod.message.nen_state.zetsu"), true);
            }

            // Handle ability key
            if (KEY_NEN_ABILITY.consumeClick()) {
                // This would open an ability selection GUI or activate the currently selected ability
                // Implementation would depend on how abilities are selected and managed
                minecraft.player.displayClientMessage(
                        Component.translatable("hxhmod.message.nen_ability.activate"), true);
            }

            // Handle Gyo toggle
            if (KEY_TOGGLE_GYO.consumeClick()) {
                NetworkHandler.sendToServer(new ToggleGyoPacket());
                minecraft.player.displayClientMessage(
                        Component.translatable(playerNen.isGyoActive() ?
                                "hxhmod.message.gyo.deactivate" : "hxhmod.message.gyo.activate"),
                        true);
            }

            // Handle En toggle
            if (KEY_TOGGLE_EN.consumeClick()) {
                NetworkHandler.sendToServer(new ToggleEnPacket());
                minecraft.player.displayClientMessage(
                        Component.translatable(playerNen.isEnActive() ?
                                "hxhmod.message.en.deactivate" : "hxhmod.message.en.activate"),
                        true);
            }
        });
    }
}