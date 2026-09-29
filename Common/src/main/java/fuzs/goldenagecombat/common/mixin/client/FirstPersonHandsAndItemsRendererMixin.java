package fuzs.goldenagecombat.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.config.ClientConfig;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
abstract class FirstPersonHandsAndItemsRendererMixin {

    @Inject(method = "submitArmWithItem",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/renderer/FirstPersonHandsAndItemsRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
                     shift = At.Shift.AFTER))
    private void submitArmWithItem(PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, float partialTicks, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo callback) {
        if (!GoldenAgeCombat.CONFIG.get(ClientConfig.class).interactAnimations) {
            return;
        }

        AvatarRenderState avatarRenderState = playerState.avatarRenderState;
        if (avatarRenderState != null && avatarRenderState.isUsingItem && state.useItemRemainingTicks > 0
                && avatarRenderState.useItemHand == hand) {
            HumanoidArm humanoidArm =
                    hand == InteractionHand.MAIN_HAND ? avatarRenderState.mainArm : avatarRenderState.mainArm.getOpposite();
            this.applyItemArmAttackTransform(poseStack, humanoidArm, attack);
        }
    }

    @Shadow
    private void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm arm, float attackValue) {
        throw new RuntimeException();
    }
}
