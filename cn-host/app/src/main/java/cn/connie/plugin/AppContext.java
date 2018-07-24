package cn.connie.plugin;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.didi.virtualapk.PluginManager;

import java.io.File;

import cn.connie.plugin.utils.AppUtil;
import cn.connie.plugin.utils.Constants;
import cn.connie.plugin.utils.ResPathCenter;

/**
 * Created by hinge on 2018/7/17.
 */

public class AppContext extends Application {

    private static AppContext mApp;
    private static volatile boolean mInit;

    public static AppContext getContext() {
        return mApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginManager.getInstance(base).init();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = AppUtil.getAppName(this);
        if (!TextUtils.equals(processName, getPackageName()) || mInit) {
            return;
        }
        init();
    }

    private void init() {
        mInit = true;
        mApp = this;

        loadPlugin(this);
    }

    /**
     * 加载插件
     *
     * @param base
     */
    private void loadPlugin(Context base) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return;
        }
        PluginManager pluginManager = PluginManager.getInstance(base);
        String pluginPath = ResPathCenter.getInstance().getPluginPath();
        File apkFile = new File(pluginPath, Constants.PLUGIN_APK_NAME);
        if (apkFile.exists()) {
            try {
                pluginManager.loadPlugin(apkFile);
            } catch (Exception e) {
                Log.e(BuildConfig.APPLICATION_ID, "loadPlugin failed...", e);
            }
        }
    }


}
