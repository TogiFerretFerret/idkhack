package sh.idktheflag.idkhack.api.utils.ducks;

import net.minecraft.client.gl.Framebuffer;

@IMixin
public interface IPostEffectProcessor {

    void overwriteBuffer(String name, Framebuffer buffer);
}