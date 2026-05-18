package sh.idktheflag.idk.api.management.accounts;

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.minecraft.UserApiService;
import sh.idktheflag.idk.api.wrapper.IMinecraft;
import sh.idktheflag.idk.mixin.accessor.IMinecraftClient;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.session.ProfileKeys;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.report.AbuseReportContext;
import net.minecraft.client.session.report.ReporterEnvironment;

import java.util.HashMap;
import java.util.Map;

public abstract class Account<T extends Account<?>> implements IMinecraft {
    protected AccountType type;
    protected String name;
    protected final AccountCache cache;

    protected Account(AccountType type, String name) {
        this.type = type;
        this.name = name;
        this.cache = new AccountCache();
    }

    public abstract boolean fetchInfo();

    public boolean login() {
        return true;
    }

    public String getUsername() {
        if (cache.username.isEmpty()) return name;
        return cache.username;
    }

    public AccountType getType() {
        return type;
    }

    public AccountCache getCache() {
        return cache;
    }

    public static void setSession(Session session) {
        IMinecraftClient mca = (IMinecraftClient) mc;
        mca.setSession(session);

        YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(mca.getProxy());
        UserApiService apiService = yggdrasilAuthenticationService.createUserApiService(session.getAccessToken());

        mca.setUserApiService(apiService);
        mca.setSocialInteractionsManager(new SocialInteractionsManager(mc, apiService));
        mca.setProfileKeys(ProfileKeys.create(apiService, session, mc.runDirectory.toPath()));
        mca.setAbuseReportContext(AbuseReportContext.create(ReporterEnvironment.ofIntegratedServer(), apiService));
    }

    public Map<String, Object> save() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", type.name());
        map.put("name", name);
        map.put("cache", cache.save());
        return map;
    }

    @SuppressWarnings("unchecked")
    public T load(Map<String, Object> map) {
        if (map == null) return (T) this;
        name = (String) map.getOrDefault("name", name);
        if (map.containsKey("cache")) {
            cache.load((Map<String, Object>) map.get("cache"));
        }
        return (T) this;
    }
}
