package ua.tiwula.militiwradio.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import ua.tiwula.militiwradio.MilitiwRadio;
import ua.tiwula.militiwradio.client.keybind.ModKeybinds;
import ua.tiwula.militiwradio.item.ModItems;

@Mod.EventBusSubscriber(
        modid = MilitiwRadio.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ClientSetup
{
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {

        event.enqueueWork(() -> {
            //MenuScreens.register(ModMenus.RADIO.get(), RadioScreen::new);
            ItemProperties.register(
                    ModItems.RADIO.get(),
                    ResourceLocation.fromNamespaceAndPath("militiwradio", "on"),
                    (stack, level, entity, seed) -> {

                        return stack.getOrCreateTag().getBoolean("on")
                                ? 1.0F
                                : 0.0F;
                    }
            );

        });

        CuriosRendererRegistry.register(ModItems.RADIO.get(), RadioCurioRenderer::new);
    }

    @SubscribeEvent
    public static void registerKeyMapping(RegisterKeyMappingsEvent event)
    {
        event.register(ModKeybinds.RADIO_PTT);
        event.register(ModKeybinds.RADIO_OPEN);
        event.register(ModKeybinds.RADIO_NEXT_CH);
        event.register(ModKeybinds.RADIO_PREV_CH);
    }

}
