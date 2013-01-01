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
package hsun324.cpsensors.gui;
 
import hsun324.cpsensors.container.ContainerBlockSensor;
import hsun324.cpsensors.item.ItemTransmitter;
import hsun324.cpsensors.tile.TileBlockSensor;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
 
public class GuiHandler implements IGuiHandler
{
 
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (id)
		{
		case 1:
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if(tileEntity instanceof TileBlockSensor)
				return new ContainerBlockSensor(player.inventory, (TileBlockSensor)tileEntity);
			break;
		}
		return null;
	}
   
   
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (id)
		{
		case 1:
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if(tileEntity instanceof TileBlockSensor)
				return new GuiBlockSensor(player.inventory, (TileBlockSensor)tileEntity);
			break;
		case 2:
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack != null && stack.stackSize > 0)
			{
				Item item = stack.getItem();
				if(item instanceof ItemTransmitter)
				{
					return new GuiItemTransmitter(world.getBlockTileEntity(x, y, z), ((ItemTransmitter) item).validTargetTypes);
				}
			}
		}
		return null;
	}
}