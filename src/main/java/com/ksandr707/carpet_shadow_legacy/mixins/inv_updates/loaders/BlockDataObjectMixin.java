package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.server.commands.data.BlockDataAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockDataAccessor.class)
public abstract class BlockDataObjectMixin {

    @Redirect(method = "setData",at=@At(value = "INVOKE",target = "Lnet/minecraft/world/level/block/entity/BlockEntity;loadWithComponents(Lnet/minecraft/world/level/storage/ValueInput;)V"))
    private void interceptBlockEntityLoad(BlockEntity instance, ValueInput view){
        Globals.updateInventory(instance);
    }
}