/*
 * EpicGuard is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EpicGuard is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package me.xneox.epicguard.bukkit;

import me.xneox.epicguard.bukkit.command.EpicGuardCommand;
import me.xneox.epicguard.bukkit.listener.CommandListener;
import me.xneox.epicguard.bukkit.listener.PlayerJoinListener;
import me.xneox.epicguard.bukkit.listener.PlayerPreLoginListener;
import me.xneox.epicguard.bukkit.listener.PlayerQuitListener;
import me.xneox.epicguard.bukkit.listener.ServerPingListener;
import me.xneox.epicguard.bukkit.module.ModuleManager;
import me.xneox.epicguard.bukkit.module.ModuleTask;
import me.xneox.epicguard.bukkit.util.Metrics;
import me.xneox.epicguard.bukkit.util.Reflections;
import me.xneox.epicguard.core.EpicGuard;
import me.xneox.epicguard.core.PlatformPlugin;
import me.xneox.epicguard.core.util.ChatUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PlatformBukkit extends JavaPlugin implements PlatformPlugin {
    private EpicGuard epicGuard;
    private ModuleManager moduleManager;

    @Override
    public void onEnable() {
        this.epicGuard = new EpicGuard(this);
        this.moduleManager = new ModuleManager(this.epicGuard);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerPreLoginListener(this.epicGuard), this);
        pm.registerEvents(new PlayerQuitListener(this.epicGuard), this);
        pm.registerEvents(new PlayerJoinListener(this.epicGuard), this);
        pm.registerEvents(new ServerPingListener(this.epicGuard), this);
        pm.registerEvents(new CommandListener(this), this);

        Bukkit.getScheduler().runTaskTimer(this, new ModuleTask(this), 20L * 5L, 20L);
        this.getCommand("epicguard").setExecutor(new EpicGuardCommand(this, this.epicGuard));

        new Metrics(this, 5845);
    }

    @Override
    public void onDisable() {
        this.epicGuard.shutdown();
    }

    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    @Override
    public void sendActionBar(@Nonnull String message, @Nonnull UUID target) {
        Player player = Bukkit.getPlayer(target);
        if (Reflections.getVersion().startsWith("v1_16")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtils.colored(message)));
        } else {
            Reflections.sendActionBar(player, message); // backwards compatibility
        }
    }

    @Override
    public String getVersion() {
        return this.getDescription().getVersion();
    }

    @Override
    public void runTaskLater(@NotNull Runnable task, long seconds) {
        Bukkit.getScheduler().runTaskLater(this, task, seconds * 20L);
    }

    @Override
    public void scheduleTask(@Nonnull Runnable task, long seconds) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, task, 20L, seconds * 20L);
    }
}
