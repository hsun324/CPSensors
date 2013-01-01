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
package hsun324.cpsensors.sensors.handlers;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import hsun324.cpsensors.sensors.ISensorHandler;
import hsun324.cpsensors.tile.TileBlockSensor;

public class InventorySensorHandler implements ISensorHandler
{
	public final IInventory inventory;
	public InventorySensorHandler(IInventory inventory)
	{
		this.inventory = inventory;
	}
	
	@Override
	public Map<String, Object> getData(TileBlockSensor caller)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("slotCapacity", inventory.getSizeInventory());
		
		int slotsOpen = 0;
		for(int i = 0; i < inventory.getSizeInventory(); i++)
			if(inventory.getStackInSlot(i) == null)
				++slotsOpen;
		dataMap.put("slotUnused", slotsOpen);

		for(int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack stack = inventory.getStackInSlot(i);
			Map<String, Object> itemData = new HashMap<String, Object>();
			itemData.put("itemId", stack == null?0:stack.itemID);
			itemData.put("itemMeta", stack == null?0:stack.getItemDamage());
			itemData.put("itemCount", stack == null?0:stack.stackSize);
			
			dataMap.put("slot" + (i + 1), itemData);
		}
		return dataMap;
	}
}
