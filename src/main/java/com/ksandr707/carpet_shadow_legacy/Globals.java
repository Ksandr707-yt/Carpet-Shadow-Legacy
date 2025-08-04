package com.ksandr707.carpet_shadow_legacy;

import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Globals {

    public static final Set<Thread> mergingThreads = new HashSet<>();
    public static final Set<Inventory> toUpdate = new HashSet<>();
    public static final Set<Inventory> inventoriesToMarkDirty = new HashSet<>();


    public static ItemStack getByIdOrNull(String shadow_id) {
        if(shadow_id == null)
            return null;
        var cache = CarpetShadowLegacy.shadowMap.get(shadow_id);
        return cache!=null ? cache.getLeft() : null;
    }

    public static ItemStack getByIdOrAdd(String shadow_id, ItemStack stack) {
        var cache = CarpetShadowLegacy.shadowMap.get(shadow_id);
        if (cache != null) {
            return cache.getLeft();
        }
        CarpetShadowLegacy.shadowMap.put(shadow_id, new Pair<>(stack, new ArrayList<Pair<Inventory, Integer>>()));
        ((ShadowItem)(Object)stack).carpet_shadow$setShadowId(shadow_id);
        return stack;
    }
    public static void markInventoryDirty(Inventory inventory) {
        inventoriesToMarkDirty.add(inventory);
    }
    public static void markDirtyInventories() {
        for (Inventory inv : inventoriesToMarkDirty) {
            try {
                inv.markDirty();
            } catch (Exception e) {
                CarpetShadowLegacy.LOGGER.error("Error marking inventory dirty", e);
            }
        }
        inventoriesToMarkDirty.clear();
    }
    public static boolean shadow_merge_check(ItemStack stack1, ItemStack stack2, boolean ret) {
        var allowed = ret;
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && mergingThreads.contains(Thread.currentThread())) {
            var shadowStack1 = (ShadowItem) (Object) stack1;
            var shadowStack2 = (ShadowItem) (Object) stack2;
            var isStack1Shadow = shadowStack1.carpet_shadow$isItShadowItem();
            var isStack2Shadow = shadowStack2.carpet_shadow$isItShadowItem();
            String shadow1 = shadowStack1.carpet_shadow$getShadowId();
            String shadow2 = shadowStack2.carpet_shadow$getShadowId();
            if (stack1.isOf(stack2.getItem()) && ((isStack1Shadow && !isStack2Shadow) || (!isStack1Shadow && isStack2Shadow)))
                allowed = true;
            if (CarpetShadowLegacySettings.shadowItemPreventCombine && allowed) {
                if (isStack1Shadow && isStack2Shadow)
                    allowed =  false;
            } else if (isStack1Shadow && shadow1.equals(shadow2) && allowed)
                    allowed =  false;
        }
        return allowed;
    }
    public static boolean isShadowIdExists(String id) {
        return CarpetShadowLegacy.shadowMap.containsKey(id);
    }

    public static void updateInventory(Object object) {
        if (object instanceof Inventory inv) {
            removeInventory(object);
            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (!stack.isEmpty() && ((ShadowItem) (Object) stack).carpet_shadow$isItShadowItem()) {
                        var shadowId = ((ShadowItem)(Object)stack).carpet_shadow$getShadowId();
                        var cache = CarpetShadowLegacy.shadowMap.get(shadowId);
                        var pair = new Pair<>(inv, index);
                        if (cache != null) {
                            inv.setStack(index, cache.getLeft());
                            if (!cache.getRight().contains(pair)) cache.getRight().add(pair);
                        } else {
                            var list = new ArrayList<Pair<Inventory, Integer>>();
                            list.add(pair);
                            CarpetShadowLegacy.shadowMap.put(shadowId, new Pair<>(stack, list));
                        }
                    }
                }
            } catch (Exception ignored){}
        }
    }

    public static void updateItemStack(ItemStack stack) {
        if (!stack.isEmpty() && ((ShadowItem) (Object) stack).carpet_shadow$isItShadowItem()) {
            var cache = CarpetShadowLegacy.shadowMap.get(((ShadowItem)(Object)stack).carpet_shadow$getShadowId());
            if (cache != null) cache.getLeft().setCount(stack.getCount());
            else CarpetShadowLegacy.shadowMap.put(((ShadowItem) (Object) stack).carpet_shadow$getShadowId(), new Pair<>(stack, new ArrayList<>()));
        }
    }
    public static void addInventory(String shadowId, Object object, int slot) {
        if (object instanceof Inventory inv) {
            var cache = CarpetShadowLegacy.shadowMap.get(shadowId);
            if (cache != null) cache.getRight().add(new Pair<>(inv, slot));
        }
    }
    public static void removeInventory(String shadowId, Object object, int slot) {
        if (object instanceof Inventory inv) {
            var cache = CarpetShadowLegacy.shadowMap.get(shadowId);
            if (cache != null) cache.getRight().remove(new Pair<>(inv, slot));
        }
    }
    public static void removeInventory(Object object) {
        if (object instanceof Inventory inv) {
            try {
                for (int index = 0; index < inv.size(); index++) {
                    ItemStack stack = inv.getStack(index);
                    if (((ShadowItem) (Object) stack).carpet_shadow$isItShadowItem()) {
                        var shadowId = ((ShadowItem)(Object)stack).carpet_shadow$getShadowId();
                        var cache = CarpetShadowLegacy.shadowMap.get(shadowId);
                        if (cache!=null) {
                            var pair = new Pair<>(inv, index);
                            cache.getRight().remove(pair);
                        }
                    }
                }
            } catch (Exception ignored){}
        }
        else if (object instanceof Entity entity && entity instanceof Inventory) {
            updateInventory((Inventory) entity);
        }
    }
    public static void updateInventories(String shadowId) {
        var cache = CarpetShadowLegacy.shadowMap.get(shadowId);
        if (cache != null)
            toUpdate.addAll(cache.getRight().stream().map(it -> it.getLeft()).toList());
    }
}
