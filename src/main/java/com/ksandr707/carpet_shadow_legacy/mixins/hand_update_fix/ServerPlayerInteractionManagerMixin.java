package com.ksandr707.carpet_shadow_legacy.mixins.hand_update_fix;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow public abstract boolean isCreative();

    @Inject(method = "interactBlock", at = @At(value = "RETURN",shift = At.Shift.BEFORE))
    private void inject_on_block_use(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir){
        if(CarpetShadowLegacySettings.shadowItemUseFix && ((ShadowItem)(Object)stack).isItShadowItem() && !isCreative()) {
            ActionResult result = cir.getReturnValue();
            if (result==ActionResult.SUCCESS || result == ActionResult.CONSUME) {
                int index = (hand == Hand.OFF_HAND) ? PlayerInventory.OFF_HAND_SLOT : player.getInventory().getSelectedSlot();
                player.currentScreenHandler.getSlotIndex(player.getInventory(), index).ifPresent(i -> player.currentScreenHandler.setReceivedStack(i, new ItemStack(Blocks.AIR)));
            }
        }
    }
}