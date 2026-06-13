package ua.tiwula.militiwradio.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ua.tiwula.militiwradio.MilitiwRadio;
import ua.tiwula.militiwradio.client.keybind.ModKeyBindingsEvents;
import ua.tiwula.militiwradio.util.OverlayLine;
import ua.tiwula.militiwradio.util.Util;
import ua.tiwula.militiwradio.item.RadioItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(
        modid = MilitiwRadio.MODID,
        value = Dist.CLIENT
)
public class RadioOverlay
{
    private static final Map<Short, Long> lastPing = new ConcurrentHashMap<>();
    private static long lastTrans;

    public static void pingReceive(short freq)
    {
        lastPing.put(freq, System.currentTimeMillis());
    }

    private static void drawBox(GuiGraphics gui, Font font, List<OverlayLine> lines, int x, int y, int w)
    {
        gui.fill(
                x,
                y,
                w+x+10,
                y+(font.lineHeight* lines.size())+10,
                0x05000000
        );

        for (int i = 0; i < lines.size(); i++)
        {
            gui.drawString(
                    font,
                    lines.get(i).line(),
                    x+5,
                    y+5+(font.lineHeight*i),
                    lines.get(i).color(),
                    false
            );
        }

    }

    @SubscribeEvent
    public static void onHud(RenderGuiOverlayEvent.Post event)
    {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;



        ItemStack stack = Util.getCuriosRadio(mc.player);
        if (stack.isEmpty()) return;

        GuiGraphics gui = event.getGuiGraphics();
        int x = 10;
        int y = gui.guiHeight()/2 - 30;
        if (stack.getTag() == null) return;

        if (ModKeyBindingsEvents.isTransmitting())
        {
            List<OverlayLine> transmitLines = new ArrayList<>();
            transmitLines.add(new OverlayLine("TRANSMITTING", 0xAAAA00));
            transmitLines.add(new OverlayLine("CH:" + stack.getTag().getShort("selected_ch"), 0xAAAAAA));
            transmitLines.add(new OverlayLine("FREQ:" + RadioItem.getFrequency(stack), 0xAAAAAA));

            drawBox(gui, mc.font, transmitLines, x, y, 200);
            lastTrans = System.currentTimeMillis();
        }
        else if (System.currentTimeMillis() - lastTrans < 1000)
        {
            List<OverlayLine> transmitLines = new ArrayList<>();
            transmitLines.add(new OverlayLine("TRANSMITTED", 0x00AA00));
            transmitLines.add(new OverlayLine("CH:" + stack.getTag().getShort("selected_ch"), 0xAAAAAA));
            transmitLines.add(new OverlayLine("FREQ:" + RadioItem.getFrequency(stack), 0xAAAAAA));

            drawBox(gui, mc.font, transmitLines, x, y, 200);
        }

        if(!lastPing.isEmpty())
        {
            List<OverlayLine> receiveLines = new ArrayList<>();
            receiveLines.add(new OverlayLine("RECEIVING", 0x00AA00));
            for (Map.Entry<Short, Long> ping : lastPing.entrySet())
            {
                if (System.currentTimeMillis() - ping.getValue() < 500)
                    receiveLines.add(new OverlayLine(String.valueOf(ping.getKey()), 0xAAAAAA));
                else
                    lastPing.remove(ping.getKey());
            }

            drawBox(gui, mc.font, receiveLines, x, y + (mc.font.lineHeight * 3) + 20, 200);
        }
    }
}
