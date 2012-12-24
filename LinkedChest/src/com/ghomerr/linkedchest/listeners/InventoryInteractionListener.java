package com.ghomerr.linkedchest.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import com.ghomerr.linkedchest.LinkedChest;
import com.ghomerr.linkedchest.enums.Messages;
import com.ghomerr.linkedchest.enums.PermissionsNodes;
import com.ghomerr.linkedchest.utils.MessagesUtils;
import com.ghomerr.linkedchest.utils.PermissionsUtils;
import com.ghomerr.linkedchest.utils.WorldUtils;

public class InventoryInteractionListener implements Listener
{
	public LinkedChest plugin = null;
	
	public InventoryInteractionListener (final LinkedChest plugin)
	{
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEvent(final PlayerInteractEvent event)
	{
		if (!event.isCancelled())
		{
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				final Block block = event.getClickedBlock();
				boolean isMasterChest = false;
				if (WorldUtils.isChest(block))
				{			
					final Player player = (Player) event.getPlayer();
					
					String chestName = plugin.getMasterChestNameFromBlock(block);
					if (chestName == null)
					{
						chestName = plugin.getLinkedChestNameFromBlock(block);
					}
					else
					{
						isMasterChest = true;
					}
	
					if (chestName != null)
					{
						final Inventory virtualInv = plugin.getVirtualInventory(chestName);
	
						if (virtualInv != null)
						{
							boolean hasPerm = true;
							
							if(plugin.isAdminChest(chestName))
							{
								hasPerm = PermissionsUtils.hasPermission(player, PermissionsNodes.OPEN_ADMIN_CHEST);
							}
							
							if (hasPerm)
							{
								if (isMasterChest)
								{
									hasPerm = PermissionsUtils.hasPermission(player, PermissionsNodes.OPEN_MASTER_CHEST);
									if (hasPerm)
									{
										player.sendMessage(MessagesUtils.getWithColors(Messages.OPENING_MASTER_CHEST, ChatColor.YELLOW,
											ChatColor.AQUA, chestName));
										event.setCancelled(true);
										player.openInventory(virtualInv);
										plugin.playersWhoOpenChests.add(player.getName());
									}
									else
									{
										event.setCancelled(true);
									}
								}
								else
								{
									hasPerm = PermissionsUtils.hasPermission(player, PermissionsNodes.OPEN_LINKED_CHEST);
									if (hasPerm)
									{
										player.sendMessage(MessagesUtils.getWithColors(Messages.OPENING_LINKED_CHEST, ChatColor.YELLOW,
												ChatColor.AQUA, chestName));
										event.setCancelled(true);
										player.openInventory(virtualInv);
										plugin.playersWhoOpenChests.add(player.getName());
									}
									else
									{
										event.setCancelled(true);
									}
								}
							}
							else
							{
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryCloseEvent(final InventoryCloseEvent event)
	{
		if (event.getInventory().getType() == InventoryType.CHEST)
		{
			final String playerName = event.getPlayer().getName();	
			if(plugin.playersWhoOpenChests.contains(playerName))
			{
				plugin.saveWorldsIfNecessary(playerName);
				plugin.playersWhoOpenChests.remove(playerName);
			}
		}
	}
}
