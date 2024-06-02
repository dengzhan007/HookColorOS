package com.wuxianlin.hookcoloros.hooks;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SystemUI {
    public static void hookVolume(final XC_LoadPackage.LoadPackageParam lpparam,
                                  int colorOsVersion, XSharedPreferences prefs) {
        try {
            XposedHelpers.findAndHookMethod("com.oplusos.systemui.common.feature.QSFeatureOption",
                    lpparam.classLoader, "isSupportVolumeSeekBar", XC_MethodReplacement.returnConstant(false));
        } catch (Throwable t) {
            XposedBridge.log(t);
        }
    }
}
