package sh.idktheflag.idkhack.impl;


import sh.idktheflag.idkhack.api.event.eventbus.EventManager;
import sh.idktheflag.idkhack.api.feature.Feature;
import sh.idktheflag.idkhack.api.gui.hudeditor.HudEditorGUI;
import sh.idktheflag.idkhack.api.management.*;
import sh.idktheflag.idkhack.api.management.breaks.BreakManager;
import sh.idktheflag.idkhack.api.management.notification.NotificationManager;
import sh.idktheflag.idkhack.api.management.shaders.ShaderManager;
import sh.idktheflag.idkhack.impl.features.commands.*;
import sh.idktheflag.idkhack.impl.features.hud.*;
import sh.idktheflag.idkhack.impl.features.modules.client.*;
import sh.idktheflag.idkhack.impl.features.modules.client.gui.*;
import sh.idktheflag.idkhack.impl.features.modules.combat.*;
import sh.idktheflag.idkhack.impl.features.modules.ghost.AutoRetotem;
import sh.idktheflag.idkhack.impl.features.modules.ghost.FastAnchor;
import sh.idktheflag.idkhack.impl.features.modules.ghost.FastMechs;
import sh.idktheflag.idkhack.impl.features.modules.ghost.LegitCrystal;
import sh.idktheflag.idkhack.impl.features.modules.misc.*;
import sh.idktheflag.idkhack.impl.features.modules.misc.autobreak.AutoBreak;
import sh.idktheflag.idkhack.impl.features.modules.movement.*;
import sh.idktheflag.idkhack.impl.features.modules.movement.timer.Timer;
import sh.idktheflag.idkhack.impl.features.modules.player.*;
import sh.idktheflag.idkhack.impl.features.modules.render.*;
import sh.idktheflag.idkhack.impl.features.modules.render.fader.FadeRenderer;
import sh.idktheflag.idkhack.impl.gui.ClickGui;
import net.minecraft.inventory.Inventory;

import java.util.Comparator;

public class Register
{
    public static Register INSTANCE;

    /**
     * Registers everything
     */
    public void registerAll()
    {
        registerManagers();
        registerFeatures();
        registerGui();

    }

    /**
     * Initializes the managers
     */
    public void registerManagers()
    {
        // Initalize the Event Bus
        IdkHackMod.EVENT_BUS = new EventManager();


        SavableManager.INSTANCE = new SavableManager();
        BindManager.INSTANCE = new BindManager();
        FeatureManager.INSTANCE = new FeatureManager();
        ShaderManager.INSTANCE = new ShaderManager();


        RotationManager.INSTANCE = new RotationManager();
        TPSManager.INSTANCE = new TPSManager();
        CommandManager.INSTANCE = new CommandManager();
        FriendManager.INSTANCE = new FriendManager();
        WaypointManager.INSTANCE = new WaypointManager();

        SoundManager.INSTANCE = new SoundManager();
        SearchManager.INSTANCE = new SearchManager();
        NotificationManager.INSTANCE = new NotificationManager();
        BoostManager.INSTANCE = new BoostManager();
        PriorityManager.INSTANCE = new PriorityManager();
        PacketManager.INSTANCE = new PacketManager();
        PopManager.INSTANCE = new PopManager();
        CrystalManager.INSTANCE = new CrystalManager();
        BreakManager.INSTANCE = new BreakManager();

        HitboxManager.INSTANCE = new HitboxManager();

        FadeRenderer.INSTANCE = new FadeRenderer();

    }

