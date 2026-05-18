package sh.idktheflag.idk.api.management.accounts.types;

import sh.idktheflag.idk.api.management.accounts.Account;
import sh.idktheflag.idk.api.management.accounts.AccountType;
import sh.idktheflag.idk.api.management.accounts.MicrosoftLogin;
import net.minecraft.client.session.Session;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MicrosoftAccount extends Account<MicrosoftAccount> {
    private String refreshToken;

    public MicrosoftAccount(String refreshToken) {
        super(AccountType.Microsoft, "");
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean fetchInfo() {
        MicrosoftLogin.LoginData loginData = MicrosoftLogin.login(refreshToken);
        if (loginData.isGood()) {
            cache.username = loginData.username;
            cache.uuid = loginData.uuid;
            return true;
        }
        return false;
    }

    @Override
    public boolean login() {
        MicrosoftLogin.LoginData loginData = MicrosoftLogin.login(refreshToken);
        if (loginData.isGood()) {
            refreshToken = loginData.newRefreshToken;
            cache.username = loginData.username;
            cache.uuid = loginData.uuid;

            setSession(new Session(loginData.username, parseUuid(loginData.uuid), loginData.mcToken, Optional.empty(), Optional.empty()));
            return true;
        }
        return false;
    }

    private UUID parseUuid(String uuid) {
        if (uuid.contains("-")) return UUID.fromString(uuid);
        return UUID.fromString(uuid.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
    }

    @Override
    public Map<String, Object> save() {
        Map<String, Object> map = super.save();
        map.put("refreshToken", refreshToken);
        return map;
    }

    @Override
    public MicrosoftAccount load(Map<String, Object> map) {
        super.load(map);
        refreshToken = (String) map.get("refreshToken");
        return this;
    }
}
