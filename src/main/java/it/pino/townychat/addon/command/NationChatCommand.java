package it.pino.townychat.addon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import it.pino.townychat.addon.TownyChat;
import it.pino.townychat.addon.chat.channel.TownyChannelType;
import it.pino.townychat.addon.command.model.TownyCommand;
import it.pino.townychat.addon.lang.TownyLanguageRegistry;
import it.pino.townychat.addon.user.TownyUserService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class NationChatCommand implements TownyCommand {

    private final @NotNull TownyUserService townyUserService;
    private final @NotNull TownyLanguageRegistry languageRegistry;

    public NationChatCommand(@NotNull TownyChat main) {
        this.townyUserService = main.getUserService();
        this.languageRegistry = main.getLanguageRegistry();
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getCommandNode() {
        return Commands.literal("nationchat")
                .requires(css -> css.getSender().hasPermission("zelchat.addon.towny.nationchat"))
                .executes(ctx -> {
                    final var sender = ctx.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage(Component.text("Only players can use this command!", NamedTextColor.RED));
                        return Command.SINGLE_SUCCESS;
                    }

                    final var channel = townyUserService.getUserChannel(player.getUniqueId());
                    if (channel.isPresent() && channel.get() == TownyChannelType.NATION) {
                        townyUserService.removeUserChannel(player.getUniqueId());
                        final var message = languageRegistry.getLanguage(TownyLanguageRegistry.LangKeys.GLOBAL_CHAT);
                        player.sendMessage(message);
                        return Command.SINGLE_SUCCESS;
                    }

                    townyUserService.getNation(player).ifPresentOrElse(town -> {
                        townyUserService.setUserChannel(player.getUniqueId(), TownyChannelType.NATION);
                        final var message = languageRegistry.getLanguage(TownyLanguageRegistry.LangKeys.NATION_CHAT);
                        player.sendMessage(message);

                    }, () -> {
                        final var message = languageRegistry.getLanguage(TownyLanguageRegistry.LangKeys.NO_NATION);
                        player.sendMessage(message);
                    });

                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }


}
