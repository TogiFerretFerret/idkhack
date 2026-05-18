package sh.idktheflag.idk.api.utils.discord.callbacks;

import com.sun.jna.Callback;
import sh.idktheflag.idk.api.utils.discord.DiscordUser;

public interface ReadyCallback extends Callback {
    void apply(final DiscordUser p0);
}
