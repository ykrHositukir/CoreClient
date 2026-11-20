package dev.coreclient.coreessentials;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Simple JSON config persisted under {@code config/coreessentials.json}.
 */
public final class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("coreessentials.json");

    public boolean hudEnabled = true;
    public boolean keystrokesEnabled = true;
    public int hudX = 6;
    public int hudY = 6;

    public static Config load() {
        try {
            if (Files.isRegularFile(FILE)) {
                Config cfg = GSON.fromJson(Files.readString(FILE), Config.class);
                if (cfg != null) {
                    return cfg;
                }
            }
        } catch (IOException | RuntimeException e) {
            // fall through to defaults; a broken config is simply replaced
        }
        Config cfg = new Config();
        save(cfg);
        return cfg;
    }

    public static void save(Config cfg) {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(cfg));
        } catch (IOException e) {
            // config persistence is best-effort
        }
    }
}
