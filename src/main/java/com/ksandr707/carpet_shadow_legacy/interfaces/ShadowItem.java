package com.ksandr707.carpet_shadow_legacy.interfaces;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.ItemStack;

public interface ShadowItem {

    static ItemStack carpet_shadow$copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        ItemStack stack = original.call(instance);
        if (((ShadowItem) (Object) instance).carpet_shadow$isItShadowItem())
            ((ShadowItem) (Object) stack).carpet_shadow$setShadowId(((ShadowItem) (Object) instance).carpet_shadow$getShadowId());
        return stack;
    }

    static ItemStack carpet_shadow$copy_supplier(ItemStack instance, ItemStack copy) {
        if (((ShadowItem) (Object) instance).carpet_shadow$isItShadowItem())
            ((ShadowItem) (Object) copy).carpet_shadow$setShadowId(((ShadowItem) (Object) instance).carpet_shadow$getShadowId());
        return copy;
    }

    boolean carpet_shadow$isItShadowItem();
    String carpet_shadow$getShadowId();
    boolean carpet_shadow$containsShadowComponent();
    void carpet_shadow$setShadowId(String id);
    void carpet_shadow$removeShadow();
}
