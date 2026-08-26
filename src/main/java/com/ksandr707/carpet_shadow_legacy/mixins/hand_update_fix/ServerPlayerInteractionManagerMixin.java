package com.ksandr707.carpet_shadow_legacy.mixins.hand_update_fix;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow public abstract boolean isCreative();

    @Inject(method = "useItemOn", at = @At(value = "RETURN",shift = At.Shift.BEFORE))
    private void inject_on_block_use(ServerPlayer player, Level world, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir){
        if(CarpetShadowLegacySettings.shadowItemUseFix && ((ShadowItem)(Object)stack).isItShadowItem() && !isCreative()) {
            InteractionResult result = cir.getReturnValue();
            if (result==InteractionResult.SUCCESS || result == InteractionResult.CONSUME) {
                int index = (hand == InteractionHand.OFF_HAND) ? Inventory.SLOT_OFFHAND : player.getInventory().getSelectedSlot();
                player.containerMenu.findSlot(player.getInventory(), index).ifPresent(i -> player.containerMenu.setRemoteSlot(i, new ItemStack(Blocks.AIR)));
            }
        }
    }
}