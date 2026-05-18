package sh.idktheflag.idk.impl.features.modules.misc;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.world.EntityEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.SavableManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.chat.ChatUtils;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EntityLogger extends Module {
    public static EntityLogger INSTANCE;

    public EntityLogger() {
        super("EntityLogger", Category.Misc);
        INSTANCE = this;
    }

    Value<Boolean> playersOnly = new ValueBuilder<Boolean>()
            .withDescriptor("Players Only")
            .withValue(true)
            .register(this);

    Value<Boolean> logToFile = new ValueBuilder<Boolean>()
            .withDescriptor("Log to File")
            .withValue(true)
            .register(this);

    @SubscribeEvent
    public void onEntityAdd(EntityEvent.Add event) {
        if (NullUtils.nullCheck()) return;

        Entity entity = event.getEntity();
        if (playersOnly.getValue() && !(entity instanceof PlayerEntity)) return;
        if (entity == mc.player) return;

        String msg = Formatting.GREEN + "[+] " + entity.getName().getString() + " at " + entity.getBlockPos().toShortString();
        ChatUtils.sendMessage(msg);

        if (logToFile.getValue()) {
            log("[+] " + entity.getName().getString() + " at " + entity.getBlockPos().toShortString());
        }
    }

    @SubscribeEvent
    public void onEntityRemove(EntityEvent.Remove event) {
        if (NullUtils.nullCheck()) return;

        Entity entity = event.getEntity();
        if (playersOnly.getValue() && !(entity instanceof PlayerEntity)) return;
        if (entity == mc.player) return;

        String msg = Formatting.RED + "[-] " + entity.getName().getString() + " at " + entity.getBlockPos().toShortString();
        ChatUtils.sendMessage(msg);

        if (logToFile.getValue()) {
            log("[-] " + entity.getName().getString() + " at " + entity.getBlockPos().toShortString());
        }
    }

    private void log(String text) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SavableManager.MAIN_FOLDER + "/entity_log.txt", true))) {
            writer.println("[" + System.currentTimeMillis() + "] " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        return "EntityLogger: Logs when entities enter or leave your render distance";
    }
}
