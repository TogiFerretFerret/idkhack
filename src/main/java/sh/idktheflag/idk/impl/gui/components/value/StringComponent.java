package sh.idktheflag.idk.impl.gui.components.value;

import sh.idktheflag.idk.api.gui.helpers.Rect;
import sh.idktheflag.idk.api.gui.widget.impl.TextEntryWidget;
import sh.idktheflag.idk.api.value.Value;

public class StringComponent extends TextEntryWidget {
    Value<String> stringValue;
    public StringComponent(Value<String> stringValue) {
        super(new Rect(0, 0, 0, 0), stringValue.getValue());
        this.stringValue = stringValue;
    }

    @Override
    public String getValue() {
        return stringValue.getValue();
    }

    @Override
    public void setValue(String value) {
        stringValue.setValue(value);
        super.setValue(value);
    }

    @Override
    public boolean isActive() {
        return stringValue.isActive();
    }
}
