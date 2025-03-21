package ru.ilezzov.coollobby.commands;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.enums.Permission;
import ru.ilezzov.coollobby.manager.CooldownManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.models.DefaultPlaceholder;
import ru.ilezzov.coollobby.utils.PermissionsChecker;

import java.util.Random;

public class FireworkCommand implements CommandExecutor {
    private final Random random = new Random();
    private final long cooldownTime = Main.getConfigFile().getInt("fw_command.cooldown");
    private final boolean isEnable = Main.getConfigFile().getBoolean("fw_command.enable");
    private final boolean isOnlyLobby = Main.getConfigFile().getBoolean("fw_command.only_lobby");
    private final CooldownManager cooldownManager = new CooldownManager(cooldownTime);
    private final DefaultPlaceholder commandPlaceholders = new DefaultPlaceholder();

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] strings) {
        if(!isEnable) {
            sender.sendMessage(PluginMessages.commandDisableCommandMessage(commandPlaceholders.getPlaceholders()));
            return true;
        }

        if (!(sender instanceof final Player player)) {
            sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders.getPlaceholders()));
            return true;
        }

        if (!PermissionsChecker.hasPermission(player, Permission.FIREWORK)) {
            sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders.getPlaceholders()));
            return true;
        }

        if (isOnlyLobby) {
            if (Main.getWorldManager().getWorld().getUID() != player.getWorld().getUID()) {
                commandPlaceholders.addPlaceholder("{WORLD}", Main.getWorldManager().getWorld().getName());
                sender.sendMessage(PluginMessages.commandOnlyLobbyCommandMessage(commandPlaceholders.getPlaceholders()));
                return true;
            }
        }

        final long cooldownTimeFinish = cooldownManager.getCooldownTime(player.getUniqueId()) - System.currentTimeMillis();

        if (cooldownTimeFinish > 0) {
            commandPlaceholders.addPlaceholder("{TIME}", String.valueOf(cooldownTimeFinish / 1000));
            sender.sendMessage(PluginMessages.pluginCooldownMessage(commandPlaceholders.getPlaceholders()));
            return true;
        }

        spawnFirework(player);
        player.sendMessage(PluginMessages.commandFwCommandMessage(commandPlaceholders.getPlaceholders()));

        if(!PermissionsChecker.hasPermission(player, Permission.NO_COOLDOWN)) {
            cooldownManager.newCooldown(player.getUniqueId());
        }

        return true;
    }

    private void spawnFirework(final Player player) {
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
        return (Firework) world.spawnEntity(player.getLocation(), EntityType.FIREWORK_ROCKET);
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


}
