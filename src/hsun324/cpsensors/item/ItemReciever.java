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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemReciever extends Item
{
	public ItemReciever(int id)
	{
		super(id);
		setIconIndex(1);
	}
	
	@Override 
	public String getTextureFile()
	{
		return "/hsun324/cpsensors/images/items.png";
	}
	
	@Override
	public String getItemName()
	{
		return "hsun324.cpsensors.sensorreceiver";
	}

	@Override
    public String getItemNameIS(ItemStack itemStack)
    {
        return getItemName();
    }
}
