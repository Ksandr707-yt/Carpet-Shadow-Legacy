package com.ksandr707.carpet_shadow_legacy.interfaces;

import net.minecraft.world.entity.item.ItemEntity;

public interface ItemEntitySlot {

    ItemEntity getEntity();

    void setEntity(ItemEntity entity);

}