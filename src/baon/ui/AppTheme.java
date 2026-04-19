package baon.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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

    public static float decimal(String key, float fallback) {
        try {
            return Float.parseFloat(text(key, String.valueOf(fallback)));
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    public static Font font(String familyKey, String fallbackFamily, int style, int size) {
        return new Font(text(familyKey, fallbackFamily), style, size);
    }

    public static float fontScale() {
        return Math.max(1.0f, decimal("--ui-font-scale", 1.0f));
    }

    public static int scaled(int size) {
        return Math.max(1, Math.round(size * fontScale()));
    }

    public static Font scaledFont(Font font) {
        if (font == null) {
            return null;
        }
        return font.deriveFont(Math.max(1.0f, font.getSize2D() * fontScale()));
    }

    public static Dimension scaledDimension(Dimension dimension) {
        if (dimension == null) {
            return null;
        }
        return new Dimension(scaleDimensionValue(dimension.width), scaleDimensionValue(dimension.height));
    }

    public static void scaleComponentTreeFonts(Component component) {
        if (component == null) {
            return;
        }

        javax.swing.JComponent swingComponent = component instanceof javax.swing.JComponent
                ? (javax.swing.JComponent) component
                : null;

        Font currentFont = component.getFont();
        if (currentFont != null) {
            Object scaledMarker = swingComponent == null ? null : swingComponent.getClientProperty("baon.fontScaled");
            if (!Boolean.TRUE.equals(scaledMarker)) {
                component.setFont(scaledFont(currentFont));
                if (swingComponent != null) {
                    swingComponent.putClientProperty("baon.fontScaled", Boolean.TRUE);
                }
            }
        }

        Object dimensionMarker = swingComponent == null ? null : swingComponent.getClientProperty("baon.dimensionScaled");
        if (!Boolean.TRUE.equals(dimensionMarker)) {
            if (component.isPreferredSizeSet()) {
                component.setPreferredSize(scaledDimension(component.getPreferredSize()));
            }
            if (component.isMinimumSizeSet()) {
                component.setMinimumSize(scaledDimension(component.getMinimumSize()));
            }
            if (component.isMaximumSizeSet()) {
                component.setMaximumSize(scaledDimension(component.getMaximumSize()));
            }
            if (swingComponent != null) {
                swingComponent.putClientProperty("baon.dimensionScaled", Boolean.TRUE);
            }
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                scaleComponentTreeFonts(child);
            }
        }
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

    private static int scaleDimensionValue(int value) {
        if (value <= 0 || value == Integer.MAX_VALUE) {
            return value;
        }
        return Math.max(1, Math.round(value * fontScale()));
    }
}



