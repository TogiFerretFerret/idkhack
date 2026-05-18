package sh.idktheflag.idkhack.mixin.accessor;

import com.mojang.authlib.minecraft.UserApiService;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.SocialInteractionsManager;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.resource.ResourceReloadLogger;
import net.minecraft.client.session.ProfileKeys;
import net.minecraft.client.session.Session;
import net.minecraft.client.session.report.AbuseReportContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.net.Proxy;

@Mixin(MinecraftClient.class)
public interface IMinecraftClient {
    @Accessor("currentFps")
    static int getFps()
    {
        return 0;
    }

    @Mutable
    @Accessor("session")
    void setSession(Session session);

    @Mutable
    @Accessor("userApiService")
    void setUserApiService(UserApiService userApiService);

    @Mutable
    @Accessor("socialInteractionsManager")
    void setSocialInteractionsManager(SocialInteractionsManager socialInteractionsManager);

    @Mutable
    @Accessor("profileKeys")
    void setProfileKeys(ProfileKeys profileKeys);

    @Mutable
    @Accessor("abuseReportContext")
    void setAbuseReportContext(AbuseReportContext abuseReportContext);

    @Accessor("networkProxy")
    Proxy getProxy();

    @Accessor("itemUseCooldown")
    int getItemUseCooldown();
    @Accessor("itemUseCooldown")
    void setItemUseCooldown(int itemUseCooldown);
    @Invoker
    boolean callDoAttack();
 
    @Invoker
    void callDoItemUse();

    @Accessor("resourceReloadLogger")
    ResourceReloadLogger getResourceReloadLogger();

    @Accessor("renderTickCounter")
    RenderTickCounter.Dynamic getRenderTickCounter();


    @Invoker("doAttack")
    boolean leftClick();
}
