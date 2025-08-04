package com.ksandr707.carpet_shadow_legacy.mixins.tooltip;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InventoryS2CPacket.class)
public abstract class InventoryS2CPacketMixin {


    @WrapOperation(method = "<init>(IILnet/minecraft/util/collection/DefaultedList;Lnet/minecraft/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"))
    public ItemStack copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        if (CarpetShadowLegacySettings.shadowItemTooltip) {
            return ShadowItem.carpet_shadow$copy_redirect(instance, original);
        }
        return original.call(instance);
    }

}
