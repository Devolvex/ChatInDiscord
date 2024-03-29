package dev.jqstln.chatindiscord.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class ChatInDiscordCommand implements CommandExecutor, TabCompleter {
    private final Plugin plugin;

    public ChatInDiscordCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return handleNoArgs(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            return handleReload(sender);
        } else {
            return handleInvalidUsage(sender);
        }
    }

    private boolean handleNoArgs(CommandSender sender) {
        String prefix = showPrefix(plugin.getConfig().getString("Prefix"));
        String reload = plugin.getConfig().getString("Messages.ReloadCommand");
        String nopermission = plugin.getConfig().getString("Messages.NoPermission");
        String invalidusage = plugin.getConfig().getString("Messages.InvalidUsage");

        if (sender.hasPermission("chatindiscord.use")) {
            Plugin chatInDiscordPlugin = Bukkit.getPluginManager().getPlugin("ChatInDiscord");
            String version = chatInDiscordPlugin.getDescription().getVersion();

            sender.sendMessage(prefix + "ChatInDiscord Help:");
            sender.sendMessage(prefix + ChatColor.AQUA + "/chatindiscord reload " + ChatColor.WHITE + "- Reloads the ChatInDiscord plugin.");
            sender.sendMessage(prefix);
            sender.sendMessage(prefix + ChatColor.GREEN + "Version: " + ChatColor.YELLOW + version);
        } else {
            sender.sendMessage(prefix + ChatColor.RED + nopermission);
        }
        return true;
    }

    private boolean handleReload(CommandSender sender) {
        String prefix = showPrefix(plugin.getConfig().getString("Prefix"));
        String reload = plugin.getConfig().getString("Messages.ReloadCommand");
        String nopermission = plugin.getConfig().getString("Messages.NoPermission");

        if (sender.hasPermission("chatindiscord.reload")) {
            reloadConfiguration();
            sender.sendMessage(prefix + ChatColor.GREEN + reload);
        } else {
            sender.sendMessage(prefix + ChatColor.RED + nopermission);
        }
        return true;
    }

    private boolean handleInvalidUsage(CommandSender sender) {
        String prefix = showPrefix(plugin.getConfig().getString("Prefix"));
        String invalidusage = plugin.getConfig().getString("Messages.InvalidUsage");

        sender.sendMessage(prefix + ChatColor.RED + invalidusage);
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("reload");
        } else {
            return Collections.emptyList();
        }
    }

    private void reloadConfiguration() {
        plugin.reloadConfig();
    }

    private String showPrefix(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}