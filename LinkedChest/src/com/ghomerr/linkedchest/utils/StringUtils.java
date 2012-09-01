package com.ghomerr.linkedchest.utils;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.ghomerr.linkedchest.constants.Constants;

public class StringUtils
{
	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);
	public static boolean isDebugEnabled = false;

	public static boolean isBlank(final String str)
	{
		return (str == null || str.isEmpty() || str.matches(Constants.BLANK_STRING_PATTERN));
	}

	public static boolean isNotBlank(final String str)
	{
		return !isBlank(str);
	}

	public static boolean isValidChestName(final String chestName)
	{
		return isNotBlank(chestName) && chestName.matches(Constants.CHEST_NAME_PATTERN);
	}

	public static String printLocation(final Location loc)
	{
		if (loc != null)
		{
			final StringBuilder strBld = new StringBuilder();

			strBld.append(loc.getWorld().getName()).append(Constants.DELIMITER).append(loc.getBlockX())
					.append(Constants.DELIMITER).append(loc.getBlockY()).append(Constants.DELIMITER)
					.append(loc.getBlockZ()).append(Constants.DELIMITER).append(loc.getYaw())
					.append(Constants.DELIMITER).append(loc.getPitch());

			return strBld.toString();
		}
		else
		{
			return Constants.EMPTY_STRING;
		}
	}

	public static String printShortLocation(final Location loc)
	{
		if (loc != null)
		{
			final StringBuilder strBld = new StringBuilder();

			strBld.append(loc.getWorld().getName()).append(Constants.DELIMITER).append(loc.getBlockX())
					.append(Constants.DELIMITER).append(loc.getBlockY()).append(Constants.DELIMITER)
					.append(loc.getBlockZ());
			return strBld.toString();
		}
		else
		{
			if (DebugUtils.isDebugEnabled())
			{
				_LOGGER.warning(Constants.TAG + "Location to print is null.");
			}
			return Constants.EMPTY_STRING;
		}
	}

	public static Location parseLocation(final JavaPlugin plugin, final String str)
	{
		Location loc = null;

		if (isNotBlank(str))
		{
			String[] split = str.split(Constants.DELIMITER);

			if (split.length >= 4)
			{
				final String worldName = split[0];
				final World world = plugin.getServer().getWorld(worldName);

				if (world != null)
				{
					try
					{
						final Double posX = Double.parseDouble(split[1]);
						final Double posY = Double.parseDouble(split[2]);
						final Double posZ = Double.parseDouble(split[3]);
						final float yaw = 0f;
						final float pitch = 0f;

						loc = new Location(world, posX, posY, posZ, yaw, pitch);
					}
					catch (final Throwable th)
					{
						_LOGGER.severe("Error while parsing location " + str);
						th.printStackTrace();
					}
				}
				else
				{
					_LOGGER.severe("Unknown world " + worldName);
				}
			}
		}
		return loc;
	}
	
	public static int countChar(final String sourceString, final char charToCount)
	{
		int numberOfCharToCount = 0;
		
		for (int i = 0; i < sourceString.length() ; i++)
		{
			final char c = sourceString.charAt(i);
			if (charToCount == c)
			{
				numberOfCharToCount++;
			}
		}
		
		return numberOfCharToCount;
	}
}
