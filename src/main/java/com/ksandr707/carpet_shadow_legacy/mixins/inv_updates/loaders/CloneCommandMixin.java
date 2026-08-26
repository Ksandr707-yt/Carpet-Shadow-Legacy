package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.server.commands.CloneCommands;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CloneCommands.class)
public abstract class CloneCommandMixin {

    @Redirect(method = "clone",at=@At(value = "INVOKE",target = "Lnet/minecraft/world/level/block/entity/BlockEntity;loadCustomOnly(Lnet/minecraft/world/level/storage/ValueInput;)V"))
    private static void interceptBlockEntityLoad(BlockEntity instance, ValueInput view){
        Globals.updateInventory(instance);
    }
}