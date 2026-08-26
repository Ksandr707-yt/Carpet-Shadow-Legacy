package com.ksandr707.carpet_shadow_legacy.mixins.tooltip;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.world.item.ItemStack;

@Mixin(ClientboundContainerSetContentPacket.class)
public abstract class InventoryS2CPacketMixin {
    @Mutable
    @Shadow
    @Final
    private ItemStack carriedItem;

    @Inject(
            method = "<init>(IILjava/util/List;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("RETURN")
    )
    private void modifyAfterConstruction(
            int syncId,
            int revision,
            List<ItemStack> contents,
            ItemStack cursorStack,
            CallbackInfo ci
    ) {
        if (CarpetShadowLegacySettings.shadowItemTooltip) {
            Operation<ItemStack> op = ignored -> new ItemStack(cursorStack.getItem(),cursorStack.getCount());
            this.carriedItem = ShadowItem.copy_redirect(cursorStack, op);
        }
    }
}