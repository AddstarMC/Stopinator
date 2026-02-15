# Stopinator

Paper plugin that replaces the default `/stop` and `/restart` behavior with a controlled shutdown: kick all players (with a configurable tick delay between each), then shut down or restart the server.

## What it does

- **/stop** and **/restart** (and aliases **/shutdown**, **/mwstop**) kick every online player with *"Server is restarting"*, then stop or restart the server.
- Kicks are spread over time (one per tick by default; adjust `TICKS_BETWEEN_KICKS` in code to spread them more).
- Shutdown or restart runs 5 ticks after the last kick.
- New logins are blocked once a stop/restart has been started.
- Restart uses the **spigot.yml** restart script; stop does a normal shutdown.

## Requirements

- Paper 1.20+
- Permission **`stopinator.use`** to use the commands (default: op).

## Commands

| Command        | Description |
|----------------|-------------|
| `/stop`        | Kick all, then stop server |
| `/restart`     | Kick all, then restart (runs restart script) |

Console use of stop/restart is intercepted so this behavior is used even when the server would normally run its built-in command.
