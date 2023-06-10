package io.github.joemama.mixin;

import io.github.joemama.BowEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowItem.class)
public abstract class MixinBowItem extends Item {
    public MixinBowItem(Settings settings) {
        super(settings);
    }

    @Redirect(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean bowMultishot$spawnMultipleArrows(World instance, Entity entity, ItemStack stack, World world, LivingEntity user, int remainingTicks) {
        ArrowEntity arrow = (ArrowEntity) entity;
        BowEvents.FIRED.invoker().onFired(arrow, stack, world, user);

        return instance.spawnEntity(arrow);
    }
}
