package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockStateArgument.class)
public abstract class BlockStateArgumentMixin {

    @Redirect(method = "setBlockState",at=@At(value = "INVOKE",target = "Lnet/minecraft/block/entity/BlockEntity;read(Lnet/minecraft/storage/ReadView;)V"))
    public void interceptBlockEntityLoad(BlockEntity instance, ReadView view){
        Globals.updateInventory(instance);
    }


}
