package sh.idktheflag.idkhack.api.utils.discord;

import java.util.Arrays;
import java.util.List;
import com.sun.jna.Structure;
import sh.idktheflag.idkhack.api.utils.discord.callbacks.*;

public class DiscordEventHandlers extends Structure {
    public DisconnectedCallback disconnected;
    public JoinRequestCallback joinRequest;
    public SpectateGameCallback spectateGame;
    public ReadyCallback ready;
    public ErroredCallback errored;
    public JoinGameCallback joinGame;
    
    protected List<String> getFieldOrder() {
        return Arrays.asList("ready", "disconnected", "errored", "joinGame", "spectateGame", "joinRequest");
    }
}