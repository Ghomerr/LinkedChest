package com.ghomerr.linkedchest.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.ghomerr.linkedchest.enums.Commands;
import com.ghomerr.linkedchest.enums.Configurations;
import com.ghomerr.linkedchest.enums.Messages;
import com.ghomerr.linkedchest.enums.PermissionsNodes;

public class PermissionsUtils
{
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

	public static boolean hasPermission(final Player player, final PermissionsNodes permissionNode)
	{
	    return player.hasPermission(permissionNode.node);
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
}
