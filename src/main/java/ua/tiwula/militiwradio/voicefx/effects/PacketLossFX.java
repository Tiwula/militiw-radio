package ua.tiwula.militiwradio.voicefx.effects;

import java.util.Arrays;
import java.util.Random;

public class PacketLossFX implements IVoiceEffect
{
    private final float k;
    private static final Random random = new Random();

    public PacketLossFX()
    {
        this(0.35f);
    }

    public PacketLossFX(float koef)
    {
        this.k = koef;
    }

    @Override
    public short[] apply(short[] data, float value)
    {
        float lossChance = value * k;

        if (random.nextFloat() < lossChance) {

            // mute chunk
            Arrays.fill(data, (short) 0);
        }
        return data;
    }
}
