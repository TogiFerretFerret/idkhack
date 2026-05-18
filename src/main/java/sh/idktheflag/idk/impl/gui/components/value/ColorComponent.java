package sh.idktheflag.idk.impl.gui.components.value;

import sh.idktheflag.idk.api.gui.helpers.Rect;
import sh.idktheflag.idk.api.gui.widget.impl.ColorWidget;
import sh.idktheflag.idk.api.utils.color.IdkColor;
import sh.idktheflag.idk.api.value.Value;

import java.awt.*;

public class ColorComponent extends ColorWidget {
    Value<IdkColor> colorValue;
    public ColorComponent(Value<IdkColor> colorValue) {
        super(colorValue.getName(), colorValue.getValue(), new Rect(0, 0, 0, 0));
        this.colorValue = colorValue;
    }

    @Override
    public Color getValue() {
        return colorValue.getValue().getColor();
    }


    @Override
    public void setSyncing(boolean syncing) {
        colorValue.getValue().setSync(syncing);
    }

    @Override
    public boolean getSyncing() {
        return colorValue.getValue().isSyncing();
    }

    @Override
    public void setValue(Color value) {
        colorValue.getValue().setColor(value);
    }

    @Override
    public boolean isActive() {
        return colorValue.isActive();
    }
}
