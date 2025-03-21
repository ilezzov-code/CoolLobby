package ru.ilezzov.coollobby.models;

import lombok.Getter;
import ru.ilezzov.coollobby.Main;

import java.util.HashMap;

@Getter
public class DefaultPlaceholder {
    private final HashMap<String, String> placeholders;

    public DefaultPlaceholder() {
        this.placeholders = new HashMap<>();
        this.placeholders.put("{P}", Main.getPrefix());
    }

    public void addPlaceholder(final String placeholder, final String value) {
        this.placeholders.put(placeholder, value);
    }


}
