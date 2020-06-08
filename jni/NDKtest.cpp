#include<jni.h>
#include<string.h>

extern "C" jstring Java_com_example_task1_MainActivity_testString(JNIEnv *env, jobject) {
   return(env)->NewStringUTF("JNI test");
}