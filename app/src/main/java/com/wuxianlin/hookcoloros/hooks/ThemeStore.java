package com.wuxianlin.hookcoloros.hooks;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ThemeStore {
    public static void hookThemeStore(final XC_LoadPackage.LoadPackageParam lpparam,
                                    int colorOsVersion, XSharedPreferences prefs) {
        XposedHelpers.findAndHookMethod("com.oppo.cdo.card.theme.dto.vip.VipUserDto",
                lpparam.classLoader, "getVipStatus", XC_MethodReplacement.returnConstant(1));
        XposedHelpers.findAndHookMethod("com.oppo.cdo.card.theme.dto.vip.VipUserDto",
                lpparam.classLoader, "getVipDays", XC_MethodReplacement.returnConstant(999));
        XposedHelpers.findAndHookMethod("com.oppo.cdo.theme.domain.dto.response.PublishProductItemDto",
                lpparam.classLoader, "getIsVipAvailable", XC_MethodReplacement.returnConstant(1));
    }
}
