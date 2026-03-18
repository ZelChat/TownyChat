package it.pino.townychat.addon.chat;

import it.pino.townychat.addon.TownyChat;
import it.pino.townychat.addon.chat.format.TownyChatFormat;
import it.pino.zelchat.api.message.ChatMessage;
import it.pino.zelchat.api.message.channel.ChannelType;
import it.pino.zelchat.api.message.formatter.ChatFormatDecorator;
import it.pino.zelchat.api.message.state.MessageState;
import it.pino.zelchat.api.module.ChatModule;
import it.pino.zelchat.api.module.annotation.ChatModuleSettings;
import it.pino.zelchat.api.module.priority.ModulePriority;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@ChatModuleSettings(pluginOwner = "TownyChatAddon", priority = ModulePriority.HIGHEST)
public final class TownyChatModule implements ChatModule {

    private final @NotNull TownyChat main;
    private final @NotNull ChatFormatDecorator decorator;

    public TownyChatModule(@NotNull TownyChat mainInstance) {
        this.main = mainInstance;
        this.decorator = main.getZelchatAPI().getFormatDecorator();
    }

    @Override
    public void handleChatMessage(final @NotNull ChatMessage chatMessage) {
        final var state = chatMessage.getState();
        if (state != MessageState.READY && state != MessageState.PROCESSING) return;

        final var channelType = chatMessage.getChannel().getType();
        if (channelType != ChannelType.EVERYONE) return;

        final var player = chatMessage.getBukkitPlayer();
        main.getUserService().getUserChannel(player.getUniqueId())
                .ifPresent(townyChannelType -> {
                    final var format = main.getFormatRegistry().getTownyChatFormat(townyChannelType);
                    switch (townyChannelType) {
                        case TOWN -> handleTownChannel(chatMessage, format);
                        case NATION -> handleNationChannel(chatMessage, format);
                        case ALLY -> handleAllyChannel(chatMessage, format);
                    }
                });
    }

    private void handleTownChannel(final @NotNull ChatMessage chatMessage, final @NotNull TownyChatFormat format) {
        final var player = chatMessage.getBukkitPlayer();
        main.getUserService().getTown(player).ifPresentOrElse(town -> {

            applyCommonSettings(chatMessage, format);

            final var residents = main.getUserService().getTownOnlineResidentsAudience(town);
            chatMessage.getChannel().setViewers(residents);
            dispatch(chatMessage);

        }, () -> main.getUserService().removeUserChannel(player.getUniqueId()));
    }

    private void handleNationChannel(final @NotNull ChatMessage chatMessage, final @NotNull TownyChatFormat format) {
        final var player = chatMessage.getBukkitPlayer();
        main.getUserService().getNation(player).ifPresentOrElse(nation -> {

            applyCommonSettings(chatMessage, format);

            final var residents = main.getUserService().getNationOnlineResidentsAudience(nation);
            chatMessage.getChannel().setViewers(residents);
            dispatch(chatMessage);

        }, () -> main.getUserService().removeUserChannel(player.getUniqueId()));
    }

    private void handleAllyChannel(final @NotNull ChatMessage chatMessage, final @NotNull TownyChatFormat format) {
        final var player = chatMessage.getBukkitPlayer();
        main.getUserService().getTown(player).ifPresentOrElse(town -> {

            applyCommonSettings(chatMessage, format);

            final var residents = main.getUserService().getAlliesOnlineResidentsAudience(town);
            chatMessage.getChannel().setViewers(residents);
            dispatch(chatMessage);

        }, () -> main.getUserService().removeUserChannel(player.getUniqueId()));
    }

    private void applyCommonSettings(final @NotNull ChatMessage chatMessage, final @NotNull TownyChatFormat format) {
        chatMessage.setFormat(format);
        chatMessage.getChannel().setType(ChannelType.CUSTOM);
    }

    private void dispatch(@NotNull ChatMessage chatMessage) {
        if(Bukkit.getPluginManager().getPlugin("DiscordSRV") == null)
            return;

        chatMessage.setState(MessageState.CANCELLED);
        decorator.applyFormat(chatMessage);
        chatMessage.getChannel().getViewers().forEach(viewer -> viewer.sendMessage(chatMessage.getMessage()));
    }

}
