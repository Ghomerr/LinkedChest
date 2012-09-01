package com.ghomerr.linkedchest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.ghomerr.linkedchest.constants.Constants;

public class FileUtils
{
	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);
	public static boolean isDebugEnabled = false;
	
	public static void saveFile(final Properties data, final String fileName)
	{
		if (DebugUtils.isDebugEnabled())
		{
			_LOGGER.info(Constants.TAG + "Start Saving file '" + fileName + "'...");
		}
		File file = new File(fileName);

		if (!file.exists())
		{
			if (DebugUtils.isDebugEnabled())
			{
				_LOGGER.info(Constants.TAG + "File '" + fileName + "' does not exist. Trying to create it.");
			}
			file = FileUtils.createNewFile(fileName);
			if (file == null || !file.exists())
			{
				_LOGGER.severe(Constants.TAG + "Unable to create file " + fileName);
			}
		}
		
		if (data != null && !data.isEmpty())
		{
			FileOutputStream out = null;
			try
			{
				out = new FileOutputStream(file);
			}
			catch (final FileNotFoundException ex)
			{
				_LOGGER.severe(Constants.TAG + "File " + fileName + " not found. ");
				ex.printStackTrace();
			}

			try
			{
				if (DebugUtils.isDebugEnabled())
				{
					_LOGGER.info(Constants.TAG + "Saving data in '" + fileName + "'...");
				}
				data.store(out, null);
				out.close();
			}
			catch (final IOException ex)
			{
				_LOGGER.severe(Constants.TAG + fileName + " file update failed !");
				ex.printStackTrace();
			}
		}
		else
		{
			if (DebugUtils.isDebugEnabled())
			{
				_LOGGER.info(Constants.TAG + "No data to save in " + fileName);
			}
		}
	}
	
	public static Properties loadData(final String fileName)
	{
		if (DebugUtils.isDebugEnabled())
		{
			_LOGGER.info(Constants.TAG + "Start loading file " + fileName);
		}
		
		final File pluginDir = new File(Constants.PLUGIN_DIR_PATH);		
		if (!pluginDir.exists())
		{
			if (DebugUtils.isDebugEnabled())
			{
				_LOGGER.info(Constants.TAG + "Plugin dir does not exist. Trying to create it.");
			}
			try
			{
				pluginDir.mkdir();
				pluginDir.setExecutable(true, false);
				pluginDir.setReadable(true, false);
				pluginDir.setWritable(true, false);
			}
			catch (final Throwable th)
			{
				_LOGGER.severe(Constants.TAG + "Unable to create plugin dir " + Constants.PLUGIN_DIR_PATH);
				th.printStackTrace();
				return null;
			}
		}
		
		final Properties data = new Properties();
		File dataFile = new File(fileName);
		
		if (!dataFile.exists())
		{
			if (DebugUtils.isDebugEnabled())
			{
				_LOGGER.info(Constants.TAG + "File '" + fileName + "' does not exist. Trying to create it.");
			}
			dataFile = createNewFile(fileName);
			if (dataFile == null || !dataFile.exists())
			{
				_LOGGER.severe(Constants.TAG + "Unable to find or create file " + fileName);
				return null;
			}
		}		
		
		FileInputStream in = null;
		try
		{
			in = new FileInputStream(dataFile);
		}
		catch (final Throwable ex)
		{
			_LOGGER.severe(Constants.TAG + "Unable to create a stream to read the file " + fileName);
			ex.printStackTrace();
			return null;
		}
	
		try
		{
			if (DebugUtils.isDebugEnabled())
			{
				_LOGGER.info(Constants.TAG + "Loading file '" + fileName + "'...");
			}
			data.load(in);
			in.close();
			return data;
		}
		catch (final IOException ex)
		{
			_LOGGER.severe(Constants.TAG + "Error while loading the file " + fileName);
			ex.printStackTrace();
			return null;
		}
	}
	
	public static File createNewFile(final String fileName)
	{
		if (DebugUtils.isDebugEnabled())
		{
			_LOGGER.info(Constants.TAG + "Creating file '" + fileName + "'...");
		}
		final File file = new File(fileName);
		
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
				file.setExecutable(true, false);
				file.setReadable(true, false);
				file.setWritable(true, false);
				return file;
			}
			catch (final Throwable th)
			{
				_LOGGER.severe(Constants.TAG + "Error while creating file " + fileName);
				th.printStackTrace();
				return null;
			}
		}
		else
		{
			if (DebugUtils.isDebugEnabled())
			{
				_LOGGER.info(Constants.TAG + "File '" + fileName + "' already exists.");
			}
			return file;
		}
	}
}
