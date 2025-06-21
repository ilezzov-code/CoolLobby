package ru.ilezzov.coollobby.events.listeners;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import ru.ilezzov.coollobby.Main;
import ru.ilezzov.coollobby.managers.LobbyManager;

public class FireTickEvent implements Listener {
    private final LobbyManager lobbyManager = Main.getLobbyManager();

    @EventHandler
    public void onBlockSpreadEvent(final BlockSpreadEvent event) {
        final Block block = event.getSource();
        final World world = block.getWorld();

        if (!lobbyManager.isLobby(world)) {
            return;
        }

        if (block.getType() != Material.FIRE) {
            return;
        }

        event.setCancelled(true);
    }


    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent event) {
        final World world = event.getBlock().getWorld();

        if (!lobbyManager.isLobby(world)) {
            return;
        }

        if (event.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL &&
                event.getCause() != BlockIgniteEvent.IgniteCause.FIREBALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBurn(final BlockBurnEvent event) {
        final World world = event.getBlock().getWorld();

        if (!lobbyManager.isLobby(world)) {
            return;
        }

        event.setCancelled(true);
    }
}
