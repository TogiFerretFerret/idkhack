package sh.idktheflag.idk.mixin.accessor;

import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;

// TODO: port to 1.21.11
// TextRenderer was significantly refactored:
// - validateAdvance field is removed
// - getFontStorage method is removed (fonts are now accessed via GlyphsProvider)
// - drawGlyph method is removed (glyphs are now BakedGlyph, rendering is different)
// - drawLayer method is removed
// - TextRenderer now uses prepare() + GlyphDrawable pattern instead
@Mixin(TextRenderer.class)
public interface ITextRenderer
{

}
