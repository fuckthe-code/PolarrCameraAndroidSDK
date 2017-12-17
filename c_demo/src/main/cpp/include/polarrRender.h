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

struct RenderState {
    float sharpen = 0;
    float dehaze = 0;
    float gamma = 0;
    float whites = 0;
    float blacks = 0;
    float saturation = 0;
    float temperature = 0;
    float contrast = 0;
    float *curves_red;//[] = {0, 0, 255, 255};
    float *curves_green;//[] = {0, 0, 255, 255};
    float *curves_blue;//[] = {0, 0, 255, 255};
    float *curves_all;//[] = {0, 0, 255, 255};
    unsigned int curves_red_size = 4;
    unsigned int curves_green_size = 4;
    unsigned int curves_blue_size = 4;
    unsigned int curves_all_size = 4;
    float hue_red = 0;
    float hue_orange = 0;
    float hue_yellow = 0;
    float hue_green = 0;
    float hue_aqua = 0;
    float hue_blue = 0;
    float hue_purple = 0;
    float hue_magenta = 0;
    float saturation_red = 0;
    float saturation_orange = 0;
    float saturation_yellow = 0;
    float saturation_green = 0;
    float saturation_aqua = 0;
    float saturation_blue = 0;
    float saturation_purple = 0;
    float saturation_magenta = 0;
    float luminance_red = 0;
    float luminance_orange = 0;
    float luminance_yellow = 0;
    float luminance_green = 0;
    float luminance_aqua = 0;
    float luminance_blue = 0;
    float luminance_purple = 0;
    float luminance_magenta = 0;
    float vignette_amount = 0;
    float vignette_feather = 0.5;
    float vignette_highlights = 0.5;
    float vignette_roundness = 0;
    float vignette_size = 1;

    float grain_amount;
    float grain_size;
    float grain_highlights;
    float grain_roughness;

public:
    RenderState();
    virtual ~RenderState();
};

class PolarrRender {
public:
    PolarrRender();

    virtual ~PolarrRender();

    void init(int renderWidth, int renderHeight);

    void setInput(unsigned int texId, int width, int height);

    void setOutput(unsigned int texId);

    void drawFrame();

    void updateSize(int width, int height);

    void renderScreen(unsigned int texId);

    void updateStates(RenderState *state);

    void setInputYUV(INPUT_YUV_TYPE yuvType, unsigned int width, unsigned int height, unsigned char *yuvBytes);

    unsigned char *getOutputYUV(INPUT_YUV_TYPE yuvType, int *len);

    RenderState* getFilter(POLARR_FILTER filterType);
};

#endif // POLARR_RENDER_H
