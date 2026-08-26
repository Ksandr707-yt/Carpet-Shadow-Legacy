package com.ksandr707.carpet_shadow_legacy.mixins.crafting;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacy;
import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.SortedMap;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Inject(
            method = "prepare*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/packs/resources/SimpleJsonResourceReloadListener;scanDirectory(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/resources/FileToIdConverter;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void addShadowRecipe(
            ResourceManager resourceManager,
            ProfilerFiller profiler,
            CallbackInfoReturnable<RecipeMap> cir,
            @Local SortedMap<Identifier, Recipe<?>> sortedMap
    ) {
        Identifier identifier = Identifier.fromNamespaceAndPath("carpet_shadow_legacy", "shadow_recipe");
        Recipe<?> recipe = new CustomRecipe() {
            @Override
            public RecipeSerializer<? extends CustomRecipe> getSerializer() {
                return BookCloningRecipe.SERIALIZER;
            }

            @Override
            public boolean matches(CraftingInput inventory, Level world) {
                if (!CarpetShadowLegacySettings.shadowCraftingGeneration)
                    return false;
                boolean enderchest = false;
                int count = 0;
                for (int i = 0; i < inventory.size(); ++i) {
                    ItemStack stack = inventory.getItem(i);
                    if (!stack.isEmpty()) {
                        if (stack.is(Items.ENDER_CHEST) && !enderchest && stack.getCount() == 1) {
                            enderchest = true;
                        }
                        count++;
                    }
                }
                return enderchest && count == 2;
            }

            @Override
            public ItemStack assemble(CraftingInput inventory) {
                if (!CarpetShadowLegacySettings.shadowCraftingGeneration){
                    return ItemStack.EMPTY;
                }

                ItemStack item = null;
                ItemStack enderchest = null;
                for (int i = 0; i < inventory.size(); ++i) {
                    ItemStack stack = inventory.getItem(i);
                    if (!stack.isEmpty()) {
                        if (stack.is(Items.ENDER_CHEST)) {
                            if (enderchest != null) item = enderchest;
                            enderchest = stack;
                        } else {
                            item = stack;
                        }
                    }
                }
                if (item == null || enderchest == null) return ItemStack.EMPTY;

                String id = ((ShadowItem) (Object) item).getShadowId();
                if (id == null) {
                    id = CarpetShadowLegacy.shadow_id_generator.nextString();
                }
                return Globals.getByIdOrAdd(id, item);
            }

            @Override
            public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
                NonNullList<ItemStack> remainders = super.getRemainingItems(inventory);
                if (!CarpetShadowLegacySettings.shadowCraftingGeneration) return remainders;

                ItemStack item = null;
                ItemStack enderchest = null;
                for (int i = 0; i < inventory.size(); ++i) {
                    ItemStack stack = inventory.getItem(i);
                    if (!stack.isEmpty()) {
                        if (stack.is(Items.ENDER_CHEST)) {
                            if (enderchest != null) item = enderchest;
                            enderchest = stack;
                        } else {
                            item = stack;
                        }
                }

                if (item != null && enderchest != null)
                    item.setCount(item.getCount() + 1);

                }
                return remainders;
            }
        };
        sortedMap.put(identifier, recipe);
    }
}