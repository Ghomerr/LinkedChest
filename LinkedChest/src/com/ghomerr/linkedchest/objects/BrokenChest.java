package com.ghomerr.linkedchest.objects;

import com.ghomerr.linkedchest.enums.BrokenChestType;

public class BrokenChest
{
	public BrokenChestType type;
	public String name;
	
	public BrokenChest()
	{
		this.type = BrokenChestType.NORMAL_CHEST;
		this.name = null;
	}
	
	public BrokenChest(final BrokenChestType type, final String name)
	{
		this.type = type;
		this.name = name;
	}
}
