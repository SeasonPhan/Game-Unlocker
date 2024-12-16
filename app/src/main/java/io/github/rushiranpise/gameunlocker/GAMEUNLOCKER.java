package io.github.rushiranpise.gameunlocker;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressLint("DiscouragedPrivateApi")
@SuppressWarnings("ConstantConditions")
public class GAMEUNLOCKER implements IXposedHookLoadPackage {

    private static final String TAG = GAMEUNLOCKER.class.getSimpleName();

    // Packages to spoof as a specific device
    private static final String[] packagesToSpoof = {
        "com.activision.callofduty.shooter",
        "com.pubg.krmobile",
        "com.miHoYo.GenshinImpact",
        "com.riotgames.league.wildrift",
        "com.google.android.googlequicksearchbox",
        "com.google.android.apps.bard",
        "flar2.devcheck"
    };

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        String packageName = loadPackageParam.packageName;

        if (Arrays.asList(packagesToSpoof).contains(packageName)) {
            spoofDeviceProperties();
            XposedBridge.log("Spoofed " + packageName + " with custom properties");
        }
    }

    private static void spoofDeviceProperties() {
        // General device properties
        //setPropValue("BRAND", "asus");
        //setPropValue("MANUFACTURER", "asus");
        //setPropValue("DEVICE", "AI2201");
        //setPropValue("MODEL", "ASUS_AI2201");

        // SDK-related properties
        setSDKPropValue("SDK_INT", 26); // Android 13
        setSDKPropValue("RELEASE", "8");
        //setSDKPropValue("CODENAME", "REL");

        XposedBridge.log("Device properties spoofed to ASUS ROG 6, Android 13");
    }

    private static void setPropValue(String key, Object value) {
        try {
            Log.d(TAG, "Setting property " + key + " to " + value);
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            XposedBridge.log("Failed to set property: " + key + "\n" + Log.getStackTraceString(e));
        }
    }

    private static void setSDKPropValue(String key, Object value) {
        try {
            Log.d(TAG, "Setting SDK property " + key + " to " + value);
            Field field = Build.VERSION.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            XposedBridge.log("Failed to set SDK property: " + key + "\n" + Log.getStackTraceString(e));
        }
    }
}
