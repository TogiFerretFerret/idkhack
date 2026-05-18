package sh.idktheflag.idkhack.impl.features.modules.client;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.impl.gui.AccountScreen;

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
