/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.roboticoverlord.micro.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Owner
 */
public class MinecartStarterPlayerListener implements Listener {

	MinecartStarter parent;

	public MinecartStarterPlayerListener(MinecartStarter p) {
		this.parent = p;
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if(e.getPlayer().isInsideVehicle()) {
			if(this.parent.mcarts.contains(e.getPlayer().getVehicle())) {
				e.getPlayer().leaveVehicle();
			}
		}
	}

}
