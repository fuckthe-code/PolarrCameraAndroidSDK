//
// Created by Colin PRO on 2017/12/12.
//

#include <jni.h>
#include <sys/types.h>
#include <stdlib.h>
#include <time.h>
#include "polarrRender.h"

PolarrRender *polarrRender;

unsigned int inputTexId = 0;
unsigned int outputTexId = 0;


unsigned char *as_unsigned_char_array(JNIEnv *env, jbyteArray array) {
    int len = env->GetArrayLength(array);
    unsigned char *buf = new unsigned char[len];
    env->GetByteArrayRegion(array, 0, len, reinterpret_cast<jbyte *>(buf));

    return buf;
}

jbyteArray as_byte_array(JNIEnv *env, unsigned char *buf, int len) {
    jbyteArray array = env->NewByteArray(len);
    env->SetByteArrayRegion(array, 0, len, reinterpret_cast<jbyte *>(buf));
    return array;
}

extern "C"
JNIEXPORT void JNICALL
Java_co_polarr_render_PolarrRenderJni_init(JNIEnv *env, jclass type, jint texId, jint width,
                                           jint height, jint outTexId, jbyteArray yuvArr) {
    polarrRender = new PolarrRender;
//    printGlString("Version", GL_VERSION);
//    printGlString("Vendor", GL_VENDOR);
//    printGlString("ShaderRender", GL_RENDERER);
//    printGlString("Extensions", GL_EXTENSIONS);
    polarrRender->init(width, height);
//    if (outTexId <= 0) {
//        return;
//    }

    inputTexId = texId;
    outputTexId = outTexId;

    polarrRender->setInput(inputTexId, width, height);
    polarrRender->setOutput(outputTexId);

    unsigned char *yuvBytes = as_unsigned_char_array(env, yuvArr);
    polarrRender->setInputYUV(INPUT_YUV_TYPE_NV21, (unsigned int) width, (unsigned int) height,
                              yuvBytes);
    delete yuvBytes;

}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_co_polarr_render_PolarrRenderJni_getYUVData(JNIEnv *env, jclass type) {
    int len;
    unsigned char *yuvBytes = polarrRender->getOutputYUV(INPUT_YUV_TYPE_NV21, &len);

    return as_byte_array(env, yuvBytes, len);
}

extern "C"
JNIEXPORT void JNICALL
Java_co_polarr_render_PolarrRenderJni_renderTest(JNIEnv *env, jclass type) {
    srand((unsigned int) time(0));
    polarrRender->updateStates(polarrRender->getFilter(DEFAULT));
    float r = rand() / (RAND_MAX + 1.0f) * 4;
    if (r < 1) {
        polarrRender->updateStates(polarrRender->getFilter(MODE1));
    } else if (r < 2) {
        polarrRender->updateStates(polarrRender->getFilter(MODE2));
    } else if (r < 3) {
        polarrRender->updateStates(polarrRender->getFilter(MODE3));
    } else {
        polarrRender->updateStates(polarrRender->getFilter(MODE4));
    }
    polarrRender->drawFrame();
    polarrRender->renderScreen(outputTexId);
}

extern "C"
JNIEXPORT void JNICALL
Java_co_polarr_render_PolarrRenderJni_resize(JNIEnv *env, jclass type, jint width, jint height) {
    polarrRender->updateSize(width, height);
}

extern "C"
JNIEXPORT void JNICALL
Java_co_polarr_render_PolarrRenderJni_release(JNIEnv *env, jclass type) {
    delete polarrRender;
}

extern "C"
JNIEXPORT void JNICALL
Java_co_polarr_render_PolarrRenderJni_setYUVData(JNIEnv *env, jclass type, jint width, jint height,
                                                 jbyteArray yuvArr) {
    unsigned char *yuvBytes = as_unsigned_char_array(env, yuvArr);
    polarrRender->setInputYUV(INPUT_YUV_TYPE_NV21, (unsigned int) width, (unsigned int) height,
                              yuvBytes);
    delete yuvBytes;
}
