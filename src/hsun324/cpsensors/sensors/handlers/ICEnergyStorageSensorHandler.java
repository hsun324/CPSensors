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

import ic2.api.IEnergyStorage;

import java.util.HashMap;
import java.util.Map;

import hsun324.cpsensors.sensors.ISensorHandler;
import hsun324.cpsensors.tile.TileBlockSensor;

public class ICEnergyStorageSensorHandler implements ISensorHandler
{
	private final Map<String, Object> dataMap = new HashMap<String, Object>();

	@Override
	public Map<String, Object> getData(Object energyStorageObj, TileBlockSensor caller)
	{
		if(energyStorageObj instanceof IEnergyStorage)
		{
			IEnergyStorage energyStorage = (IEnergyStorage) energyStorageObj;
			
			dataMap.put("energyLevel", energyStorage.getStored());
			dataMap.put("energyOutput", energyStorage.getOutput());
			dataMap.put("energyCapacity", energyStorage.getCapacity());
			return dataMap;
		}
		return null;
	}

}
