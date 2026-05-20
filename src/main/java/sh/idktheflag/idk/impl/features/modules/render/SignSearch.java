package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.event.events.world.ChunkDataEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.utils.render.world.RenderType;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SignSearch extends Module {
    public static SignSearch INSTANCE;

    public SignSearch() {
        super("SignSearch", Category.Render);
        INSTANCE = this;
    }

    Value<String> textToFind = new ValueBuilder<String>()
            .withDescriptor("Text")
            .withValue("")
            .register(this);

    Value<IdkColor> color = new ValueBuilder<IdkColor>()
            .withDescriptor("Color")
            .withValue(new IdkColor(0, 255, 255, 100))
            .register(this);

    private final Set<BlockPos> signs = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public void onChunkData(ChunkDataEvent event) {
        if (NullUtils.nullCheck()) return;

        for (BlockEntity be : event.getChunk().getBlockEntities().values()) {
            if (be instanceof SignBlockEntity sign) {
                if (textToFind.getValue().isEmpty() || containsText(sign)) {
                    signs.add(be.getPos());
                }
            }
        }
    }

    @Override
    public void onEnable() {
        if (NullUtils.nullCheck()) return;
        signs.clear();
        int range = mc.options.getViewDistance().getValue();
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                WorldChunk chunk = mc.world.getChunk(mc.player.getChunkPos().x + x, mc.player.getChunkPos().z + z);
                if (chunk != null) {
                    for (BlockEntity be : chunk.getBlockEntities().values()) {
                        if (be instanceof SignBlockEntity sign) {
                            if (textToFind.getValue().isEmpty() || containsText(sign)) {
                                signs.add(be.getPos());
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean containsText(SignBlockEntity sign) {
        for (int i = 0; i < 4; i++) {
            if (sign.getText(true).getMessage(i, false).getString().toLowerCase().contains(textToFind.getValue().toLowerCase())) return true;
            if (sign.getText(false).getMessage(i, false).getString().toLowerCase().contains(textToFind.getValue().toLowerCase())) return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        for (BlockPos pos : signs) {
            Box bb = new Box(pos);
            
            RenderUtil.renderBox(event.getMatrices(), RenderType.FILL, bb, color.getValue().getColor(), color.getValue().getColor());
            RenderUtil.renderBox(event.getMatrices(), RenderType.LINES, bb, color.getValue().getColor(), color.getValue().getColor());
        }
    }

    @Override
    public void onDisable() {
        signs.clear();
    }

    @Override
    public String getDescription() {
        return "SignSearch: Find signs containing specific text";
    }
}
