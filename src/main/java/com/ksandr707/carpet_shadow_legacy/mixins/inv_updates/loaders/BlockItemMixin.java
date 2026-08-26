package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;

import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Redirect(method = "updateCustomBlockEntityTag(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)Z",at=@At(value = "INVOKE",target = "Lnet/minecraft/world/item/component/TypedEntityData;loadInto(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/core/HolderLookup$Provider;)Z"))
    private static boolean interceptBlockEntityLoad(TypedEntityData instance, BlockEntity blockEntity, HolderLookup.Provider registryLookup){
        Globals.updateInventory(blockEntity);
        return instance.loadInto(blockEntity, registryLookup);
    }
}