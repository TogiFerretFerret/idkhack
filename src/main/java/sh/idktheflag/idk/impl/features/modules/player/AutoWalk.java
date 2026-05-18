package sh.idktheflag.idk.impl.features.modules.player;

import sh.idktheflag.idk.api.event.eventbus.SubscribeEvent;
import sh.idktheflag.idk.api.event.events.key.InputEvent;
import sh.idktheflag.idk.api.feature.module.Module;
import sh.idktheflag.idk.api.utils.NullUtils;


public class AutoWalk extends Module {

    public AutoWalk()
    {
        super("AutoWalk", Category.Player);
    }


    @SubscribeEvent
    public void onUpdate(InputEvent event)
    {
        if (NullUtils.nullCheck()) return;

        event.input.playerInput = new net.minecraft.util.PlayerInput(
            true, 
            event.input.playerInput.backward(), 
            event.input.playerInput.left(), 
            event.input.playerInput.right(), 
            event.input.playerInput.jump(), 
            event.input.playerInput.sneak(), 
            event.input.playerInput.sprint()
        );
    }


    @Override
    public String getDescription()
    {
        return "AutoWalk: Walks forward automatically";
    }
}