package net.minecraftforge.client.settings;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.IntHashMap;
import net.minecraftforge.fml.common.FMLLog;

import java.util.*;

public class KeyBindingMap
{
    private static final IntHashMap<HashMap<KeyModifierSet, Collection<KeyBinding>>> map =
            new IntHashMap<HashMap<KeyModifierSet, Collection<KeyBinding>>>();

    public Collection<KeyBinding> lookupActive(int keyCode)
    {

        // Check to see if any keybindings have said keycode
        if (!map.containsItem(keyCode))
        {
            return new ArrayList<KeyBinding>();
        }

        /** All the keybindings with a certain keycode, keyed by their modifiers */
        HashMap<KeyModifierSet, Collection<KeyBinding>> keyLookupResult = map.lookup(keyCode);

        // The currently pressed modifiers
        Set<KeyModifier> activeModifiers = KeyModifier.getActiveModifiers();
        // The set with the most matches
        KeyModifierSet greatestMatchingSet = null;
        // The most matches found
        int greatestMatches = -1;
        for (KeyModifierSet modifierSet : keyLookupResult.keySet())
        {
            // get the number of matches between the active modifiers and the current set
            int matches = modifierSet.getQuantityMatching(activeModifiers);
            if (matches > greatestMatches)
            {
                greatestMatches = matches;
                greatestMatchingSet = modifierSet;
            }
        }

        Collection<KeyBinding> bindings = new ArrayList<KeyBinding>();
        if (greatestMatches >= 0 && greatestMatches >= greatestMatchingSet.size())
        {
            bindings.addAll(keyLookupResult.get(greatestMatchingSet));
        }
        return bindings;
    }

    public List<KeyBinding> lookupAll(int keyCode)
    {
        List<KeyBinding> matchingBindings = new ArrayList<KeyBinding>();
        if (!map.containsItem(keyCode))
            map.addKey(keyCode, new HashMap<KeyModifierSet, Collection<KeyBinding>>());
        for (Collection<KeyBinding> bindings : map.lookup(keyCode).values())
        {
            matchingBindings.addAll(bindings);
        }
        return matchingBindings;
    }

    public List<KeyBinding> lookupAllModifiers(int keyCode, KeyModifierSet modifiers)
    {
        List<KeyBinding> bindings = new ArrayList<KeyBinding>();

        /** All the keybindings with a certain keycode, keyed by their modifiers */
        HashMap<KeyModifierSet, Collection<KeyBinding>> keyLookupResult = map.lookup(keyCode);
        int mostModifiers = modifiers.size();
        for (KeyModifierSet modifierSet : keyLookupResult.keySet())
        {
            if (modifierSet.containsAll(modifiers))
            {
                bindings.addAll(keyLookupResult.get(modifierSet));
                if (modifierSet.size() > mostModifiers)
                    mostModifiers = modifierSet.size();
            }
        }
        if (mostModifiers == 0)
            return new ArrayList<KeyBinding>();

        return bindings;
    }

    public void addKey(int keyCode, KeyBinding keyBinding)
    {
        KeyModifierSet modifierSet = keyBinding.getKeyModifierSet();
        if (!map.containsItem(keyCode))
            map.addKey(keyCode, new HashMap<KeyModifierSet, Collection<KeyBinding>>());
        HashMap<KeyModifierSet, Collection<KeyBinding>> bindingsMap = map.lookup(keyCode);
        if (!bindingsMap.containsKey(modifierSet))
            bindingsMap.put(modifierSet, new ArrayList<KeyBinding>());
        bindingsMap.get(modifierSet).add(keyBinding);
    }

    public void removeKey(KeyBinding keyBinding)
    {
        KeyModifierSet modifierSet = keyBinding.getKeyModifierSet();
        int keyCode = keyBinding.getKeyCode();
        if (!map.containsItem(keyCode))
            map.addKey(keyCode, new HashMap<KeyModifierSet, Collection<KeyBinding>>());
        HashMap<KeyModifierSet, Collection<KeyBinding>> bindingsMap = map.lookup(keyCode);
        if (bindingsMap.containsKey(modifierSet))
        {
            Collection<KeyBinding> bindingsForKey = bindingsMap.get(modifierSet);
            bindingsForKey.remove(keyBinding);
            if (bindingsForKey.isEmpty())
                bindingsMap.remove(modifierSet);
            if (bindingsMap.isEmpty())
                map.removeObject(keyCode);
        }
    }

    public void clearMap()
    {
        map.clearMap();
    }
}
