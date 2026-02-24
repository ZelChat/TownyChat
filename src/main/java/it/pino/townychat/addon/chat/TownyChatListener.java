package it.pino.townychat.addon.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import it.pino.townychat.addon.TownyChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public final class TownyChatListener implements Listener {

    private final @NotNull TownyChat main;

    public TownyChatListener(@NotNull TownyChat mainInstance) {
        this.main = mainInstance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        if(event.isCancelled()) return;

        final var player = event.getPlayer();
        final var hasChannel = main.getUserService().getUserChannel(player.getUniqueId()).isPresent();
        if(!hasChannel) return;

        event.setCancelled(true);

    }
}
