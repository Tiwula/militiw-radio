package ua.tiwula.militiwradio.network.packet;

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

public class ToggleRadioPacket
{
    private final boolean state;
    private final boolean isCuriosSlot;

    public ToggleRadioPacket(boolean state, boolean isCuriosSlot)
    {
        this.state = state;
        this.isCuriosSlot = isCuriosSlot;
    }

    public static void encode(ToggleRadioPacket msg, FriendlyByteBuf buf)
    {
        buf.writeBoolean(msg.state);
        buf.writeBoolean(msg.isCuriosSlot);
    }

    public static ToggleRadioPacket decode(FriendlyByteBuf buf)
    {
        return new ToggleRadioPacket(buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(ToggleRadioPacket msg, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();

            if (player == null)
                return;

            ItemStack stack = findRadio(player, msg.isCuriosSlot);
            if (stack.isEmpty()) return;

            stack.getOrCreateTag().putBoolean("on", msg.state);
            if (msg.state)
                if (RadioItem.getFrequency(stack) != 0)
                    RadioManager.addListener(RadioItem.getFrequency(stack), player.getUUID());
            else
            {
                RadioManager.removeListener(RadioItem.getFrequency(stack), player.getUUID());
                RadioManager.stopTalking(player.getUUID());
            }

            if (MilitiwRadio.DEBUG)
                player.sendSystemMessage(Component.literal((msg.state ? "Start" : "Stop")+" listening on "+ RadioItem.getFrequency(stack)));
        });

        ctx.get().setPacketHandled(true);
    }
}
