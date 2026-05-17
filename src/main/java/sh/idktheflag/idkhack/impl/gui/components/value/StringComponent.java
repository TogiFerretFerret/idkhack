package sh.idktheflag.idkhack.impl.gui.components.value;

import sh.idktheflag.idkhack.api.gui.helpers.Rect;
import sh.idktheflag.idkhack.api.gui.widget.impl.TextEntryWidget;
import sh.idktheflag.idkhack.api.value.Value;

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
