package fr.redboxing.manhunt;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import fr.redboxing.manhunt.commands.ManHuntCommand;
import fr.redboxing.manhunt.utils.CommandUtils;
import lombok.Getter;
import lombok.Setter;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class ManHunt extends JavaPlugin {
    @Getter
    private List<UUID> hunters = new ArrayList<>();

    @Getter
    private List<SpeedRunner> speedrunners = new ArrayList<>();

    @Getter
    @Setter
    private BukkitTask compassTask;

    private int i = 0;
    private int index = 0;

    @Override
    public void onEnable() {
        getLogger().info("Loading ManHunt Plugin");
        getServer().getPluginManager().registerEvents(new ManHuntListener(this), this);

        CommandUtils.registerCommand("manhunt", new ManHuntCommand(this), this);
    }

    public void addHunter(Player player) {
        this.hunters.add(player.getUniqueId());
    }

    public void removeHunter(Player player) {
        this.hunters.remove(player.getUniqueId());
    }

    public void addSpeedRunner(Player player) {
        this.speedrunners.add(new SpeedRunner(player));
    }

    public void removeSpeedRunner(Player player) {
        this.speedrunners.remove(new SpeedRunner(player));
    }

    public boolean isHunter(Player player) {
        for(UUID uui : this.hunters) {
            if(uui == player.getUniqueId())
                return true;
        }

        return false;
    }

    public boolean isSpeedRunner(Player player) {
        for(SpeedRunner speedRunner : speedrunners) {
            if(speedRunner.getPlayer() == player)
                return true;
        }

        return false;
    }

    public SpeedRunner getSpeedRunner(Player player) {
        for(SpeedRunner speedRunner : speedrunners) {
            if(speedRunner.getPlayer() == player)
                return speedRunner;
        }

        return null;
    }

    public SpeedRunner getRandomSpeedRunner() {
        SpeedRunner speedRunner = this.speedrunners.get(this.index);

        if(this.speedrunners.size() > 1) {
            i++;
            if(i == 100) {
                ++this.index;
                this.i = 0;
            }

            if (this.index > this.speedrunners.size())
                this.index = 0;
        }

        return speedRunner;
    }
}
