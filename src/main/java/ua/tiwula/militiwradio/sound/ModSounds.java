package ua.tiwula.militiwradio.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "militiwradio");

    public static final RegistryObject<SoundEvent> ON =
            SOUNDS.register("on",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath("militiwradio", "radio.on")
                    ));

    public static final RegistryObject<SoundEvent> FREQ =
            SOUNDS.register("freq",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath("militiwradio", "radio.freq")
                    ));

    public static final RegistryObject<SoundEvent> START =
            SOUNDS.register("start",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath("militiwradio", "radio.start")
                    ));

    public static final RegistryObject<SoundEvent> END =
            SOUNDS.register("end",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath("militiwradio", "radio.end")
                    ));
}
