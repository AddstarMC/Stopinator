package au.com.addstar.ccm.stopinator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/** Blocks new logins once a stop or restart has been initiated. */
public final class ShutdownBlockListener implements Listener {

    private static final Component KICK_MESSAGE =
            MiniMessage.miniMessage().deserialize("<red>Server is shutting down</red>");

    private final Stopinator plugin;

    public ShutdownBlockListener(Stopinator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (plugin.isShutdownInitiated()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, KICK_MESSAGE);
        }
    }
}
