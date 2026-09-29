package fuzs.goldenagecombat.common.mixin.client;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItems.class)
abstract class FirstPersonHandsAndItemsMixin {

    @Inject(method = "itemUsed", at = @At("HEAD"), cancellable = true)
    public void itemUsed(InteractionHand hand, CallbackInfo callback) {
        if (!GoldenAgeCombat.CONFIG.get(ClientConfig.class).noReequipWhenUsing) {
            return;
        }

        // don't play the re-equip animation when beginning to use an item, like shield or bow
        if (Minecraft.getInstance().player.isUsingItem() && Minecraft.getInstance().player.getUsedItemHand() == hand) {
            callback.cancel();
        }
    }
}
