package sh.idktheflag.idk.impl.gui.components.value;

import sh.idktheflag.idk.api.value.Value;

public interface ICustomComponent<Type> {

    void setValue(Value<Type> value);
}