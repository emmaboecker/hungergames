package net.stckoverflw.hg.game

import com.destroystokyo.paper.MaterialSetTag
import com.destroystokyo.paper.MaterialTags
import net.axay.kspigot.event.listen
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Container
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom

class ChestFilling(val game: HungerGamesGame) {

    data class Item(val itemStack: ItemStack, val chance: Int)

    private val items = listOf(
        Item(ItemStack(Material.TRIDENT), 2),
        Item(itemStack(Material.TRIDENT) {
            addEnchantment(Enchantment.LOYALTY, 1)
        }, 1),
        Item(ItemStack(Material.IRON_SWORD), 4),
        Item(ItemStack(Material.IRON_SWORD), 3),
        Item(itemStack(Material.IRON_SWORD) {
            addEnchantment(Enchantment.VANISHING_CURSE, 1)
        }, 6),
        Item(itemStack(Material.IRON_SWORD) {
            addEnchantment(Enchantment.DAMAGE_ALL, 1)
        }, 5),
        Item(itemStack(Material.IRON_SWORD) {
            addEnchantment(Enchantment.DAMAGE_ALL, 1)
            addEnchantment(Enchantment.FIRE_ASPECT, 1)
        }, 4),
        Item(ItemStack(Material.STONE_AXE), 6),
        Item(ItemStack(Material.STONE_SWORD), 7),
        Item(ItemStack(Material.STONE_SWORD), 8),
        Item(ItemStack(Material.STONE_SHOVEL), 5),
        Item(ItemStack(Material.WOODEN_SWORD), 10),
        Item(itemStack(Material.WOODEN_SWORD) {
            addEnchantment(Enchantment.DAMAGE_ALL, 3)
        }, 2),
        Item(ItemStack(Material.WOODEN_AXE), 7),
        Item(ItemStack(Material.DIAMOND_SWORD), 5),
        Item(ItemStack(Material.DIAMOND_SWORD), 4),
        Item(itemStack(Material.DIAMOND_SWORD) {
            addEnchantment(Enchantment.DAMAGE_ALL, 1)
        }, 2),
        Item(ItemStack(Material.DIAMOND_AXE), 2),
        Item(ItemStack(Material.DIAMOND_SHOVEL), 5),
        Item(ItemStack(Material.CROSSBOW), 5),
        Item(itemStack(Material.CROSSBOW) {
            addEnchantment(Enchantment.PIERCING, 1)
        }, 3),
        Item(ItemStack(Material.BOW), 6),
        Item(itemStack(Material.BOW) {
            addEnchantment(Enchantment.ARROW_FIRE, 1)
        }, 2),
        Item(ItemStack(Material.APPLE, 2), 15),
        Item(ItemStack(Material.APPLE, 3), 10),
        Item(ItemStack(Material.COOKED_BEEF, 2), 10),
        Item(ItemStack(Material.COOKED_BEEF, 1), 12),
        Item(ItemStack(Material.COOKED_BEEF, 1), 8),
        Item(ItemStack(Material.BREAD, 1), 9),
        Item(ItemStack(Material.BREAD, 1), 9),
        Item(ItemStack(Material.BREAD, 2), 7),
        Item(ItemStack(Material.BREAD, 3), 6),
        Item(ItemStack(Material.COOKED_CHICKEN, 1), 12),
        Item(ItemStack(Material.GOLDEN_APPLE, 1), 7),
        Item(ItemStack(Material.LEATHER_HELMET), 6),
        Item(ItemStack(Material.CHAINMAIL_HELMET), 6),
        Item(ItemStack(Material.IRON_HELMET), 5),
        Item(ItemStack(Material.IRON_HELMET), 6),
        Item(itemStack(Material.IRON_HELMET) { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1) }, 3),
        Item(ItemStack(Material.GOLDEN_HELMET), 4),
        Item(ItemStack(Material.DIAMOND_HELMET), 3),
        Item(itemStack(Material.CHAINMAIL_CHESTPLATE) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 9),
        Item(ItemStack(Material.GOLDEN_CHESTPLATE), 6),
        Item(itemStack(Material.GOLDEN_CHESTPLATE) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 5),
        Item(ItemStack(Material.IRON_CHESTPLATE), 6),
        Item(ItemStack(Material.IRON_CHESTPLATE), 4),
        Item(itemStack(Material.IRON_CHESTPLATE) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        }, 2),
        Item(ItemStack(Material.LEATHER_LEGGINGS), 5),
        Item(ItemStack(Material.LEATHER_LEGGINGS), 6),
        Item(ItemStack(Material.CHAINMAIL_LEGGINGS), 5),
        Item(ItemStack(Material.IRON_LEGGINGS), 7),
        Item(ItemStack(Material.IRON_LEGGINGS), 8),
        Item(itemStack(Material.GOLDEN_LEGGINGS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 4),
        Item(ItemStack(Material.DIAMOND_LEGGINGS), 4),
        Item(itemStack(Material.DIAMOND_LEGGINGS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 4),
        Item(ItemStack(Material.LEATHER_BOOTS), 5),
        Item(ItemStack(Material.CHAINMAIL_BOOTS), 4),
        Item(itemStack(Material.CHAINMAIL_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 4),
        Item(ItemStack(Material.IRON_BOOTS), 5),
        Item(itemStack(Material.IRON_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_FALL, 3)
        }, 4),
        Item(ItemStack(Material.GOLDEN_BOOTS), 4),
        Item(itemStack(Material.GOLDEN_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        }, 5),
        Item(ItemStack(Material.DIAMOND_BOOTS), 4),
        Item(ItemStack(Material.DIAMOND_BOOTS), 3),
        Item(itemStack(Material.DIAMOND_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_FALL, 3)
        }, 2),
        Item(itemStack(Material.DIAMOND_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        }, 3),
        Item(ItemStack(Material.LAPIS_LAZULI, 1), 30),
        Item(ItemStack(Material.SHIELD, 1), 10),
        Item(ItemStack(Material.ARROW, 1), 13),
        Item(ItemStack(Material.ARROW, 2), 12),
        Item(ItemStack(Material.ARROW, 3), 10),
        Item(ItemStack(Material.ARROW, 4), 8),
        Item(ItemStack(Material.ARROW, 5), 5),
        Item(ItemStack(Material.TOTEM_OF_UNDYING), 1),
        Item(ItemStack(Material.ENCHANTED_GOLDEN_APPLE), 1),
        Item(ItemStack(Material.ENDER_PEARL, 2), 2),
        Item(ItemStack(Material.ENDER_PEARL), 3),
        Item(ItemStack(Material.ENDER_PEARL), 4),
        Item(ItemStack(Material.ENDER_PEARL), 5),
        Item(ItemStack(Material.EXPERIENCE_BOTTLE, 1), 5),
        Item(ItemStack(Material.EXPERIENCE_BOTTLE, 1), 5),
        Item(ItemStack(Material.EXPERIENCE_BOTTLE, 2), 5),
        Item(ItemStack(Material.EXPERIENCE_BOTTLE, 3), 5),
        Item(ItemStack(Material.DRAGON_HEAD, 1), 2),
        Item(ItemStack(Material.RAVAGER_SPAWN_EGG, 1), 1),
        Item(itemStack(Material.SPLASH_POTION) {
            meta {
                (this as PotionMeta).basePotionData = PotionData(PotionType.INSTANT_HEAL, false, false)
            }
        }, 4),
        Item(itemStack(Material.POTION) {
            meta {
                (this as PotionMeta).basePotionData = PotionData(PotionType.FIRE_RESISTANCE, true, false)
            }
        }, 2),
        Item(itemStack(Material.SPLASH_POTION) {
            meta {
                (this as PotionMeta).basePotionData = PotionData(PotionType.INSTANT_DAMAGE, false, false)
            }
        }, 3),
        Item(itemStack(Material.SPLASH_POTION) {
            meta {
                (this as PotionMeta).basePotionData = PotionData(PotionType.INVISIBILITY, false, false)
            }
        }, 2),
        Item(itemStack(Material.SPLASH_POTION) {
            meta {
                (this as PotionMeta).basePotionData = PotionData(PotionType.SPEED, false, false)
            }
        }, 3),
        Item(itemStack(Material.SPLASH_POTION) {
            meta {
                (this as PotionMeta).basePotionData = PotionData(PotionType.WEAKNESS, false, false)
            }
        }, 2),
        Item(ItemStack(Material.OAK_PLANKS, 2), 15),
        Item(ItemStack(Material.OAK_PLANKS, 3), 14),
        Item(ItemStack(Material.OAK_PLANKS, 4), 13),
        Item(ItemStack(Material.OAK_PLANKS, 5), 12),
        Item(ItemStack(Material.OAK_PLANKS, 6), 11),
        Item(ItemStack(Material.LAVA_BUCKET, 1), 5),
        Item(ItemStack(Material.WATER_BUCKET, 1), 5),
    )

    var tries = 4

    var armourLimit = 2
    var enchantableLimit = 4

    private var refill = System.currentTimeMillis()

    private var lastRefills = mutableMapOf<Location, Long>()

    init {
        listen<PlayerInteractEvent>(priority = EventPriority.LOW) {
            val clickedBlock = it.clickedBlock ?: return@listen
            if (clickedBlock.state is Container && (lastRefills[clickedBlock.location] ?: 0) < refill) {
                lastRefills[clickedBlock.location] = System.currentTimeMillis()
                val container = clickedBlock.state as Container
                var newContent = container.inventory.contents.map { _ ->
                    game.chestFilling.getItem(tries)
                }

                val previous = newContent

                newContent = ensureNoMoreThan(newContent, MaterialTags.BOOTS, armourLimit)
                newContent = ensureNoMoreThan(newContent, MaterialTags.LEGGINGS, armourLimit)
                newContent = ensureNoMoreThan(newContent, MaterialTags.CHESTPLATES, armourLimit)
                newContent = ensureNoMoreThan(newContent, MaterialTags.HELMETS, armourLimit)
                newContent = ensureNoMoreThan(newContent, MaterialTags.SWORDS, armourLimit)
                newContent = ensureNoMoreThan(newContent, MaterialTags.ENCHANTABLE, enchantableLimit)

                if (newContent != previous) {
                    println(previous.joinToString { item -> item.type.name })
                    println(newContent.joinToString { item -> item.type.name })
                }

                container.inventory.contents = newContent.toTypedArray()
            }
        }
    }

    private fun ensureNoMoreThan(items: List<ItemStack>, tag: MaterialSetTag, max: Int): List<ItemStack> {
        var mutableItems = items
        while (mutableItems.any { item ->
                tag.isTagged(
                    item
                )
            } && items.count { item ->
                tag.isTagged(
                    item
                )
            } > max) {
            mutableItems = items.map { item ->
                if (tag.isTagged(item)) {
                    game.chestFilling.getItem(2)
                } else {
                    item
                }
            }
        }

        return mutableItems
    }

    fun getItem(tries: Int, random: Random = ThreadLocalRandom.current().asKotlinRandom()): ItemStack {
        return buildList {
            for (i in 0 until tries) {
                val item = items.random(random)
                if (item.chance >= (1..100).random(random)) {
                    add(item)
                }
            }
        }.minByOrNull { it.chance }?.itemStack ?: ItemStack(Material.AIR)
    }

    fun refillChests() {
        refill = System.currentTimeMillis()
    }

}