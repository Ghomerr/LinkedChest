package com.ghomerr.linkedchest.listeners;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.ghomerr.linkedchest.LinkedChest;
import com.ghomerr.linkedchest.constants.Constants;
import com.ghomerr.linkedchest.enums.Commands;
import com.ghomerr.linkedchest.enums.Configurations;
import com.ghomerr.linkedchest.enums.Messages;
import com.ghomerr.linkedchest.utils.ConfigurationUtils;
import com.ghomerr.linkedchest.utils.GlobalUtils;
import com.ghomerr.linkedchest.utils.MessagesUtils;
import com.ghomerr.linkedchest.utils.PermissionsUtils;
import com.ghomerr.linkedchest.utils.StringUtils;
import com.ghomerr.linkedchest.utils.WorldUtils;

public class CommandsListener implements CommandExecutor
{
	public LinkedChest plugin = null;

	public CommandsListener (final LinkedChest plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] split)
	{
		if (sender instanceof Player)
		{
			final Player player = (Player) sender;

			final Commands main = Commands.commands.get(label);
			switch (main)
			{
				case LINKED_CHEST:
				{
					if (split.length >= 1)
					{
						final String arg1 = split[0];
						final Commands command = Commands.commands.get(arg1);

						if (command != null)
						{
							if (PermissionsUtils.hasPermission(player, command))
							{
								switch (command)
								{
									case ADD:
									{
										if (split.length == 2 || split.length == 3)
										{
											final String chestName = split[1];
											if (!plugin.hasChestName(chestName))
											{
												final Block block = player.getTargetBlock(null, 6);
												if (WorldUtils.isChest(block))
												{
													String checkChestName = plugin.getMasterChestNameFromBlock(block);
													// Is Master Chest
													if (checkChestName != null)
													{
														player.sendMessage(MessagesUtils.getWithColors(
																Messages.TARGET_CHEST_ALREADY_MASTER, ChatColor.RED, ChatColor.AQUA,
																chestName, checkChestName));
													}
													else
													{
														checkChestName = plugin.getLinkedChestNameFromBlock(block);
														// Is Linked Chest
														if (checkChestName != null)
														{
															player.sendMessage(MessagesUtils.getWithColors(
																	Messages.TARGET_CHEST_ALREADY_LINKED, ChatColor.RED, ChatColor.AQUA,
																	chestName, checkChestName));
														}
													}

													// Is New Chest
													if (checkChestName == null)
													{
														final Block doubleChestBlock = WorldUtils.getSameChestNearby(block, Block.class);
														boolean isAdminChest = false;
														String adminOption = null;

														if (split.length == 3)
														{
															adminOption = split[2];
															isAdminChest = Commands.ADMIN_CHEST.has(adminOption);
														}

														if (plugin.addVirtualInventory(chestName, block, doubleChestBlock, isAdminChest))
														{
															player.sendMessage(MessagesUtils.getWithColors(Messages.MASTER_CHEST_ADDED,
																	ChatColor.GREEN, ChatColor.AQUA, chestName));
														}
														else
														{
															player.sendMessage(MessagesUtils.getWithColors(Messages.INVALID_CHEST_NAME,
																	ChatColor.RED, ChatColor.AQUA, chestName));
														}
													}
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColor(Messages.TARGET_BLOCK_ISNOT_CHEST,
															ChatColor.RED));
												}
											}
											else
											{
												player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_ALREADY_EXISTS,
														ChatColor.RED, ChatColor.AQUA, chestName));
											}
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_ADD, ChatColor.YELLOW));
										}
										break;
									}

									case ADMIN_CHEST:
									{
										if (split.length == 2)
										{
											final String chestName = split[1];
											boolean isAdmin = false;
											if (plugin.hasChestName(chestName))
											{
												try
												{
													isAdmin = plugin.toggleAdminChest(chestName);
													if (isAdmin)
													{
														player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_IS_ADMIN,
																ChatColor.RED, ChatColor.AQUA, chestName));
													}
													else
													{
														player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_ISNOT_ADMIN,
																ChatColor.GREEN, ChatColor.AQUA, chestName));
													}
												}
												catch (final IllegalArgumentException ex)
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_ADMIN_FAILED,
															ChatColor.RED, ChatColor.AQUA, chestName));
												}
											}
											else
											{
												player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_NOT_FOUND, ChatColor.RED,
														ChatColor.AQUA, chestName));
											}
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_ALIASES, ChatColor.YELLOW));
										}
										break;
									}

									case ALIASES:
									{
										if (split.length == 2)
										{
											final String helpCommand = split[1];
											if (StringUtils.isNotBlank(helpCommand))
											{
												final Commands helpCmd = Commands.commands.get(helpCommand);
												if (helpCmd != null)
												{
													player.sendMessage(plugin.getAliasesListForCommand(helpCmd));
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.ALIASES_UNKNOWN_COMMAND,
															ChatColor.RED, ChatColor.AQUA, helpCommand));
												}
											}
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_ALIASES, ChatColor.YELLOW));
										}
										break;
									}

									case CONFIG:
									{
										if (split.length == 1)
										{
											player.sendMessage(plugin.getConfigList());
										}
										else
										{
											String confParam = null;
											String confValue = null;

											if (split.length == 2)
											{
												confParam = split[1];
											}
											else if (split.length == 3)
											{
												confParam = split[1];
												confValue = split[2];
											}

											if (StringUtils.isNotBlank(confParam))
											{
												final Configurations conf = Configurations.list.get(confParam);
												if (conf != null)
												{
													final StringBuilder strBld = new StringBuilder();

													// Toggle boolean parameters
													if (conf.type == Boolean.class && StringUtils.isBlank(confValue))
													{
														final Boolean bool = ConfigurationUtils.toggleBooleanValue(conf);
														if (bool != null)
														{
															strBld.append(MessagesUtils.getWithColor(Messages.CONFIGURATION_UPDATED,
																	ChatColor.GREEN));
															plugin.appendConfigParam(conf, strBld);

														}
													}
													// Modify boolean parameters
													else if (conf.type == Boolean.class)
													{
														final Boolean bool = Boolean.parseBoolean(confValue);
														if (ConfigurationUtils.setConfigValue(conf, bool))
														{
															strBld.append(MessagesUtils.getWithColor(Messages.CONFIGURATION_UPDATED,
																	ChatColor.GREEN));
															plugin.appendConfigParam(conf, strBld);
														}
														else
														{
															strBld.append(MessagesUtils.getWithColors(Messages.CONFIGURATION_VALUE_INVALID,
																	ChatColor.RED, ChatColor.AQUA, confValue, confParam));
														}
													}
													// Other parameters
													else if (StringUtils.isNotBlank(confValue))
													{
														boolean valueIsValid = true;
														// check value
														switch (conf)
														{
															case LANGUAGE:
																if (!Constants.DEFAULT_LANGUAGE.equals(confValue.toLowerCase()))
																{
																	final File languageFile = new File(
																			Constants.LANGUAGE_FILE_PATH.replace(Constants.LANGUAGE_TAG,
																					confValue.toLowerCase()));
																	valueIsValid = languageFile.exists();
																}
																break;
														}

														if (valueIsValid)
														{
															// String Configuration parameters
															if (conf.type == String.class)
															{
																if (ConfigurationUtils.setConfigValue(conf, confValue))
																{
																	boolean postTreatmentIsOk = true;
																	// post treatment
																	switch (conf)
																	{
																		case LANGUAGE:
																			postTreatmentIsOk = MessagesUtils.loadMessages(confValue);
																			break;
																	}

																	if (postTreatmentIsOk)
																	{
																		strBld.append(MessagesUtils.getWithColor(
																				Messages.CONFIGURATION_UPDATED, ChatColor.GREEN));
																		plugin.appendConfigParam(conf, strBld);
																	}
																	else
																	{
																		strBld.append(MessagesUtils.getWithColors(
																				Messages.CONFIGURATION_UPDATE_FAILED, ChatColor.RED,
																				ChatColor.AQUA, confParam, confValue));
																	}
																}
																else
																{
																	strBld.append(MessagesUtils.getWithColors(
																			Messages.CONFIGURATION_UPDATE_FAILED, ChatColor.RED,
																			ChatColor.AQUA, confParam, confValue));
																}
															}
															// Others...
															// else if (...)
															// {
															// }
															// Wrong type of parameters
															else
															{
																strBld.append(MessagesUtils.getWithColors(
																		Messages.CONFIGURATION_VALUE_INVALID, ChatColor.RED,
																		ChatColor.AQUA, confValue, confParam));
															}
														}
														else
														{
															strBld.append(MessagesUtils.getWithColors(Messages.CONFIGURATION_VALUE_INVALID,
																	ChatColor.RED, ChatColor.AQUA, confValue, confParam));
														}
													}
													else
													{
														strBld.append(MessagesUtils.getWithColors(Messages.CONFIGURATION_VALUE_NEEDED,
																ChatColor.RED, ChatColor.AQUA, confParam));
													}

													// Final prompt
													if (strBld.length() > 0)
													{
														player.sendMessage(strBld.toString());
													}
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.CONFIGURATION_PARAM_UNKNOWN,
															ChatColor.RED, ChatColor.AQUA, confParam));
												}
											}
										}
										break;
									}

									case DETAILS:
									{
										if (split.length == 2)
										{
											final String chestName = split[1];
											player.sendMessage(plugin.getChestDetails(chestName));
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_DETAILS, ChatColor.YELLOW));
										}
										break;
									}

									case LINK:
									{
										if (split.length == 2)
										{
											final String chestName = split[1];
											if (plugin.hasChestName(chestName))
											{
												final Block block = player.getTargetBlock(null, 6);
												if (WorldUtils.isChest(block))
												{
													String chestNameCheck = plugin.getMasterChestNameFromBlock(block);
													if (chestNameCheck == null)
													{
														chestNameCheck = plugin.getLinkedChestNameFromBlock(block);
														if (chestNameCheck == null)
														{
															final String singleShortLoc = StringUtils.printShortLocation(block
																	.getLocation());
															boolean linkSuccess = plugin.addLinkedChest(singleShortLoc, chestName);

															final Location doubleLoc = WorldUtils.getSameChestNearby(block, Location.class);
															if (doubleLoc != null)
															{
																final String doubleShortLoc = StringUtils.printShortLocation(doubleLoc);
																linkSuccess = linkSuccess
																		&& plugin.addLinkedChest(doubleShortLoc, chestName);
															}

															if (linkSuccess)
															{
																player.sendMessage(MessagesUtils.getWithColors(Messages.LINK_CHEST_SUCCESS,
																		ChatColor.GREEN, ChatColor.AQUA, chestName));
															}
															else
															{
																player.sendMessage(MessagesUtils.getWithColors(Messages.LINK_CHEST_FAIL,
																		ChatColor.RED, ChatColor.AQUA, chestName));
															}
														}
														else
														{
															player.sendMessage(MessagesUtils.getWithColors(
																	Messages.CANNOT_LINK_LINKED_CHEST, ChatColor.RED, ChatColor.AQUA,
																	chestNameCheck));
														}
													}
													else
													{
														player.sendMessage(MessagesUtils.getWithColors(Messages.CANNOT_LINK_MASTER_CHEST,
																ChatColor.RED, ChatColor.AQUA, chestNameCheck));
													}
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColor(Messages.TARGET_BLOCK_ISNOT_CHEST,
															ChatColor.RED));
												}
											}
											else
											{
												player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_NOT_FOUND, ChatColor.RED,
														ChatColor.AQUA, chestName));
											}
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_LINK, ChatColor.YELLOW));
										}
										break;
									}

									case LIST:
									{
										String param = null;
										if (split.length == 2)
										{
											param = split[1];
										}

										player.sendMessage(plugin.getLinkedChestsList(param));
										break;
									}

									// /lc move myChest
									case MOVE:
									{
										if (split.length == 2)
										{
											final String chestName = split[1];
											if (plugin.hasChestName(chestName))
											{
												final Block target = player.getTargetBlock(null, 6);
												if (WorldUtils.isChest(target))
												{
													String checkChestName = plugin.getMasterChestNameFromBlock(target);
													if (checkChestName != null)
													{
														player.sendMessage(MessagesUtils.getWithColors(
																Messages.TARGET_CHEST_ALREADY_MASTER, ChatColor.RED, ChatColor.AQUA,
																chestName, checkChestName));
													}

													if (checkChestName == null)
													{
														final Chest chest = (Chest) target.getState();
														final Inventory targetInv = chest.getInventory();
														final ItemStack[] targetContent = targetInv.getContents();
														if (targetContent == null || GlobalUtils.arrayIsEmpty(targetContent))
														{
															final Inventory masterInv = plugin.getVirtualInventory(chestName);
															final ItemStack[] masterChestContent = masterInv.getContents();
															if (targetInv.getSize() >= GlobalUtils.arrayLength(masterChestContent))
															{
																if(plugin.updateMasterChest(chestName, chest))
																{
																	// If copy succeeded, master chest can be cleared
																	if (GlobalUtils.copyArray(masterChestContent, targetContent))
																	{
	    																targetInv.setContents(targetContent);
	    																masterInv.clear();
																	}
																	
    																player.sendMessage(MessagesUtils.getWithColors(Messages.MASTER_CHEST_MOVE_SUCCESS, ChatColor.GREEN,
																			ChatColor.AQUA, chestName));
																}
																else
																{
																	player.sendMessage(MessagesUtils.getWithColors(Messages.MASTER_CHEST_MOVE_FAILURE, ChatColor.RED,
																			ChatColor.AQUA, chestName));
																}
															}
															else
															{
																player.sendMessage(MessagesUtils.getWithColor(Messages.TARGET_CHEST_SIZE_INVALID,
																		ChatColor.RED));
															}
														}
														else
														{
															player.sendMessage(MessagesUtils.getWithColor(Messages.TARGET_CHEST_NOT_EMPTY,
																	ChatColor.RED));
														}
													}
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColor(Messages.TARGET_BLOCK_ISNOT_CHEST,
															ChatColor.RED));
												}
											}
											else
											{
												player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_NOT_FOUND, ChatColor.RED,
														ChatColor.AQUA, chestName));
											}
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_MOVE, ChatColor.YELLOW));
										}

										break;
									}

									case OPEN:
									{
										if (split.length == 2)
										{
											final String chestName = split[1];
											if (plugin.hasChestName(chestName))
											{
												if (plugin.openVirtualInventory(player, chestName))
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.REMOTE_OPENING_CHEST,
															ChatColor.YELLOW, ChatColor.AQUA, chestName));
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.OPEN_CHEST_FAILED,
															ChatColor.RED, ChatColor.AQUA, chestName));
												}
											}
											else
											{
												player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_NOT_FOUND, ChatColor.RED,
														ChatColor.AQUA, chestName));
											}
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_OPEN, ChatColor.YELLOW));
										}
										break;
									}
									
									case POSITIONS:
									{
										if (split.length == 2)
										{
											final String chestName = split[1];
											player.sendMessage(plugin.getLinkedChestsPositions(chestName));
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_POSITIONS, ChatColor.YELLOW));
										}
										break;
									}

									case REMOVE:
									{
										if (split.length == 2)
										{
											final String chestName = split[1];
											if (plugin.hasChestName(chestName))
											{
												if (plugin.removeVirtualInventory(chestName))
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_REMOVED, ChatColor.GREEN,
															ChatColor.AQUA, chestName));
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.REMOVE_CHEST_FAILED,
															ChatColor.RED, ChatColor.AQUA, chestName));
												}
											}
											else
											{
												player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_NOT_FOUND, ChatColor.RED,
														ChatColor.AQUA, chestName));
											}
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_REMOVE, ChatColor.YELLOW));
										}
										break;
									}

									case UNLINK:
									{
										if (split.length == 1)
										{
											final Block block = player.getTargetBlock(null, 6);
											if (WorldUtils.isChest(block))
											{
												String chestNameCheck = plugin.getMasterChestNameFromBlock(block);
												if (chestNameCheck == null)
												{
													chestNameCheck = plugin.getLinkedChestNameFromBlock(block);
													if (chestNameCheck != null)
													{
														boolean removeSuccess = plugin.removeLinkedChestLocation(block.getLocation());
														final Location doubleLoc = WorldUtils.getSameChestNearby(block, Location.class);
														if (doubleLoc != null)
														{
															removeSuccess = plugin.removeLinkedChestLocation(doubleLoc);
														}

														if (removeSuccess)
														{
															player.sendMessage(MessagesUtils.getWithColors(Messages.UNLINK_CHEST_SUCCESS,
																	ChatColor.GREEN, ChatColor.AQUA, chestNameCheck));
														}
														else
														{
															player.sendMessage(MessagesUtils.getWithColor(Messages.UNLINK_CHEST_FAIL,
																	ChatColor.RED));
														}
													}
													else
													{
														player.sendMessage(MessagesUtils.getWithColor(Messages.CANNOT_UNLINK_CHEST,
																ChatColor.RED));
													}
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.CANNOT_UNLINK_MASTER_CHEST,
															ChatColor.RED, ChatColor.AQUA, chestNameCheck));
												}
											}
											else
											{
												player.sendMessage(MessagesUtils.getWithColor(Messages.TARGET_BLOCK_ISNOT_CHEST,
														ChatColor.RED));
											}
										}
										else if (split.length == 2)
										{
											final String name = split[1];
											if (plugin.hasChestName(name))
											{
												if (plugin.unlinkAllChestsOf(name))
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.UNLINK_CHESTS_SUCCESS,
															ChatColor.GREEN, ChatColor.AQUA, name));
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.UNLINK_CHESTS_FAIL,
															ChatColor.RED, ChatColor.AQUA, name));
												}
											}
											else
											{
												player.sendMessage(MessagesUtils.getWithColors(Messages.CHEST_NOT_FOUND, ChatColor.RED,
														ChatColor.AQUA, name));
											}
										}
										else
										{
											player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_UNLINK, ChatColor.YELLOW));
										}

										break;
									}

									case VERSION:
									{
										player.sendMessage(MessagesUtils.getWithColors(Messages.CURRENT_VERSION, ChatColor.YELLOW,
												ChatColor.AQUA, plugin.getDescription().getVersion()));
										break;
									}

									case HELP:
									{
										String helpCommand = null;

										if (split.length == 2)
										{
											helpCommand = split[1];
											if (StringUtils.isNotBlank(helpCommand))
											{
												final Commands helpCmd = Commands.commands.get(helpCommand);
												if (helpCmd != null)
												{
													player.sendMessage(MessagesUtils.getWithColor(helpCmd.helpMessage, ChatColor.YELLOW));
												}
												else
												{
													player.sendMessage(MessagesUtils.getWithColors(Messages.UNKNOWN_COMMAND, ChatColor.RED,
															ChatColor.AQUA, helpCommand));
												}
											}
											else
											{
												player.sendMessage(MessagesUtils.getWithColor(Messages.HELP_INVALID_CMD, ChatColor.RED));
											}
										}
										else
										{
											plugin.displayCompleteHelpMessage(player);
										}
										break;
									}

									default:
									{
										player.sendMessage(MessagesUtils.getWithColor(Commands.LINKED_CHEST.helpMessage, ChatColor.YELLOW));
									}
								}
							}
						}
						else
						{
							player.sendMessage(MessagesUtils.getWithColors(Messages.UNKNOWN_COMMAND, ChatColor.RED, ChatColor.AQUA, arg1));
							plugin.displayCompleteHelpMessage(player);
						}
					}
					else
					{
						plugin.displayCompleteHelpMessage(player);
					}
				}
			}
		}
		return true;
	}
}
