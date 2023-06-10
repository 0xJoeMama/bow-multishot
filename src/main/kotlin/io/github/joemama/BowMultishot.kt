package io.github.joemama

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.Items
import net.minecraft.util.TypedActionResult

@Environment(EnvType.SERVER)
object BowMultishot : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        UseItemCallback.EVENT.register { player, world, hand ->
            val stack = player.getStackInHand(hand)
            if (world.isClient) {
                return@register TypedActionResult.pass(stack)
            }

            if (stack.isOf(Items.BEETROOT_SOUP)) {
                player.addStatusEffect(StatusEffectInstance(StatusEffects.REGENERATION, 10 * 20, 3, false, false))
                player.addStatusEffect(StatusEffectInstance(StatusEffects.SPEED, 10 * 20, 2, false, true))
                player.setStackInHand(hand, Items.BOWL.defaultStack)
                TypedActionResult.consume(stack)
            } else {
                TypedActionResult.pass(stack)
            }
        }

        BowEvents.ENCHANT.register { random, stack, _ ->
            if (random.nextInt(4) == 0) {
                stack.addEnchantment(Enchantments.MULTISHOT, 1)
            }
        }

        BowEvents.FIRED.register { arrow, stack, world, user ->
            if (EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack) > 0) {
                for (i in 0 until 2) {
                    val other = ArrowEntity(world, user)
                    other.setVelocity(
                        user,
                        user.pitch,
                        user.yaw - 10 + i * 20,
                        0f,
                        arrow.velocity.length().toFloat(),
                        0f
                    )
                    other.isCritical = arrow.isCritical
                    other.damage = arrow.damage - 1
                    other.punch = arrow.punch
                    other.setOnFireFor(arrow.fireTicks)
                    other.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED
                    world.spawnEntity(other)
                }
            }
        }
    }
}