package ua.tiwula.militiwradio.voicefx.effects;

public class FilterFX implements IVoiceEffect
{
    @Override
    public short[] apply(short[] data, float value)
    {
        for (int i = 1; i < data.length-1; i++)
        {
            data[i] = (short)((data[i - 1] + data[i] + data[i + 1]) / 3);
        }
        return data;
    }
}
