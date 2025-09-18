package com.ksandr707.carpet_shadow_legacy.interfaces;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.ItemStack;

public interface ShadowItem {

    static ItemStack copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        ItemStack stack = original.call(instance);
        if (((ShadowItem) (Object) instance).isItShadowItem())
            ((ShadowItem) (Object) stack).setShadowId(((ShadowItem) (Object) instance).getShadowId());
        return stack;
    }

    static ItemStack copy_supplier(ItemStack instance, ItemStack copy) {
        if (((ShadowItem) (Object) instance).isItShadowItem())
            ((ShadowItem) (Object) copy).setShadowId(((ShadowItem) (Object) instance).getShadowId());
        return copy;
    }

    boolean isItShadowItem();
    String getShadowId();
    boolean containsShadowComponent();
    void setShadowId(String id);
    void removeShadow();
}