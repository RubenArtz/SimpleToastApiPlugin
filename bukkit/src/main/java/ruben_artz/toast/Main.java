package ruben_artz.toast;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ruben_artz.toast.enviroment.Environment;
import ruben_artz.toast.events.playerJoin;
import ruben_artz.toast.utils.Updater;

@Getter
public class Main extends JavaPlugin {
    public static final int MINECRAFT_1_16_PROTOCOL_VERSION = 16;

    @Getter
    public static TaskScheduler scheduler;
    @Getter
    @Setter
    public static Environment environment;
    static String prefix = "&8[&9Toast API&8]&f ";
    public String latestversion;
    String version = getDescription().getVersion();

    public static void sendMessage(String text) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + text));
    }

    public static boolean isVersionAtLeast_1_16() {
        return getEnvironment().getMinecraftVersion() > MINECRAFT_1_16_PROTOCOL_VERSION;
    }

    @Override
    public void onEnable() {
        environment = new Environment();

        scheduler = UniversalScheduler.getScheduler(this);

        if (!isVersionAtLeast_1_16()) {
            sendMessage("&cThis plug-in only works in versions 1.16.5-1.21.");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        sendMessage("&6==================================================");
        sendMessage(" ");
        sendMessage("&6API created by Wuason6x9 and maintained by RubenArtz");
        sendMessage("&6you can find the source code here: https://github.com/Wuason6x9/SimpleToastApi");
        sendMessage(" ");
        sendMessage("&6==================================================");

        PluginManager event = getServer().getPluginManager();

        event.registerEvents(new playerJoin(), this);

        Updater.launch();
    }

    @Override
    public void onDisable() {
        Updater.shutdown();
    }
}