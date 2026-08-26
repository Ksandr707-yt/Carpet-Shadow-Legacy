package com.ksandr707.carpet_shadow_legacy;

import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

public class Globals {

    public static final Set<Thread> mergingThreads = new HashSet<>();
    public static final Set<Container> inventoriesToMarkDirty = new HashSet<>();

    public static ItemStack getByIdOrNull(String shadow_id) {
        if(shadow_id == null)
            return null;
        var cache = CarpetShadowLegacy.shadowMap.get(shadow_id);
        return cache!=null ? cache.getA() : null;
    }

    public static ItemStack getByIdOrAdd(String shadow_id, ItemStack stack) {
        var cache = CarpetShadowLegacy.shadowMap.get(shadow_id);
        if (cache != null) {
            return cache.getA();
        }
        CarpetShadowLegacy.shadowMap.put(shadow_id, new Pair<>(stack, new ArrayList<Pair<Container, Integer>>()));
        ((ShadowItem)(Object)stack).setShadowId(shadow_id);
        return stack;
    }
    public static boolean shadow_merge_check(ItemStack stack1, ItemStack stack2, boolean ret) {
        var allowed = ret;
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && mergingThreads.contains(Thread.currentThread())) {
            var shadowStack1 = (ShadowItem) (Object) stack1;
            var shadowStack2 = (ShadowItem) (Object) stack2;
            var isStack1Shadow = shadowStack1.isItShadowItem();
            var isStack2Shadow = shadowStack2.isItShadowItem();
            String shadow1 = shadowStack1.getShadowId();
            String shadow2 = shadowStack2.getShadowId();
            if (stack1.is(stack2.getItem()) && ((isStack1Shadow && !isStack2Shadow) || (!isStack1Shadow && isStack2Shadow)))
                allowed = true;
            if (CarpetShadowLegacySettings.shadowItemPreventCombine && allowed) {
                if (isStack1Shadow && isStack2Shadow)
                    allowed =  false;
            } else if (isStack1Shadow && shadow1.equals(shadow2) && allowed)
                    allowed =  false;
        }
        return allowed;
    }
    public static void updateInventory(Object object) {
        if (object instanceof Container inv) {
            removeInventory(object);
            try {
                for (int index = 0; index < inv.getContainerSize(); index++) {
                    ItemStack stack = inv.getItem(index);
                    if (!stack.isEmpty() && ((ShadowItem) (Object) stack).isItShadowItem()) {
                        var shadowId = ((ShadowItem)(Object)stack).getShadowId();
                        var cache = CarpetShadowLegacy.shadowMap.get(shadowId);
                        var pair = new Pair<>(inv, index);
                        if (cache != null) {
                            inv.setItem(index, cache.getA());
                            if (!cache.getB().contains(pair)) cache.getB().add(pair);
                        } else {
                            var list = new ArrayList<Pair<Container, Integer>>();
                            list.add(pair);
                            CarpetShadowLegacy.shadowMap.put(shadowId, new Pair<>(stack, list));
                        }
                    }
                }
            } catch (Exception ignored){}
        }
    }

    public static void addInventory(String shadowId, Object object, int slot) {
        if (object instanceof Container inv) {
            var cache = CarpetShadowLegacy.shadowMap.get(shadowId);
            if (cache != null) cache.getB().add(new Pair<>(inv, slot));
        }
    }
    public static void removeInventory(String shadowId, Object object, int slot) {
        if (object instanceof Container inv) {
            var cache = CarpetShadowLegacy.shadowMap.get(shadowId);
            if (cache != null) cache.getB().remove(new Pair<>(inv, slot));
        }
    }
    public static void removeInventory(Object object) {
        if (object instanceof Container inv) {
            try {
                for (int index = 0; index < inv.getContainerSize(); index++) {
                    ItemStack stack = inv.getItem(index);
                    if (((ShadowItem) (Object) stack).isItShadowItem()) {
                        var shadowId = ((ShadowItem)(Object)stack).getShadowId();
                        var cache = CarpetShadowLegacy.shadowMap.get(shadowId);
                        if (cache!=null) {
                            var pair = new Pair<>(inv, index);
                            cache.getB().remove(pair);
                        }
                    }
                }
            } catch (Exception ignored){}
        }
        else if (object instanceof Entity entity && entity instanceof Container) {
            updateInventory(entity);
        }
    }
}