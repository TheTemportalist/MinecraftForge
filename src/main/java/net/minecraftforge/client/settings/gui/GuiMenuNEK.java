package net.minecraftforge.client.settings.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TheTemportalist on 4/10/2016.
 *
 * @author TheTemportalist
 */
public class GuiMenuNEK extends GuiScrollContainer<String> {

	private Runnable[] sidebarActions;

	private String[] keyBindingCategories;
	private HashMap<String, KeyBinding[]> keyBindingsByCategory;

	private final GuiOptions optionsGui;
	private final GameSettings settings;

	public GuiMenuNEK(GuiOptions options, GameSettings settings) {
		this.optionsGui = options;
		this.settings = settings;

		this.keyBindingsByCategory = this.fetchKeyBindingCategories();
		this.keyBindingCategories = this.keyBindingsByCategory.keySet().toArray(new String[0]);
	}

	private HashMap<String, KeyBinding[]> fetchKeyBindingCategories() {
		HashMap<String, List<KeyBinding>> categoryMap = new HashMap<String, List<KeyBinding>>();
		for (KeyBinding keyBinding : Minecraft.getMinecraft().gameSettings.keyBindings) {
			if (!categoryMap.containsKey(keyBinding.getKeyCategory()))
				categoryMap.put(keyBinding.getKeyCategory(), new ArrayList<KeyBinding>());
			categoryMap.get(keyBinding.getKeyCategory()).add(keyBinding);
		}

		HashMap<String, KeyBinding[]> categories = new HashMap<String, KeyBinding[]>();
		for (Map.Entry<String, List<KeyBinding>> entry : categoryMap.entrySet()) {
			categories.put(entry.getKey(), entry.getValue().toArray(new KeyBinding[0]));
		}

		return categories;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.initSidebar();
		this.buttonList.add(new GuiButton(1337, width / 2 - 100, height - 28, I18n.format("gui.done")));
	}

	private void initSidebar() {
		this.initSidebarActions();

		int boxTop = 63;
		int boxBottom = this.height - 32;
		int labelHeight = 20;
		int centerY = (boxTop + boxBottom) / 2;
		int spread = 10;

		String[] sidebarLabels = new String[] {"Conflicts", "Export", "Import"};

		int startY = centerY;
		startY -= (labelHeight * sidebarLabels.length) / 2;
		startY -= (spread * (sidebarLabels.length - 1)) / 2;

		for (int i = 0; i < sidebarLabels.length; i++) {
			this.buttonList.add(new GuiButton(i, 0, startY + (labelHeight + spread) * i, 75, labelHeight, sidebarLabels[i]));
		}

	}

	private void initSidebarActions() {
		this.sidebarActions = new Runnable[]{
				new Runnable() {
					@Override public void run() {
						onClick_Conflicts();
					}
				},
				new Runnable() {
					@Override public void run() {
						onClick_Export();
					}
				},
				new Runnable() {
					@Override public void run() {
						onClick_Import();
					}
				}
		};
	}

	private void onClick_Conflicts() {

	}

	private void onClick_Export() {

	}

	private void onClick_Import() {

	}

	@Override
	protected GuiScrollPanel<String> createScrollPanel() {
		return new GuiScrollPanelCategories(this, this.keyBindingCategories, this.keyBindingsByCategory);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id < this.sidebarActions.length)
			this.sidebarActions[button.id].run();
		else if (button.id == 1337)
			this.mc.displayGuiScreen(this.optionsGui);
	}

	@Override
	protected void drawScreenPre(int mouseX, int mouseY, float partialTicks) {
		this.drawCenteredString(this.fontRendererObj, "Controls", this.width / 2, 5, 0xffffff);
	}

}
