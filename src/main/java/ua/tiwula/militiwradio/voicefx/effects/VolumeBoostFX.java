package ua.tiwula.militiwradio.voicefx.effects;

public class VolumeBoostFX implements IVoiceEffect
{
    private final float str;
    private final float drive;
    private final int threshold;
    private final int ratio;

    public VolumeBoostFX()
    {
        this(3f, 3f, 4, 12000);
    }

    public VolumeBoostFX(float str, float drive, int threshold, int ratio)
    {
        this.str = str;
        this.drive = drive;
        this.threshold = threshold;
        this.ratio = ratio;
    }

    @Override
    public short[] apply(short[] data, float value)
    {
        for (int i = 0; i < data.length; i++)
        {
            /*if (Math.abs(data[i]) > threshold)
            {
                data[i] = (short)(threshold + (data[i] - threshold) / ratio);
            }*/

            int sample = (int)(data[i] * str);
            if (sample > Short.MAX_VALUE)
            {
                sample = Short.MAX_VALUE;
            }
            else if (sample < Short.MIN_VALUE)
            {
                sample = Short.MIN_VALUE;
            }
            data[i] = (short) sample;

            /*double normalized = data[i] / 32768.0;
            normalized = Math.tanh(normalized * drive);
            data[i] = (short)(normalized * 32767);*/
        }

        return data;
    }
}
