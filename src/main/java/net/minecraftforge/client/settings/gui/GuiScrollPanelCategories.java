package net.minecraftforge.client.settings.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class GuiScrollPanelCategories extends GuiScrollPanel<String>
{

    static final ResourceLocation WIDGITS = new ResourceLocation("textures/gui/widgets.png");

    private HashMap<String, KeyBinding[]> keyBindingsByCategory;

    GuiScrollPanelCategories(GuiScreen parent, String[] categories,
            HashMap<String, KeyBinding[]> keyBindingsByCategory)
    {
        super(parent, categories, parent.width, parent.height, 63, parent.height - 32, 20);
        this.keyBindingsByCategory = keyBindingsByCategory;
    }

    @Override
    public int getListWidth()
    {
        return (int) (this.width * 0.6);
    }

    @Override
    protected void onElementClicked(int index, String entry, boolean isDoubleClick, int mouseX,
            int mouseY)
    {
        this.mc.displayGuiScreen(new GuiMenuKeyBindings(
                this.getParent(), entry, this.keyBindingsByCategory.get(entry)));
    }

    @Override
    protected void drawEntry(int index, String entry, int x, int y, int listWidth, int slotHeight,
            int mouseX,
            int mouseY, boolean isSelected)
    {
        this.mc.getTextureManager().bindTexture(GuiScrollPanelCategories.WIDGITS);
        GlStateManager.color(1, 1, 1, 1);

        slotHeight += 4;
        int slotWidth = listWidth;
        int k = isSelected ? 2 : 1;
        int halfWidth = slotWidth / 2;
        x = this.width / 2 - listWidth / 2;
        this.getParent().drawTexturedModalRect(
                x, y, 0, 46 + k * 20, halfWidth, slotHeight
        );
        this.getParent().drawTexturedModalRect(
                x + halfWidth, y, 200 - halfWidth, 46 + k * 20, halfWidth, slotHeight
        );
        this.getParent().drawCenteredString(this.mc.fontRendererObj,
                I18n.format(entry), x + halfWidth, y + 5, 0xffffff
        );

    }

}
