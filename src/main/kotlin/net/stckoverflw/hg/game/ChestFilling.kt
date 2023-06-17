package net.stckoverflw.hg.game

import net.axay.kspigot.items.itemStack
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom

class ChestFilling(val game: HungerGamesGame) {

    data class Item(val itemStack: ItemStack, val chance: Int)

    private val items = listOf(
        Item(ItemStack(Material.IRON_SWORD), 4),
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
        Item(ItemStack(Material.STONE_AXE), 7),
        Item(ItemStack(Material.STONE_SHOVEL), 8),
        Item(ItemStack(Material.WOODEN_SWORD), 12),
        Item(itemStack(Material.WOODEN_SWORD) {
            addEnchantment(Enchantment.DAMAGE_ALL, 3)
        }, 3),
        Item(ItemStack(Material.WOODEN_AXE), 15),
        Item(ItemStack(Material.DIAMOND_SWORD), 4),
        Item(itemStack(Material.DIAMOND_SWORD) {
            addEnchantment(Enchantment.DAMAGE_ALL, 1)
        }, 2),
        Item(ItemStack(Material.DIAMOND_AXE), 2),
        Item(ItemStack(Material.DIAMOND_SHOVEL), 5),
        Item(ItemStack(Material.CROSSBOW), 7),
        Item(ItemStack(Material.BOW), 10),
        Item(ItemStack(Material.APPLE, 1), 20),
        Item(ItemStack(Material.APPLE, 2), 15),
        Item(ItemStack(Material.APPLE, 3), 10),
        Item(ItemStack(Material.APPLE, 3), 4),
        Item(ItemStack(Material.COOKED_BEEF, 2), 10),
        Item(ItemStack(Material.COOKED_BEEF, 1), 12),
        Item(ItemStack(Material.COOKED_BEEF, 5), 3),
        Item(ItemStack(Material.BREAD, 1), 12),
        Item(ItemStack(Material.BREAD, 2), 11),
        Item(ItemStack(Material.BREAD, 3), 9),
        Item(ItemStack(Material.BREAD, 5), 4),
        Item(ItemStack(Material.BREAD, 5), 1),
        Item(ItemStack(Material.COOKED_CHICKEN, 1), 12),
        Item(ItemStack(Material.GOLDEN_APPLE, 1), 5),
        Item(ItemStack(Material.LEATHER_HELMET), 15),
        Item(ItemStack(Material.CHAINMAIL_HELMET), 15),
        Item(ItemStack(Material.IRON_HELMET), 8),
        Item(itemStack(Material.IRON_HELMET) { addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1) }, 6),
        Item(ItemStack(Material.GOLDEN_HELMET), 7),
        Item(ItemStack(Material.DIAMOND_HELMET), 3),
        Item(ItemStack(Material.LEATHER_CHESTPLATE), 12),
        Item(itemStack(Material.CHAINMAIL_CHESTPLATE) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 9),
        Item(ItemStack(Material.CHAINMAIL_CHESTPLATE), 12),
        Item(ItemStack(Material.GOLDEN_CHESTPLATE), 6),
        Item(itemStack(Material.GOLDEN_CHESTPLATE) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 5),
        Item(ItemStack(Material.IRON_CHESTPLATE), 8),
        Item(ItemStack(Material.LEATHER_LEGGINGS), 15),
        Item(ItemStack(Material.CHAINMAIL_LEGGINGS), 15),
        Item(ItemStack(Material.IRON_LEGGINGS), 10),
        Item(ItemStack(Material.GOLDEN_LEGGINGS), 10),
        Item(itemStack(Material.GOLDEN_LEGGINGS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 10),
        Item(ItemStack(Material.DIAMOND_LEGGINGS), 8),
        Item(itemStack(Material.DIAMOND_LEGGINGS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 5),
        Item(ItemStack(Material.LEATHER_BOOTS), 12),
        Item(ItemStack(Material.CHAINMAIL_BOOTS), 10),
        Item(itemStack(Material.CHAINMAIL_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_FALL, 2)
        }, 9),
        Item(itemStack(Material.CHAINMAIL_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        }, 4),
        Item(ItemStack(Material.IRON_BOOTS), 9),
        Item(itemStack(Material.IRON_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_FALL, 3)
        }, 7),
        Item(ItemStack(Material.GOLDEN_BOOTS), 6),
        Item(itemStack(Material.GOLDEN_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        }, 5),
        Item(ItemStack(Material.DIAMOND_BOOTS), 4),
        Item(itemStack(Material.DIAMOND_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_FALL, 3)
        }, 3),
        Item(itemStack(Material.DIAMOND_BOOTS) {
            addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        }, 5),
        Item(ItemStack(Material.LAPIS_LAZULI, 1), 80),
        Item(ItemStack(Material.SHIELD, 1), 30),
        Item(ItemStack(Material.ARROW, 1), 15),
        Item(ItemStack(Material.ARROW, 2), 12),
        Item(ItemStack(Material.ARROW, 3), 10),
        Item(ItemStack(Material.ARROW, 4), 8),
        Item(ItemStack(Material.ARROW, 5), 5),
        Item(ItemStack(Material.TOTEM_OF_UNDYING), 1),
        Item(ItemStack(Material.ENCHANTED_GOLDEN_APPLE), 1),
    )

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

}