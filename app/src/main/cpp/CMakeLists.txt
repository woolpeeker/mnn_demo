# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("mnn_mnist")


# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )


# ======================== OpenCV =============================
set( OpenCV_DIR /home/luojiapeng/Libs/OpenCV-android-sdk/sdk/native/jni )
find_package(OpenCV REQUIRED)
if(OpenCV_FOUND)
    include_directories(${OpenCV_INCLUDE_DIRS})
    message(STATUS "OpenCV library status:")
    message(STATUS "    version: ${OpenCV_VERSION}")
    message(STATUS "    libraries: ${OpenCV_LIBS}")
    message(STATUS "    include path: ${OpenCV_INCLUDE_DIRS}")
else(OpenCV_FOUND)
    message(FATAL_ERROR "OpenCV library not found")
endif(OpenCV_FOUND)
# ======================== OpenCV  END =============================

# ======================== MNN =====================================
set(MNN_DIR /home/luojiapeng/Libs/MNN-1.1.2/)

include_directories(${MNN_DIR}/include)
include_directories(${MNN_DIR}/include/MNN)
include_directories(${MNN_DIR}/tools)
include_directories(${MNN_DIR}/tools/cpp)
include_directories(${MNN_DIR}/source)
include_directories(${MNN_DIR}/source/backend)
include_directories(${MNN_DIR}/source/core)
include_directories(get_result.h)
aux_source_directory(. SRCS)
add_library(MNN SHARED IMPORTED)
set(dis_DIR ../../../../libs)
set_target_properties(
        MNN
        PROPERTIES IMPORTED_LOCATION
        ${dis_DIR}/libMNN.so
)
# ======================== MNN =====================================

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        ${SRCS}
        )


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt thhhird-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        native-lib
        MNN

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        ${OpenCV_LIBS}
        jnigraphics
        )
