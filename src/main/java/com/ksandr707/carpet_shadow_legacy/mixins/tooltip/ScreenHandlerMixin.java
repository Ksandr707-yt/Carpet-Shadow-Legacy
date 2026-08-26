package com.ksandr707.carpet_shadow_legacy.mixins.tooltip;

import com.google.common.base.Suppliers;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

@Mixin(AbstractContainerMenu.class)
public abstract class ScreenHandlerMixin {

    @WrapOperation(method = "sendAllDataToRemote", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;", ordinal = 0))
    public ItemStack copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        return ShadowItem.copy_redirect(instance, original);
    }

    @WrapOperation(method = "broadcastChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;triggerSlotListeners(ILnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;)V", ordinal = 0))
    public void updateTrackedSlot_redirect(AbstractContainerMenu instance, int slot, ItemStack stack, Supplier<ItemStack> copySupplier, Operation<Void> original) {
        Supplier<ItemStack> new_supplier = Suppliers.memoize(() -> ShadowItem.copy_supplier(stack, copySupplier.get()));
        original.call(instance, slot, stack, new_supplier);
    }

    @WrapOperation(method = "broadcastChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;synchronizeSlotToRemote(ILnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;)V", ordinal = 0))
    public void checkSlotUpdates_redirect(AbstractContainerMenu instance, int slot, ItemStack stack, Supplier<ItemStack> copySupplier, Operation<Void> original) {
        Supplier<ItemStack> new_supplier = Suppliers.memoize(() -> ShadowItem.copy_supplier(stack, copySupplier.get()));
        original.call(instance, slot, stack, new_supplier);
    }

    @WrapOperation(method = "broadcastFullState", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;triggerSlotListeners(ILnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;)V", ordinal = 0))
    public void updateTrackedSlot_redirect2(AbstractContainerMenu instance, int slot, ItemStack stack, Supplier<ItemStack> copySupplier, Operation<Void> original) {
        Supplier<ItemStack> new_supplier = Suppliers.memoize(() -> ShadowItem.copy_supplier(stack, copySupplier.get()));
        original.call(instance, slot, stack, new_supplier);
    }

}