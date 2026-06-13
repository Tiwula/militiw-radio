package ua.tiwula.militiwradio.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ua.tiwula.militiwradio.MilitiwRadio;

public class ModItems
{
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MilitiwRadio.MODID);

    public static final RegistryObject<Item> RADIO =
            ITEMS.register("radio",
                    () -> new RadioItem(
                            new Item.Properties().stacksTo(1),
                            1024,
                            (short) 30,
                            (short)512,
                            (byte)8,
                            true,
                            10000,
                            20
                    ));
/*
    public static final RegistryObject<Item> SIMPLE_RADIO =
            ITEMS.register("simple_radio",
                    () -> new RadioItem(
                            new Item.Properties().stacksTo(1),
                            (short) 10,
                            (short)128,
                            (byte)4,
                            false
                    ));*/

}
