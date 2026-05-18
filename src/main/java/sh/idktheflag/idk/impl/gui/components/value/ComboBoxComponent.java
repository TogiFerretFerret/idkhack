package sh.idktheflag.idk.impl.gui.components.value;

import sh.idktheflag.idk.api.gui.context.Context;
import sh.idktheflag.idk.api.gui.helpers.MouseHelper;
import sh.idktheflag.idk.api.gui.helpers.Rect;
import sh.idktheflag.idk.api.gui.widget.impl.ComboBoxWidget;
import sh.idktheflag.idk.api.value.Value;

public class ComboBoxComponent extends ComboBoxWidget {
    Value<String> value;
    public ComboBoxComponent(Value<String> value) {
        super(value.getName(), value.getValue(), value.getModes(), new Rect(0, 0, 0, 0));
        this.value = value;
    }

    @Override
    public void draw(Context context, MouseHelper mouse) {
        setValue(value.getValue());
        super.draw(context, mouse);
    }

    @Override
    public void click(Context context, MouseHelper mouse, int button) {
        super.click(context, mouse, button);

    }

    @Override
    public boolean isActive() {
        return value.isActive();
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        this.value.setValue(value);
    }
}
