package me.yukiironite;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class EventsListener implements Listener {
	private Map<String, List<Zone>> zonesByWorld;
	private HashMap<String, List<UUID>> playersByZone;

	public EventsListener(Map<String, List<Zone>> zonesByWorld) {
		this.zonesByWorld = zonesByWorld;
		this.playersByZone = new HashMap<String, List<UUID>>();

		this.zonesByWorld.values().stream()
			.flatMap(zones -> zones.stream())
			.map(Zone::getName)
			.forEach(name -> this.playersByZone.put(name, new ArrayList<UUID>()));
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location playerLocation = player.getLocation();
		World playerWorld = playerLocation.getWorld();
		String worldName = playerWorld.getName();
		List<Zone> worldZones = this.zonesByWorld.getOrDefault(worldName, new ArrayList<Zone>());

		Map<Boolean, List<Zone>> playerInZones = worldZones.stream()
			.collect(Collectors.partitioningBy(zone -> zone.isInZone(player)));

		playerInZones.get(Boolean.TRUE)
			.stream()
			.filter(zone -> !isPlayerInZone(player, zone))
			.forEach(enteredZone -> {
				addPlayerToZone(player, enteredZone);
				enteredZone.onEnter(player);
			});
		
		playerInZones.get(Boolean.FALSE)
			.stream()
			.filter(zone -> isPlayerInZone(player, zone))
			.forEach(exitedZone -> {
				removePlayerFromZone(player, exitedZone);
				exitedZone.onExit(player);
			});
	}

	public boolean isPlayerInZone(Player player, Zone zone) {
		return this.playersByZone
			.get(zone.getName())
			.contains(player.getUniqueId());
	}

	public void addPlayerToZone(Player player, Zone zone) {
		this.playersByZone
			.get(zone.getName())
			.add(player.getUniqueId());
	}

	public void removePlayerFromZone(Player player, Zone zone) {
		this.playersByZone
			.get(zone.getName())
			.remove(player.getUniqueId());
	}
}
