package com.ghomerr.linkedchest.enums;

import java.util.HashMap;

import com.ghomerr.linkedchest.constants.Constants;

public enum PermissionsNodes
{
	// COMMANDS
	ADD(Commands.ADD, Constants.PERMISSIONS_ROOT + "add", true),
	ADMIN_CHEST(Commands.ADMIN_CHEST, Constants.PERMISSIONS_ROOT + "adminchest", true),
	ALIASES(Commands.ALIASES, Constants.PERMISSIONS_ROOT + "aliases", false),
	CONFIG(Commands.CONFIG, Constants.PERMISSIONS_ROOT + "config", true),
	DETAILS(Commands.DETAILS, Constants.PERMISSIONS_ROOT + "details", false),
	HELP(Commands.HELP, Constants.PERMISSIONS_ROOT + "help", false),
	LINK(Commands.LINK, Constants.PERMISSIONS_ROOT + "link", true),
	LIST(Commands.LIST, Constants.PERMISSIONS_ROOT + "list", false),
	MOVE(Commands.MOVE, Constants.PERMISSIONS_ROOT + "move", true),
	OPEN_REMOTE(Commands.OPEN, Constants.PERMISSIONS_ROOT + "openremote", true),
	REMOVE(Commands.REMOVE, Constants.PERMISSIONS_ROOT + "remove", true),
	UNLINK(Commands.UNLINK, Constants.PERMISSIONS_ROOT + "unlink", true),
	VERSION(Commands.VERSION, Constants.PERMISSIONS_ROOT + "version", false),

	// ACTIONS
	OPEN_ADMIN_CHEST(Messages.ACTION_OPEN_CHEST_ADMIN, Constants.PERMISSIONS_ROOT + "openadminchest", true),
	OPEN_MASTER_CHEST(Messages.ACTION_OPEN_MASTER_CHEST, Constants.PERMISSIONS_ROOT + "openmasterchest", false),
	OPEN_LINKED_CHEST(Messages.ACTION_OPEN_LINKED_CHEST, Constants.PERMISSIONS_ROOT + "openlinkedchest", false),
	PLACE_LINKED_SIGN(Messages.ACTION_PLACE_LINKED_SIGN, Constants.PERMISSIONS_ROOT + "placelinkedsign", true);

	public Commands command;
	public String node;
	public boolean isAdminCommand;
	public Messages actionMessage;

	public static HashMap<Commands, PermissionsNodes> permsCommandsMap = new HashMap<Commands, PermissionsNodes>();

	PermissionsNodes(final Commands cmd, final String node, final boolean isAdminCommand)
	{
		this.command = cmd;
		this.node = node;
		this.isAdminCommand = isAdminCommand;
		this.actionMessage = null;
	}

	PermissionsNodes(final Messages actionMessage, final String node, final boolean isAdminCommand)
	{
		this.command = null;
		this.node = node;
		this.isAdminCommand = isAdminCommand;
		this.actionMessage = actionMessage;
	}

	static
	{
		for (final PermissionsNodes permNode : PermissionsNodes.values())
		{
			if (permNode.command != null)
			{
				permsCommandsMap.put(permNode.command, permNode);
			}
		}
	}

	public static PermissionsNodes getPermissionNodeForCommand(final Commands cmd)
	{
		return permsCommandsMap.get(cmd);
	}
}
