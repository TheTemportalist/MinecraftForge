package net.minecraftforge.client.settings;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class KeyModifierSet
{

    public static final KeyModifierSet NONE = new KeyModifierSet();

    private final EnumSet<KeyModifier> modifiers;

    public KeyModifierSet(KeyModifier... modifiers)
    {
        this(Arrays.asList(modifiers));
    }

    public KeyModifierSet(List<KeyModifier> modifiers)
    {
        this.modifiers = EnumSet.allOf(KeyModifier.class);
        this.modifiers.addAll(modifiers);
    }

    public boolean contains(KeyModifier modifier)
    {
        return this.modifiers.contains(modifier);
    }

    public int getQuantityMatching(Set<KeyModifier> modifiers)
    {
        int matches = 0;
        for (KeyModifier modifier : modifiers)
            if (this.modifiers.contains(modifier))
                matches++;
        return matches;
    }

    public boolean isActive()
    {
        for (KeyModifier modifier : this.modifiers)
            if (!modifier.isActive())
                return false;
        return true;
    }

    public boolean contains(int keyCode)
    {
        if (KeyModifier.isKeyCodeModifier(keyCode))
        {
            for (KeyModifier modifier : this.modifiers)
                if (modifier.matches(keyCode))
                    return true;
        }
        return false;
    }

    public int size()
    {
        return this.modifiers == null ? 0 : this.modifiers.size();
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        for (KeyModifier m : this.modifiers)
            hashCode += 1 << m.ordinal();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof KeyModifierSet)
        {
            KeyModifierSet other = (KeyModifierSet) obj;
            if (this.modifiers.size() == other.modifiers.size())
            {
                return this.modifiers.containsAll(other.modifiers) &&
                        other.modifiers.containsAll(this.modifiers);
            }
        }
        return false;
    }

    public boolean containsAll(KeyModifierSet set)
    {
        return this.modifiers.containsAll(set.modifiers);
    }

}
