package ruben_artz.toast.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ruben_artz.toast.Main;
import ruben_artz.toast.utils.Updater;

public class playerJoin implements Listener {
    private static final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.isOp() && !player.hasPermission("*")) {
            return;
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> checkForUpdates(player), 100L);
    }

    private void checkForUpdates(Player player) {
        try {
            String latestVersion = Updater.fetchLatestVersion();
            if (latestVersion == null) {
                return;
            }

            String currentVersion = plugin.getDescription().getVersion();

            if (!currentVersion.equals(latestVersion)) {
                notifyUpdateAvailable(latestVersion, player);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to check for updates: " + e.getMessage());
        }
    }

    private void notifyUpdateAvailable(String latestVersion, Player player) {
        String message = ChatColor.translateAlternateColorCodes('&',
                "&a&l[" + plugin.getDescription().getName() + "] &aThere is a newer plugin version available: &a&l"
                        + latestVersion + "&a, you're on: &a&l" + plugin.getDescription().getVersion()
                        + "\n&aDownload: https://stn-studios.dev/toast-api");

        player.sendMessage(message);
    }
}