# 泼辣滤镜Camera SDK Java版版本
## 版权限制
包含本SDK在内的所有版本库中的内容，属于Polarr, Inc.版权所有。未经允许均不得用于商业目的。当前版本的示例SDK失效时间为2018年1月31日。如需要获取完整授权等更多相关信息，请联系我们[info@polarr.co](mailto:info@polarr.co)

## 增加 dependencies 到 Gradle文件
```groovy
// render sdk
compile (name: 'renderer-camera-release', ext: 'aar')
```
## 在GL线程中初始化 PolarrRender
```java
PolarrRender polarrRender = new PolarrRender();
@Override
public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    // call in gl thread
    boolean fastMode = true; // true 为Camera应用优化
    polarrRender.initRender(getResources(), getWidth(), getHeight(), fastMode);
}
```
## 创建或传入Texture
### 创建Texture
```java
// 只需要调用一次
polarrRender.createInputTexture();
// bind a bitmap to sdk
int inputTexture = polarrRender.getTextureId();
GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTexture);
GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);

// 输入Texture变化后需要调用
polarrRender.updateInputTexture();
```
### 传入一个输入Texture
```java
//  默认为GL_TEXTURE_2D格式
polarrRender.setInputTexture(inputTexture);
// 输入Texture变化后需要调用
polarrRender.updateInputTexture();
```
### 传入不同类型的输入Texture
```java
polarrRender.setInputTexture(inputTexture, textureType); // PolarrRender.TEXTURE_2D, PolarrRender.EXTERNAL_OES
// 输入Texture变化后需要调用
polarrRender.updateInputTexture();
```
## 设置输出Texture (非必须)
如果不设置输出Texture，SDK将会创建一个输出Texture。通过[获取输出的Texture](#获取输出的Texture)获取
```java
//  必须为GL_TEXTURE_2D格式
polarrRender.setOutputTexture(outputTexture);
```
## 更新渲染尺寸。更新后需要更新输入Texture
```java
// call in gl thread
polarrRender.updateSize(width, height);
```
## 渲染
```java
@Override
public void onDrawFrame(GL10 gl) {
    // call in GL thread
    polarrRender.drawFrame();
}
```
## 自动增强
### 全局自动增强
返回值为全局自动增强后需要改变的调整值
```java
// call in gl thread
float percent; // 增强的百分比 (0,1)
Map<String, Object> changedStates = polarrRender.autoEnhanceGlobal(percent);
```
### 面部自动增强
进行面部自动增强前需要先[进行人脸识别](##人脸识别)
```java
//包含人脸识别后人脸信息的数据。这里可以是只包含人脸信息的数据，也可以是全部调整数据。
Map<String, Object> faceStates;
// 需要自动识别人脸的索引
int faceIndex = 0;
// 进行面部自动增强，并将增强后的参数设置给传入的map call in gl thread
polarrRender.autoEnhanceFace(faceStates, faceIndex);
// 更新渲染数据 call in gl thread
polarrRender.updateStates(faceStates);
```
## 面部调整
调整面部属性及渲染参数，需要先[进行人脸识别](##人脸识别)
### 面部渲染参数调整
```java
// 识别人脸并返回渲染所需的人脸参数
Map<String, Object> faceStates;
// 获取面部参数信息
List<FaceItem> faces = (List<FaceItem>) faceStates.get("faces");
FaceItem faceItem = faces.get(index);
FaceState faceAdjustments = faceItem.adjustments;
 
faceAdjustments.skin_smoothness = 0; // 皮肤平滑 (-1f,+1f)
faceAdjustments.skin_tone = 0; // 皮肤光泽 (-1f,+1f)
faceAdjustments.skin_hue = 0; // 皮肤色相 (-1f,+1f)
faceAdjustments.skin_saturation = 0;  // 皮肤饱和度 (-1f,+1f)
faceAdjustments.skin_shadows = 0; // 皮肤阴影 (-1f,+1f)
faceAdjustments.skin_highlights = 0; // 皮肤高光 (-1f,+1f)
faceAdjustments.teeth_whitening = 0; // 牙齿美白 (0f,+1f)
faceAdjustments.teeth_brightness = 0; // 牙齿亮度 (0f,+1f)
faceAdjustments.eyes_brightness = 0; // 眼睛亮度 (0f,+1f)
faceAdjustments.eyes_contrast = 0; // 眼睛对比度 (0f,+1f)
faceAdjustments.eyes_clarity = 0; // 眼睛清晰度 (0f,+1f)
faceAdjustments.lips_brightness = 0; // 嘴唇亮度 (0f,+1f)
faceAdjustments.lips_saturation = 0; // 嘴唇饱和度 (-1f,+1f)
```
### 面部尺寸属性调整
```java
// 识别人脸并返回渲染所需的人脸参数
Map<String, Object> faceStates;
// 获取面部参数信息
List<FaceFeaturesState> faceFeaturesStates = (List<FaceFeaturesState>) faceStates.get("face_features");
FaceFeaturesState featureSate = faceFeaturesStates.get(index);
 
featureSate.eye_size = {0, 0};  // 眼睛大小 {(-1f,+1f),(-1f,+1f)}
featureSate.face_width = 0; // 脸宽 (-1f,+1f)
featureSate.forehead_height = 0; // 前额高度 (-1f,+1f)
featureSate.chin_height = 0; // 下巴高度 (-1f,+1f)
featureSate.nose_width = 0; // 鼻子宽度 (-1f,+1f)
featureSate.nose_height = 0; // 鼻子长度 (-1f,+1f)
featureSate.mouth_width = 0; // 嘴宽度 (-1f,+1f)
featureSate.mouth_height = 0; // 嘴高度 (-1f,+1f)
featureSate.smile = 0; // 微笑程度 (-1f,+1f)
```
## 重置图片
重置图片为原始状态
```java
stateMap.clear();
// 如果需要重置人脸信息
FaceUtil.ResetFaceStates(faceStates);

// call in gl thread
polarrRender.updateStates(stateMap);
```
## 获取输出的Texture
```java
int out = polarrRender.getOutputId();
```
## 释放资源
```java
// call in GL thread
polarrRender.release();
```
## 滤镜工具
SDK 内置了泼辣修图的滤镜包，滤镜包数据内置于renderer module中。
### 获取滤镜列表
```java
// 获取滤镜包
List<FilterPackage> packages = FilterPackageUtil.GetAllFilters(getResources());
// 获取滤镜
FilterItem filterItem = filterPackage.filters.get(0);
```
### 设置滤镜参数
```java
renderView.updateStates(filterItem.state);
```
### 调整滤镜程度
返回滤镜的参考调整范围。程度为50%时表示滤镜的原始数值，增加表示增强各个参数的程度，减少表示减弱各个参数的程度。
```java
float adjustmentValue = 0.5f; // 滤镜程度 (0f, 1f)
Map<String, Object> interpolateStates = FilterPackageUtil.GetRefStates(filterItem.state, adjustmentValue);
```
## 基本全局调整属性

| 属性 | 取值范围 | 描述 |
|-----|:-------:|-----:|
| exposure | -1, +1 | [曝光](http://polaxiong.com/wiki/hou-qi-shu-yu/pu-guang.html) |
| gamma | -1, +1 | [亮度](http://polaxiong.com/wiki/hou-qi-shu-yu/liang-du.html) |
| contrast | -1, +1 | [对比度](http://polaxiong.com/wiki/hou-qi-shu-yu/dui-bi-du.html)|
| saturation | -1, +1 | [饱和度](http://polaxiong.com/wiki/hou-qi-shu-yu/bao-he-du.html)|
| vibrance | -1, +1 | [自然饱和度](http://polaxiong.com/wiki/hou-qi-shu-yu/zi-ran-bao-he-du.html)|
| distortion_horizontal | -1, +1 | [水平透视](http://polaxiong.com/wiki/hou-qi-shu-yu/shui-ping-tou-shi.html)|
| distortion_vertical | -1, +1 | [垂直透视](http://polaxiong.com/wiki/hou-qi-shu-yu/chui-zhi-tou-shi.html)|
| distortion_amount | -1, +1 | [镜头扭曲](http://polaxiong.com/wiki/hou-qi-shu-yu/jing-tou-niu-qu.html)|
| fringing | -1, +1 | [色差](http://polaxiong.com/wiki/hou-qi-shu-yu/se-cha.html)|
| color_denoise | 0, +1 | [降噪色彩](http://polaxiong.com/wiki/hou-qi-shu-yu/jiang-zao-se-cai.html)|
| luminance_denoise | 0, +1 | [降噪明度](http://polaxiong.com/wiki/hou-qi-shu-yu/jiang-zao-ming-du.html)|
| dehaze | -1, +1 | [去雾](http://polaxiong.com/wiki/hou-qi-shu-yu/qu-wu.html)|
| diffuse | 0, +1 | [眩光](http://polaxiong.com/wiki/hou-qi-shu-yu/xuan-guang.html)|
| temperature | -1, +1 | [色温](http://polaxiong.com/wiki/hou-qi-shu-yu/se-wen.html)|
| tint | -1, +1 | [色调](http://polaxiong.com/wiki/hou-qi-shu-yu/se-tiao.html)|
| highlights | -1, +1 | [高光](http://polaxiong.com/wiki/hou-qi-shu-yu/gao-guang.html)|
| shadows | -1, +1 | [阴影](http://polaxiong.com/wiki/hou-qi-shu-yu/yin-ying.html)|
| whites | -1, +1 | [白色色阶](http://polaxiong.com/wiki/hou-qi-shu-yu/bai-se-se-jie.html)|
| blacks | -1, +1 | [黑色色阶](http://polaxiong.com/wiki/hou-qi-shu-yu/hei-se-se-jie.html)|
| clarity | -1, +1 | [清晰度](http://polaxiong.com/wiki/hou-qi-shu-yu/qing-xi-du.html)|
| sharpen | 0, +1 | [锐化](http://polaxiong.com/wiki/hou-qi-shu-yu/rui-hua.html)
| highlights_hue | 0, +1 | [色调高光色相](http://polaxiong.com/wiki/hou-qi-shu-yu/se-tiao-gao-guang.html)|
| highlights_saturation | 0, +1 | [色调高光饱和度](http://polaxiong.com/wiki/hou-qi-shu-yu/se-tiao-gao-guang.html)|
| shadows_hue | 0, +1 | [色调阴影色相](http://polaxiong.com/wiki/hou-qi-shu-yu/se-tiao-yin-ying.html)|
| shadows_saturation | 0, +1 | [色调阴影饱和度](http://polaxiong.com/wiki/hou-qi-shu-yu/se-tiao-yin-ying.html)|
| balance | -1, +1 | [色调平衡](http://polaxiong.com/wiki/hou-qi-shu-yu/se-tiao-ping-heng.html)|
|  |  |  |
| hue_red | -1, +1 | [HSL色相红色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| hue_orange | -1, +1 | [HSL色相橘色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| hue_yellow | -1, +1 | [HSL色相黄色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| hue_green | -1, +1 | [HSL色相绿色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| hue_aqua | -1, +1 | [HSL色相青色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| hue_blue | -1, +1 | [HSL色相蓝色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| hue_purple | -1, +1 | [HSL色相紫色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| hue_magenta | -1, +1 | [HSL色相品红](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)
|  |  |  |
| saturation_red | -1, +1 | [HSL饱和度红色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| saturation_orange | -1, +1 | [HSL饱和度橘色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| saturation_yellow | -1, +1 | [HSL饱和度黄色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| saturation_green | -1, +1 | [HSL饱和度绿色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| saturation_aqua | -1, +1 | [HSL饱和度青色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| saturation_blue | -1, +1 | [HSL饱和度蓝色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| saturation_purple | -1, +1 | [HSL饱和度紫色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| saturation_magenta | -1, +1 | [HSL饱和度品红](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
|  |  |  |
| luminance_red | -1, +1 | [HSL明度红色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| luminance_orange | -1, +1 | [HSL明度橘色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| luminance_yellow | -1, +1 | [HSL明度黄色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| luminance_green | -1, +1 | [HSL明度绿色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| luminance_aqua | -1, +1 | [HSL明度青色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| luminance_blue | -1, +1 | [HSL明度蓝色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| luminance_purple | -1, +1 | [HSL明度紫色](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
| luminance_magenta | -1, +1 | [HSL明度品红](http://polaxiong.com/wiki/hou-qi-shu-yu/hsl.html)|
|  |  |  |
| grain_amount | 0, +1 | [噪点程度](http://polaxiong.com/wiki/hou-qi-shu-yu/zao-dian-cheng-du.html)|
| grain_size | 0, +1 | [噪点大小](http://polaxiong.com/wiki/hou-qi-shu-yu/zao-dian-da-xiao.html)|
|  |  |
| mosaic_size | 0, +1 | [像素化](http://polaxiong.com/wiki/hou-qi-shu-yu/xiang-su-hua.html)
| mosaic_pattern | "square","hexagon","dot","triangle","diamond" | [像素化](http://polaxiong.com/wiki/hou-qi-shu-yu/xiang-su-hua.html)
## 人脸识别
### 使用第三方人脸识别库进行人脸识别
```java
// 人脸数据，支持多张人脸，每张人脸数据的点个数必须为106个
List<List<PointF>> facePoints = new ArrayList<>();
// 进行识别时的输入尺寸
int detectWidth = 720;
int detectHeight = 960;
  
Map<String, Object> faces = FaceUtil.GetFaceFeaturesWithPoints(facePoints, detectWidth, detectHeight);
  
renderStates.putAll(faces);
```
### 重置人脸信息
```java
// 不需要初始化识别工具
FaceUtil.ResetFaceStates(faceStates);
```