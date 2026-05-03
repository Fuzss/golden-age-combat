package fuzs.goldenagecombat.common.mixin.client;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.config.ClientConfig;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
abstract class GuiMixin {

    @ModifyVariable(method = "extractHearts", at = @At("HEAD"), ordinal = 5, argsOnly = true)
    public int renderHearts(int oldHealth) {
        if (!GoldenAgeCombat.CONFIG.get(ClientConfig.class).noFlashingHearts) {
            return oldHealth;
        }

        return 0;
    }
}
