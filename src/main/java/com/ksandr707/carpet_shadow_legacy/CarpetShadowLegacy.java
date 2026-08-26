package com.ksandr707.carpet_shadow_legacy;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ksandr707.carpet_shadow_legacy.component.ShadowNBTData;
import com.ksandr707.carpet_shadow_legacy.utility.RandomString;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLevelEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oshi.util.tuples.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarpetShadowLegacy implements CarpetExtension, ModInitializer {
    public static final Map<String, Pair<ItemStack, List<Pair<Container, Integer>>>> shadowMap = new HashMap<>();
    public static final Logger LOGGER = LogManager.getLogger("Carpet-Shadow-Legacy");
    public static RandomString shadow_id_generator = new RandomString(CarpetShadowLegacySettings.shadowItemIdSize);

    @Override
    public void onGameStarted() {
        CarpetShadowLegacy.LOGGER.info("Carpet Shadow Legacy Loaded!");
        CarpetServer.settingsManager.parseSettingsClass(CarpetShadowLegacySettings.class);
        shadow_id_generator = new RandomString(CarpetShadowLegacySettings.shadowItemIdSize);
    }

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(new CarpetShadowLegacy());
        new ShadowNBTData();
            ServerLevelEvents.LOAD.register((server, world) -> {
                for (Player player : world.players()) {
                    Globals.updateInventory(player.getInventory());
                    Globals.updateInventory(player.getEnderChestInventory());
                }
            });
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                Player player = handler.player;
                Globals.updateInventory(player.getInventory());
                Globals.updateInventory(player.getEnderChestInventory());
            });
            ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
                Globals.removeInventory(handler.player.getInventory());
                Globals.removeInventory(handler.player.getEnderChestInventory());
            });
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