package sh.idktheflag.idkhack.impl.features.modules.misc;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.network.ServerEvent;
import sh.idktheflag.idkhack.api.event.events.render.ScreenEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.Pair;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.mixin.accessor.IDeathScreen;
import sh.idktheflag.idkhack.mixin.accessor.IDisconnectedScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.text.Text;

import java.awt.*;

public class AutoReconnect extends Module {


    public AutoReconnect()
    {
        super("AutoReconnect", Category.Misc);
    }

    Value<Number> distance = new ValueBuilder<Number>()
            .withDescriptor("Time")
            .withValue(1)
            .withRange(1, 10)
            .register(this);

    public Pair<ServerAddress, ServerInfo> lastServerConnection;

    @SubscribeEvent
    public void onAttempt(ServerEvent.AttemptConnect event)
    {
        lastServerConnection = new Pair<>(event.address, event.info);
    }

    @SubscribeEvent
    public void onGuiOpen(ScreenEvent.SetScreen event)
    {
        if (event.getGuiScreen() instanceof DisconnectedScreen)
            event.setGuiScreen(new AutoReconnectScreen((IDisconnectedScreen) event.getGuiScreen()));
    }


    public class AutoReconnectScreen extends DisconnectedScreen {
        private final long finalTime;

        public AutoReconnectScreen(IDisconnectedScreen screen)
        {
            super(screen.getParent(), Text.of("AutoReconnect"), screen.getDisconnectionInfo());
            finalTime = System.currentTimeMillis() + (distance.getValue().longValue() * 1000);
        }

        public void render(DrawContext context, int mouseX, int mouseY, float delta)
        {
            super.render(context, mouseX, mouseY, delta);
            long time = finalTime - System.currentTimeMillis();

            if (lastServerConnection == null)
            {
                return;
            }
            if (time <= 0)
                ConnectScreen.connect(new TitleScreen(), mc, lastServerConnection.key(), lastServerConnection.value(), false, null);

            String text = "Reconnecting in " + (time / 1000) + "s";
            context.drawText(mc.textRenderer, text, width / 2 - mc.textRenderer.getWidth(text) / 2, 8, Color.WHITE.getRGB(), true);
        }
    }

    @Override
    public String getDescription()
    {
        return "AutoReconnect: automatically rejoins the server if you get kicked/disconnected";
    }

}
