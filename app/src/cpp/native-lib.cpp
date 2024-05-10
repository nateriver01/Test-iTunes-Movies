#include <jni.h>
#include <string>

using namespace std;
const int ENV_PRODUCTION = 1;
const int ENV_STAGING = 2;

extern "C" {

JNIEXPORT jstring JNICALL
Java_id_hmd_itunesmovies_utils_AppConstants_hostName(
        JNIEnv *env, jobject, jint envtype) {
    std::string hostNm;
    if (envtype == ENV_PRODUCTION) {
        hostNm = "itunes.apple.com";
    } else if (envtype == ENV_STAGING) {
        hostNm = "itunes.apple.com";
    } else {
        hostNm = "itunes.apple.com";
    }
    return env->NewStringUTF(hostNm.c_str());
}

JNIEXPORT jstring JNICALL
Java_id_hmd_itunesmovies_utils_AppConstants_getPreferenceName(JNIEnv *env, jobject thiz, jint environmentStage) {
    int type = (int) environmentStage;
    string preferenceName;
    if (type == ENV_PRODUCTION) {
        preferenceName = "preference_iTunesMovies_production";
    } else if (type == ENV_STAGING) {
        preferenceName = "preference_iTunesMovies_staging";
    } else {
        preferenceName = "ppreference_iTunesMovies";
    }
    return env->NewStringUTF(preferenceName.c_str());
}


JNIEXPORT jstring JNICALL
Java_id_hmd_itunesmovies_utils_AppConstants_dbPassword(
        JNIEnv *env,
        jobject, jint envtype) {
    std::string pin;
    if (envtype == ENV_PRODUCTION) {
        pin = "sha256/+V/2espvUvUrHOqTnQderjpv2FbkVji+vezMpTkezAY=";
    } else if (envtype == ENV_STAGING) {
        pin = "sha1/VREaYDh5DVSApWYwZ4weAm9v5YE=";
    } else {
        pin = "sha256/+V/2espvUvUrHOqTnQderjpv2FbkVji+vezMpTkezAY=";
    }
    return env->NewStringUTF(pin.c_str());
}

JNIEXPORT jstring JNICALL
Java_id_hmd_itunesmovies_utils_AppConstants_appUrl(
        JNIEnv *env, jobject, jint envtype) {
    std::string apUr;
    if (envtype == ENV_PRODUCTION) {
        apUr = "https://itunes.apple.com";
    } else if (envtype == ENV_STAGING) {
        apUr = "https://itunes.apple.com";
    } else {
        apUr = "https://itunes.apple.com";
    }
    return env->NewStringUTF(apUr.c_str());
}

JNIEXPORT jstring JNICALL
Java_id_hmd_itunesmovies_utils_AppConstants_getSharedPreferencePassword(JNIEnv *env, jobject thiz, jint environmentStage) {
    int type = (int) environmentStage;
    string value;
    //random character
    if (type == ENV_PRODUCTION) {
        value = "]Wvx2wf9&+r^dW/2`~yx[]z9<p}C-Lxe";
    } else if (type == ENV_STAGING) {
        value = "]Wvx2wf9&+r^dW/2`~yx[]z9<p}C-Lxe";
    } else {
        value = "]Wvx2wf9&+r^dW/2`~yx[]z9<p}C-Lxe";
    }
    return env->NewStringUTF(value.c_str());
}

JNIEXPORT jstring JNICALL
Java_id_hmd_itunesmovies_utils_AppConstants_getDbName(JNIEnv *env, jobject thiz, jint environmentStage) {
    int type = (int) environmentStage;
    string dbName;
    if (type == ENV_PRODUCTION) {
        dbName = "itunesmovies_db";
    } else if (type == ENV_STAGING) {
        dbName = "itunesmovies_staging";
    } else {
        dbName = "itunesmovies_db";
    }
    return env->NewStringUTF(dbName.c_str());
}

}