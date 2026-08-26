package com.ksandr707.carpet_shadow_legacy.mixins.tooltip;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientboundContainerSetSlotPacket.class)
public abstract class ScreenHandlerSlotUpdateS2CPacketMixin {
    @WrapOperation(method = "<init>(IIILnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"))
    public ItemStack copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        if (CarpetShadowLegacySettings.shadowItemTooltip) {
            return ShadowItem.copy_redirect(instance, original);
        }
        return original.call(instance);
    }
}