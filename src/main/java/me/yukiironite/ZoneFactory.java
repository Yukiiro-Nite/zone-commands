package me.yukiironite;

import org.bukkit.configuration.ConfigurationSection;

public class ZoneFactory {
  public static Zone fromConfig(String zoneName, ConfigurationSection zoneConfig) {
    String type = zoneConfig.getString("type").toLowerCase();
    
    switch(type) {
      case "zone": return regionZoneFromConfig(zoneName, zoneConfig);
      case "boundry": return boundryZoneFromConfig(zoneName, zoneConfig);
      default: return null;
    }
  }

  public static RegionZone regionZoneFromConfig(String zoneName, ConfigurationSection zoneConfig) {
    String zoneWorld = zoneConfig.getString("world");
    double startX = zoneConfig.getDouble("from.x");
    double startY = zoneConfig.getDouble("from.y");
    double startZ = zoneConfig.getDouble("from.z");
    double endX = zoneConfig.getDouble("to.x");
    double endY = zoneConfig.getDouble("to.y");
    double endZ = zoneConfig.getDouble("to.z");

    ZoneEventHandler enterHandler = ZoneEventHandler.fromConfig(zoneConfig.getConfigurationSection("enter"));
    ZoneEventHandler exitHandler = ZoneEventHandler.fromConfig(zoneConfig.getConfigurationSection("exit"));

    return new RegionZone(zoneName, zoneWorld, enterHandler, exitHandler, startX, startY, startZ, endX, endY, endZ);
  }

  public static BoundryZone boundryZoneFromConfig(String zoneName, ConfigurationSection zoneConfig) {
    String zoneWorld = zoneConfig.getString("world");
    String condition = zoneConfig.getString("condition");
    ZoneEventHandler enterHandler = ZoneEventHandler.fromConfig(zoneConfig.getConfigurationSection("enter"));
    ZoneEventHandler exitHandler = ZoneEventHandler.fromConfig(zoneConfig.getConfigurationSection("exit"));

    return new BoundryZone(zoneName, zoneWorld, enterHandler, exitHandler, condition);
  }
}