package com.example.hxhmod.item;

import com.example.hxhmod.capabilities.PlayerNen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class NenAwakeningScrollItem extends Item {

    public NenAwakeningScrollItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            PlayerNen playerNen = PlayerNen.getNen(player);

            // Check if the player has already awakened their Nen
            if (playerNen.isNenAwakened()) {
                player.sendSystemMessage(Component.translatable("hxhmod.message.nen_awakening.already_awakened")
                        .withStyle(ChatFormatting.YELLOW));
                return InteractionResultHolder.fail(stack);
            }

            // Perform the Nen awakening ritual
            playerNen.awakenNen();

            // Add effects to signify the awakening
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1));
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0));

            // Play sound
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 0.5f);

            // Send messages
            player.sendSystemMessage(Component.translatable("hxhmod.message.nen_awakening.success")
                    .withStyle(ChatFormatting.GREEN));
            player.sendSystemMessage(Component.translatable("hxhmod.message.nen_awakening.instructions")
                    .withStyle(ChatFormatting.GOLD));

            // Consume the item
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            // Trigger advancement
            if (player instanceof ServerPlayer) {
                // triggerAdvancement((ServerPlayer) player, HxHAdvancementTriggers.NEN_AWAKENED);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        tooltip.add(Component.translatable("item.hxhmod.nen_awakening_scroll.tooltip.1")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.hxhmod.nen_awakening_scroll.tooltip.2")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.hxhmod.nen_awakening_scroll.tooltip.3")
                .withStyle(ChatFormatting.RED));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        // Always show enchantment glint
        return true;
    }
}