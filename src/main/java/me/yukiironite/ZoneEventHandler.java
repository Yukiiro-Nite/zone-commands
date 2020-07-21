package me.yukiironite;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.ArrayList;

public class ZoneEventHandler {
  private List<String> commands;
  private TeleportCommand teleportCommand;
  public ZoneEventHandler(List<String> commands, TeleportCommand teleportCommand) {
    this.commands = commands;
    this.teleportCommand = teleportCommand;
  }

  public static ZoneEventHandler fromConfig(ConfigurationSection handlerConfig) {
    List<String> commands = null;
    TeleportCommand teleportCommand = null;

    if(handlerConfig != null) {
      if(handlerConfig.contains("command")) {
        if(handlerConfig.isString("command")) {
          commands = new ArrayList<String>();
          commands.add(handlerConfig.getString("command"));
        } else if(handlerConfig.isList("command")) {
          commands = handlerConfig.getStringList("command");
        }
      }

      if(handlerConfig.contains("tp")) {
        teleportCommand = TeleportCommand.fromConfig(handlerConfig.getConfigurationSection("tp"));
      }
    }

    return new ZoneEventHandler(commands, teleportCommand);
  }

  public void handle(Player player) {
    if(teleportCommand != null) {
      Vector motion = teleportCommand.motionFrom(player);
      Location destination = teleportCommand.locationFrom(player);

      player.setFallDistance(0);
      player.setVelocity(motion);
      player.teleport(destination, TeleportCause.PLUGIN);
    }

    if(commands != null) {
      for(String command : commands) {
        boolean result = Bukkit.dispatchCommand(
          Bukkit.getConsoleSender(),
          Solver.expand(command, player)
        );

        if(!result) {
          break;
        }
      }
    }
  }
}