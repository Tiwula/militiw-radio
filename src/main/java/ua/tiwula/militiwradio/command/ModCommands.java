package ua.tiwula.militiwradio.command;

import com.mojang.brigadier.Command;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ua.tiwula.militiwradio.MilitiwRadio;
import ua.tiwula.militiwradio.item.RadioItem;
import ua.tiwula.militiwradio.util.Util;

//@Mod.EventBusSubscriber(modid = MilitiwRadio.MODID)
public class ModCommands
{

    //@SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event)
    {
        event.getDispatcher().register(
                Commands.literal("militwradio")
                        .then(Commands.literal("fillenergy")
                        .requires(source -> source.hasPermission(2))
                        .executes(ctx -> {
                            ItemStack stack = (Util.findRadio(ctx.getSource().getPlayerOrException(), false));
                            if (stack.isEmpty()) ctx.getSource().sendFailure(Component.literal("No radio"));
                            stack.getOrCreateTag().putInt("energy", ((RadioItem)stack.getItem()).MAX_ENERGY);
                            ctx.getSource().sendSuccess(() -> Component.literal("Filled up!"), false);
                            return Command.SINGLE_SUCCESS;
                        }))
        );
    }
}
