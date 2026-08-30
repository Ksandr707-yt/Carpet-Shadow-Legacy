package com.ksandr707.carpet_shadow_legacy;

import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.*;

public class Globals {

    public static final Set<Thread> mergingThreads = new HashSet<>();

    public static final Set<Inventory> toUpdate = Collections.newSetFromMap(new WeakHashMap<>());

    public static String createShadowId() {
        return UUID.randomUUID().toString();
    }

    public static ItemStack getByIdOrNull(String shadow_id) {
        if(shadow_id == null)
            return null;
        return CarpetShadowLegacy.shadowMap.getIfPresent(shadow_id);
    }

    public static ItemStack getByIdOrAdd(String shadow_id, ItemStack stack) {
        ItemStack reference = CarpetShadowLegacy.shadowMap.getIfPresent(shadow_id);
        if (reference != null)
            return reference;
        ((ShadowItem)(Object)stack).setShadowId(shadow_id);
        CarpetShadowLegacy.shadowMap.put(shadow_id, stack);
        return stack;
    }

    public static boolean shadow_merge_check(ItemStack stack1, ItemStack stack2, boolean ret) {
        var allowed = ret;
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && mergingThreads.contains(Thread.currentThread())) {
            var shadowStack1 = (ShadowItem) (Object) stack1;
            var shadowStack2 = (ShadowItem) (Object) stack2;
            var isStack1Shadow = shadowStack1.isShadowItem();
            var isStack2Shadow = shadowStack2.isShadowItem();
            String shadow1 = shadowStack1.getShadowId();
            String shadow2 = shadowStack2.getShadowId();
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
}