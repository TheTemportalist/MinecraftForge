package net.minecraftforge.client.settings.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiMenuCategories extends GuiScrollContainer<String>
{

    private static final GameSettings.Options[] controlOptions = new GameSettings.Options[] {
            GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY,
            GameSettings.Options.TOUCHSCREEN
    };

    private String[] keyBindingCategories;
    private HashMap<String, KeyBinding[]> keyBindingsByCategory;

    private final GuiOptions optionsGui;
    private final GameSettings settings;
    private int buttonID_Done, buttonID_ResetAll, buttonID_Conflicts;
    private GuiButton buttonReset;

    public GuiMenuCategories(GuiOptions options, GameSettings settings)
    {
        this.optionsGui = options;
        this.settings = settings;

        this.keyBindingsByCategory = this.fetchKeyBindingCategories();
        this.keyBindingCategories = this.keyBindingsByCategory.keySet().toArray(new String[0]);
    }

    private HashMap<String, KeyBinding[]> fetchKeyBindingCategories()
    {
        HashMap<String, List<KeyBinding>> categoryMap = new HashMap<String, List<KeyBinding>>();
        for (KeyBinding keyBinding : Minecraft.getMinecraft().gameSettings.keyBindings)
        {
            if (!categoryMap.containsKey(keyBinding.getKeyCategory()))
                categoryMap.put(keyBinding.getKeyCategory(), new ArrayList<KeyBinding>());
            categoryMap.get(keyBinding.getKeyCategory()).add(keyBinding);
        }

        HashMap<String, KeyBinding[]> categories = new HashMap<String, KeyBinding[]>();
        for (Map.Entry<String, List<KeyBinding>> entry : categoryMap.entrySet())
        {
            categories.put(entry.getKey(), entry.getValue().toArray(new KeyBinding[0]));
        }

        return categories;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        int nextBID = 0;

        int i = 0;
        for (GameSettings.Options option : GuiMenuCategories.controlOptions)
        {
            if (option.getEnumFloat())
            {
                this.buttonList.add(new GuiOptionSlider(option.returnEnumOrdinal(),
                        this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), option));
            }
            else
            {
                this.buttonList.add(new GuiOptionButton(option.returnEnumOrdinal() + (nextBID++),
                        this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1),
                        option, this.settings.getKeyBinding(option)));
            }
            ++i;
        }

        this.buttonList.add(new GuiButton(this.buttonID_Conflicts = nextBID++,
                this.width / 2 - 85 - 100,
                this.height - 29, 100, 20, "Conflicts"));
        this.buttonList.add(new GuiButton(this.buttonID_Done = nextBID++,
                this.width / 2 - 75,
                this.height - 29, 150, 20, I18n.format("gui.done")));
        this.buttonList.add(this.buttonReset = new GuiButton(this.buttonID_ResetAll = nextBID++,
                this.width / 2 + 85,
                this.height - 29, 100, 20,
                I18n.format("controls.resetAll", new Object[0])));

    }

    private void onClick_Conflicts()
    {
        this.mc.displayGuiScreen(new GuiMenuKeyBindingConflicts(this, this.settings));
    }

    @Override
    protected GuiScrollPanel<String> createScrollPanel()
    {
        return new GuiScrollPanelCategories(this, this.keyBindingCategories,
                this.keyBindingsByCategory);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        if (button.id == this.buttonID_Conflicts)
            this.onClick_Conflicts();
        else if (button.id == this.buttonID_Done)
            this.mc.displayGuiScreen(this.optionsGui);
        else if (button.id == this.buttonID_ResetAll)
        {
            for (KeyBinding keyBinding : this.settings.keyBindings)
            {
                keyBinding.setToDefault();
            }
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (button instanceof GuiOptionButton)
        {
            this.settings.setOptionValue(((GuiOptionButton) button).returnEnumOptions(), 1);
            button.displayString = this.settings
                    .getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
        }
    }

    @Override
    protected void drawScreenPre(int mouseX, int mouseY, float partialTicks)
    {
        this.drawCenteredString(this.fontRendererObj, "Controls", this.width / 2, 5, 0xffffff);

        boolean allButtonsAreDefault = true;
        for (KeyBinding keyBinding : this.settings.keyBindings)
        {
            if (!keyBinding.isSetToDefaultValue())
            {
                allButtonsAreDefault = false;
                break;
            }
        }
        this.buttonReset.enabled = !allButtonsAreDefault;

    }

}
