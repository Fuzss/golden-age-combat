package fuzs.goldenagecombat.neoforge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Either;
import fuzs.goldenagecombat.neoforge.GoldenAgeCombatNeoForge;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.resources.RegistryLoadTask;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceManagerRegistryLoadTask;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * NeoForge equivalent of Fabric's {@code net.fabricmc.fabric.impl.item.EnchantmentUtil#modify} hooked into
 * {@code ResourceManagerRegistryLoadTask}, so that enchantments loaded from the data pack can be modified in place.
 */
@Mixin(ResourceManagerRegistryLoadTask.class)
abstract class ResourceManagerRegistryLoadTaskNeoForgeMixin {
    @Unique
    private RegistryOps.RegistryInfoLookup goldenagecombat$registryInfoLookup;

    @Inject(method = "load", at = @At("HEAD"))
    private void goldenagecombat$captureRegistries(RegistryOps.RegistryInfoLookup context, Executor executor, CallbackInfoReturnable<CompletableFuture<?>> callback) {
        this.goldenagecombat$registryInfoLookup = context;
    }

    @SuppressWarnings("unchecked")
    @WrapOperation(method = "lambda$load$2",
                   at = @At(value = "NEW", target = "net/minecraft/resources/RegistryLoadTask$PendingRegistration"))
    private <T> RegistryLoadTask.PendingRegistration<T> goldenagecombat$modifyEnchantment(ResourceKey<T> key, Either<T, Exception> value, RegistrationInfo info, Operation<RegistryLoadTask.PendingRegistration<T>> operation) {
        if (value.left().isPresent()) {
            T leftValue = value.left().get();
            if (leftValue instanceof Enchantment enchantment) {
                T modified = (T) GoldenAgeCombatNeoForge.modifyEnchantment((ResourceKey<Enchantment>) key,
                        enchantment,
                        this.goldenagecombat$registryInfoLookup);
                if (modified != null) {
                    // clear the known pack info to force the server to sync the modified data pack to the client
                    info = new RegistrationInfo(Optional.empty(), info.lifecycle());
                    value = Either.left(modified);
                }
            }
        }

        return operation.call(key, value, info);
    }
}
