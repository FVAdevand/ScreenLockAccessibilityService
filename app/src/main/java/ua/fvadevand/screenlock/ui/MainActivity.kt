package ua.fvadevand.screenlock.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import androidx.localbroadcastmanager.content.LocalBroadcastManager

import ua.fvadevand.screenlock.services.LockScreenAccessibilityService

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == LockScreenAccessibilityService.ACTION_SCREEN_LOCK) {
            LocalBroadcastManager.getInstance(this)
                .sendBroadcast(
                    Intent(LockScreenAccessibilityService.ACTION_SCREEN_LOCK)
                        .setPackage(packageName)
                )
        }
        finish()
    }
}
