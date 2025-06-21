package ru.ilezzov.coollobby.managers;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.permission.Permission;
import ru.ilezzov.coollobby.permission.PermissionsChecker;

public class DoubleJumpManager {
    private final int cooldownTime = Main.getConfigFile().getInt("double_jump.cooldown");

    private final BukkitTask doubleJumpTask;
    @Getter
    private final CooldownManager cooldownManager = new CooldownManager(cooldownTime);
    private final LobbyManager lobbyManager = Main.getLobbyManager();

    private final boolean isOnlyLobby = Main.getConfigFile().getBoolean("double_jump.only_lobby");

    public DoubleJumpManager() {
        this.doubleJumpTask = startDoubleJumpTask();
    }

    public void stopTask() {
        doubleJumpTask.cancel();
    }

    private BukkitTask startDoubleJumpTask() {
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
                    if (cooldownManager.checkCooldown(player.getUniqueId())) {
                        continue;
                    }
                    if (isOnlyLobby) {
                        if (!lobbyManager.isLobby(player.getWorld())) {
                            continue;
                        }
                    }
                    if (!player.isOnGround()) {
                        continue;
                    }
                    player.setAllowFlight(true);
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 10);
    }

}
