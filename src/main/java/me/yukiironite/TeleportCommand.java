package me.yukiironite;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TeleportCommand {
  private String world;
  private String x;
  private String y;
  private String z;
  private String motionX;
  private String motionY;
  private String motionZ;

  public TeleportCommand(String world, String x, String y, String z, String motionX, String motionY, String motionZ) {
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
    this.motionX = motionX;
    this.motionY = motionY;
    this.motionZ = motionZ;
  }

	public static TeleportCommand fromConfig(ConfigurationSection configurationSection) {
    return new TeleportCommand(
      configurationSection.getString("world"),
      configurationSection.getString("location.x"),
      configurationSection.getString("location.y"),
      configurationSection.getString("location.z"),
      configurationSection.getString("motion.x"),
      configurationSection.getString("motion.y"),
      configurationSection.getString("motion.z")
    );
	}

	public Location locationFrom(Player player) {
    World destWorld = Bukkit.getWorld(this.world);
    double destX = Solver.expandAndSolve(this.x, player, player.getLocation().getX());
    double destY = Solver.expandAndSolve(this.y, player, player.getLocation().getY());
    double destZ = Solver.expandAndSolve(this.z, player, player.getLocation().getZ());

		return new Location(destWorld, destX, destY, destZ);
	}

	public Vector motionFrom(Player player) {
    double destX = Solver.expandAndSolve(this.motionX, player, player.getVelocity().getX());
    double destY = Solver.expandAndSolve(this.motionY, player, player.getVelocity().getY());
    double destZ = Solver.expandAndSolve(this.motionZ, player, player.getVelocity().getZ());

		return new Vector(destX, destY, destZ);
	}
}