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
	//private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);
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
//				if (block.getType() == Material.HOPPER && event.getMaterial() == Material.FLINT)
//				{
//					event.setCancelled(true);
//					event.getPlayer().sendMessage("Hopper Owner: " + WorldUtils.getMetadata(block, Constants.HOPPER_CREATOR_KEY, plugin));
//				}
//				else
//				{
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
								if (!event.isBlockInHand())
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
												if (plugin.isDisplayOpenMessageEnabled())
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.OPENING_MASTER_CHEST, ChatColor.YELLOW,
														ChatColor.AQUA, chestName));
												}
												
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
												
												if (plugin.isDisplayOpenMessageEnabled())
												{	
													player.sendMessage(MessagesUtils.getWithColors(Messages.OPENING_LINKED_CHEST, ChatColor.YELLOW,
														ChatColor.AQUA, chestName));
												}
												
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
								else
								{
									if (plugin.isDisplayOpenMessageEnabled())
									{	
										player.sendMessage(MessagesUtils.getWithColor(Messages.CANNOT_OPEN_WITH_BLOCK, ChatColor.RED));
									}
								}	
							}
						}
					}
//				}
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
	
//	@EventHandler(priority = EventPriority.LOWEST)
//	public void onHopperMoveItemEvent(final InventoryMoveItemEvent event)
//	{
//		_LOGGER.info(Constants.TAG + "-----------------------------------");
//		System.out.println("Init: " + event.getInitiator());
//		System.out.println("Source: " + event.getSource() + " size= " + event.getSource().getSize());
//		System.out.println("Dest: " + event.getDestination() + " size= " + event.getDestination().getSize());
//		System.out.println("Item: " + event.getItem());
//		
//		if (!event.isCancelled())
//		{
//			final InventoryHolder initiator = event.getInitiator().getHolder();
//			final HopperType hopperType = WorldUtils.getHopperType(initiator);
//			
//			Location hopperLocation = null;
//			switch (hopperType)
//			{
//				case BLOCK:
//					hopperLocation = ((Hopper) initiator).getLocation();
//					break;
//					
//				case MINECART:
//					hopperLocation = ((Minecart) initiator).getLocation();
//					break;
//			}
//			
//			if (hopperLocation != null)
//			{
//				if (DebugUtils.isDebugEnabled())
//				{
//					_LOGGER.info(Constants.TAG + "Hopper location: " + StringUtils.printShortLocation(hopperLocation));
//				}
//				
//				// Source is LinkedChest ? WILL NEVER HAPPENS !!! 
//				// final InventoryHolder source = event.getSource().getHolder();
//				// manageHolderBeforeSendingEvent(event, source, true);
//
//				// Destination is LinkedChest ?
//				final InventoryHolder destination = event.getDestination().getHolder();
//				manageHolderBeforeSendingEvent(event, destination, initiator, false);
//			}
//		}
//		_LOGGER.info(Constants.TAG + "-----------------------------------");
//	}
	
