package com.ksandr707.carpet_shadow_legacy.mixins.general;

import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ShadowItem {

    @Override
    public boolean isShadowItem() {
        String id = getShadowId();
        return id != null && !id.isEmpty() && id.matches("\\S+?");
    }

    @Override
    public String getShadowId() {
        NbtComponent customData = ((ItemStack)(Object)this).get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null) {
            NbtCompound tag = customData.copyNbt();
            if (tag.contains(SHADOW_ID_KEY)) {
                return tag.getString(SHADOW_ID_KEY);
            }
        }
        return null;
    }

    @Override
    public void setShadowId(String id) {
        ItemStack stack = (ItemStack)(Object)this;
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        NbtCompound tag = (customData != null) ? customData.copyNbt() : new NbtCompound();
        tag.putString(SHADOW_ID_KEY, id);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }

    @Override
    public void removeShadow() {
        ItemStack stack = (ItemStack)(Object)this;
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null) {
            NbtCompound tag = customData.copyNbt();
            tag.remove(SHADOW_ID_KEY);
            if (tag.isEmpty()) {
                stack.remove(DataComponentTypes.CUSTOM_DATA);
            } else {
                stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
            }
        }
    }
}