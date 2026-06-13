package ua.tiwula.militiwradio;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import ua.tiwula.militiwradio.item.ModItems;

public class ModCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, MilitiwRadio.MODID);

    public static final RegistryObject<CreativeModeTab> MILITIW_RADIO_TAB = CREATIVE_TABS
            .register("militiw_radio_tab", () -> CreativeModeTab.builder()
                    .title(Component.literal("Militiw Radio"))
                    .icon(() -> new ItemStack(ModItems.RADIO.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.RADIO.get());
                    })
                    .build());
}
