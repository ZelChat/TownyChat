package it.pino.townychat.addon.lang;

import it.pino.townychat.addon.PluginLifeCycle;
import it.pino.townychat.addon.config.ConfigKeys;
import it.pino.townychat.addon.config.ConfigurationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TownyLanguageRegistry implements PluginLifeCycle {

    private final @NotNull Map<LangKeys, Component> languageMap = new ConcurrentHashMap<>(7);
    private final @NotNull MiniMessage miniMessage = MiniMessage.miniMessage();

    private final @NotNull ConfigurationManager configurationManager;

    public TownyLanguageRegistry(final @NotNull ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @Override
    public void init() {
        registerLanguages();
    }

    @Override
    public void reload() {
        languageMap.clear();
        registerLanguages();
    }

    private void registerLanguages(){
        for (final var langKey : LangKeys.values()) {
            registerLanguage(langKey);
        }
    }

    private void registerLanguage(final @NotNull LangKeys langKey) {
        final var messageString = configurationManager.get(langKey.getConfigKey())
                .replace("&", "")
                .replace("§", "");

        final var messageComponent = miniMessage.deserialize(messageString);

        languageMap.put(langKey, messageComponent);
    }

    @NotNull
    public Component getLanguage(final @NotNull LangKeys key) {
        return languageMap.get(key);
    }


    public enum LangKeys {
        NO_TOWN(ConfigKeys.LANG_NO_TOWN),
        NO_NATION(ConfigKeys.LANG_NO_NATION),
        NO_ALLIES(ConfigKeys.LANG_NO_ALLIES),
        TOWN_CHAT(ConfigKeys.LANG_TOWN_CHAT),
        NATION_CHAT(ConfigKeys.LANG_NATION_CHAT),
        ALLY_CHAT(ConfigKeys.LANG_ALLY_CHAT),
        GLOBAL_CHAT(ConfigKeys.LANG_GLOBAL_CHAT),
        ALREADY_GLOBAL_CHAT(ConfigKeys.LANG_ALREADY_GLOBAL);

        private final @NotNull ConfigKeys configKey;

        LangKeys(final @NotNull ConfigKeys configKey) {
            this.configKey = configKey;
        }

        @NotNull ConfigKeys getConfigKey() {
            return configKey;
        }
    }

}
