package sh.idktheflag.idk.api.management.accounts;

import java.util.HashMap;
import java.util.Map;

public class AccountCache {
    public String username = "";
    public String uuid = "";

    public Map<String, Object> save() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("uuid", uuid);
        return map;
    }

    public void load(Map<String, Object> map) {
        if (map == null) return;
        username = (String) map.getOrDefault("username", "");
        uuid = (String) map.getOrDefault("uuid", "");
    }
}
