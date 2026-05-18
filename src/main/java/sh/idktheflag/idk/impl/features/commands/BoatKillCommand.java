package sh.idktheflag.idk.impl.features.commands;

import sh.idktheflag.idk.api.command.Command;
import sh.idktheflag.idk.api.management.PacketManager;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class BoatKillCommand extends Command
{


    public BoatKillCommand()
    {
        super("BoatKill", "kills you with boats", new String[]{"boatkill"});
    }

    @Override
    public void run(String[] args)
    {
        if (!(mc.player.getVehicle() instanceof BoatEntity boat)) return;


        Vec3d originalPos = new Vec3d(boat.getX(), boat.getY(), boat.getZ());
        boat.setPosition(originalPos.add(0, 0.05, 0));
        VehicleMoveC2SPacket groundPacket = VehicleMoveC2SPacket.fromVehicle(boat);
        boat.setPosition(originalPos.add(0, 20, 0));
        VehicleMoveC2SPacket skyPacket = VehicleMoveC2SPacket.fromVehicle(boat);
        boat.setPosition(originalPos);
        for (int i = 0; i < 20; i++)
        {
            PacketManager.INSTANCE.sendPacket(skyPacket);
            PacketManager.INSTANCE.sendPacket(groundPacket);
        }
        PacketManager.INSTANCE.sendPacket(VehicleMoveC2SPacket.fromVehicle(boat));
    }

}
