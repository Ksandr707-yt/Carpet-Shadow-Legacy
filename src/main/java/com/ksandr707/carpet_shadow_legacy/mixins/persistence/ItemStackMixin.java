package com.ksandr707.carpet_shadow_legacy.mixins.persistence;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ItemStack.class)
    public abstract class ItemStackMixin {

    @ModifyReturnValue(at = @At("RETURN"), method = "fromNbt")
    private static Optional<ItemStack> post_fromNbt(
            Optional<ItemStack> original,
            RegistryWrapper.WrapperLookup registries,
            NbtElement nbt
    ) {
        if (original.isPresent()) {
            ItemStack stack = original.get();
            String shadowId = ((ShadowItem) (Object) stack).getShadowId();
            if (shadowId != null && !shadowId.isEmpty()) {
                if (CarpetShadowLegacySettings.shadowItemMode.shouldResetCount()) {
                    stack.setCount(0);
                } else if (CarpetShadowLegacySettings.shadowItemMode.shouldLoadItem()) {
                    stack = Globals.getByIdOrAdd(shadowId, stack);
                    return Optional.of(stack);
                }
            }

            if (nbt instanceof NbtCompound compound && compound.contains("shadow")) {
                if (CarpetShadowLegacySettings.shadowItemMode.shouldResetCount()) {
                    stack.setCount(0);
                } else if (CarpetShadowLegacySettings.shadowItemMode.shouldLoadItem()) {
                shadowId = compound.getString("shadow");
                ((ShadowItem) (Object) stack).setShadowId(shadowId);
                stack = Globals.getByIdOrAdd(shadowId, stack);
                return Optional.of(stack);
                }
            }
        }
        return original;
    }
}