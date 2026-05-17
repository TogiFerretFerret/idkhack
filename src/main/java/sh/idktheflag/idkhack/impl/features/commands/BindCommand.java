package sh.idktheflag.idkhack.impl.features.commands;

import sh.idktheflag.idkhack.api.command.Command;
import sh.idktheflag.idkhack.api.feature.Feature;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.FeatureManager;
import sh.idktheflag.idkhack.api.utils.StringUtils;
import sh.idktheflag.idkhack.api.utils.chat.ChatMessage;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;
import net.minecraft.client.util.InputUtil;

public class BindCommand extends Command {
    public BindCommand() {
        super("Bind", "bind a module", new String[]{"bind"});
    }

    @Override
    public void run(String[] args) {
        if (args.length > 2){
            for (Feature feature : FeatureManager.INSTANCE.getFeatures()){
                if (feature instanceof Module){
                    Module module = ((Module) feature);
                    String modName = module.getName().replace(" ", "");
                    if (modName.equalsIgnoreCase(args[1])){
                        if(args[2].contains("MOUSE_")){
                            module.getBind().setIsMouse(true);
                            module.getBind().setKey(Integer.parseInt(args[2].replace("MOUSE_", "")));
                        }else{
                            module.getBind().setIsMouse(false);
                            module.getBind().setKey(StringUtils.getKeyCode(args[2].toUpperCase()));
                        }
                        ChatUtils.sendMessage(new ChatMessage(
                                "Bound " + module.getName() + " to " + StringUtils.getKeyName(module.getBind().getKey()),
                                false,
                                0
                        ));
                    }
                }
            }
        } else {
            ChatUtils.sendMessage(new ChatMessage(
                    "Please input a valid command",
                    false,
                    0
            ));
        }
    }
    @Override
    public String[] getFill(String[] args)
    {
        if (args.length == 2)
        {
            return new String[]{"{FEATURE}", "key"};
        } else
        {
            return new String[]{"{FEATURE}"};
        }
    }
}
