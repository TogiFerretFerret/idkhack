package sh.idktheflag.idk.api.event.events.world;

import lombok.Getter;
import lombok.Setter;
import sh.idktheflag.idk.api.event.Event;
import net.minecraft.world.chunk.WorldChunk;

@Getter
@Setter
public class ChunkDataEvent extends Event
{
    private WorldChunk chunk;
    boolean seenChunk;

    public ChunkDataEvent(WorldChunk chunk, boolean seenChunk)
    {
        this.chunk = chunk;
        this.seenChunk = seenChunk;

    }


}
