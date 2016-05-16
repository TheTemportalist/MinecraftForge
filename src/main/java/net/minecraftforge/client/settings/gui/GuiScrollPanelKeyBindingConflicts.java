package net.minecraftforge.client.settings.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import java.util.ArrayList;
import java.util.List;

class GuiScrollPanelKeyBindingConflicts extends GuiScrollPanelKeyBindings
{

    private static KeyBinding[] getConflictingEntries(GameSettings settings)
    {
        List<KeyBinding> conflictKeys = new ArrayList<KeyBinding>();
        List<KeyBinding> conflictModifiers = new ArrayList<KeyBinding>();

        for (KeyBinding keyBinding : settings.keyBindings)
        {
            if (KeyBinding.hasConflictingKey(keyBinding))
                conflictKeys.add(keyBinding);
            else if (!conflictKeys.contains(keyBinding) && KeyBinding
                    .hasConflictingModifier(keyBinding))
                conflictModifiers.add(keyBinding);
        }

        conflictKeys.addAll(conflictModifiers);
        return conflictKeys.toArray(new KeyBinding[0]);
    }

    private final GameSettings settings;

    GuiScrollPanelKeyBindingConflicts(GuiScreen parent, GameSettings settings)
    {
        super(parent, getConflictingEntries(settings));
        this.settings = settings;
    }

    @Override
    protected void onBindingChanged(KeyBinding keyBinding)
    {
        this.refreshWithEntries(getConflictingEntries(this.settings));
    }

}
