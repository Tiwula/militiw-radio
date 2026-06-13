package ua.tiwula.militiwradio;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ua.tiwula.militiwradio.item.ModItems;
import ua.tiwula.militiwradio.item.RadioItem;
import ua.tiwula.militiwradio.network.RadioManager;
import ua.tiwula.militiwradio.util.Util;

@Mod.EventBusSubscriber(
        modid = MilitiwRadio.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        ServerPlayer player = (ServerPlayer) event.getEntity();

        for (ItemStack stack : player.getInventory().items)
        {
            if (!stack.is(ModItems.RADIO.get()))
                continue;

            CompoundTag tag = stack.getTag();
            short freq = (tag != null ? RadioItem.getFrequency(stack) : 0);

            if (freq != 0)
            {
                RadioManager.addListener(freq, player.getUUID());
                if (MilitiwRadio.DEBUG)
                    player.sendSystemMessage(Component.literal("Start listening on "+ RadioItem.getFrequency(stack)));
            }
        }
        ItemStack stack = Util.getCuriosRadio(player);
        if(!stack.isEmpty())
        {
            CompoundTag tag = stack.getTag();
            short freq = (tag != null ? RadioItem.getFrequency(stack) : 0);

            if (freq != 0)
            {
                RadioManager.addListener(freq, player.getUUID());
                if (MilitiwRadio.DEBUG)
                    player.sendSystemMessage(Component.literal("Start listening on "+ RadioItem.getFrequency(stack)));
            }
        }

    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {

        ServerPlayer player = (ServerPlayer) event.getEntity();

        RadioManager.stopTalking(player.getUUID());
        RadioManager.removeListener(player.getUUID());
    }
}
