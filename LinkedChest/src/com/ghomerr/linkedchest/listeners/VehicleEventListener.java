package com.ghomerr.linkedchest.listeners;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import com.ghomerr.linkedchest.LinkedChest;
import com.ghomerr.linkedchest.constants.Constants;
import com.ghomerr.linkedchest.utils.DebugUtils;
import com.ghomerr.linkedchest.utils.WorldUtils;

public class VehicleEventListener implements Listener
{
	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);
	public LinkedChest plugin = null;
	public HashSet<String> playersWithHoppersInHand = new HashSet<String>();
	
	public VehicleEventListener (final LinkedChest plugin)
	{
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteractEvent(final PlayerInteractEvent event)
	{
		if (!event.isCancelled() && event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			final Material mat = event.getMaterial();
			if (mat == Material.HOPPER_MINECART)
			{
				// When player right click with a hopper minecrat in hand, store the player name
				final String playerName = event.getPlayer().getName();
				if (DebugUtils.isDebugEnabled())
				{
					_LOGGER.info(Constants.TAG + playerName + " has right-clicked holding a HopperMinecart.");
				}
				playersWithHoppersInHand.add(playerName);
			}
			else if (mat == Material.FLINT && event.getClickedBlock().getType() == Material.HOPPER)
			{
				event.setCancelled(true);
				final Hopper hopper = (Hopper) event.getClickedBlock();
				event.getPlayer().sendMessage("Hopper Owner: " + WorldUtils.getMetadata(hopper, Constants.HOPPER_CREATOR_KEY, plugin));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onVehicleCreateEvent(final VehicleCreateEvent event)
	{
		final Vehicle vehicle = event.getVehicle();
		if (vehicle.getType() == EntityType.MINECART_HOPPER)
		{
			final List<Entity> nearbyEntities = vehicle.getNearbyEntities(6, 6, 6);
			for (final Entity entity : nearbyEntities)
			{
				if (entity.getType() == EntityType.PLAYER)
				{
					final Player player = (Player) entity;
					final String playerName = player.getName();
					
					if (player != null 
						&& player.getItemInHand().getType() == Material.HOPPER_MINECART
						&& playersWithHoppersInHand.contains(playerName))
					{
						// While creating vehicle, if this is the same player who right-clicked before, add metadata to the minecart hopper
						if (DebugUtils.isDebugEnabled())
						{
							_LOGGER.info(Constants.TAG + playerName + " has created a HopperMinecart");
						}
						playersWithHoppersInHand.remove(playerName);
						WorldUtils.setMetadata(vehicle, Constants.HOPPER_CREATOR_KEY, playerName, plugin);
						break;
					}
					else
					{
						player.sendMessage("You are not creating HopperCart");
					}
				}
			}
		}
	}
	
	// DEBUG WITH FLINT
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent event)
	{
		if (DebugUtils.isDebugEnabled())
		{
			final Player player = event.getPlayer();
			final Entity clickedEntity = event.getRightClicked();
			
			if (!event.isCancelled() 
					&& clickedEntity.getType() == EntityType.MINECART_HOPPER
					&& player.getItemInHand().getType() == Material.FLINT)
			{
				event.setCancelled(true);
				final HopperMinecart hopperMc = (HopperMinecart) clickedEntity;
				player.sendMessage("Hopper Owner: " + WorldUtils.getMetadata(hopperMc, Constants.HOPPER_CREATOR_KEY, plugin));
			}
		}
	}
}
