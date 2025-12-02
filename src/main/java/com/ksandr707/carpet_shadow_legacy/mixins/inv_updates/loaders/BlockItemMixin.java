package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Redirect(method = "writeNbtToBlockEntity",at=@At(value = "INVOKE",target = "Lnet/minecraft/entity/TypedEntityData;applyToBlockEntity(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Z"))
    private static boolean interceptBlockEntityLoad(TypedEntityData instance, BlockEntity blockEntity, RegistryWrapper.WrapperLookup registryLookup){
        Globals.updateInventory(blockEntity);
        return instance.applyToBlockEntity(blockEntity, registryLookup);
    }
}