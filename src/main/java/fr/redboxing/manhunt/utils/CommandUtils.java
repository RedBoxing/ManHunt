package fr.redboxing.manhunt.utils;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandUtils {
    public static void registerCommand(String cmd, CommandExecutor executor, JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand(cmd);
        command.setExecutor(executor);

        if(CommodoreProvider.isSupported()) {
            Commodore commodore = CommodoreProvider.getCommodore(plugin);

            commodore.register(command, LiteralArgumentBuilder.literal("manhunt")
                    .then(LiteralArgumentBuilder.literal("hunter")
                            .then(LiteralArgumentBuilder.literal("add")
                                    .then(RequiredArgumentBuilder.argument("player", StringArgumentType.string())))
                            .then(LiteralArgumentBuilder.literal("remove")
                                    .then(RequiredArgumentBuilder.argument("player", StringArgumentType.string()))))
                    .then(LiteralArgumentBuilder.literal("speedrunner")
                            .then(LiteralArgumentBuilder.literal("add")
                                    .then(RequiredArgumentBuilder.argument("player", StringArgumentType.string())))
                            .then(LiteralArgumentBuilder.literal("remove")
                                    .then(RequiredArgumentBuilder.argument("player", StringArgumentType.string()))))
                    .then(LiteralArgumentBuilder.literal("start"))
                    .then(LiteralArgumentBuilder.literal("stop"))
            );
        }
    }
}
