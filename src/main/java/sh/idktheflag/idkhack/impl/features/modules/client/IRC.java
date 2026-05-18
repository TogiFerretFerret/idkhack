package sh.idktheflag.idkhack.impl.features.modules.client;

import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import net.minecraft.util.Formatting;

public class IRC extends Module {

    public static IRC INSTANCE;

    public IRC() {
        super("IRC", Category.Client);
        INSTANCE = this;
        setEnabled(true);
    }
    @Override
    public void onDisable(){
        if(mc.player != null && mc.world != null){
            ChatUtils.sendMessage(Formatting.RED + "You will no longer see irc message");
        }
    }


    @Override
    public String getDescription() {
        return "IRC: Talk with other idkhack users";
    }
}
