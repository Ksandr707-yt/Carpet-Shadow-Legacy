package com.ksandr707.carpet_shadow_legacy;

import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.*;

public class CarpetShadowLegacySettings {
    public static final String SHADOW = "shadow_items";
    @Rule( categories = {SHADOW, BUGFIX})
    public static Mode shadowItemMode = Mode.PERSIST;
    @Rule( categories = {SHADOW})
    public static int shadowItemIdSize = 5;
    @Rule( categories = {SHADOW, FEATURE})
    public static boolean shadowSuppressionGeneration = false;
    @Rule( categories = {SHADOW, FEATURE})
    public static boolean shadowCraftingGeneration = false;
    @Rule( categories = {SHADOW, FEATURE})
    public static boolean shadowItemTooltip = false;
    @Rule( categories = {SHADOW, BUGFIX})
    public static boolean shadowItemInventoryFragilityFix = false;
    @Rule( categories = {SHADOW, BUGFIX})
    public static boolean shadowItemTransferFragilityFix = false;
    @Rule( categories = {SHADOW, BUGFIX, EXPERIMENTAL})
    public static boolean shadowItemUpdateFix = false;
    @Rule( categories = {SHADOW, OPTIMIZATION, FEATURE})
    public static boolean shadowItemPreventCombine = false;
    @Rule( categories = {SHADOW, OPTIMIZATION, FEATURE})
    public static boolean shadowItemUseFix = false;

    public enum Mode{
        UNLINK(false,false),
        PERSIST(true,false),
        VANISH(true,true);

        private final boolean shouldLoadItem;
        private final boolean shouldResetCount;

        public boolean shouldLoadItem() {
            return shouldLoadItem;
        }
        public boolean shouldResetCount() {
            return shouldResetCount;
        }

        Mode(boolean shouldLoadItem, boolean shouldResetCount) {
            this.shouldLoadItem = shouldLoadItem;
            this.shouldResetCount = shouldResetCount;
        }
    }
}