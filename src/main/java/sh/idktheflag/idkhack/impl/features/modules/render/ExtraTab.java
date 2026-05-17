package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;

public class ExtraTab extends Module {
    public static ExtraTab INSTANCE;

    public ExtraTab() {
        super("ExtraTab", Category.Render);
        INSTANCE = this;
    }


    @Override
    public String getDescription() {
        return "ExtraTab: Modifies the tab so you can see everyone online";
    }
}
