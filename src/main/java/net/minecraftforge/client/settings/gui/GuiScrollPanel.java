package net.minecraftforge.client.settings.gui;

import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public abstract class GuiScrollPanel<T> extends GuiListExtended
{

    private final GuiScreen parent;
    private T[] entries;
    private ListEntry<T>[] listEntries;

    public GuiScrollPanel(GuiScreen parent, T[] entries,
            int width, int height, int top, int bottom, int slotHeight)
    {
        super(parent.mc, width, height, top, bottom, slotHeight);
        this.parent = parent;
        this.refreshWithEntries(entries);
    }

    protected GuiScreen getParent()
    {
        return this.parent;
    }

    private void initListEntries()
    {
        ListEntry[] listEntries = new ListEntry[this.getSize()];
        for (int i = 0; i < this.getSize(); i++)
            listEntries[i] = new ListEntry<T>(this, this.entries[i]);
        this.listEntries = (ListEntry<T>[]) listEntries;
    }

    protected void refreshWithEntries(T[] entries)
    {
        this.entries = entries;
        this.initListEntries();
    }

    @Override
    public int getListWidth()
    {
        return (int) (this.getParent().width * 0.9);
    }

    @Override
    protected int getScrollBarX()
    {
        return this.width / 2 + this.getListWidth() / 2;
    }

    @Override
    public IGuiListEntry getListEntry(int index)
    {
        return this.listEntries[index];
    }

    @Override
    protected int getSize()
    {
        return this.entries.length;
    }

    protected T getElement(int index)
    {
        return this.entries[index];
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
    {
        this.onElementClicked(slotIndex, this.entries[slotIndex], isDoubleClick, mouseX, mouseY);
    }

    protected abstract void onElementClicked(int index, T entry, boolean isDoubleClick,
            int mouseX, int mouseY);

    protected abstract void drawEntry(int index, T entry, int x, int y, int listWidth,
            int slotHeight,
            int mouseX, int mouseY, boolean isSelected);

    protected boolean mousePressed(int index, T entry, int mouseX, int mouseY, int mouseEvent,
            int relativeX, int relativeY)
    {
        return false;
    }

    protected void mouseReleased(int index, T entry, int x, int y, int mouseEvent, int relativeX,
            int relativeY)
    {
    }

    protected boolean keyTyped(char typedChar, int keyCode) throws IOException
    {
        return false;
    }

    public class ListEntry<T> implements IGuiListEntry
    {

        private final GuiScrollPanel<T> owner;
        private final T entry;

        ListEntry(GuiScrollPanel<T> owner, T entry)
        {
            this.owner = owner;
            this.entry = entry;
        }

        @Override
        public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
        {
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight,
                int mouseX,
                int mouseY, boolean isSelected)
        {
            this.owner.drawEntry(slotIndex, this.entry, x, y, listWidth, slotHeight, mouseX, mouseY,
                    isSelected);
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent,
                int relativeX, int relativeY)
        {
            return this.owner
                    .mousePressed(slotIndex, this.entry, mouseX, mouseY, mouseEvent, relativeX,
                            relativeY);
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX,
                int relativeY)
        {
            this.owner.mouseReleased(slotIndex, this.entry, x, y, mouseEvent, relativeX, relativeY);
        }

    }

}
