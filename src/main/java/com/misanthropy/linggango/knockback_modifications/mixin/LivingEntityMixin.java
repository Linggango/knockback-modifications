package com.misanthropy.linggango.knockback_modifications.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Redirect(
            method = "knockback(DDD)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D"
            )
    )
    private double redirectKnockbackResistance(LivingEntity entity, Attribute attribute) {
        double resistance = entity.getAttributeValue(attribute);
        if (attribute == Attributes.KNOCKBACK_RESISTANCE && resistance >= 1.0) {
            if (!(entity instanceof Player)) {
                LivingEntity attacker = entity.getLastHurtByMob();
                if (attacker instanceof Player player && entity.tickCount == entity.getLastHurtByMobTimestamp()) {
                    int enchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, player);
                    double attr = player.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
                    double extra = enchant + attr;
                    if (extra > 0.0) {
                        double bleedThrough = Math.min(0.25, 0.05 * extra);
                        return 1.0 - bleedThrough;
                    }
                }
            }
        }
        return resistance;
    }
}