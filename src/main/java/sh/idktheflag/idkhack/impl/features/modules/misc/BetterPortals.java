package sh.idktheflag.idkhack.impl.features.modules.misc;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;

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
