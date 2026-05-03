package fuzs.goldenagecombat.neoforge.client;

import fuzs.goldenagecombat.common.GoldenAgeCombat;
import fuzs.goldenagecombat.common.client.GoldenAgeCombatClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = GoldenAgeCombat.MOD_ID, dist = Dist.CLIENT)
public class GoldenAgeCombatNeoForgeClient {

    public GoldenAgeCombatNeoForgeClient() {
        ClientModConstructor.construct(GoldenAgeCombat.MOD_ID, GoldenAgeCombatClient::new);
    }
}
