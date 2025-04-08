package ru.ilezzov.coollobby.managers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.enums.Permission;
import ru.ilezzov.coollobby.utils.PermissionsChecker;

import static ru.ilezzov.coollobby.events.DoubleJumpEvent.cooldownManager;

public class DoubleJumpManager {
    private final BukkitTask doubleJumpTask;
    private final boolean IS_ONLY_LOBBBY = Main.getConfigFile().getBoolean("double_jump.only_lobby");

    public DoubleJumpManager() {
        this.doubleJumpTask = getDoubleJumpManager().runTaskTimer(Main.getInstance(), 0, 5);
    }

    public void stop() {
        doubleJumpTask.cancel();
    }

    private BukkitRunnable getDoubleJumpManager() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.CREATIVE) {
                        continue;
                    }
                    if (!PermissionsChecker.hasPermission(player, Permission.DOUBLE_JUMP)) {
                        continue;
                    }
                    if (!cooldownManager.checkCooldown(player.getUniqueId())) {
                        continue;
                    }
                    if (IS_ONLY_LOBBBY) {
                        if (!Main.getLobbyManager().isLobby(player.getWorld())) {
                            continue;
                        }
                    }
                    if (!player.isOnGround()) {
                        continue;
                    }
                    if (player.isFlying()) {
                        continue;
                    }
                    player.setAllowFlight(true);
                }
            }
        };
    }
}
