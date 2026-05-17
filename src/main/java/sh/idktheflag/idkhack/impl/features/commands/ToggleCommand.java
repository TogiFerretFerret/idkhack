package sh.idktheflag.idkhack.impl.features.commands;

import sh.idktheflag.idkhack.api.command.Command;
import sh.idktheflag.idkhack.api.feature.Feature;
import sh.idktheflag.idkhack.api.feature.module.Module;
import sh.idktheflag.idkhack.api.management.FeatureManager;
import sh.idktheflag.idkhack.api.utils.chat.ChatMessage;
import sh.idktheflag.idkhack.api.utils.chat.ChatUtils;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("Toggle", "toggles a meowdule", new String[]{"toggle"});
    }

    @Override
    public void run(String[] args) {
        if (args.length > 1){
            for (Feature feature : FeatureManager.INSTANCE.getFeatures()){
                if (feature instanceof Module){
                    Module module = ((Module) feature);
                    String modName = module.getName().replace(" ", "");
                    if (modName.equalsIgnoreCase(args[1])){
                        module.toggle();
                        return;
                    }
                }
            }
        } else {
            ChatUtils.sendMessage(new ChatMessage(
                    "Please input a valid meowdule",
                    false,
                    0
            ));
        }
    }
    @Override
    public String[] getFill(String[] args)
    {
        return new String[]{"{FEATURE}"};
    }
}
