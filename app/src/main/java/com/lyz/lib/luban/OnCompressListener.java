package com.lyz.lib.luban;

import android.graphics.Bitmap;

import java.util.List;

public interface OnCompressListener {

  /**
   * Fired when the compression is started, override to handle in your own code
   */
  void onStart();

  /**
   * Fired when a compression returns successfully, override to handle in your own code
   */
  void onSuccess(Bitmap file);

  /**
   * Fired when a compression fails to complete, override to handle in your own code
   */
  void onError(Throwable e);

  /**
   * add：2018-1-11 08:55:38
   */
  void onComplete(List<Bitmap> file);
}
