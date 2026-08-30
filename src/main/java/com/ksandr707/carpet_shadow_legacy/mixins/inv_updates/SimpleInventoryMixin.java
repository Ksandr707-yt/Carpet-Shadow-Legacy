package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates;

import com.ksandr707.carpet_shadow_legacy.interfaces.InventoryItem;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleInventory.class)
public abstract class SimpleInventoryMixin {

    @Shadow
    public abstract ItemStack getStack(int slot);

    @Inject(method = "removeStack(I)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
    public void track_remove(int slot, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack curr = getStack(slot);
        if (((ShadowItem) (Object) curr).getShadowId() != null) {
            ((InventoryItem) (Object) curr).removeSlot((Inventory) this, slot);
        }
    }

    @Inject(method = "setStack", at = @At("HEAD"))
    public void track_set(int slot, ItemStack next, CallbackInfo ci) {
        ItemStack curr = getStack(slot);
        if (((ShadowItem) (Object) curr).getShadowId() != null) {
            ((InventoryItem) (Object) curr).removeSlot((Inventory) this, slot);
        }
        if (((ShadowItem) (Object) next).getShadowId() != null) {
            ((InventoryItem) (Object) next).addSlot((Inventory) this, slot);
        }
    }
}