package fr.redboxing.manhunt;

import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
public class ManHuntListener implements Listener {
    private final ManHunt plugin;

    @EventHandler
    private void onDamagedByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(event.getFinalDamage() > player.getHealth()) {
            handlePlayerDeath(player, event.getDamager(), event);
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(event.getFinalDamage() > player.getHealth()) {
            handlePlayerDeath(player, null, event);
        }
    }

    private void handlePlayerDeath(Player player, Entity killer, Cancellable event) {
        if(plugin.isSpeedRunner(player)) {
            event.setCancelled(true);

            if(plugin.getSpeedrunners().size() == 1) {
                for(UUID uuid : plugin.getHunters()) {
                    Player player1 = Bukkit.getPlayer(uuid);
                    if(player1 == null) continue;

                    player1.setGameMode(GameMode.SPECTATOR);
                    player1.sendTitle(ChatColor.GREEN + "VICTORY", ChatColor.GREEN + player.getName() + " have died");
                }

                for(SpeedRunner speedRunner : plugin.getSpeedrunners()) {
                    speedRunner.getPlayer().setGameMode(GameMode.SPECTATOR);
                    speedRunner.getPlayer().sendTitle(ChatColor.RED + "LOSE", ChatColor.RED + "You have died");
                }

                Arrays.stream(player.getInventory().getContents()).spliterator().forEachRemaining(itemStack -> {
                    player.getWorld().dropItem(player.getLocation(), itemStack);
                });

                plugin.getCompassTask().cancel();
                Bukkit.broadcastMessage("[" + ChatColor.AQUA + "ManHunt" + ChatColor.WHITE + "] " + ChatColor.GOLD + "ManHunt Stopped !");
            } else if(plugin.getSpeedrunners().size() > 1) {
                for(Player player1 : Bukkit.getOnlinePlayers()) {
                    player1.sendTitle(ChatColor.RED + player.getName() + " DIED", killer != null ? ChatColor.RED + "Killed by " + killer.getName() : "");
                }

                player.setGameMode(GameMode.SPECTATOR);
                plugin.getSpeedrunners().remove(plugin.getSpeedRunner(player));

                Arrays.stream(player.getInventory().getContents()).spliterator().forEachRemaining(itemStack -> {
                    player.getWorld().dropItem(player.getLocation(), itemStack);
                });
            }
        }
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if(plugin.getHunters().contains(player.getUniqueId())) {
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }

    @EventHandler
    private void onPlayerEnterPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        if(plugin.isSpeedRunner(player) && plugin.getSpeedrunners().size() > 0) {
            SpeedRunner speedRunner = plugin.getSpeedRunner(player);
            if( speedRunner== null) return;
            speedRunner.setLastPortalUsed(event.getFrom());
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(plugin.isSpeedRunner(player)) {

        } else if(plugin.isHunter(player)) {

        }
    }
}
