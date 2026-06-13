package ua.tiwula.militiwradio.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import ua.tiwula.militiwradio.MilitiwRadio;
import ua.tiwula.militiwradio.network.RadioManager;

import java.util.function.Supplier;

import static ua.tiwula.militiwradio.util.Util.findRadio;

public class SetChannelFreqPacket
{
    private final short freq;
    private final boolean isCuriosSlot;

    public SetChannelFreqPacket(short freq, boolean isCuriosSlot)
    {
        this.freq = freq;
        this.isCuriosSlot = isCuriosSlot;
    }

    public static void encode(SetChannelFreqPacket msg, FriendlyByteBuf buf)
    {
        buf.writeShort(msg.freq);
        buf.writeBoolean(msg.isCuriosSlot);
    }

    public static SetChannelFreqPacket decode(FriendlyByteBuf buf)
    {
        return new SetChannelFreqPacket(buf.readShort(), buf.readBoolean());
    }

    public static void handle(SetChannelFreqPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();

            if (player == null) return;

            ItemStack stack = findRadio(player, msg.isCuriosSlot);
            if (stack.isEmpty()) return;
            CompoundTag tag = stack.getOrCreateTag();
            byte ch = tag.getByte("selected_ch");
            short freq = tag.getShort("ch"+ch);
            if (freq != 0)
                RadioManager.removeListener(freq, player.getUUID());
            tag.putShort("ch"+ch, msg.freq);
            if (msg.freq != 0)
            {
                RadioManager.addListener(msg.freq, player.getUUID());
                if (MilitiwRadio.DEBUG)
                    player.sendSystemMessage(Component.literal("Listening on " + msg.freq));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
