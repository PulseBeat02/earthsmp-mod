package io.github.pulsebeat02.smpearth.mixin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public final class DragonEggContainerMixin {

  @Shadow public @NotNull ServerPlayerEntity player;

  @Inject(at = @At("HEAD"), method = "onClickSlot", cancellable = true)
  private void preventEggFromBeingPlaced(
      @NotNull final ClickSlotC2SPacket packet, @NotNull final CallbackInfo ci) {

    final Collection<ItemStack> raw = packet.getModifiedStacks().values();
    final List<ItemStack> itemStacks = new ArrayList<>(raw);
    final ScreenHandler handler = this.player.currentScreenHandler;
    final int slot = packet.getSlot();
    if (slot == -999) {
      return;
    }

    final Collection<ItemStack> others = this.getOtherItemStacks(packet, handler, slot);
    itemStacks.addAll(others);

    final boolean found = itemStacks.stream().anyMatch(this::check);
    if (found) {
      ci.cancel();
      this.player.currentScreenHandler.syncState();
    }
  }

  @Unique
  private @NotNull @Unmodifiable Collection<ItemStack> getOtherItemStacks(
      @NotNull final ClickSlotC2SPacket packet,
      @NotNull final ScreenHandler handler,
      final int slot) {
    final ItemStack packetStack = packet.getStack();
    final ItemStack cursorStack = handler.getCursorStack();
    final ItemStack slotStack = handler.getSlot(slot).getStack();
    final ItemStack invStack = this.player.getInventory().getStack(packet.getButton());
    return List.of(packetStack, cursorStack, slotStack, invStack);
  }

  @Unique
  private boolean check(@NotNull final ItemStack stack) {
    return stack.isOf(Items.DRAGON_EGG);
  }
}
