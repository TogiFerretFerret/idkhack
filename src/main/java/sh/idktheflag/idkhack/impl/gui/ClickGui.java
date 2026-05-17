package sh.idktheflag.idkhack.impl.gui;

import sh.idktheflag.idkhack.impl.features.modules.client.gui.Sn0wGui;
import sh.idktheflag.idkhack.impl.gui.components.CategoryFrame;
import sh.idktheflag.idkhack.impl.gui.renderer.Renderer;
import sh.idktheflag.idkhack.api.feature.Feature;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.gui.GUI;
import sh.idktheflag.idkhack.api.gui.context.Context;
import sh.idktheflag.idkhack.api.gui.helpers.Rect;
import net.minecraft.client.gui.DrawContext;

public class ClickGui extends GUI {

    public static Context CONTEXT = new Context(null, Sn0wGui.INSTANCE, Sn0wGui.INSTANCE, new Renderer(), null);

    public static ClickGui INSTANCE;
    public ClickGui() {
        super(CONTEXT);
    }

    @Override
    public void addComponents() {
        super.addComponents();

        int offset = 100;
        for (Module.Category category : Feature.Category.values()) {
            if(category.equals(Feature.Category.Hud)) continue;

            getContext().getComponents().add(new CategoryFrame(category, new Rect(offset, 40, 100, 200)));
            offset += getContext().getMetrics().getFrameWidth() + 5;
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta)
    {
        super.render(drawContext, mouseX, mouseY, delta);
    }
}