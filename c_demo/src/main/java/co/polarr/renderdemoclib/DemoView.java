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

package co.polarr.renderdemoclib;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import co.polarr.render.PolarrRenderJni;

class DemoView extends GLSurfaceView {
    public DemoView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        setRenderer(new Renderer(context));

        prepareYUVDemoData(context);

        // run on non-gl thread
//        {
//            PolarrRenderJni.init(0,
//                    w, h,
//                    stride, scanline,
//                    0, yuvData, true);
//            PolarrRenderJni.renderTest();
//            PolarrRenderJni.getYUVData();
//            PolarrRenderJni.renderTest();
//            PolarrRenderJni.release();
//        }
    }

    private static void doFlow() {
        PolarrRenderJni.setYUVData(w, h, stride, scanline, yuvData);
        PolarrRenderJni.renderTest();
        PolarrRenderJni.getYUVData();
    }

    static int w = 0;
    static int h = 0;
    static int stride = 0;
    static int scanline = 0;
    static byte[] yuvData;

    private static void prepareYUVDemoData(Context context) {
        String fileName = "";

        int demoIndex = 0;

        switch (demoIndex) {

            case 0: {
                fileName = "yuv.dat";
                w = 960;
                h = 720;
                stride = 960;
                scanline = 720;
            }
        }

        yuvData = new byte[stride * scanline * 3 / 2];
        try {
            InputStream is = context.getAssets().open(fileName);
            is.read(yuvData);
            is.close();
        } catch (IOException e) {
            System.out.println("IoException:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class Renderer implements GLSurfaceView.Renderer {
        private final Context context;
        private int outputTex;

        public Renderer(Context context) {
            this.context = context;
        }

        public void onDrawFrame(GL10 gl) {
            GLES20.glClearColor(0, 0, 0, 1);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            doFlow();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
//            PolarrRenderJni.resize(width, height);
//            updateSize(outputTex, width, height);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // debug texture,width,height
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.rooftops);
//            if (bitmap != null)
            {
//                int width = bitmap.getWidth();
//                int height = bitmap.getHeight();
//                GLES20.glGenTextures(1, texParams, 0);
//                int intputTexture = genTexture(width, height);
//                int textureId = texParams[0];
//                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, intputTexture);
//                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);
//                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
//                outputTex = genOutputTexture(width, height);
//                PolarrRenderJni.init(intputTexture, width, height, outputTex);
//                bitmap.recycle();

                int intputTexture = genTexture(w, h);
                outputTex = genTexture(w, h);

                PolarrRenderJni.init(intputTexture,
                        w, h,
                        stride, scanline,
                        outputTex, yuvData, false);
            }
        }

        private static void checkGLError(String step) {
            int error = GLES20.glGetError();
            if (error != GLES20.GL_NO_ERROR) {
                Log.e(step, String.format("%x,", error));
            }
        }

        private int genTexture(int width, int height) {
            int[] textures = new int[1];
            GLES20.glGenTextures(1, textures, 0);

            int texture = textures[0];
            updateSize(texture, width, height);

            return texture;
        }

        private void updateSize(int texture, int width, int height) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, width, height,
                    0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);

            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }
}
