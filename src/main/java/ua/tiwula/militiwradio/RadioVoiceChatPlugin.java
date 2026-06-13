package ua.tiwula.militiwradio;

import de.maxhenkel.voicechat.api.*;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.events.VoicechatServerStartedEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.tiwula.militiwradio.item.RadioItem;
import ua.tiwula.militiwradio.network.ModNetwork;
import ua.tiwula.militiwradio.network.RadioManager;
import ua.tiwula.militiwradio.network.packet.VoicePingS2CPacket;
import ua.tiwula.militiwradio.util.Util;
import ua.tiwula.militiwradio.voicefx.RadioDSP;
import ua.tiwula.militiwradio.voicefx.effects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ForgeVoicechatPlugin
public class RadioVoiceChatPlugin implements VoicechatPlugin
{
    public static VoicechatServerApi api;

    private static boolean wasTalking;

    @Override
    public String getPluginId()
    {
        return MilitiwRadio.MODID;
    }

    @Override
    public void initialize(VoicechatApi api)
    {

    }

    @Override
    public void registerEvents(EventRegistration registration)
    {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicPacket);
        registration.registerEvent(VoicechatServerStartedEvent.class, this::onServerStarted);
    }

    private void onServerStarted(VoicechatServerStartedEvent event)
    {

        api = event.getVoicechat();
        /*
        VolumeCategory speakers = api.volumeCategoryBuilder()
                .setId(SPEAKER_CATEGORY)
                .setName("Speakers")
                .setDescription("The volume of all speakers")
                .setIcon(getIcon("assets/walkietalkie/textures/block/speaker.png"))
                .build();
        api.registerVolumeCategory(speakers);*/
    }

    private void onMicPacket(MicrophonePacketEvent event)
    {
        //log.debug("MIC PACKET");
        VoicechatConnection senderConnection = event.getSenderConnection();

        if (senderConnection == null) return;

        ServerPlayer senderPlayer = (ServerPlayer) senderConnection.getPlayer().getPlayer();
        //((Player) senderConnection.getPlayer()).sendSystemMessage(Component.literal(String.valueOf(senderPlayer == null)));
        if (senderPlayer == null) return;

        boolean talking = RadioManager.isTalking(senderPlayer.getUUID());

        /*if (talking && !wasTalking)
        {
            //senderPlayer.getPlayer().(Component.literal("Start transmitting voice..."));
        }

        if (!talking && wasTalking)
        {
            //senderPlayer.sendSystemMessage(Component.literal("Stop transmitting voice..."));
        }

        wasTalking = talking;*/
        if (!talking) return;

        ItemStack senderStack = Util.getCuriosRadio(senderPlayer);
        if (senderStack.isEmpty()) return;

        short freq = RadioItem.getFrequency(senderStack);
        if (freq == 0) return;


        MinecraftServer server = senderPlayer.getServer();
        if (server == null) return;
        PlayerList list = server.getPlayerList();

        //senderPlayer.sendSystemMessage(Component.literal("Transmitting audio"));
        for (UUID listener : RadioManager.getListeners(freq))
        {
            if (listener.equals(senderPlayer.getUUID())) continue;

            ServerPlayer receiver = list.getPlayer(listener);
            if (receiver == null) continue;
            if (RadioManager.isTalking(listener)) continue;
            float distance = senderPlayer.distanceTo(receiver);
            int range = ((RadioItem)senderStack.getItem()).RANGE;
            if (distance > range) continue;
            float quality = distance/range;

            VoicechatConnection connection = api.getConnectionOf(listener);
            if (connection == null) continue;

            // TEMP //
            List<IVoiceEffect> fx = new ArrayList<>();
            //fx.add(new FilterFX());
            fx.add(new VolumeBoostFX());
            fx.add(new NoiseFX());
            fx.add(new PacketLossFX());
            //fx.add(new BandLimitFX());

            byte[] opusData = RadioDSP.process(event.getPacket().getOpusEncodedData(), fx, quality);//applyFilter(event.getPacket().getOpusEncodedData(), quality);

            ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> receiver), new VoicePingS2CPacket(freq));

            api.sendStaticSoundPacketTo(
                    api.getConnectionOf(listener),
                    event.getPacket()
                            .staticSoundPacketBuilder()
                            .opusEncodedData(opusData)
                            .build()
            );
        }
    }

    /*public byte[] applyFilter(byte[] data, float quality)
    {
        OpusDecoder decoder = api.createDecoder();
        OpusEncoder encoder = api.createEncoder();

        short[] pcmData = decoder.decode(data);

        byte[] opusData = encoder.encode();

        decoder.close();
        encoder.close();

        return opusData;
    }*/
}
