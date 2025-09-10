package com.ksandr707.carpet_shadow_legacy.mixins.inv_updates;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacy;
import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "tick", at=@At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V", shift = At.Shift.AFTER))
    public void afterWorldTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci){
        try{
            if(CarpetShadowLegacySettings.shadowItemUpdateFix) {
                for (Inventory inv : Globals.inventoriesToMarkDirty) {
                    try {
                        inv.markDirty();
                    } catch (Throwable ex) {
                        CarpetShadowLegacy.LOGGER.error("Caught Exception while propagating shadow stack updates: ", ex);
                    }
                }
            }
        }catch (Throwable error){
            CarpetShadowLegacy.LOGGER.error("Caught Exception while propagating shadow stack updates: ",error);
        }finally {
            Globals.inventoriesToMarkDirty.clear();
        }
    }
}