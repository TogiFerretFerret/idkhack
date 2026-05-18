package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.TickEvent;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.utils.color.ColorUtil;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.utils.render.world.RenderType;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockESP extends Module {

    Value<String> page = new ValueBuilder<String>()
            .withDescriptor("Category")
            .withValue("Ores")
            .withModes("Ores", "Storage", "Combat", "Other")
            .register(this);

    // Ores
    Value<Boolean> diamond = new ValueBuilder<Boolean>()
            .withDescriptor("Diamond").withValue(true)
            .withPageParent(page).withPage("Ores").register(this);
    Value<Boolean> emerald = new ValueBuilder<Boolean>()
            .withDescriptor("Emerald").withValue(false)
            .withPageParent(page).withPage("Ores").register(this);
    Value<Boolean> gold = new ValueBuilder<Boolean>()
            .withDescriptor("Gold").withValue(false)
            .withPageParent(page).withPage("Ores").register(this);
    Value<Boolean> iron = new ValueBuilder<Boolean>()
            .withDescriptor("Iron").withValue(false)
            .withPageParent(page).withPage("Ores").register(this);
    Value<Boolean> redstone = new ValueBuilder<Boolean>()
            .withDescriptor("Redstone").withValue(false)
            .withPageParent(page).withPage("Ores").register(this);
    Value<Boolean> lapis = new ValueBuilder<Boolean>()
            .withDescriptor("Lapis").withValue(false)
            .withPageParent(page).withPage("Ores").register(this);
    Value<Boolean> coal = new ValueBuilder<Boolean>()
            .withDescriptor("Coal").withValue(false)
            .withPageParent(page).withPage("Ores").register(this);
    Value<Boolean> copper = new ValueBuilder<Boolean>()
            .withDescriptor("Copper").withValue(false)
            .withPageParent(page).withPage("Ores").register(this);
    Value<Boolean> ancient = new ValueBuilder<Boolean>()
            .withDescriptor("Ancient Debris").withValue(false)
            .withPageParent(page).withPage("Ores").register(this);

    // Storage
    Value<Boolean> chest = new ValueBuilder<Boolean>()
            .withDescriptor("Chest").withValue(true)
            .withPageParent(page).withPage("Storage").register(this);
    Value<Boolean> enderChest = new ValueBuilder<Boolean>()
            .withDescriptor("Ender Chest").withValue(false)
            .withPageParent(page).withPage("Storage").register(this);
    Value<Boolean> shulker = new ValueBuilder<Boolean>()
            .withDescriptor("Shulker Box").withValue(false)
            .withPageParent(page).withPage("Storage").register(this);
    Value<Boolean> barrel = new ValueBuilder<Boolean>()
            .withDescriptor("Barrel").withValue(false)
            .withPageParent(page).withPage("Storage").register(this);
    Value<Boolean> furnace = new ValueBuilder<Boolean>()
            .withDescriptor("Furnace").withValue(false)
            .withPageParent(page).withPage("Storage").register(this);

    // Combat
    Value<Boolean> obsidian = new ValueBuilder<Boolean>()
            .withDescriptor("Obsidian").withValue(false)
            .withPageParent(page).withPage("Combat").register(this);
    Value<Boolean> bedrock = new ValueBuilder<Boolean>()
            .withDescriptor("Bedrock").withValue(false)
            .withPageParent(page).withPage("Combat").register(this);
    Value<Boolean> respawnAnchor = new ValueBuilder<Boolean>()
            .withDescriptor("Respawn Anchor").withValue(false)
            .withPageParent(page).withPage("Combat").register(this);

    // Other
    Value<Boolean> spawner = new ValueBuilder<Boolean>()
            .withDescriptor("Spawner").withValue(false)
            .withPageParent(page).withPage("Other").register(this);
    Value<Boolean> portal = new ValueBuilder<Boolean>()
            .withDescriptor("Nether Portal").withValue(false)
            .withPageParent(page).withPage("Other").register(this);

    // Render
    Value<Number> range = new ValueBuilder<Number>()
            .withDescriptor("Range").withValue(20).withRange(5, 50).register(this);
    Value<Boolean> fill = new ValueBuilder<Boolean>()
            .withDescriptor("Fill").withValue(true).register(this);
    Value<Boolean> outline = new ValueBuilder<Boolean>()
            .withDescriptor("Outline").withValue(true).register(this);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile Map<BlockPos, Color> found = new ConcurrentHashMap<>();

    public BlockESP() {
        super("BlockESP", Category.Render);
    }

    @Override
    public void onDisable() {
        found.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) return;
        executor.submit(this::scan);
    }

    private void scan() {
        if (mc.world == null || mc.player == null) return;
        Map<BlockPos, Color> result = new ConcurrentHashMap<>();
        int r = range.getValue().intValue();
        BlockPos center = mc.player.getBlockPos();

        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos pos = center.add(x, y, z);
                    Block block = mc.world.getBlockState(pos).getBlock();
                    Color color = getColor(block);
                    if (color != null) result.put(pos.toImmutable(), color);
                }
            }
        }
        found = result;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        for (Map.Entry<BlockPos, Color> entry : found.entrySet()) {
            BlockPos pos = entry.getKey();
            Color color = entry.getValue();
            Box bb = new Box(pos);

            if (fill.getValue()) {
                RenderUtil.renderBox(RenderType.FILL, bb, ColorUtil.newAlpha(color, 40), ColorUtil.newAlpha(color, 40));
            }
            if (outline.getValue()) {
                RenderUtil.renderBox(RenderType.LINES, bb, color, color);
            }
        }
    }

    private Color getColor(Block block) {
        // Ores
        if (diamond.getValue() && (block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE))
            return new Color(0, 220, 255);
        if (emerald.getValue() && (block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE))
            return new Color(0, 255, 60);
        if (gold.getValue() && (block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE || block == Blocks.NETHER_GOLD_ORE))
            return new Color(255, 215, 0);
        if (iron.getValue() && (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE))
            return new Color(210, 180, 140);
        if (redstone.getValue() && (block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE))
            return Color.RED;
        if (lapis.getValue() && (block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE))
            return new Color(30, 80, 200);
        if (coal.getValue() && (block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE))
            return new Color(60, 60, 60);
        if (copper.getValue() && (block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE))
            return new Color(210, 120, 60);
        if (ancient.getValue() && block == Blocks.ANCIENT_DEBRIS)
            return new Color(160, 80, 40);

        // Storage
        if (chest.getValue() && (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST))
            return new Color(200, 140, 60);
        if (enderChest.getValue() && block == Blocks.ENDER_CHEST)
            return new Color(80, 0, 160);
        if (shulker.getValue() && block instanceof ShulkerBoxBlock)
            return new Color(((ShulkerBoxBlock) block).getColor().getEntityColor());
        if (barrel.getValue() && block == Blocks.BARREL)
            return new Color(160, 100, 40);
        if (furnace.getValue() && (block == Blocks.FURNACE || block == Blocks.BLAST_FURNACE || block == Blocks.SMOKER))
            return new Color(120, 120, 120);

        // Combat
        if (obsidian.getValue() && (block == Blocks.OBSIDIAN || block == Blocks.CRYING_OBSIDIAN))
            return new Color(80, 30, 140);
        if (bedrock.getValue() && block == Blocks.BEDROCK)
            return new Color(60, 60, 80);
        if (respawnAnchor.getValue() && block == Blocks.RESPAWN_ANCHOR)
            return new Color(255, 50, 255);

        // Other
        if (spawner.getValue() && block == Blocks.SPAWNER)
            return new Color(20, 160, 20);
        if (portal.getValue() && block == Blocks.NETHER_PORTAL)
            return new Color(100, 50, 255);

        return null;
    }

    @Override
    public String getDescription() {
        return "BlockESP: highlight specific blocks in the world";
    }
}
