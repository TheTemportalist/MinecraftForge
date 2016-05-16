package net.minecraftforge.client.settings.gui;

import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public abstract class GuiScrollContainer<T> extends GuiScreen
{

    private GuiScrollPanel<T> scroll;

    @Override
    public void initGui()
    {
        this.scroll = this.createScrollPanel();
        this.scroll.registerScrollButtons(7, 8);
    }

    protected abstract GuiScrollPanel<T> createScrollPanel();

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.scroll.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        if (!this.scroll.mouseClicked(mouseX, mouseY, mouseButton))
            super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (!this.scroll.mouseReleased(mouseX, mouseY, state))
            super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (!this.scroll.keyTyped(typedChar, keyCode))
            super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawBackground(0);
        this.scroll.drawScreen(mouseX, mouseY, partialTicks);
        this.drawScreenPre(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void drawScreenPre(int mouseX, int mouseY, float partialTicks)
    {
    }

}
