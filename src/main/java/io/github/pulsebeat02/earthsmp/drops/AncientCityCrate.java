package io.github.pulsebeat02.earthsmp.drops;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.EarthSMPMod;
import net.minecraft.loot.LootTables;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public final class AncientCityCrate extends LootCrate {

  public AncientCityCrate(@NotNull final EarthSMPMod mod) {
    super(mod, Continent.EU, LootTables.ANCIENT_CITY_CHEST, 4);
  }

  @Override
  public boolean condition() {
    final Calendar calendar = Calendar.getInstance();
    return this.checkDay(calendar) && this.checkTime(calendar);
  }

  public boolean checkDay(@NotNull final Calendar calendar) {
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
  }

  public boolean checkTime(@NotNull final Calendar calendar) {
    return calendar.get(Calendar.HOUR_OF_DAY) == 16 && calendar.get(Calendar.MINUTE) == 0;
  }
}
