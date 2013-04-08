package com.ghomerr.linkedchest.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class WorldUtils
{
	//private static final Logger LOGGER = Logger.getLogger(Constants.MINECRAFT);
	//public static boolean isDebugEnabled = false;
	
//	public static String getVirtualChestNameFromSign (final LinkedChest plugin, final Block chest)
//	{
//		String chestName = null;
//		Sign sign = null;
//		Block upperBlock = null;
//		Block underBlock = null;
//
//		for (final BlockFace face : BlockFace.values())
//		{
//			Block adjacent = null;
//
//			switch (face)
//			{
//				case NORTH:
//				case NORTH_EAST:
//				case EAST:
//				case SOUTH_EAST:
//				case SOUTH:
//				case SOUTH_WEST:
//				case WEST:
//				case NORTH_WEST:
//					adjacent = chest.getRelative(face);
//					break;
//				case UP:
//					upperBlock = chest.getRelative(face);
//					adjacent = upperBlock;
//					break;
//				case DOWN:
//					underBlock = chest.getRelative(face);
//					adjacent = chest.getRelative(face, 2);
//					break;
//			}
//
//			if (isSign(adjacent))
//			{
//				sign = (Sign) adjacent.getState();
//				chestName = getChestNameOnSign(sign);
//				if (chestName != null && plugin.hasChestName(chestName))
//				{
//					return chestName;
//				}
//			}
//		}
//
//		if (sign == null && (upperBlock != null || underBlock != null))
//		{
//			for (final BlockFace face : BlockFace.values())
//			{
//				Block upperAdjacentBlock = null;
//				Block underAdjacentBlock = null;
//				
//				switch (face)
//				{
//					case NORTH:
//					case EAST:
//					case SOUTH:
//					case WEST:
//						if (upperBlock != null)
//						{
//							upperAdjacentBlock = upperBlock.getRelative(face);
//						}
//						if (underBlock != null)
//						{
//							underAdjacentBlock = underBlock.getRelative(face);
//						}
//						break;
//				}
//				
//				if (isSign(upperAdjacentBlock))
//				{
//					sign = (Sign) upperAdjacentBlock.getState();
//					chestName = getChestNameOnSign(sign);
//					if (chestName != null && plugin.hasChestName(chestName))
//					{
//						return chestName;
//					}
//				}
//				
//				if (isSign(underAdjacentBlock))
//				{
//					sign = (Sign) underAdjacentBlock.getState();
//					chestName = getChestNameOnSign(sign);
//					if (chestName != null && plugin.hasChestName(chestName))
//					{
//						return chestName;
//					}
//				}
//			}
//		}
//
//		return null;
//	}

	public static boolean isSign(final Block block)
	{
		return block != null && 
				(block.getType() == Material.SIGN 
				|| block.getType() == Material.SIGN_POST
				|| block.getType() == Material.WALL_SIGN);
	}
	
	public static boolean isChest(final Block block)
	{
		//LOGGER.info("[LC-Debug] Block type: " + block.getType());
		final Material blockType = block.getType();
		return block != null && 
				(blockType == Material.CHEST 
				|| blockType == Material.TRAPPED_CHEST);
	}
	
	public static boolean areSameChests(final Block b1, final Block b2)
	{
		if (b1 != null && b2 != null)
		{
			final Material t1 = b1.getType(), t2 = b2.getType();
			return t1 == t2 
				&& (t1 == Material.CHEST 
				|| t1 == Material.TRAPPED_CHEST);
		}
		else
		{
			return false;
		}
	}
	
	public static <T> T getSameChestNearby(final Block block, final Class<T> returnedType)
	{
		for (final BlockFace face : BlockFace.values())
		{
			switch(face)
			{
				case NORTH:
				case EAST:
				case SOUTH:
				case WEST:
				{
					final Block nearbyBlock = block.getRelative(face);
					if (areSameChests(block, nearbyBlock))
					{
						if (returnedType == Block.class)
						{
							return returnedType.cast(nearbyBlock);
						}
						else if (returnedType == Location.class)
						{
							return returnedType.cast(nearbyBlock.getLocation());
						}
					}
				}
			}
		}
		return null;
	}
	
//	public static String getChestNameOnSign(final Sign sign)
//	{
//		String chestName = null;
//		
//		if (sign != null)
//		{
//			chestName = getChestNameOnSign(sign.getLines());
//		}
//		
//		return chestName;
//	}
	
//	public static String getChestNameOnSign(final String[] lines)
//	{
//		String chestName = null;
//		final String firstLine = lines[0];
//
//		final int startIndex = firstLine.indexOf(Constants.CHEST_NAME_TAG);
//		final int endIndex = firstLine.lastIndexOf(Constants.CHEST_NAME_TAG);
//
//		if (startIndex >= 0 && endIndex > 0)
//		{
//			chestName = firstLine.substring(startIndex + 1, endIndex);
//			if (StringUtils.isBlank(chestName))
//			{
//				return null;
//			}
//			else
//			{
//				chestName = chestName.toLowerCase();
//			}
//		}
//
//		return chestName;
//	}
}
