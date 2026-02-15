package au.com.addstar.ccm.stopinator;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * Intercepts stop/restart when run from the console so we always use our kick-then-shutdown
 * sequence even when the server's built-in command would otherwise run (e.g. Paper/Brigadier).
 */
public final class ConsoleStopListener implements Listener {

    private final Stopinator plugin;

    public ConsoleStopListener(Stopinator plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onServerCommand(ServerCommandEvent event) {
        String raw = event.getCommand().trim();
        if (raw.startsWith("/")) {
            raw = raw.substring(1).trim();
        }
        if (raw.isEmpty()) {
            return;
        }
        String first = raw.split("\\s+")[0].toLowerCase();
        boolean isRestart = first.equals("restart");
        boolean isStop = first.equals("stop") || first.equals("shutdown") || first.equals("mwstop");
        if (!isStop && !isRestart) {
            return;
        }
        event.setCancelled(true);
        plugin.initiateShutdown(isRestart);
    }
}
