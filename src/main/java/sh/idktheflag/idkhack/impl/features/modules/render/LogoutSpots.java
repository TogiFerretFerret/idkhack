package sh.idktheflag.idkhack.impl.features.modules.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.event.events.network.PacketEvent;
import sh.idktheflag.idkhack.api.event.events.player.TeamColorEvent;
import sh.idktheflag.idkhack.api.event.events.render.EntityOutlineEvent;
import sh.idktheflag.idkhack.api.event.events.render.RenderWorldEvent;
import sh.idktheflag.idkhack.api.event.events.world.EntityEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.FriendManager;
import sh.idktheflag.idkhack.api.management.PopManager;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.Pair;
import sh.idktheflag.idkhack.api.utils.chat.ChatMessage;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import sh.idktheflag.idkhack.api.utils.color.ColorUtil;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.utils.color.TextSection;
import sh.idktheflag.idkhack.api.utils.math.MathUtil;
import sh.idktheflag.idkhack.api.utils.render.Interpolator;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.render.WireframeEntityRenderer;
import sh.idktheflag.idkhack.api.utils.render.world.RenderType;
import sh.idktheflag.idkhack.api.utils.render.world.buffers.RenderBuffers;
import sh.idktheflag.idkhack.api.utils.world.EntityUtils;
import sh.idktheflag.idkhack.api.utils.world.WorldUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.features.modules.client.HudColors;
import sh.idktheflag.idkhack.impl.features.modules.client.Manager;
import sh.idktheflag.idkhack.impl.features.modules.client.sense.PingedLocation;
import sh.idktheflag.idkhack.mixin.accessor.ILimbAnimator;
import sh.idktheflag.idkhack.mixin.accessor.IWorldRenderer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.BlockBreakingInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogoutSpots extends Module
{

    Value<Sn0wColor> fill = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Fill Color")
            .withValue(new Sn0wColor(47, 0, 255, 150))
            .register(this);
    Value<Sn0wColor> line = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Line Color")
            .withValue(new Sn0wColor(255, 255, 255, 255))
            .register(this);
    Value<String> mode = new ValueBuilder<String>()
            .withDescriptor("Mode")
            .withValue("Box")
            .withModes("Box", "Model")
            .register(this);
    public Value<Boolean> pops = new ValueBuilder<Boolean>()
            .withDescriptor("Pops")
            .withValue(true)
            .register(this);
    public Value<Sn0wColor> normalColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Normal Color")
            .withValue(new Sn0wColor(0, 255, 255, 255))
            .register(this);
    public Value<Sn0wColor> friendsColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Friends Color")
            .withValue(new Sn0wColor(0, 255, 255, 255))
            .register(this);

    public Value<Sn0wColor> borderColor = new ValueBuilder<Sn0wColor>()
            .withDescriptor("Border Color")
            .withValue(new Sn0wColor(255, 0, 0, 255))
            .register(this);

    public LogoutSpots()
    {
        super("LogoutESP", Category.Render);
    }

    public static List<LoggedPlayer> players = new CopyOnWriteArrayList<LoggedPlayer>();


    Map<UUID, Pair<PlayerEntity, Long>> lastPlayers = new ConcurrentHashMap<>();


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (NullUtils.nullCheck()) return;


        for (Map.Entry<UUID, Pair<PlayerEntity, Long>> entry : lastPlayers.entrySet())
        {
            long time = System.currentTimeMillis() - entry.getValue().value();

            if (time > 200L)
            {
                lastPlayers.remove(entry.getKey());
            }
        }

    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldEvent event)
    {
        if (NullUtils.nullCheck()) return;

        players.forEach(loggedPlayer -> renderEntity(event.getMatrices(), loggedPlayer.player, loggedPlayer.modelPlayer, loggedPlayer, event));


    }

    @SubscribeEvent
    public void onEntityRemove(EntityEvent.Remove event)
    {

        // crystal being removed from world
        if (event.getEntity() instanceof PlayerEntity entity)
        {
            lastPlayers.put(entity.getUuid(), new Pair<>(entity, System.currentTimeMillis()));
        }
    }

    @Override
    public void onDisable()
    {
        super.onDisable();
        if (NullUtils.nullCheck()) return;


        players.clear();

    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event)
    {
        if (NullUtils.nullCheck()) return;

        if (event.getPacket() instanceof PlayerListS2CPacket pac)
        {
            if (pac.getActions().contains(PlayerListS2CPacket.Action.ADD_PLAYER))
            {
                for (PlayerListS2CPacket.Entry ple : pac.getPlayerAdditionEntries())
                {
                    for (LoggedPlayer player : players)
                    {
                        if (false) // TODO 1.21.11: if (!player.id.equals(ple.profile().getId())) continue;

                        ChatUtils.sendMessage(new ChatMessage("[Logout Spots] " + Manager.INSTANCE.getMainColor() + player.playerName + Formatting.RESET + " logged back in!", false, 99922));
                        players.remove(player);
                    }
                }
            }
        }

        if (event.getPacket() instanceof PlayerRemoveS2CPacket pac)
        {
            for (UUID uuid2 : pac.profileIds())
            {
                PlayerEntity playerEntity = mc.world.getPlayerByUuid(uuid2);
                Pair<PlayerEntity, Long> pair = lastPlayers.get(uuid2);

                if (playerEntity == null && pair != null)
                {
                    playerEntity = pair.key();
                }
                if (playerEntity != null)
                {

                    if (playerEntity == mc.player) return;

                    // TODO: port to 1.21.11 - anonymous PlayerEntity creation changed
                    // Entire entity creation and setup commented out
                    lastPlayers.remove(uuid2);
                    break;
                }
            }
        }
    }

    // TODO: port to 1.21.11 - renderEntity method signature changed
    private void renderEntity() {
        if (true) return; // disabled
        if (mode.getValue().equals("Model"))
        {
            modelBase.leftPants.visible = false;
            // TODO 1.21.11: modelBase.rightPants.visible = false;
            // TODO 1.21.11: modelBase.leftSleeve.visible = false;
            // TODO 1.21.11: modelBase.rightSleeve.visible = false;
            // TODO 1.21.11: modelBase.jacket.visible = false;
            // TODO 1.21.11: modelBase.hat.visible = false;

            // TODO 1.21.11: double x = entity.getX();
            // TODO 1.21.11: double y = entity.getY();
            // TODO 1.21.11: double z = entity.getZ();
            // TODO 1.21.11: matrices = RenderUtil.matrixFrom(x, y, z); 
            // TODO 1.21.11: matrices.push();


            // TODO 1.21.11: matrices.multiply(RotationAxis.POSITIVE_Y.rotation(MathUtil.rad(180 - entity.bodyYaw)));
            // TODO 1.21.11: prepareScale(matrices);

            // TODO: port to 1.21.11 - animateModel/setAngles now take render states instead of entity params
            // AnimalModel removed, PlayerEntityModel no longer generic
            // modelBase.animateModel(...) and modelBase.setAngles(...) need PlayerEntityRenderState
            // TODO 1.21.11: WireframeEntityRenderer.renderModel(matrices, (EntityModel) modelBase, RenderType.BOTH, fill.getValue().getColor(), line.getValue().getColor());
            // TODO: port to 1.21.11 - RenderSystem.setShaderColor() removed
            // TODO 1.21.11: matrices.pop();
        // TODO 1.21.11: } else
        {
            // TODO 1.21.11: RenderUtil.renderBox(RenderType.FILL, entity.getBoundingBox(), fill.getValue().getColor(), fill.getValue().getColor());
            // TODO 1.21.11: RenderUtil.renderBox(RenderType.LINES, entity.getBoundingBox(), line.getValue().getColor(), line.getValue().getColor());

        }
        // TODO 1.21.11: if (player != null)
        {
            // TODO 1.21.11: RenderBuffers.scheduleRender(() ->
            {

                renderWaypoint(player, event);
                // RenderSystem.enableBlend(); // TODO: port to 1.21.11

            // TODO 1.21.11: });
        }

    }


    // TODO 1.21.11: private void renderWaypoint(LoggedPlayer loc, RenderWorldEvent event)
    {
        // TODO 1.21.11: Vec3d interpolate = Interpolator.getInterpolatedEyePos(mc.getCameraEntity(), event.getTickProgress());
        Camera camera = mc.gameRenderer.getCamera();
        // TODO 1.21.11: Vec3d pos = camera.getCameraPos();


        // TODO 1.21.11: double dx = (pos.getX() - interpolate.getX()) - loc.player.getX();
        // TODO 1.21.11: double dy = (pos.getY() - interpolate.getY()) - (loc.player.getY() + loc.player.getHeight() + (loc.player.isSneaking() ? 0.4f : 0.43f));
        // TODO 1.21.11: double dz = (pos.getZ() - interpolate.getZ()) - loc.player.getZ();
        // TODO 1.21.11: double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);


        // TODO 1.21.11: TextSection[] text = new TextSection[1];
        // TODO 1.21.11: text[0] = new TextSection(renderEntityName(loc.player, loc), ColorUtil.newAlpha(fill.getValue().getColor(), 255));

        // RenderSystem.enableBlend(); // TODO: port to 1.21.11
        // RenderSystem.defaultBlendFunc(); // TODO: port to 1.21.11
        // TODO 1.21.11: GL11.glDepthFunc(GL11.GL_ALWAYS);

        // TODO 1.21.11: RenderUtil.drawWaypoint(text, loc.player.getX(), loc.player.getY() + loc.player.getHeight() + (loc.player.isSneaking() ? 0.4f : 0.43f), loc.player.getZ(), mc.gameRenderer.getCamera(), borderColor.getValue().getColor());
        // TODO 1.21.11: GL11.glDepthFunc(GL11.GL_LEQUAL);
        // RenderSystem.disableBlend(); // TODO: port to 1.21.11

    }


    // TODO 1.21.11: private String renderEntityName(final PlayerEntity entityPlayer, LoggedPlayer player)
    {
        // TODO 1.21.11: String s = Formatting.RED + player.playerName + " logout";

        // TODO 1.21.11: final double ceil;
        // TODO 1.21.11: String s2 = Formatting.GREEN.toString();
        // TODO 1.21.11: if ((ceil = Math.ceil(entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount())) > 0.0)
        {

            // TODO 1.21.11: if ((entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) <= 5)
            {
                s2 = Formatting.RED.toString();
            // TODO 1.21.11: } else if ((entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) > 5 && (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) <= 10)
            {
                // TODO 1.21.11: s2 = Formatting.GOLD.toString();
            // TODO 1.21.11: } else if ((entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) > 10 && (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) <= 15)
            {
                // TODO 1.21.11: s2 = Formatting.YELLOW.toString();
            // TODO 1.21.11: } else if ((entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) > 15 && (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) <= 20)
            {
                // TODO 1.21.11: s2 = Formatting.DARK_GREEN.toString();
            // TODO 1.21.11: } else if ((entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) > 20)
            {
                // TODO 1.21.11: s2 = Formatting.GREEN.toString();
            // TODO 1.21.11: }
        // TODO 1.21.11: } else
        {
            // TODO 1.21.11: s2 = Formatting.DARK_RED.toString();
        }

        // TODO 1.21.11: int popsForPlayer = PopManager.INSTANCE.getPops(player.playerName);
        // TODO 1.21.11: String popstring = Formatting.RED + " (" + Formatting.WHITE + MathHelper.floor(mc.player.distanceTo(entityPlayer)) + "m" + Formatting.RED + ")";
        // TODO 1.21.11: if (pops.getValue())
        {
            // TODO 1.21.11: if (popsForPlayer < 1)
            {

            // TODO 1.21.11: } else
            {
                popstring = Formatting.RED + " (" + Formatting.WHITE + MathHelper.floor(mc.player.distanceTo(entityPlayer)) + "m" + Formatting.AQUA + " -" + popsForPlayer + Formatting.RED + ")";
            // TODO 1.21.11: }
        }
        // TODO 1.21.11: return new StringBuilder().insert(0, s).append(s2).append(" ").append((ceil > 0.0) ? Integer.valueOf((int) ceil) : "0").append(popstring).toString();
    }

    // TODO 1.21.11: private Color renderPing(final PlayerEntity entityPlayer)
    {
        // TODO 1.21.11: if (FriendManager.INSTANCE.isFriend(entityPlayer))
        {
            return friendsColor.getValue().getColor();
        }
        // TODO 1.21.11: if (entityPlayer.isInvisible())
        {
            // TODO 1.21.11: return new Color(128, 128, 128);
        }
        // TODO 1.21.11: return normalColor.getValue().getColor();
    }

    private static void prepareScale() { // TODO: port to 1.21.11
        // TODO 1.21.11: matrixStack.scale(-1.0F, -1.0F, 1.0F);
        // TODO 1.21.11: matrixStack.scale(1.6f, 1.8f, 1.6f);
        // TODO 1.21.11: matrixStack.translate(0.0F, -1.501F, 0.0F);

    }

    @Override
    public String getDescription()
    {
        return "LogoutESP: highlights logged players";
    }


    // TODO: port LoggedPlayer to 1.21.11
    static class LoggedPlayer {
        public String playerName = "";
        public Box bb = null;
        public UUID id = null;
    }

}