package it.pino.townychat.addon;

import com.google.common.base.Preconditions;
import com.palmergames.bukkit.towny.TownyAPI;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import it.pino.townychat.addon.chat.TownyChatModule;
import it.pino.townychat.addon.chat.format.TownyFormatRegistry;
import it.pino.townychat.addon.command.*;
import it.pino.townychat.addon.config.ConfigurationManager;
import it.pino.townychat.addon.lang.TownyLanguageRegistry;
import it.pino.townychat.addon.user.TownyUserListeners;
import it.pino.townychat.addon.user.TownyUserService;
import it.pino.zelchat.api.ZelChatAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TownyChat implements PluginLifeCycle {

    private final @NotNull JavaPlugin boostrapPlugin;

    private final @NotNull ConfigurationManager configManager;
    private final @NotNull TownyFormatRegistry formatRegistry;
    private final @NotNull TownyLanguageRegistry languageRegistry;
    private final @NotNull TownyUserService townyService;

    private @Nullable TownyAPI townyAPI;
    private @Nullable ZelChatAPI zelchatAPI;

    TownyChat(final @NotNull JavaPlugin boostrapPlugin) {
        this.boostrapPlugin = boostrapPlugin;
        this.configManager = new ConfigurationManager(boostrapPlugin);
        this.formatRegistry = new TownyFormatRegistry(configManager);
        this.languageRegistry = new TownyLanguageRegistry(configManager);
        this.townyService = new TownyUserService(this);

    }

    @Override
    public void init() {
        registerHooksInstances();
        registerServices();
        registerChatModule();
        registerCommands();
        registerListener();
    }

    @Override
    public void reload() {
        reloadServices();
    }

    private void registerHooksInstances() {
        if (Bukkit.getPluginManager().getPlugin("Towny") != null) {
            townyAPI = TownyAPI.getInstance();
        }

        if (Bukkit.getPluginManager().getPlugin("ZelChat") != null) {
            zelchatAPI = ZelChatAPI.get();
        }
    }

    private void registerServices(){
        configManager.init();
        formatRegistry.init();
        languageRegistry.init();
    }

    private void registerChatModule(){
        final var module = new TownyChatModule(this);
        getZelchatAPI().getModuleManager().register(boostrapPlugin , module);
    }


    private void registerCommands(){
        boostrapPlugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            new AllyChatCommand(this).register(event);
            new NationChatCommand(this).register(event);
            new TownChatCommand(this).register(event);
            new GlobalChatCommand(this).register(event);
            new ChatReloadCommand(this).register(event);
        });
    }

    private void registerListener(){
       boostrapPlugin.getServer().getPluginManager().registerEvents(new TownyUserListeners(townyService), boostrapPlugin);
    }

    private void reloadServices(){
        configManager.reload();
        formatRegistry.reload();
        languageRegistry.reload();
    }

    public @NotNull ConfigurationManager getConfigManager() {
        return configManager;
    }

    public @NotNull TownyFormatRegistry getFormatRegistry() {
        return formatRegistry;
    }

    public @NotNull TownyLanguageRegistry getLanguageRegistry() {
        return languageRegistry;
    }

    public @NotNull TownyUserService getUserService() {
        return townyService;
    }

    public @NotNull TownyAPI getTownyAPI() {
        return Preconditions.checkNotNull(townyAPI, "TownyAPI has not been initialized yet!");
    }

    public @NotNull ZelChatAPI getZelchatAPI() {
        return Preconditions.checkNotNull(zelchatAPI, "ZelChatAPI has not been initialized yet!");
    }
}
