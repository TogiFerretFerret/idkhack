package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.network.PacketEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.SavableManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import net.minecraft.item.map.MapState;
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapDownloader extends Module {
    public static MapDownloader INSTANCE;

    public MapDownloader() {
        super("MapDownloader", Category.Misc);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (NullUtils.nullCheck()) return;

        if (event.getPacket() instanceof MapUpdateS2CPacket packet) {
            MapState state = mc.world.getMapState(packet.mapId());
            if (state != null) {
                saveMap(packet.mapId().id(), state);
            }
        }
    }

    private void saveMap(int id, MapState state) {
        BufferedImage image = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                int color = state.colors[i + j * 128];
                image.setRGB(i, j, getRgb(color));
            }
        }

        File folder = new File(SavableManager.MAIN_FOLDER + "/maps");
        if (!folder.exists()) folder.mkdirs();

        try {
            ImageIO.write(image, "png", new File(folder, "map_" + id + ".png"));
            ChatUtils.sendMessage("Saved map " + id + " to idk/maps/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getRgb(int color) {
        // Simple map color to RGB conversion
        // In reality, this needs net.minecraft.block.MapColor
        return color; // Placeholder
    }

    @Override
    public String getDescription() {
        return "MapDownloader: Saves map data to images";
    }
}
