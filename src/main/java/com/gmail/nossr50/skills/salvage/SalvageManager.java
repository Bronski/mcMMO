package com.gmail.nossr50.skills.salvage;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.skills.SkillManager;
import com.gmail.nossr50.util.Misc;

public class SalvageManager extends SkillManager {
    public SalvageManager(McMMOPlayer mcMMOPlayer) {
        super(mcMMOPlayer, SkillType.SALVAGE);
    }

    public void handleSalvage(Location location, ItemStack item) {
        Player player = getPlayer();

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if (getSkillLevel() < Salvage.advancedSalvageUnlockLevel && item.getDurability() != 0) {
            player.sendMessage("You aren't skilled enough to salvage damaged items."); // TODO: Localize
            return;
        }

        double salvagePercentage = Math.min((((Salvage.salvageMaxBonus / Salvage.salvageMaxBonusLevel) * getSkillLevel()) / 100.0D), Salvage.salvageMaxBonus / 100.0D);
        int salvageAmount = Salvage.calculateSalvageAmount(item.getDurability(), item.getType().getMaxDurability(), salvagePercentage, Salvage.getSalvagedAmount(item));

        if (salvageAmount == 0) {
            player.sendMessage("This item is too damaged to be salvaged."); // TODO: Localize
            return;
        }

        player.setItemInHand(new ItemStack(Material.AIR));
        location.add(0, 1, 0);

        Map<Enchantment, Integer> enchants = item.getEnchantments();

        if (!enchants.isEmpty()) {
            ItemStack enchantBook = arcaneSalvageCheck(enchants);

            if (enchantBook != null) {
                Misc.dropItem(location, enchantBook);
            }
        }

        Misc.dropItems(location, new ItemStack(Salvage.getSalvagedItem(item)), salvageAmount);

        player.playSound(player.getLocation(), Sound.ANVIL_USE, Misc.ANVIL_USE_VOLUME, Misc.ANVIL_USE_PITCH);
        player.sendMessage(LocaleLoader.getString("Repair.Skills.SalvageSuccess"));
    }

    private ItemStack arcaneSalvageCheck(Map<Enchantment, Integer> enchants) {
        Player player = getPlayer();

        if (getSkillLevel() < Salvage.arcaneSalvageRank1) { // TODO: Permissions
            player.sendMessage("You were unable to extract the knowledge contained within this item."); // TODO: Localize
            return null;
        }

        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta enchantMeta = (EnchantmentStorageMeta) book.getItemMeta();

        boolean downgraded = false;

        for (Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            Enchantment enchantment = enchant.getKey();

            if (100 > Misc.getRandom().nextInt(activationChance)) {
                int enchantLevel = enchant.getValue();

                if (Salvage.arcaneSalvageDowngrades && enchantLevel > 1 && 50 > Misc.getRandom().nextInt(activationChance)) {
                    enchantMeta.addStoredEnchant(enchantment, enchantLevel - 1, true);
                    downgraded = true;
                }
                else {
                    enchantMeta.addStoredEnchant(enchantment, enchantLevel, true);
                }
            }
            else {
                downgraded = true;
            }
        }

        Map<Enchantment, Integer> newEnchants = enchantMeta.getStoredEnchants();

        if (newEnchants.isEmpty()) {
            player.sendMessage("You were unable to extract the knowledge contained within this item."); // TODO: Localize
            return null;
        }

        if (downgraded || newEnchants.size() < enchants.size()) {
            player.sendMessage("You were only able to extract some of the knowledge contained within this item."); // TODO: Localize
        }
        else {
            player.sendMessage("You able to extract all of the knowledge contained within this item!"); // TODO: Localize
        }

        book.setItemMeta(enchantMeta);
        return book;
    }
}