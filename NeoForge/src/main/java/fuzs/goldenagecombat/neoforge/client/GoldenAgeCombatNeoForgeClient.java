package fuzs.goldenagecombat.neoforge.client;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.client.GoldenAgeCombatClient;
import fuzs.goldenagecombat.common.client.util.AttributeTooltipHelper;
import fuzs.goldenagecombat.common.config.ClientConfig;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.event.GatherSkippedAttributeTooltipsEvent;

@Mod(value = GoldenAgeCombat.MOD_ID, dist = Dist.CLIENT)
public class GoldenAgeCombatNeoForgeClient {

    public GoldenAgeCombatNeoForgeClient() {
        ClientModConstructor.construct(GoldenAgeCombat.MOD_ID, GoldenAgeCombatClient::new);
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        NeoForge.EVENT_BUS.addListener((GatherSkippedAttributeTooltipsEvent event) -> {
            if (GoldenAgeCombat.CONFIG.get(ClientConfig.class).attributesStyle
                    != ClientConfig.AttributesStyle.VANILLA) {
                event.setSkipAll(true);
            }
        });
        NeoForge.EVENT_BUS.addListener((AddAttributeTooltipsEvent event) -> {
            if (GoldenAgeCombat.CONFIG.get(ClientConfig.class).attributesStyle != ClientConfig.AttributesStyle.LEGACY) {
                return;
            }

            if (event.shouldShow()) {
                AttributeTooltipHelper.addLegacyAttributeTooltips(event.getStack(),
                        event::addTooltipLines,
                        event.getContext().tooltipDisplay(),
                        event.getContext().player());
            }
        });
    }
}
