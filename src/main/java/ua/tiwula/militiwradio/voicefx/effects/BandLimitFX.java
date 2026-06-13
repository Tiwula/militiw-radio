package ua.tiwula.militiwradio.voicefx.effects;

public class BandLimitFX implements IVoiceEffect
{
    @Override
    public short[] apply(short[] data, float value)
    {
        // simple high-pass approximation (removes bass)
        short prev = 0;

        for (int i = 0; i < data.length; i++) {

            short cur = data[i];

            short filtered = (short)(cur - prev * 0.92f);

            prev = cur;

            data[i] = filtered;
        }
        return data;
    }
}
