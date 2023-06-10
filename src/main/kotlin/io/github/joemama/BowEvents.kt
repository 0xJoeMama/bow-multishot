package io.github.joemama

import io.github.joemama.BowEvents.BowEnchantHandler
import io.github.joemama.BowEvents.BowFiredHandler
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

object BowEvents {
    @JvmField
    val ENCHANT: Event<BowEnchantHandler> =
        EventFactory.createArrayBacked(
            BowEnchantHandler::class.java
        ) {
            BowEnchantHandler { random, stack, player ->
                for (handler in it) {
                    handler.onEnchant(random, stack, player)
                }
            }
        }

    @JvmField
    val FIRED: Event<BowFiredHandler> =
        EventFactory.createArrayBacked(
            BowFiredHandler::class.java
        ) {
            BowFiredHandler { arrow, stack, world, user ->
                for (handler in it) {
                    handler.onFired(arrow, stack, world, user)
                }
            }
        }

    fun interface BowEnchantHandler {
        fun onEnchant(random: Random, stack: ItemStack, player: PlayerEntity)
    }

    fun interface BowFiredHandler {
        fun onFired(arrow: ArrowEntity, stack: ItemStack, world: World, user: LivingEntity)
    }
}