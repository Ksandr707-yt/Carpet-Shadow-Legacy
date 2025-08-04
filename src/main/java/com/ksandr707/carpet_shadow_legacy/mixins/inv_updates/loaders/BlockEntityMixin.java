package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates.loaders;


import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.newAPI.ShadowComponent;
import com.ksandr707.carpet_shadow_legacy.newAPI.ShadowNBTData;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

    @Inject(method = "createFromNbt", at = @At("RETURN"))
    private static void interceptBlockEntityLoad(BlockPos pos, BlockState state, NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfoReturnable<BlockEntity> cir){
        Globals.updateInventory(cir.getReturnValue());
    }
    @Inject(method = "readComponents(Lnet/minecraft/item/ItemStack;)V", at = @At("RETURN"))
    private void onBlockEntityLoad(ItemStack stack, CallbackInfo ci) {
        if (this instanceof Inventory) {
            Globals.updateInventory((Inventory) (Object) this);
        }
    }
}
