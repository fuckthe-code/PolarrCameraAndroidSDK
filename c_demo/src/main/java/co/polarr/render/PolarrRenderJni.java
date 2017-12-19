/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.polarr.render;

// Wrapper for native library

public class PolarrRenderJni {

     static {
          System.loadLibrary("PolarrRenderJNI");
     }

     public static native void init(int texId, int width, int height,
                                    int stride, int scanline,
                                    int outTexId, byte[] yuvArr, boolean needEgl);

     public static native void resize(int width, int height);

     public static native void renderTest();

     public static native void release();

     public static native byte[] getYUVData();
     public static native byte[] getRgbData();

     public static native void setYUVData(int width, int height,
                                          int stride, int scanline, byte[] yuvArr);
}
