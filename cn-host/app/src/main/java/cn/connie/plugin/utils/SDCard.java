package cn.connie.plugin.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cn.connie.plugin.AppContext;


public class SDCard {

    private static String SECONDARY_SD_ROOT = null;
    private static String CHIEF_SD_ROOT = null;

    public static String getSDCardPath() {
        String sdcardRootPath = SDCard.getChiefSDCardRootPath();
        String secondSdcard = SDCard.getSecondarySDCardRootPath();
        long chiefSize = 0;
        long secondSize = 0;
        if (!TextUtils.isEmpty(sdcardRootPath)) {
            chiefSize = SDCard.getSDCardTotalSize(sdcardRootPath);
        }
        if (!TextUtils.isEmpty(secondSdcard)) {
            secondSize = SDCard.getSDCardTotalSize(secondSdcard);
        }
        return (chiefSize >= secondSize) ? sdcardRootPath : secondSdcard;
    }

    /**
     * 外置主要的存储卡路径
     */
    public static String getChiefSDCardRootPath() {
        if (TextUtils.isEmpty(CHIEF_SD_ROOT)) {
            CHIEF_SD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
            File file = new File(CHIEF_SD_ROOT);
            if (!file.exists()) {// 如果文件夹不存在，返回空
                CHIEF_SD_ROOT = null;
            }
        }
        return CHIEF_SD_ROOT;
    }

    /***
     * 外置次要的存储卡路径
     */
    public static String getSecondarySDCardRootPath() {
        if (TextUtils.isEmpty(SECONDARY_SD_ROOT)) {
            getExternalSDCardStoragePath();
        }
        return SECONDARY_SD_ROOT;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardTotalSize(String sdcardPath) {
        try {
            // 取得SDCard当前的状态
            String status = Environment.getExternalStorageState();
            if (status.equals(Environment.MEDIA_MOUNTED)) {
                android.os.StatFs statfs = new android.os.StatFs(sdcardPath);
                // 获取SDCard上BLOCK总数
                long nTotalBlocks = statfs.getBlockCountLong();
                // 获取SDCard上每个block的SIZE
                long nBlocSize = statfs.getBlockSizeLong();
                // 获取可供程序使用的Block的数量
                long nAvailaBlock = statfs.getAvailableBlocksLong();
                // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
                long nFreeBlock = statfs.getFreeBlocksLong();
                // 计算SDCard 总容量大小MB
                long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;
                // 计算 SDCard 剩余大小MB
                long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;
                return nSDTotalSize;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static String getExternalSDCardStoragePath() {
        File file = new File("/system/etc/vold.fstab");
        FileReader fr = null;
        BufferedReader br = null;
        String path = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            return null;
        }
        try {
            if (fr != null) {
                br = new BufferedReader(fr);
                String s = br.readLine();
                while (s != null) {
                    if (s.startsWith("dev_mount")) {
                        String[] tokens = s.split("\\s");
                        path = tokens[2];
                        if (!Environment.getExternalStorageDirectory().getAbsolutePath().equals(path)) {
                            break;
                        }
                    }
                    s = br.readLine();
                }
            }
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                return null;
            }
        }
        SECONDARY_SD_ROOT = path;
        return path;
    }

    /**
     * 获取cache目录路径
     */
    public static File getDiskCachePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            return new File(AppContext.getContext().getExternalCacheDir().getPath());
        } else {
            return new File(AppContext.getContext().getCacheDir().getPath());
        }
    }
}