    /**
     * Registers the various features in the client
     */
    public void registerFeatures()
    {

        // Client modules
        FeatureManager.INSTANCE.getFeatures().add(new Manager());
        FeatureManager.INSTANCE.getFeatures().add(new HudColors());
        FeatureManager.INSTANCE.getFeatures().add(new IRC());
        FeatureManager.INSTANCE.getFeatures().add(new RPC());

        FeatureManager.INSTANCE.getFeatures().add(new FontModule());
        FeatureManager.INSTANCE.getFeatures().add(new AntiCheat());
        FeatureManager.INSTANCE.getFeatures().add(new Optimizer());
        FeatureManager.INSTANCE.getFeatures().add(new Safety());

        FeatureManager.INSTANCE.getFeatures().add(new Sn0wGui());
//        FeatureManager.INSTANCE.getFeatures().add(new KamiGui());
        FeatureManager.INSTANCE.getFeatures().add(new HudEditorModule());
//        FeatureManager.INSTANCE.getFeatures().add(new OtherGui());


        if (IdkHackMod.BARITONE_AVAILABLE)
            FeatureManager.INSTANCE.getFeatures().add(new Baritone());

        // Combat modules
        FeatureManager.INSTANCE.getFeatures().add(new KillAura());
        FeatureManager.INSTANCE.getFeatures().add(new CatAura());
        FeatureManager.INSTANCE.getFeatures().add(new AutoTotem());
        FeatureManager.INSTANCE.getFeatures().add(new AutoFeetPlace());
        FeatureManager.INSTANCE.getFeatures().add(new Lighter());
        FeatureManager.INSTANCE.getFeatures().add(new FastProjectile());
        FeatureManager.INSTANCE.getFeatures().add(new SelfTrap());
        FeatureManager.INSTANCE.getFeatures().add(new AutoTrap());
        FeatureManager.INSTANCE.getFeatures().add(new Platformer());
        FeatureManager.INSTANCE.getFeatures().add(new SelfFill());
        FeatureManager.INSTANCE.getFeatures().add(new thirty2ktp());
        FeatureManager.INSTANCE.getFeatures().add(new thirty2kaura());
        FeatureManager.INSTANCE.getFeatures().add(new Auto32k());
        FeatureManager.INSTANCE.getFeatures().add(new Anti32k());
        FeatureManager.INSTANCE.getFeatures().add(new Holefill());
        FeatureManager.INSTANCE.getFeatures().add(new AntiChainPop());
        FeatureManager.INSTANCE.getFeatures().add(new AutoAnvil());
        FeatureManager.INSTANCE.getFeatures().add(new AntiRevert());
        FeatureManager.INSTANCE.getFeatures().add(new AntiHolecamp());
        FeatureManager.INSTANCE.getFeatures().add(new AimAssist());
        FeatureManager.INSTANCE.getFeatures().add(new AutoAnchor());
        FeatureManager.INSTANCE.getFeatures().add(new BowAim());
        FeatureManager.INSTANCE.getFeatures().add(new AntiPearl());
        FeatureManager.INSTANCE.getFeatures().add(new Catcher());
        FeatureManager.INSTANCE.getFeatures().add(new AutoMace());
        FeatureManager.INSTANCE.getFeatures().add(new AutoPlacer());
        FeatureManager.INSTANCE.getFeatures().add(new Criticals());
        FeatureManager.INSTANCE.getFeatures().add(new AutoXP());

        // Player modules
        FeatureManager.INSTANCE.getFeatures().add(new Velocity());
        FeatureManager.INSTANCE.getFeatures().add(new XCarry());
        FeatureManager.INSTANCE.getFeatures().add(new FastUse());
        FeatureManager.INSTANCE.getFeatures().add(new Reach());
        FeatureManager.INSTANCE.getFeatures().add(new Yaw());
        FeatureManager.INSTANCE.getFeatures().add(new AntiPotion());
        FeatureManager.INSTANCE.getFeatures().add(new NoFall());
        FeatureManager.INSTANCE.getFeatures().add(new AutoWalk());
        FeatureManager.INSTANCE.getFeatures().add(new Phase());
        FeatureManager.INSTANCE.getFeatures().add(new AntiPackets());
        FeatureManager.INSTANCE.getFeatures().add(new Scaffold());
        FeatureManager.INSTANCE.getFeatures().add(new Tweaks());
        FeatureManager.INSTANCE.getFeatures().add(new AutoArmor());
        FeatureManager.INSTANCE.getFeatures().add(new MiddleClick());
        FeatureManager.INSTANCE.getFeatures().add(new Blink());
        FeatureManager.INSTANCE.getFeatures().add(new EntityControl());
        FeatureManager.INSTANCE.getFeatures().add(new AntiCrash());
        FeatureManager.INSTANCE.getFeatures().add(new Godmode());
        FeatureManager.INSTANCE.getFeatures().add(new AntiDesync());
        FeatureManager.INSTANCE.getFeatures().add(new NoAnim());
        FeatureManager.INSTANCE.getFeatures().add(new AutoRegear());
        FeatureManager.INSTANCE.getFeatures().add(new ChestStealer());
        FeatureManager.INSTANCE.getFeatures().add(new PingSpoof());
        FeatureManager.INSTANCE.getFeatures().add(new Nuker());
        FeatureManager.INSTANCE.getFeatures().add(new PortalGodMode());

        // Movement modules
        FeatureManager.INSTANCE.getFeatures().add(new NoSlow());
        FeatureManager.INSTANCE.getFeatures().add(new Sprint());
        FeatureManager.INSTANCE.getFeatures().add(new Flight());
        FeatureManager.INSTANCE.getFeatures().add(new FastClimb());
        FeatureManager.INSTANCE.getFeatures().add(new Parkour());
        FeatureManager.INSTANCE.getFeatures().add(new Jesus());
        FeatureManager.INSTANCE.getFeatures().add(new ElytraSwap());
        FeatureManager.INSTANCE.getFeatures().add(new LongJump());
        FeatureManager.INSTANCE.getFeatures().add(new ElytraFly());
        FeatureManager.INSTANCE.getFeatures().add(new Speed());
        FeatureManager.INSTANCE.getFeatures().add(new Timer());
        FeatureManager.INSTANCE.getFeatures().add(new TickBase());
        FeatureManager.INSTANCE.getFeatures().add(new NoAccel());
        FeatureManager.INSTANCE.getFeatures().add(new Step());
        FeatureManager.INSTANCE.getFeatures().add(new AntiVoid());
        FeatureManager.INSTANCE.getFeatures().add(new Dolphin());
        FeatureManager.INSTANCE.getFeatures().add(new IceSpeed());
        FeatureManager.INSTANCE.getFeatures().add(new BoatFly());
        FeatureManager.INSTANCE.getFeatures().add(new FastFirework());
        FeatureManager.INSTANCE.getFeatures().add(new Holesnap());
        FeatureManager.INSTANCE.getFeatures().add(new FastFall());
        FeatureManager.INSTANCE.getFeatures().add(new TpExploit());
        FeatureManager.INSTANCE.getFeatures().add(new PacketFly());

        // Render modules
        FeatureManager.INSTANCE.getFeatures().add(new NoRender());
        FeatureManager.INSTANCE.getFeatures().add(new HoleEsp());
        FeatureManager.INSTANCE.getFeatures().add(new FullBright());
        FeatureManager.INSTANCE.getFeatures().add(new Search());
        FeatureManager.INSTANCE.getFeatures().add(new ESP());
        FeatureManager.INSTANCE.getFeatures().add(new Tracers());
        FeatureManager.INSTANCE.getFeatures().add(new BlockESP());
        FeatureManager.INSTANCE.getFeatures().add(new ExtraTab());
        FeatureManager.INSTANCE.getFeatures().add(new Trails());
        FeatureManager.INSTANCE.getFeatures().add(new Freecam());
        FeatureManager.INSTANCE.getFeatures().add(new ViewClip());
        FeatureManager.INSTANCE.getFeatures().add(new Trajectories());
        FeatureManager.INSTANCE.getFeatures().add(new WeatherEditor());
        FeatureManager.INSTANCE.getFeatures().add(new Chat());
        FeatureManager.INSTANCE.getFeatures().add(new ViewModel());
        FeatureManager.INSTANCE.getFeatures().add(new Capes());
        FeatureManager.INSTANCE.getFeatures().add(new Particles());
        FeatureManager.INSTANCE.getFeatures().add(new DeathEffects());
        FeatureManager.INSTANCE.getFeatures().add(new CustomSky());
        FeatureManager.INSTANCE.getFeatures().add(new BlockHighlight());
        FeatureManager.INSTANCE.getFeatures().add(new AspectRatio());
        FeatureManager.INSTANCE.getFeatures().add(new NewChunks());
        FeatureManager.INSTANCE.getFeatures().add(new Ambience());
        FeatureManager.INSTANCE.getFeatures().add(new PhaseESP());
        FeatureManager.INSTANCE.getFeatures().add(new TimeChanger());

        // Misc modules
        FeatureManager.INSTANCE.getFeatures().add(new FakePlayer());
        FeatureManager.INSTANCE.getFeatures().add(new Spammer());
        FeatureManager.INSTANCE.getFeatures().add(new ChatSuffix());
        FeatureManager.INSTANCE.getFeatures().add(new AutoTool());
        FeatureManager.INSTANCE.getFeatures().add(new AutoBreak());
        FeatureManager.INSTANCE.getFeatures().add(new Alerts());
        FeatureManager.INSTANCE.getFeatures().add(new AutoReplenish());
        FeatureManager.INSTANCE.getFeatures().add(new AntiAFK());
        FeatureManager.INSTANCE.getFeatures().add(new MultiTask());
        FeatureManager.INSTANCE.getFeatures().add(new ExtraPlace());
        FeatureManager.INSTANCE.getFeatures().add(new Heaven());
        FeatureManager.INSTANCE.getFeatures().add(new AutoFish());
        FeatureManager.INSTANCE.getFeatures().add(new AutoReconnect());
        FeatureManager.INSTANCE.getFeatures().add(new AutoRespawn());
        FeatureManager.INSTANCE.getFeatures().add(new Announcer());
        FeatureManager.INSTANCE.getFeatures().add(new AutoEZ());
        FeatureManager.INSTANCE.getFeatures().add(new SmallShield());
        FeatureManager.INSTANCE.getFeatures().add(new Tracker());
        FeatureManager.INSTANCE.getFeatures().add(new NameHider());
        FeatureManager.INSTANCE.getFeatures().add(new Undead());
        FeatureManager.INSTANCE.getFeatures().add(new NoEntityTrace());
        FeatureManager.INSTANCE.getFeatures().add(new BetterPortals());
        FeatureManager.INSTANCE.getFeatures().add(new FastLatency());
        FeatureManager.INSTANCE.getFeatures().add(new KillSounds());
        FeatureManager.INSTANCE.getFeatures().add(new IllegalLog());
        FeatureManager.INSTANCE.getFeatures().add(new Disabler());


        // Ghost modules
        FeatureManager.INSTANCE.getFeatures().add(new LegitCrystal());
        FeatureManager.INSTANCE.getFeatures().add(new AutoRetotem());
        FeatureManager.INSTANCE.getFeatures().add(new FastAnchor());
        FeatureManager.INSTANCE.getFeatures().add(new FastMechs());


        // Hud modules
        FeatureManager.INSTANCE.getFeatures().add(new Watermark());
        FeatureManager.INSTANCE.getFeatures().add(new FeatureList());
        FeatureManager.INSTANCE.getFeatures().add(new Info());
        FeatureManager.INSTANCE.getFeatures().add(new Coords());
        FeatureManager.INSTANCE.getFeatures().add(new CSGOInfo());
        FeatureManager.INSTANCE.getFeatures().add(new ArmorHud());
        FeatureManager.INSTANCE.getFeatures().add(new BlinkDisplay());
        FeatureManager.INSTANCE.getFeatures().add(new KillNotify());
        FeatureManager.INSTANCE.getFeatures().add(new InventoryViewer());
        FeatureManager.INSTANCE.getFeatures().add(new ArrowInfo());
        FeatureManager.INSTANCE.getFeatures().add(new TotemCounter());
        FeatureManager.INSTANCE.getFeatures().add(new TextRadar());
        FeatureManager.INSTANCE.getFeatures().add(new Welcomer());
        FeatureManager.INSTANCE.getFeatures().add(new ArmorWarner());
        FeatureManager.INSTANCE.getFeatures().add(new Compass());
        FeatureManager.INSTANCE.getFeatures().add(new Display32k());
        FeatureManager.INSTANCE.getFeatures().add(new HealthBar());
        FeatureManager.INSTANCE.initModuleCounts();
        // Sort the features alphabetically
        FeatureManager.INSTANCE.getFeatures().sort(Comparator.comparing(Feature::getName));

        //Commands
        CommandManager.INSTANCE.getCommands().add(new HelpCommand());
        CommandManager.INSTANCE.getCommands().add(new BindCommand());
        CommandManager.INSTANCE.getCommands().add(new FriendCommand());
        CommandManager.INSTANCE.getCommands().add(new GrabCommand());
        CommandManager.INSTANCE.getCommands().add(new TPCommand());
        CommandManager.INSTANCE.getCommands().add(new ConfigCommand());
        CommandManager.INSTANCE.getCommands().add(new FolderCommand());
        CommandManager.INSTANCE.getCommands().add(new HClipCommand());
        CommandManager.INSTANCE.getCommands().add(new SpammerCommand());
        CommandManager.INSTANCE.getCommands().add(new SearchCommand());
        CommandManager.INSTANCE.getCommands().add(new ClearPopsCommand());
        CommandManager.INSTANCE.getCommands().add(new AutoRegearCommand());
        CommandManager.INSTANCE.getCommands().add(new WaypointCommand());
        CommandManager.INSTANCE.getCommands().add(new MathCommand());
        CommandManager.INSTANCE.getCommands().add(new BoatKillCommand());
        CommandManager.INSTANCE.getCommands().add(new DupeCommand());

        CommandManager.INSTANCE.getCommands().add(new ToggleCommand());

    }

    public void registerGui()
    {
        // register the types of guis
        ClickGui.INSTANCE = new ClickGui();
        HudEditorGUI.INSTANCE = new HudEditorGUI();

        //register the actual gui
        Sn0wGui.INSTANCE.registerGUI();

        HudEditorModule.INSTANCE.registerGUI();
    }

}
