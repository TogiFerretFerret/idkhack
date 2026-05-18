package sh.idktheflag.idk.api.event.events.player;

import sh.idktheflag.idk.api.event.Event;
import net.minecraft.entity.Entity;

public class TeamColorEvent extends Event {
    private final Entity entity;
    private int color;

    public TeamColorEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}