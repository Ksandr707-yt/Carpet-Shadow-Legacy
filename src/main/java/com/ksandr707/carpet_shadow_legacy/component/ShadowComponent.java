package com.ksandr707.carpet_shadow_legacy.component;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public record ShadowComponent(String shadowId) implements TooltipAppender {

    public static final Identifier IDENTIFIER = Identifier.of("carpet-shadow", "shadow");
    public static final Codec<ShadowComponent> CODEC;
    public static final PacketCodec<RegistryByteBuf, ShadowComponent> PACKET_CODEC;



    public ShadowComponent(Text shadowIdText) {
        this(shadowIdText.getString());
    }

    public Text getTextShadowId() {
        return Text.of(shadowId);
    }
    public Text getTooltip() {
        MutableText text = Text.literal("shadow_id: ");
        MutableText sub = Text.literal(shadowId);
        sub.formatted(Formatting.GOLD, Formatting.BOLD);
        text.append(sub);
        text.formatted(Formatting.DARK_GRAY, Formatting.ITALIC);
        return text;
    }

    public boolean shouldShowTooltip() {
        return shadowId!=null && !shadowId.isEmpty() && shadowId.matches("\\S+?");
    }

    @Override
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        if (shouldShowTooltip()) tooltip.accept(getTooltip());
    }

    static {
        CODEC = TextCodecs.STRINGIFIED_CODEC.fieldOf("shadow_id").xmap(ShadowComponent::new, ShadowComponent::getTextShadowId).codec();
        PACKET_CODEC = TextCodecs.REGISTRY_PACKET_CODEC.xmap(ShadowComponent::new, ShadowComponent::getTextShadowId);
    }
}