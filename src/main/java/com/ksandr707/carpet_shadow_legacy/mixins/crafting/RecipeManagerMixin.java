package com.ksandr707.carpet_shadow_legacy.mixins.crafting;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.gson.JsonElement;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;", shift = At.Shift.BY, by=2))
    private void addShadowRecipe(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci, @Local(ordinal = 0) ImmutableMultimap.Builder<RecipeType<?>, RecipeEntry<?>> builder, @Local(ordinal = 0) ImmutableMap.Builder<Identifier, RecipeEntry<?>> builder2){
        Identifier identifier = Identifier.of("carpet_shadow","shadow_recipe");
        Recipe<?> recipe = new BookCloningRecipe(CraftingRecipeCategory.MISC) {
            @Override
            public boolean matches(CraftingRecipeInput inventory, World world) {
                if (!CarpetShadowLegacySettings.shadowCraftingGeneration)
                    return false;
                boolean enderchest = false;
                List<ItemStack> stacks = new ArrayList<>();
                int count = 0;
                for(int i = 0; i < inventory.getSize(); ++i) {
                    ItemStack itemStack2 = inventory.getStackInSlot(i);
                    if (!itemStack2.isEmpty()) {
                        if (itemStack2.getItem().equals(Items.ENDER_CHEST) && !enderchest)
                            enderchest = true;
                        else {
                            stacks.add(itemStack2);
                        }
                        count++;
                    }
                }
                return enderchest && count == 2;
            }

            @Override
            public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup wrapperLookup) {
                if (!CarpetShadowLegacySettings.shadowCraftingGeneration)
                    return ItemStack.EMPTY;

                ItemStack item = null;
                ItemStack enderchest = null;
                for(int i = 0; i < inventory.getSize(); ++i) {
                    ItemStack itemStack2 = inventory.getStackInSlot(i);
                    if (!itemStack2.isEmpty()) {
                        if (itemStack2.getItem().equals(Items.ENDER_CHEST) && itemStack2.getCount() == 1) {
                            if (enderchest != null)
                                item = enderchest;
                            enderchest = itemStack2;
                        }else
                            item = itemStack2;
                    }
                }
                if (item==null || enderchest==null)
                    return ItemStack.EMPTY;

                String id = ((ShadowItem)(Object)item).getShadowId();
                if (id == null){
                    id = CarpetShadowLegacy.shadow_id_generator.nextString();
                }
                return Globals.getByIdOrAdd(id, item);
            }

            @Override
            public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput inventory) {
                ItemStack item = null;
                ItemStack enderchest = null;
                for(int i = 0; i < inventory.getSize(); ++i) {
                    ItemStack itemStack2 = inventory.getStackInSlot(i);
                    if (!itemStack2.isEmpty()) {
                        if (itemStack2.getItem().equals(Items.ENDER_CHEST)) {
                            if (enderchest != null)
                                item = enderchest;
                            enderchest = itemStack2;
                        }else
                            item = itemStack2;
                    }
                }

                if (item != null && enderchest != null)
                    item.setCount(item.getCount() + 1);

                return super.getRemainder(inventory);
            }

            @Override
            public boolean fits(int width, int height) {
                if (!CarpetShadowLegacySettings.shadowCraftingGeneration)
                    return false;
                return width * height >= 2;
            }
        };
        RecipeEntry<?> recipeEntry = new RecipeEntry<>(identifier, recipe);
        builder.put(recipe.getType(), recipeEntry);
        builder2.put(identifier, recipeEntry);
    }
}