package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates;

import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow public abstract ItemStack getItem();

    @Shadow @Final public Container container;

    @Shadow public abstract int getContainerSlot();

    @Inject(method = "setByPlayer",
            at = @At(value = "HEAD"))
    public void remember_inventory(ItemStack next, CallbackInfo ci) {
            ItemStack curr = getItem();
            if(((ShadowItem)(Object)curr).isItShadowItem()){
                var shadowId = ((ShadowItem)(Object)curr).getShadowId();
                Globals.removeInventory(shadowId, this.container, getContainerSlot());
            }
            if(((ShadowItem)(Object)next).isItShadowItem()){
                var shadowId = ((ShadowItem)(Object)next).getShadowId();
                Globals.addInventory(shadowId, this.container, getContainerSlot());
            }
    }
}