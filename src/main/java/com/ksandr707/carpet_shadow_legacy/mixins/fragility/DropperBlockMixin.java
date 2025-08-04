package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.DropperBlock;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(DropperBlock.class)
public abstract class DropperBlockMixin {

    @WrapOperation(method = "dispense", at=@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;"), slice = @Slice(
            from = @At(value = "INVOKE",target = "Lnet/minecraft/block/entity/HopperBlockEntity;transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", shift = At.Shift.AFTER)
    ))
    ItemStack fix_dispense(ItemStack instance, Operation<ItemStack> original){
        if(CarpetShadowLegacySettings.shadowItemTransferFragilityFix && ((ShadowItem)(Object)instance).carpet_shadow$isItShadowItem()){
            return instance;
        }else{
            return original.call(instance);
        }
    }

}
