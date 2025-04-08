package ru.ilezzov.coollobby.events;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.enums.Permission;
import ru.ilezzov.coollobby.managers.CooldownManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.PluginPlaceholder;
import ru.ilezzov.coollobby.utils.PermissionsChecker;

public class DoubleJumpEvent implements Listener {
    private final boolean isEnableParticle = Main.getConfigFile().getBoolean("double_jump.double_jump_particle.enable");
    private final Particle particle = getParticle();

    private final boolean isEnableSound = Main.getConfigFile().getBoolean("double_jump.double_jump_sound.enable");
    private final Sound sound = getSound();

    private static final int cooldownSeconds = Main.getConfigFile().getInt("double_jump.cooldown");
    public static final CooldownManager cooldownManager = new CooldownManager(cooldownSeconds);

    private final PluginPlaceholder placeholder = new PluginPlaceholder();

    @EventHandler
    public void onPlayerToggleFlight(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();

        if (Main.getFlyManager().isAllowFlight(player.getUniqueId())) {
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (!PermissionsChecker.hasPermission(player, Permission.DOUBLE_JUMP)) {
            return;
        }

        event.setCancelled(true);
        Main.getApi().createDoubleJump(player, particle, sound);
        player.sendMessage(PluginMessages.playerDoubleJumpMessage(placeholder));
        startCooldownBarTask(player).runTaskTimer(Main.getInstance(), 0, 20);

        if (!PermissionsChecker.hasPermission(player, Permission.NO_COOLDOWN)) {
            cooldownManager.newCooldown(player.getUniqueId());
        }
    }

    private BukkitRunnable startCooldownBarTask(final Player player) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (cooldownManager.checkCooldown(player.getUniqueId())) {
                    return;
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
