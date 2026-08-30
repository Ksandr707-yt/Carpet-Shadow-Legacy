package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacy;
import com.ksandr707.carpet_shadow_legacy.interfaces.InventoryItem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Redirect(method = "writeNbtToBlockEntity",at=@At(value = "INVOKE",target = "Lnet/minecraft/component/type/NbtComponent;applyToBlockEntity(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Z"))
    private static boolean interceptBlockEntityLoad(NbtComponent instance, BlockEntity blockEntity, RegistryWrapper.WrapperLookup registryLookup) {
        InventoryItem.readNbt(blockEntity, instance.copyNbt(), registryLookup);
        return instance.applyToBlockEntity(blockEntity, registryLookup);
    }
}