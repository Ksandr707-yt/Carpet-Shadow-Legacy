package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacy;
import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "setCount", at=@At("RETURN"))
    public void propagate_update(int count, CallbackInfo ci){
        if (CarpetShadowLegacySettings.shadowItemUpdateFix &&
                ((ShadowItem) this).isItShadowItem()) {
            String shadowId = ((ShadowItem) this).getShadowId();
            var cache = CarpetShadowLegacy.shadowMap.get(shadowId);

            if (cache != null) {
                for (var entry : cache.getRight()) {
                    Globals.inventoriesToMarkDirty.add(entry.getLeft());
                }
            }
        }
    }
}