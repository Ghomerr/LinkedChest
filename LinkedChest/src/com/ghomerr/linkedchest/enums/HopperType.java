package com.ghomerr.linkedchest.enums;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public enum HopperType
{
	BLOCK(Block.class),
	
	MINECART(Entity.class),
	
	OTHER;
	
	public Class<?> type;
	
	HopperType(final Class<?> type)
	{
		this.type = type;
	}
	
	HopperType()
	{
		type = null;
	}
}
