package dev.felnull.somsupporter.feature.party;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class PartyConfigManager {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static PartyConfig instance;

    private static Path getConfigPath() {
        File gameDir = Minecraft.getInstance().gameDirectory;
        return gameDir.toPath()
                .resolve("config")
                .resolve("party_helper")
                .resolve("config.json");
    }

    public static PartyConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static void save() {
        save(get());
    }

    public static void save(PartyConfig cfg) {
        Path path = getConfigPath();
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING))
            {
                GSON.toJson(cfg, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static PartyConfig load() {
        Path path = getConfigPath();

        try {
            if (!Files.exists(path)) {
                // 初回 → 空ファイル作成
                PartyConfig cfg = new PartyConfig();
                save(cfg);
                return cfg;
            }

            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                return GSON.fromJson(reader, PartyConfig.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new PartyConfig();
        }
    }
}
