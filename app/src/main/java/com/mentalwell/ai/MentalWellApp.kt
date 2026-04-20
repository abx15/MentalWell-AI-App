package com.mentalwell.ai

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.FirebaseApp
import com.mentalwell.ai.utils.Constants
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for MentalWell AI.
 * Handles the initialization of Firebase, Hilt, and core notification channels.
 */
@HiltAndroidApp
class MentalWellApp : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Create Notification Channels
        createNotificationChannel()
    }

    /**
     * Creates the primary notification channel for FCM notifications on Android O and above.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MentalWell Notifications"
            val descriptionText = "Channel for MentalWell AI mood and chat alerts."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.FCM_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
