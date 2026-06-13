package ua.tiwula.militiwradio.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import ua.tiwula.militiwradio.network.packet.*;

public class ModNetwork
{
    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL =
            NetworkRegistry.newSimpleChannel(
                    ResourceLocation.fromNamespaceAndPath("militiwradio", "main"),
                    () -> PROTOCOL,
                    PROTOCOL::equals,
                    PROTOCOL::equals
            );

    public static void register() {

        int id = 0;

        CHANNEL.registerMessage(
                id++,
                ToggleRadioPacket.class,
                ToggleRadioPacket::encode,
                ToggleRadioPacket::decode,
                ToggleRadioPacket::handle
        );

        CHANNEL.registerMessage(
                id++,
                SetChannelPacket.class,
                SetChannelPacket::encode,
                SetChannelPacket::decode,
                SetChannelPacket::handle
        );

        CHANNEL.registerMessage(
                id++,
                SetChannelFreqPacket.class,
                SetChannelFreqPacket::encode,
                SetChannelFreqPacket::decode,
                SetChannelFreqPacket::handle
        );

        CHANNEL.registerMessage(
                id++,
                StartTransmitPacket.class,
                StartTransmitPacket::encode,
                StartTransmitPacket::decode,
                StartTransmitPacket::handle
        );

        CHANNEL.registerMessage(
                id++,
                StopTransmitPacket.class,
                StopTransmitPacket::encode,
                StopTransmitPacket::decode,
                StopTransmitPacket::handle
        );

        CHANNEL.registerMessage(
                id++,
                VoicePingS2CPacket.class,
                VoicePingS2CPacket::encode,
                VoicePingS2CPacket::decode,
                VoicePingS2CPacket::handle
        );
    }
}
