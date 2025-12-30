package net.sievert.melee_stop_fix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MeleeStopFixConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE = "melee_stop_fix.json";

    public static MeleeStopFixConfig INSTANCE = new MeleeStopFixConfig();

    public boolean disableCreeperStopDuringIgnition = true;

    public double creeperIgnitedChaseSpeed = 1.0;

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(FILE);

        if (!Files.exists(path)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            MeleeStopFixConfig loaded = GSON.fromJson(reader, MeleeStopFixConfig.class);
            if (loaded != null) {
                INSTANCE = loaded;
            }
        } catch (Exception e) {
            INSTANCE = new MeleeStopFixConfig();
            save();
        }
    }

    public static void save() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(FILE);
        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(INSTANCE, writer);
        } catch (Exception ignored) {}
    }

    private MeleeStopFixConfig() {}
}
