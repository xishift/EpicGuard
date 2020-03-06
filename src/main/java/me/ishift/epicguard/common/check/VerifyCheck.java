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

package me.ishift.epicguard.common.check;

import me.ishift.epicguard.common.Config;
import me.ishift.epicguard.common.AttackSpeed;

import java.util.ArrayList;
import java.util.List;

public class VerifyCheck {
    private static List<String> rejoined = new ArrayList<>();

    public static boolean perform(String nickname) {
        if (Config.rejoinCheck && AttackSpeed.isUnderAttack()) {
            if (!rejoined.contains(nickname)) {
                rejoined.add(nickname);
                return true;
            }
        }
        return false;
    }
}