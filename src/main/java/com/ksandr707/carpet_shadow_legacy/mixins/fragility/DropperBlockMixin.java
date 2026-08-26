package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DropperBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(DropperBlock.class)
public abstract class DropperBlockMixin {

    @WrapOperation(method = "dispenseFrom", at=@At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;"), slice = @Slice(
            from = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.AFTER)
    ))
    ItemStack fix_dispense(ItemStack instance, Operation<ItemStack> original){
        if(CarpetShadowLegacySettings.shadowItemTransferFragilityFix && ((ShadowItem)(Object)instance).isItShadowItem()){
            return instance;
        }else{
            return original.call(instance);
        }
    }
}