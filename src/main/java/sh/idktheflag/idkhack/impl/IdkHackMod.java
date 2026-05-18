 package sh.idktheflag.idkhack.impl;

import baritone.api.BaritoneAPI;
import sh.idktheflag.idkhack.api.event.eventbus.EventManager;
import sh.idktheflag.idkhack.api.management.SavableManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.launch.MixinBootstrap;

import javax.naming.CommunicationException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.logging.Logger;

public class IdkHackMod implements ModInitializer
{
    public static Logger LOGGER = Logger.getLogger("sn0w");

    public static long START_TIME = System.currentTimeMillis();

    public static String NAME = "IdkE";
    public final static String VERSION = "2.1.2+2";

    /**
     * Git hash of the client
     */
    public static final String HASH = "e8812e43bcb80db8";

    /**
     * The snowflake text ⚑
     * In chat this is ⚑
     */
    public static String NAME_UNICODE = "⚑";

    /**
     * Event System
     */
    public static EventManager EVENT_BUS;
    public static boolean BARITONE_AVAILABLE;

    @Override
    public void onInitialize()
    {
        BARITONE_AVAILABLE = exists("baritone.api.BaritoneAPI");

        LOGGER.info(IdkHackMod.NAME_UNICODE + "Sn0w (" + VERSION + ")");
        Register.INSTANCE = new Register();
        Register.INSTANCE.registerAll();

        SavableManager.INSTANCE.load();
        ShutdownHook.setup();
        System.out.println(getClass().getClassLoader());
        System.out.println(MixinBootstrap.class.getClassLoader());
    }

    public static boolean isBaritonePaused()
    {
        if (!IdkHackMod.BARITONE_AVAILABLE) return false;

        return BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing();
    }

    static class ShutdownHook extends Thread
    {
        public static void setup()
        {
            Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        }

        @Override
        public void run()
        {
            super.run();
            try
            {
                SavableManager.INSTANCE.save();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static boolean exists(String name)
    {
        try
        {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e)
        {
            return false;
        }
    }

}
