package ua.tiwula.militiwradio.item;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import ua.tiwula.militiwradio.client.screen.RadioScreen;
import ua.tiwula.militiwradio.network.ModNetwork;
import ua.tiwula.militiwradio.network.packet.ToggleRadioPacket;
import ua.tiwula.militiwradio.sound.ModSounds;

import java.util.List;

public class RadioItem extends Item implements ICurioItem
{
    public final int RANGE;
    public final short MIN_FREQ;
    public final short MAX_FREQ;
    public final byte MAX_CHANNELS;
    public final int MAX_ENERGY;
    public final int MAX_REC_ENERGY;
    public final boolean HAS_CRYPT;

    public RadioItem(Properties properties, int range, short minFreq, short maxFreq, byte maxCh, boolean hasCrypt,
                     int maxEnergy, int maxRec)
    {
        super(properties.stacksTo(1));
        RANGE = range;
        MIN_FREQ = minFreq;
        MAX_FREQ = maxFreq;
        MAX_CHANNELS = maxCh;
        HAS_CRYPT = hasCrypt;
        MAX_ENERGY = maxEnergy;
        MAX_REC_ENERGY = maxRec;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            if (player.isShiftKeyDown() && stack.hasTag())
            {
                CompoundTag tag = stack.getTag();
                //if (tag.getInt("energy") < 20) return InteractionResultHolder.success(stack);
                boolean power = !tag.getBoolean("on");
                if (power) player.playSound(ModSounds.ON.get());
                ModNetwork.CHANNEL.sendToServer(new ToggleRadioPacket(power, false));
                return InteractionResultHolder.success(stack);
            }

            /*player.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new RadioMenu(id, inv, null),
                    Component.literal("Radio")
            ));*/
            Minecraft.getInstance().setScreen(new RadioScreen(player.getItemInHand(hand), this, false));
            return InteractionResultHolder.success(stack);
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        CompoundTag tag = stack.getTag();
        if (tag == null) return;
        tooltip.add(Component.literal("Power: "+(tag.getBoolean("on") ? "ON" : "OFF")));
        tooltip.add(Component.literal("CH: " + tag.getByte("selected_ch")));
        tooltip.add(Component.literal("Freq: " + RadioItem.getFrequency(stack)));
        //tooltip.add(Component.literal("Energy: " + tag.getInt("energy")/MAX_ENERGY*100 + "%").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected)
    {
        if (level.isClientSide)
            return;

        if (!stack.hasTag())
        {
            CompoundTag nbtCompound = new CompoundTag();
            nbtCompound.putBoolean("on", false);
            nbtCompound.putByte("selected_ch", (byte)1);
            for (int i = 1; i <= MAX_CHANNELS; i++)
                nbtCompound.putShort("ch"+i, (short)0);
            //nbtCompound.putInt("energy", 0);
            stack.setTag(nbtCompound);
        }
    }

    /*@Override
    public ICapabilityProvider initCapabilities(
            ItemStack stack,
            @Nullable CompoundTag nbt
    ) {
        return new EnergyCapabilityProvider(stack);
    }
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }


    @Override
    public int getBarWidth(ItemStack stack) {

        int energy = stack.getOrCreateTag().getInt("energy");

        return Math.round(
                13f * energy / MAX_ENERGY
        );

    }*/

    /*public static void setPower(ItemStack stack, boolean state)
    {
        if (stack.hasTag())
            stack.getTag().putBoolean("on", state);
    }*/
    /*public static boolean togglePower(ItemStack stack)
    {
        if (!stack.hasTag())
            return false;
        CompoundTag tag = stack.getTag();
        boolean power = !tag.getBoolean("on");
        //ModNetwork.CHANNEL.sendToServer(new ToggleRadioPacket(power, hand));
        return power;
    }*/

    public static short getFrequency(ItemStack stack)
    {
        if (stack.hasTag())
            return stack.getTag().getShort("ch" + stack.getTag().getByte("selected_ch"));
        return 0;
    }

    /*public boolean consumeEnergy(ItemStack stack, int cost, ServerPlayer player)
    {
        CompoundTag tag = stack.getOrCreateTag();
        int energy = tag.getInt("energy");
        if (energy < cost)
        {
            tag.putInt("energy", 0);
            tag.putBoolean("on", false);
            RadioManager.removeListener(RadioItem.getFrequency(stack), player.getUUID());
            RadioManager.stopTalking(player.getUUID());
        }

        tag.putInt("energy", energy - cost);
        return true;
    }*/
}