//	private void manageHolderBeforeSendingEvent(final InventoryMoveItemEvent event, final InventoryHolder holder, 
//			final InventoryHolder initiator, final boolean isSource)
//	{
//		// Holder is LinkedChest
//		if (WorldUtils.isChest(holder))
//		{
//			final Location holderLocation = ((Chest) holder).getLocation();
//			boolean isLinkedChest = true;
//			
//			String chestName = plugin.getLinkedChestNameFrom(holderLocation);
//			// Is Master Chest
//			if (chestName == null)
//			{
//				chestName = plugin.getMasterChestNameFrom(holderLocation);
//				isLinkedChest = false;
//			}
//
//			if (chestName != null)
//			{
//				if (initiator instanceof Metadatable)
//				{
//					final Metadatable objectHolder = (Metadatable) initiator;
//					final Object objPlayerName = WorldUtils.getMetadata(objectHolder, Constants.HOPPER_CREATOR_KEY, plugin);
//					if (objPlayerName != null)
//					{
//						final OfflinePlayer player = plugin.getServer().getOfflinePlayer(String.valueOf(objPlayerName));
//						final boolean isAdminChest = plugin.isAdminChest(chestName);
//						if (player != null 
//								&& (isAdminChest && PermissionsUtils.hasPermission(player.getPlayer(), PermissionsNodes.OPEN_ADMIN_CHEST))
//									|| !isLinkedChest && PermissionsUtils.hasPermission(player.getPlayer(), PermissionsNodes.OPEN_MASTER_CHEST)
//									|| isLinkedChest && PermissionsUtils.hasPermission(player.getPlayer(), PermissionsNodes.OPEN_LINKED_CHEST))
//						{
//							if (DebugUtils.isDebugEnabled())
//							{
//								_LOGGER.info(Constants.TAG + "Hopper created by " + objPlayerName + " is moving items to/from LinkedChest " + chestName);
//							}
//							
//							final Inventory virtualInv = plugin.getVirtualInventory(chestName);
//							Inventory sourceInv;
//							Inventory destInv;
////							if (isSource)
////							{
////								sourceInv = virtualInv;
////								destInv = event.getDestination();
////							}
////							else
////							{
//								sourceInv = event.getSource();
//								destInv = virtualInv;
////							}
//								
//							// When the master chest is known
//							if (isLinkedChest)
//							{
//								event.setCancelled(true);
////								plugin.getServer().getPluginManager().callEvent(
////										new InventoryMoveItemEvent(sourceInv, event.getItem(), destInv, false));
//								final ItemStack movedItem = event.getItem();
//								//sourceInv.removeItem(movedItem);
//								// TODO: LA SOURCE DOIT ETRE LE COFFRE INITIAL !
//								System.out.println("TEST: " + sourceInv.contains(movedItem));
//								//initiator.getInventory().remove(event.getItem());
//								destInv.addItem(movedItem);
//							}
//						}
////						else
////						{
////							if (DebugUtils.isDebugEnabled())
////							{
////								_LOGGER.warning(Constants.TAG + "Player is null (" + player + ") or player has not the permission.");
////							}
////							
////							// Player had not the permission: the hopper will be broken
////							if (player != null)
////							{
////								final HopperType type = WorldUtils.getHopperType(holder);
////								switch (type)
////								{
////									case BLOCK:
////										holderLocation.getBlock().breakNaturally();
////										break;
////										
////									case MINECART:
////										((Minecart) holder).setDamage(10);
////										break;
////								}
////							}
////						}
//					}
//					else
//					{
//						if (DebugUtils.isDebugEnabled())
//						{
//							_LOGGER.warning(Constants.TAG + "No player found for: " + objPlayerName);
//						}
//					}
//				}
//				else
//				{
//					if (DebugUtils.isDebugEnabled())
//					{
//						_LOGGER.info(Constants.TAG + "Holder cannot manage metadata.");
//					}
//				}
//			}
//			else
//			{
//				if (DebugUtils.isDebugEnabled())
//				{
//					_LOGGER.warning(Constants.TAG + "Normal chest.");
//				}
//			}
//		}
//	}
	

	
	// Method for Logger
//	@EventHandler(priority = EventPriority.MONITOR)
//	public void onInventoryClickEvent(final InventoryClickEvent event)
//	{
//		System.out.println("------------------------------");
//		System.out.println("RawSlot=" + event.getRawSlot()
//				+ "|Slot=" + event.getSlot()
//				+ "|CurrentItem=" + event.getCurrentItem()
//				+ "|Cursor=" + event.getCursor()
//				+ "|Holder=" + event.getInventory().getHolder()
//				+ "|Inventory.MaxStackSize=" + event.getInventory().getMaxStackSize()
//				+ "|Inventory.Size=" + event.getInventory().getSize()
//				+ "|Inventory.Content.Length=" + event.getInventory().getContents().length
//				+ "|SlotType=" + event.getSlotType()
//				+ "|WhoClicked=" + event.getWhoClicked());
//		System.out.println("------------------------------");
//	}
}
