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

import net.minecraft.item.ItemStack;

import forestry.api.apiculture.IBeeHousing;
import hsun324.cpsensors.sensors.ISensorHandler;
import hsun324.cpsensors.tile.TileBlockSensor;

public class BeeHousingSensorHandler implements ISensorHandler
{
	public final IBeeHousing beeHousing;
	public BeeHousingSensorHandler(IBeeHousing beeHousing)
	{
		this.beeHousing = beeHousing;
	}
	
	@Override
	public Map<String, Object> getData(TileBlockSensor caller)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		ItemStack queen = beeHousing.getQueen();
		ItemStack drone = beeHousing.getDrone();
		
		dataMap.put("queenId", queen == null ? 0 : queen.itemID);
		dataMap.put("queenMeta", queen == null ? 0 : queen.getItemDamage());
		dataMap.put("droneId", drone == null ? 0 : drone.itemID);
		dataMap.put("droneMeta", drone == null ? 0 : drone.getItemDamage());
		dataMap.put("housingBiomeId", beeHousing.getBiomeId());
		dataMap.put("housingTemperature", beeHousing.getTemperature().toString());
		dataMap.put("housingHumidity", beeHousing.getHumidity().toString());
		dataMap.put("housingCanBreed", beeHousing.canBreed());
		dataMap.put("housingSealed", beeHousing.isSealed());
		dataMap.put("housingSelfLighted", beeHousing.isSelfLighted());
		dataMap.put("housingSunlightSimulated", beeHousing.isSunlightSimulated());
		dataMap.put("housingHellish", beeHousing.isHellish());
		dataMap.put("errorState", beeHousing.getErrorOrdinal());
		
		return dataMap;
	}
}
