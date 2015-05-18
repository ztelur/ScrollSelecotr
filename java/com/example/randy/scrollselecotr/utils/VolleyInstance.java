package com.example.randy.scrollselecotr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyInstance
{
  private static Context mContext;
  private static VolleyInstance mInstance;
  private FileUtil fileUtil;
  private ImageLoader mImageLoader;
  private RequestQueue mRequestQueue;

 

  
  private VolleyInstance(Context paramContext)
  {
    mContext = paramContext;
    this.fileUtil = new FileUtil(paramContext);
    this.mRequestQueue = getRequestQueue();
    this.mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache()
    {
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int mMemoryCacheSize = this.maxMemory / 8;
      private final LruCache<String, Bitmap> cache = new LruCache(this.mMemoryCacheSize)
      {
        protected int sizeOf(String param, Bitmap bitmap)
        {
          return bitmap.getRowBytes() * bitmap.getHeight();
        }
      };

      public Bitmap getBitmap(String paramAnonymousString)
      {
        if (this.cache.get(paramAnonymousString) != null)
          return (Bitmap)this.cache.get(paramAnonymousString);
        if (VolleyInstance.this.fileUtil.isFileExsited(paramAnonymousString))
        {
          Bitmap localBitmap = VolleyInstance.this.fileUtil.getBitmap(paramAnonymousString);
          this.cache.put(paramAnonymousString, localBitmap);
        }
        return (Bitmap)this.cache.get(paramAnonymousString);
      }

      public void putBitmap(String paramAnonymousString, Bitmap paramAnonymousBitmap)
      {
        if (!VolleyInstance.this.fileUtil.isFileExsited(paramAnonymousString))
        	VolleyInstance.this.fileUtil.savaBitMap(paramAnonymousString, paramAnonymousBitmap);
        this.cache.put(paramAnonymousString, paramAnonymousBitmap);
      }
    });
  }

  public static VolleyInstance getInstance(Context paramContext)
  {
    try
    {
      if (mInstance == null)
        mInstance = new VolleyInstance(paramContext);
      VolleyInstance localMySingleton = mInstance;
      return localMySingleton;
    }
    finally
    {
    }
  }

  public <T> void addToRequestQueue(Request<T> paramRequest)
  {
    getRequestQueue().add(paramRequest);
  }

  public ImageLoader getImageloader()
  {
    return this.mImageLoader;
  }

  public RequestQueue getRequestQueue()
  {
    if (this.mRequestQueue == null)
      this.mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
    return this.mRequestQueue;
  }
}