package sh.idktheflag.idkhack.api.utils.world;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sh.idktheflag.idkhack.impl.features.modules.client.AntiCheat;

@Getter
@Setter
@AllArgsConstructor
public class AntiFeetPlaceResult
{
    boolean placeAvailable;
    boolean isAntiFeetPlace;
}
