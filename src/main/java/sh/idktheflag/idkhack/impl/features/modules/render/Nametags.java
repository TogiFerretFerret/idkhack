package sh.idktheflag.idkhack.impl.features.modules.render;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.feature.Feature.Category;
import sh.idktheflag.idkhack.api.value.Value;
import sh.idktheflag.idkhack.api.value.builder.ValueBuilder;
import sh.idktheflag.idkhack.api.utils.color.Sn0wColor;

// TODO: port to 1.21.11 - module disabled
public class Nametags extends Module {
    public static Nametags INSTANCE;
    Value<String> page = new ValueBuilder<String>()
    public Value<Sn0wColor> borderColor = new ValueBuilder<Sn0wColor>()
    public Value<Boolean> safeBorder = new ValueBuilder<Boolean>()
    public Value<Sn0wColor> safeColor = new ValueBuilder<Sn0wColor>()
    public Value<Sn0wColor> boxColor = new ValueBuilder<Sn0wColor>()
    public Value<Sn0wColor> normalColor = new ValueBuilder<Sn0wColor>()
    public Value<Sn0wColor> friendsColor = new ValueBuilder<Sn0wColor>()
    public Value<Boolean> friendBorder = new ValueBuilder<Boolean>()
    public Value<Sn0wColor> SneakingColor = new ValueBuilder<Sn0wColor>()
    public Value<Sn0wColor> popColorA = new ValueBuilder<Sn0wColor>()
    public Value<Sn0wColor> popColorB = new ValueBuilder<Sn0wColor>()
    public Value<Sn0wColor> popColorC = new ValueBuilder<Sn0wColor>()
    public Value<Boolean> ping = new ValueBuilder<Boolean>()
    public Value<Boolean> items = new ValueBuilder<Boolean>()
    public Value<Boolean> durability = new ValueBuilder<Boolean>()
    public Value<Boolean> pops = new ValueBuilder<Boolean>()
    public Value<Boolean> dash = new ValueBuilder<Boolean>()
    public Value<Boolean> itemName = new ValueBuilder<Boolean>()
    public Value<Boolean> enchantNames = new ValueBuilder<Boolean>()
    public Value<Boolean> shortEnchants = new ValueBuilder<Boolean>()
    public Value<Boolean> rainbow32k = new ValueBuilder<Boolean>()
    public Value<Boolean> cursedRed = new ValueBuilder<Boolean>()
    public Value<Boolean> health = new ValueBuilder<Boolean>()
    public Value<Boolean> entityId = new ValueBuilder<Boolean>()
    public Value<Boolean> gamemode = new ValueBuilder<Boolean>()
    public Value<Number> range = new ValueBuilder<Number>()
    public Value<Number> scalingSet = new ValueBuilder<Number>()
    public Value<Number> closeScaling = new ValueBuilder<Number>()
    Value<Boolean> ColoredPing = new ValueBuilder<Boolean>()
    public Value<Boolean> tabHealth = new ValueBuilder<Boolean>()
    boxColor.setActive(page.equals("Colors"));
    normalColor.setActive(page.equals("Colors"));
    friendsColor.setActive(page.equals("Colors"));
    return friendsColor.getValue().getColor();
    return normalColor.getValue().getColor();
    RenderUtil.renderRect(matrices, -width - 1.0f, -1.0f, width * 2.0f + 2.0f, mc.textRenderer.fontHeight + 1.5f, 0.0, boxColor.getValue().getColor().getRGB());
}
