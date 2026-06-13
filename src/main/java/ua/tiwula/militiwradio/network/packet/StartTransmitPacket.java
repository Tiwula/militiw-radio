package ua.tiwula.militiwradio.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import ua.tiwula.militiwradio.MilitiwRadio;
import ua.tiwula.militiwradio.util.Util;
import ua.tiwula.militiwradio.item.RadioItem;
import ua.tiwula.militiwradio.network.RadioManager;
import ua.tiwula.militiwradio.sound.ModSounds;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class StartTransmitPacket
{
    private final short freq;

    public StartTransmitPacket(short freq) {
        this.freq = freq;
    }

    public static void encode(StartTransmitPacket msg, FriendlyByteBuf buf)
    {
        buf.writeShort(msg.freq);
    }

    public static StartTransmitPacket decode(FriendlyByteBuf buf)
    {
        return new StartTransmitPacket(buf.readShort());
    }

    public static void handle(StartTransmitPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();

            if (player == null)
                return;

            ItemStack stack = Util.getCuriosRadio(player);
            int range = ((RadioItem)stack.getItem()).RANGE;
            if (!stack.isEmpty() && stack.hasTag() && RadioItem.getFrequency(stack) != 0 && stack.getTag().getBoolean("on"))
            {
                RadioManager.startTalking(player.getUUID(), msg.freq);
                PlayerList list = Objects.requireNonNull(ctx.get().getSender()).server.getPlayerList();
                for (UUID listener : RadioManager.getListeners(msg.freq))
                {
                    if (player.getUUID().equals(listener)) continue;
                    ServerPlayer receiver = list.getPlayer(listener);
                    if (receiver == null) continue;
                    float distance = player.distanceTo(receiver);
                    if (distance > range) continue;
                    receiver.playNotifySound(ModSounds.START.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                }
                if (MilitiwRadio.DEBUG)
                    player.sendSystemMessage(Component.literal("Transmitting to " + msg.freq));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
