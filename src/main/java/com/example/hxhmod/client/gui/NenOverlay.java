package com.example.hxhmod.client.gui;

import com.example.hxhmod.HxHMod;
import com.example.hxhmod.capabilities.PlayerNen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

/**
 * GUI overlay to display the player's Nen information
 */
public class NenOverlay extends GuiComponent implements IGuiOverlay {
    // Texture location for the overlay
    private static final ResourceLocation OVERLAY_TEXTURE =
            HxHMod.location("textures/gui/nen_overlay.png");

    // Texture dimensions
    private static final int TEXTURE_WIDTH = 128;
    private static final int TEXTURE_HEIGHT = 128;

    // Bar dimensions
    private static final int BAR_WIDTH = 82;
    private static final int BAR_HEIGHT = 8;

    // State icon dimensions
    private static final int ICON_SIZE = 16;

    @Override
    public void render(PoseStack poseStack, Minecraft minecraft, int screenWidth, int screenHeight, float partialTick) {
        Player player = minecraft.player;
        if (player == null) return;

        // Get player's Nen capability
        player.getCapability(PlayerNen.CAPABILITY).ifPresent(playerNen -> {
            // Only render if player has awakened Nen
            if (!playerNen.isNenAwakened()) return;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, OVERLAY_TEXTURE);

            // Calculate positions
            int left = 5;
            int top = screenHeight - 25;

            // Draw background
            blit(poseStack, left, top, 0, 0, BAR_WIDTH, BAR_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

            // Calculate aura bar width
            int auraWidth = (int) ((float) playerNen.getAuraPoints() / playerNen.getMaxAuraPoints() * BAR_WIDTH);

            // Draw aura bar
            blit(poseStack, left, top, 0, BAR_HEIGHT, auraWidth, BAR_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);

            // Draw Nen state icon
            int stateIconX = left + BAR_WIDTH + 5;
            int stateIconY = top - 4;
            int iconV = 16 + (playerNen.getNenState().ordinal() * ICON_SIZE);
            blit(poseStack, stateIconX, stateIconY, 0, iconV, ICON_SIZE, ICON_SIZE, TEXTURE_WIDTH, TEXTURE_HEIGHT);

            // Draw aura points text
            String auraText = playerNen.getAuraPoints() + "/" + playerNen.getMaxAuraPoints();
            minecraft.font.draw(poseStack, auraText, left + (BAR_WIDTH / 2) - (minecraft.font.width(auraText) / 2),
                    top - 10, 0xFFFFFF);

            // Draw Nen type indicator if determined
            if (playerNen.getNenType() != null) {
                int typeIconX = stateIconX + ICON_SIZE + 5;
                int typeIconY = stateIconY;
                int typeIconV = 80 + (playerNen.getNenType().ordinal() * ICON_SIZE);
                blit(poseStack, typeIconX, typeIconY, 0, typeIconV, ICON_SIZE, ICON_SIZE, TEXTURE_WIDTH, TEXTURE_HEIGHT);
            }

            // Draw active ability indicators
            int abilityX = left;
            int abilityY = top - 25;

            // This would iterate through active abilities and draw their icons
            // Implementation would depend on how ability activation is tracked

            // Draw En radius indicator if active
            if (playerNen.isEnActive()) {
                String enText = "En: " + playerNen.getEnRadius() + "m";
                minecraft.font.draw(poseStack, enText, left, top - 40, 0xAAAAAA);
            }

            // Draw Gyo indicator if active
            if (playerNen.isGyoActive()) {
                minecraft.font.draw(poseStack, "Gyo", left + 50, top - 40, 0xAAAAAA);
            }
        });
    }
}