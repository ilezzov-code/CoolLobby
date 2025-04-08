package ru.ilezzov.coollobby.api;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ilezzov.coollobby.Main;

import java.util.Random;
import java.util.UUID;

public class CoolLobbyApi {
    private final Random random = new Random();

    public CoolLobbyApi() {}

    public void sendFirework(final Player player) {
        final World world = player.getWorld();

        final Firework firework = createFirework(world, player);
        final FireworkMeta fireworkMeta = firework.getFireworkMeta();
        final FireworkEffect fireworkEffect = FireworkEffect.builder()
                .with(getFireworkRandomType())
                .withColor(getRandomColors())
                .withFade(getRandomColors())
                .flicker(true)
                .trail(true)
                .build();

        fireworkMeta.addEffect(fireworkEffect);
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }

    private Firework createFirework(final World world, final Player player) {
        return (Firework) world.spawnEntity(player.getLocation(), EntityType.FIREWORK);
    }

    private FireworkEffect.Type getFireworkRandomType() {
        final FireworkEffect.Type[] types = FireworkEffect.Type.values();
        return types[random.nextInt(types.length)];
    }

    private Color[] getRandomColors() {
        final int count = random.nextInt(3) + 1;
        final Color[] colors = new Color[count];

        for (int i = 0; i < count; i++) {
            colors[i] = Color.fromRGB(random.nextInt(0xFFFFFF));
        }

        return colors;
    }

    public void sendLighting(final Player player) {
        final World world = player.getWorld();
        world.strikeLightningEffect(player.getLocation());
    }

    public void sendSpit(final Player player) {
        final World world = player.getWorld();
        final LlamaSpit llamaSpit = (LlamaSpit) world.spawnEntity(player.getLocation().toVector().add(player.getLocation().getDirection().multiply(0.8)).toLocation(player.getWorld()).add(0.0, 1.0, 0.0), EntityType.LLAMA_SPIT);

        llamaSpit.setVelocity(player.getEyeLocation().getDirection().multiply(1));
        world.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 1, 1);
    }

    public void createDoubleJump(final Player player, final Particle particle, final Sound sound) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));

        if (particle != null)
            playParticles(player, particle).runTaskTimer(Main.getInstance(), 0, 1);
        if (sound !=null)
            playSound(player, sound);
    }

    private BukkitRunnable playParticles(final Player player, final Particle particle) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnGround()) {
                    cancel();
                }

                if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.WATER) {
                    cancel();
                }

                player.getWorld().spawnParticle(particle, player.getLocation(), 1, 0, 0, 0, 0);
            }
        };
    }

    private void playSound(final Player player, final Sound sound) {
        player.playSound(player, sound, 1, 1);
    }

    public boolean setFly(final Player player) {
        final UUID playerUniqueId = player.getUniqueId();

        if (Main.getFlyManager().isAllowFlight(playerUniqueId)) {
            disableFlyMode(player);
            Main.getFlyManager().addPlayer(playerUniqueId, false);
            return false;
        }

        enableFlyMode(player);
        Main.getFlyManager().addPlayer(playerUniqueId, true);
        return true;
    }

    private void enableFlyMode(final Player player) {
        player.setAllowFlight(true);
    }

    private void disableFlyMode(final Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public void setLevel(final Player player, final int level) {
        if (level == -1) {
            return;
        }
        player.setLevel(level);
    }

    public void setGameMode(final Player player, final GameMode gameMode) {
        if (gameMode == null) {
            return;
        }
        player.setGameMode(gameMode);
    }

    public void setFoodLevel(final Player player, final int foodLevel) {
        if (foodLevel == -1) {
            return;
        }
        player.setFoodLevel(foodLevel);
    }

}
