package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.interfaces.InventoryItem;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "readNbt", at = @At("RETURN"))
    private void onEntityLoad(NbtCompound nbt, CallbackInfo ci) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof Inventory inv) {
            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (((ShadowItem) (Object) stack).getShadowId() != null) {
                        ((InventoryItem) (Object) stack).addSlot(inv, index);
                    }
                }
            } catch(Exception ignored) {}
        }
    }
}