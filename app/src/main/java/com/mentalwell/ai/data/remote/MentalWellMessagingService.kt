package com.mentalwell.ai.data.remote

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

/**
 * Service to handle incoming Firebase Cloud Messaging notifications.
 */
class MentalWellMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Handle incoming messages containing mood insights or chat
        Log.d("FCM", "Message received from: ${message.from}")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Sync FCM token with Backend or Firestore
        Log.d("FCM", "New token generated: $token")
    }
}
