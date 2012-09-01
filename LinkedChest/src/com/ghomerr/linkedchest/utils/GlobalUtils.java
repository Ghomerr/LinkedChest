package com.ghomerr.linkedchest.utils;

public class GlobalUtils
{
	public static boolean arrayIsNotEmpty(final Object[] array)
	{
		if (array != null && array.length > 0)
		{
			for (final Object el : array)
			{
				if (el != null)
				{
					return true;
				}
			}
		}	
		return false;
	}
	
	public static boolean arrayIsEmpty(final Object[] array)
	{
		return !arrayIsNotEmpty(array);
	}
}
