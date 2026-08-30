package com.ksandr707.carpet_shadow_legacy.mixins.tooltip;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @ModifyReturnValue(method = "getTooltip", at = @At("RETURN"))
    private List<Text> postToolTip(List<Text> original) {
        var list = new ArrayList<>(original);
        if (CarpetShadowLegacySettings.shadowItemTooltip) {
            String shadowId = ((ShadowItem) this).getShadowId();
            if (shadowId != null && !shadowId.isEmpty()) {
                int maxLen = CarpetShadowLegacySettings.shadowItemIdSize;
                String displayId = (maxLen > 0 && shadowId.length() > maxLen)
                        ? shadowId.substring(0, maxLen)
                        : shadowId;
                MutableText text = Text.literal("shadow_id: ");
                MutableText sub = Text.literal(displayId);
                sub.formatted(Formatting.GOLD, Formatting.BOLD);
                text.append(sub);
                text.formatted(Formatting.DARK_GRAY, Formatting.ITALIC);
                list.add(text);
            }
        }
        return list;
    }

    @ModifyReturnValue(method = "copyWithCount", at = @At("RETURN"))
    private ItemStack removeShadowOnCopyWithCount(ItemStack original, int count) {
        if (original != null && !original.isEmpty() && ((ShadowItem) (Object) this).isShadowItem()) {
            ((ShadowItem) (Object) original).removeShadow();
        }
        return original;
    }
}