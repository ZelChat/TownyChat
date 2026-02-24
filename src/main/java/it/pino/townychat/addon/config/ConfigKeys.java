package it.pino.townychat.addon.config;

import org.jetbrains.annotations.NotNull;

public enum ConfigKeys {

    LANG_NO_TOWN("messages.no-town", "<red>You are not in a town!"),
    LANG_NO_NATION("messages.no-nation", "<red>You are not in a nation!"),
    LANG_NO_ALLIES("messages.no-allies", "<red>Your town has no allies!"),
    LANG_TOWN_CHAT("messages.town-chat-enable", "<green>You will now chat in your town"),
    LANG_NATION_CHAT("messages.nation-chat-enable", "<green>You will now chat in your nation"),
    LANG_ALLY_CHAT("messages.ally-chat-enable", "<green>You will now chat with your allies"),
    LANG_GLOBAL_CHAT("messages.global-chat-enable", "<green>You will now chat in global chat"),
    LANG_ALREADY_GLOBAL("messages.already-global-chat", "<red>You are already in global chat!"),

    TOWN_CHAT_FORMAT("formats.town-chat.format", "&8[&f%townyadvanced_town%&8] &f%player_name &8» &7{message}"),
    TOWN_CHAT_HOVER("formats.town-chat.hover", ""),
    TOWN_CHAT_ACTION("formats.town-chat.action", "none"),

    ALLY_CHAT_FORMAT("formats.ally-chat.format", "&8[&a%townyadvanced_town%&8] &f%player_name &8» &7{message}"),
    ALLY_CHAT_HOVER("formats.ally-chat.hover", ""),
    ALLY_CHAT_ACTION("formats.ally-chat.action", "none"),

    NATION_CHAT_FORMAT("formats.nation-chat.format", "&8[&f%townyadvanced_nation%&r&8] &f%player_name &8» &7{message}"),
    NATION_CHAT_HOVER("formats.nation-chat.hover", ""),
    NATION_CHAT_ACTION("formats.nation-chat.action", "none");

    private final String path;
    private final String defaultValue;

    ConfigKeys(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    @NotNull
    public String getPath() {
        return path;
    }

    @NotNull
    public String getDefaultValue() {
        return defaultValue;
    }

}
