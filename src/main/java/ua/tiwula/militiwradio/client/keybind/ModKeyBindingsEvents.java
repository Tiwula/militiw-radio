package ua.tiwula.militiwradio.client.keybind;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ua.tiwula.militiwradio.MilitiwRadio;
import ua.tiwula.militiwradio.item.RadioItem;
import ua.tiwula.militiwradio.network.ModNetwork;
import ua.tiwula.militiwradio.network.packet.SetChannelPacket;
import ua.tiwula.militiwradio.network.packet.StartTransmitPacket;
import ua.tiwula.militiwradio.network.packet.StopTransmitPacket;
import ua.tiwula.militiwradio.client.screen.RadioScreen;
import ua.tiwula.militiwradio.sound.ModSounds;
import ua.tiwula.militiwradio.util.Util;

import java.util.Objects;

@Mod.EventBusSubscriber(
        modid = MilitiwRadio.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class ModKeyBindingsEvents
{
    private static boolean wasPressed;

    private static boolean transmitting;

    public static boolean isTransmitting()
    {
        return transmitting;
    }

    private static void checkTransmitting(ItemStack radio)
    {
        boolean pressed = ModKeybinds.RADIO_PTT.isDown();

        short freq = RadioItem.getFrequency(radio);
        if (freq == 0 || !Objects.requireNonNull(radio.getTag()).getBoolean("on")) return;

        if (pressed && !wasPressed)
        {
            ModNetwork.CHANNEL.sendToServer(new StartTransmitPacket(freq));
            transmitting = true;
            Minecraft.getInstance().player.playSound(ModSounds.START.get(), 1.0f, 1.0f);
        }

        if (!pressed && wasPressed)
        {
            ModNetwork.CHANNEL.sendToServer(new StopTransmitPacket(freq));
            transmitting = false;
            Minecraft.getInstance().player.playSound(ModSounds.START.get(), 1.0f, 1.0f);
        }

        wasPressed = pressed;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        ItemStack radio = Util.getCuriosRadio(Minecraft.getInstance().player);

        if (radio.isEmpty()) return;
        CompoundTag tag = radio.getTag();
        if (tag == null) return;

        checkTransmitting(radio);

        while (ModKeybinds.RADIO_OPEN.consumeClick())
        {
            Minecraft.getInstance().setScreen(new RadioScreen(radio, (RadioItem) radio.getItem(), true));
        }

        while (ModKeybinds.RADIO_NEXT_CH.consumeClick())
        {
            if (!tag.getBoolean("on")) break;
            byte curCh = tag.getByte("selected_ch");
            byte maxCh = ((RadioItem) radio.getItem()).MAX_CHANNELS;
            if (curCh < maxCh)
                curCh++;
            else
                curCh = 1;
            Minecraft.getInstance().player.playSound(ModSounds.FREQ.get(), 1.0f, 1.0f);
            Minecraft.getInstance().player.displayClientMessage(Component.literal("[CH:"+curCh+"]"), true);
            ModNetwork.CHANNEL.sendToServer(
                    new SetChannelPacket(curCh, true));
        }
        while (ModKeybinds.RADIO_PREV_CH.consumeClick())
        {
            if (!tag.getBoolean("on")) break;
            byte curCh = tag.getByte("selected_ch");
            byte maxCh = ((RadioItem) radio.getItem()).MAX_CHANNELS;
            if (curCh > 1)
                curCh--;
            else
                curCh = maxCh;
            Minecraft.getInstance().player.playSound(ModSounds.FREQ.get(), 1.0f, 1.0f);
            Minecraft.getInstance().player.displayClientMessage(Component.literal("[CH:"+curCh+"]"), true);
            ModNetwork.CHANNEL.sendToServer(
                    new SetChannelPacket(curCh, true));
        }
    }
}
