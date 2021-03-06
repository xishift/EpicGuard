package me.xneox.epicguard.core.command;

import me.xneox.epicguard.core.command.sub.*;
import me.xneox.epicguard.core.EpicGuard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * This class holds all registered subcommands, and handles the user command/tab suggestion input.
 */
public class CommandExecutor {
    private final Map<String, SubCommand> commandMap = new HashMap<>();

    private final EpicGuard epicGuard;

    public CommandExecutor(EpicGuard epicGuard) {
        this.epicGuard = epicGuard;

        this.commandMap.put("analyze", new AnalyzeCommand());
        this.commandMap.put("blacklist", new BlacklistCommand());
        this.commandMap.put("help", new HelpCommand());
        this.commandMap.put("reload", new ReloadCommand());
        this.commandMap.put("status", new StatusCommand());
        this.commandMap.put("whitelist", new WhitelistCommand());
    }

    public void handle(@Nonnull String[] args, @Nonnull Sender<?> sender) {
        String prefix = this.epicGuard.messages().command().prefix();
        if (args.length < 1) {
            sender.sendMessage(prefix + "&7You are running &6EpicGuard v" + this.epicGuard.platform().version()
                    + "&7. Run &c/guard help &7to see available commands and statistics.");
            return;
        }

        SubCommand command = this.commandMap.get(args[0]);
        if (command == null) {
            sender.sendMessage(prefix + this.epicGuard.messages().command().unknownCommand());
            return;
        }

        command.execute(sender, args, this.epicGuard);
    }

    @Nullable
    public Collection<String> onTabComplete(@Nonnull String[] args) {
        // If no argument is specified, send all available subcommands.
        if (args.length == 1) {
            return this.commandMap.keySet();
        }

        SubCommand command = this.commandMap.get(args[0]);
        if (command != null) {
            return command.suggest(args, this.epicGuard);
        }
        return null;
    }
}
