package it.pino.townychat.addon.command.model;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEvent;

public interface TownyCommand {

    LiteralCommandNode<CommandSourceStack> getCommandNode();

    default void register(RegistrarEvent<Commands> registar){
        registar.registrar().register(getCommandNode());
    }
}
