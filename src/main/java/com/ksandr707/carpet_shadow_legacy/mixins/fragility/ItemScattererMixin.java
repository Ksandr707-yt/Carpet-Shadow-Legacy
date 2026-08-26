package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Containers.class)
public abstract class ItemScattererMixin {

    @ModifyExpressionValue(method = "dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE",target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    private static boolean exitLoop(boolean empty, @Share("break") LocalBooleanRef quit){
        return empty || quit.get();
    }

    @WrapOperation(method = "dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE",target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"))
    private static ItemStack modify_split(ItemStack stack, int amount, Operation<ItemStack> original, @Share("break") LocalBooleanRef quit){
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShadowItem)(Object)stack).isItShadowItem()){
            quit.set(true);
            return stack;
        }
        return original.call(stack, amount);
    }
}