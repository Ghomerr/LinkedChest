package com.ghomerr.linkedchest.utils;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.ghomerr.linkedchest.constants.Constants;
import com.ghomerr.linkedchest.enums.Commands;
import com.ghomerr.linkedchest.enums.Configurations;
import com.ghomerr.linkedchest.enums.Messages;
import com.ghomerr.linkedchest.enums.PermissionsNodes;

public class PermissionsUtils
{
	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);

	private static PluginManager _pm = null;

	private static boolean _usePermissions = false;

	private static boolean _usePermissionsBukkit = false;
	private static boolean _usePermissionsEx = false;
	private static boolean _useNativePermissions = false;

	private static PermissionManager _permManager = null;

	public static boolean isPermissionsEnabled()
	{
		final Boolean bool = ConfigurationUtils.getConfigValueByType(Configurations.USE_PERMISSIONS, Boolean.class);
		if (bool != null)
		{
			return bool.booleanValue();
		}
		else
		{
			return Configurations.USE_PERMISSIONS.getDefaultValueByType(Boolean.class);
		}
	}

	public static boolean togglePermissionsState()
	{
		return ConfigurationUtils.toggleBooleanValue(Configurations.USE_PERMISSIONS);
	}

	public static void loadPermissions(final JavaPlugin plugin)
	{
		_pm = plugin.getServer().getPluginManager();
		_usePermissions = isPermissionsEnabled();

		try
		{
			if (_usePermissions)
			{
				// Search PermissionsBukkit plugin
				Plugin permPlugin = _pm.getPlugin(Constants.PLUGIN_PERMISSIONS_BUKKIT);

				if (permPlugin != null)
				{
					if (permPlugin.isEnabled())
					{
						_usePermissionsBukkit = true;
						_LOGGER.info(Constants.TAG + permPlugin.getDescription().getFullName()
								+ "  will be used to manage Permissions.");
					}
					else
					{
						_usePermissionsBukkit = forcePermissionsPluginLoading(permPlugin);
						_usePermissions = _usePermissionsBukkit;
					}
				}
				else
				{
					// Search PermissionsEx plugin
					permPlugin = _pm.getPlugin(Constants.PLUGIN_PERMISSIONS_EX);

					if (permPlugin != null)
					{
						boolean pluginLoaded = permPlugin.isEnabled();
						if (!pluginLoaded)
						{
							pluginLoaded = forcePermissionsPluginLoading(permPlugin);
						}

						if (pluginLoaded)
						{
							_permManager = PermissionsEx.getPermissionManager();

							if (_permManager != null)
							{
								_usePermissionsEx = true;
								_LOGGER.info(Constants.TAG + permPlugin.getDescription().getFullName()
										+ "  will be used to manage Permissions.");
							}
							else
							{
								_usePermissions = false;
								_LOGGER.warning(Constants.TAG + permPlugin.getDescription().getFullName()
										+ " has not been loaded correctly. Permissions disabled.");
							}
						}
					}
					else
					{
						// Use Native Bukkit's Permission is used
						_useNativePermissions = true;
						_LOGGER.info(Constants.TAG + "Native Bukkit's Permissions will be used to manage Permissions.");
					}
				}
			}
		}
		catch (final Throwable th)
		{
			_usePermissions = false;
			_LOGGER.severe(Constants.TAG + "Permissions loading has failed. Permissions disabled.");
			th.printStackTrace();
		}

		if (_usePermissions != isPermissionsEnabled())
		{
			ConfigurationUtils.setConfigValue(Configurations.USE_PERMISSIONS, _usePermissions);
		}
	}
	
	public static boolean hasPermission(final Player player, final PermissionsNodes permissionNode)
	{
		boolean hasPerm = false;
		
		if (permissionNode != null)
		{
			try
			{
				if (isPermissionsEnabled())
				{
					if (_usePermissionsBukkit || _useNativePermissions)
					{
						hasPerm = player.hasPermission(permissionNode.node);
					}
					else if (_usePermissionsEx)
					{
						hasPerm = _permManager.has(player, permissionNode.node);
					}
				}
				else
				{
					if (permissionNode.isAdminCommand)
					{
						if (player.isOp())
						{
							hasPerm = true;
						}
					}
					else
					{
						hasPerm = true;
					}
				}
			}
			catch (final Throwable th)
			{
				_LOGGER.severe(Constants.TAG + "Permissions access has failed. Permissions disabled.");
				th.printStackTrace();
				ConfigurationUtils.setConfigValue(Configurations.USE_PERMISSIONS, false);
			}
		}

		if (!hasPerm && permissionNode.actionMessage != null)
		{
			player.sendMessage(ChatColor.RED
					+ MessagesUtils.getWithColor(permissionNode.actionMessage, ChatColor.RED));
		}

		return hasPerm;
	}

	public static boolean hasPermission(final Player player, final Commands command)
	{
		boolean hasPerm = false;

		final PermissionsNodes permissionNode = PermissionsNodes.getPermissionNodeForCommand(command);

		hasPerm = hasPermission(player, permissionNode);

		if (!hasPerm && permissionNode.command != null)
		{
			player.sendMessage(ChatColor.RED
					+ MessagesUtils.getWithColors(Messages.COMMAND_NOT_ALLOWED, ChatColor.RED, ChatColor.AQUA,
							command.name()));
		}

		return hasPerm;
	}

	private static boolean forcePermissionsPluginLoading(final Plugin permPlugin)
	{
		boolean pluginLoaded = false;

		try
		{
			_pm.enablePlugin(permPlugin);

			if (permPlugin.isEnabled())
			{
				pluginLoaded = true;
			}
		}
		catch (final Throwable th)
		{
			_LOGGER.warning(Constants.TAG + permPlugin.getDescription().getFullName()
					+ " could not have been forced to load. Permissions disabled.");
			th.printStackTrace();
		}

		if (pluginLoaded)
		{
			_LOGGER.info(Constants.TAG + permPlugin.getDescription().getFullName()
					+ "  has been forced to load and will be used to manage Permissions.");
		}

		return pluginLoaded;
	}
}
