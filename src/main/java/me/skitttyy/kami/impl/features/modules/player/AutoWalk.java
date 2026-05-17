package me.skitttyy.kami.impl.features.modules.player;

import me.skitttyy.kami.api.event.eventbus.SubscribeEvent;
import me.skitttyy.kami.api.event.events.key.InputEvent;
import me.skitttyy.kami.api.feature.module.Module;
import me.skitttyy.kami.api.utils.NullUtils;


public class AutoWalk extends Module {

    public AutoWalk()
    {
        super("AutoWalk", Category.Player);
    }


    @SubscribeEvent
    public void onUpdate(InputEvent event)
    {
        if (NullUtils.nullCheck()) return;

        // TODO: port to 1.21.11 - event.input.getMovementInput().y++;
    }


    @Override
    public String getDescription()
    {
        return "AutoWalk: Walks forward automatically";
    }
}