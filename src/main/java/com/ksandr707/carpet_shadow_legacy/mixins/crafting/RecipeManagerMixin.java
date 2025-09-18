package com.ksandr707.carpet_shadow_legacy.mixins.crafting;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacy;
import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

@Mixin(ServerRecipeManager.class)
public abstract class RecipeManagerMixin {

    @Inject(
            method = "prepare*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/JsonDataLoader;load(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/resource/ResourceFinder;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;Ljava/util/Map;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void addShadowRecipe(
            ResourceManager resourceManager,
            Profiler profiler,
            CallbackInfoReturnable<PreparedRecipes> cir,
            @Local SortedMap<Identifier, Recipe<?>> sortedMap
    ) {
        Identifier identifier = Identifier.of("carpet_shadow_legacy", "shadow_recipe");
        Recipe<?> recipe = new BookCloningRecipe(CraftingRecipeCategory.MISC) {
            @Override
            public boolean matches(CraftingRecipeInput inventory, World world) {
                if (!CarpetShadowLegacySettings.shadowCraftingGeneration)
                    return false;
                boolean enderchest = false;
                List<ItemStack> stacks = new ArrayList<>();
                int count = 0;
                for (int i = 0; i < inventory.size(); ++i) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        if (stack.isOf(Items.ENDER_CHEST) && !enderchest) {
                            enderchest = true;
                        }
                        count++;
                    }
                }
                return enderchest && count == 2;
            }

            @Override
            public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup wrapperLookup) {
                if (!CarpetShadowLegacySettings.shadowCraftingGeneration){
                    return ItemStack.EMPTY;
                }

                ItemStack item = null;
                ItemStack enderchest = null;
                for (int i = 0; i < inventory.size(); ++i) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        if (stack.isOf(Items.ENDER_CHEST)) {
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
            public DefaultedList<ItemStack> getRecipeRemainders(CraftingRecipeInput inventory) {
                DefaultedList<ItemStack> remainders = super.getRecipeRemainders(inventory);
                if (!CarpetShadowLegacySettings.shadowCraftingGeneration) return remainders;

                ItemStack item = null;
                ItemStack enderchest = null;
                for (int i = 0; i < inventory.size(); ++i) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        if (stack.isOf(Items.ENDER_CHEST)) {
                            if (enderchest != null) item = enderchest;
                            enderchest = stack;
                        } else {
                            item = stack;
                        }
                }

                if (item != null)
                    item.setCount(item.getCount() + 1);

                }
                return remainders;
            }
        };
        sortedMap.put(identifier, recipe);
    }
}