package io.github.pulsebeat02.smpearth.mixin;

import java.util.List;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BannerItem.class)
public final class BannerItemMixin {

  /**
   * @author PulseBeat_02
   * @reason removes banner limit
   */
  @Overwrite
  public static void appendBannerTooltip(final ItemStack stack, final List<Text> tooltip) {
    final NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(stack);
    if (nbtCompound != null && nbtCompound.contains("PatternList")) {
      final NbtList nbtList = nbtCompound.getList("PatternList", 10);
      for (int i = 0; i < nbtList.size(); ++i) {
        final NbtCompound nbtCompound2 = nbtList.getCompound(i);
        final DyeColor dyeColor = DyeColor.byId(nbtCompound2.getInt("Color"));
        final RegistryEntry<BannerPattern> registryEntry =
            BannerPattern.byId(nbtCompound2.getString("Pattern"));
        if (registryEntry != null) {
          registryEntry
              .getKey()
              .map((key) -> key.getValue().toShortTranslationKey())
              .ifPresent(
                  (translationKey) -> {
                    tooltip.add(
                        Text.translatable(
                                "block.minecraft.banner."
                                    + translationKey
                                    + "."
                                    + dyeColor.getName())
                            .formatted(Formatting.GRAY));
                  });
        }
      }
    }
  }
}
