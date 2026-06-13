package ua.tiwula.militiwradio.voicefx.effects;

public interface IVoiceEffect
{
    short[] apply(short[] data, float value);
}
