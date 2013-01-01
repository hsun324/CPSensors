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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import hsun324.cpsensors.item.ItemTransmitter;
import hsun324.cpsensors.sensors.TargetType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		if(packet.length < 1)
			return;
		switch(packet.data[0])
		{
		case 1:
			if(packet.length > 17 && player instanceof EntityPlayerMP)
			{
				try
				{
					InputStream in = new ByteArrayInputStream(packet.data);
					in.read();
					byte[] typeNameLengthBytes = new byte[4];
					in.read(typeNameLengthBytes);
					int typeNameLength = CoreLibrary.byteToInt(typeNameLengthBytes);
					
					byte[] typeNameBytes = new byte[typeNameLength];
					in.read(typeNameBytes);
					String typeName = new String(typeNameBytes, "UTF-16");
					System.out.println(typeName);
					
					TargetType selectedType = TargetType.valueOf(typeName);
					if(selectedType != null && selectedType != TargetType.INVALID)
					{
						byte[] intBytes = new byte[4];
						
						in.read(intBytes);
						int targetX = CoreLibrary.byteToInt(intBytes);
						in.read(intBytes);
						int targetY = CoreLibrary.byteToInt(intBytes);
						in.read(intBytes);
						int targetZ = CoreLibrary.byteToInt(intBytes);
						
						ItemStack transmissionCard = new ItemStack(CoreProxy.sensorTransmissionCard, 1, 0);
						ItemTransmitter.setTarget(transmissionCard, selectedType, targetX, targetY, targetZ);
						
						EntityPlayerMP entityPlayer = (EntityPlayerMP) player;
						
						ItemStack selectedStack = entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem];
						if(selectedStack != null && selectedStack.getItem() instanceof ItemTransmitter)
						{
							if(selectedStack.stackSize == 1)
								entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = transmissionCard;
							else
							{
								if(!entityPlayer.inventory.addItemStackToInventory(transmissionCard))
									entityPlayer.dropPlayerItem(transmissionCard);
								--entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem].stackSize;
							}
						}
						else
						{
							for(int i = 0; i < entityPlayer.inventory.mainInventory.length; i++)
							{
								ItemStack stack = entityPlayer.inventory.mainInventory[i];
								if(stack != null && stack.getItem() instanceof ItemTransmitter)
								{
									if(stack.stackSize == 1)
										entityPlayer.inventory.mainInventory[i] = transmissionCard;
									else
									{
										if(entityPlayer.inventory.addItemStackToInventory(transmissionCard))
											entityPlayer.dropPlayerItem(transmissionCard);
										--entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem].stackSize;
									}
									break;
								}
							}
						}
						entityPlayer.closeScreen();
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			break;
		
		}
	}
}
