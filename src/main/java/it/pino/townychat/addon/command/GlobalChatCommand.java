package it.pino.townychat.addon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEvent;
import it.pino.townychat.addon.TownyChat;
import it.pino.townychat.addon.command.model.TownyCommand;
import it.pino.townychat.addon.lang.TownyLanguageRegistry;
import it.pino.townychat.addon.user.TownyUserService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GlobalChatCommand implements TownyCommand {

    private final @NotNull TownyUserService townyUserService;
    private final @NotNull TownyLanguageRegistry languageRegistry;

    public GlobalChatCommand(@NotNull TownyChat main) {
        this.townyUserService = main.getUserService();
        this.languageRegistry = main.getLanguageRegistry();
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getCommandNode() {
        return Commands.literal("globalchat")
                .executes(ctx -> {
                    final var sender = ctx.getSource().getSender();
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage(Component.text("Only players can use this command!", NamedTextColor.RED));
                        return Command.SINGLE_SUCCESS;
                    }

                    townyUserService.getUserChannel(player.getUniqueId()).ifPresentOrElse(ch -> {
                        townyUserService.removeUserChannel(player.getUniqueId());
                        final var message = languageRegistry.getLanguage(TownyLanguageRegistry.LangKeys.GLOBAL_CHAT);
                        player.sendMessage(message);

                    }, () -> {
                        final var message = languageRegistry.getLanguage(TownyLanguageRegistry.LangKeys.ALREADY_GLOBAL_CHAT);
                        player.sendMessage(message);
                    });

                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
