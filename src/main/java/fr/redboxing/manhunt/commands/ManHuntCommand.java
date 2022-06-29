package fr.redboxing.manhunt.commands;

import fr.redboxing.manhunt.CompassTask;
import fr.redboxing.manhunt.ManHunt;
import fr.redboxing.manhunt.SpeedRunner;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ManHuntCommand implements CommandExecutor, TabCompleter {
    private final ManHunt plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args[0]) {
            case "start":
                if(plugin.getCompassTask() != null && !plugin.getCompassTask().isCancelled()) {
                    sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.RED + "Game is already started !");
                    return true;
                }

                if(plugin.getSpeedrunners().size() == 0) {
                    sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.RED + "Game can not start without a speedrunner online !");
                    return true;
                }

                for(UUID uuid : plugin.getHunters()) {
                    Player player =  Bukkit.getPlayer(uuid);
                    if(player == null) continue;
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.setExp(0);
                    player.setLevel(0);

                    player.getInventory().clear();
                    player.getInventory().addItem(new ItemStack(Material.COMPASS));
                }

                for(SpeedRunner speedRunner : plugin.getSpeedrunners()) {
                    Player player =  speedRunner.getPlayer();
                    if(player == null) continue;
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.setExp(0);
                    player.setLevel(0);

                    player.getInventory().clear();
                }

                plugin.setCompassTask(new CompassTask(plugin).runTaskTimer(plugin, 10, 10));
                Bukkit.broadcastMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.GOLD + "ManHunt Started !");
                break;
            case "stop":
                if(plugin.getCompassTask() == null || plugin.getCompassTask() != null && plugin.getCompassTask().isCancelled()) {
                    sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.RED + "Game is has not yet started !");
                    return true;
                }

                plugin.getCompassTask().cancel();
                Bukkit.broadcastMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.GOLD + "ManHunt Stopped !");
                break;
            case "hunter":
                if(args[1].equals("add")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    if(player == null) {
                        sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.RED + "Player not found !");
                        return true;
                    }

                    if (plugin.getHunters().contains(player.getUniqueId())) {
                        sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.RED + player.getName() + " is already a hunter !");
                        return true;
                    }

                    plugin.addHunter(player);
                    sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.GREEN + player.getName() + " added to hunters");
                } else if(args[1].equals("remove")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    if(player == null) {
                        sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.RED + "Player not found !");
                        return true;
                    }

                    plugin.removeHunter(player);
                    sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.GREEN + player.getName() + " remove from hunters");
                }
                break;
            case "speedrunner":
                if(args[1].equals("add")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    if(player == null) {
                        sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.RED + "Player not found !");
                        return true;
                    }

                    if (plugin.getSpeedrunners().contains(player.getUniqueId())) {
                        sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.RED + player.getName() + " is already a speedrunner !");
                        return true;
                    }

                    plugin.addSpeedRunner(player);
                    sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.GREEN + player.getName() + " added to speedrunners");
                } else if(args[1].equals("remove")) {
                    Player player = Bukkit.getPlayer(args[2]);
                    if(player == null) {
                        sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.RED + "Player not found !");
                        return true;
                    }

                    plugin.removeSpeedRunner(player);
                    sender.sendMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.GREEN + player.getName() + " remove from speedrunner");
                }
                break;
        }

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
