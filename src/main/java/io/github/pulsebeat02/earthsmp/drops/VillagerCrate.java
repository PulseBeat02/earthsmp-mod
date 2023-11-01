package io.github.pulsebeat02.earthsmp.drops;

import io.github.pulsebeat02.earthsmp.Continent;
import java.util.Calendar;

import io.github.pulsebeat02.earthsmp.utils.Utils;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SUNDAY;

public final class VillagerCrate extends LootCrate {

  private static final Identifier CUSTOM_LOOT_TABLE;

  static {
    CUSTOM_LOOT_TABLE = new Identifier("earthsmp", "villager_egg_table");
  }

  public VillagerCrate() {
    super(Continent.AS, CUSTOM_LOOT_TABLE, 2);
  }

  @Override
  public boolean condition() {
    return Utils.checkTime(MONDAY, 21, 0);
  }
}
