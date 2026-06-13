package ua.tiwula.militiwradio.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import ua.tiwula.militiwradio.client.RadioOverlay;

import java.util.function.Supplier;

public class VoicePingS2CPacket
{
    private final short freq;

    public VoicePingS2CPacket(short freq)
    {
        this.freq = freq;
    }

    public static void encode(VoicePingS2CPacket msg, FriendlyByteBuf buf)
    {
        buf.writeShort(msg.freq);
    }

    public static VoicePingS2CPacket decode(FriendlyByteBuf buf)
    {
        return new VoicePingS2CPacket(buf.readShort());
    }

    public static void handle(VoicePingS2CPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            RadioOverlay.pingReceive(msg.freq);
        });

        ctx.get().setPacketHandled(true);
    }
}
