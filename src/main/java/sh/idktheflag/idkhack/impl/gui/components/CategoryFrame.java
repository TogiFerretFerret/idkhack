package sh.idktheflag.idkhack.impl.gui.components;

import sh.idktheflag.idkhack.impl.gui.components.module.FeatureButton;
import sh.idktheflag.idkhack.api.feature.Feature;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.gui.component.impl.FrameComponent;
import sh.idktheflag.idkhack.api.gui.helpers.Rect;
import sh.idktheflag.idkhack.api.management.FeatureManager;

public class CategoryFrame extends FrameComponent {

    Module.Category category;

    public CategoryFrame(Module.Category category, Rect dims) {
        super(category.toString(), dims);
        this.category = category;
        for (Feature feature : FeatureManager.INSTANCE.getFeatures()){
            if (feature.getCategory() == this.category){
                getFlow().getComponents().add(new FeatureButton(feature, new Rect(0, 0, 0, 0)));
            }
        }
    }

    public Module.Category getCategory() {
        return category;
    }

    public void setCategory(Module.Category category) {
        this.category = category;
    }
}
