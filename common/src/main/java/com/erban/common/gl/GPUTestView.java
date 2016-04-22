/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.erban.common.gl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import static javax.microedition.khronos.egl.EGL10.EGL_ALPHA_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_BLUE_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_DEPTH_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_GREEN_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_NONE;
import static javax.microedition.khronos.egl.EGL10.EGL_RED_SIZE;
import static javax.microedition.khronos.egl.EGL10.EGL_STENCIL_SIZE;

public class GPUTestView extends GLSurfaceView {
    private static final String TAG = "GPUTestView";
    public static final int MSG_GPU_TEST_FINISH = 0x1230;
    private String _gpuvendor, _gpumodel;
    private Handler _handler;

    public GPUTestView(Context context) {
        super(context);
        init();
    }

    public GPUTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void defaultConfig() {
        setEGLConfigChooser(new EGLConfigChooser() {
            @Override
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                // TODO Auto-generated method stub

                int[] num_config = new int[1];

                boolean check = false;

                int[] configs = new int[]{
                        EGL_DEPTH_SIZE, 16,
                        EGL_STENCIL_SIZE, 0,
                        EGL_RED_SIZE, 8,
                        EGL_GREEN_SIZE, 8,
                        EGL_BLUE_SIZE, 8,
                        EGL_ALPHA_SIZE, 8,
                        EGL10.EGL_RENDERABLE_TYPE, 4,
                        EGL_NONE
                };

                check = egl.eglInitialize(display, new int[]{2, 0});
                if (!check) {
                    Log.e(TAG, "default eglInitialize failed");
                    sendMessageToMainThread("N/A");
                    return null;
                }

                check = false;

                egl.eglChooseConfig(display, configs, null, 0, num_config);

                int configSize = num_config[0];
                if (configSize < 0) {
                    Log.e(TAG, "default eglChooseConfig failed step1");
                    sendMessageToMainThread("N/A");
                    return null;
                }

                EGLConfig[] mEGLConfigs = new EGLConfig[configSize];

                try {
                    check = egl.eglChooseConfig(display, configs, mEGLConfigs, 1, num_config);
                    if (!check) {
                        Log.e(TAG, "default eglChooseConfig failed step2");
                        sendMessageToMainThread("N/A");
                        return null;
                    }
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                    sendMessageToMainThread("N/A");
                    return null;
                }


                return mEGLConfigs[0];
            }
        });
    }

    private void init() {
        // Tell the surface view we want to create an OpenGL ES 2.0-compatible
        // context, and set an OpenGL ES 2.0-compatible renderer.
        setEGLContextClientVersion(2);

        defaultConfig();
        getHolder().setFormat(PixelFormat.RGBA_8888);

        setRenderer(new GPUTestRenderer());
    }

    public void startTest(Handler handler) {
        _handler = handler;
        requestRender();
    }

    public String getGPUVendor() {
        return _gpuvendor;
    }

    public String getGPUModel() {
        return _gpumodel;
    }

    public void sendMessageToMainThread(String content) {
        if (_handler != null) {
            Message msg = Message.obtain(_handler, MSG_GPU_TEST_FINISH, content);
            _handler.sendMessage(msg);
        }
    }

    ///////////////////////////////////////////////////////
    public class GPUTestRenderer implements Renderer {

        public GPUTestRenderer() {

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

//			IntBuffer maxTextureSize = IntBuffer.allocate(4);
//			gl.glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS, maxTextureSize);		
//			Log.d(TAG, "texture " + maxTextureSize.get(0));
//			
//			gl.glGetIntegerv(GLES20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS, maxTextureSize);	
//			Log.d(TAG, "vertex " + maxTextureSize.get(0));
//			
//			gl.glGetIntegerv(GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, maxTextureSize);	
//			Log.d(TAG, "combined " + maxTextureSize.get(0));		
//			
//			gl.glGetIntegerv(GLES20.GL_MAX_VERTEX_ATTRIBS, maxTextureSize);	
//			Log.d(TAG, "atrribs " + maxTextureSize.get(0));

            _gpuvendor = gl.glGetString(GLES20.GL_VENDOR);
            Log.d(TAG, "vendor " + _gpuvendor);

            String version = gl.glGetString(GLES20.GL_VERSION);
            Log.d(TAG, "version " + version);

            _gpumodel = gl.glGetString(GLES20.GL_RENDERER);
            Log.d(TAG, "Model " + _gpumodel);

            sendMessageToMainThread(_gpumodel);
        }
    }
}
