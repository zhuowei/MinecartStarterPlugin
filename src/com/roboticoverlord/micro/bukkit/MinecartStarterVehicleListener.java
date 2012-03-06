package com.roboticoverlord.micro.bukkit;

import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class MinecartStarterVehicleListener implements Listener {

	private MinecartStarter parent;

	public MinecartStarterVehicleListener(MinecartStarter parent) {
		this.parent = parent;
	}

	@EventHandler
	public void onVehicleDamage(VehicleDamageEvent e) {
		if (e.getVehicle() instanceof Minecart) {
			if (parent.mcarts.contains(e.getVehicle())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onVehicleExit(VehicleExitEvent e) {
		if (e.getVehicle() instanceof Minecart) {
			if (parent.mcarts.contains(e.getVehicle())) {
				e.getVehicle().remove();
				parent.mcarts.remove(e.getVehicle());
			}
		}
	}
}
