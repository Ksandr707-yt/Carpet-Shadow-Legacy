package com.ksandr707.carpet_shadow_legacy.mixins.persistence;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(
            method = "place*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/BlockItem;copyComponentsToBlockEntity(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onAfterCopyComponents(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (world.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity shulkerBox) {
            for (int index = 0; index < shulkerBox.size(); index++) {
                ItemStack stack = shulkerBox.getStack(index);
                if (stack.isEmpty()) continue;

                String shadowId = ((ShadowItem) (Object) stack).getShadowId();
                if (shadowId != null) {
                    if (CarpetShadowLegacySettings.shadowItemMode.shouldResetCount()) {
                        stack.setCount(0);
                    } else if (CarpetShadowLegacySettings.shadowItemMode.shouldLoadItem()) {
                        ItemStack registered = Globals.getByIdOrAdd(shadowId, stack);
                        shulkerBox.setStack(index, registered);
                    }
                }
            }
        }
    }
}