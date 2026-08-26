package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ItemEntitySlot;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @WrapOperation(method = "merge(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;"))
    private static ItemStack redirect_copy(ItemStack stack, int count, Operation<ItemStack> original) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) stack).isItShadowItem()) {
            return stack;
        }
        return original.call(stack, count);
    }

    @ModifyReturnValue(method = "areMergable(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"))
    private static boolean canMerge(boolean original, ItemStack stack1, ItemStack stack2) {
        Globals.mergingThreads.add(Thread.currentThread());
        boolean ret = Globals.shadow_merge_check(stack1, stack2, original);
        Globals.mergingThreads.remove(Thread.currentThread());
        return ret;
    }

    @Inject(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.BY, by = 2))
    public void setEntityForStack(Player player, CallbackInfo ci, @Local(ordinal = 0) ItemStack stack) {
        ((ItemEntitySlot) (Object) stack).setEntity((ItemEntity)(Object)this);
    }

    @Inject(method = "playerTouch", at = @At(value = "RETURN"))
    public void resetEntityForStack(Player player, CallbackInfo ci) {
        final var itemStack = ((ItemEntity)(Object)this).getItem();
        ((ItemEntitySlot) (Object) itemStack).setEntity(null);
    }

    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void merging_start(Player player, CallbackInfo ci){
        Globals.mergingThreads.add(Thread.currentThread());
    }
    @Inject(method = "playerTouch", at = @At("RETURN"))
    private void merging_end(Player player, CallbackInfo ci){
        Globals.mergingThreads.remove(Thread.currentThread());
    }
}