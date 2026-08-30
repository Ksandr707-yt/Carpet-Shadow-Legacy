package com.ksandr707.carpet_shadow_legacy;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class CarpetShadowLegacy implements CarpetExtension, ModInitializer {
    public static final Cache<String, ItemStack> shadowMap = CacheBuilder.newBuilder().weakValues().build();
    public static final Logger LOGGER = LogManager.getLogger("Carpet-Shadow-Legacy");

    @Override
    public void onGameStarted() {
        CarpetShadowLegacy.LOGGER.info("Carpet Shadow Legacy Loaded!");
        CarpetServer.settingsManager.parseSettingsClass(CarpetShadowLegacySettings.class);
    }

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(new CarpetShadowLegacy());
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        shadowMap.invalidateAll();
    }

    @Override
    public void onTick(MinecraftServer server) {
        if (server.getTicks() % 1000 == 0)
            CarpetShadowLegacy.shadowMap.cleanUp();
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        InputStream langFile = getClass().getClassLoader().getResourceAsStream("assets/carpet-shadow-legacy/lang/%s.json".formatted(lang));
        if (langFile == null) {
            return Collections.emptyMap();
        }
        String jsonData;
        try {
            jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Collections.emptyMap();
        }
        Gson gson = new GsonBuilder().setLenient().create();
        return gson.fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
    }
}