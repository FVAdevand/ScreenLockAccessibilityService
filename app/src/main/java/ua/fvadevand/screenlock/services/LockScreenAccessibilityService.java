package ua.fvadevand.screenlock.services;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.Collections;
import java.util.List;

import ua.fvadevand.screenlock.MainActivity;
import ua.fvadevand.screenlock.R;

public class LockScreenAccessibilityService extends AccessibilityService {

    public static final String ACTION_SCREEN_LOCK = "ACTION_SCREEN_LOCK";
    private static final String TAG = "LockScreenAccessibility";
    private static final String SHORTCUT_ID = "ua.fvadevand.screenlock_shortcut";
    private BroadcastReceiver mReceiver;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "onServiceConnected: ");
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        if (!shortcutManager.isRequestPinShortcutSupported()) return;
        List<ShortcutInfo> pinnedShortcuts = shortcutManager.getPinnedShortcuts();
        if (pinnedShortcuts.isEmpty()) {
            ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(this, SHORTCUT_ID)
                    .setShortLabel(getString(R.string.label_short_shortcut))
                    .setLongLabel(getString(R.string.label_long_shortcut))
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_shortcut))
                    .setIntent(new Intent(this, MainActivity.class)
                            .setAction(ACTION_SCREEN_LOCK))
                    .build();
            shortcutManager.requestPinShortcut(pinShortcutInfo, null);
        } else {
            if (!pinnedShortcuts.get(0).isEnabled()) {
                shortcutManager.enableShortcuts(Collections.singletonList(SHORTCUT_ID));
            }
        }
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(ACTION_SCREEN_LOCK)) {
                    performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN);
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(ACTION_SCREEN_LOCK));
    }

    @Override
    public boolean onUnbind(Intent intent) {
        ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
        if (shortcutManager.isRequestPinShortcutSupported()) {
            shortcutManager.disableShortcuts(Collections.singletonList(SHORTCUT_ID), getString(R.string.message_disabled_shortcut));
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        }
        return super.onUnbind(intent);
    }
}
