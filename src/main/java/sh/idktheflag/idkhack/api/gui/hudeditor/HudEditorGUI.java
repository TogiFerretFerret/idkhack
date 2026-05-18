package sh.idktheflag.idkhack.api.gui.hudeditor;

import sh.idktheflag.idkhack.api.feature.Feature;
import sh.idktheflag.idkhack.api.feature.hud.HudComponent;
import sh.idktheflag.idkhack.api.management.FeatureManager;

import sh.idktheflag.idkhack.impl.features.modules.client.gui.IdkGui;
import sh.idktheflag.idkhack.impl.gui.components.CategoryFrame;
import sh.idktheflag.idkhack.impl.gui.renderer.Renderer;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.gui.hudeditor.components.HudDisplay;
import sh.idktheflag.idkhack.api.gui.context.Context;
import sh.idktheflag.idkhack.api.gui.helpers.Rect;

public class HudEditorGUI extends HudEditor {

    public static Context CONTEXT = new Context(null, IdkGui.INSTANCE, IdkGui.INSTANCE, new Renderer(), null);

    public static HudEditorGUI INSTANCE;

    public HudEditorGUI() {
        super(CONTEXT);
    }

    @Override
    public void addComponents() {
        super.addComponents();

        int offset = 100;
        for (Module.Category category : Feature.Category.values()) {
            if (category == Feature.Category.Hud) {
                getContext().getComponents().add(new CategoryFrame(category, new Rect(offset, 40, 100, 200)));
                offset += getContext().getMetrics().getFrameWidth() + 10;
            }
        }
        for(Feature feature : FeatureManager.INSTANCE.getFeatures()){
            if(feature.getCategory() == Feature.Category.Hud){
                HudComponent hudModule = (HudComponent) feature;
                getContext().getComponents().add(new HudDisplay(new Rect(hudModule.xPos.getValue().intValue(), hudModule.yPos.getValue().intValue(), hudModule.getWidth(), hudModule.getHeight()), hudModule));
            }
        }
    }


}