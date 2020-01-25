package ua.fvadevand.screenlock.services

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.view.accessibility.AccessibilityEvent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ua.fvadevand.screenlock.R
import ua.fvadevand.screenlock.ui.MainActivity

class LockScreenAccessibilityService : AccessibilityService() {
    private val receiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != null && intent.action == ACTION_SCREEN_LOCK) {
                    performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
                }
            }
        }
    }

    companion object {
        const val ACTION_SCREEN_LOCK = "ua.fvadevand.screenlock.action.SCREEN_LOCK"
        private const val TAG = "LockScreenAccessibility"
        private const val SHORTCUT_ID = "ua.fvadevand.screenlock.shortcut.SCREEN_LOCK"
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

    }

    override fun onInterrupt() {

    }

    override fun onServiceConnected() {
        getSystemService(ShortcutManager::class.java)?.apply {
            if (!isRequestPinShortcutSupported) return
            if (pinnedShortcuts.isEmpty()) {
                val pinShortcutInfo =
                    ShortcutInfo.Builder(this@LockScreenAccessibilityService, SHORTCUT_ID)
                        .setShortLabel(getString(R.string.label_short_shortcut))
                        .setLongLabel(getString(R.string.label_long_shortcut))
                        .setIcon(
                            Icon.createWithResource(
                                this@LockScreenAccessibilityService,
                                R.mipmap.ic_shortcut
                            )
                        )
                        .setIntent(
                            Intent(this@LockScreenAccessibilityService, MainActivity::class.java)
                                .setAction(ACTION_SCREEN_LOCK)
                        )
                        .build()
                requestPinShortcut(pinShortcutInfo, null)
            } else if (!pinnedShortcuts[0].isEnabled) {
                enableShortcuts(listOf(SHORTCUT_ID))
            }
            LocalBroadcastManager.getInstance(this@LockScreenAccessibilityService)
                .registerReceiver(receiver, IntentFilter(ACTION_SCREEN_LOCK))
        }
    }

    override fun onUnbind(intent: Intent): Boolean {
        getSystemService(ShortcutManager::class.java)?.apply {
            if (isRequestPinShortcutSupported) {
                disableShortcuts(
                    listOf(SHORTCUT_ID),
                    getString(R.string.message_disabled_shortcut)
                )
                LocalBroadcastManager.getInstance(this@LockScreenAccessibilityService)
                    .unregisterReceiver(receiver)
            }
        }
        return super.onUnbind(intent)
    }
}
