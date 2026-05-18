package sh.idktheflag.idk.impl.gui.components;

import sh.idktheflag.idk.impl.gui.components.module.FeatureButton;
import sh.idktheflag.idk.api.feature.Feature;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.gui.component.impl.FrameComponent;
import sh.idktheflag.idk.api.gui.helpers.Rect;
import sh.idktheflag.idk.api.management.FeatureManager;

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
