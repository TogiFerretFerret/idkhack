package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.PacketManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BookBot extends Module {
    public static BookBot INSTANCE;

    public BookBot() {
        super("BookBot", Category.Misc);
        INSTANCE = this;
    }

    Value<Number> pagesCount = new ValueBuilder<Number>()
            .withDescriptor("Pages")
            .withValue(50)
            .withRange(1, 100)
            .register(this);

    private final Random rand = new Random();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) return;

        if (mc.player.getMainHandStack().getItem() == Items.WRITABLE_BOOK) {
            List<String> pages = new ArrayList<>();
            for (int i = 0; i < pagesCount.getValue().intValue(); i++) {
                pages.add(generateRandomText());
            }

            PacketManager.INSTANCE.sendPacket(new BookUpdateC2SPacket(mc.player.getInventory().selectedSlot, pages, Optional.of("idk bot")));
            toggle();
        }
    }

    private String generateRandomText() {
        StringBuilder sb = new Random().ints(100, 32, 126)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
        return sb.toString();
    }

    @Override
    public String getDescription() {
        return "BookBot: Automatically write random text in books";
    }
}
