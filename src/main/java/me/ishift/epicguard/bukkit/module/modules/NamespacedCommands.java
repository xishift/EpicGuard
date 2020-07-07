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

package me.ishift.epicguard.bukkit.module.modules;

import me.ishift.epicguard.bukkit.module.Module;
import me.ishift.epicguard.bukkit.module.ModuleManager;
import me.ishift.epicguard.core.util.ChatUtils;
import org.bukkit.entity.Player;

public class NamespacedCommands extends Module {
    public NamespacedCommands(ModuleManager manager) {
        super(manager);
    }

    @Override
    public boolean execute(Player player, String command, String[] args) {
        if (command == null || args == null) {
            return false;
        }

        if (this.getManager().blockNamespacedCommands && command.contains(":")) {
            if (this.getManager().blockNamespacedCommandsWhitelistEnabled && this.getManager().blockNamespacedCommandsWhitelist.stream().anyMatch(command::startsWith)) {
                return false;
            }

            if (this.getManager().blockNamespacedCommandsBypass && player.hasPermission("epicguard.bypass.namespaced-commands")) {
                return false;
            }

            player.sendMessage(ChatUtils.colored(this.getManager().getEpicGuard().getMessages().blockedCommand));
            return true;
        }
        return false;
    }
}