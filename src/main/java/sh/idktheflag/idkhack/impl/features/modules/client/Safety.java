package sh.idktheflag.idkhack.impl.features.modules.client;

import sh.idktheflag.idkhack.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idkhack.api.event.events.TickEvent;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.NullUtils;
import sh.idktheflag.idkhack.api.utils.players.PlayerUtils;
import sh.idktheflag.idkhack.api.utils.world.HoleUtils;
import net.minecraft.util.Formatting;

public class Safety extends Module {
    public Safety() {
        super("Safety", Category.Client);
        safety = SafetyMode.UNSAFE;

    }

    SafetyMode safety;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event){
        if (NullUtils.nullCheck()) return;

        if (mc.world.getBlockState(PlayerUtils.getPlayerPos()).isSolid()){
            safety = SafetyMode.SAFE;
            return;
        }

        if (HoleUtils.isHole(PlayerUtils.getPlayerPos())){
            safety = SafetyMode.SAFE;
            return;
        }

        safety = SafetyMode.UNSAFE;
    }

    @Override
    public String getHudInfo() {
        if(NullUtils.nullCheck()){
            return "";
        }else {
            return safety != null ? safety.color + (safety.toString().substring(0, 1).toUpperCase() + safety.toString().toLowerCase().substring(1)) : "Null";
        }
    }

    enum SafetyMode {
        SAFE(Formatting.GREEN),
        UNSAFE(Formatting.RED);

        Formatting color;

        SafetyMode(Formatting color){
            this.color = color;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }


    @Override
    public String getDescription()
    {
        return "Safety: useless just looks cool in arraylist lol";
    }

}
