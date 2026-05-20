package sh.idktheflag.idk.impl.features.modules.render;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;
import sh.idktheflag.idk.api.management.FriendManager;
import sh.idktheflag.idk.api.utils.players.PlayerUtils;
import sh.idktheflag.idk.api.utils.render.RenderUtil;
import sh.idktheflag.idk.api.value.Value;
import sh.idktheflag.idk.api.value.builder.ValueBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sh.idktheflag.idk.api.wrapper.IMinecraft.mc;

public class Nametags extends Module {
    public static Nametags INSTANCE;

    public Nametags() {
        super("Nametags", Category.Render);
        INSTANCE = this;
    }

    public Value<Boolean> armor = new ValueBuilder<Boolean>()
            .withDescriptor("Armor")
            .withValue(true)
            .register(this);

    public Value<Number> scale = new ValueBuilder<Number>()
            .withDescriptor("Scale")
            .withValue(1.0)
            .withRange(0.1, 5.0)
            .withPlaces(1)
            .register(this);

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event) {
        if (NullUtils.nullCheck()) return;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (player.isSpectator()) continue;

            Vec3d renderPos = player.getLerpedPos(event.getTickDelta()).add(0, player.getHeight() + 0.5, 0);
            String text = player.getName().getString() + " " + PlayerUtils.getColoredHealth(player, true);
            
            RenderUtil.drawText(text, renderPos, scale.getValue().floatValue());

            if (armor.getValue()) {
                renderArmor(player, renderPos, event.getTickDelta());
            }
        }
    }

    private void renderArmor(PlayerEntity player, Vec3d pos, float delta) {
        List<ItemStack> armorItems = new ArrayList<>();
        armorItems.add(player.getEquippedStack(EquipmentSlot.FEET));
        armorItems.add(player.getEquippedStack(EquipmentSlot.LEGS));
        armorItems.add(player.getEquippedStack(EquipmentSlot.CHEST));
        armorItems.add(player.getEquippedStack(EquipmentSlot.HEAD));
        Collections.reverse(armorItems);
        
        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getOffHandStack();
        
        List<ItemStack> items = new ArrayList<>();
        items.add(mainHand);
        items.addAll(armorItems);
        items.add(offHand);

        float s = 0.4f * scale.getValue().floatValue();
        float xOffset = -((items.size() - 1) * 16 * s) / 2f;

        for (ItemStack stack : items) {
            if (stack.isEmpty()) {
                xOffset += 16 * s;
                continue;
            }

            MatrixStack matrices = RenderUtil.matrixFrom(pos.x, pos.y + 0.5 * scale.getValue().floatValue(), pos.z);
            matrices.push();
            matrices.multiply(mc.gameRenderer.getCamera().getRotation().conjugate(new org.joml.Quaternionf()));
            matrices.scale(s, s, s);
            matrices.translate(xOffset / s, 0, 0);
            
            RenderUtil.renderItem(stack, ItemDisplayContext.GUI, matrices, null, mc.world, player.getId());
            
            matrices.pop();
            xOffset += 16 * s;
        }
    }

    @Override
    public String getDescription() {
        return "Nametags: Custom nametags for players";
    }
}
