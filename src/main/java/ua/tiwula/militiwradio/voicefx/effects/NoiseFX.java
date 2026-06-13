package ua.tiwula.militiwradio.voicefx.effects;

import java.util.Random;

public class NoiseFX implements IVoiceEffect
{
    private static final Random random = new Random();
    private final float noiseStr;

    public NoiseFX(float noiseStr)
    {
        this.noiseStr = noiseStr;
    }
    public NoiseFX()
    {
        this.noiseStr = 800;
    }


    @Override
    public short[] apply(short[] data, float value)
    {
        float noiseLevel = value * noiseStr;

        for (int i = 0; i < data.length; i++) {

            float noise = (random.nextFloat() - 0.5f) * noiseLevel;

            int v = data[i] + (int)noise;

            data[i] = (short)v;
        }
        return data;
    }
}
