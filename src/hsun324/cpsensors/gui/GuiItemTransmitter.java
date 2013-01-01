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

import hsun324.cpsensors.core.CoreLibrary;
import hsun324.cpsensors.sensors.TargetType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

public class GuiItemTransmitter extends GuiScreen
{
	private int selected = 0;
	private List<TargetType> validTargetTypes = null;
	private GuiButton[] buttons = new GuiButton[3];
	private int xSize = 228;
	private int ySize = 82;
	private TileEntity target = null;
	
	public GuiItemTransmitter(TileEntity target, List<TargetType> validTargetTypes)
	{
		super();
		this.target = target;
		this.validTargetTypes = validTargetTypes;
	}
	
	@Override
    public void drawScreen(int par1, int par2, float par3)
    {
		drawBackground(0);
		super.drawScreen(par1, par2, par3);
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
	    super.initGui();

	    int bw = this.xSize - 20;
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

	    this.controlList.add(this.buttons[0] = new GuiButton(1, x + 10 + bw * 1 / 6, y + 50, bw / 6, 20, "<"));
	    this.controlList.add(this.buttons[1] = new GuiButton(2, x + 10 + bw * 1 / 3, y + 50, bw / 3, 20, "OK"));
	    this.controlList.add(this.buttons[2] = new GuiButton(3, x + 10 + bw * 2 / 3, y + 50, bw / 6, 20, ">"));
	}

	@Override
	public void drawBackground(int i)
	{
	    int id = mc.renderEngine.getTexture("/hsun324/cpsensors/images/selectiongui.png");
	    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	    
	    mc.renderEngine.bindTexture(id);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
	    GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
	    fontRenderer.drawString("Sensor Configuration Selection", (width - fontRenderer.getStringWidth("Sensor Configuration Selection"))/2, y + 20, 0xFF000000);
	    this.drawCenteredString(fontRenderer, validTargetTypes.get(selected).getDescription(), width / 2, y + 24 + fontRenderer.FONT_HEIGHT, -1);
	}

	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
	
	@Override 
    protected void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
		case 1:
			if(--selected < 0)
				selected = validTargetTypes.size() - 1;
			break;
		case 2:
			try
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream(17);
				out.write(1);
				
				System.out.println(validTargetTypes.get(selected).name());
				byte[] selectedTypeName = validTargetTypes.get(selected).name().getBytes("UTF-16");
				
				out.write(CoreLibrary.intToByte(selectedTypeName.length));
				out.write(selectedTypeName);
				out.write(CoreLibrary.intToByte(target.xCoord));
				out.write(CoreLibrary.intToByte(target.yCoord));
				out.write(CoreLibrary.intToByte(target.zCoord));
				byte[] payload = out.toByteArray();
				
				Packet250CustomPayload packet = new Packet250CustomPayload();
				packet.channel = "cpsensors";
				packet.isChunkDataPacket = false;
				packet.data = payload;
				packet.length = payload.length;
				
				Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			break;
		case 3:
			if(++selected >= validTargetTypes.size())
				selected = 0;
		}
	}
}
