package sh.idktheflag.idkhack.api.utils.players;

public class idkhackUser {
    private final String name;
    private final String uuid;
    private String selectedCape;

    public idkhackUser(String name, String uuid, String selectedCape) {
        this.name = name;
        this.uuid = uuid;
        this.selectedCape = selectedCape;
    }

    public String getName() {
        return name;
    }

    public String getUUID() {
        return uuid;
    }

    public String getSelectedCape() {
        return selectedCape;
    }

    public void setSelectedCape(String selectedCape) {
        this.selectedCape = selectedCape;
    }


}
