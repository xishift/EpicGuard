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

package me.xneox.epicguard.core.manager;

import me.xneox.epicguard.core.util.Cooldown;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.diorite.libs.org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class CooldownManager {
    private final Map<String, Cooldown> cooldowns = new HashMap<>();

    public void add(@NonNull Cooldown cooldown) {
        Validate.notNull(cooldown, "Cooldown cannot be null!");
        String id = cooldown.getId();
        cooldowns.put(id, cooldown);
    }

    public boolean hasCooldown(@Nonnull String id) {
        Validate.notNull(id, "Cooldown ID cannot be null!");
        Cooldown cooldown = cooldowns.get(id);
        if (cooldown == null) {
            return false;
        }
        if (cooldown.hasExpired()) {
            this.cooldowns.remove(id);
            return false;
        }
        return true;
    }
}
