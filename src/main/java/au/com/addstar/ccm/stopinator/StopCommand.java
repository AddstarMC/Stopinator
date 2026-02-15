package au.com.addstar.ccm.stopinator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Handles /stop and /restart (and aliases): delegates to the plugin's kick-then-shutdown sequence.
 */
public final class StopCommand implements CommandExecutor {

    private final Stopinator plugin;

    public StopCommand(Stopinator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args) {
        boolean isRestart = label.equalsIgnoreCase("restart");
        if (!label.equalsIgnoreCase("stop") && !isRestart) {
            return false;
        }
        plugin.initiateShutdown(isRestart);
        return true;
    }
}
