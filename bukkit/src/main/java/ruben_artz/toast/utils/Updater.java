package ruben_artz.toast.utils;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ruben_artz.toast.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updater {
    private static final Main plugin = Main.getPlugin(Main.class);

    private static final String UPDATE_URL = "https://stn-studios.dev/versions/toast.txt";
    private static final int TIMEOUT = 1250;

    private static BukkitTask updater;

    public static void launch() {
        if (updater != null) {
            updater.cancel();
        }

        updater = new BukkitRunnable() {
            @Override
            public void run() {
                Updater.getUpdater();
            }
        }.runTaskTimer(plugin, 0L, 5 * 60 * 60 * 20L);
    }

    public static void shutdown() {
        if (updater != null) {
            updater.cancel();
        }

        updater = null;
    }

    public static void getUpdater() {
        try {
            String latestVersion = fetchLatestVersion();
            if (latestVersion == null) {
                return;
            }

            String currentVersion = plugin.getDescription().getVersion();

            if (!currentVersion.equals(latestVersion)) {
                notifyUpdateAvailable(latestVersion);
            }
        } catch (Exception ignored) {
        }
    }

    public static String fetchLatestVersion() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(UPDATE_URL).openConnection();
        connection.setConnectTimeout(TIMEOUT);
        connection.setReadTimeout(TIMEOUT);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.readLine();
        } finally {
            connection.disconnect();
        }
    }

    private static void notifyUpdateAvailable(String latestVersion) {
        Main.sendMessage("&8--------------------------------------------------------------------------------------");
        Main.sendMessage("&fYou are currently using an &eoutdated version &fof the &eToastAPI &fplugin.");
        Main.sendMessage("&fPlease download the latest version (&e" + latestVersion + "&f) from:");
        Main.sendMessage("&9https://stn-studios.dev/toast-api");
        Main.sendMessage("&fTo update, remove the old jar file and restart your server.");
        Main.sendMessage("&8--------------------------------------------------------------------------------------");

    }
}