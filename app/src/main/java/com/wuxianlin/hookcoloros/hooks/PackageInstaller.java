package com.wuxianlin.hookcoloros.hooks;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class PackageInstaller {
    public static void hookPackageInstaller(final XC_LoadPackage.LoadPackageParam lpparam,
                                     int colorOsVersion, XSharedPreferences prefs) {
        XposedHelpers.findAndHookMethod("android.app.SharedPreferencesImpl",
                lpparam.classLoader, "getInt", String.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        File mFile = (File) XposedHelpers.getObjectField(param.thisObject, "mFile");
                        if (!mFile.getName().equals("rom_update.xml")) return;
                        String key = (String) param.args[0];
                        if ("account_switch".equals(key)||
                                "ad_switch".equals(key)||
                                "appdetail_switch".equals(key)||
                                "delete_apk".equals(key)||
                                "suggest_switch".equals(key)||
                                "version_check_switch".equals(key))
                            param.setResult(0);
                    }
                });
        /*XposedHelpers.findAndHookMethod("android.content.pm.OplusPackageManager",
                lpparam.classLoader, "isClosedSuperFirewall", XC_MethodReplacement.returnConstant(true));*/
    }
}
