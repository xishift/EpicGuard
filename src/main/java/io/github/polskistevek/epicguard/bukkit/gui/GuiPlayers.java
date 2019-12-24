package io.github.polskistevek.epicguard.bukkit.gui;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import io.github.polskistevek.epicguard.bukkit.manager.UserManager;
import io.github.polskistevek.epicguard.bukkit.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import io.github.polskistevek.epicguard.bukkit.GuardBukkit;
import io.github.polskistevek.epicguard.utils.ChatUtil;
import io.github.polskistevek.epicguard.utils.GeoAPI;
import io.github.polskistevek.epicguard.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuiPlayers {
    public static Inventory inv;

    public static void show(Player p) {
        int i = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack itemStack;
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(ChatUtil.fix("&7UUID: &3" + player.getUniqueId()));
            lore.add(ChatUtil.fix("&7First Join: &6" + new Date(player.getFirstPlayed())));
            try {
                lore.add(ChatUtil.fix("&7Country: &6" + GeoAPI.getDatabase().country(player.getAddress().getAddress()).getCountry().getIsoCode()));
            } catch (IOException | GeoIp2Exception e) {
                Logger.throwException(e);
            }
            lore.add(ChatUtil.fix("&7OP: " + (player.isOp() ? "&aYes" : "&cNo")));
            if (GuardBukkit.IP_HISTORY_ENABLE) {
                lore.add("");
                lore.add(ChatUtil.fix("&7IP History:"));
                for (String adress : UserManager.getUser(player).getAdresses()) {
                    if (player.getAddress().getAddress().getHostAddress().equals(adress)) {
                        lore.add(ChatUtil.fix("&8-> &c" + adress + " &8(&aCurrent&8)"));
                    } else {
                        lore.add(ChatUtil.fix("&8-> &c" + adress));
                    }
                }
            }
            if (player.isOp()) {
                itemStack = new ItemBuilder(Material.DIAMOND_HELMET).setTitle("&c" + player.getName()).addLores(lore).build();
            } else {
                itemStack = new ItemBuilder(Material.IRON_HELMET).setTitle("&a" + player.getName()).addLores(lore).build();
            }
            ItemStack back = new ItemBuilder(Material.ARROW).setTitle("&cBack to main menu").addLore("").addLore("&7Click to go back.").build();
            inv.setItem(i, itemStack);
            inv.setItem(35, back);
            i++;
        }
        p.openInventory(inv);
    }
}