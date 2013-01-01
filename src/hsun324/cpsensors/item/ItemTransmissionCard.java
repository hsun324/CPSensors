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

import hsun324.cpsensors.sensors.TargetType;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemTransmissionCard extends Item
{
	
	public ItemTransmissionCard(int id)
	{
		super(id);
		setIconIndex(2);
		setMaxStackSize(1);
	}
	
	@Override 
	public String getTextureFile()
	{
		return "/hsun324/cpsensors/images/items.png";
	}
	
	@Override
	public String getItemName()
	{
		return "hsun324.cpsensors.sensortransmissioncard";
	}
	
	@Override
    public String getItemNameIS(ItemStack itemStack)
    {
        return getItemName();
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean advanced)
	{
		TargetType targetType = getTargetType(itemStack);
		
		if(targetType != TargetType.INVALID)
		{
			String name = getName(itemStack);
			if(name != null)
				info.add(name + " (" + targetType.getDescription() + ")");
			else
				info.add(targetType.getDescription());
			if(targetType.requiresTarget())
			{
				int[] location = getLocation(itemStack);
				info.add(String.format("x: %d, y: %d, z: %d", location[0], location[1], location[2]));
			}
		}
		else
			info.add(targetType.getDescription());
	}

	public NBTTagCompound getTag(ItemStack itemStack)
	{
		NBTTagCompound tag = itemStack.getTagCompound();
		if(tag == null)
			itemStack.setTagCompound(tag = new NBTTagCompound());
		return tag;
	}
	
	public TargetType getTargetType(ItemStack itemStack)
	{		
		NBTTagCompound tag = getTag(itemStack);
		String typeString = tag.getString("type");
		TargetType targetType = TargetType.valueOf(typeString);
		
		return targetType == null ? TargetType.INVALID : targetType;
	}

	public int[] getLocation(ItemStack itemStack)
	{
		int[] location = getTag(itemStack).getIntArray("location");
		return location == null || location.length != 3 ? new int[]{0, 0, 0} : location;
	}
	
	public String getName(ItemStack itemStack)
	{
		String name = getTag(itemStack).getString("name");
		return name.trim().isEmpty() ? null : name;
	}

	public void setName(ItemStack itemStack, String name)
	{
		getTag(itemStack).setString("name", name);
	}

	public boolean targetValid(World world, ItemStack stack)
	{
		TargetType targetType = getTargetType(stack);
		if(targetType == TargetType.INVALID)
			return false;
		
		if(!targetType.requiresTarget())
			return true;
		
		TileEntity tileEntity = getTarget(world, stack);
		return tileEntity != null && targetType.getIdentifier().isAssignableFrom(tileEntity.getClass());
	}

	public TileEntity getTarget(World world, ItemStack stack)
	{
		int[] location = getLocation(stack);
		return world.getBlockTileEntity(location[0], location[1], location[2]);
	}
}
