package sh.idktheflag.idkhack.api.command;

import sh.idktheflag.idkhack.api.wrapper.IMinecraft;
import sh.idktheflag.idkhack.impl.IdkHackMod;

public abstract class Command implements IMinecraft {

    String name;
    String desc;
    String[] alias;

    public Command(String name, String desc, String[] alias){
        this.name = name;
        this.desc = desc;
        this.alias = alias;
        IdkHackMod.EVENT_BUS.register(this);
    }

    public abstract void run(String[] args);

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getAlias() {
        return alias;
    }

    public String[] getFill(String args[]){
        return new String[]{};
    }
}
