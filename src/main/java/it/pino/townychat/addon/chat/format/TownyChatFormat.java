package it.pino.townychat.addon.chat.format;

import it.pino.zelchat.api.message.format.Format;
import it.pino.zelchat.api.message.format.FormatComponent;
import it.pino.zelchat.api.message.format.type.FormatType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class TownyChatFormat implements Format {

    private static final @NotNull FormatType FORMAT_TYPE = FormatType.CUSTOM;

    private final @NotNull String name;
    private final @NotNull List<FormatComponent> components;

    public TownyChatFormat(
            @NotNull String name,
            @NotNull List<FormatComponent> components
    ) {
        this.name = name;
        this.components = components;
    }


    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull FormatType getType() {
        return FORMAT_TYPE;
    }

    @Override
    public @NotNull @UnmodifiableView Collection<FormatComponent> getComponents() {
        return Collections.unmodifiableCollection(this.components);
    }

    @Override
    public @Nullable Integer getWeight() {
        return null;
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    public static final class Component implements FormatComponent {

        private final @NotNull String format;
        private final @NotNull String hover;
        private final @NotNull String action;

        public Component(
                @NotNull String format,
                @NotNull String hover,
                @NotNull String action
        ) {
            this.format = format;
            this.hover = hover;
            this.action = action;
        }


        @Override
        public @NotNull String getName() {
            return "";
        }

        @Override
        public @NotNull String getFormat() {
            return this.format;
        }

        @Override
        public @NotNull String getHoverMessage() {
            return this.hover;
        }

        @Override
        public @NotNull String getAction() {
            return this.action;
        }
    }
}
