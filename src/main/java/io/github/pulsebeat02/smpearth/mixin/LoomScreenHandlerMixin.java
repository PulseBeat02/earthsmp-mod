package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LoomScreenHandler.class)
public final class LoomScreenHandlerMixin {
  @Shadow @Final Slot bannerSlot;
  @Shadow @Final Slot dyeSlot;
  @Shadow @Final private Slot outputSlot;

  /**
   * @author PulseBeat_02
   * @reason removes banner limit
   */
  @Overwrite
  private void updateOutputSlot(final RegistryEntry<BannerPattern> pattern) {
    final ItemStack itemStack = this.bannerSlot.getStack();
    final ItemStack itemStack2 = this.dyeSlot.getStack();
    ItemStack itemStack3 = ItemStack.EMPTY;
    if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
      itemStack3 = itemStack.copyWithCount(1);
      final DyeColor dyeColor = ((DyeItem) itemStack2.getItem()).getColor();
      NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack3);
      final NbtList nbtList;
      if (nbtCompound != null && nbtCompound.contains("PatternList", 9)) {
        nbtList = nbtCompound.getList("PatternList", 10);
      } else {
        nbtList = new NbtList();
        if (nbtCompound == null) {
          nbtCompound = new NbtCompound();
        }
        nbtCompound.put("PatternList", nbtList);
      }
      final NbtCompound nbtCompound2 = new NbtCompound();
      nbtCompound2.putString("Pattern", pattern.value().getId());
      nbtCompound2.putInt("Color", dyeColor.getId());
      nbtList.add(nbtCompound2);
      BlockItem.setBlockEntityNbt(itemStack3, BlockEntityType.BANNER, nbtCompound);
    }
    if (!ItemStack.areEqual(itemStack3, this.outputSlot.getStack())) {
      this.outputSlot.setStackNoCallbacks(itemStack3);
    }
  }
}
