package sh.idktheflag.idkhack.impl.gui.components.value;

import sh.idktheflag.idkhack.api.value.Value;

public interface ICustomComponent<Type> {

    void setValue(Value<Type> value);
}