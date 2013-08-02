package com.ghomerr.linkedchest.utils;

import java.util.logging.Logger;

import com.ghomerr.linkedchest.constants.Constants;

public final class GlobalUtils
{
	private static final Logger _LOGGER = Logger.getLogger(Constants.MINECRAFT);
	
	public static int arrayLength(final Object[] array)
	{
		int length = 0;
		if (array != null && array.length > 0)
		{
			for (final Object el : array)
			{
				if (el != null)
				{
					length++;
				}
			}
		}	
		return length;
	}
	
	public static boolean arrayIsEmpty(final Object[] array)
	{
		return arrayLength(array) == 0;
	}
	
	public static boolean copyArray(final Object[] source, final Object[] dest)
	{
		final int sourceLength = arrayLength(source);
		if (sourceLength <= dest.length) 
		{
			int cursor = 0;
			for (Object o : source.clone())
			{
				if (o != null)
				{
					dest[cursor] = o;
					cursor++;
				}
			}
			return true;
		}
		else
		{
			_LOGGER.warning("Cannot copy the source array to the dest array because it contains more element than the dest can bear.");
			return false;
		}
	}
}
