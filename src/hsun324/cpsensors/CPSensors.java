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
package hsun324.cpsensors;

import java.io.File;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import hsun324.cpsensors.core.CoreProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "CPSensors",
	name = "CPSensors",
	version = "0.4")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class CPSensors 
{
	@Mod.Instance("CPSensors")
	public static CPSensors instance;
	
	public static int sensorBlockID;
	public static int sensorTransmissionCardID;
	public static int sensorRecieverID;
	public static int sensorTransmitterID;
	public static boolean turtlePeripheralEnabled;
	
	public static Logger logger = Logger.getLogger("CPSensors");
	
	@Mod.PreInit
	public void preInit( FMLPreInitializationEvent preinitEvent )
	{
		loadConfiguration(preinitEvent.getSuggestedConfigurationFile());
	}

	@Mod.Init
	public void init( FMLInitializationEvent evt )
	{
		logger.info("[CPSensors] Launching");
		logger.info(new File(".").getAbsolutePath());
		
		CoreProxy.instance.registerObjects();
		CoreProxy.instance.registerRender();
		
		logger.info("[CPSensors] Launch Successful");
	}
	
	private static void loadConfiguration( File configurationFile )
	{
		Configuration configFile = new Configuration(configurationFile);
		
		Property prop = configFile.getBlock("sensorBlockID", 187);
		prop.comment = "Sensor Block ID";
		sensorBlockID = prop.getInt();
		
		prop = configFile.getItem("sensorTransmissionCardID", 9510);
		prop.comment = "Sensor Transmission Card Item ID";
		sensorTransmissionCardID = prop.getInt();
		
		prop = configFile.getItem("sensorRecieverID", 9511);
		prop.comment = "Sensor Reciever Item ID";
		sensorRecieverID = prop.getInt();
		
		prop = configFile.getItem("sensorTransmitterID", 9512);
		prop.comment = "Sensor Transmitter Item ID";
		sensorTransmitterID = prop.getInt();
		
		configFile.save();
	}
}
