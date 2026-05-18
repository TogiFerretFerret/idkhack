package sh.idktheflag.idkhack.api.management.accounts.types;

import sh.idktheflag.idkhack.api.management.accounts.Account;
import sh.idktheflag.idkhack.api.management.accounts.AccountType;
import net.minecraft.client.session.Session;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

public class CrackedAccount extends Account<CrackedAccount> {
    public CrackedAccount(String name) {
        super(AccountType.Cracked, name);
    }

    @Override
    public boolean fetchInfo() {
        cache.username = name;
        cache.uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8)).toString();
        return true;
    }

    @Override
    public boolean login() {
        setSession(new Session(name, UUID.fromString(cache.uuid), "", Optional.empty(), Optional.empty()));
        return true;
    }
}
