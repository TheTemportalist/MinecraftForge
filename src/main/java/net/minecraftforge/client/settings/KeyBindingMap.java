package net.minecraftforge.client.settings;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.IntHashMap;

import java.util.*;

public class KeyBindingMap
{
    private static final IntHashMap<HashMap<KeyModifierSet, Collection<KeyBinding>>> map =
            new IntHashMap<HashMap<KeyModifierSet, Collection<KeyBinding>>>();

    public Collection<KeyBinding> lookupActive(int keyCode)
    {
        Set<KeyModifier> activeModifiers = KeyModifier.getActiveModifiers();
        if (!map.containsItem(keyCode))
            map.addKey(keyCode, new HashMap<KeyModifierSet, Collection<KeyBinding>>());
        HashMap<KeyModifierSet, Collection<KeyBinding>> keyLookupResult = map.lookup(keyCode);

        IntHashMap<Collection<KeyModifierSet>> res = new IntHashMap<Collection<KeyModifierSet>>();
        int greatestMatches = -1;
        for (KeyModifierSet modifierSet : keyLookupResult.keySet()) {
            int matches = modifierSet.getQuantityMatching(activeModifiers);
            if (!res.containsItem(matches))
                res.addKey(matches, new ArrayList<KeyModifierSet>());
            res.lookup(matches).add(modifierSet);
            if (matches > greatestMatches) greatestMatches = matches;
        }

        Collection<KeyBinding> bindings = new ArrayList<KeyBinding>();
        if (greatestMatches >= 0)
            for (KeyModifierSet modifierSet : res.lookup(greatestMatches)) {
                bindings.addAll(keyLookupResult.get(modifierSet));
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
