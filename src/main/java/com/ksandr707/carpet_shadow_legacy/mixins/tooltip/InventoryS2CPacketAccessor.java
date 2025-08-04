package com.ksandr707.carpet_shadow_legacy.mixins.tooltip;

import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InventoryS2CPacket.class)
public interface InventoryS2CPacketAccessor {
    @Accessor("cursorStack")
    ItemStack getCursorStack();

    @Accessor("cursorStack")
    void setCursorStack(ItemStack stack);
}
