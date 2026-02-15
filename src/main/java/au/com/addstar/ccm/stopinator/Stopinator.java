package au.com.addstar.ccm.stopinator;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Stopinator extends JavaPlugin {

    private static final Component KICK_MESSAGE =
            MiniMessage.miniMessage().deserialize("<red>Server is restarting</red>");

    /** Ticks to wait between kicking each player (1 = one kick per tick). Increase to spread kicks. */
    private static final int TICKS_BETWEEN_KICKS = 1;

    /** Ticks to wait after the last kick before calling shutdown/restart. */
    private static final int TICKS_AFTER_LAST_KICK_BEFORE_SHUTDOWN = 5;

    /** True once /stop or /restart has been run; used to block new logins until the server exits. */
    private volatile boolean shutdownInitiated;

    public boolean isShutdownInitiated() {
        return shutdownInitiated;
    }

    public void setShutdownInitiated(boolean shutdownInitiated) {
        this.shutdownInitiated = shutdownInitiated;
    }

    /**
     * Runs the kick-then-shutdown/restart sequence. Safe to call from command executor or
     * ServerCommandEvent; no-op if shutdown was already initiated.
     */
    public void initiateShutdown(boolean isRestart) {
        if (shutdownInitiated) {
            return;
        }
        shutdownInitiated = true;

        getLogger().info(isRestart ? "Restart initiated (Stopinator)." : "Stop initiated (Stopinator).");

        Runnable afterKicks = isRestart
                ? () -> getServer().spigot().restart()
                : () -> getServer().shutdown();

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        if (players.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(this, afterKicks, TICKS_AFTER_LAST_KICK_BEFORE_SHUTDOWN);
            return;
        }

        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            Bukkit.getScheduler().runTaskLater(this, () -> player.kick(KICK_MESSAGE), (long) i * TICKS_BETWEEN_KICKS);
        }
        Bukkit.getScheduler().runTaskLater(
                this,
                afterKicks,
                (long) players.size() * TICKS_BETWEEN_KICKS + TICKS_AFTER_LAST_KICK_BEFORE_SHUTDOWN);
    }

    @Override
    public void onEnable() {
        StopCommand stopCommand = new StopCommand(this);
        registerCommand("stop", stopCommand);
        registerCommand("restart", stopCommand);
        getServer().getPluginManager().registerEvents(new ShutdownBlockListener(this), this);
        getServer().getPluginManager().registerEvents(new ConsoleStopListener(this), this);
    }

    private void registerCommand(String name, StopCommand executor) {
        PluginCommand cmd = getCommand(name);
        if (cmd == null) {
            getLogger().warning("Command '" + name + "' not in plugin.yml or already taken; console stop/restart will still be intercepted.");
            return;
        }
        cmd.setExecutor(executor);
    }

    @Override
    public void onDisable() {
    }
}
