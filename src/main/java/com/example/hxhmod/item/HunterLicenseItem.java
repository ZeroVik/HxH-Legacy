package com.example.hxhmod.item;

import com.example.hxhmod.capabilities.PlayerNen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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

public class HunterLicenseItem extends Item {

    public HunterLicenseItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            // The Hunter License grants various privileges

            // Grant temporary status effects
            player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 12000, 0)); // 10 minutes
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0)); // 5 minutes

            // Check if player has awakened Nen
            PlayerNen playerNen = PlayerNen.getNen(player);
            if (!playerNen.isNenAwakened()) {
                player.sendSystemMessage(Component.translatable("hxhmod.message.hunter_license.no_nen")
                        .withStyle(ChatFormatting.GOLD));
            }

            // Set cooldown (can only use once every 30 minutes)
            player.getCooldowns().addCooldown(this, 36000);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        tooltip.add(Component.translatable("item.hxhmod.hunter_license.tooltip.1")
                .withStyle(Style.EMPTY.withColor(0xF5DEB3).withItalic(true)));
        tooltip.add(Component.translatable("item.hxhmod.hunter_license.tooltip.2")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.hxhmod.hunter_license.tooltip.3")
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        // Hunter License always has enchantment glint
        return true;
    }
}