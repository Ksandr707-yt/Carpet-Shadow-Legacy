package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow
    public abstract void setByPlayer(ItemStack stack);

    @WrapOperation(method = "tryRemove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;remove(I)Lnet/minecraft/world/item/ItemStack;"))
    public ItemStack fixFragility_tryTakeStackRange(Slot instance, int amount, Operation<ItemStack> original) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) instance.getItem()).isItShadowItem() &&
                amount == instance.getItem().getCount()) {
            ItemStack ret = instance.getItem();
            instance.setByPlayer(ItemStack.EMPTY);
            return ret;
        }
        return original.call(instance, amount);
    }

    @Inject(method = "safeInsert(Lnet/minecraft/world/item/ItemStack;I)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    public void fixFragility_insertStack(ItemStack stack, int count, CallbackInfoReturnable<ItemStack> cir) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) stack).isItShadowItem() &&
                count == stack.getCount()) {
            this.setByPlayer(stack);
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
