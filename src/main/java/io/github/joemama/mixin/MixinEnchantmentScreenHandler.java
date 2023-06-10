package io.github.joemama.mixin;

import io.github.joemama.BowEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentScreenHandler.class)
public abstract class MixinEnchantmentScreenHandler extends ScreenHandler {
    @Shadow
    @Final
    private Inventory inventory;

    @Shadow
    @Final
    private Random random;

    protected MixinEnchantmentScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "onButtonClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAbilities()Lnet/minecraft/entity/player/PlayerAbilities;"))
    private void bowMultisoht$addMultishot(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = this.inventory.getStack(0);
        if (stack.isOf(Items.BOW)) {
            BowEvents.ENCHANT.invoker().onEnchant(this.random, stack, player);
        }
    }
}
