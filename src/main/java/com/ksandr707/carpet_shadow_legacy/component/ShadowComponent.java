package com.ksandr707.carpet_shadow_legacy.component;

import com.mojang.serialization.Codec;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record ShadowComponent(String shadowId) implements TooltipProvider {

    public static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath("carpet-shadow", "shadow");
    public static final Codec<ShadowComponent> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, ShadowComponent> PACKET_CODEC;



    public ShadowComponent(Component shadowIdText) {
        this(shadowIdText.getString());
    }

    public Component getTextShadowId() {
        return Component.nullToEmpty(shadowId);
    }
    public Component getTooltip() {
        MutableComponent text = Component.literal("shadow_id: ");
        MutableComponent sub = Component.literal(shadowId);
        sub.withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD);
        text.append(sub);
        text.withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);
        return text;
    }

    public boolean shouldShowTooltip() {
        return shadowId!=null && !shadowId.isEmpty() && shadowId.matches("\\S+?");
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type, DataComponentGetter components) {
        if (shouldShowTooltip()) tooltip.accept(getTooltip());
    }

    static {
        CODEC = ComponentSerialization.CODEC.fieldOf("shadow_id").xmap(ShadowComponent::new, ShadowComponent::getTextShadowId).codec();
        PACKET_CODEC = ComponentSerialization.STREAM_CODEC.map(ShadowComponent::new, ShadowComponent::getTextShadowId);
    }
}