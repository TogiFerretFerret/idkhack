package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.feature.Feature.Category;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;

// TODO: port to 1.21.11 - module disabled
public class LogoutSpots extends Module {
    Value<Sn0wColor> fill = new ValueBuilder<Sn0wColor>()
    Value<Sn0wColor> line = new ValueBuilder<Sn0wColor>()
    Value<String> mode = new ValueBuilder<String>()
    public Value<Boolean> pops = new ValueBuilder<Boolean>()
    public Value<Sn0wColor> normalColor = new ValueBuilder<Sn0wColor>()
    public Value<Sn0wColor> friendsColor = new ValueBuilder<Sn0wColor>()
    public Value<Sn0wColor> borderColor = new ValueBuilder<Sn0wColor>()
    return friendsColor.getValue().getColor();
    // TODO 1.21.11: return normalColor.getValue().getColor();
}
