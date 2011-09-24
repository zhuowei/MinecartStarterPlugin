package com.roboticoverlord.micro.bukkit;

//Bukkit imports
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

//Java imports
import java.util.HashSet;
import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.event.player.PlayerListener;

public class MinecartStarter extends JavaPlugin {

	/**
	 * Minecart listener class
	 */
	VehicleListener vehicleListener = new MinecartStarterVehicleListener(this);
	PlayerListener playerListener = new MinecartStarterPlayerListener(this);
	/**
	 * Logger magic
	 */
	public static final Logger log = Logger.getLogger("Minecraft");
	/**
	 * List of minecart entities
	 */
	public HashSet<Minecart> mcarts;

	public MinecartStarter() {
		this.mcarts = new HashSet<Minecart>();
	}

	/**
	 * Outputs a message when disabled
	 */
	public void onDisable() {
		this.mcarts.clear();
		log.log(Level.INFO,"[{0}] Plugin disabled. (version{1})", new Object[]{this.getDescription().getName(), this.getDescription().getVersion()});
	}

	/**
	 * Enables the plugin
	 */
	public void onEnable() {

		this.mcarts.clear();

		PluginManager pm = getServer().getPluginManager();

		//this.cartMonitor.start();

		//Event updates the database file on quit
		pm.registerEvent(Event.Type.VEHICLE_DAMAGE, this.vehicleListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.VEHICLE_EXIT, this.vehicleListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_TELEPORT, this.playerListener, Event.Priority.Normal, this);

		//Print that the plugin has been enabled!
		log.log(Level.INFO,"[" + this.getDescription().getName() + "] Plugin enabled! (version " + this.getDescription().getVersion() + ")");

	}

	private boolean hasPermission(Player p) {
		return p.hasPermission("micro.minecartstarter.can");
	}

	private boolean isAnonymous(Object t) {
		return !(t instanceof Player);
	}

	/**
	 * Called when a user performs a command
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();

		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (commandName.equals("minecart")) {

				if (this.isAnonymous(sender)) {
					return false;
				}
				if (!this.hasPermission(player)) {
					sender.sendMessage("You don't have permission.");
					return false;
				}
				
				World w = ((Player) sender).getWorld();
				int amion = w.getBlockTypeIdAt(((Player) sender).getLocation());
				if (amion == 66 || amion == 27 || amion == 28) {
					Minecart m = w.spawn(((Player) sender).getLocation(), Minecart.class);
					m.setPassenger(((Player) sender));

					this.mcarts.add(m);

					Vector v = m.getVelocity();
					double degreeRotation = (((Player) sender).getLocation().getYaw() - 90.0F) % 360.0F;
					if (degreeRotation < 0.0D) {
						degreeRotation += 360.0D;
					}

					CardinalDirection d = getDirection(degreeRotation);
					switch (d) {
						case North:
							v.setX(-8);
							break;
						case East:
							v.setZ(-8);
							break;
						case South:
							v.setX(8);
							break;
						case West:
							v.setZ(8);
							break;
					}
					m.setVelocity(v);
				}

				return true;
			}


		}
		return false;
	}

	private enum CardinalDirection {

		North,
		East,
		South,
		West,
		Unknown
	}

	private CardinalDirection getDirection(double degrees) {
		if (degrees <= 45.0D || degrees > 315.0D) {
			return CardinalDirection.North;
		}
		if (degrees > 45.0D && degrees <= 135.0D) {
			return CardinalDirection.East;
		}
		if (degrees > 135.0D && degrees <= 225.0D) {
			return CardinalDirection.South;
		}
		if (degrees > 225.0D && degrees <= 315.0D) {
			return CardinalDirection.West;
		}
		return CardinalDirection.Unknown;
	}
}
