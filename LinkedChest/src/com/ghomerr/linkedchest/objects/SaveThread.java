package com.ghomerr.linkedchest.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.ghomerr.linkedchest.constants.Constants;
import com.ghomerr.linkedchest.utils.FileUtils;

public class SaveThread implements Runnable
{
	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);
	
	private Properties _data = null;
	private String _fileName = null;
	
	public SaveThread(final Properties data, final String fileName)
	{
		_data = data;
		_fileName = fileName;
	}
	
	@Override
	public void run()
	{
		File file = new File(_fileName);

		if (!file.exists())
		{
			file = FileUtils.createNewFile(_fileName);
			if (file == null || !file.exists())
			{
				_LOGGER.severe(Constants.TAG + "Unable to create file " + _fileName);
			}
		}
		
		if (_data != null && !_data.isEmpty())
		{
			FileOutputStream out = null;
			try
			{
				out = new FileOutputStream(file);
			}
			catch (final FileNotFoundException ex)
			{
				_LOGGER.severe(Constants.TAG + "File " + _fileName + " not found. ");
				ex.printStackTrace();
			}

			try
			{
				_data.store(out, null);
				out.close();
			}
			catch (final IOException ex)
			{
				_LOGGER.severe(Constants.TAG + _fileName + " file update failed !");
				ex.printStackTrace();
			}
		}
		else
		{
			_LOGGER.info(Constants.TAG + "No data to save in " + _fileName);
		}
	}
}
