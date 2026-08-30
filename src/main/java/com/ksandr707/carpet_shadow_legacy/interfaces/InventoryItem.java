package com.ksandr707.carpet_shadow_legacy.interfaces;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;

import java.util.Collection;

public interface InventoryItem {

    Collection<Inventory> getInventories();

    void addSlot(Inventory inventory, int slot);

    void removeSlot(Inventory inventory, int slot);

    static void readNbt(BlockEntity instance, NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (instance instanceof Inventory inv) {
            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (((ShadowItem) (Object) stack).getShadowId() != null) {
                        ((InventoryItem) (Object) stack).removeSlot(inv, index);
                    }
                }
            }catch (Exception ignored){}
            instance.read(nbt, registryLookup);

            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (((ShadowItem) (Object) stack).getShadowId() != null) {
                        ((InventoryItem) (Object) stack).addSlot(inv, index);
                    }
                }
            }catch (Exception ignored){}
        } else {
            instance.read(nbt, registryLookup);
        }
    }
}