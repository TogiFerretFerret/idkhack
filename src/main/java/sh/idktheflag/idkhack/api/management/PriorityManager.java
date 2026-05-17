package sh.idktheflag.idkhack.api.management;

import sh.idktheflag.idkhack.impl.features.modules.misc.autobreak.AutoBreak;

public class PriorityManager {

    public static PriorityManager INSTANCE;

    public boolean usageLock;
    public String usageLockCause;

    public PriorityManager()
    {
        this.usageLock = false;
        this.usageLockCause = "NONE";
    }


    public void lockUsageLock(String cause)
    {
        this.usageLock = true;
        this.usageLockCause = cause;
    }

    public void unlockUsageLock()
    {
        this.usageLock = false;
        this.usageLockCause = "NONE";
    }

    public boolean isUsageLocked()
    {
        return this.usageLock;
    }
}
