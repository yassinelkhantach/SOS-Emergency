package com.example.sosemergency.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sosemergency.R;
import com.example.sosemergency.entities.Contact;
import com.example.sosemergency.entities.EmergencyType;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Utility class for sending emergency SMS messages to contacts and services.
 */
public class EmergencySenderUtility {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    /**
     * Sends emergency SMS messages to all contacts.
     *
     * @param context       The context of the application.
     * @param emergencyType The type of emergency.
     * @param currentLatLng The current location in LatLng format.
     */
    public static void sendEmergencySmsToContacts(Context context, EmergencyType emergencyType, LatLng currentLatLng) {
        List<Contact> contacts = ContactPersistenceManager.getContacts();
        String emergencyTypeName = emergencyType != null ? getEmergencyTypeName(context, emergencyType) : null;

        if (contacts.isEmpty()) {
            showToast(context, "No contacts in the database");
            return;
        }

        for (Contact contact : contacts) {
            sendEmergency(context, contact.getPhoneNumber(), emergencyTypeName, currentLatLng);
        }

        showToast(context, "Emergency report sent to all contacts!");
    }

    /**
     * Sends emergency SMS messages to a specific service based on the emergency type.
     *
     * @param context       The context of the application.
     * @param emergencyType The type of emergency.
     * @param currentLatLng The current location in LatLng format.
     */
    public static void sendEmergencySmsToService(Context context, EmergencyType emergencyType, LatLng currentLatLng) {
        String[] serviceNames = context.getResources().getStringArray(R.array.emergency_service_name);
        String[] servicePhoneNumbers = context.getResources().getStringArray(R.array.emergency_service_phone);

        String emergencyTypeName = emergencyType.getName(context);

        // Find the index of the emergency type
        int index = -1;
        for (int i = 0; i < serviceNames.length; i++) {
            if (serviceNames[i].equalsIgnoreCase(emergencyTypeName)) {
                index = i;
                break;
            }
        }

        // If the emergency type is found, send SMS to the corresponding service
        if (index != -1 && index < servicePhoneNumbers.length) {
            String phoneNumber = servicePhoneNumbers[index];
            sendEmergency(context, phoneNumber, emergencyTypeName, currentLatLng);
            showToast(context, "Emergency report sent to the " + emergencyTypeName + " service!");
        } else {
            showToast(context, emergencyTypeName + " service not found for the given emergency type");
        }
    }

    /**
     * Initiates the process of sending an emergency SMS.
     * Checks for SEND_SMS permission and requests it if not granted.
     *
     * @param context       The context of the application.
     * @param phoneNumber   The phone number of the contact to send the SMS.
     * @param emergencyType The type of emergency.
     */
    private static void sendEmergency(Context context, String phoneNumber, String emergencyType, LatLng currentLatLng) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            sendSms(context, phoneNumber, emergencyType, currentLatLng);
        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions((AppCompatActivity) context,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
    }

    /**
     * Sends an emergency SMS to the specified phone number without including the location.
     *
     * @param context       The context of the application.
     * @param phoneNumber   The phone number of the contact to send the SMS.
     * @param emergencyType The type of emergency.
     */
    private static void sendSms(Context context, String phoneNumber, String emergencyType, LatLng currentLatLng) {
        String smsMessage = createSmsMessage(context, emergencyType, currentLatLng);
        SmsManager.getDefault().sendTextMessage(phoneNumber, null, smsMessage, null, null);
        showToast(context, "Emergency report sent!");
    }

    /**
     * Displays a toast message.
     *
     * @param context The context of the application.
     * @param message The message to display in the toast.
     */
    private static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Creates an emergency SMS message with the type and current location.
     *
     * @param context       The context of the application.
     * @param emergencyType The type of emergency.
     * @param currentLatLng The current location in LatLng format.
     * @return The formatted emergency SMS message.
     */
    private static String createSmsMessage(Context context, String emergencyType, LatLng currentLatLng) {
        String emergencySmsTemplate = context.getString(R.string.emergency_sms_template);
        String locationSmsTemplate = context.getString(R.string.emergency_location_sms_template);
        String typeSmsTemplate = context.getString(R.string.emergency_location_sms_template);

        StringBuilder smsMessageBuilder = new StringBuilder(emergencySmsTemplate);

        if (emergencyType != null) {
            smsMessageBuilder.append("\n").append(String.format(typeSmsTemplate, emergencyType));
        }

        if (currentLatLng != null) {
            String mapsLink = createMapsLink(currentLatLng);
            if (!mapsLink.isEmpty()) {
                smsMessageBuilder.append("\n").append(String.format(locationSmsTemplate, mapsLink));
            }
        }

        return smsMessageBuilder.toString();
    }

    /**
     * Creates a Google Maps link for the provided location in LatLng format.
     *
     * @param currentLatLng The current location in LatLng format.
     * @return The Google Maps link for the specified location.
     */
    private static String createMapsLink(LatLng currentLatLng) {
        return "https://www.google.com/maps/search/?api=1&query=" +
                currentLatLng.latitude + "," + currentLatLng.longitude;
    }

    /**
     * Retrieves the localized name of the emergency type.
     *
     * @param context       The context of the application.
     * @param emergencyType The type of emergency.
     * @return The localized name of the emergency type.
     */
    private static String getEmergencyTypeName(Context context, EmergencyType emergencyType) {
        switch (emergencyType) {
            case FIRE:
                return context.getString(R.string.report_fire_icon_text);
            case ACCIDENT:
                return context.getString(R.string.report_accident_icon_text);
            case EARTHQUAKE:
                return context.getString(R.string.report_earthquake_icon_text);
            case CRIME:
                return context.getString(R.string.report_crime_icon_text);
            case FLOOD:
                return context.getString(R.string.report_flood_icon_text);
            case BRIBERY:
                return context.getString(R.string.report_bribery_icon_text);
            default:
                return "";
        }
    }
}