package ua.tiwula.militiwradio.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ua.tiwula.militiwradio.MilitiwRadio;
import ua.tiwula.militiwradio.item.RadioItem;
import ua.tiwula.militiwradio.network.ModNetwork;
import ua.tiwula.militiwradio.network.packet.SetChannelFreqPacket;
import ua.tiwula.militiwradio.network.packet.SetChannelPacket;
import ua.tiwula.militiwradio.sound.ModSounds;

import static org.lwjgl.glfw.GLFW.*;

public class RadioScreen extends Screen
{
    private final StringBuilder input = new StringBuilder();
    private final boolean state;
    private byte selectedCh = 1;
    private final RadioItem item;
    private final CompoundTag tag;
    private boolean inCurios;
    private final short[] chs;


    private static ResourceLocation TEXTURE;

    public RadioScreen(ItemStack stack, RadioItem item, boolean inCurios)
    {
        super(Component.literal("Radio"));
        this.inCurios = inCurios;
        this.item = item;
        tag = stack.getOrCreateTag();
        this.state = tag.getBoolean("on");
        this.selectedCh = tag.getByte("selected_ch");
        chs = new short[item.MAX_CHANNELS];
        for (int i = 0; i < item.MAX_CHANNELS; i++)
        {
            chs[i] = tag.getShort("ch"+(i+1));
        }
        TEXTURE = ResourceLocation.fromNamespaceAndPath(MilitiwRadio.MODID,
                state ? "textures/gui/radio_on.png" : "textures/gui/radio_off.png");
    }

    //@Override
    //protected void renderBackground(GuiGraphics gfx, float partialTick, int mouseX, int mouseY) {
    //    gfx.blit(TEXTURE, (this.width - 114) / 2, (this.width - 497) / 2, 0, 0, 114, 497);
    //}

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (state)
        {
            if (keyCode == GLFW_KEY_ENTER && !input.isEmpty())
            {
                chs[selectedCh -1] = Short.parseShort(input.toString());
                if (chs[selectedCh -1] < item.MIN_FREQ) chs[selectedCh -1] = item.MIN_FREQ;
                if (chs[selectedCh -1] > item.MAX_FREQ) chs[selectedCh -1] = item.MAX_FREQ;
                input.setLength(0);
                Minecraft.getInstance().player.playSound(ModSounds.FREQ.get(), 1.0f, 1.0f);
                ModNetwork.CHANNEL.sendToServer(
                        new SetChannelFreqPacket(chs[selectedCh -1], inCurios));
                return true;
            }
            else if (keyCode == GLFW_KEY_BACKSPACE)
            {
                if (!input.isEmpty())
                {
                    input.deleteCharAt(input.length() - 1);
                }
                else
                {
                    chs[selectedCh -1] = 0;
                    ModNetwork.CHANNEL.sendToServer(
                            new SetChannelFreqPacket(chs[selectedCh -1], inCurios));
                }
                return true;
            }
            else if (keyCode == GLFW_KEY_LEFT && selectedCh > 1)
            {
                selectedCh--;
                Minecraft.getInstance().player.playSound(ModSounds.FREQ.get(), 1.0f, 1.0f);
                ModNetwork.CHANNEL.sendToServer(
                        new SetChannelPacket(selectedCh, inCurios));
            }
            else if (keyCode == GLFW_KEY_RIGHT && selectedCh < item.MAX_CHANNELS)
            {
                selectedCh++;
                Minecraft.getInstance().player.playSound(ModSounds.FREQ.get(), 1.0f, 1.0f);
                ModNetwork.CHANNEL.sendToServer(
                        new SetChannelPacket(selectedCh, inCurios));
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {

        if (Character.isDigit(chr) && state && input.length() < 3) {
            input.append(chr);
            return true;
        }

        return super.charTyped(chr, modifiers);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(gfx);
        int texW = 256;
        int texH = 256;

        float scale = Math.min(width / texW, height / texH)*2;

        float guiW = texW * scale;
        float guiH = texH * scale;

        float x = (width - guiW) / 2f;
        float y = (height - guiH) / 2f;

        gfx.pose().pushPose();

        gfx.pose().translate(x, y, 0);
        gfx.pose().scale(scale, scale, 1f);

        gfx.blit(TEXTURE, 0, 0, 0, 0, texW, texH, texW, texH);

        if (state)
        {
            gfx.drawString(font,
                    "CH:"+ selectedCh,
                    115,
                    118,
                    0x2C4041,
                    false
            );
            gfx.drawString(font,
                    input.isEmpty() ? (chs[selectedCh -1] == 0 ? "EMPTY" : "[" + chs[selectedCh -1] + "]") : ">" + input,
                    115,
                    118+font.lineHeight,
                    0x2C4041,
                    false
            );
        }
        gfx.pose().popPose();

        super.render(gfx, mouseX, mouseY, partialTick);

    }

    /*@Override
    protected void renderBg(GuiGraphics gfx)
    {

    }*/

}
