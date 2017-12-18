//
// Created by Colin PRO on 2017/12/18.
//

#ifndef POLARR_PHOTO_EDITOR_ANDROID_RENDERSTATE_H
#define POLARR_PHOTO_EDITOR_ANDROID_RENDERSTATE_H

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
#endif //POLARR_PHOTO_EDITOR_ANDROID_RENDERSTATE_H
