package io.github.pulsebeat02.earthsmp.drops;

import io.github.pulsebeat02.earthsmp.Continent;
import io.github.pulsebeat02.earthsmp.EarthSMPMod;
import java.util.Calendar;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public final class VillagerCrate extends LootCrate {

  private static final Identifier CUSTOM_LOOT_TABLE;

  static {
    CUSTOM_LOOT_TABLE = new Identifier("earthsmp", "villager_egg_table");
  }

  public VillagerCrate(@NotNull final EarthSMPMod mod) {
    super(mod, Continent.AS, CUSTOM_LOOT_TABLE, 2);
  }

  @Override
  public boolean condition() {
    final Calendar calendar = Calendar.getInstance();
    return this.checkDay(calendar) && this.checkTime(calendar);
  }

  public boolean checkDay(@NotNull final Calendar calendar) {
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
  }

  public boolean checkTime(@NotNull final Calendar calendar) {
    return calendar.get(Calendar.HOUR_OF_DAY) == 21 && calendar.get(Calendar.MINUTE) == 0;
  }
}
