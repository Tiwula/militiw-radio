package ua.tiwula.militiwradio.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import ua.tiwula.militiwradio.MilitiwRadio;
import ua.tiwula.militiwradio.item.RadioItem;
import ua.tiwula.militiwradio.network.RadioManager;

import java.util.function.Supplier;

import static ua.tiwula.militiwradio.util.Util.findRadio;

public class SetChannelPacket
{
    private final byte channel;
    private final boolean isCuriosSlot;

    public SetChannelPacket(byte channel, boolean isCuriosSlot) {
        this.channel = channel;
        this.isCuriosSlot = isCuriosSlot;
    }

    public static void encode(SetChannelPacket msg, FriendlyByteBuf buf)
    {
        buf.writeByte(msg.channel);
        buf.writeBoolean(msg.isCuriosSlot);
    }

    public static SetChannelPacket decode(FriendlyByteBuf buf)
    {
        return new SetChannelPacket(buf.readByte(), buf.readBoolean());
    }

    public static void handle(SetChannelPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();

            if (player == null)
                return;

            ItemStack stack = findRadio(player, msg.isCuriosSlot);
            if (stack.isEmpty()) return;
            CompoundTag tag = stack.getOrCreateTag();
            if (!tag.getBoolean("on")) return;
            short freq = RadioItem.getFrequency(stack);
            if (freq != 0)
                RadioManager.removeListener(freq, player.getUUID());
            tag.putByte("selected_ch", msg.channel);
            freq = tag.getShort("ch"+msg.channel);
            if (freq != 0)
            {
                RadioManager.addListener(freq, player.getUUID());
                if (MilitiwRadio.DEBUG)
                    player.sendSystemMessage(Component.literal("Listening on " + freq));
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
