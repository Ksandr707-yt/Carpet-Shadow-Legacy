package com.ksandr707.carpet_shadow_legacy.mixins.general;

import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LootTable.class)
public class LootTableMixin {

    @WrapOperation(method = "method_331", at=@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copyWithCount(I)Lnet/minecraft/item/ItemStack;"))
    private static ItemStack fix_survival_shulkers(ItemStack instance, int count, Operation<ItemStack> original){
        if (((ShadowItem)(Object)instance).carpet_shadow$isItShadowItem()){
            if (instance.getCount() == count){
                return instance;
            }else if (count < instance.getMaxCount()) {
                instance.setCount(count);
                return instance;
            }
        }
        return original.call(instance, count);
    }
}
