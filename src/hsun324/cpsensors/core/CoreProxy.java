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

import hsun324.cpsensors.CPSensors;
import hsun324.cpsensors.block.BlockSensor;
import hsun324.cpsensors.gui.GuiHandler;
import hsun324.cpsensors.item.ItemReciever;
import hsun324.cpsensors.item.ItemTransmissionCard;
import hsun324.cpsensors.item.ItemTransmitter;
import hsun324.cpsensors.sensors.TargetType;
import hsun324.cpsensors.tile.TileBlockSensor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class CoreProxy
{
	@SidedProxy( clientSide = "hsun324.cpsensors.core.CoreProxyClient", serverSide = "hsun324.cpsensors.core.CoreProxy" )
	public static CoreProxy instance;
	
	public static GuiHandler guiHandler = new GuiHandler();
	public static PacketHandler packetHandler = new PacketHandler();

	public static BlockSensor sensorBlock;
	public static Item sensorTransmissionCard;
	public static Item sensorReciever;
	public static Item sensorTransmitter;

	public static ItemStack sensorTimeCard;

	public void registerObjects()
	{
		sensorBlock = new BlockSensor(CPSensors.sensorBlockID);
		GameRegistry.registerBlock(sensorBlock, "cpsensor");
		GameRegistry.registerTileEntity(TileBlockSensor.class, "TBcpsensor");
		
		sensorTransmissionCard = new ItemTransmissionCard(CPSensors.sensorTransmissionCardID);
		GameRegistry.registerItem(sensorTransmissionCard, "cptranscard");
		sensorReciever = new ItemReciever(CPSensors.sensorRecieverID);
		GameRegistry.registerItem(sensorReciever, "cprec");
		sensorTransmitter = new ItemTransmitter(CPSensors.sensorTransmitterID);
		GameRegistry.registerItem(sensorTransmitter, "cptrans");

	    GameRegistry.addRecipe(new ItemStack(sensorReciever, 1), " TR", "IGI", Character.valueOf('T'), Block.torchRedstoneActive, Character.valueOf('R'), Item.redstone, Character.valueOf('I'), Item.ingotIron, Character.valueOf('G'), Item.ingotGold);
	    GameRegistry.addRecipe(new ItemStack(sensorTransmitter, 1), "  T", "III", Character.valueOf('T'), Block.torchRedstoneActive, Character.valueOf('I'), Item.ingotIron);
	    GameRegistry.addRecipe(new ItemStack(sensorBlock, 1), "SRS", "SSS", Character.valueOf('S'), Block.stone, Character.valueOf('R'), sensorReciever);
	    
	    TargetType.initTypes();
	    sensorTimeCard = new ItemStack(sensorTransmissionCard, 1);
	    ItemTransmitter.setTarget(sensorTimeCard, TargetType.TIME, 1, 1, 1);
	    
	    GameRegistry.addShapelessRecipe(sensorTimeCard, sensorTransmitter, Item.pocketSundial);
	    
        LanguageRegistry.addName(sensorBlock, "Sensor");
        LanguageRegistry.addName(sensorTransmissionCard, "Sensor Transmission Card");
        LanguageRegistry.addName(sensorReciever, "Sensor Receiver");
        LanguageRegistry.addName(sensorTransmitter, "Sensor Transmitter");
        NetworkRegistry.instance().registerGuiHandler(CPSensors.instance, guiHandler);
        NetworkRegistry.instance().registerChannel(packetHandler, "cpsensors");
	}
	
    public void registerRender(){}
}
