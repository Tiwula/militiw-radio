package ua.tiwula.militiwradio.voicefx.effects;

import java.util.Random;

public class CorruptionFX implements IVoiceEffect
{
    private final float chance;
    private static final Random random = new Random();

    public CorruptionFX()
    {
        this(1.01f);
    }

    public CorruptionFX(float chance)
    {
        this.chance = chance; //0.01f
    }



    @Override
    public short[] apply(short[] data, float value)
    {
        float vChance = value * chance;

        for (int i = 0; i < data.length; i++) {

            if (random.nextFloat() < vChance) {

                data[i] += (short)(random.nextInt(2000) - 1000);
            }
        }
        return data;
    }
}
