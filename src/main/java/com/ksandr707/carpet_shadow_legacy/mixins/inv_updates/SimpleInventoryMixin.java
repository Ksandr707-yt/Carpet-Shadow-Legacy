package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates;

import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleContainer.class)
public abstract class SimpleInventoryMixin {

    @Shadow
    public abstract ItemStack getItem(int slot);

    @Inject(method = "removeItemNoUpdate(I)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"))
    public void track_remove(int slot, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack curr = getItem(slot);
        if (((ShadowItem) (Object) curr).isItShadowItem()) {
            var shadowId = ((ShadowItem)(Object)curr).getShadowId();
            Globals.removeInventory(shadowId, this, slot);
        }
    }

    @Inject(method = "setItem", at = @At("HEAD"))
    public void track_set(int slot, ItemStack next, CallbackInfo ci) {
        ItemStack curr = getItem(slot);
        if (((ShadowItem) (Object) curr).isItShadowItem()) {
            var shadowId = ((ShadowItem)(Object)curr).getShadowId();
            Globals.removeInventory(shadowId, this, slot);
        }
        if (((ShadowItem) (Object) next).isItShadowItem()) {
            var shadowId = ((ShadowItem)(Object)next).getShadowId();
            Globals.addInventory(shadowId, this, slot);
        }
    }
}