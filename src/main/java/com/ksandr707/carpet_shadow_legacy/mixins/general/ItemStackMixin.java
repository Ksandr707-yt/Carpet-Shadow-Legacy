package com.ksandr707.carpet_shadow_legacy.mixins.general;

import com.ksandr707.carpet_shadow_legacy.component.ShadowComponent;
import com.ksandr707.carpet_shadow_legacy.component.ShadowNBTData;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(ItemStack.class)
public class ItemStackMixin implements ShadowItem {

    @Override
    public boolean isItShadowItem() {
        var shadowId = this.getShadowId();
        return shadowId!=null && !shadowId.isEmpty() && shadowId.matches("\\S+?");
    }

    @Override
    public String getShadowId() {
        if (!containsShadowComponent()) return null;
        var component = ((ItemStack)(Object)this).getComponents().get(ShadowNBTData.SHADOW);
        return component!=null ? component.shadowId() : null;
    }

    @Override
    public boolean containsShadowComponent() {
        return ((ItemStack)(Object)this).getComponents().contains(ShadowNBTData.SHADOW);
    }

    @Override
    public void setShadowId(String id) {
        ((ItemStack)(Object)this).set(ShadowNBTData.SHADOW, new ShadowComponent(id));
    }

    @Override
    public void removeShadow() {
        ((ItemStack)(Object)this).remove(ShadowNBTData.SHADOW);
    }
}