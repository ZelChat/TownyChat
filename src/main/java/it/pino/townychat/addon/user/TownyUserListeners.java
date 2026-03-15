package it.pino.townychat.addon.user;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public final class TownyUserListeners implements Listener {

    private final @NotNull TownyUserService townyUserService;

    public TownyUserListeners(@NotNull TownyUserService townyUserService) {
        this.townyUserService = townyUserService;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(final @NotNull PlayerQuitEvent event) {
        var uniqueId = event.getPlayer().getUniqueId();
        townyUserService.removeUserChannel(uniqueId);
    }
}
