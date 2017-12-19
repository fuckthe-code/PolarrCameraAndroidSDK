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

#ifndef POLARR_RENDER_H
#define POLARR_RENDER_H 1

#include "renderState.h"

enum INPUT_YUV_TYPE {
    INPUT_YUV_TYPE_NV21
};

enum POLARR_FILTER {
    MODE1,
    MODE2,
    MODE3,
    MODE4,
    DEFAULT
};

class RenderInternal;

class PolarrRender {
public:
    PolarrRender();

    virtual ~PolarrRender();

    void init(int renderWidth, int renderHeight, bool needEgl);

    void setInput(unsigned int texId, int width, int height);

    void setOutput(unsigned int texId);

    void drawFrame();

    void updateSize(int width, int height);

    void renderScreen(unsigned int texId);

    void updateStates(RenderState *state);

    void setInputYUV(INPUT_YUV_TYPE yuvType,
                     unsigned int width, unsigned int height,
                     unsigned int stride, unsigned int scanline,
                     unsigned char *yuvBytes);

    unsigned char *getOutputYUV(INPUT_YUV_TYPE yuvType, int *len);

    RenderState *getFilter(POLARR_FILTER filterType);

    unsigned char *getOutputRgb(int *len);

private:
    RenderInternal *params;
};

#endif // POLARR_RENDER_H
