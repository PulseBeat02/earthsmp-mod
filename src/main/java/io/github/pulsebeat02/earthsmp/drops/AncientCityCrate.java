package io.github.pulsebeat02.earthsmp.drops;

import static java.util.Calendar.SATURDAY;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.loot.LootTables;

public final class AncientCityCrate extends LootCrate {

  public AncientCityCrate() {
    super(Continent.EU, LootTables.ANCIENT_CITY_CHEST, 3);
  }

  @Override
  public boolean condition() {
    return Utils.checkTime(SATURDAY, 16, 0);
  }
}
