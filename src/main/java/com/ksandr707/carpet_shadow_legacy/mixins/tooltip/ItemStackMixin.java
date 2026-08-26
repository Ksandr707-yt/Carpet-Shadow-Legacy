package com.ksandr707.carpet_shadow_legacy.mixins.tooltip;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.component.ShadowNBTData;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract DataComponentMap getComponents();

    @ModifyReturnValue(method = "getTooltipLines", at = @At("RETURN"))
    private List<Component> postToolTip(List<Component> original) {
        var list = new ArrayList<>(original);
        if (CarpetShadowLegacySettings.shadowItemTooltip) {
            if (((ShadowItem) this).isItShadowItem())
                list.add(this.getComponents().get(ShadowNBTData.SHADOW).getTooltip());
        }
        return list;
    }

    @ModifyReturnValue(method = "copy", at =@At("RETURN"))
    private ItemStack removeTooltipInCopy(ItemStack original) {
            if (((ShadowItem) this).isItShadowItem())
                ((ShadowItem) (Object) original).removeShadow();
        return original;
    }
}