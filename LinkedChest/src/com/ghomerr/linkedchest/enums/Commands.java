package com.ghomerr.linkedchest.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import com.ghomerr.linkedchest.constants.Constants;

public enum Commands
{
	ADD(Messages.HELP_ADD, new String[] {"ad", "add"}),

	ADMIN_CHEST(Messages.HELP_ADMIN_CHEST, new String[] {"ac", "achest", "admin", "admchest", "adminchest"}, "a"),

	ALIASES(Messages.HELP_ALIASES, new String[] {"al", "alias", "aliases"}),

	CONFIG(Messages.HELP_CONFIG, new String[] {"cf", "conf", "config", "configuration"}),

	DETAILS(Messages.HELP_DETAILS, new String[] {"dt", "det", "detail", "details"}),

	HELP(Messages.HELP_HELP, new String[] {"hp", "help"}),

	LINK(Messages.HELP_LINK, new String[] {"ln", "link"}),

	LINKED_CHEST(Messages.HELP_LINKED_CHEST, new String[] {"lc", "lchest", "linkchest", "linkedchest"}),

	LIST(Messages.HELP_LIST, new String[] {"ls", "li", "list", "liste"}),

	MOVE(Messages.HELP_MOVE, new String[] {"mv", "move"}),

	OPEN(Messages.HELP_OPEN, new String[] {"op", "open"}),
	
	POSITIONS(Messages.HELP_POSITIONS, new String[] {"ps", "po", "pos", "position", "positions"}),

	UNLINK(Messages.HELP_UNLINK, new String[] {"ul", "un", "ulink", "unlink", "fr", "free"}),

	REMOVE(Messages.HELP_REMOVE, new String[] {"rm", "dl", "rem", "del", "remove", "delete"}),

	VERSION(Messages.HELP_VERSION, new String[] {"vr", "ver", "version"});

	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);

	public static HashMap<String, Commands> commands = new HashMap<String, Commands>();

	public HashSet<String> list;
	public String option;
	public Messages helpMessage;

	Commands(final Messages help, final String[] tab)
	{
		this.helpMessage = help;
		this.list = new HashSet<String>();
		for (String cmd : tab)
		{
			this.list.add(cmd);
		}
	}

	Commands (final Messages help, final String[] tab, final String option)
	{
		this(help, tab);
		this.option = option;
	}

	static
	{
		for (final Commands cmd : Commands.values())
		{
			for (final String str : cmd.list)
			{
				commands.put(str, cmd);
			}
		}
	}

	public boolean has(final String cmd)
	{
		return list.contains(cmd.toLowerCase());
	}

	public boolean contains(final String optionList)
	{
		return optionList.contains(this.option);
	}

	public static void registerMainCommand(final JavaPlugin plugin, final CommandExecutor cmdExec)
	{
		for (final String cmd : Commands.LINKED_CHEST.list)
		{
			final PluginCommand pluginCmd = plugin.getCommand(cmd);
			if (pluginCmd != null)
			{
				pluginCmd.setExecutor(cmdExec);
			}
			else
			{
				_LOGGER.severe(Constants.TAG + "Command " + cmd + " could not be added.");
			}
		}
	}
}
