package com.wuxianlin.hookcoloros.hooks;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class PackageInstaller {
    public static void hookPackageInstaller(final XC_LoadPackage.LoadPackageParam lpparam,
                                     int colorOsVersion, XSharedPreferences prefs) {
        XposedHelpers.findAndHookMethod("android.content.pm.OplusPackageManager",
                lpparam.classLoader, "isClosedSuperFirewall", XC_MethodReplacement.returnConstant(true));
    }
}
