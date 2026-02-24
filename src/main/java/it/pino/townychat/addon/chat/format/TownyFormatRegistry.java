package it.pino.townychat.addon.chat.format;

import it.pino.townychat.addon.PluginLifeCycle;
import it.pino.townychat.addon.chat.channel.TownyChannelType;
import it.pino.townychat.addon.config.ConfigKeys;
import it.pino.townychat.addon.config.ConfigurationManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TownyFormatRegistry implements PluginLifeCycle {

    private final @NotNull Map<TownyChannelType, TownyChatFormat> townyChatFormatMap = new ConcurrentHashMap<>(3);

    private final @NotNull ConfigurationManager configurationManager;

    public TownyFormatRegistry(final @NotNull ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @Override
    public void init() {
        registerFormats();
    }

    @Override
    public void reload() {
        townyChatFormatMap.clear();
        registerFormats();
    }

    private void registerFormats(){
        for (final var formatKey : FormatKey.values()) {
            registerFormat(formatKey);
        }
    }

    private void registerFormat(final @NotNull FormatKey formatKey) {
        final var formatString = configurationManager.get(formatKey.getFormatKey());
        final var hoverString = configurationManager.get(formatKey.getHoverKey());
        final var actionString = configurationManager.get(formatKey.getActionKey());

        final var townyFormatComponent = new TownyChatFormat.Component(formatString, hoverString, actionString);
        final var townyChatFormat = new TownyChatFormat(formatKey.name(), Collections.singletonList(townyFormatComponent));

        switch (formatKey) {
            case TOWN -> townyChatFormatMap.put(TownyChannelType.TOWN, townyChatFormat);
            case ALLY -> townyChatFormatMap.put(TownyChannelType.ALLY, townyChatFormat);
            case NATION -> townyChatFormatMap.put(TownyChannelType.NATION, townyChatFormat);
        }

    }

    public @NotNull TownyChatFormat getTownyChatFormat(@NotNull TownyChannelType townyChannelType) {
        return townyChatFormatMap.get(townyChannelType);
    }


    private enum FormatKey {
        TOWN(ConfigKeys.TOWN_CHAT_FORMAT, ConfigKeys.TOWN_CHAT_HOVER, ConfigKeys.TOWN_CHAT_ACTION),
        ALLY(ConfigKeys.ALLY_CHAT_FORMAT, ConfigKeys.ALLY_CHAT_HOVER, ConfigKeys.ALLY_CHAT_ACTION),
        NATION(ConfigKeys.NATION_CHAT_FORMAT, ConfigKeys.NATION_CHAT_HOVER, ConfigKeys.NATION_CHAT_ACTION);

        private final @NotNull ConfigKeys formatKey;
        private final @NotNull ConfigKeys hoverKey;
        private final @NotNull ConfigKeys actionKey;

        FormatKey(final @NotNull ConfigKeys formatKey,
                  final @NotNull ConfigKeys hoverKey,
                  final @NotNull ConfigKeys actionKey) {
            this.formatKey = formatKey;
            this.hoverKey = hoverKey;
            this.actionKey = actionKey;
        }

        public @NotNull ConfigKeys getFormatKey() {
            return formatKey;
        }

        public @NotNull ConfigKeys getHoverKey() {
            return hoverKey;
        }

        public @NotNull ConfigKeys getActionKey() {
            return actionKey;
        }
    }
}
