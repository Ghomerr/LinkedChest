package com.ghomerr.linkedchest.enums;

import java.util.HashMap;

import com.ghomerr.linkedchest.constants.Constants;

public enum Configurations
{
	DEBUG("debug", Boolean.class, Boolean.FALSE),
	
//	HOPPERS("hoppers", Boolean.class, Boolean.TRUE),
	
	USE_UTF8("useutf8", Boolean.class, Boolean.FALSE),
	
	DISPLAY_OPEN_MESSAGE("displayopenmessage", Boolean.class, Boolean.TRUE),
	
	LANGUAGE("language", String.class, Constants.DEFAULT_LANGUAGE),
	
	SAVEALL_PERIOD("saveallperiod", String.class, Constants.DISABLED),
	
	USE_PERMISSIONS("usepermissions", Boolean.class, Boolean.FALSE);
	
	public String name;
	public Class<?> type;
	public Object defaultValue;
	
	public static HashMap<String, Configurations> list = new HashMap<String, Configurations>();
	
	Configurations(final String name, final Class<?> type, final Object defaultValue)
	{
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
	
	static
	{
		for (final Configurations conf : Configurations.values())
		{
			list.put(conf.name, conf);
		}
	}
	
	public <T> T getDefaultValueByType(final Class<T> type)
	{
		if (this.type == type)
		{
			return type.cast(defaultValue);
		}
		else
		{
			return null;
		}
	}
}
