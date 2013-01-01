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
package hsun324.cpsensors.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CoreLibrary
{
	public static final Material materialSensor = Material.rock;
	public static void dropItem(World world, int x, int y, int z, ItemStack items)
	{
		if(!isClient(world))
		{
			double iX = world.rand.nextFloat() * 0.7D + (1.0D - 0.7D) * 0.5D;
			double iY = world.rand.nextFloat() * 0.7D + (1.0D - 0.7D) * 0.5D;
			double iZ = world.rand.nextFloat() * 0.7D + (1.0D - 0.7D) * 0.5D;
			EntityItem item = new EntityItem(world, x + iX, y + iY, z + iZ, items);
			item.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(item);
		}
	}
	public static boolean isClient(World world)
	{
		return world.isRemote;
	}	
	public static int byteToInt(byte[] bytes)
	{
		if(bytes.length != 4)
			return Integer.MIN_VALUE;
		return ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
	}
	public static byte[] intToByte(int number)
	{
		return ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(number).array();
	}
}
