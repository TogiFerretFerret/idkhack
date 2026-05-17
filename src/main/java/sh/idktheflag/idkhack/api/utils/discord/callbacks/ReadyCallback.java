package sh.idktheflag.idkhack.api.utils.discord.callbacks;

import com.sun.jna.Callback;
import sh.idktheflag.idkhack.api.utils.discord.DiscordUser;

public interface ReadyCallback extends Callback {
    void apply(final DiscordUser p0);
}
