package io.github.pulsebeat02.earthsmp.drops;

import static java.util.Calendar.FRIDAY;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.loot.LootTables;

public final class BastionCrate extends LootCrate {

  public BastionCrate() {
    super(Continent.AU, LootTables.BASTION_TREASURE_CHEST, 2);
  }

  @Override
  public boolean condition() {
    return Utils.checkTime(FRIDAY, 20, 0);
  }
}
