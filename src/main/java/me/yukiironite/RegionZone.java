package me.yukiironite;

import org.bukkit.entity.Player;
import org.bukkit.Location;

public class RegionZone extends Zone {
  private double startX;
  private double startY;
  private double startZ;
  private double endX;
  private double endY;
  private double endZ;

  public RegionZone (String name, String worldName, ZoneEventHandler enterHandler, ZoneEventHandler exitHandler, double startX, double startY, double startZ, double endX, double endY, double endZ) {
    super(name, worldName, enterHandler, exitHandler);
    this.startX = Math.min(startX, endX);
    this.startY = Math.min(startY, endY);
    this.startZ = Math.min(startZ, endZ);
    this.endX = Math.max(startX, endX);
    this.endY = Math.max(startY, endY);
    this.endZ = Math.max(startZ, endZ);
  }

  public boolean isInZone(Player player) {
    Location location = player.getLocation();
    double x = location.getX();
    double y = location.getY();
    double z = location.getZ();

    return super.isInZone(player) &&
      this.startX < x && this.endX > x &&
      this.startY < y && this.endY > y &&
      this.startZ < z && this.endZ > z;
  }
}