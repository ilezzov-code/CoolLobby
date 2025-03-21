package ru.ilezzov.coollobby.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ilezzov.coollobby.Main;

public class DoubleJumpManager {
    public static void start() {
        getDoubleJumpRunnable().runTaskTimer(Main.getInstance(), 0, 10);
    }

    private static BukkitRunnable getDoubleJumpRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                final boolean isOnlyLobby = Main.getConfigFile().getBoolean("double_jump.only_lobby");

                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (Main.getFlyManager().isAllowFlight(player.getUniqueId())) {
                        return;
                    }

                    if (!player.isOnGround()) {
                        return;
                    }

                    if (player.isFlying()) {
                        return;
                    }

                    if (isOnlyLobby) {
                        if (Main.getWorldManager().getWorld().getUID() != player.getWorld().getUID()) {
                            return;
                        }
                    }

                    player.setAllowFlight(true);
                }
            }
        };
    }
}
