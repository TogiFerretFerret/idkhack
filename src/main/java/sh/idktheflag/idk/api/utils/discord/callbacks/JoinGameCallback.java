package sh.idktheflag.idk.api.utils.discord.callbacks;

import com.sun.jna.Callback;

public interface JoinGameCallback extends Callback {
    void apply(final String p0);
}
