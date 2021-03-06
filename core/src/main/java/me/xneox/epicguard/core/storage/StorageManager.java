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

package me.xneox.epicguard.core.storage;

import com.google.common.net.InetAddresses;
import org.apache.commons.lang3.Validate;
import me.xneox.epicguard.core.storage.impl.JsonStorageProvider;
import me.xneox.epicguard.core.user.PendingUser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * This class manages the stored data and some global cache.
 *
 * TODO: Other storage implementations than just JSON.
 */
@SuppressWarnings("UnstableApiUsage")
public class StorageManager {
    private final StorageProvider provider; // Storage implementation.
    private final Collection<String> pingCache = new HashSet<>(); // Stores addresses of users who pinged the server.

    public StorageManager() {
        this.provider = new JsonStorageProvider();
        this.provider.load();
    }

    public StorageProvider provider() {
        return provider;
    }

    /**
     * Retrieves a list of nicknames used by specified IP Address.
     */
    @Nonnull
    public List<String> accounts(@Nonnull PendingUser user) {
        Validate.notNull(user, "BotUser cannot be null!");
        return this.provider.accountMap().getOrDefault(user.address(), new ArrayList<>());
    }

    /**
     * If the user's address is not in the accountMap, it will be added.
     */
    public void updateAccounts(@Nonnull PendingUser user) {
        Validate.notNull(user, "BotUser cannot be null!");

        List<String> accounts = this.accounts(user);
        if (!accounts.contains(user.nickname())) {
            accounts.add(user.nickname());
        }

        this.provider.accountMap().put(user.address(), accounts);
    }

    /**
     * Searches for the last used address of the specified nickname.
     * Returns null if not found.
     */
    @Nullable
    public String findByNickname(@Nonnull String nickname) {
        return this.provider.accountMap().entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(nick -> nick.equalsIgnoreCase(nickname)))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public void blacklistPut(@Nonnull String value) {
        if (InetAddresses.isInetAddress(value)) {
            this.provider.addressBlacklist().add(value);
        } else {
            this.provider.nameBlacklist().add(value);
        }
    }

    public void whitelistPut(@Nonnull String value) {
        if (InetAddresses.isInetAddress(value)) {
            this.provider.addressWhitelist().add(value);
        } else {
            this.provider.nameWhitelist().add(value);
        }
    }

    public boolean isBlacklisted(String value) {
        if (InetAddresses.isInetAddress(value)) {
            return this.provider.addressBlacklist().contains(value);
        }
        return this.provider.nameBlacklist().contains(value);
    }

    public boolean isWhitelisted(String value) {
        if (InetAddresses.isInetAddress(value)) {
            return this.provider.addressWhitelist().contains(value);
        }
        return this.provider.nameWhitelist().contains(value);
    }

    public void removeBlacklist(String value) {
        if (InetAddresses.isInetAddress(value)) {
            this.provider.addressBlacklist().remove(value);
        } else {
            this.provider.nameBlacklist().remove(value);
        }
    }

    public void removeWhitelist(String value) {
        if (InetAddresses.isInetAddress(value)) {
            this.provider.addressWhitelist().remove(value);
        } else {
            this.provider.nameWhitelist().remove(value);
        }
    }

    @Nonnull
    public Collection<String> pingCache() {
        return this.pingCache;
    }
}
