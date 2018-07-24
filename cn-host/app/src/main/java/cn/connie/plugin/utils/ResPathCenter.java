package cn.connie.plugin.utils;


import java.io.File;

/**
 * 路径管理中心
 */
public class ResPathCenter {

    private static final String ROOT_PATH = "cn-host";
    private static final String PLUGIN = "plugin";
    private static final String IMAGE = "image";
    private static final String DATA = "data";
    private static final String DOWNLOAD = "download";
    private static final String LOG = "log";
    private final String STR_SDCARD_ROOT;
    private static ResPathCenter mInstance;

    private ResPathCenter() {
        STR_SDCARD_ROOT = SDCard.getSDCardPath();
    }

    public String getSDCardPath() {
        return STR_SDCARD_ROOT;
    }

    public synchronized static ResPathCenter getInstance() {
        if (mInstance == null) {
            mInstance = new ResPathCenter();
        }
        return mInstance;
    }


    /**
     * 获取数据根目录。
     *
     * @return 数据根目录。
     */
    public String getRootPath() {
        String path = new StringBuffer().append(STR_SDCARD_ROOT).append(File.separator).append(ROOT_PATH).toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 功能:获取当前用户的数据目录 /sdcard/pieplus/data/username
     */
    public String getDataPath() {
        String path = new StringBuffer().append(getRootPath()).append(File.separator).append(DATA).toString();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }


    public String getImagePath() {
        String imagePath = new StringBuffer().append(getDataPath()).append(File.separator).append(IMAGE).toString();
        File file = new File(imagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return imagePath;
    }


    public String getDownloadPath() {
        StringBuilder sb = new StringBuilder();
        String apkPath = sb.append(getDataPath()).append(File.separator).append(DOWNLOAD).toString();
        File file = new File(apkPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return apkPath;
    }
    public String getPluginPath(){
        StringBuilder sb = new StringBuilder();
        String apkPath = sb.append(getDataPath()).append(File.separator).append(PLUGIN).toString();
        File file = new File(apkPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return apkPath;
    }


    public String getLogPath() {
        StringBuilder sb = new StringBuilder();
        String logFile = sb.append(getDataPath()).append(File.separator).append(LOG).toString();
        File file = new File(logFile);
        if (!file.exists()) {
            file.mkdirs();
        }
        return logFile;
    }
}
