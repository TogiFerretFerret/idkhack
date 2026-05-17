package sh.idktheflag.idkhack.impl.features.modules.misc;

import sh.idktheflag.idkhack.api.feature.module.Module;

public class MultiTask extends Module {
    public static MultiTask INSTANCE;

    public MultiTask() {
        super("MultiTask", Category.Misc);
        INSTANCE = this;
    }

    @Override
    public String getDescription() {
        return "MultiTask: Do more then one thing at once. IE: eating and mining";
    }

}