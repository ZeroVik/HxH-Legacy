package com.example.hxhmod.nen;

import net.minecraft.nbt.CompoundTag;

/**
 * Enum representing the different Nen types from Hunter x Hunter
 */
public enum NenType {
    ENHANCER("enhancer", "Enhances natural abilities and objects", 100, 80, 60, 60, 80, 0),
    TRANSMUTER("transmuter", "Changes aura properties", 80, 100, 60, 80, 60, 0),
    CONJURER("conjurer", "Creates objects from aura", 60, 80, 100, 60, 40, 0),
    EMITTER("emitter", "Projects aura away from body", 60, 80, 60, 100, 40, 0),
    MANIPULATOR("manipulator", "Controls objects or living things", 60, 60, 40, 60, 100, 0),
    SPECIALIST("specialist", "Unique abilities outside other categories", 0, 0, 0, 0, 0, 100);

    private final String name;
    private final String description;

    // Efficiency percentages for using abilities of other types
    private final int enhancerEfficiency;
    private final int transmuterEfficiency;
    private final int conjurerEfficiency;
    private final int emitterEfficiency;
    private final int manipulatorEfficiency;
    private final int specialistEfficiency;

    NenType(String name, String description, int enhancerEfficiency, int transmuterEfficiency,
            int conjurerEfficiency, int emitterEfficiency, int manipulatorEfficiency, int specialistEfficiency) {
        this.name = name;
        this.description = description;
        this.enhancerEfficiency = enhancerEfficiency;
        this.transmuterEfficiency = transmuterEfficiency;
        this.conjurerEfficiency = conjurerEfficiency;
        this.emitterEfficiency = emitterEfficiency;
        this.manipulatorEfficiency = manipulatorEfficiency;
        this.specialistEfficiency = specialistEfficiency;
    }

    /**
     * Get efficiency percentage when using abilities of another Nen type
     * @param otherType The Nen type to check efficiency against
     * @return Efficiency percentage (0-100)
     */
    public int getEfficiencyFor(NenType otherType) {
        switch (otherType) {
            case ENHANCER:
                return enhancerEfficiency;
            case TRANSMUTER:
                return transmuterEfficiency;
            case CONJURER:
                return conjurerEfficiency;
            case EMITTER:
                return emitterEfficiency;
            case MANIPULATOR:
                return manipulatorEfficiency;
            case SPECIALIST:
                return specialistEfficiency;
            default:
                return 0;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get a NenType from NBT data
     */
    public static NenType fromNBT(CompoundTag nbt) {
        int ordinal = nbt.getInt("NenType");
        NenType[] types = values();
        return ordinal >= 0 && ordinal < types.length ? types[ordinal] : ENHANCER;
    }

    /**
     * Save NenType to NBT data
     */
    public void toNBT(CompoundTag nbt) {
        nbt.putInt("NenType", ordinal());
    }
}