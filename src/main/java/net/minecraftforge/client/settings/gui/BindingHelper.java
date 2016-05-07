package net.minecraftforge.client.settings.gui;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.HashMap;

/**
 * Created by TheTemportalist on 4/12/2016.
 *
 * @author TheTemportalist
 */
public class BindingHelper {

	public static boolean hasModifierCtrl(KeyBinding keyBinding) {
		return keyBinding.getKeyModifierSet().contains(KeyModifier.CONTROL);
		//return keyBinding.getKeyModifier() == KeyModifier.CONTROL;
	}

	public static boolean hasModifierAlt(KeyBinding keyBinding) {
		return keyBinding.getKeyModifierSet().contains(KeyModifier.ALT);
		//return BindingHelper.modifiers.containsKey(keyBinding.getKeyDescription()) &&
		//		BindingHelper.modifiers.get(keyBinding.getKeyDescription())[1];
		//return keyBinding.getKeyModifier() == KeyModifier.ALT;
	}

	public static boolean hasModifierShift(KeyBinding keyBinding) {
		return keyBinding.getKeyModifierSet().contains(KeyModifier.SHIFT);
		//return BindingHelper.modifiers.containsKey(keyBinding.getKeyDescription()) &&
		//		BindingHelper.modifiers.get(keyBinding.getKeyDescription())[2];
		//return keyBinding.getKeyModifier() == KeyModifier.SHIFT;
	}

	public static boolean isConflictingKey(KeyBinding keyBinding) {
		return false;
	}

	public static boolean isConflictingModifier(KeyBinding keyBinding) {
		return false;
	}

	public static boolean isModifierKeyCode(int keyCode) {
		return isModifierCtrl(keyCode) || isModifierAlt(keyCode) || isModifierShift(keyCode);
	}

	public static boolean isModifierCtrl(int keyCode) {
		return KeyModifier.CONTROL.matches(keyCode);
	}

	public static boolean isModifierAlt(int keyCode) {
		return KeyModifier.ALT.matches(keyCode);
	}

	public static boolean isModifierShift(int keyCode) {
		return KeyModifier.SHIFT.matches(keyCode);
	}

	// ~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static final HashMap<String, Boolean[]> modifiers = new HashMap<String, Boolean[]>();

	static void setModifiers(KeyBinding keyBinding) {
		setModifiers(keyBinding, KeyModifier.CONTROL.isActive(),
				KeyModifier.ALT.isActive(), KeyModifier.SHIFT.isActive());
	}

	static void setModifiers(KeyBinding keyBinding, boolean ctrl, boolean alt, boolean shift) {
		BindingHelper.modifiers.put(keyBinding.getKeyDescription(), new Boolean[]{
			ctrl, alt, shift
		});
	}

}
