package com.example.sosemergency.entities;

import android.content.Context;
import com.example.sosemergency.R;

/**
 * Enum representing different types of emergencies.
 */
public enum EmergencyType {
    FIRE(R.string.report_fire_icon_text, R.drawable.ic_fire_45dp),
    ACCIDENT(R.string.report_accident_icon_text, R.drawable.ic_car_accident_45dp),
    EARTHQUAKE(R.string.report_earthquake_icon_text, R.drawable.ic_earthquake_damage_45dp),
    CRIME(R.string.report_crime_icon_text, R.drawable.ic_crime_45dp),
    FLOOD(R.string.report_flood_icon_text, R.drawable.ic_flood_45dp),
    BRIBERY(R.string.report_bribery_icon_text, R.drawable.ic_bribery_45dp);

    private final int nameResourceId;  // Resource ID for the emergency name
    private final int iconResourceId;  // Resource ID for the emergency icon

    /**
     * Constructor for EmergencyType enum.
     *
     * @param nameResourceId Resource ID for the emergency name.
     * @param iconResourceId Resource ID for the emergency icon.
     */
    EmergencyType(int nameResourceId, int iconResourceId) {
        this.nameResourceId = nameResourceId;
        this.iconResourceId = iconResourceId;
    }

    /**
     * Get the localized name of the emergency.
     *
     * @param context The context used to retrieve the localized string.
     * @return The localized name of the emergency.
     */
    public String getName(Context context) {
        return context.getString(nameResourceId);
    }

    /**
     * Get the resource ID for the emergency icon.
     *
     * @return The resource ID for the emergency icon.
     */
    public int getIconResourceId() {
        return iconResourceId;
    }

    /**
     * Convert a string representation of emergency type to the corresponding enum value.
     *
     * @param emergencyTypeName The string representation of the emergency type.
     * @return The EmergencyType enum corresponding to the given string, or null if not found.
     */
    public static EmergencyType fromString(Context context, String emergencyTypeName) {
        for (EmergencyType emergencyType : values()) {
            if (emergencyType.getName(context).equalsIgnoreCase(emergencyTypeName)) {
                return emergencyType;
            }
        }
        return null;
    }
}