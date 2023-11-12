package io.github.pulsebeat02.smpearth.mixin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public final class DragonEggContainerMixin {

  @Shadow public ServerPlayerEntity player;

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
    final ItemStack packetStack = packet.getStack();
    final ItemStack cursorStack = handler.getCursorStack();
    final ItemStack slotStack = handler.getSlot(slot).getStack();
    final ItemStack invStack = this.player.getInventory().getStack(packet.getButton());
    itemStacks.add(packetStack);
    itemStacks.add(cursorStack);
    itemStacks.add(slotStack);
    itemStacks.add(invStack);
    for (final ItemStack itemStack : itemStacks) {
      if (this.check(itemStack)) {
        ci.cancel();
        this.player.currentScreenHandler.syncState();
        return;
      }
    }
  }

  @Unique
  private boolean check(@NotNull final ItemStack stack) {
    return stack.isOf(Items.DRAGON_EGG);
  }
}
