package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {

    @Redirect(method = "tick",at=@At(value = "INVOKE",target = "Lnet/minecraft/block/entity/BlockEntity;read(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)V"))
    public void interceptBlockEntityLoad(BlockEntity instance, NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup){
        Globals.updateInventory(instance);
    }
}