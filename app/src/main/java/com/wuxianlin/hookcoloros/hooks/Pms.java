package com.wuxianlin.hookcoloros.hooks;

import com.wuxianlin.hookcoloros.ColorOSUtils;

import java.io.File;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Pms {

    public static void hookAndroid(final XC_LoadPackage.LoadPackageParam lpparam,
                                   int colorOsVersion, XSharedPreferences prefs){
        try{
            if(prefs.getBoolean("hook_adb_install", true))
                hookAdbInstall(lpparam, colorOsVersion);
        }catch (Throwable t){
            XposedBridge.log(t);
        }
        try{
            if(prefs.getBoolean("hook_app_hide", true))
                hookAppHide(lpparam, colorOsVersion);
        }catch (Throwable t){
            XposedBridge.log(t);
        }
        try{
            if(prefs.getBoolean("hook_32bit_translator", true))
                hook32BitTranslator(lpparam, colorOsVersion);
        }catch (Throwable t){
            XposedBridge.log(t);
        }
    }

    private static void hookAdbInstall(final XC_LoadPackage.LoadPackageParam lpparam, int colorOsVersion){
        XposedHelpers.findAndHookMethod(colorOsVersion >= ColorOSUtils.OplusOS_11_2 ?
                        "com.android.server.pm.OplusPackageInstallInterceptManager" :
                        "com.android.server.pm.ColorPackageInstallInterceptManager",
                lpparam.classLoader, "readFromStatusFileLocked", File.class,
                XC_MethodReplacement.returnConstant(false));
    }

    private static void hookAppHide(final XC_LoadPackage.LoadPackageParam lpparam, int colorOsVersion){
        XposedHelpers.findAndHookMethod(colorOsVersion >= ColorOSUtils.OplusOS_11_2 ?
                        "com.android.server.pm.OplusOsPackageManagerHelper" :
                        "com.android.server.pm.ColorPackageManagerHelper", lpparam.classLoader,
                colorOsVersion >= ColorOSUtils.OplusOS_11_2 ? "isOplusDefaultAppPolicyEnabled" :
                        "isColorDefaultAppPolicyEnabled",
                XC_MethodReplacement.returnConstant(false));
        XposedHelpers.findAndHookMethod(colorOsVersion >= ColorOSUtils.OplusOS_11_2 ?
                        "com.android.server.pm.OplusRuntimePermGrantPolicyManager" :
                        "com.android.server.pm.ColorRuntimePermGrantPolicyManager",
                lpparam.classLoader,
                "getIgnoreAppList",
                XC_MethodReplacement.returnConstant(null));
        XposedHelpers.findAndHookMethod(colorOsVersion >= ColorOSUtils.OplusOS_11_2 ?
                        "com.android.server.pm.OplusPackageManagerServiceEx" :
                        "com.android.server.pm.ColorPackageManagerServiceEx",
                lpparam.classLoader, "needHideApp",
                String.class, boolean.class, boolean.class,
                XC_MethodReplacement.returnConstant(false));
        XposedBridge.hookAllMethods(XposedHelpers.findClass(colorOsVersion >= ColorOSUtils.OplusOS_11_2 ?
                                "com.android.server.pm.OplusForbidUninstallAppManager" :
                                "com.android.server.pm.ColorForbidUninstallAppManager",
                        lpparam.classLoader),
                "isForbidDeletePackage",
                XC_MethodReplacement.returnConstant(false));
    }

    private static void hook32BitTranslator(final XC_LoadPackage.LoadPackageParam lpparam, int colorOsVersion){
        if(colorOsVersion<ColorOSUtils.OS_14_0)
            return;
        XposedHelpers.findAndHookMethod(
                "com.android.server.pm.PackageManagerServiceExtImpl", lpparam.classLoader,
                "isTranslatorWhitelistApp", String.class, XC_MethodReplacement.returnConstant(true));
    }
}
