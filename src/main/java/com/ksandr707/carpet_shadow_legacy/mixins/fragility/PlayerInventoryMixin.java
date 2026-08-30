package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ItemEntitySlot;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow
    public abstract void setStack(int slot, ItemStack stack);

    @WrapOperation(method = "insertStack(ILnet/minecraft/item/ItemStack;)Z", at=@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setCount(I)V"), slice = @Slice(from = @At(value = "INVOKE",target = "Lnet/minecraft/entity/player/PlayerEntity;isInCreativeMode()Z")))
    private void modify_count(ItemStack instance, int count, Operation<Void> original){
        if(count==0 && CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) instance).getShadowId() != null){
            ItemEntity entity = ((ItemEntitySlot) (Object) instance).getEntity();
            if (entity != null)
                entity.discard();
            else
                instance.setCount(0);
        }else{
            original.call(instance, count);
        }
    }

    @Inject(method = "addStack(ILnet/minecraft/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setStack(ILnet/minecraft/item/ItemStack;)V"), cancellable = true)
    public void add_shadow_item(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) stack).isShadowItem()) {
            this.setStack(slot, stack);
            ItemEntity entity = ((ItemEntitySlot) (Object) stack).getEntity();
            if (entity != null) {
                entity.discard();
            }
            cir.setReturnValue(-1);
        }
    }
}