package sh.idktheflag.idkhack.api.utils.ducks;


public interface IChatHudLine {
    String getMessageText();

    int getOverrideId();

    void setOverrideId(int id);
    
}