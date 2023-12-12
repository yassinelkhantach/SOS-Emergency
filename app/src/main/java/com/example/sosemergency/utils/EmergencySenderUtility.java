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
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Utility class for sending emergency SMS messages to contacts.
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
    public static void sendEmergencySmsToContacts(Context context, String emergencyType, LatLng currentLatLng) {
        List<Contact> contacts = ContactPersistenceManager.getContacts();

        if (contacts.isEmpty()) {
            showToast(context, "No contacts in the database");
            return;
        }

        for (Contact contact : contacts) {
            sendEmergencySms(context, contact.getPhoneNumber(), emergencyType);
        }

        showToast(context, "Emergency report sent to all contacts!");
    }

    /**
     * Sends emergency SMS messages to all contacts without specifying an emergency type.
     *
     * @param context The context of the application.
     */
    public static void sendEmergencySmsToContacts(Context context) {
        List<Contact> contacts = ContactPersistenceManager.getContacts();

        if (contacts.isEmpty()) {
            showToast(context, "No contacts in the database");
            return;
        }

        for (Contact contact : contacts) {
            sendEmergencySms(context, contact.getPhoneNumber(), null);
        }

        showToast(context, "Emergency report sent to all contacts!");
    }

    /**
     * Initiates the process of sending an emergency SMS.
     * Checks for SEND_SMS permission, and requests it if not granted.
     *
     * @param context     The context of the application.
     * @param phoneNumber The phone number of the contact to send the SMS.
     * @param emergencyType The type of emergency.
     */
    private static void sendEmergencySms(Context context, String phoneNumber, String emergencyType) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            sendSms(context, phoneNumber, emergencyType);
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
    private static void sendSms(Context context, String phoneNumber, String emergencyType) {
        String smsMessage = createSmsMessage(context, emergencyType, null);
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
        String locationSmsTemplate = context.getString(R.string.location_sms_template);

        StringBuilder smsMessageBuilder = new StringBuilder(String.format(emergencySmsTemplate, emergencyType));

        String mapsLink = createMapsLink(currentLatLng);
        if (!mapsLink.isEmpty()) {
            smsMessageBuilder.append("\n").append(String.format(locationSmsTemplate, mapsLink));
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
}