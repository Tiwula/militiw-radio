package ua.tiwula.militiwradio.voicefx.effects;

import static ua.tiwula.militiwradio.voicefx.RadioDSP.lerp;

public class DistortionFX implements IVoiceEffect
{
    @Override
    public short[] apply(short[] data, float value)
    {
        float drive = lerp(1f, 1.5f, value);

        for (int i = 0; i < data.length; i++) {

            int v = (int)(data[i] * drive);

            // hard clip
            if (v > 7000) v = 7000;
            if (v < -7000) v = -7000;

            data[i] = (short)v;
        }
        return data;
    }
}
