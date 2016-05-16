package net.minecraftforge.client.settings.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;

import java.io.IOException;

class GuiMenuKeyBindings extends GuiScrollContainer<KeyBinding>
{

    private final GuiScreen parent;
    private final String category;
    private final KeyBinding[] keyBindings;

    GuiMenuKeyBindings(GuiScreen parent, String category, KeyBinding[] keyBindings)
    {
        this.parent = parent;
        this.category = category;
        this.keyBindings = keyBindings;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList
                .add(new GuiButton(1337, width / 2 - 100, height - 28, I18n.format("gui.done")));
    }

    @Override
    protected GuiScrollPanel<KeyBinding> createScrollPanel()
    {
        return new GuiScrollPanelKeyBindings(this, this.keyBindings);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if (button.id == 1337)
            this.mc.displayGuiScreen(this.parent);
    }

    @Override
    protected void drawScreenPre(int mouseX, int mouseY, float partialTicks)
    {
        this.drawCenteredString(this.fontRendererObj, "Controls: " + I18n.format(this.category),
                this.width / 2, 5, 0xffffff);
    }

}
