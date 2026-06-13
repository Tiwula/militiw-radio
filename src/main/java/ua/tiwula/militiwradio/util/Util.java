package ua.tiwula.militiwradio.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import ua.tiwula.militiwradio.item.ModItems;
import ua.tiwula.militiwradio.item.RadioItem;

import java.util.concurrent.atomic.AtomicReference;

public class Util
{
    public static ItemStack getCuriosRadio(Player player)
    {
        AtomicReference<ItemStack> radioRef = new AtomicReference<>(ItemStack.EMPTY);

        CuriosApi.getCuriosInventory(player).ifPresent(inv -> {
            inv.findFirstCurio(ModItems.RADIO.get()).ifPresent(slot -> {
                radioRef.set(slot.stack());
            });
        });

        return radioRef.get();
    }

    public static ItemStack findRadio(Player player, boolean inCurios)
    {
        if (inCurios)
        {
            ItemStack curios = getCuriosRadio(player);

            if (!curios.isEmpty()) {
                return curios;
            }
        }
        else
        {
            ItemStack main = player.getMainHandItem();

            if (main.getItem() instanceof RadioItem)
            {
                return main;
            }

            ItemStack off = player.getOffhandItem();

            if (off.getItem() instanceof RadioItem)
            {
                return off;
            }
        }

        return ItemStack.EMPTY;
    }
}
