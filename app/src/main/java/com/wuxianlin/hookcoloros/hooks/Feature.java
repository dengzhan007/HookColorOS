package com.wuxianlin.hookcoloros.hooks;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.wuxianlin.hookcoloros.HookUtils;

import java.io.File;
import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Feature {

    public static void hookPms(final XC_LoadPackage.LoadPackageParam lpparam,
                               int colorOsVersion, XSharedPreferences prefs){
        XposedHelpers.findAndHookMethod("com.android.server.pm.PackageManagerService",
                lpparam.classLoader, "hasSystemFeature",
                String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String name=(String)param.args[0];
                if(name.equals("oppo.systemui.highlight.nodeveloper")
                        ||name.equals("oppo.settings.verification.dialog.disallow")
                        ||name.equals("oppo.settings.account.dialog.disallow")
                        /*||name.equals("oppo.systemui.notdisadblenotification.dm")*/)
                    param.setResult(true);
                else if((Build.MODEL.endsWith("t")||Build.MODEL.endsWith("T00")||Build.MODEL.endsWith("T10")||Build.MODEL.endsWith("T20"))&&name.equals("oppo.common_center.lock.simcard"))
                    param.setResult(false);
            }
        });
    }

    public static void hookOplusFeature(final XC_LoadPackage.LoadPackageParam lpparam,
                                    int colorOsVersion, XSharedPreferences prefs) {
        XposedHelpers.findAndHookMethod("com.android.server.content.OplusFeatureConfigManagerService",
                lpparam.classLoader, "hasFeatureMap", String.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if("oplus.software.startup_strategy_restrict".equals(param.args[0])) {
                            param.setResult(false);
                        }
                        //XposedBridge.log("hasFeatureMap:"+param.args[0]);
                    }
                });
    }

    public static void update(){
        Uri uri = Uri.parse("content://com.oplus.customize.coreapp.configmanager.configprovider.AppFeatureProvider").buildUpon().appendPath("app_feature").build();
        for(String feature:new String[]{"com.android.systemui.highlight_nodeveloper",
                "com.android.settings.account_dialog.disable",
                "com.android.settings.verification_dialog.disable",
                "com.android.systemui.otg_auto_close_alarm_disable",
                "com.android.settings.need_show_2g3g"}) {
            Cursor cursor = HookUtils.getContext().getContentResolver()
                    .query(uri, null, "featurename=?", new String[]{feature}, null);
            if(cursor!=null&&cursor.getCount()>0)
                continue;
            ContentValues values = new ContentValues();
            values.put("featurename", feature);
            //values.put("parameters", "boolean=true");
            HookUtils.getContext().getContentResolver().insert(uri, values);
        }
    }

    public static void hookAppFeature(final XC_LoadPackage.LoadPackageParam lpparam,
                                      int colorOsVersion, XSharedPreferences prefs){
        XposedHelpers.findAndHookMethod(
                "com.oplus.customize.appfeature.configprovider.AppFeatureProvider",
                lpparam.classLoader, "query",
                Uri.class, String[].class, String.class, String[].class,String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String query = (String)param.args[2];
                String[] args = (String[])param.args[3];
                /*Cursor cursor = (Cursor)param.getResult();
                if("featurename=?".equals(query)){
                    cursor.getColumnIndex("parameters");
                }*/
                //XposedBridge.log("AppFeatureProvider.query:"+query+",args:"+
                //        Arrays.toString((String[])args));
            }
        });
        update();
    }

}
