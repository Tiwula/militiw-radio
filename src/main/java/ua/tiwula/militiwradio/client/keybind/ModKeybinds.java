package ua.tiwula.militiwradio.client.keybind;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds
{
    public static final KeyMapping RADIO_PTT =
            new KeyMapping(
                    "key.militiwradio.ptt",
                    GLFW.GLFW_KEY_B,
                    "key.categories.militiwradio"
            );
    public static final KeyMapping RADIO_NEXT_CH =
            new KeyMapping(
                    "key.militiwradio.nextch",
                    GLFW.GLFW_KEY_RIGHT_BRACKET,
                    "key.categories.militiwradio"
            );
    public static final KeyMapping RADIO_PREV_CH =
            new KeyMapping(
                    "key.militiwradio.prevch",
                    GLFW.GLFW_KEY_LEFT_BRACKET,
                    "key.categories.militiwradio"
            );
    public static final KeyMapping RADIO_OPEN =
            new KeyMapping(
                    "key.militiwradio.open",
                    GLFW.GLFW_KEY_HOME,
                    "key.categories.militiwradio"
            );
}
