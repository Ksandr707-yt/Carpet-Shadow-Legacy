package com.ksandr707.carpet_shadow_legacy.mixins.general;

import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.ksandr707.carpet_shadow_legacy.newAPI.ShadowComponent;
import com.ksandr707.carpet_shadow_legacy.newAPI.ShadowNBTData;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(ItemStack.class)
public class ItemStackMixin implements ShadowItem {

    @Override
    public boolean carpet_shadow$isItShadowItem() {
        var shadowId = this.carpet_shadow$getShadowId();
        return shadowId!=null && !shadowId.isEmpty() && shadowId.matches("\\S+?");
    }

    @Override
    public String carpet_shadow$getShadowId() {
        if (!carpet_shadow$containsShadowComponent()) return null;
        var component = ((ItemStack)(Object)this).getComponents().get(ShadowNBTData.SHADOW);
        return component!=null ? component.shadowId() : null;
    }

    @Override
    public boolean carpet_shadow$containsShadowComponent() {
        return ((ItemStack)(Object)this).getComponents().contains(ShadowNBTData.SHADOW);
    }

    @Override
    public void carpet_shadow$setShadowId(String id) {
        ((ItemStack)(Object)this).set(ShadowNBTData.SHADOW, new ShadowComponent(id));
    }

    @Override
    public void carpet_shadow$removeShadow() {
        ((ItemStack)(Object)this).remove(ShadowNBTData.SHADOW);
    }
}
