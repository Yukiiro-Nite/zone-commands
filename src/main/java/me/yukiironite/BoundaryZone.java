package me.yukiironite;

import org.bukkit.entity.Player;

public class BoundaryZone extends Zone {
  private String condition;

  public BoundaryZone(String zoneName, String worldName, ZoneEventHandler enterHandler, ZoneEventHandler exitHandler, String condition) {
    super(zoneName, worldName, enterHandler, exitHandler);

    this.condition = condition;
  }

  public boolean isInZone(Player player) {
    return super.isInZone(player) && Solver.expandAndSolveBool(condition, player);
  }
}