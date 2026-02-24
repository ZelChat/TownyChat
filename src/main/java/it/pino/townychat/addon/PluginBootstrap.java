package it.pino.townychat.addon;

import org.bukkit.plugin.java.JavaPlugin;

public final class PluginBootstrap extends JavaPlugin {

    @Override
    public void onEnable() {
        try{
            new TownyChat(this).init();
        } catch (final Exception e){
            getLogger().severe("Failed to enable TownyChat addon due to an error: " + e);
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

}
