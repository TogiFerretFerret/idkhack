package sh.idktheflag.idkhack.impl.features.modules.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.network.ServerEvent;
import sh.idktheflag.idkhack.api.event.events.render.*;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.feature.module.Category;
import sh.idktheflag.idkhack.api.management.FriendManager;
import sh.idktheflag.idkhack.api.management.shaders.ShaderManager;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;
import sh.idktheflag.idkhack.api.utils.math.MathUtil;
import sh.idktheflag.idkhack.api.utils.render.RenderUtil;
import sh.idktheflag.idkhack.api.utils.render.world.buffers.RenderBuffers;
import sh.idktheflag.idkhack.api.utils.world.BlockUtils;
import sh.idktheflag.idkhack.api.utils.world.EntityUtils;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.impl.features.modules.client.HudColors;
import sh.idktheflag.idkhack.mixin.accessor.IGameRenderer;
import sh.idktheflag.idkhack.mixin.accessor.IWorldRenderer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.satin.api.managed.ManagedShaderEffect;
import org.lwjgl.opengl.GL32C;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import net.minecraft.block.entity.*;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;

import java.awt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Optional;
import java.util.UUID;

// TODO: port to 1.21.11 - module disabled, see TODO.md
public class Shaders extends Module {
    public Shaders() {
        super("Shaders", Category.Render);
    }
}
