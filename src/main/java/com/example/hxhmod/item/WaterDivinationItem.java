package com.example.hxhmod.item;

import com.example.hxhmod.capabilities.PlayerNen;
import com.example.hxhmod.nen.NenType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class WaterDivinationItem extends Item {

    public WaterDivinationItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            PlayerNen playerNen = PlayerNen.getNen(player);

            // Check if player has already determined their Nen type
            if (playerNen.getNenType() != null) {
                player.sendSystemMessage(Component.translatable("hxhmod.message.water_divination.already_done")
                        .withStyle(ChatFormatting.YELLOW));
                return InteractionResultHolder.fail(stack);
            }

            // Check if player has awakened Nen
            if (!playerNen.isNenAwakened()) {
                player.sendSystemMessage(Component.translatable("hxhmod.message.water_divination.no_nen")
                        .withStyle(ChatFormatting.RED));
                return InteractionResultHolder.fail(stack);
            }

            player.sendSystemMessage(Component.translatable("hxhmod.message.water_divination.need_water")
                    .withStyle(ChatFormatting.BLUE));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.PASS;

        BlockState blockState = level.getBlockState(pos);

        // Check if the block is water
        if (blockState.is(Blocks.WATER)) {
            if (!level.isClientSide) {
                PlayerNen playerNen = PlayerNen.getNen(player);

                // Check if player has already determined their Nen type
                if (playerNen.getNenType() != null) {
                    player.sendSystemMessage(Component.translatable("hxhmod.message.water_divination.already_done")
                            .withStyle(ChatFormatting.YELLOW));
                    return InteractionResult.FAIL;
                }

                // Check if player has awakened Nen
                if (!playerNen.isNenAwakened()) {
                    player.sendSystemMessage(Component.translatable("hxhmod.message.water_divination.no_nen")
                            .withStyle(ChatFormatting.RED));
                    return InteractionResult.FAIL;
                }

                // Determine Nen type
                NenType type = determineNenType(player);
                playerNen.setNenType(type);

                // Send message about result
                player.sendSystemMessage(Component.translatable("hxhmod.message.water_divination.result")
                        .withStyle(ChatFormatting.GREEN));
                player.sendSystemMessage(Component.translatable("hxhmod.message.water_divination." + type.getName())
                        .withStyle(ChatFormatting.GOLD));

                // Play sound effect
                level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0f, 1.0f);

                // Spawn particles around the water
                // Particle implementation would be in another class
                // particleHandler.spawnWaterDivinationParticles(level, pos, type);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    /**
     * Determine the player's Nen type based on various factors
     */
    private NenType determineNenType(Player player) {
        // In a full implementation, this could use player stats, achievements, or playstyle
        // For now, we'll use a weighted random choice

        Random random = new Random(player.getUUID().getLeastSignificantBits());
        int roll = random.nextInt(100);

        // Distribution roughly follows the rarity in the HxH universe
        if (roll < 30) {
            return NenType.ENHANCER;     // 30%
        } else if (roll < 50) {
            return NenType.TRANSMUTER;   // 20%
        } else if (roll < 70) {
            return NenType.EMITTER;      // 20%
        } else if (roll < 85) {
            return NenType.CONJURER;     // 15%
        } else if (roll < 95) {
            return NenType.MANIPULATOR;  // 10%
        } else {
            return NenType.SPECIALIST;   // 5%
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        tooltip.add(Component.translatable("item.hxhmod.water_divination.tooltip.1")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.hxhmod.water_divination.tooltip.2")
                .withStyle(ChatFormatting.GRAY));
    }
}