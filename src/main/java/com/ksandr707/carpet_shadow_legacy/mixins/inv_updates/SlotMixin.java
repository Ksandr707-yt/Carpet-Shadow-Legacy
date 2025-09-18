package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates;

import com.ksandr707.carpet_shadow_legacy.Globals;
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

    @Inject(method = "setStack",
            at = @At(value = "HEAD"))
    public void remember_inventory(ItemStack next, CallbackInfo ci) {
            ItemStack curr = getStack();
            if(((ShadowItem)(Object)curr).isItShadowItem()){
                var shadowId = ((ShadowItem)(Object)curr).getShadowId();
                Globals.removeInventory(shadowId, this.inventory, getIndex());
            }
            if(((ShadowItem)(Object)next).isItShadowItem()){
                var shadowId = ((ShadowItem)(Object)next).getShadowId();
                Globals.addInventory(shadowId, this.inventory, getIndex());
            }
    }
}