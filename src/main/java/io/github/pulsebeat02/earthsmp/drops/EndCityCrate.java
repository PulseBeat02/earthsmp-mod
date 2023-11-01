package io.github.pulsebeat02.earthsmp.drops;

import static java.util.Calendar.SUNDAY;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.loot.LootTables;

public final class EndCityCrate extends LootCrate {

  public EndCityCrate() {
    super(Continent.NA, LootTables.END_CITY_TREASURE_CHEST, 3);
  }

  @Override
  public boolean condition() {
    return Utils.checkTime(SUNDAY, 10, 0);
  }
}
