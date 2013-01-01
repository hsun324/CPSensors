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
package hsun324.cpsensors.tile;


import hsun324.cpsensors.item.ItemTransmissionCard;
import hsun324.cpsensors.sensors.ISensorHandler;
import hsun324.cpsensors.sensors.TargetType;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileBlockSensor extends TileEntity implements IInventory, IHostedPeripheral
{
	private IInventory inventory;
	
	public TileBlockSensor()
	{
		inventory = new InventoryBasic("cpsensor", 9);
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slotNum)
	{
		return inventory.getStackInSlot(slotNum);
	}

	@Override
	public ItemStack decrStackSize(int slotNum, int amount)
	{
		return inventory.decrStackSize(slotNum, amount);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotNum)
	{
		return inventory.getStackInSlotOnClosing(slotNum);
	}

	@Override
	public void setInventorySlotContents(int slotNum, ItemStack item)
	{
		inventory.setInventorySlotContents(slotNum, item);
	}

	@Override
	public String getInvName()
	{
		return inventory.getInvName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inventory.isUseableByPlayer(player);
	}

	@Override
	public void openChest() { }

	@Override
	public void closeChest() { }
    
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        NBTTagList tagList = tagCompound.getTagList("Inventory");
       
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = tag.getByte("Slot");
            if(slot >= 0 && slot < inventory.getSizeInventory())
            	inventory.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tag));
        }
    }
   
    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        NBTTagList itemList = new NBTTagList();
       
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            if(stack != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
    }

	@Override
	public String getType() 
	{
		return "cpsensor";
	}

	@Override
	public String[] getMethodNames() 
	{
		return new String[] { "readSensor", "setName" };
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception 
	{
	    switch (method)
	    {
			case 0:
				if (arguments.length < 1)
					throw new Exception("not enough arguments (req. 1)");
				
				if (!(arguments[0] instanceof Double))
					throw new Exception("bad argument (expected number)");
				
				int slot = (int) Math.floor(((Double)arguments[0]).doubleValue()) - 1;
			
				if ((slot < 0) || (slot >= this.inventory.getSizeInventory()))
					throw new Exception("bad argument (expected 1~" + this.inventory.getSizeInventory() + ")");
				
				ItemStack stack = this.inventory.getStackInSlot(slot);
				if (stack == null || !(stack.getItem() instanceof ItemTransmissionCard))
					return new Object[] { null, null, null, null };
				
				ItemTransmissionCard card = (ItemTransmissionCard) stack.getItem();
				TargetType targetType = card.getTargetType(stack);
				if(targetType == TargetType.INVALID)
					return new Object[] { null, "INVALID", "INVALID", null };
				
				if(!card.targetValid(this.worldObj, stack))
					return new Object[] { null, "BAD", targetType.name(), null };
				
				if(targetType.getHandler() != null)
				{
					try
					{
						if(targetType.requiresTarget())
						{
							ISensorHandler handler = targetType.getHandler().getConstructor(targetType.getIdentifier()).newInstance(card.getTarget(this.worldObj, stack));
							return new Object[] { card.getName(stack), "OK", targetType.name(), handler.getData(this) };
						}
						else
						{
							ISensorHandler handler = targetType.getHandler().getConstructor().newInstance();
							return new Object[] { card.getName(stack), "OK", targetType.name(), handler.getData(this) };
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
						throw new Exception("error running (" + e.getClass().getSimpleName() + " was thrown)");
					}
				}
				
				return new Object[] { null, null, null, null };
			case 1:
				if (arguments.length < 2)
					throw new Exception("not enough arguments (req. 2)");
				
				if (!(arguments[0] instanceof Double))
					throw new Exception("bad argument (expected number)");
				
				slot = (int) Math.floor(((Double)arguments[0]).doubleValue()) - 1;
			
				if ((slot < 0) || (slot >= this.inventory.getSizeInventory()))
					throw new Exception("bad argument (expected 1~" + this.inventory.getSizeInventory() + ")");
				
				stack = this.inventory.getStackInSlot(slot);
				if (stack == null || !(stack.getItem() instanceof ItemTransmissionCard))
					return new Object[] { false };

				card = (ItemTransmissionCard) stack.getItem();
				if(card.getTargetType(stack) == TargetType.INVALID)
					return new Object[] { false };
				card.setName(stack, arguments[1].toString());
				
				return new Object[] { true };
		}
		return new Object[0];
	}

	@Override
	public boolean canAttachToSide(int side) 
	{
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) { }

	@Override
	public void detach(IComputerAccess computer) { }

	@Override
	public void update()
	{
		
	}
}
