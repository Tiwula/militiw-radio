package ua.tiwula.militiwradio;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ua.tiwula.militiwradio.item.ModItems;
import ua.tiwula.militiwradio.network.ModNetwork;
import ua.tiwula.militiwradio.sound.ModSounds;

@Mod(MilitiwRadio.MODID)
public class MilitiwRadio
{
    public static final String MODID = "militiwradio";
    public static final boolean DEBUG = false;

    public MilitiwRadio(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModNetwork.register();
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        //ModMenus.MENUS.register(modEventBus);
    }
}
