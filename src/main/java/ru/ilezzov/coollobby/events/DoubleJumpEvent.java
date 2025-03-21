package ru.ilezzov.coollobby.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.enums.Permission;
import ru.ilezzov.coollobby.manager.CooldownManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.DefaultPlaceholder;
import ru.ilezzov.coollobby.utils.PermissionsChecker;

public class DoubleJumpEvent implements Listener {
    private final CooldownManager cooldownManager = new CooldownManager(Main.getConfigFile().getLong("double_jump.cooldown"));
    private final DefaultPlaceholder doubleJumpPlaceholders = new DefaultPlaceholder();

    private final boolean isEnableDoubleJump = Main.getConfigFile().getBoolean("double_jump.enable");
    private final boolean isEnableDoubleJumpSound = Main.getConfigFile().getBoolean("double_jump.double_jump_sound.enable");
    private final boolean isEnableDoubleJumpParticle = Main.getConfigFile().getBoolean("double_jump.double_jump_particle.enable");

    private final Sound doubleJumpSound = getDoubleJumpSound();

    @EventHandler
    public void onPlayerToggleFly(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        
        if (!isEnableDoubleJump) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (Main.getFlyManager().isAllowFlight(player.getUniqueId())) {
            return;
        }

        final long cooldownTimeFinish = cooldownManager.getCooldownTime(player.getUniqueId()) - System.currentTimeMillis();

        if (cooldownTimeFinish > 0) {
            doubleJumpPlaceholders.addPlaceholder("{TIME}", String.valueOf(cooldownTimeFinish / 1000));
            player.sendMessage(PluginMessages.doubleJumpCooldownMessage(doubleJumpPlaceholders.getPlaceholders()));
            event.setCancelled(true);
            return;
        }

        if (!PermissionsChecker.hasPermission(player, Permission.DOUBLE_JUMP)) {
            return;
        }

        event.setCancelled(true);

        createDoubleJump(player);
        sendDoubleJumpEffect(player);
        playSound(player);

        player.sendMessage(PluginMessages.doubleJumpJumpMessage(doubleJumpPlaceholders.getPlaceholders()));

        cooldownManager.newCooldown(player.getUniqueId());
    }

    private void createDoubleJump(final Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));
    }

    private void playSound(final Player player) {
        if (isEnableDoubleJumpSound) {
            player.playSound(player, doubleJumpSound, 1, 1);
        }
    }

    private void sendDoubleJumpEffect(final Player player) {
        if (isEnableDoubleJumpParticle) {
            sendJumpEffectRunnable(player, getDoubleJumpParticle()).runTaskTimer(Main.getInstance(), 0, 1);
        }
    }

    private BukkitRunnable sendJumpEffectRunnable(final Player player, final Particle particle) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnGround()) {
                    cancel();
                }

                if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.WATER) {
                    cancel();
                }

                player.spawnParticle(particle, player.getLocation(), 1, 0, 0, 0, 0);
            }
        };
    }

    private Sound getDoubleJumpSound() {
        return Sound.valueOf(Main.getConfigFile().getString("double_jump.double_jump_sound.sound"));
    }

    private Particle getDoubleJumpParticle() {
        return Particle.valueOf(Main.getConfigFile().getString("double_jump.double_jump_particle.particle"));
    }
}

