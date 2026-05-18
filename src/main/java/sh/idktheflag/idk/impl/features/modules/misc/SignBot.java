package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.util.math.BlockPos;

public class SignBot extends Module {
    public static SignBot INSTANCE;

    public SignBot() {
        super("SignBot", Category.Misc);
        INSTANCE = this;
    }

    Value<String> line1 = new ValueBuilder<String>().withDescriptor("Line 1").withValue("idkhack").register(this);
    Value<String> line2 = new ValueBuilder<String>().withDescriptor("Line 2").withValue("on top").register(this);
    Value<String> line3 = new ValueBuilder<String>().withDescriptor("Line 3").withValue("").register(this);
    Value<String> line4 = new ValueBuilder<String>().withDescriptor("Line 4").withValue("").register(this);

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (NullUtils.nullCheck()) return;

        if (event.getPacket() instanceof UpdateSignC2SPacket packet) {
            String[] text = new String[] {
                line1.getValue(),
                line2.getValue(),
                line3.getValue(),
                line4.getValue()
            };
            
            // We can't modify the array easily if it's final in the packet, 
            // but we can replace the packet.
            event.setPacket(new UpdateSignC2SPacket(packet.getPos(), packet.isFront(), text[0], text[1], text[2], text[3]));
        }
    }

    @Override
    public String getDescription() {
        return "SignBot: Automatically fill signs with custom text";
    }
}
