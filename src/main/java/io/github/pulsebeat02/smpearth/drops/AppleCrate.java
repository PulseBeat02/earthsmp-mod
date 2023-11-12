package io.github.pulsebeat02.smpearth.drops;

import static java.util.Calendar.SATURDAY;

import io.github.pulsebeat02.smpearth.Continent;
import io.github.pulsebeat02.smpearth.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class AppleCrate extends LootCrate {

  public AppleCrate() {
    super(Continent.EU, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE), 1);
  }

  @Override
  public boolean condition() {
    return Utils.checkTime(SATURDAY, 16, 0);
  }
}
