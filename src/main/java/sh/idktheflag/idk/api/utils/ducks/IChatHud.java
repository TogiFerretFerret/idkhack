package sh.idktheflag.idk.api.utils.ducks;

import net.minecraft.text.Text;

public interface IChatHud {
    void addChatMessageWithId(Text message, int id);

    void addChatMessageNoId(Text message);


}