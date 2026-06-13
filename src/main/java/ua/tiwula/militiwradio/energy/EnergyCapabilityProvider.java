package ua.tiwula.militiwradio.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EnergyCapabilityProvider
        implements ICapabilityProvider
{

    private final LazyOptional<IEnergyStorage> energy;

    public EnergyCapabilityProvider(ItemStack stack) {

        energy = LazyOptional.of(() ->
                new RadioEnergyStorage(stack));
    }

    @Override
    public <T> LazyOptional<T> getCapability(
            @NotNull Capability<T> cap,
            @Nullable Direction side
    ) {
        return cap == ForgeCapabilities.ENERGY
                ? energy.cast()
                : LazyOptional.empty();
    }
}
