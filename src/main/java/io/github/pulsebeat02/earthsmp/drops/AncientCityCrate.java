package io.github.pulsebeat02.earthsmp.drops;

import static java.util.Calendar.SATURDAY;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;

public final class AncientCityCrate extends LootCrate {

  public AncientCityCrate() {
    super(Continent.EU, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), 1);
  }

  @Override
  public boolean condition() {
    return Utils.checkTime(SATURDAY, 16, 0);
  }
}
