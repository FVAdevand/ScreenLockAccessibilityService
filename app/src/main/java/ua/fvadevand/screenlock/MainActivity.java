package ua.fvadevand.screenlock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import ua.fvadevand.screenlock.services.LockScreenAccessibilityService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getAction() != null && getIntent().getAction().equals(LockScreenAccessibilityService.ACTION_SCREEN_LOCK)) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LockScreenAccessibilityService.ACTION_SCREEN_LOCK));
        }
        finish();
    }
}
