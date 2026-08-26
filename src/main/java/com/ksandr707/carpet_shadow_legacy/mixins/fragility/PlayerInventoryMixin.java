package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ItemEntitySlot;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow
    public abstract void setItem(int slot, ItemStack stack);

    @WrapOperation(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at=@At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setCount(I)V"), slice = @Slice(from = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z")))
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

    @Inject(method = "addResource(ILnet/minecraft/world/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setItem(ILnet/minecraft/world/item/ItemStack;)V"), cancellable = true)
    public void add_shadow_item(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) stack).isItShadowItem()) {
            this.setItem(slot, stack);
            ItemEntity entity = ((ItemEntitySlot) (Object) stack).getEntity();
            if (entity != null) {
                entity.discard();
            }
            cir.setReturnValue(-1);
        }
    }
}