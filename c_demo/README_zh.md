# 泼辣滤镜Camera SDK C++版本
## 版权限制
包含本SDK在内的所有版本库中的内容，属于Polarr, Inc.版权所有。未经允许均不得用于商业目的。如需要获取完整授权等更多相关信息，请联系我们[info@polarr.co](mailto:info@polarr.co)

## 增加头文件
```objectivec
#include "polarrRender.h"
```
## 初始化 PolarrRender
如果在非OpenGL环境下调用，请将needEgl参数设置为true。
```objectivec
polarrRender = new PolarrRender;
bool needEgl;
polarrRender->init(width, height, needEgl);
```
## 设置Texture
### 设置输入Texture
```objectivec
//  必须为GL_TEXTURE_2D格式
polarrRender->setInput(inputTexId, width, height);
```
### 设置输出Texture
```objectivec
//  必须为GL_TEXTURE_2D格式
polarrRender->setOutput(outputTexId);
```
## 更新渲染尺寸
```objectivec
polarrRender.updateSize(width, height);
```
## 滤镜
### 滤镜列表
参考头文件中的内置滤镜
```objectivec
enum POLARR_FILTER {
    MODE1,
    MODE2,
    MODE3,
    MODE4,
    DEFAULT
};
```
### 获取滤镜数据
```objectivec
RenderState *state = polarrRender->getFilter(MODE1);
```
## 设置滤镜参数
```objectivec
polarrRender->updateStates(state);
```
## 渲染
```objectivec
polarrRender->drawFrame();
```
## 渲染到屏幕
```objectivec
polarrRender->renderScreen(outputTexId);
```
## YUV支持
### 输入YUV数据
设置YUV数据前，需要先 [更新渲染尺寸](#更新渲染尺寸)为输入数据的真实尺寸。
```objectivec
// 目前只支持NV21格式
polarrRender->setInputYUV(INPUT_YUV_TYPE_NV21, width, height, stride, scanline, *yuvBytes);
```
### 渲染YUV数据
调用[渲染](#渲染)接口

### 输出YUV数据
如果设置了输入YUV的stride和scanline，那么输出的YUV数据以相同的stride和scanline输出。
如果在输入YUV数据后调用了updateSize方法，那么输出的YUV数据中实际图片大小为updateSize的宽高。数据大小仍为stride*scanline。
```objectivec
// 目前只支持NV21格式
int len;
unsigned char *yuvBytes = polarrRender->getOutputYUV(INPUT_YUV_TYPE_NV21, &len);
```
## 输出RGBA数据
输出数据格式为RGBA_8888
```objectivec
int len;
unsigned char *rgbaBytes = polarrRender->getOutputRgb(&len);
```
## 释放资源
```objectivec
delete polarrRender;
```