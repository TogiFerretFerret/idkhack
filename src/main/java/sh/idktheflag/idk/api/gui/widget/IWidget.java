package sh.idktheflag.idk.api.gui.widget;

import sh.idktheflag.idk.api.gui.helpers.Rect;

public interface IWidget<Type> {

    Type getValue();
    void setValue(Type value);
    String getTitle();
    void setTitle(String title);
    Rect getDisplayDims();
}
