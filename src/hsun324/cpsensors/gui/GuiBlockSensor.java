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

import hsun324.cpsensors.container.ContainerBlockSensor;
import hsun324.cpsensors.tile.TileBlockSensor;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
 
 
public class GuiBlockSensor extends GuiContainer
{
    public GuiBlockSensor(InventoryPlayer playerInventory, TileBlockSensor tileEntity)
    {
        super(new ContainerBlockSensor(playerInventory, tileEntity));
        ySize = 141;
    }
   
   
    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
        fontRenderer.drawString("Sensor", 6, 6, 0x000000);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 , 0x000000);
    }
   
   
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int picture = mc.renderEngine.getTexture("/hsun324/cpsensors/images/sensorgui.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
       
        this.mc.renderEngine.bindTexture(picture);
       
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}