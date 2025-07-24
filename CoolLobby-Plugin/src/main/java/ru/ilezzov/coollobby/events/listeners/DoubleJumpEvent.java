package ru.ilezzov.coollobby.events.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.api.CoolLobbyApi;
import ru.ilezzov.coollobby.database.data.player.PlayerData;
import ru.ilezzov.coollobby.database.data.player.PlayerDataRepository;
import ru.ilezzov.coollobby.managers.CooldownManager;
import ru.ilezzov.coollobby.managers.DoubleJumpManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.permission.Permission;
import ru.ilezzov.coollobby.permission.PermissionsChecker;
import ru.ilezzov.coollobby.placeholder.PluginPlaceholder;

public class DoubleJumpEvent implements Listener {
    private final CooldownManager cooldownManager = Main.getDoubleJumpManager().getCooldownManager();
    private final PlayerDataRepository playerDataRepository = Main.getPlayerDataRepository();

    private final CoolLobbyApi api = Main.getApi();

    private final boolean isEnableParticle = Main.getConfigFile().getBoolean("double_jump.double_jump_particle.enable");
    private final Particle particle = getParticle();

    private final boolean isEnableSound = Main.getConfigFile().getBoolean("double_jump.double_jump_sound.enable");
    private final Sound sound = getSound();

    private final PluginPlaceholder placeholder = new PluginPlaceholder();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerToggleFlightEvent(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (!PermissionsChecker.hasPermission(player, Permission.DOUBLE_JUMP)) {
            return;
        }

        final PlayerData playerData = playerDataRepository.getFromCacheSync(player.getUniqueId());

        if (playerData == null) {
            playerDataRepository.get(player.getUniqueId());
            event.setCancelled(true);
            return;
        }

        if (playerData.isFly()) {
            return;
        }

        event.setCancelled(true);
        api.createDoubleJump(player, particle, sound);

        final Component message = PluginMessages.playerDoubleJumpMessage(placeholder);
        if (message != null) {
            player.sendMessage(message);
        }

        if (!PermissionsChecker.hasPermission(player, Permission.NO_COOLDOWN)) {
            cooldownManager.newCooldown(player.getUniqueId());
            startCooldownBarTask(player).runTaskTimer(Main.getInstance(), 0, 20);
        }
    }
    private BukkitRunnable startCooldownBarTask(final Player player) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!cooldownManager.checkCooldown(player.getUniqueId())) {
                    cancel();
                }
                placeholder.addPlaceholder("{TIME}", cooldownManager.getRemainingTime(player.getUniqueId()));
                player.sendActionBar(PluginMessages.playerDoubleJumpActionBarCooldownMessage(placeholder));
            }
        };
    }

    private Particle getParticle() {
        if (!isEnableParticle) {
            return null;
        }
        return Particle.valueOf(Main.getConfigFile().getString("double_jump.double_jump_particle.particle"));
    }

    private Sound getSound() {
        if (!isEnableSound) {
            return null;
        }
        return Sound.valueOf(Main.getConfigFile().getString("double_jump.double_jump_sound.sound"));
    }
}
