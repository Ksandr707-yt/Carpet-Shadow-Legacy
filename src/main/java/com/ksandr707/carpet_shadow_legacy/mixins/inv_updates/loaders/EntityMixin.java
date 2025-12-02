package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.storage.ReadView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "readData", at = @At("RETURN"))
    private void onEntityLoad(ReadView view, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof Inventory) {
            entity.getEntityWorld().getServer().execute(() -> {
                Globals.updateInventory(entity);
            });
        }
    }
}