package it.pino.townychat.addon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import it.pino.townychat.addon.TownyChat;
import it.pino.townychat.addon.command.model.TownyCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class ChatReloadCommand implements TownyCommand {

    private final @NotNull TownyChat main;

    private final Component reloadMessage = Component.text("TownyChat addon configuration reloaded!", NamedTextColor.GREEN);

    public ChatReloadCommand(@NotNull TownyChat main) {
        this.main = main;
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getCommandNode() {
        return Commands.literal("townychataddonreload")
                .requires(css -> css.getSender().hasPermission("zelchat.addon.towny.reload"))
                .executes(ctx -> {

                    final var sender = ctx.getSource().getSender();
                    main.reload();
                    sender.sendMessage(reloadMessage);

                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
