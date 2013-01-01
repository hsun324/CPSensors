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
package hsun324.cpsensors.container;
 
import hsun324.cpsensors.tile.TileBlockSensor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
 
 
public class ContainerBlockSensor extends Container
{
	
	public class SensorSlot extends Slot
	{
		public SensorSlot(IInventory inventory, int inventorySlotId, int x, int y)
		{
			super(inventory, inventorySlotId, x, y);
		}

	}

	private TileBlockSensor sensor;
	private InventoryPlayer inventory;
	
	
	public ContainerBlockSensor(InventoryPlayer playerInventory, TileBlockSensor tileEntity)
	{
		sensor = tileEntity;
		inventory = playerInventory;

        for(int i = 0; i < 9; i++)
        {
        	addSlotToContainer(new SensorSlot(sensor, i, 8 + i * 18, 22));
        }
		
        for(int j = 0; j < 3; j++)
        {
            for(int k = 0; k < 9; k++)
            {
            	addSlotToContainer(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 60 + j * 18));
            }
        }

        for(int l = 0; l < 9; l++)
        {
        	addSlotToContainer(new Slot(inventory, l, 8 + l * 18, 118));
        }
	}

	@Override
	public boolean canInteractWith( EntityPlayer player )
	{
		return sensor.isUseableByPlayer( player );
	}
	
    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotNum)
    {
        ItemStack itemStack = null;
        Slot currentSlot = (Slot)this.inventorySlots.get(slotNum);

        if (currentSlot != null && currentSlot.getHasStack())
        {
            ItemStack currentStack = currentSlot.getStack();
            itemStack = currentStack.copy();

            if (slotNum < 9)
            {
                if (!this.mergeItemStack(currentStack, 9, 45, true))
                {
                    return null;
                }
            }
            else
            {
            	for(int i = 0; i < 9; i++)
            	{
	                if (((Slot)this.inventorySlots.get(i)).getHasStack() || !((Slot)this.inventorySlots.get(i)).isItemValid(currentStack))
	                {
	                	continue;
	                }
	
	                if (currentStack.stackSize == 1)
	                {
	                    ((Slot)this.inventorySlots.get(i)).putStack(currentStack.copy());
	                    currentStack.stackSize = 0;
	                }
	                else if (currentStack.stackSize >= 1)
	                {
	                	ItemStack sc = currentStack.copy();
	                	sc.stackSize = 1;
	                    ((Slot)this.inventorySlots.get(i)).putStack(sc);
	                    --currentStack.stackSize;
	                }
                    break;
            	}
            }

            if (currentStack.stackSize == 0)
            {
            	currentSlot.putStack((ItemStack)null);
            }
            else
            {
            	currentSlot.onSlotChanged();
            }

            if (currentStack.stackSize == itemStack.stackSize)
            {
                return null;
            }

            currentSlot.onPickupFromSlot(entityPlayer, currentStack);
        }

        return itemStack;
    }
}