package com.ghomerr.linkedchest.enums;

public enum Messages
{
	HELP_MAIN("Main command: /linkedchest or /linkchest or /lchest or /lc"),
	HELP_ADMINS("Default admins commands: add<n><o> | admin<n> | config <p><v> | move<n> | open<n> | remove<n> ; <n> = chest name  ; <o> = options (a=admin chest) ; <p><v> = conf param and/or value"),
	HELP_PLAYERS("Default players commands: alias<c> | details<n> | list | help<c> | version ; <n> = chest name ; <c> = command"),
	HELP_CHEST("The chest name can contains: a-z, A-Z, 0-9, _ et -, for 1 to 20 characters."),
	HELP_INVALID_CMD("Invalid help command."),

	HELP_ADD("Create a master chest from the targeted chest by specifying its name and options. Example: /lc add myChest a ; (a=admin chest)"),
	HELP_ADMIN_CHEST("Chest option and command to modify the admin status of a chest. Example: /lc admin myChest"),
	HELP_ALIASES("Display all aliases of the command. Example: /lc alias add"),
	HELP_CONFIG("Display the current plugin configuration or update a given configuration parameter. Example: /lc config language en, /lc config debug, /lc config"),
	HELP_DETAILS("Display chest details. Example: /lc details myChest"),
	HELP_HELP("Display the main help when used without parameters or the help for a specified command. Example: /lc help help"),
	HELP_LINK("Link the targeted chest to a master chest. Example: /lc link myChest"),
	HELP_LINKED_CHEST("Main command of the plugin used before all other commands. Example: /lc help"),
	HELP_LIST("Display the available linked chests or those which start with given letters. Example: /lc list a"),
	HELP_MOVE("Move the given master chest to the targeted empty chest. Example: /lc move myChest"),
	HELP_OPEN("Remotely open a master chest. Example: /lc open myChest"),
	HELP_REMOVE("Remove the link to a master chest. Example: /lc del myChest"),
	HELP_UNLINK("Unlink the targeted chest or unlink all chests linked to the given master chest. Example: /lc unlink myChest"),
	HELP_VERSION("Display the plugin version. Example: /lc version"),

	ALIASES_AVAILABLE(1, "Available aliases for the command {command}: "),
	ALIASES_UNKNOWN_COMMAND(1, "Invalid aliases request: {command} is unknown."),

	ACTION_OPEN_CHEST_ADMIN("You are not allowed to open an admin chest."),
	ACTION_OPEN_MASTER_CHEST("You are not allowed to open a master chest."),
	ACTION_OPEN_LINKED_CHEST("You are not allowed to open a linked chest."),
	ACTION_PLACE_LINKED_SIGN("You are not allowed to place a sign to link a chest."),

	MASTER_CHEST_ADDED(1, "The master chest {chest} has been added."),
	INVALID_CHEST_NAME(1, "Invalid chest name : {chest}"),
	CHEST_ALREADY_EXISTS(1, "The chest {chest} already exists."),
	REMOTE_OPENING_CHEST(1, "Remotely opening the chest {chest}..."),
	OPEN_CHEST_FAILED(1, "Unable to open the chest {chest}."),
	CHEST_REMOVED(1, "The chest {chest} has been removed."),
	REMOVE_CHEST_FAILED(1, "Unable to remove the chest {chest}."),
	CONFIG_LIST("Current configuration: "),
	AVAILABLE_CHESTS("Available chests: "),
	FOUND_CHESTS("Found chests: "),
	OPENING_MASTER_CHEST(1, "Opening master chest {chest}..."),
	OPENING_LINKED_CHEST(1, "Opening linked chest {chest}..."),
	CANNOT_OPEN_WITH_BLOCK("You cannot open a linked chest with a block in hand."),
	MASTER_CHEST_UNBREAKABLE(1, "You cannot destroy the master chest {chest} without removing it previously."),
	MASTER_CHEST_MOVE_SUCCESS(1, "The master chest {c} has been correclty moved."),
	MASTER_CHEST_MOVE_FAILURE(1, "The master chest {c} has not been correctly moved."),
	DEBUG_ENABLED("Debug enabled."),
	DEBUG_DISABLED("Debug disabled."),
	CHEST_IS_ADMIN(1, "The chest {chest} is now restricted to admins."),
	CHEST_ISNOT_ADMIN(1, "The chest {chest} is not restricted to admins anymore."),
	CHEST_ADMIN_FAILED(1, "Unable to modify the admin status of the chest {chest}."),
	CHEST_DETAILS(1, "Details of chest {chest}: "),
	CHEST_NOT_FOUND(1, "The chest {chest} doesn't exist."),
	UNKNOWN_COMMAND("The command {command} doesn't exist."),
	COMMAND_NOT_ALLOWED(1, "You are not allowed to use the command {command}."),
	PERMISSIONS_ENABLED("Permissions enabled."),
	PERMISSIONS_DISABLED("Permissions disabled."),
	TARGET_CHEST_ALREADY_MASTER(1, "You cannot do that. The targeted chest is the master chest {chest}."),
	TARGET_CHEST_ALREADY_LINKED(2, "Unable to add the chest {chest1}. The targeted chest is linked to the chest {chest2}."),
	TARGET_CHEST_NOT_EMPTY("The targeted chest is not empty."),
	TARGET_CHEST_SIZE_INVALID("The targeted chest has not the same size as the chosen master chest."),
	CANNOT_PLACE_CHEST_NEARBY(1, "You cannot place a chest nearby the master chest {chest}."),
	CURRENT_VERSION(1, "Plugin current version: {version}"),
	TARGET_BLOCK_ISNOT_CHEST("Target block is not a chest."),
	CANNOT_LINK_MASTER_CHEST(1, "You cannot link the master chest {chest}."),
	CANNOT_UNLINK_MASTER_CHEST(1, "You cannot unlink the master chest {chest}."),
	CANNOT_LINK_LINKED_CHEST(1, "The targeted chest is already linked to the chest {chest}. "),
	CANNOT_UNLINK_CHEST("You cannot unlink this chest as it is not linked."),
	LINK_CHEST_FAIL(1, "Unable to link the targeted chest to the chest {chest}."),
	LINK_CHEST_SUCCESS(1, "The target chest has been linked to the chest {chest}."),
	UNLINK_CHEST_FAIL("Unable to unlink the targeted chest."),
	UNLINK_CHEST_SUCCESS(1, "The targeted chest is no more linked to the chest {chest}."),
	UNLINK_CHESTS_FAIL(1, "Unable to remove the links of the chest {chest}."),
	UNLINK_CHESTS_SUCCESS(1, "All links of the chest {chest} have been removed."),
	BREAK_LINK_CHEST(1, "You have broken a chest linked to the chest {chest}."),
	CONFIGURATION_UPDATED("Configuration updated: "),
	CONFIGURATION_PARAM_UNKNOWN(1, "Configuration parameter {param} is unknown."),
	CONFIGURATION_VALUE_INVALID(2, "The value {value} for the configuration parameter {param} is invalid."),
	CONFIGURATION_UPDATE_FAILED(2, "Unable to update the configuration parameter {param} with value {value}."),
	CONFIGURATION_VALUE_NEEDED(1, "The configuration parameter {param} needs a value to be updated.");

	public String message;
	public int varNumber;

	Messages(final String message)
	{
		this.message = message;
		this.varNumber = 0;
	}

	Messages(final int varNumber, final String message)
	{
		this.message = message;
		this.varNumber = varNumber;
	}
}
