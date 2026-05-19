package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.event.events.world.ChunkDataEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.management.SavableManager;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.utils.render.world.RenderType;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StashFinder extends Module {
    public static StashFinder INSTANCE;

    public StashFinder() {
        super("StashFinder", Category.Render);
        INSTANCE = this;
    }

    Value<Number> minStorage = new ValueBuilder<Number>()
            .withDescriptor("Min Storage")
            .withValue(4)
            .withRange(1, 20)
            .register(this);

    Value<IdkColor> color = new ValueBuilder<IdkColor>()
            .withDescriptor("Color")
            .withValue(new IdkColor(255, 255, 0, 100))
            .register(this);

    private final Map<ChunkPos, Integer> stashes = new ConcurrentHashMap<>();

    @SubscribeEvent
    public void onChunkData(ChunkDataEvent event) {
        if (NullUtils.nullCheck()) return;

        int count = 0;
        for (BlockEntity be : event.getChunk().getBlockEntities().values()) {
            if (be instanceof ChestBlockEntity || be instanceof EnderChestBlockEntity || be instanceof ShulkerBoxBlockEntity || be instanceof BarrelBlockEntity) {
                count++;
            }
        }

        if (count >= minStorage.getValue().intValue()) {
            ChunkPos pos = event.getChunk().getPos();
            if (!stashes.containsKey(pos)) {
                stashes.put(pos, count);
                logStash(pos, count);
            }
        }
    }

    @Override
    public void onEnable() {
        if (NullUtils.nullCheck()) return;
        stashes.clear();
        int range = mc.options.getViewDistance().getValue();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                var chunk = mc.world.getChunk(mc.player.getChunkPos().x + x, mc.player.getChunkPos().z + z);
                if (chunk != null) {
                    int count = 0;
                    for (BlockEntity be : chunk.getBlockEntities().values()) {
                        if (be instanceof ChestBlockEntity || be instanceof EnderChestBlockEntity || be instanceof ShulkerBoxBlockEntity || be instanceof BarrelBlockEntity) {
                            count++;
                        }
                    }
                    if (count >= minStorage.getValue().intValue()) {
                        stashes.put(chunk.getPos(), count);
                    }
                }
            }
        }
    }

    private void logStash(ChunkPos pos, int count) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SavableManager.MAIN_FOLDER + "/stashes.txt", true))) {
            writer.println("Stash at " + pos.getCenterX() + ", " + pos.getCenterZ() + " (Count: " + count + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        for (Map.Entry<ChunkPos, Integer> entry : stashes.entrySet()) {
            ChunkPos pos = entry.getKey();
            Box bb = new Box(pos.getStartX(), 0, pos.getStartZ(), pos.getEndX(), 255, pos.getEndZ());
            
            RenderUtil.renderBox(RenderType.FILL, bb, color.getValue().getColor(), color.getValue().getColor());
            RenderUtil.renderBox(RenderType.LINES, bb, color.getValue().getColor(), color.getValue().getColor());
        }
    }

    @Override
    public void onDisable() {
        stashes.clear();
    }

    @Override
    public String getDescription() {
        return "StashFinder: Find and log chest stashes";
    }
}
