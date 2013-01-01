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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;

import hsun324.cpsensors.sensors.ISensorHandler;
import hsun324.cpsensors.tile.TileBlockSensor;

public class TankContainerSensorHandler implements ISensorHandler
{
	public final ITankContainer tankContainer;
	public TankContainerSensorHandler(ITankContainer tankContainer)
	{
		this.tankContainer = tankContainer;
	}

	@Override
	public Map<String, Object> getData(TileBlockSensor caller)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();

		int currentTank = 0;
		List<ILiquidTank> takenTanks = new ArrayList<ILiquidTank>();
		for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
		{
			ILiquidTank[] tanks = tankContainer.getTanks(direction);
			for(ILiquidTank tank : tanks)
			{
				// Really hacky way to cull duplicate tanks...
				boolean found = false;
				for(ILiquidTank takenTank : takenTanks)
				{
					if(takenTank.equals(tank) || (takenTank.getLiquid() == null && tank.getLiquid() == null) ||
							(takenTank.getLiquid().itemID == tank.getLiquid().itemID &&
							 takenTank.getLiquid().itemMeta == tank.getLiquid().itemMeta &&
							 takenTank.getLiquid().amount == tank.getLiquid().amount &&
							 takenTank.getCapacity() == tank.getCapacity()) || takenTank.equals(tank) || takenTank.hashCode() == tank.hashCode())
					{
						found = true;
						break;
					}
				}
				
				if(!found)
				{
					Map<String, Object> tankData = new HashMap<String, Object>();
					if(tank.getLiquid() == null)
					{
						tankData.put("liquidID", 0);
						tankData.put("liquidMeta", 0);
						tankData.put("liquidLevel", 0);
					}
					else
					{
						tankData.put("liquidID", tank.getLiquid().itemID);
						tankData.put("liquidMeta", tank.getLiquid().itemMeta);
						tankData.put("liquidLevel", tank.getLiquid().amount);
					}
					tankData.put("liquidCapacity", tank.getCapacity());
					tankData.put("liquidPressure", tank.getTankPressure());
					dataMap.put("tank" + (++currentTank), tankData);
					takenTanks.add(tank);
				}
			}
		}
		dataMap.put("tanks", takenTanks.size());
		return dataMap;
	}
}
