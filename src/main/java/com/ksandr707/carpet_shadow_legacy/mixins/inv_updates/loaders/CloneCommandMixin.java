package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;


import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.command.CloneCommand;
import net.minecraft.storage.ReadView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CloneCommand.class)
public abstract class CloneCommandMixin {

    @Redirect(method = "execute",at=@At(value = "INVOKE",target = "Lnet/minecraft/block/entity/BlockEntity;readComponentlessData(Lnet/minecraft/storage/ReadView;)V"))
    private static void interceptBlockEntityLoad(BlockEntity instance, ReadView view){
        Globals.updateInventory(instance);
    }


}
