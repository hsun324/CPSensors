/*
 *  Copyright (C) 2012 Henry Sun
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package hsun324.cpsensors.sensors;

import java.util.ArrayList;
import java.util.List;

import buildcraft.api.power.IPowerProvider;

import forestry.api.apiculture.IBeeHousing;
import hsun324.cpsensors.sensors.handlers.*;
import ic2.api.IEnergyStorage;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;

public class TargetType
{
	private static List<TargetType> types = new ArrayList<TargetType>();
	public static TargetType INVALID;
	public static TargetType TIME;
	
	public static void initTypes()
	{
		TIME = new TargetType("TIME", "Time", false, null, TimeSensorHandler.class);
		INVALID = new TargetType("INVALID", "Invalid", null, null);
		
		try
		{
			new TargetType("ICENERGYSTORAGE", "IC2 Energy Storage", IEnergyStorage.class, ICEnergyStorageSensorHandler.class);
		}
		catch(NoClassDefFoundError e) { };
		try
		{
			new TargetType("BEEHOUSING", "Forestry Bee Housing", IBeeHousing.class, BeeHousingSensorHandler.class);
		}
		catch(NoClassDefFoundError e) { };
		try
		{
			new TargetType("BCPOWERPROVIDER", "BC3 Power Provider / Engine", IPowerProvider.class, BCPowerProviderSensorHandler.class);
		}
		catch(NoClassDefFoundError e) { };

		new TargetType("LIQUIDTANK", "Liquid Tank", ILiquidTank.class, LiquidTankSensorHandler.class);
		new TargetType("LIQUIDCONTAINER", "Liquid Container", ITankContainer.class, TankContainerSensorHandler.class);
		new TargetType("INVENTORY", "Inventory", IInventory.class, InventorySensorHandler.class);
	}
	
	private final String name;
	private final String description;
	private final boolean reqTarget;
	private final Class<?> identifier;
	private final Class<? extends ISensorHandler> handler;
	
	private TargetType(String name, String description, Class<?> identifier, Class<? extends ISensorHandler> handler)
	{
		this(name, description, true, identifier, handler);
	}
	private TargetType(String name, String description, boolean reqTarget, Class<?> identifier, Class<? extends ISensorHandler> handler)
	{
		this.name = name;
		this.description = description;
		this.reqTarget = reqTarget;
		this.identifier = identifier;
		this.handler = handler;
		types.add(this);
	}
	
	public String name()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public boolean requiresTarget()
	{
		return reqTarget;
	}


	public Class<?> getIdentifier()
	{
		return identifier;
	}
	
	public Class<? extends ISensorHandler> getHandler()
	{
		return handler;
	}
	
	public static TargetType valueOf(String name)
	{
		for(TargetType type : types)
			if(type.name.equalsIgnoreCase(name))
				return type;
		return null;
	}
	
	public static TargetType[] values()
	{
		return types.toArray(new TargetType[0]);
	}
}