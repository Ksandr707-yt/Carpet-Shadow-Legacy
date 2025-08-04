package com.ksandr707.carpet_shadow_legacy.mixins.tooltip;


import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.ksandr707.carpet_shadow_legacy.newAPI.ShadowNBTData;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract ComponentMap getComponents();

    @ModifyReturnValue(method = "getTooltip", at = @At("RETURN"))
    private List<Text> postToolTip(List<Text> original) {
        var list = new ArrayList<>(original);
        if (CarpetShadowLegacySettings.shadowItemTooltip) {
            if (((ShadowItem) (Object) this).carpet_shadow$isItShadowItem())
                list.add(this.getComponents().get(ShadowNBTData.SHADOW).getTooltip());
        }
        return list;
    }

    @ModifyReturnValue(method = "copy", at =@At("RETURN"))
    private ItemStack removeTooltipInCopy(ItemStack original) {
            if (((ShadowItem) (Object) this).carpet_shadow$isItShadowItem())
                ((ShadowItem) (Object) original).carpet_shadow$removeShadow();
        return original;
    }
}
