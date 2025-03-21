package ru.ilezzov.coollobby.commands;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.World;
import org.bukkit.block.data.type.Fire;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.manager.CooldownManager;
import ru.ilezzov.coollobby.messages.PluginMessages;
import ru.ilezzov.coollobby.utils.Permissions;

import java.util.HashMap;
import java.util.Random;

import static ru.ilezzov.coollobby.manager.CooldownManager.*;

public class FwCommand implements CommandExecutor {
    private final Random random = new Random();
    private final long cooldownTime = 1000 * 10;
    private final CooldownManager cooldownManager = new CooldownManager(cooldownTime);

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String s, final @NotNull String @NotNull [] strings) {
        final HashMap<String, String> commandPlaceholders = new HashMap<>();
        commandPlaceholders.put("{P}", Main.getPrefix());

        if (!(sender instanceof final Player player)) {
            sender.sendMessage(PluginMessages.pluginNoConsoleMessage(commandPlaceholders));
            return true;
        }

        if (!Permissions.hasPermission(player, Permissions.COMMAND_FW_COMMAND)) {
            sender.sendMessage(PluginMessages.pluginNoPermsMessage(commandPlaceholders));
            return true;
        }

        final long cooldownTimeFinish = cooldownManager.getCooldownTime(player.getUniqueId()) - System.currentTimeMillis();

        if (cooldownTimeFinish > 0) {
            commandPlaceholders.put("{TIME}", String.valueOf(cooldownTimeFinish / 1000));
            sender.sendMessage(PluginMessages.pluginCooldownMessage(commandPlaceholders));
            return true;
        }

        spawnFirework(player);
        cooldownManager.newCooldown(player.getUniqueId());
        player.sendMessage(PluginMessages.commandFwCommandMessage(commandPlaceholders));

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
