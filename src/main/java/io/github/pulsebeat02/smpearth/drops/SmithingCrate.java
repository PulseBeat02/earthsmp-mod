package io.github.pulsebeat02.smpearth.drops;

import static java.util.Calendar.SUNDAY;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class SmithingCrate extends LootCrate {

  public SmithingCrate() {
    super(Continent.NA, new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), 2);
  }

  @Override
  public boolean condition() {
    return Utils.checkTime(SUNDAY, 10, 0);
  }
}
