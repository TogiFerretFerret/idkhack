package sh.idktheflag.idk.impl.features.modules.misc;


import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import sh.idktheflag.idk.impl.IdkHackMod;
import sh.idktheflag.idk.mixin.accessor.IChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;

public class ChatSuffix extends Module {

    Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("IDKHACK")
            .register(this);

    public ChatSuffix()
    {
        super("ChatSuffix", Category.Misc);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event)
    {
        if (NullUtils.nullCheck()) return;


        if (event.getPacket() instanceof ChatMessageC2SPacket packet)
        {
            if (allowMessage(packet.chatMessage()))
            {
                String newMessage = getText(packet.chatMessage());
                if (newMessage.length() >= 254) return;

                ((IChatMessageC2SPacket) event.getPacket()).setMessage(newMessage);
            }
        }
    }

    public String getText(String message)
    {

        if (mode.getValue().equals("IDKHACK"))
        {
            return message + " " + IdkHackMod.NAME_UNICODE;
        } else
        {
            return message + ChatUtils.hephaestus(" | " + mode.getValue().toLowerCase());
        }

    }

    boolean allowMessage(String message)
    {

        boolean allow = true;

        for (String s : filters)
        {
            if (message.startsWith(s))
            {
                allow = false;
                break;
            }
        }

        return allow;
    }

    String[] filters = new String[]{
            ".",
            "/",
            ",",
            ":",
            "`",
            "-"
    };
    @Override
    public String getDescription()
    {
        return "ChatSuffix: append stuff after ur chat message";
    }


}
