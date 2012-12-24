package com.ghomerr.linkedchest.listeners;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.ghomerr.linkedchest.LinkedChest;
import com.ghomerr.linkedchest.constants.Constants;
import com.ghomerr.linkedchest.enums.BrokenChestType;
import com.ghomerr.linkedchest.enums.Messages;
import com.ghomerr.linkedchest.objects.BrokenChest;
import com.ghomerr.linkedchest.utils.DebugUtils;
import com.ghomerr.linkedchest.utils.MessagesUtils;
import com.ghomerr.linkedchest.utils.StringUtils;
import com.ghomerr.linkedchest.utils.WorldUtils;

public class BlockEventListener implements Listener
{
	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);

	public LinkedChest plugin = null;
	public BlockEventListener (final LinkedChest plugin)
	{
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreakEvent(final BlockBreakEvent event)
	{
		final Block block = event.getBlock();
		final BrokenChest chest = getBrokenChest(block);
		switch (chest.type)
		{
			case MASTER_CHEST:
			{
				event.setCancelled(true);
				event.getPlayer().sendMessage(
						MessagesUtils.getWithColors(Messages.MASTER_CHEST_UNBREAKABLE, ChatColor.RED, ChatColor.AQUA,
								chest.name));
				_LOGGER.info(Constants.TAG + "Master Chest '" + chest.name + "' has been saved from being destroyed by "
						+ event.getPlayer().getName());
				break;
			}

			case LINKED_CHEST:
			{
				if (plugin.removeLinkedChestLocation(block.getLocation()))
				{
					event.getPlayer().sendMessage(
							MessagesUtils.getWithColors(Messages.BREAK_LINK_CHEST, ChatColor.YELLOW, ChatColor.AQUA,
									chest.name));
					if (DebugUtils.isDebugEnabled())
					{
						_LOGGER.info(Constants.TAG + "Linked Chest '" + chest.name + "' has been destroyed by "
								+ event.getPlayer().getName());
					}
				}
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityExplodeEvent(final EntityExplodeEvent event)
	{
		if (!event.isCancelled())
		{
			final List<Block> blockList = event.blockList();
			for (final Block block : blockList)
			{
				final BrokenChest chest = getBrokenChest(block);
				switch (chest.type)
				{
					case MASTER_CHEST:
					{
						event.setCancelled(true);
						_LOGGER.info(Constants.TAG + "Master Chest '" + chest.name
								+ "' has been saved from explosion caused by "
								+ event.getEntity() + " at " + StringUtils.printShortLocation(event.getLocation()));
						break;
					}
	
					case LINKED_CHEST:
					{
						if(plugin.removeLinkedChestLocation(block.getLocation()))
						{
							if (DebugUtils.isDebugEnabled())
							{
								_LOGGER.info(Constants.TAG + "Linked Chest '" + chest.name + "' has been destroyed by explosion caused by "
										+ event.getEntity() + " at " + StringUtils.printShortLocation(event.getLocation()));
							}
						}
						break;
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBurnEvent(final BlockBurnEvent event)
	{
		if (!event.isCancelled())
		{
			final Block block = event.getBlock();
			final BrokenChest chest = getBrokenChest(block);
			switch (chest.type)
			{
				case MASTER_CHEST:
				{
					event.setCancelled(true);
					_LOGGER.info(Constants.TAG + "Master Chest '" + chest.name + "' has been saved from burning");
					break;
				}
	
				case LINKED_CHEST:
				{
					if (plugin.removeLinkedChestLocation(block.getLocation()))
					{
						if (DebugUtils.isDebugEnabled())
						{
							_LOGGER.info(Constants.TAG + "Linked Chest '" + chest.name + "' has been destroyed by burning");
						}
					}
					break;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlaceEvent(final BlockPlaceEvent event)
	{
		if (!event.isCancelled() && event.getPlayer() != null)
		{
			final Block placedBlock = event.getBlockPlaced();
			if (WorldUtils.isChest(placedBlock))
			{
				final Block block = WorldUtils.getChestNearby(placedBlock, Block.class);
				if (block != null)
				{
					String chestName = plugin.getMasterChestNameFromBlock(block);
					if (chestName != null)
					{
						event.getPlayer().sendMessage(
								MessagesUtils.getWithColors(Messages.CANNOT_PLACE_CHEST_NEARBY, ChatColor.RED,
										ChatColor.AQUA, chestName));
						event.setCancelled(true);
					}
					else
					{
						chestName = plugin.getLinkedChestNameFromBlock(block);
						if (chestName != null)
						{
							final Location loc = placedBlock.getLocation();
							if (plugin.addLinkedChest(loc, chestName))
							{
								if (DebugUtils.isDebugEnabled())
								{
									_LOGGER.info(Constants.TAG + "Double Linked Chest '" + chestName + "' has been added at " + loc);
								}
							}
						}
					}
				}
			}
		}
	}

	private BrokenChest getBrokenChest(final Block block)
	{
		BrokenChest chest = new BrokenChest();
		if (WorldUtils.isChest(block))
		{
			String chestName = plugin.getMasterChestNameFromBlock(block);
			if (chestName == null)
			{
				chestName = plugin.getLinkedChestNameFromBlock(block);
				if (chestName != null)
				{
					chest = new BrokenChest(BrokenChestType.LINKED_CHEST, chestName);
				}
			}
			else
			{
				chest = new BrokenChest(BrokenChestType.MASTER_CHEST, chestName);
			}
		}
		return chest;
	}
}
