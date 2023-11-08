package io.github.pulsebeat02.earthsmp.drops;

import static java.util.Calendar.FRIDAY;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;

public final class TotemCrate extends LootCrate {

  public TotemCrate() {
    super(Continent.AU, new ItemStack(Items.TOTEM_OF_UNDYING), 1);
  }

  @Override
  public boolean condition() {
    return Utils.checkTime(FRIDAY, 20, 0);
  }
}
