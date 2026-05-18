package sh.idktheflag.idk.impl.gui.components.value;

import sh.idktheflag.idk.api.gui.context.Context;
import sh.idktheflag.idk.api.gui.helpers.MouseHelper;
import sh.idktheflag.idk.api.gui.widget.impl.SliderWidget;
import sh.idktheflag.idk.api.value.Value;

public class SliderComponent extends SliderWidget {

    Value<Number> val;

    public SliderComponent(Value<Number> value) {
        super(value.getName(), value.getValue(), value.getMin(), value.getMax());
        this.val = value;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        super.draw(context, mouse);
        setValue(val.getValue());
        min = val.getMin();
        max = val.getMax();
    }

    @Override
    public void setValue(Number value){
        super.setValue(value);
        val.setValue(value);
    }

    @Override
    public boolean isActive() {
        return val.isActive();
    }
}
