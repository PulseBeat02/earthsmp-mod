package io.github.pulsebeat02.smpearth.mixin;

import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(AnvilScreenHandler.class)
public final class AnvilScreenHandlerMixin {

    @ModifyConstant(method = "updateResult()V", constant = @Constant(intValue = 40, ordinal = 2))
    private int limit(final int value) {
        return Integer.MAX_VALUE;
    }
}
