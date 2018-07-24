package cn.connie.plugin.utils;

import android.content.Context;
import android.content.Intent;

import com.didi.virtualapk.PluginManager;

/**
 * Created by hinge on 2018/7/17.
 */

public final class PluginUtil {

    private PluginUtil() {
    }

    /**
     * 页面跳转
     *
     * @param context
     * @param packageName
     * @param clazzName
     */
    public static void toActivity(Context context, String packageName, String clazzName) {
        if (PluginManager.getInstance(context).getLoadedPlugin(packageName) == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(context, clazzName);
        context.startActivity(intent);
    }
}
