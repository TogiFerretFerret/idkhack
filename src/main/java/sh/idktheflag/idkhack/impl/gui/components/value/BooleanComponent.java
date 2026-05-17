package sh.idktheflag.idkhack.impl.gui.components.value;

import sh.idktheflag.idkhack.api.gui.context.Context;
import sh.idktheflag.idkhack.api.gui.helpers.MouseHelper;
import sh.idktheflag.idkhack.api.gui.helpers.Rect;
import sh.idktheflag.idkhack.api.gui.widget.impl.BooleanWidget;
import sh.idktheflag.idkhack.api.value.Value;

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
