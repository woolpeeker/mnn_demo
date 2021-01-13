#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>
#include "get_result.h"
#include <android/log.h>

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "JNI_LOGGER", __VA_ARGS__)

using namespace cv;

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_mnn_1mnist_MainActivity_mnist(JNIEnv *env, jobject obj, jobject bitmap, jstring jstr) {
    AndroidBitmapInfo info;
    void *pixels;
    char cap[12];
    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
              info.format == ANDROID_BITMAP_FORMAT_RGB_565);
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        Mat temp(info.height, info.width, CV_8UC4, pixels);
        Mat temp2 = temp.clone();
        //将jstring类型转换成C++里的const char*类型
        const char *path = env->GetStringUTFChars(jstr, 0);

        Mat RGB;
        //先将图像格式由BGRA转换成RGB，不然识别结果不对
        cvtColor(temp2, RGB, COLOR_RGBA2RGB);
        //调用之前定义好的mnist()方法，识别文字图像
        LOGD("start inference");
        int result = mnist(RGB, path);
        LOGD("finish inference");
        //将图像转回RGBA格式，Android端才可以显示
        Mat show(info.height, info.width, CV_8UC4, pixels);
        cvtColor(RGB, temp, COLOR_RGB2RGBA);
        //将int类型的识别结果转成jstring类型，并返回
        string re_reco = to_string(result);
        const char* ss = re_reco.c_str();
        LOGD("result is %s", re_reco.c_str());



    } else {
        Mat temp(info.height, info.width, CV_8UC2, pixels);
        return (env)->NewStringUTF("error");
    }
    AndroidBitmap_unlockPixels(env, bitmap);
    return (env)->NewStringUTF(cap);
}
