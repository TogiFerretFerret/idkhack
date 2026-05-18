package sh.idktheflag.idk.impl.features.modules.client;

import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.impl.gui.AccountScreen;

public class AccountsModule extends Module {
    public static AccountsModule INSTANCE;

    public AccountsModule() {
        super("Accounts", Category.Client);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        mc.setScreen(new AccountScreen(mc.currentScreen));
        setEnabled(false);
    }
}
