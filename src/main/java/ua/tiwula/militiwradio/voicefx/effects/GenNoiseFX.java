package ua.tiwula.militiwradio.voicefx.effects;

import java.util.Random;

public class GenNoiseFX implements IVoiceEffect
{
    private static final Random random = new Random();
    private final float noiseStr;

    public GenNoiseFX()
    {
        this.noiseStr = 1800;
    }
    public GenNoiseFX(float noiseStr)
    {
        this.noiseStr = noiseStr;
    }

    @Override
    public short[] apply(short[] data, float value)
    {
        for (int i = 0; i < data.length; i++)
        {
            data[i] = (short)((random.nextFloat() - 0.5f) * noiseStr);
        }
        return data;
    }
}
