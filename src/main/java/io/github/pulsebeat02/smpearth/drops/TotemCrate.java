package io.github.pulsebeat02.smpearth.drops;

import static java.util.Calendar.FRIDAY;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class TotemCrate extends LootCrate {

  public TotemCrate() {
    super(Continent.AU, new ItemStack(Items.TOTEM_OF_UNDYING), 1);
  }

  @Override
  public boolean condition() {
    return Utils.checkTime(FRIDAY, 20, 0);
  }
}
