package ua.tiwula.militiwradio.voicefx;

import de.maxhenkel.voicechat.api.opus.OpusDecoder;
import de.maxhenkel.voicechat.api.opus.OpusEncoder;
import ua.tiwula.militiwradio.voicefx.effects.IVoiceEffect;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static ua.tiwula.militiwradio.RadioVoiceChatPlugin.api;

public class RadioDSP {

    private static final Random random = new Random();

    // ===== MAIN ENTRY =====
    public static byte[] process(
            byte[] data,
            List<IVoiceEffect> effects,
            float quality)
    {
        OpusDecoder decoder = api.createDecoder();
        OpusEncoder encoder = api.createEncoder();

        short[] pcm = decoder.decode(data);

        // clamp
        quality = clamp(quality, 0f, 1f);

        for (IVoiceEffect fx : effects)
        {
            pcm = fx.apply(pcm, quality);
        }

        return encoder.encode(pcm);
    }


    // ===== helpers =====
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }
}