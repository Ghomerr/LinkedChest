package com.ghomerr.linkedchest.utils;

import com.ghomerr.linkedchest.enums.Configurations;

public class DebugUtils
{
	public static boolean isDebugEnabled()
	{
		final Boolean bool = ConfigurationUtils.getConfigValueByType(Configurations.DEBUG, Boolean.class);
		if (bool != null)
		{
			return bool.booleanValue();
		}
		else
		{
			return Configurations.DEBUG.getDefaultValueByType(Boolean.class);
		}
	}

	public static boolean toggleDebug()
	{
		return ConfigurationUtils.toggleBooleanValue(Configurations.DEBUG);
	}

	public static void setDebugEnabled(final boolean state)
	{
		final Boolean bool = new Boolean(state);
		ConfigurationUtils.setConfigValue(Configurations.DEBUG, bool);
	}
}
