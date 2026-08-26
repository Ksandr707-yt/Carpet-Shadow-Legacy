package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockInput.class)
public abstract class BlockStateArgumentMixin {

    @Redirect(method = "place",at=@At(value = "INVOKE",target = "Lnet/minecraft/world/level/block/entity/BlockEntity;loadWithComponents(Lnet/minecraft/world/level/storage/ValueInput;)V"))
    public void interceptBlockEntityLoad(BlockEntity instance, ValueInput view){
        Globals.updateInventory(instance);
    }
}