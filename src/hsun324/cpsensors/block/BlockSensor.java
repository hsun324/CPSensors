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

package hsun324.cpsensors.block;


import hsun324.cpsensors.CPSensors;
import hsun324.cpsensors.core.CoreLibrary;
import hsun324.cpsensors.tile.TileBlockSensor;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSensor extends BlockContainer
{

	public BlockSensor(int id)
	{
		super(id, 16*0 + 2, CoreLibrary.materialSensor);
		setResistance(5.0F);
		setHardness(3.0F);
		setBlockBounds(0f, 0f, 0f, 1f, .5f, 1f);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileBlockSensor();
	}
	
	@Override
	public int getBlockTextureFromSide(int side)
	{
	        switch(side)
	        {
	        case 0:
	                return 16*0 + 1;
	        case 1:
	                return 16*0 + 0;
	        }
	        return blockIndexInTexture;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	public String getTextureFile()
	{
		return "/hsun324/cpsensors/images/terrain.png";
	}

	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f, float g, float t)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(tileEntity == null || player.isSneaking())
			return false;
		
		player.openGui(CPSensors.instance, 1, world, x, y, z);
		return true;
	}
    
    @Override
    public void breakBlock(World world, int x, int y, int z, int i, int j)
    {
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, i, j);
    }    
    
    @Override
    public String getBlockName()
    {
    	return "hsun324.cpsensors.sensorblock";
    }
   
   
    private void dropItems(World world, int x, int y, int z)
    {
    	TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
    	if(tileEntity instanceof IInventory)
        {
         	IInventory inventory = (IInventory) tileEntity;
            for(int i = 0; i < inventory.getSizeInventory(); i++)
            {
                ItemStack itemStack = inventory.getStackInSlot(i);
                if(itemStack != null && itemStack.stackSize > 0)
                {
                	CoreLibrary.dropItem(world, x, y, z, itemStack);
                }
            }
        }
    }
}
