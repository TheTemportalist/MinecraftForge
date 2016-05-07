package net.minecraftforge.client.settings.gui;

import javafx.util.Pair;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.client.settings.KeyModifierSet;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheTemportalist on 4/10/2016.
 *
 * @author TheTemportalist
 */
class GuiScrollPanelKeyBindings extends GuiScrollPanel<KeyBinding> {

	private int selectedIndex = -1;
	private GuiButton[] keyButtons;
	private GuiButton[] resetButtons;

	GuiScrollPanelKeyBindings(GuiScreen parent, KeyBinding[] elements) {
		super(parent, elements, parent.width, parent.height, 20, parent.height - 32, 20);

		this.keyButtons = new GuiButton[this.getSize()];
		this.resetButtons = new GuiButton[this.getSize()];
		for (int i = 0; i < this.getSize(); i++) {
			this.keyButtons[i] = new GuiButtonTypedIndex(0, i, "");
			this.resetButtons[i] = new GuiButtonTypedIndex(1, i, I18n.format("controls.reset"));
		}

	}

	@Override
	protected boolean mousePressed(int index, KeyBinding entry, int mouseX, int mouseY,
			int mouseEvent, int relativeX, int relativeY) {
		//NotEnoughKeys.log(index + "");
		if (this.selectedIndex >= 0) {
			KeyBinding keyBinding = this.getElement(this.selectedIndex);
			int keyCode = mouseEvent - 100;

			List<KeyModifier> modifiers = new ArrayList<KeyModifier>();
			for (KeyModifier modifier : KeyModifier.MODIFIER_VALUES)
				if (modifier.isActive()) modifiers.add(modifier);
			keyBinding.setKeyModifierAndCode(keyCode, new KeyModifierSet(modifiers));

			this.mc.gameSettings.setOptionKeyBinding(keyBinding, keyCode);
			KeyBinding.resetKeyBindingArrayAndHash();

			this.selectedIndex = -1;

			return true;
		}
		else if (this.keyButtons[index].mousePressed(this.mc, mouseX, mouseY)) {
			//NotEnoughKeys.log(index + " key");
			this.selectedIndex = index;
			return true;
		}
		else if (this.resetButtons[index].mousePressed(this.mc, mouseX, mouseY)) {
			//NotEnoughKeys.log(index + " reset");
			entry.setToDefault();
			this.mc.gameSettings.setOptionKeyBinding(entry, entry.getKeyCodeDefault());
			KeyBinding.resetKeyBindingArrayAndHash();
			BindingHelper.setModifiers(entry, false, false, false);
			return true;
		}
		return false;
	}

	@Override
	protected void onElementClicked(int index, KeyBinding entry, boolean isDoubleClick, int mouseX,
			int mouseY) {
		/*
		NotEnoughKeys.log(entry.getKeyDescription());
		this.selectedIndex = index;
		*/
	}

	@Override
	protected boolean keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.selectedIndex >= 0 && !BindingHelper.isModifierKeyCode(keyCode)) {
			KeyBinding keyBinding = this.getElement(this.selectedIndex);

			if (keyCode == Keyboard.KEY_ESCAPE) {
				keyCode = 0;
			}
			else if (keyCode == 0 && typedChar > 0) {
				keyCode = typedChar + 256;
			}

			//keyBinding.setKeyModifierAndCode(KeyModifier.NONE, keyCode);
			if (keyCode == 0)
				//BindingHelper.setModifiers(keyBinding);
				keyBinding.setKeyModifierAndCode(keyCode);
			else{
				//BindingHelper.setModifiers(keyBinding, false, false, false);
				List<KeyModifier> modifiers = new ArrayList<KeyModifier>();
				for (KeyModifier modifier : KeyModifier.MODIFIER_VALUES)
					if (modifier.isActive()) modifiers.add(modifier);
				keyBinding.setKeyModifierAndCode(keyCode, new KeyModifierSet(modifiers));
			}
			this.mc.gameSettings.setOptionKeyBinding(keyBinding, keyCode);
			KeyBinding.resetKeyBindingArrayAndHash();

			this.selectedIndex = -1;
			return true;
		}
		return super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void drawEntry(int index, KeyBinding entry, int x, int y, int listWidth, int slotHeight,
			int mouseX, int mouseY, boolean isSelected) {
		GlStateManager.color(1, 1, 1, 1);

		//this.getParent().drawString(this.mc.fontRendererObj, "t", x, y, 0x00ff00);

		slotHeight += 4;
		int textHeight = y + slotHeight / 5 * 2;

		/** Button Description **/
		{
			this.getParent().drawString(this.mc.fontRendererObj,
					I18n.format(entry.getKeyDescription()),
					x, textHeight, 0xffffff
			);
		}

		/** Reset Button **/
		int resetWidth = 50;
		{
			this.mc.getTextureManager().bindTexture(GuiScrollPanelCategories.WIDGITS);
			this.resetButtons[index].xPosition = x + listWidth - resetWidth - 10;
			this.resetButtons[index].yPosition = y;
			this.resetButtons[index].width = resetWidth;
			this.resetButtons[index].height = slotHeight;
			this.resetButtons[index].enabled = !entry.isSetToDefaultValue();
			this.resetButtons[index].drawButton(this.mc, mouseX, mouseY);
		}

		/** KeyBinding Button **/
		int keyWidth = 180;//95;
		{
			this.mc.getTextureManager().bindTexture(GuiScrollPanelCategories.WIDGITS);
			this.keyButtons[index].xPosition = this.resetButtons[index].xPosition - 10 - keyWidth;
			this.keyButtons[index].yPosition = y;
			this.keyButtons[index].width = keyWidth;
			this.keyButtons[index].height = slotHeight;
			this.keyButtons[index].displayString = this.getButtonName(index, entry);
			this.keyButtons[index].drawButton(this.mc, mouseX, mouseY);
		}
		/** End **/

	}

	private String getButtonName(int index, KeyBinding keyBinding) {
		String display = "";
		for (KeyModifier modifier : KeyModifier.values()) {
			if (keyBinding.getKeyModifierSet().contains(modifier))
				display += modifier.name() + "+";
		}
		display += GameSettings.getKeyDisplayString(keyBinding.getKeyCode());
		if (this.selectedIndex == index) { // if is changing
			return TextFormatting.WHITE + "> " + TextFormatting.YELLOW + display + TextFormatting.WHITE + " <";
		}
		else {
			TextFormatting prefix = null;
			if (KeyBinding.hasConflictingModifier(keyBinding))
				prefix = TextFormatting.GOLD;
			else if (KeyBinding.hasConflictingKey(keyBinding))
				prefix = TextFormatting.RED;
			return (prefix != null ? prefix : "") + display;
		}
	}

	private class GuiButtonTypedIndex extends GuiButton {

		private final int type, index;

		GuiButtonTypedIndex(int type, int index, String content) {
			super(10 + (2 * index) + type, 0, 0, content);
			this.type = type;
			this.index = index;
		}

	}

}
