package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.BlockDataObject;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockDataObject.class)
public abstract class BlockDataObjectMixin {

    @Redirect(method = "setNbt",at=@At(value = "INVOKE",target = "Lnet/minecraft/block/entity/BlockEntity;read(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)V"))
    private void interceptBlockEntityLoad(BlockEntity instance, NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup){
        Globals.updateInventory(instance);
    }
}