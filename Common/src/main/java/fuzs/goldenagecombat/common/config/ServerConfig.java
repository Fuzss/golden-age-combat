package fuzs.goldenagecombat.common.config;

import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config
    public final FishingRodConfig fishingRod = new FishingRodConfig();
    @Config(description = "Health only regenerates every 4 seconds, while requiring 18 or more food points. Surplus saturation does not yield quick health regeneration.")
    public boolean legacyFoodMechanics = false;
    @Config(description = "Player is knocked back by attacks which do not cause any damage, such as when hit by snowballs, eggs, and fishing rod hooks.")
    public boolean weakAttackKnockBack = true;
    @Config(description = "Sprinting and attacking no longer interfere with each other, making critical hits possible at all times.")
    public boolean criticalHitsWhileSprinting = true;
    @Config(description = "Give Regeneration V and Absorption I instead of Regeneration II and Absorption IV after consuming a notch apple.")
    public boolean goldenAppleEffects = true;
    @Config(description = "Expand all entity hitboxes by 10%, making hitting a target possible from a slightly greater range and with much increased accuracy.")
    public boolean inflateHitboxes = true;
    @Config(description = "Allow using the \"Attack\" button while the \"Use Item\" button is held for mining blocks. Does not make a lot of sense, but it used to be a feature in old pvp.")
    public boolean interactWhileUsing = false;
    @Config(description = "Makes knockback stronger towards targets not on the ground (does not apply when in water).")
    public boolean upwardsKnockback = true;
    @Config(description = "Is the sweeping edge enchantment required to perform a sweep attack.")
    public boolean requireSweepingEdge = true;
    @Config(description = "Attacking will no longer stop the player from sprinting. Very useful when swimming, so you can fight underwater without being stopped on every hit.")
    public boolean sprintAttacks = true;

    public static class FishingRodConfig implements ConfigCore {
        @Config(description = "Fishing rod deals knockback upon hitting an entity.")
        public boolean causeKnockback = true;
        @Config(description = "Entities reeled in using a fishing rod are slightly launched upwards.")
        public boolean launchEntities = true;
        @Config(description = "Hooking entities with a fishing rod causes only 3 damage points to the rod instead of 5.")
        public boolean slowerBreaking = true;
    }
}
