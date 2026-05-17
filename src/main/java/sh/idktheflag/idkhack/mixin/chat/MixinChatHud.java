package sh.idktheflag.idkhack.mixin.chat;

import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import sh.idktheflag.idkhack.api.utils.ducks.IChatHud;
import sh.idktheflag.idkhack.api.utils.ducks.IChatHudLine;
import sh.idktheflag.idkhack.impl.features.modules.client.Manager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

@Mixin(ChatHud.class)
public abstract class MixinChatHud implements IChatHud
{
    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;
    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Unique
    private int nextId;

    @Shadow
    public abstract void addMessage(Text message);

    @Shadow
    public abstract void addMessage(Text message, MessageSignatureData a, MessageIndicator indicator);

    @Shadow
    public abstract double getChatScale();

    @Override
    public void addChatMessageWithId(Text message, int id)
    {
        addMessage(message, null, null);
    }


    @Override
    public void addChatMessageNoId(Text message)
    {
        addMessage(message, null, null);
    }
}
