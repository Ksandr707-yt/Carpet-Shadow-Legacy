package com.ksandr707.carpet_shadow_legacy.component;

import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ShadowNBTData {
    public static final DataComponentType<ShadowComponent> SHADOW = register(ShadowComponent.IDENTIFIER, (builder ->
        builder.persistent(ShadowComponent.CODEC)
                .networkSynchronized(ShadowComponent.PACKET_CODEC)
                .cacheEncoding()
    ));

    private static <T> DataComponentType<T> register(Identifier id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return (DataComponentType)Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, ((DataComponentType.Builder)builderOperator.apply(DataComponentType.builder())).build());
    }
}