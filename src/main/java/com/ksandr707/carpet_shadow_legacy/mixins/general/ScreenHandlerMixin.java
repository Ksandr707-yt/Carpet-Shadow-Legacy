package com.ksandr707.carpet_shadow_legacy.mixins.general;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacy;
import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class ScreenHandlerMixin {

    @Shadow
    public abstract Slot getSlot(int index);

    @Shadow
    public abstract ItemStack getCarried();

    @WrapOperation(
            method = "clicked",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;doClick(IILnet/minecraft/world/inventory/ContainerInput;Lnet/minecraft/world/entity/player/Player;)V"
            )
    )
    private void handle_shadowing(AbstractContainerMenu instance, int slotIndex, int button, ContainerInput actionType, Player player, Operation<Void> original) {
        try {
            original.call(instance, slotIndex, button, actionType, player);
        } catch (Throwable error) {
            if (actionType != ContainerInput.SWAP && actionType != ContainerInput.PICKUP && actionType != ContainerInput.QUICK_CRAFT)
                throw error;
            ItemStack stack1 = this.getSlot(slotIndex).getItem();
            ItemStack stack2 = player.getInventory().getItem(button);
            ItemStack stack3 = this.getCarried();
            ItemStack shadow = null;
            if (stack1 == stack2 || stack1 == stack3)
                shadow = stack1;
            else if (stack2 == stack3)
                shadow = stack2;

            if (shadow != null) {
                CarpetShadowLegacy.LOGGER.warn("New Shadow Item Created");
                String shadow_id = ((ShadowItem) (Object) shadow).getShadowId();
                if (shadow_id == null || shadow_id.isEmpty())
                    shadow_id = CarpetShadowLegacy.shadow_id_generator.nextString();
                Globals.getByIdOrAdd(shadow_id, shadow);
            }
        }
    }

    @Inject(
            method = "doClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/Slot;setByPlayer(Lnet/minecraft/world/item/ItemStack;)V",
                    shift = At.Shift.BEFORE
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/entity/player/Player;updateTutorialInventoryAction(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/inventory/ClickAction;)V"
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/inventory/Slot;setChanged()V"
                    )
            )
    )
    private void reintroduceSuppressionShadowing1(int slotIndex, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci) {
        if (CarpetShadowLegacySettings.shadowSuppressionGeneration) {
            Slot slot = this.getSlot(slotIndex);
            slot.setChanged();
        }
    }

    @Inject(
            method = "doClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/Slot;setByPlayer(Lnet/minecraft/world/item/ItemStack;)V",
                    shift = At.Shift.BEFORE
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/world/inventory/ContainerInput;SWAP:Lnet/minecraft/world/inventory/ContainerInput;",
                            opcode = Opcodes.GETSTATIC
                    ),
                    to = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/world/inventory/ContainerInput;CLONE:Lnet/minecraft/world/inventory/ContainerInput;",
                            opcode = Opcodes.GETSTATIC
                    )
            )
    )
    private void reintroduceSuppressionShadowing2(int slotIndex, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci) {
        if (CarpetShadowLegacySettings.shadowSuppressionGeneration) {
            Slot slot = this.getSlot(slotIndex);
            slot.setChanged();
        }
    }
}