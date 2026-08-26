package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    @Inject(method = "tryMoveInItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"))
    private static void fix_transfer_start(Container from, Container to, ItemStack stack, int slot, Direction side, CallbackInfoReturnable<ItemStack> cir) {
        Globals.mergingThreads.add(Thread.currentThread());
    }
    @Inject(method = "tryMoveInItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;", at = @At("RETURN"))
    private static void fix_transfer_end(Container from, Container to, ItemStack stack, int slot, Direction side, CallbackInfoReturnable<ItemStack> cir) {
        Globals.mergingThreads.remove(Thread.currentThread());
    }
}