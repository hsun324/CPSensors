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
package hsun324.cpsensors.item;

import java.util.ArrayList;
import java.util.List;

import hsun324.cpsensors.CPSensors;
import hsun324.cpsensors.sensors.TargetType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemTransmitter extends Item
{
	public List<TargetType> validTargetTypes = null;
	public ItemTransmitter(int id)
	{
		super(id);
		setIconIndex(0);
	}
	
	@Override 
	public String getTextureFile()
	{
		return "/hsun324/cpsensors/images/items.png";
	}
	
	@Override
	public String getItemName()
	{
		return "hsun324.cpsensors.sensortransmitter";
	}

	@Override
    public String getItemNameIS(ItemStack itemStack)
    {
        return getItemName();
    }

	@Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
		if (player == null)
			return false;
		boolean isServer = player instanceof EntityPlayerMP;
		if (isServer)
			return false;

		validTargetTypes = isValidTarget(world, x, y, z);
		
		if (validTargetTypes != null)
		{
			player.openGui(CPSensors.instance, 2, world, x, y, z);
			return true;
		}
		return false;
    }
	
	public static void setTarget(ItemStack transmissionCard, TargetType targetType, int x, int y, int z)
	{
		NBTTagCompound tag = transmissionCard.getTagCompound();
		if(tag == null)
		{
			tag = new NBTTagCompound();
			transmissionCard.setTagCompound(tag);
		}

		tag.setString("type", targetType.name());
		tag.setIntArray("location", new int[]{x, y, z});
	}

	private List<TargetType> isValidTarget(World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		List<TargetType> validTypeList = new ArrayList<TargetType>();
		if(tileEntity != null)
			for(TargetType targetType : TargetType.values())
				if(targetType.requiresTarget() && targetType.getIdentifier() != null)
					if(targetType.getIdentifier().isAssignableFrom(tileEntity.getClass()))
						validTypeList.add(targetType);
		return validTypeList.isEmpty() ? null : validTypeList;
	}
	
}
