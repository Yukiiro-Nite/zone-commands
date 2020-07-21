package me.yukiironite;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Zone {
  private String name;
  private String worldName;

  private ZoneEventHandler enterHandler;
  private ZoneEventHandler exitHandler;

  public Zone(String name, String worldName, ZoneEventHandler enterHandler, ZoneEventHandler exitHandler) {
    setName(name);
    setWorldName(worldName);
    setEnterHandler(enterHandler);
    setExitHandler(exitHandler);
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWorldName() {
    return this.worldName;
  }

  public void setWorldName(String worldName) {
    this.worldName = worldName;
  }

  public ZoneEventHandler getEnterHandler() {
    return this.enterHandler;
  }

  public void setEnterHandler(ZoneEventHandler enterHandler) {
    this.enterHandler = enterHandler;
  }

  public ZoneEventHandler getExitHandler() {
    return this.exitHandler;
  }

  public void setExitHandler(ZoneEventHandler exitHandler) {
    this.exitHandler = exitHandler;
  }

  public boolean isInZone(Player player) {
    Location location = player.getLocation();
    String world = location.getWorld().getName();

    return this.worldName.equals(world);
  }

  public void onEnter(Player player) {
    if(enterHandler != null) {
      enterHandler.handle(player);
    }
  }

  public void onExit(Player player) {
    if(exitHandler != null) {
      exitHandler.handle(player);
    }
  }
}