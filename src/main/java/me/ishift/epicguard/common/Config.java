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

package me.ishift.epicguard.common;

import de.leonhard.storage.Yaml;
import me.ishift.epicguard.common.detection.ProxyChecker;
import me.ishift.epicguard.common.detection.ProxyManager;
import me.ishift.epicguard.common.types.GeoMode;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

public class Config {
    public static String firewallBlacklistCommand;
    public static String firewallWhitelistCommand;
    public static boolean firewallEnabled;

    public static int connectSpeed;
    public static int pingSpeed;

    public static boolean autoWhitelist;
    public static int autoWhitelistTime;

    public static String apiKey;

    public static List<String> countryList;
    public static GeoMode countryMode;
    public static boolean cityEnabled;
    public static boolean countryEnabled;

    public static List<String> blockedNames;
    public static boolean updater;
    public static boolean tabCompleteBlock;

    public static List<String> blockedCommands;
    public static List<String> allowedCommands;

    public static List<String> opProtectionList;
    public static String opProtectionAlert;
    public static String opProtectionCommand;

    public static boolean allowedCommandsBypass;
    public static boolean blockedCommandsEnable;
    public static boolean allowedCommandsEnable;

    public static boolean opProtectionEnable;
    public static boolean rejoinCheck;
    public static boolean serverListCheck;

    public static boolean filterEnabled;
    public static List<String> filterValues;

    public static boolean customTabComplete;
    public static List<String> customTabCompleteList;
    public static boolean customTabCompleteBypass;

    public static boolean advancedProxyChecker;

    private static void load(Yaml config) {
        firewallEnabled = config.getBoolean("firewall");
        firewallBlacklistCommand = config.getString("firewall.command-blacklist");
        firewallWhitelistCommand = config.getString("firewall.command-whitelist");
        connectSpeed = config.getInt("speed.connection");
        pingSpeed = config.getInt("speed.ping-speed");
        autoWhitelist = config.getBoolean("auto-whitelist.enabled");
        autoWhitelistTime = config.getInt("auto-whitelist.time");
        apiKey = config.getString("antibot.api-key");
        countryList = config.getStringList("countries.list");
        updater = config.getBoolean("updater");
        tabCompleteBlock = config.getBoolean("fully-block-tab-complete");
        blockedCommands = config.getStringList("command-protection.list");
        allowedCommands = config.getStringList("allowed-commands.list");
        opProtectionList = config.getStringList("op-protection.list");
        opProtectionAlert = config.getString("op-protection.alert");
        opProtectionCommand = config.getString("op-protection.command");
        blockedCommandsEnable = config.getBoolean("command-protection.enabled");
        allowedCommandsEnable = config.getBoolean("allowed-commands.enabled");
        opProtectionEnable = config.getBoolean("op-protection.enabled");
        blockedNames = config.getStringList("antibot.name-contains");
        filterEnabled = config.getBoolean("console-filter.enabled");
        filterValues = config.getStringList("console-filter.messages");
        customTabComplete = config.getBoolean("custom-tab-complete.enabled");
        customTabCompleteList = config.getStringList("custom-tab-complete.list");
        allowedCommandsBypass = config.getBoolean("bypass.allowed-commands");
        customTabCompleteBypass = config.getBoolean("bypass.custom-tab-complete");

        serverListCheck = config.getOrSetDefault("antibot.server-list-check", true);
        rejoinCheck = config.getOrSetDefault("antibot.rejoin-check", true);

        cityEnabled = config.getOrSetDefault("download-databases.city", true);
        countryEnabled = config.getOrSetDefault("download-databases.country", true);

        final String countryModeString = config.getString("countries.mode");
        countryMode = GeoMode.valueOf(countryModeString);

        advancedProxyChecker = config.getOrSetDefault("advanced-proxy-checker.enabled", false);
        // Setting example.
        config.setDefault("advanced-proxy-checker.checkers.1.url", "http://proxycheck.io/v2/{ADDRESS}");
        config.setDefault("advanced-proxy-checker.checkers.1.contains", Arrays.asList("yes", "VPN"));
        if (!advancedProxyChecker) {
            return;
        }

        final String basePath = "advanced-proxy-checker.checkers";
        config.getSection(basePath).singleLayerKeySet().stream().map(num -> basePath + "." + num).forEachOrdered(path -> {
            final String url = config.getString(path + ".url");
            final List<String> contains = config.getStringList(path + ".contains");
            ProxyManager.getCheckers().add(new ProxyChecker(url, contains));
        });
    }

    public static void loadBukkit() {
        final Yaml config = new Yaml("config.yml", "plugins/EpicGuard");
        load(config);
    }

    public static void loadBungee() {
        final Yaml config = new Yaml("config_bungee.yml", "plugins/EpicGuard");
        load(config);
    }
}
