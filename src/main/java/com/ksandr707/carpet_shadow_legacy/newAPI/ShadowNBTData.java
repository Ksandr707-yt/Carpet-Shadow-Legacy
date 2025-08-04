package com.ksandr707.carpet_shadow_legacy.newAPI;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class ShadowNBTData {
    public static final ComponentType<ShadowComponent> SHADOW = register(ShadowComponent.IDENTIFIER, (builder ->
        builder.codec(ShadowComponent.CODEC)
                .packetCodec(ShadowComponent.PACKET_CODEC)
                .cache()
    ));

    private static <T> ComponentType<T> register(Identifier id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return (ComponentType)Registry.register(Registries.DATA_COMPONENT_TYPE, id, ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
    }
}
