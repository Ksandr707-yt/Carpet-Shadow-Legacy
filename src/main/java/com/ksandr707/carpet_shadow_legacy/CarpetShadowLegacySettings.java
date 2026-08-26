package com.ksandr707.carpet_shadow_legacy;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import com.ksandr707.carpet_shadow_legacy.utility.RandomString;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

import static carpet.api.settings.RuleCategory.*;

public class CarpetShadowLegacySettings {
    public static final String SHADOW = "shadow_items";
    @Rule( categories = {SHADOW}, validators = {IdSizeValidator.class})
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

    private static class IdSizeValidator extends Validator<Integer> {
        @Override
        public Integer validate(@Nullable CommandSourceStack source, CarpetRule<Integer> changingRule, Integer newValue, String userInput) {
            try {
                CarpetShadowLegacy.shadow_id_generator = new RandomString(newValue);
                return newValue;
            } catch (IllegalArgumentException ex) {
                return changingRule.value();
            }
        }
    }
}