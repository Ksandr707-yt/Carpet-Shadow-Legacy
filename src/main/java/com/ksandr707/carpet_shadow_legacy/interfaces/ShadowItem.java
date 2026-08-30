package com.ksandr707.carpet_shadow_legacy.interfaces;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.item.ItemStack;

public interface ShadowItem {
    String SHADOW_ID_KEY = "shadow_id";

    static ItemStack copy_redirect(ItemStack instance, Operation<ItemStack> original) {
        ItemStack stack = original.call(instance);
        String id = ((ShadowItem) (Object) instance).getShadowId();
        if (id != null && !id.isEmpty())
            ((ShadowItem) (Object) stack).setShadowId(id);
        return stack;
    }

    static ItemStack copy_supplier(ItemStack instance, ItemStack copy) {
        String id = ((ShadowItem) (Object) instance).getShadowId();
        if (id != null && !id.isEmpty())
            ((ShadowItem) (Object) copy).setShadowId(id);
        return copy;
    }

    boolean isShadowItem();
    String getShadowId();
    void setShadowId(String id);
    void removeShadow();
}