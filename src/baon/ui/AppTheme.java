package baon.ui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppTheme {
    private static final Map<String, String> VALUES = loadTheme();

    private AppTheme() {
    }

    public static String text(String key, String fallback) {
        String value = VALUES.get(key);
        return value == null || value.isEmpty() ? fallback : value;
    }

    public static int integer(String key, int fallback) {
        try {
            return Integer.parseInt(text(key, String.valueOf(fallback)));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    public static Font font(String familyKey, String fallbackFamily, int style, int size) {
        return new Font(text(familyKey, fallbackFamily), style, size);
    }

    public static Color color(String key, String fallback) {
        return parseColor(text(key, fallback), fallback);
    }

    private static Map<String, String> loadTheme() {
        HashMap<String, String> values = new HashMap<String, String>();
        Path[] candidates = new Path[] { Paths.get("src", "baon", "ui", "theme.css"), Paths.get("src", "theme.css"), Paths.get("theme.css") };

        for (Path candidate : candidates) {
            if (!Files.exists(candidate)) {
                continue;
            }

            try {
                List<String> lines = Files.readAllLines(candidate, StandardCharsets.UTF_8);
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.startsWith("--")) {
                        continue;
                    }

                    int separatorIndex = trimmed.indexOf(':');
                    if (separatorIndex < 0) {
                        continue;
                    }

                    String key = trimmed.substring(0, separatorIndex).trim();
                    String value = trimmed.substring(separatorIndex + 1).trim();
                    if (value.endsWith(";")) {
                        value = value.substring(0, value.length() - 1).trim();
                    }
                    values.put(key, value);
                }
                break;
            } catch (IOException ignored) {
                // theme fallback logic
            }
        }

        return values;
    }

    private static Color parseColor(String value, String fallback) {
        try {
            String normalized = value.trim();
            if (normalized.startsWith("#")) {
                return Color.decode(normalized);
            }

            if (normalized.startsWith("rgb(") && normalized.endsWith(")")) {
                String[] parts = normalized.substring(4, normalized.length() - 1).split(",");
                return new Color(parseChannel(parts[0]), parseChannel(parts[1]), parseChannel(parts[2]));
            }

            if (normalized.startsWith("rgba(") && normalized.endsWith(")")) {
                String[] parts = normalized.substring(5, normalized.length() - 1).split(",");
                return new Color(parseChannel(parts[0]), parseChannel(parts[1]), parseChannel(parts[2]), parseAlpha(parts[3]));
            }
        } catch (RuntimeException ignored) {
            // color fallback logic
        }

        return parseFallbackColor(fallback);
    }

    private static int parseChannel(String value) {
        return Integer.parseInt(value.trim());
    }

    private static int parseAlpha(String value) {
        String trimmed = value.trim();
        if (trimmed.contains(".")) {
            return Math.max(0, Math.min(255, Math.round(Float.parseFloat(trimmed) * 255.0f)));
        }
        return Math.max(0, Math.min(255, Integer.parseInt(trimmed)));
    }

    private static Color parseFallbackColor(String fallback) {
        if (fallback.startsWith("#")) {
            return Color.decode(fallback);
        }
        if (fallback.startsWith("rgba(") || fallback.startsWith("rgb(")) {
            return parseColor(fallback, "#000000");
        }
        return Color.BLACK;
    }
}



