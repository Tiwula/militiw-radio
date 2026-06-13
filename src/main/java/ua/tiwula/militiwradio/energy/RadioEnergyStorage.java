package ua.tiwula.militiwradio.energy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import ua.tiwula.militiwradio.item.RadioItem;

public class RadioEnergyStorage implements IEnergyStorage
{

    private final ItemStack stack;

    public RadioEnergyStorage(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (Minecraft.getInstance().level != null) return 0;

        int rec = Math.min(Math.min(maxReceive, ((RadioItem)stack.getItem()).MAX_REC_ENERGY), getMaxEnergyStored()-getEnergyStored());

        if (!simulate && !Minecraft.getInstance().level.isClientSide)
            stack.getOrCreateTag().putInt("energy", getEnergyStored() + rec);
        return rec;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        return 0;
        /*
        int ext = Math.min(maxExtract, getEnergyStored());

        if (!simulate && !Minecraft.getInstance().level.isClientSide)
            stack.getOrCreateTag().putInt("energy", getEnergyStored()-ext);
        return ext;*/
    }

    @Override
    public int getEnergyStored()
    {
        return stack.getOrCreateTag().getInt("energy");
    }

    @Override
    public int getMaxEnergyStored()
    {
        return ((RadioItem)stack.getItem()).MAX_ENERGY;
    }

    @Override
    public boolean canExtract()
    {
        return false;
    }

    @Override
    public boolean canReceive()
    {
        return true;
    }
}
