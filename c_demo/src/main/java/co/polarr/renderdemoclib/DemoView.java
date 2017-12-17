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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
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
    }

    private static class Renderer implements GLSurfaceView.Renderer {
        private final Context context;
        private int outputTex;
        private byte[] yuvData;

        public Renderer(Context context) {
            this.context = context;
        }

        public void onDrawFrame(GL10 gl) {
            GLES20.glClearColor(0, 0, 0, 1);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            PolarrRenderJni.renderTest();
            PolarrRenderJni.getYUVData();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            PolarrRenderJni.resize(width, height);
            updateSize(outputTex, width, height);
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

                int w = 960;
                int h = 720;
                yuvData = new byte[w * h * 3 / 2];
                try {
                    InputStream is = context.getAssets().open("yuv.dat");
                    is.read(yuvData);
                    is.close();
                } catch (IOException e) {
                    System.out.println("IoException:" + e.getMessage());
                    e.printStackTrace();
                }

                int intputTexture = genTexture(w, h);
                outputTex = genTexture(w, h);

                PolarrRenderJni.init(intputTexture, w, h, outputTex, yuvData);

                PolarrRenderJni.renderTest();
                yuvData = PolarrRenderJni.getYUVData();
                PolarrRenderJni.setYUVData(w, h, yuvData);

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
