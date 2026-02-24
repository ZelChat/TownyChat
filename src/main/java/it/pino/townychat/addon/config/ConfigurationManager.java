package it.pino.townychat.addon.config;

import com.google.common.base.Preconditions;
import it.pino.townychat.addon.PluginLifeCycle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public final class ConfigurationManager implements PluginLifeCycle {

    private final @NotNull JavaPlugin boostrapPlugin;

    private @Nullable FileConfiguration configuration;

    public ConfigurationManager(final @NotNull JavaPlugin boostrapPlugin) {
        this.boostrapPlugin = boostrapPlugin;
    }

    public void init() {
        boostrapPlugin.saveDefaultConfig();
        this.configuration = boostrapPlugin.getConfig();
        validateConfiguration();
    }

    public void reload(){
        boostrapPlugin.reloadConfig();
        this.configuration = boostrapPlugin.getConfig();
        validateConfiguration();
    }

    private @NotNull FileConfiguration getConfiguration() {
        return Preconditions.checkNotNull(this.configuration,
                "Configuration file has not been initialized yet!");
    }

    @NotNull
    public String get(final @NotNull ConfigKeys key) {
        return getConfiguration().getString(
                key.getPath(),
                key.getDefaultValue()
        );
    }

    private void validateConfiguration(){
        Arrays.stream(ConfigKeys.values()).forEach(key -> {
            final var path = key.getPath();
            if(getConfiguration().getString(path) == null){
                boostrapPlugin.getLogger().warning("Falling back to default for missing config key: " + path);
            }
        });
    }
}
