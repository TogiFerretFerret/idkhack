package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;

public class BetterPortals extends Module {
    public static BetterPortals INSTANCE;


    public BetterPortals()
    {
        super("BetterPortals", Category.Misc);
        INSTANCE = this;
    }


    @Override
    public String getDescription()
    {
        return "BetterPortals: lets you open guis in portals";
    }
}
