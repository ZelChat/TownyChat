package it.pino.townychat.addon;

public interface PluginLifeCycle {

    default void init(){}

    default void shutdown(){}

    default void reload(){}
}
