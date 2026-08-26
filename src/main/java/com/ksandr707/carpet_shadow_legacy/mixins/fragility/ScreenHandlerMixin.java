package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShifingItem;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class ScreenHandlerMixin {
    @Shadow
    public abstract ItemStack getCarried();

    @Inject(method = "doClick", at = @At("HEAD"))
    public void merging_start(int slotIndex, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci){
        Globals.mergingThreads.add(Thread.currentThread());
    }

    @Inject(method = "doClick", at = @At("RETURN"))
    public void merging_end(int slotIndex, int buttonNum, ContainerInput containerInput, Player player, CallbackInfo ci){
        Globals.mergingThreads.remove(Thread.currentThread());
    }

    @WrapOperation(method = "doClick", slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;mayPickup(Lnet/minecraft/world/entity/player/Player;)Z", ordinal = 1)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;setCarried(Lnet/minecraft/world/item/ItemStack;)V", ordinal = 1)
    )
    public void remove_shadow_stack(AbstractContainerMenu instance, ItemStack stack, Operation<Void> original) {
        String shadowId1 = ((ShadowItem) (Object) getCarried()).getShadowId();
        String shadowId2 = ((ShadowItem) (Object) stack).getShadowId();
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShadowItem)(Object)stack).isItShadowItem() && shadowId1.equals(shadowId2)) {
            instance.setCarried(ItemStack.EMPTY);
        } else {
            original.call(instance, stack);
        }
    }

    @WrapOperation(method = "doClick", slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;mayPickup(Lnet/minecraft/world/entity/player/Player;)Z")
    ),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;quickMoveStack(Lnet/minecraft/world/entity/player/Player;I)Lnet/minecraft/world/item/ItemStack;"))
    public ItemStack fix_shift(AbstractContainerMenu instance, Player player, int index, Operation<ItemStack> original) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix) {
            Slot og = instance.slots.get(index);
            ItemStack og_item = og.getItem();
            if (((ShadowItem) (Object) og_item).isItShadowItem()) {
                ItemStack mirror = og_item.copy();
                ((ShadowItem) (Object) mirror).setShadowId(((ShadowItem) (Object) og_item).getShadowId());
                og.setByPlayer(mirror);
                ((ShifingItem)(Object)mirror).setShiftMoving(true);
                ItemStack ret = original.call(instance, player, index);
                ((ShifingItem)(Object)mirror).setShiftMoving(false);
                if (ret == ItemStack.EMPTY) {
                    og_item = Globals.getByIdOrAdd(((ShadowItem) (Object) og_item).getShadowId(), og_item);
                    og.setByPlayer(og_item);
                    og_item.setCount(mirror.getCount());
                }
                return ret;
            }
        }
        return original.call(instance, player, index);
    }

    @WrapOperation(method = "moveItemStackTo", slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;getItem()Lnet/minecraft/world/item/ItemStack;", ordinal = 1)
    ),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 0))
    public ItemStack fix_shift(ItemStack instance, int amount, Operation<ItemStack> original) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShadowItem) (Object) instance).isItShadowItem()) {
            String shadow_id = ((ShadowItem) (Object) instance).getShadowId();
            ItemStack og_item = Globals.getByIdOrNull(shadow_id);
            if (og_item != null) {
                og_item.setCount(instance.getCount());
                instance.setCount(0);
                return og_item;
            }
        }
        return original.call(instance, amount);
    }

    @WrapOperation(method = "moveItemStackTo",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",ordinal = 0))
    public boolean fix_shift2(ItemStack instance, Operation<Boolean> original) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && ((ShifingItem) (Object) instance).isShiftMoving()) {
            return true;
        }
        return original.call(instance);
    }

    @WrapOperation(method = "doClick",
            slice = @Slice(
                    from = @At(value = "INVOKE",target = "Lnet/minecraft/core/NonNullList;get(I)Ljava/lang/Object;"),
                    to = @At(value = "INVOKE",target = "Ljava/util/Set;add(Ljava/lang/Object;)Z")
            ),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;canItemQuickReplace(Lnet/minecraft/world/inventory/Slot;Lnet/minecraft/world/item/ItemStack;Z)Z"))
    public boolean fixQuickCraft(Slot slot, ItemStack stack, boolean allowOverflow, Operation<Boolean> original) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix) {
            ItemStack slotStack = slot.getItem();
            ItemStack ref1 = Globals.getByIdOrNull(((ShadowItem) (Object) slotStack).getShadowId());
            ItemStack ref2 = Globals.getByIdOrNull(((ShadowItem) (Object) stack).getShadowId());
            if(slotStack == ref1 || stack == ref2)
                return false;
        }
        return original.call(slot, stack, allowOverflow);
    }
}
