package sh.idktheflag.idk.impl.gui.components.value;

import sh.idktheflag.idk.api.gui.context.Context;
import sh.idktheflag.idk.api.gui.helpers.MouseHelper;
import sh.idktheflag.idk.api.gui.helpers.Rect;
import sh.idktheflag.idk.api.gui.widget.impl.BooleanWidget;
import sh.idktheflag.idk.api.value.Value;

public class BooleanComponent extends BooleanWidget {

    Value<Boolean> value;

    public BooleanComponent(Value<Boolean> booleanValue) {
        super(booleanValue.getName(), new Rect(0, 0, 0, 0));
        this.value = booleanValue;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        setValue(value.getValue());
        super.draw(context, mouse);
    }

    @Override
    public void setValue(Boolean value) {
        super.setValue(value);
        this.value.setValue(value);
    }

    @Override
    public boolean isActive() {
        return value.isActive();
    }
}
