package com.gdcp.newsclient.utils;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;


/**
 * Created by asus- on 2017/9/21.
 */

public class DiskLruCacheUtils {
    private DiskLruCache diskLruCache = null;


    public DiskLruCacheUtils(Context context) {
        File cacheDir = getDiskCacheDir(context, "json");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        try {
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public boolean downloadUrlToStream(String urlString, OutputStream outputStream,InputStream inputStream) {

        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            in = new BufferedInputStream(inputStream, 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(key.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public void inputDiskCache(final String url,final String json) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String key = hashKeyForDisk(url);
                try {
                    DiskLruCache.Editor editor = diskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream out = editor.newOutputStream(0);
                        InputStream   in =  new ByteArrayInputStream(json.getBytes("UTF-8"));
                        if (downloadUrlToStream(url, out,in)) {
                            editor.commit();
                            Log.d("aaa", "ok");
                        } else {
                            editor.abort();
                        }
                    }
                    diskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void closeDisCache() {
        if (diskLruCache != null) {
            try {
                diskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteCache(){
        if (diskLruCache!=null){
            try {
                diskLruCache.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getCacheSize(){
        if (diskLruCache!=null){
            DecimalFormat df = new DecimalFormat("#.##");
            float d=diskLruCache.size()/(1024f*1024);
            return  df.format(d);
        }
        return "0";
    }
    public String getJsonString(String imageUrl) {
        String key = hashKeyForDisk(imageUrl);
        DiskLruCache.Snapshot snapShot = null;
        try {
            snapShot = diskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                return convertStreamToString(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                is.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }



        return sb.toString();

    }





}
