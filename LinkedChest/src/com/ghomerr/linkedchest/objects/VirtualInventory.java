package com.ghomerr.linkedchest.objects;

import java.util.HashSet;

import org.bukkit.Location;

import com.ghomerr.linkedchest.constants.Constants;
import com.ghomerr.linkedchest.enums.Commands;
import com.ghomerr.linkedchest.utils.StringUtils;

public class VirtualInventory
{
	//public Inventory inventory = null;
	
	public Location singleLocation = null;
	public Location doubleLocation = null;
	
	public String singleShortLoc = null;
	public String doubleShortLoc = null;
	
	public boolean isDoubleChest = false;
	public boolean isAdmin = false;
	
	public HashSet<String> linkedChests = new HashSet<String>();
	
	public VirtualInventory(final Location loc, final boolean isAdmin)
	{
		super();
		this.singleLocation = loc;
		this.singleShortLoc = StringUtils.printShortLocation(loc);
		this.isAdmin = isAdmin;
	}
	
//	public VirtualInventory(final Inventory inv, final Location loc, final boolean isAdmin)
//	{
//		this(loc, isAdmin);
//		//this.inventory = inv;
//		this.isAdmin = isAdmin;
//	}
	
	public VirtualInventory(/*final Inventory inv, */final Location singleLoc, final Location doubleLoc, final boolean isAdmin)
	{
		this(/*inv, */singleLoc, isAdmin);
		this.doubleLocation = doubleLoc;
		this.doubleShortLoc = StringUtils.printShortLocation(doubleLoc);
		this.isDoubleChest = true;
	}
	
	public String getDataString()
	{
		final StringBuilder strBld = new StringBuilder();
		
		strBld.append(StringUtils.printShortLocation(singleLocation));
		if (isAdmin)
		{
			strBld.append(Constants.OPTION_DELIM).append(Commands.ADMIN_CHEST.option);
		}
		if (isDoubleChest)
		{
			strBld.append(Constants.SEPARATOR).append(StringUtils.printShortLocation(doubleLocation));
		}
		
		return strBld.toString();
	}
}
