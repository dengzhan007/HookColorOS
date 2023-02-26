package com.wuxianlin.hookcoloros.hooks;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Launcher {

    public static void hookLauncher(final XC_LoadPackage.LoadPackageParam lpparam,
                                    int colorOsVersion, XSharedPreferences prefs) {
        if (prefs.getBoolean("hook_launcher_app_limit", true))
            hookLauncherAppLimit(lpparam);
    }

    private static void hookLauncherAppLimit(final XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("android.app.SharedPreferencesImpl",
                lpparam.classLoader, "getInt", String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                File mFile = (File) XposedHelpers.getObjectField(param.thisObject, "mFile");
                if (!mFile.getName().equals("Configuration.xml")) return;
                String key = (String) param.args[0];
                if (key.equals("lock_app_limit"))
                    param.setResult(1000);
            }
        });
    }
}
