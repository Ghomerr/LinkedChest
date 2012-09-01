package com.ghomerr.linkedchest.utils;

import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import com.ghomerr.linkedchest.constants.Constants;
import com.ghomerr.linkedchest.enums.Configurations;

public class ConfigurationUtils
{
	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);

	private static Object _configMonitor = new Object();
	private static Properties _configData = null;
	private static HashMap<Configurations, Object> _configMap = new HashMap<Configurations, Object>();

	public static boolean loadConfiguration()
	{
		_configData = FileUtils.loadData(Constants.CONFIG_FILE_PATH);
		if (_configData != null)
		{
			for (final Configurations conf : Configurations.values())
			{
				final String value = _configData.getProperty(conf.name);
				if (StringUtils.isNotBlank(value))
				{
					if (conf.type == Boolean.class)
					{
						_configMap.put(conf, new Boolean(Boolean.parseBoolean(value)));
					}
					else
					{
						_configMap.put(conf, value);
					}
					_LOGGER.info(Constants.TAG + "Configuration '" + conf.name + "' = " + value);
				}
				else
				{
					_LOGGER.warning(Constants.TAG + "Configuration '" + conf.name + "' not found. Using default value : "
							+ conf.defaultValue);
					_configMap.put(conf, conf.defaultValue);
				}
			}
		}
		else
		{
			_LOGGER.severe(Constants.TAG + "Configuration file failed to be loaded.");
		}

		if (Configurations.values().length == _configMap.size())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static <T> T getConfigValueByType(final Configurations conf, final Class<T> type)
	{
		Object value = null;
		synchronized(_configMonitor)
		{
			value = _configMap.get(conf);
		}
		
		if (value != null)
		{
			return type.cast(value);
		}
		else
		{
			return null;
		}
	}

	public static <T> boolean setConfigValue(final Configurations conf, final T value)
	{
		if (conf.type.isInstance(value))
		{
			synchronized(_configMonitor)
			{
				_configMap.put(conf, value);
				_configData.put(conf.name, String.valueOf(value));
				FileUtils.saveFile(_configData, Constants.CONFIG_FILE_PATH);
			}
			return true;
		}
		else
		{
			_LOGGER.warning(Constants.TAG + "Input value '" + value + "' is not of type : " + conf.type);
			return false;
		}
	}
	
	public static Boolean toggleBooleanValue(final Configurations conf)
	{
		Boolean ret = null;
		
		if (conf.type == Boolean.class)
		{
			ret = setConfigValue(conf, !getConfigValueByType(conf, Boolean.class));
		}
		else
		{
			_LOGGER.warning(Constants.TAG + "Configuration " + conf.name + " is not of Boolean type.");
		}
			
		return ret;
	}
	
	public static void saveConfiguration()
	{
		synchronized(_configMonitor)
		{
			FileUtils.saveFile(_configData, Constants.CONFIG_FILE_PATH);
		}
	}
}
