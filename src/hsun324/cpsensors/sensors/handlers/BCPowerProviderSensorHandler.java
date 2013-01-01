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

import buildcraft.api.power.IPowerProvider;

import net.minecraftforge.common.ForgeDirection;

import hsun324.cpsensors.sensors.ISensorHandler;
import hsun324.cpsensors.tile.TileBlockSensor;

public class BCPowerProviderSensorHandler implements ISensorHandler
{
	Map<String, Object> dataMap = new HashMap<String, Object>();
	
	@Override
	public Map<String, Object> getData(Object powerProviderObj, TileBlockSensor caller)
	{
		if(powerProviderObj instanceof IPowerProvider)
		{
			IPowerProvider powerProvider = (IPowerProvider) powerProviderObj;
			
			dataMap.clear();
			
			dataMap.put("providerLatency", powerProvider.getLatency());
			dataMap.put("providerMinEnergyReceived", powerProvider.getMinEnergyReceived());
			dataMap.put("providerMaxEnergyReceived", powerProvider.getMaxEnergyReceived());
			dataMap.put("providerCapacity", powerProvider.getMaxEnergyStored());
			dataMap.put("providerActivationEnergy", powerProvider.getActivationEnergy());
			dataMap.put("providerEnergyLevel", (double) powerProvider.getEnergyStored());
			for(ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
				dataMap.put("providerSide" + direction.ordinal(), powerProvider.isPowerSource(direction));
			
			return dataMap;
		}
		return null;
	}
	
}
