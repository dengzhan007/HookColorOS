package com.wuxianlin.hookcoloros.hooks;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.wuxianlin.hookcoloros.ColorOSUtils;
import com.wuxianlin.hookcoloros.HookUtils;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MultiApp {
    public static void hookAndroid(final XC_LoadPackage.LoadPackageParam lpparam,
                                   int colorOsVersion, XSharedPreferences prefs) {
        if (colorOsVersion >= ColorOSUtils.OplusOS_11_1) {
            try{
                if(prefs.getBoolean("hook_multiapp_limit", true))
                    hookMultiAppConfig(lpparam, colorOsVersion);
            }catch (Throwable t){
                XposedBridge.log(t);
            }
        }
    }

    private static void hookMultiAppConfig(final XC_LoadPackage.LoadPackageParam lpparam, int colorOsVersion) throws Throwable{
        XposedHelpers.findAndHookMethod("com.oplus.multiapp.OplusMultiAppConfig", lpparam.classLoader, "getMaxCreatedNum", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(1000);
            }
        });
        XposedHelpers.findAndHookMethod("com.oplus.multiapp.OplusMultiAppConfig", lpparam.classLoader, "getAllowedPkgList", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Context systemContext = HookUtils.getContext();
                PackageManager pm = systemContext.getPackageManager();
                List<PackageInfo> packs = pm.getInstalledPackages(0);
                List<String> mMultiAppList = new ArrayList();
                for (PackageInfo pi : packs) {
                    if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 &&
                            (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) {
                        mMultiAppList.add(pi.packageName);
                    }
                }
                if (!mMultiAppList.isEmpty()) param.setResult(mMultiAppList);
            }
        });
    }
}
