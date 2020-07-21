package me.yukiironite;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.stream.Collectors;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class ZoneCommands extends JavaPlugin {

	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		FileConfiguration config = this.getConfig();
		Map<String, List<Zone>> zonesByWorld = createZonesFromConfig(config);

		//Register Event Listeners
		getServer()
			.getPluginManager()
			.registerEvents(new EventsListener(zonesByWorld), this);
		
		Bukkit.getServer().getLogger().info("Zone Commands Enabled!");
	}

	public Map<String, List<Zone>> createZonesFromConfig(FileConfiguration config) {
		ConfigurationSection zonesConfig = config.getConfigurationSection("zones");
		Set<String> zoneNames = zonesConfig.getKeys(false);

		return zoneNames.stream()
			.map(zoneName -> ZoneFactory.fromConfig(zoneName, zonesConfig.getConfigurationSection(zoneName)))
			.filter(zone -> zone != null)
			.collect(Collectors.groupingBy(Zone::getWorldName));
	}
}
