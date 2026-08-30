package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates;

import com.ksandr707.carpet_shadow_legacy.interfaces.InventoryItem;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow public abstract ItemStack getStack();

    @Shadow @Final public Inventory inventory;

    @Shadow public abstract int getIndex();

    @Inject(method = "setStack(Lnet/minecraft/item/ItemStack;)V",
            at = @At(value = "HEAD"))
    public void remember_inventory(ItemStack next, CallbackInfo ci) {
        ItemStack curr = getStack();
        if(((ShadowItem)(Object)curr).getShadowId() != null){
            ((InventoryItem)(Object)curr).removeSlot(this.inventory, getIndex());
        }
        if(((ShadowItem)(Object)next).getShadowId() != null){
            ((InventoryItem)(Object)next).addSlot(this.inventory, getIndex());
        }
    }
}