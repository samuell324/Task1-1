LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := NDKtest
LOCAL_SRC_FILES := NDKtest.cpp
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)