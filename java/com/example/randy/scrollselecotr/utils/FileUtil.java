package com.example.randy.scrollselecotr.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.widget.Toast;


public class FileUtil
{
  private static final String FLODER_NAME = "/swtcache";
  private static String mDataRootPath = null;
  private static String sdPath = Environment.getExternalStorageDirectory().getPath();
  private Context context;

  public FileUtil(Context paramContext)
  {
    this.context = paramContext;
    mDataRootPath = paramContext.getCacheDir().getPath();
  }

  public static long getAvailableExternalMemorySize(File paramFile)
  {
    StatFs localStatFs = new StatFs(paramFile.getPath());
    long l1 = localStatFs.getBlockSize();
    long l2 = localStatFs.getAvailableBlocks();
    return l2 * l1;
  }

  public static String getStorageDirectory()
  {
      String path=sdPath+"/swtcache";
      checkRootDIrExsited(path);
      return path;
  }

  public static long getTotalInternalMemorySize()
  {
    StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
    return localStatFs.getBlockSize() * localStatFs.getBlockCount();
  }

  public static boolean isExternalStorageAvailable()
  {
    return Environment.getExternalStorageState().equals("mounted");
  }

    /**
     * ɾ����Ŀ���ļ����µ�������ݵ��ļ�
     * @return
     */
  public static boolean deleteAllFile()
  {
    File localFile = new File(getStorageDirectory());
    String[] arrayOfString = localFile.list();
    int length = arrayOfString.length;
    if ((localFile.exists()) && (localFile.isDirectory()))
    {
      arrayOfString = localFile.list();
      for(int i = 0; i < length;i++){
      	File file = new File(localFile + File.separator + arrayOfString[i]);
        deleteFile(file);
      }
      return true;
    }
    return false;
  }

    /**
     * ɾ��ĳһ���ļ�
     * @param file
     * @return
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        } else {

        }
    }

  public Bitmap getBitmap(String paramString)
  {
    String str = paramString.replaceAll("[^\\w]", "");
    return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + str + ".png");
  }

  public long getFileSize(String paramString)
  {
    String str = paramString.replaceAll("[^\\w]", "");
    return new File(getStorageDirectory() + File.separator + str).length();
  }
  public static void checkRootDIrExsited(String paramString) {
      File file=new File(paramString);
      if(!file.exists()) {
          mkRootDir(paramString);

      } else {
      }
  }
  public static void mkRootDir(String paramString) {
      File file=new File(paramString);
      file.mkdirs();
  }
  public boolean isFileExsited(String paramString)
  {
    String str = paramString.replaceAll("[^\\w]", "");
    boolean bool = new File(getStorageDirectory() + File.separator + str + ".png").exists();
    //Toast.makeText(this.context, getStorageDirectory() + File.separator + str + ": " + bool, 0).show();
    return bool;
  }

  public void savaBitMap(String paramString, Bitmap bitmap)
  {
	  String str = paramString.replaceAll("[^\\w]", "");
	  File f = new File(getStorageDirectory() + File.separator + str + ".png");
      try {
	      f.createNewFile();
	      FileOutputStream fOut = null;
          fOut = new FileOutputStream(f);
          bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
          fOut.flush();
          fOut.close();
      } catch (Exception e) {
              e.printStackTrace();
      }


    
  }

    /**
     * ��ȡ�ļ���url,����ļ�������,������""
     * @param paramString
     * @return
     */
    public String getBitMapURL(String paramString) {
        String str = paramString.replaceAll("[^\\w]", "");
        String path=getStorageDirectory() + File.separator + str + ".png";
        File f = new File(path);
        if (f.exists()) {
            return path;
        } else {
            return "";
        }
    }
}